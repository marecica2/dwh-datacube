package org.bmsource.dwh.api.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bmsource.dwh.api.importer.fileManager.FileManager;
import org.bmsource.dwh.api.importer.fileManager.FileSystemImpl;
import org.bmsource.dwh.api.parsers.XslxParser;
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

@RestController()
@RequestMapping("/import")
public class ImportController {

  private FileManager fileManager = new FileSystemImpl();

  @GetMapping
  public String init() {
    return fileManager.createTransaction();
  }

  @PostMapping("/{transactionId}")
  public List<String> handleUpload(HttpServletRequest request, @PathVariable("transactionId") String transactionId) throws IOException,
      FileUploadException {
    List<String> files = new ArrayList<>();

    if (ServletFileUpload.isMultipartContent(request)) {
      ServletFileUpload upload = new ServletFileUpload();
      FileItemIterator fileItemIterator = upload.getItemIterator(request);
      while (fileItemIterator.hasNext()) {
        FileItemStream fis = fileItemIterator.next();
        String name = fis.getName();
        System.out.println("Upload started for " + name);
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
  public String columnMapping(
      @PathVariable("transactionId") String transactionId,
      @RequestBody MappingRequest filesParam
  ) throws Exception {
    System.out.println(filesParam);
    List<String> files = fileManager.getFiles(transactionId);
    InputStream is = fileManager.getStream(transactionId, files.get(0));
    XslxParser.parse(is);
    return "ok";
  }

  @GetMapping(value = "/{transactionId}/preview")
  public String preview(@PathVariable("transactionId") String transactionId) throws Exception {
    List<String> files = fileManager.getFiles(transactionId);
    InputStream is = fileManager.getStream(transactionId, files.get(0));
    XslxParser.parse(is);
    return "ok";
  }
}
