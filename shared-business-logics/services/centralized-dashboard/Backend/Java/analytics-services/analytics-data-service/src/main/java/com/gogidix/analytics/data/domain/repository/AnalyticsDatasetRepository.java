package com.gogidix.analytics.data.domain.repository;

import com.gogidix.analytics.data.domain.model.AnalyticsDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for AnalyticsDataset aggregate.
 */
@Repository
public interface AnalyticsDatasetRepository extends JpaRepository<AnalyticsDataset, String> {

    List<AnalyticsDataset> findByTenantId(String tenantId);

    List<AnalyticsDataset> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<AnalyticsDataset> findByTenantIdAndIsPublicTrue(String tenantId);

    List<AnalyticsDataset> findByTenantIdAndIsActiveTrue(String tenantId);

    List<AnalyticsDataset> findByTenantIdAndDatasetType(String tenantId, AnalyticsDataset.DatasetType datasetType);

    @Query("SELECT d FROM AnalyticsDataset d WHERE d.tenantId = :tenantId AND " +
           "(LOWER(d.datasetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<AnalyticsDataset> searchByTenantId(@Param("tenantId") String tenantId, @Param("search") String search);
}
