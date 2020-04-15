package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.security.model.Role;
import org.bmsource.dwh.security.model.RoleProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RepositoryRestResource(collectionResourceRel = "roles", path = "roles", excerptProjection = RoleProjection.class)
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    @Query(value = "SELECT * FROM master.Roles where name IN (:roles)", nativeQuery = true)
    Set<Role> find(@Param("roles") List<String> roles);
}
