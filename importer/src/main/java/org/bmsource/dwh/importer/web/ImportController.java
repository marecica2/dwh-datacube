package org.bmsource.dwh.importer.web;

import org.bmsource.dwh.common.fileManager.FileManager;
import org.bmsource.dwh.common.fileManager.TmpFileManager;
import org.bmsource.dwh.common.importer.ImportService;
import org.bmsource.dwh.common.reader.ExcelRowMapper;
import org.bmsource.dwh.common.reader.DataReader;
import org.bmsource.dwh.common.reader.ExcelReader;
import org.bmsource.dwh.common.reader.MappingResult;
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
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream);
            return MappingResponse.builder().setSourceFields(columnMapping.getHeaderRow(),
                columnMapping.getPreviewRow()).setFactModel(Fact.class).autoSuggestionMapping().build();
        }
    }

    @PostMapping(value = "/{transactionId}/preview", consumes = "application/json")
    public List<Fact> preview(@PathVariable("projectId") String projectId,
                              @PathVariable("transactionId") String transactionId,
                              @RequestBody PreviewRequestBody mappingParam) throws Exception {

        List<String> files = fileManager.getFiles(transactionId);
        try (InputStream stream1 = fileManager.getStream(transactionId, files.get(0)); InputStream stream2 =
            fileManager.getStream(transactionId, files.get(0));) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream1);
            return new ExcelRowMapper<>(Fact.class, columnMapping.getHeaderRow(), mappingParam.getMapping()).mapList(reader.readContent(stream2, 100));
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
}
