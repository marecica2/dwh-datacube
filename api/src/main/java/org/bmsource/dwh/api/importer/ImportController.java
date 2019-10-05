package org.bmsource.dwh.api.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bmsource.dwh.api.fileManager.FileManager;
import org.bmsource.dwh.api.fileManager.FileSystemImpl;
import org.bmsource.dwh.api.model.Fact;
import org.bmsource.dwh.api.model.FactModelMapper;
import org.bmsource.dwh.api.reader.DataReader;
import org.bmsource.dwh.api.reader.MappingResult;
import org.bmsource.dwh.api.reader.ExcelReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


class MappingRequest {

    List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}

class PreviewRequest {

    Map<String, String> mapping;

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
                                         @RequestBody MappingRequest filesParam
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
    public List<Fact> preview(@PathVariable("transactionId") String transactionId, @RequestBody PreviewRequest mappingParam) throws Exception {
        List<String> files = fileManager.getFiles(transactionId);
        try (
            InputStream stream1 = fileManager.getStream(transactionId, files.get(0));
            InputStream stream2 = fileManager.getStream(transactionId, files.get(0));
        ) {
            DataReader reader = new ExcelReader();
            MappingResult columnMapping = reader.readHeaderRow(stream1);
            FactModelMapper mapper = new FactModelMapper(columnMapping.getHeaderRow(), mappingParam.getMapping());
            return reader
                .readContent(stream2, 100)
                .stream()
                .map(row -> mapper.mapRow(row))
                .collect(Collectors.toList());
        }
    }
}
