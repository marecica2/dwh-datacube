package org.bmsource.dwh.masterdata;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;


@RestController()
@RequestMapping("{projectId}/master-data")
public class MasterDataController {

    SimpleImportService importService = new SimpleImportService();

    @PostMapping(value = "/zip-code-location/import", consumes = "multipart/form-data")
    public DeferredResult<ResponseEntity<?>> start(MultipartHttpServletRequest request) throws Exception {
        Class<ZipCodeLocation> classType = ZipCodeLocation.class;
        return importExcelStream(request, classType);
    }

    private DeferredResult<ResponseEntity<?>> importExcelStream(MultipartHttpServletRequest request,
                                                                Class<ZipCodeLocation> classType) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        try {
            MultipartFile multipartFile = request.getFile("file");
            String name = multipartFile.getOriginalFilename();
            try (InputStream inputStream = multipartFile.getInputStream();) {
                importService.start(classType, inputStream, (rowsCount) -> {
                    result.setResult(ResponseEntity.ok(rowsCount));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
