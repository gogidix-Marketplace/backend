package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.KpiWidget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for KpiWidget entities
 */
@Repository
public interface KpiWidgetRepository extends MongoRepository<KpiWidget, String> {

    /**
     * Find widgets by tenant ID
     */
    List<KpiWidget> findByTenantId(String tenantId);

    /**
     * Find widgets by tenant ID and not deleted
     */
    List<KpiWidget> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find widgets by dashboard ID
     */
    List<KpiWidget> findByDashboardId(String dashboardId);

    /**
     * Find widget by ID and tenant ID
     */
    Optional<KpiWidget> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find widget by ID and tenant ID and not deleted
     */
    Optional<KpiWidget> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find widgets by dashboard ID and active status
     */
    List<KpiWidget> findByDashboardIdAndActive(String dashboardId, boolean active);

    /**
     * Find active widgets by tenant
     */
    List<KpiWidget> findByTenantIdAndActiveTrue(String tenantId);

    /**
     * Count widgets by dashboard
     */
    long countByDashboardId(String dashboardId);
}
