package com.gogidix.analytics.bi.domain.repository;

import com.gogidix.analytics.bi.domain.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Dashboard aggregate.
 */
@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, String> {

    List<Dashboard> findByTenantId(String tenantId);

    List<Dashboard> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<Dashboard> findByTenantIdAndIsPublicTrue(String tenantId);

    List<Dashboard> findByTenantIdAndOwnerIdAndIsFavoriteTrue(String tenantId, String ownerId);

    List<Dashboard> findByTenantIdAndCategory(String tenantId, String category);

    @Query("SELECT d FROM Dashboard d WHERE d.tenantId = :tenantId AND " +
           "(LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Dashboard> searchByTenantId(@Param("tenantId") String tenantId, @Param("search") String search);

    boolean existsBySlug(String slug);
}
