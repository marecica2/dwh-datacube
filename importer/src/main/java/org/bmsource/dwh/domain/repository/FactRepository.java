package org.bmsource.dwh.domain.repository;

import org.bmsource.dwh.domain.model.Fact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Optional;

@Component
@RepositoryRestResource(collectionResourceRel = "facts", path = "facts")
public interface FactRepository extends PagingAndSortingRepository<Fact, BigInteger> {

     Optional<Fact> findByTransactionId(String transactionId);

}
