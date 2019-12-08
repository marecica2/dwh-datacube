package org.bmsource.dwh;

import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.common.utils.StringUtils;
import org.bmsource.dwh.masterdata.ExcelReaderHandler;
import org.bmsource.dwh.masterdata.GenericExcelReader;
import org.bmsource.dwh.masterdata.MasterDataNormalizer;
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
import java.util.function.Function;
import java.util.stream.Collectors;

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
        importExcel(FileUtils.openInputStream(file), taxonomyRepository, Taxonomy.class,
            MasterDataNormalizer.normalizeTaxonomy);
    }

    public void hasServiceTypeMapping(String... optFileName) throws Exception {
        File file = getResource("matrix.xlsx", optFileName);
        importExcel(FileUtils.openInputStream(file), serviceTypeMappingRepository, ServiceTypeMapping.class,
            MasterDataNormalizer.normalizeServiceTypeMapping);
    }

    public void hasRateCards(String... optFileName) throws Exception {
        File file = getResource("standard_rate_card_small.xlsx", optFileName);
        importExcel(FileUtils.openInputStream(file), rateCardRepository, RateCard.class,
            MasterDataNormalizer.normalizeRateCards);
    }

    private <Type, Repository extends CrudRepository<Type, ?>> void importExcel(
        InputStream inputStream,
        Repository repository,
        Class<Type> classType,
        Function<Type, Type> transform
    ) {
        ExcelReaderHandler<Type> handler = new ExcelReaderHandler<Type>() {
            @Override
            public void onStart() {
                repository.deleteAll();
            }

            @Override
            public void onRead(List<Type> items) {
                try {
                    List<Type> itemsTransformed = items
                        .stream()
                        .map(this::transform)
                        .collect(Collectors.toList());
                    repository.saveAll(itemsTransformed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public Type transform(Type item) {
                return transform.apply(item);
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
