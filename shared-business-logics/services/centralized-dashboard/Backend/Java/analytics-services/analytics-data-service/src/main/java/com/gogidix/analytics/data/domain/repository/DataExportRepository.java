package com.gogidix.analytics.data.domain.repository;

import com.gogidix.analytics.data.domain.model.DataExport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DataExport aggregate.
 */
@Repository
public interface DataExportRepository extends JpaRepository<DataExport, String> {

    List<DataExport> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    List<DataExport> findByTenantIdAndRequestedByOrderByCreatedAtDesc(String tenantId, String requestedBy);

    List<DataExport> findByTenantIdAndStatus(String tenantId, DataExport.ExportStatus status);

    @Query("SELECT e FROM DataExport e WHERE e.tenantId = :tenantId " +
           "AND e.status IN ('PENDING', 'PROCESSING') " +
           "AND e.startedAt < :cutoffTime")
    List<DataExport> findStuckExports(@Param("tenantId") String tenantId, @Param("cutoffTime") LocalDateTime cutoffTime);

    @Query("SELECT e FROM DataExport e WHERE e.tenantId = :tenantId " +
           "AND e.expiresAt < :now AND e.status = 'COMPLETED'")
    List<DataExport> findExpiredExports(@Param("tenantId") String tenantId, @Param("now") LocalDateTime now);

    Optional<DataExport> findByTenantIdAndIdAndRequestedBy(String tenantId, String id, String requestedBy);

    void deleteByExpiresAtBefore(LocalDateTime cutoffDate);
}
