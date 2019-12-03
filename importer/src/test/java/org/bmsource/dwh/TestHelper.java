package org.bmsource.dwh;

import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.masterdata.ExcelReaderHandler;
import org.bmsource.dwh.masterdata.GenericExcelReader;
import org.bmsource.dwh.masterdata.model.RateCard;
import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.model.Taxonomy;
import org.bmsource.dwh.masterdata.repository.RateCardRepository;
import org.bmsource.dwh.masterdata.repository.ServiceTypeMappingRepository;
import org.bmsource.dwh.masterdata.repository.TaxonomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@Component
public class TestHelper {

    @Autowired
    TaxonomyRepository taxonomyRepository;

    @Autowired
    ServiceTypeMappingRepository serviceTypeMappingRepository;

    @Autowired
    RateCardRepository rateCardRepository;

    public void hasTaxonomy(String... optFileName) throws Exception {
        File file = getResource("taxonomy.xlsx", optFileName);
        this.importExcel(FileUtils.openInputStream(file), taxonomyRepository, Taxonomy.class);
    }

    public void hasServiceTypeMapping(String... optFileName) throws Exception {
        File file = getResource("matrix.xlsx", optFileName);
        this.importExcel(FileUtils.openInputStream(file), serviceTypeMappingRepository, ServiceTypeMapping.class);
    }

    public void hasRateCards(String... optFileName) throws Exception {
        File file = getResource("standard_rate_card_small.xlsx", optFileName);
        this.importExcel(FileUtils.openInputStream(file), rateCardRepository, RateCard.class);
    }

    private <Type, Repository extends CrudRepository<Type, ?>> void importExcel(
        InputStream inputStream,
        Repository repository,
        Class<Type> classType
    ) {
        ExcelReaderHandler<Type> handler = new ExcelReaderHandler<Type>() {
            @Override
            public void onStart() {
                repository.deleteAll();
            }

            @Override
            public void onRead(List<Type> items) {
                try {
                    repository.saveAll(items);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        GenericExcelReader<Type> excelParser = new GenericExcelReader<>(handler, classType);
        try {
            excelParser.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getResource(String defaultFile, String[] optFileName) throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:" + defaultFile);
        if (optFileName != null && optFileName.length > 0)
            file = ResourceUtils.getFile("classpath:" + optFileName[0]);
        return file;
    }
}
