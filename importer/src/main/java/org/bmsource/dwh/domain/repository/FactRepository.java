package org.bmsource.dwh.domain.repository;

import org.bmsource.dwh.domain.model.Fact;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface FactRepository extends PagingAndSortingRepository<Fact, BigInteger> {

     Optional<Fact> findByTransactionId(String transactionId);

}
