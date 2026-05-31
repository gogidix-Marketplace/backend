package com.gogidix.management.shared.infrastructure.persistence;

import com.gogidix.management.shared.domain.BaseEntity;
import com.gogidix.management.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends MongoRepository<T, String> {

    @Query("{ 'tenantId': ?0 }")
    List<T> findAllByTenantId(String tenantId);

    Page<T> findAllByTenantId(String tenantId, Pageable pageable);

    @Query("{ '_id': ?0, 'tenantId': ?1 }")
    Optional<T> findByIdAndTenantId(String id, String tenantId);

    long countByTenantId(String tenantId);

    @Query(value = "{ '_id': ?0, 'tenantId': ?1 }", exists = true)
    boolean existsByIdAndTenantId(String id, String tenantId);

    @Query(value = "{ '_id': ?0, 'tenantId': ?1 }", delete = true)
    void deleteByIdAndTenantId(String id, String tenantId);

    default String getCurrentTenantId() {
        return RequestContextHolder.getTenantId();
    }

    default List<T> findAllForCurrentTenant() {
        return findAllByTenantId(getCurrentTenantId());
    }

    default Optional<T> findByIdForCurrentTenant(String id) {
        return findByIdAndTenantId(id, getCurrentTenantId());
    }

    default long countForCurrentTenant() {
        return countByTenantId(getCurrentTenantId());
    }

    default boolean existsForCurrentTenant(String id) {
        return existsByIdAndTenantId(id, getCurrentTenantId());
    }

    default void deleteForCurrentTenant(String id) {
        deleteByIdAndTenantId(id, getCurrentTenantId());
    }
}
