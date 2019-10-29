package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.BaseFact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@RestController
public class MasterDataController {

    @Autowired
    ZipCodeLocationRepository repository;

    @PostMapping(value = "/zip-code-locations/import")
    public DeferredResult<ResponseEntity<?>> importZipCodes(MultipartHttpServletRequest request) {
        Class<ZipCodeLocation> classType = ZipCodeLocation.class;
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        return importModel(
            request,
            classType,
            (Void) -> repository.deleteAll(),
            zipCodeLocations -> {
                try {
                    repository.saveAll(zipCodeLocations);
                } catch (Exception e) {
                    new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            },
            result
        );
    }

    private <T extends BaseFact> DeferredResult<ResponseEntity<?>> importModel(MultipartHttpServletRequest request,
                                                                               Class<T> classType,
                                                                               Consumer<Void> onStart,
                                                                               Consumer<List<T>> onRead,
                                                                               DeferredResult<ResponseEntity<?>> result
    ) {
        SimpleExcelParser excelParser = new SimpleExcelParser<T>(
            onStart,
            onRead,
            rows -> {
                result.setResult(new ResponseEntity<Integer>(HttpStatus.CREATED));
            },
            classType
        );
        try {
            MultipartFile multipartFile = request.getFile("file");
            String name = multipartFile.getOriginalFilename();
            try (InputStream inputStream = multipartFile.getInputStream();) {
                excelParser.parse(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
