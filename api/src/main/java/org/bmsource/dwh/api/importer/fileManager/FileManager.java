package org.bmsource.dwh.api.importer.fileManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileManager {

  String createTransaction();

  String addFile(String transactionId, String fileName, InputStream stream) throws IOException;

  List<String> getFiles(String transactionId);

  InputStream getStream(String transactionId, String fileName) throws IOException;
}
