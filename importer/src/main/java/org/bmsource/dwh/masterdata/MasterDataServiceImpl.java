package org.bmsource.dwh.masterdata;

import org.bmsource.dwh.common.utils.StringUtils;
import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.bmsource.dwh.masterdata.repository.ServiceTypeMappingRepository;
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

    private Map<String, ServiceTypeMapping> serviceTypesBySupplierServiceType;

    @Autowired
    ServiceTypeMappingRepository serviceTypeMappingRepository;

    @PostConstruct
    void init() {
        Iterable<ServiceTypeMapping> serviceTypes = serviceTypeMappingRepository.findAll();
        serviceTypesBySupplierServiceType = groupBy(serviceTypes,
            item -> StringUtils.normalize(item.getSupplierServiceType()));
    }

    @Override
    public String getStandardServiceType(String supplierServiceType) {
        if (serviceTypesBySupplierServiceType.get(supplierServiceType) != null)
            return serviceTypesBySupplierServiceType.get(supplierServiceType).getStandardServiceTypeParcel();
        return null;
    }

    private <K, V> Map<K, V> groupBy(Iterable<V> serviceTypes, Function<V, K> fn) {
        return StreamSupport.stream(serviceTypes.spliterator(), false)
            .collect(Collectors.toMap(fn, item -> item));
    }
}
