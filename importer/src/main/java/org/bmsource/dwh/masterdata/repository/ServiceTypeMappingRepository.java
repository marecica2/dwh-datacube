package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.ServiceTypeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Component
@Transactional
@RepositoryRestResource(collectionResourceRel = "service-types", path = "service-types")
public interface ServiceTypeMappingRepository extends JpaRepository<ServiceTypeMapping, BigInteger> {

    @Modifying
    @Query("DELETE FROM ServiceTypeMapping s")
    void delete();
}
