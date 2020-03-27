package org.bmsource.dwh.courier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public interface FactRawRepository extends JpaRepository<RawFact, BigInteger> {

}
