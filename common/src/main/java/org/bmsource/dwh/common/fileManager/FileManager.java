package org.bmsource.dwh.common.fileManager;

import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Profile("unit-test")
public interface FileManager {

  String createTransaction();

  String addFile(String transactionId, String fileName, InputStream stream) throws IOException;

  List<String> getFiles(String transactionId);

  InputStream getStream(String transactionId, String fileName) throws IOException;

  OutputStream writeStream(String transactionId, String fileName) throws IOException;
}
