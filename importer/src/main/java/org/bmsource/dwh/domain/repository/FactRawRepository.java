package org.bmsource.dwh.domain.repository;

import org.bmsource.dwh.domain.model.RawFact;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;

public interface FactRawRepository extends PagingAndSortingRepository<RawFact, BigInteger> {

}
