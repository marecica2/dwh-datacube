package org.bmsource.dwh.courier.masterdata.repository;

import org.bmsource.dwh.courier.masterdata.model.ZipCodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Component
@Transactional
@RepositoryRestResource(collectionResourceRel = "zip-code-locations", path = "zip-code-locations")
public interface ZipCodeLocationRepository extends JpaRepository<ZipCodeLocation, BigInteger> {
    @Modifying
    @Query("DELETE FROM ZipCodeLocation z")
    void delete();
}
