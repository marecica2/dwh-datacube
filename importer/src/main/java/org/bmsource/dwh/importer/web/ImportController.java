package org.bmsource.dwh.importer.web;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bmsource.dwh.common.appstate.AppStateService;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.FileSystemImpl;
import org.bmsource.dwh.common.reader.*;
import org.bmsource.dwh.importer.Fact;
import org.bmsource.dwh.importer.ImporterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class MappingRequestBody {

    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}

class PreviewRequestBody {

    private Map<String, String> mapping;

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}

class UploadRequestBody {

    private Map<String, Object> config;

    private Map<String, String> mapping;

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}

@RestController()
@RequestMapping("/import")
public class ImportController {

    @Autowired
    AppStateService appStateService;

    private FileManager fileManager = new FileSystemImpl();

    @GetMapping
    public String initUpload() {
        return fileManager.createTransaction();
    }

    @PostMapping("/{transactionId}")
    public List<String> fileUpload(HttpServletRequest request, @PathVariable("transactionId") String transactionId) throws IOException, FileUploadException {

        List<String> files = new ArrayList<>();
        if (ServletFileUpload.isMultipartContent(request)) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator fileItemIterator = upload.getItemIterator(request);

            while (fileItemIterator.hasNext()) {
                FileItemStream fis = fileItemIterator.next();
                String name = fis.getName();
                try (InputStream stream = fis.openStream()) {
                    if (!fis.isFormField()) {
                        fileManager.addFile(transactionId, name, stream);
                        files.add(name);
                    }
                }
            }
        }
        return files;
    }

    @PostMapping(value = "/{transactionId}/mapping", consumes = "application/json")
    public MappingResponse columnMapping(@PathVariable("transactionId") String transactionId,
                                         @RequestBody MappingRequestBody filesParam) throws Exception {

        List<String> files = fileManager.getFiles(transactionId);
        try (InputStream stream = fileManager.getStream(transactionId, files.get(0))) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream);
            return MappingResponse.builder().setSourceFields(columnMapping.getHeaderRow(),
                columnMapping.getPreviewRow()).setFactModel(Fact.class).autoSuggestionMapping().build();
        }
    }

    @PostMapping(value = "/{transactionId}/preview", consumes = "application/json")
    public List<Fact> preview(@PathVariable("transactionId") String transactionId,
                              @RequestBody PreviewRequestBody mappingParam) throws Exception {

        List<String> files = fileManager.getFiles(transactionId);
        try (InputStream stream1 = fileManager.getStream(transactionId, files.get(0)); InputStream stream2 =
            fileManager.getStream(transactionId, files.get(0));) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream1);
            return new FactModelMapper<>(Fact.class, columnMapping.getHeaderRow(), mappingParam.getMapping()).mapList(reader.readContent(stream2, 100));
        }
    }

    @Async("asyncExecutor")
    @PostMapping(value = "/{transactionId}/start", consumes = "application/json")
    public void start(@PathVariable("transactionId") String transactionId,
                      @RequestBody UploadRequestBody uploadRequestBody) throws Exception {
        String tenant = "0000-0000-0000-0001";
        String project = "1";

        List<String> files = fileManager.getFiles(transactionId);
        for (String file : files) {
            try (InputStream stream = fileManager.getStream(transactionId, file);) {
                DataReader reader = new ExcelReader();
                reader.readContent(stream, new DataHandler() {
                    @Override
                    public void onStart() {
//                        Map<String, Object> state = appStateService.getState(tenant, project,
//                            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue());
//                        if(state.get("running") != null && (Boolean) state.get("running")) {
//                            throw new RuntimeException("Import already running");
//                        }
                    }

                    @Override
                    public void onRead(List<List<Object>> rows, List<Object> header, int rowsCount,
                                       int totalRowsCount) {
                        long before = System.currentTimeMillis();
                        List<Fact> facts = new FactModelMapper<Fact>(Fact.class, header,
                            uploadRequestBody.getMapping()).mapList(rows);
                        System.out.println("Parsed from file " + file + " " + rowsCount + " of total " + totalRowsCount);
                        Map<String, Object> state = new HashMap<>();
                        state.put("running", true);
                        state.put("file", files.indexOf(file));
                        state.put("files", files.size());
                        state.put("fileName", file);
                        state.put("rowsCount", rowsCount);
                        state.put("totalRowsCount", totalRowsCount);

                        appStateService.updateState(tenant, project,
                            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue(), state);
                        System.out.println((System.currentTimeMillis() - before) + " ms");
                    }

                    @Override
                    public void onFinish(int totalRowsCount) {
                        Map<String, Object> state = new HashMap<>();
                        state.put("running", false);
                        appStateService.updateState(tenant, project,
                            ImporterConfiguration.StateType.IMPORT_STATUS_STATE.getValue(), state);
                        // Trigger post process
                    }
                });
            }
        }
    }
}
