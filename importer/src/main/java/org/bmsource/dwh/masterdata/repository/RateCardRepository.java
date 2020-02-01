package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.RateCard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Component
@RepositoryRestResource(collectionResourceRel = "rate-cards", path = "rate-cards")
public interface RateCardRepository extends PagingAndSortingRepository<RateCard, BigInteger> {

    @Modifying
    @Transactional
    @Query("DELETE FROM RateCard r")
    void delete();
}
