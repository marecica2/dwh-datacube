package org.bmsource.dwh.common.fileManager;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@Profile("unit-test")
public class ResourceFileManager implements FileManager {

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
    File file = null;
    try {
      file = ResourceUtils.getFile(this.getClass().getResource(fileName));
    } catch (FileNotFoundException e) {
      throw new ItemStreamException(e);
    }
    return FileUtils.openInputStream(file);
  }

  @Override
  public OutputStream writeStream(String transactionId, String fileName) throws IOException {
    String tempDir = System.getProperty("java.io.tmpdir");
    File file = new File(tempDir + SLASH + transactionId + SLASH + fileName);
    return FileUtils.openOutputStream(file);
  }

  private File createFile(String transactionId, String name) {
    return new File(BASE_DIR + SLASH + transactionId + SLASH + name);
  }
}
