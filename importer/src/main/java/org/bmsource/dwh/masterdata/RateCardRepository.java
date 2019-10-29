package org.bmsource.dwh.masterdata;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@RepositoryRestResource(collectionResourceRel = "rate-cards", path = "rate-cards")
public interface RateCardRepository extends PagingAndSortingRepository<RateCard, BigInteger> {
}
