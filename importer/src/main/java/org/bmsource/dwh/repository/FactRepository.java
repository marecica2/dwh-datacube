package org.bmsource.dwh.repository;

import org.bmsource.dwh.model.Fact;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface FactRepository extends PagingAndSortingRepository<Fact, BigInteger> {

     Optional<Fact> findByTransactionId(String transactionId);

}
