package org.bmsource.dwh.api.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.api.parsers.XslxParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  private String TMP_UPLOAD_DIR = Paths.get(".").toAbsolutePath().normalize().toString() + "/tmp";

  @GetMapping
  public String init() {
    return UUID.randomUUID().toString();
  }

  @PostMapping
  public List<String> handleUpload(HttpServletRequest request) throws IOException, FileUploadException {
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
            File targetFile = new File(TMP_UPLOAD_DIR + "/" + name);
            FileUtils.copyInputStreamToFile(stream, targetFile);
            System.out.println("Upload completed " + targetFile);
            files.add(name);
          }
        }
      }
    }
    return files;
  }

  @PostMapping(value = "/mapping", consumes = "application/json")
  public String columnMapping(
      @RequestParam("transactionId") String transactionId,
      @RequestBody MappingRequest filesParam
  ) {
    System.out.println(filesParam);
    XslxParser.parse(TMP_UPLOAD_DIR + "/" + transactionId + "/" + );
    return "ok";
  }
}
