package org.bmsource.dwh.api.fileManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class FileSystemImpl implements FileManager {

  private static String SLASH = FileSystems.getDefault().getSeparator();

  private static String BASE_DIR = Paths.get(".") + SLASH + "tmp" + SLASH;

  @Override
  public String createTransaction() {
    String transaction =  UUID.randomUUID().toString();
    File baseDir = new File(BASE_DIR + SLASH + transaction);
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
    File baseDir = new File(BASE_DIR + SLASH + transactionId);
    return Arrays.stream(baseDir.listFiles()).map(file -> file.getName()).collect(Collectors.toList());
  }

  @Override
  public InputStream getStream(String transactionId, String fileName) throws IOException {
    File src = createFile(transactionId, fileName);
    return FileUtils.openInputStream(src);
  }

  private File createFile(String transactionId, String name) {
    return new File(BASE_DIR + SLASH + transactionId + SLASH + name);
  }
}
