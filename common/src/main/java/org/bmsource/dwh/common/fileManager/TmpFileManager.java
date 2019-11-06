package org.bmsource.dwh.common.fileManager;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class TmpFileManager implements FileManager {

  static String SLASH = FileSystems.getDefault().getSeparator();

  String baseDir() {
    return System.getProperty("java.io.tmpdir") + SLASH + "dwh";
  }

  @Override
  public String createTransaction() {
    String transaction =  UUID.randomUUID().toString();
    File baseDir = new File(baseDir() + SLASH + transaction);
    baseDir.mkdirs();
    return transaction;
  }

  @Override
  public String addFile(String transactionId, String name, InputStream stream) throws IOException {
    File dest = createFile(transactionId, name);
    FileUtils.copyInputStreamToFile(stream, dest);
    return name;
  }

  @Override
  public List<String> getFiles(String transactionId) {
    File baseDir = new File(baseDir() + SLASH + transactionId);
    return Arrays.stream(baseDir.listFiles()).map(file -> file.getName()).collect(Collectors.toList());
  }

  @Override
  public InputStream getStream(String transactionId, String fileName) throws IOException {
    File src = createFile(transactionId, fileName);
    return FileUtils.openInputStream(src);
  }

  @Override
  public OutputStream writeStream(String transactionId, String fileName) throws IOException {
    File dest = createFile(transactionId, fileName);
    try {
      return new FileOutputStream(dest);
    } catch (FileNotFoundException e) {
      throw new IOException(e);
    }
  }

  @Override
  public String errorFile(String fileName) {
    return SLASH + "errors" + SLASH + fileName;
  };

  File createFile(String transactionId, String name) throws IOException {
    File file = new File(baseDir() + SLASH + transactionId + SLASH + name);
    file.getParentFile().mkdirs();
    file.createNewFile();
    return file;
  }
}
