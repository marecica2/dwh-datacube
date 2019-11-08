package org.bmsource.dwh.common.fileManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface FileManager {

  String createTransaction();

  String addFile(String transactionId, String fileName, InputStream stream) throws IOException;

  List<String> getFiles(String transactionId);

  InputStream getStream(String transactionId, String fileName) throws IOException;

  InputStream getStream(String tenant, String projectId, String fileName) throws IOException;

  OutputStream writeStream(String transactionId, String fileName) throws IOException;

  OutputStream writeErrorStream(String transactionId, String fileName) throws IOException;

  void exportErrors(String transactionId, String tenant, String project) throws IOException;

  void delete(String transactionId) throws IOException;
}
