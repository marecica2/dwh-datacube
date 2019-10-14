package org.bmsource.dwh.importer.web;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.FileSystemImpl;
import org.bmsource.dwh.common.reader.*;
import org.bmsource.dwh.importer.Fact;
import org.bmsource.dwh.importer.SimpleImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("/import")
public class ImportController {

    private FileManager fileManager = new FileSystemImpl();

    @Autowired
    SimpleImportService importService;

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

    @ResponseBody
    @PostMapping(value = "/{transactionId}/start", consumes = "application/json")
    public ResponseEntity start(@PathVariable("transactionId") String transactionId,
                                @RequestBody UploadRequestBody uploadRequestBody) throws Exception {

        String tenant = "0000-0000-0000-0001";
        String project = "1";
        if (importService.checkRunningImport(tenant, project)) {
            return new ResponseEntity(HttpStatus.TOO_MANY_REQUESTS);
        }
        importService.startImport(transactionId, uploadRequestBody.getMapping(), tenant, project);
        return new ResponseEntity(HttpStatus.OK);
    }
}
