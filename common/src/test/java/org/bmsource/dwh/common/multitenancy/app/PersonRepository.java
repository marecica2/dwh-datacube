package org.bmsource.dwh.common.multitenancy.app;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
