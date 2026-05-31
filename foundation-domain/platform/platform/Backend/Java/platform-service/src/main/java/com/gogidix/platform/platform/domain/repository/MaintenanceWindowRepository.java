package com.gogidix.platform.platform.domain.repository;

import com.gogidix.platform.platform.domain.model.MaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MaintenanceWindow entity operations.
 */
@Repository
public interface MaintenanceWindowRepository extends JpaRepository<MaintenanceWindow, String> {

    /**
     * Find active maintenance windows for a tenant
     */
    List<MaintenanceWindow> findByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Find maintenance windows that are currently active (within time range)
     */
    List<MaintenanceWindow> findByTenantIdAndIsActiveTrueAndStartTimeBeforeAndEndTimeAfter(
        String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find upcoming maintenance windows
     */
    List<MaintenanceWindow> findByTenantIdAndStartTimeAfterOrderByStartTimeAsc(String tenantId, LocalDateTime startTime);

    /**
     * Find maintenance windows by status
     */
    List<MaintenanceWindow> findByTenantIdAndStatus(String tenantId, MaintenanceWindow.MaintenanceStatus status);

    /**
     * Find maintenance windows affecting specific services
     */
    List<MaintenanceWindow> findByTenantIdAndAffectedServicesContaining(String tenantId, String serviceName);

    /**
     * Check if maintenance is active for a tenant at a given time
     */
    boolean existsByTenantIdAndIsActiveTrueAndStartTimeBeforeAndEndTimeAfter(
        String tenantId, LocalDateTime startTime, LocalDateTime endTime);
}
