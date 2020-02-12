package org.bmsource.dwh.masterdata.repository;

import org.bmsource.dwh.masterdata.model.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RepositoryRestResource(collectionResourceRel = "taxonomy", path = "taxonomy")
public interface TaxonomyRepository extends JpaRepository<Taxonomy, String> {

    @Modifying
    @Query("DELETE FROM Taxonomy t")
    void delete();

}
