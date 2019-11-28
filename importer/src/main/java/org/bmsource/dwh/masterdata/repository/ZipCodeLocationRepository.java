package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.ZipCodeLocation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RepositoryRestResource(collectionResourceRel = "zip-code-locations", path = "zip-code-locations")
public interface ZipCodeLocationRepository extends PagingAndSortingRepository<ZipCodeLocation, BigInteger> {
}
