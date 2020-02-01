package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.Taxonomy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@RepositoryRestResource(collectionResourceRel = "taxonomy", path = "taxonomy")
public interface TaxonomyRepository extends PagingAndSortingRepository<Taxonomy, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Taxonomy t")
    void delete();
}
