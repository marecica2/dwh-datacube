package org.bmsource.dwh.repository;

import org.bmsource.dwh.model.RawFact;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

public interface FactRawRepository extends PagingAndSortingRepository<RawFact, BigInteger> {

}
