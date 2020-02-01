package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Component
@RepositoryRestResource(collectionResourceRel = "service-types", path = "service-types")
public interface ServiceTypeMappingRepository extends PagingAndSortingRepository<ServiceTypeMapping, BigInteger> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ServiceTypeMapping s")
    void delete();
}
