package org.bmsource.dwh.common.fileManager;

import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.common.utils.ZipUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TmpFileManager implements FileManager {

    static String SLASH = FileSystems.getDefault().getSeparator();

    static String ERROR_FILE_NAME = "errors";

    String baseDir() {
        return System.getProperty("java.io.tmpdir") + SLASH + "dwh";
    }

    @Override
    public String createTransaction() {
        String transaction = UUID.randomUUID().toString();
        File baseDir = new File(baseDir() + SLASH + transaction);
        baseDir.mkdirs();
        return transaction;
    }

    @Override
    public String addFile(String transactionId, String fileName, InputStream stream) throws IOException {
        File dest = createFile(transactionId + SLASH + fileName);
        FileUtils.copyInputStreamToFile(stream, dest);
        return fileName;
    }

    @Override
    public List<String> getFiles(String transactionId) {
        File baseDir = new File(baseDir() + SLASH + transactionId);
        return Arrays.stream(baseDir.listFiles()).map(file -> file.getName()).collect(Collectors.toList());
    }

    @Override
    public InputStream getStream(String transactionId, String fileName) throws IOException {
        File src = createFile(transactionId + SLASH + fileName);
        return FileUtils.openInputStream(src);
    }

    @Override
    public InputStream getStream(String tenant, String projectId, String fileName) throws IOException {
        File src = createFile(tenant + SLASH + projectId + SLASH + fileName);
        return FileUtils.openInputStream(src);
    }

    @Override
    public OutputStream writeStream(String transactionId, String fileName) throws IOException {
        File dest = createFile(transactionId + SLASH + fileName);
        try {
            return new FileOutputStream(dest);
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        }
    }

    @Override
    public OutputStream writeErrorStream(String transactionId, String fileName) throws IOException {
        return writeStream(transactionId, SLASH + ERROR_FILE_NAME + SLASH + fileName);
    }

    @Override
    public void exportErrors(String transactionId, String tenant, String project) throws IOException {
        String errorsFolder = baseDir() + SLASH + transactionId + SLASH + ERROR_FILE_NAME;
        String dest = baseDir() + SLASH + tenant + SLASH + project;
        Files.createDirectories(Paths.get(dest));
        ZipUtils.zipFolder(errorsFolder, dest + SLASH + ERROR_FILE_NAME + ".zip");
    }

    @Override
    public void delete(String transactionId) throws IOException {
        FileUtils.deleteDirectory(new File(baseDir() + SLASH + transactionId));
    }

    File createFile(String path) throws IOException {
        String absolutePath = baseDir() + SLASH + path;
        String folders = absolutePath.substring(0, absolutePath.lastIndexOf(SLASH));
        Files.createDirectories(Paths.get(folders));
        File file = new File(absolutePath);
        file.createNewFile();
        return file;
    }
}
