package org.bmsource.dwh.masterdata.web;

import org.bmsource.dwh.common.BaseFact;
import org.bmsource.dwh.masterdata.*;
import org.bmsource.dwh.masterdata.model.RateCard;
import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.model.Taxonomy;
import org.bmsource.dwh.masterdata.model.ZipCodeLocation;
import org.bmsource.dwh.masterdata.repository.RateCardRepository;
import org.bmsource.dwh.masterdata.repository.ServiceTypeMappingRepository;
import org.bmsource.dwh.masterdata.repository.TaxonomyRepository;
import org.bmsource.dwh.masterdata.repository.ZipCodeLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@RestController
public class MasterDataController {

    @Autowired
    ZipCodeLocationRepository zipCodeRepository;

    @Autowired
    TaxonomyRepository taxonomyRepository;

    @Autowired
    RateCardRepository rateCardRepository;

    @Autowired
    ServiceTypeMappingRepository serviceTypeMappingRepository;

    @PostMapping(value = "/zip-code-locations/import")
    public DeferredResult<ResponseEntity<?>> importZipCodes(MultipartHttpServletRequest request) {
        Class<ZipCodeLocation> classType = ZipCodeLocation.class;
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        return importModel(
            request,
            classType,
            (Void) -> zipCodeRepository.deleteAll(),
            zipCodeLocations -> {
                try {
                    zipCodeRepository.saveAll(zipCodeLocations);
                } catch (Exception e) {
                    new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            },
            result
        );
    }

    @PostMapping(value = "/service-types/import")
    public DeferredResult<ResponseEntity<?>> importServiceTypes(MultipartHttpServletRequest request) {
        Class<ServiceTypeMapping> classType = ServiceTypeMapping.class;
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        return importModel(
            request,
            classType,
            (Void) -> serviceTypeMappingRepository.deleteAll(),
            items -> {
                try {
                    serviceTypeMappingRepository.saveAll(items);
                } catch (Exception e) {
                    new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            },
            result
        );
    }

    @PostMapping(value = "/taxonomy/import")
    public DeferredResult<ResponseEntity<?>> importTaxonomy(MultipartHttpServletRequest request) {
        Class<Taxonomy> classType = Taxonomy.class;
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        return importModel(
            request,
            classType,
            (Void) -> taxonomyRepository.deleteAll(),
            items -> {
                try {
                    taxonomyRepository.saveAll(items);
                } catch (Exception e) {
                    new ResponseEntity<Integer>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            },
            result
        );
    }

    @PostMapping(value = "/rate-cards/import")
    public DeferredResult<ResponseEntity<?>> importRateCards(MultipartHttpServletRequest request) {
        Class<RateCard> classType = RateCard.class;
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        return importModel(
            request,
            classType,
            (Void) -> rateCardRepository.deleteAll(),
            items -> {
                try {
                    rateCardRepository.saveAll(items);
                } catch (Exception e) {
                    e.printStackTrace();
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
        GenericExcelReader excelParser = new GenericExcelReader<T>(
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
