package org.bmsource.dwh.importer.web;

import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.TmpFileManager;
import org.bmsource.dwh.common.importer.ImportService;
import org.bmsource.dwh.common.io.DataRow;
import org.bmsource.dwh.common.io.reader.*;
import org.bmsource.dwh.importer.Fact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@RestController()
@RequestMapping("{projectId}/import")
public class ImportController {

    private FileManager fileManager = new TmpFileManager();

    @Autowired
    ImportService importService;

    @GetMapping
    public String initUpload() {
        return fileManager.createTransaction();
    }

    @PostMapping("/{transactionId}")
    public List<String> fileUpload(MultipartHttpServletRequest request,
                                   @PathVariable("projectId") String projectId,
                                   @PathVariable("transactionId") String transactionId) throws IOException {
        List<String> files = new ArrayList<>();

        MultipartFile multipartFile = request.getFile("file");
        String name = multipartFile.getOriginalFilename();
        try (InputStream inputStream = multipartFile.getInputStream();) {
            fileManager.addFile(transactionId, name, inputStream);
            files.add(name);
        }
        return files;
    }

    @PostMapping(value = "/{transactionId}/mapping", consumes = "application/json")
    public MappingResponse columnMapping(@PathVariable("projectId") String projectId,
                                         @PathVariable("transactionId") String transactionId,
                                         @RequestBody MappingRequestBody filesParam) throws Exception {
        List<String> files = fileManager.getFiles(transactionId);
        try (InputStream stream = fileManager.getStream(transactionId, files.get(0))) {
            MappingResult columnMapping = getColumnMapping(stream);
            return MappingResponse.builder().setSourceFields(columnMapping.getHeaderRow(),
                columnMapping.getPreviewRow()).setFactModel(Fact.class).autoSuggestionMapping().build();
        }
    }

    @PostMapping(value = "/{transactionId}/preview", consumes = "application/json")
    public List<PreviewResponse> preview(@PathVariable("projectId") String projectId,
                                         @PathVariable("transactionId") String transactionId,
                                         @RequestBody PreviewRequestBody mappingParam) throws Exception {

        List<String> files = fileManager.getFiles(transactionId);
        List<PreviewResponse> rows = new ArrayList<>();
        try (
            InputStream stream1 = fileManager.getStream(transactionId, files.get(0));
            ExcelBeanReader<Fact> reader = new ExcelBeanReader<>(stream1, Fact.class, mappingParam.getMapping());
        ) {
            int count = 0;
            while (reader.hasNextRow() && count < 100) {
                DataRow<Fact> item = reader.nextValidatedRow();
                rows.add(new PreviewResponse(item.getFact(), item.getErrors().getFieldErrors(), item.isValid()));
                count++;
            }
            return rows;
        }
    }

    @ResponseBody
    @PostMapping(value = "/{transactionId}/start", consumes = "application/json")
    public ResponseEntity start(@RequestHeader("x-tenant") String tenant,
                                @PathVariable("projectId") String projectId,
                                @PathVariable("transactionId") String transactionId,
                                @RequestBody UploadRequestBody uploadRequestBody) {
        List<String> files = fileManager.getFiles(transactionId);
        System.out.println(tenant + projectId);
        importService.runImport(tenant, projectId, transactionId, files, uploadRequestBody.getMapping());
        return new ResponseEntity(HttpStatus.OK);
    }


    private MappingResult getColumnMapping(InputStream inputStream) throws Exception {
        ExcelReader<List<Object>> reader = new ExcelReader<>(inputStream);
        List<String> headerRow = reader.getHeader();
        List<Object> previewRow = reader.nextRow();
        reader.close();
        return new MappingResult(headerRow, previewRow);
    }
}
