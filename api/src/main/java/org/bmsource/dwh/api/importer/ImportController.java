package org.bmsource.dwh.api.importer;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bmsource.dwh.api.fileManager.FileManager;
import org.bmsource.dwh.api.fileManager.FileSystemImpl;
import org.bmsource.dwh.api.model.Fact;
import org.bmsource.dwh.api.model.FactModelMapper;
import org.bmsource.dwh.api.reader.DataReader;
import org.bmsource.dwh.api.reader.ExcelReader;
import org.bmsource.dwh.api.reader.MappingResult;
import org.bmsource.dwh.api.sse.AppStatus;
import org.bmsource.dwh.api.sse.ImportStatus;
import org.bmsource.dwh.api.sse.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class MappingRequestBody {

    List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}

class PreviewRequestBody {

    Map<String, String> mapping;

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }
}

class UploadRequestBody {

    Map<String, Object> config;

    Map<String, String> mapping;

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
    NotificationService notificationService;

    private FileManager fileManager = new FileSystemImpl();

    @GetMapping
    public String init() {
        return fileManager.createTransaction();
    }

    @PostMapping("/{transactionId}")
    public List<String> handleUpload(HttpServletRequest request, @PathVariable("transactionId") String transactionId)
        throws IOException, FileUploadException {
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
                                         @RequestBody MappingRequestBody filesParam
    ) throws Exception {
        List<String> files = fileManager.getFiles(transactionId);

        try (InputStream stream = fileManager.getStream(transactionId, files.get(0))) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream);
            return MappingResponse
                .builder()
                .setSourceFields(columnMapping.getHeaderRow(), columnMapping.getPreviewRow())
                .setFactModel(Fact.class)
                .autoSuggestionMapping()
                .build();
        }
    }

    @PostMapping(value = "/{transactionId}/preview", consumes = "application/json")
    public List<Fact> preview(@PathVariable("transactionId") String transactionId, @RequestBody PreviewRequestBody mappingParam) throws Exception {
        List<String> files = fileManager.getFiles(transactionId);
        try (
            InputStream stream1 = fileManager.getStream(transactionId, files.get(0));
            InputStream stream2 = fileManager.getStream(transactionId, files.get(0));
        ) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream1);
            return new FactModelMapper(columnMapping.getHeaderRow(), mappingParam.getMapping())
                .mapList(reader.readContent(stream2, 100));
        }
    }

    @Async("asyncExecutor")
    @PostMapping(value = "/{transactionId}/start", consumes = "application/json")
    public void start(@PathVariable("transactionId") String transactionId, @RequestBody UploadRequestBody uploadRequestBody) throws Exception {
        List<String> files = fileManager.getFiles(transactionId);
        for (String file : files) {
            try (
                InputStream stream = fileManager.getStream(transactionId, file);
            ) {
                DataReader reader = new ExcelReader();
                reader
                    .readContent(stream, (items, header, rowsCount, totalRowsCount) -> {
                        List<Fact> facts = new FactModelMapper(header, uploadRequestBody.getMapping()).mapList(items);
                        System.out.println("Parsed from file " + file + " " + rowsCount + " of total " + totalRowsCount);
                        AppStatus status = new AppStatus(new ImportStatus(true, file.length(), files.indexOf(file), file, rowsCount, totalRowsCount));
                        notificationService.sendSseEvent(status);
                    }, () -> {
                        AppStatus status = new AppStatus(new ImportStatus(false));
                        notificationService.sendSseEvent(status);
                    });
            }
        }
    }

    @GetMapping("/{transactionId}/status")
    public SseEmitter streamEvents(@PathVariable("transactionId") String transactionId) {
        return notificationService.initSseEmitters();
    }
}
