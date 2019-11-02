package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.Taxonomy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

@Component
@RepositoryRestResource(collectionResourceRel = "taxonomy", path = "taxonomy")
public interface TaxonomyRepository extends PagingAndSortingRepository<Taxonomy, String> {
}
