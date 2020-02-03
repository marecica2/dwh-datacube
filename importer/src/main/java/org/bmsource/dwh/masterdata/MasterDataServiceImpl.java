package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.model.Taxonomy;
import org.bmsource.dwh.masterdata.repository.ServiceTypeMappingRepository;
import org.bmsource.dwh.masterdata.repository.TaxonomyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Scope("prototype")
public class MasterDataServiceImpl implements MasterDataService {

    private static Logger logger = LoggerFactory.getLogger(MasterDataServiceImpl.class);

    private Map<String, ServiceTypeMapping> serviceTypesBySupplierServiceType;

    private Map<String, Taxonomy> taxonomy;

    @Autowired
    ServiceTypeMappingRepository serviceTypeMappingRepository;

    @Autowired
    TaxonomyRepository taxonomyRepository;

    @PostConstruct
    public void init() {
        Iterable<ServiceTypeMapping> serviceTypes = serviceTypeMappingRepository.findAll();
        Iterable<Taxonomy> taxonomy = taxonomyRepository.findAll();

        serviceTypesBySupplierServiceType = groupBy(serviceTypes,
            item -> item.getSupplierServiceType());

        this.taxonomy = groupBy(taxonomy,
            item -> item.getName());

        logger.info("Masterdata service initialized");
    }

    @Override
    public String getStandardServiceType(String supplierServiceType) {
        if (serviceTypesBySupplierServiceType.get(supplierServiceType) != null) {
            return serviceTypesBySupplierServiceType.get(supplierServiceType).getStandardServiceTypeParcel();
        }
        return null;
    }

    @Override
    public String getServiceGroup(String supplierServiceType) {
        ServiceTypeMapping stm = serviceTypesBySupplierServiceType.get(supplierServiceType);
        if (stm != null) {
            Taxonomy taxonomy = this.taxonomy.get(stm.getStandardServiceTypeParcel());
            if (taxonomy != null)
                return taxonomy.getStandardServiceTypeGroup();
        }
        return null;
    }

    private <K, V> Map<K, V> groupBy(Iterable<V> serviceTypes, Function<V, K> fn) {
        return StreamSupport.stream(serviceTypes.spliterator(), false)
            .collect(Collectors.toMap(fn, item -> item));
    }
}
