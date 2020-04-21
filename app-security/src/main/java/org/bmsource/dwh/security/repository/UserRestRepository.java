package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users", excerptProjection = UserProjection.class)
public interface UserRestRepository extends JpaRepository<User, Long> {
    @Query(
            value = "select u, t, r from User u left outer join fetch u.tenants t left outer join fetch u.roles r",
            countQuery = "select count (u) from User u"
    )
    Page<User> findAllEager(Pageable pageable);
}
