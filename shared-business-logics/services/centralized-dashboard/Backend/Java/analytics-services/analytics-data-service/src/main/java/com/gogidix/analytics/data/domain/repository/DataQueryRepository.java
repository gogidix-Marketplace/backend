package com.gogidix.analytics.data.domain.repository;

import com.gogidix.analytics.data.domain.model.DataQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for DataQuery aggregate.
 */
@Repository
public interface DataQueryRepository extends JpaRepository<DataQuery, String> {

    List<DataQuery> findByTenantId(String tenantId);

    List<DataQuery> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<DataQuery> findByTenantIdAndIsPublicTrue(String tenantId);

    List<DataQuery> findByTenantIdAndOwnerIdAndIsFavoriteTrue(String tenantId, String ownerId);

    List<DataQuery> findByTenantIdAndCategory(String tenantId, String category);

    @Query("SELECT q FROM DataQuery q WHERE q.tenantId = :tenantId AND " +
           "(LOWER(q.queryName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<DataQuery> searchByTenantId(@Param("tenantId") String tenantId, @Param("search") String search);

    List<DataQuery> findByTenantIdAndQueryType(String tenantId, DataQuery.QueryType queryType);
}
