package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import com.gogidix.aiservices.aisalesforecastingservice.infrastructure.persistence.document.SalesForecastingDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for SalesForecast.
 */
@Repository
public interface SpringDataSalesForecastingRepository extends MongoRepository<SalesForecastingDocument, String> {

    /**
     * Find segments by tenant ID with pagination.
     */
    Page<SalesForecastingDocument> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find segments by tenant ID and segment type.
     */
    Page<SalesForecastingDocument> findByTenantIdAndForecastType(String tenantId, ForecastType segmentType, Pageable pageable);

    /**
     * Find segments by tenant ID and active status.
     */
    Page<SalesForecastingDocument> findByTenantIdAndActive(String tenantId, boolean active, Pageable pageable);

    /**
     * Find segments by tenant ID, segment type, and active status.
     */
    Page<SalesForecastingDocument> findByTenantIdAndForecastTypeAndActive(
            String tenantId, ForecastType segmentType, boolean active, Pageable pageable);

    /**
     * Find active segments by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'active': true }")
    Page<SalesForecastingDocument> findActiveByTenantId(String tenantId, Pageable pageable);

    /**
     * Count segments by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Count active segments by tenant ID.
     */
    long countByTenantIdAndActive(String tenantId, boolean active);

    /**
     * Check if segment exists by ID and tenant ID.
     */
    boolean existsByIdAndTenantId(String id, String tenantId);

    /**
     * Find by ID and tenant ID.
     */
    Optional<SalesForecastingDocument> findByIdAndTenantId(String id, String tenantId);

    /**
     * Delete by ID and tenant ID.
     */
    void deleteByIdAndTenantId(String id, String tenantId);
}
