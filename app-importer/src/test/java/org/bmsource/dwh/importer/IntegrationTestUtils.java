package org.bmsource.dwh.importer;

import org.apache.commons.io.FileUtils;
import org.bmsource.dwh.courier.masterdata.MasterDataNormalizer;
import org.bmsource.dwh.courier.masterdata.model.RateCard;
import org.bmsource.dwh.courier.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.courier.masterdata.model.Taxonomy;
import org.bmsource.dwh.courier.masterdata.repository.RateCardRepository;
import org.bmsource.dwh.courier.masterdata.repository.ServiceTypeMappingRepository;
import org.bmsource.dwh.courier.masterdata.repository.TaxonomyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class IntegrationTestUtils extends org.bmsource.dwh.common.utils.IntegrationTestUtils {

    @Autowired(required = false)
    TaxonomyRepository taxonomyRepository;

    @Autowired(required = false)
    ServiceTypeMappingRepository serviceTypeMappingRepository;

    @Autowired(required = false)
    RateCardRepository rateCardRepository;

    public void hasTaxonomy(String... optFileName) throws Exception {
        File file = getResource("taxonomy.xlsx", optFileName);
        importExcel(FileUtils.openInputStream(file), taxonomyRepository, Taxonomy.class,
            MasterDataNormalizer.normalizeTaxonomy, taxonomyRepository::delete);
    }

    public void hasServiceTypeMapping(String... optFileName) throws Exception {
        File file = getResource("matrix.xlsx", optFileName);
        importExcel(FileUtils.openInputStream(file), serviceTypeMappingRepository, ServiceTypeMapping.class,
            MasterDataNormalizer.normalizeServiceTypeMapping, serviceTypeMappingRepository::delete);
    }

    public void hasRateCards(String... optFileName) throws Exception {
        File file = getResource("standard_rate_card_small.xlsx", optFileName);
        importExcel(FileUtils.openInputStream(file), rateCardRepository, RateCard.class,
            MasterDataNormalizer.normalizeRateCards, rateCardRepository::delete);
    }
}
