package org.bmsource.dwh.security.repository;

import org.bmsource.dwh.security.model.User;
import org.bmsource.dwh.security.model.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users", excerptProjection = UserProjection.class)
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query(
            value = "select u, t, r from User u left outer join fetch u.tenants t left outer join fetch u.roles r",
            countQuery = "select count (u) from User u"
    )
    Page<User> findAllEager(Pageable pageable);
}
