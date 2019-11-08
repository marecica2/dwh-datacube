package org.bmsource.dwh.common.fileManager;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
@Primary
@Profile("unit-test")
public class ResourceFileManager extends TmpFileManager {

    @Override
    String baseDir() {
        return System.getProperty("java.io.tmpdir") + SLASH + "dwh-test";
    }

    @Override
    public InputStream getStream(String transactionId, String fileName) throws IOException {
        try {
            File resourceFile = ResourceUtils.getFile(this.getClass().getResource(fileName));
            File file = createFile(transactionId + SLASH + fileName);
            FileUtils.copyFile(resourceFile, file);
            return FileUtils.openInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        }
    }
}
