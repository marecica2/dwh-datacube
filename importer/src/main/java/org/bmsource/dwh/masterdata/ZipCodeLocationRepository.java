package org.bmsource.dwh.masterdata;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigInteger;

@RepositoryRestResource(collectionResourceRel = "zip-code-locations", path = "zip-code-locations")
public interface ZipCodeLocationRepository extends PagingAndSortingRepository<ZipCodeLocation, BigInteger> {
}
