package com.gogidix.shared.warehousing.expiration.domain.repository;

import com.gogidix.shared.warehousing.expiration.domain.entity.ExpiryTracking;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpiryTracking.TrackingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Expiry Tracking Repository with Multi-Tenant Support
 */
@Repository
public interface ExpiryTrackingRepository extends MongoRepository<ExpiryTracking, String> {

    /**
     * Find tracking records by SKU and tenant
     */
    List<ExpiryTracking> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find tracking records by status and tenant
     */
    List<ExpiryTracking> findByTenantIdAndStatus(String tenantId, TrackingStatus status);

    /**
     * Find tracking records by expiry date range
     */
    List<ExpiryTracking> findByTenantIdAndExpiryDateBetween(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find tracking records expiring before date
     */
    List<ExpiryTracking> findByTenantIdAndExpiryDateBefore(String tenantId, LocalDate date);

    /**
     * Find tracking records where monitoring is active
     */
    List<ExpiryTracking> findByTenantIdAndMonitoringActiveTrue(String tenantId);

    /**
     * Find tracking records by batch/lot ID
     */
    List<ExpiryTracking> findByBatchLotId(String batchLotId);

    /**
     * Find all tracking records for a tenant
     */
    List<ExpiryTracking> findByTenantId(String tenantId);

    /**
     * Delete tracking records by batch/lot ID
     */
    void deleteByBatchLotId(String batchLotId);
}
