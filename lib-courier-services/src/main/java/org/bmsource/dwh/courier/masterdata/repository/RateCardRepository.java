package org.bmsource.dwh.courier.masterdata.repository;

import org.bmsource.dwh.courier.masterdata.model.RateCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Component
@Transactional
@RepositoryRestResource(collectionResourceRel = "rate-cards", path = "rate-cards")
public interface RateCardRepository extends JpaRepository<RateCard, BigInteger> {

    @Modifying
    @Query("DELETE FROM RateCard r")
    void delete();
}
