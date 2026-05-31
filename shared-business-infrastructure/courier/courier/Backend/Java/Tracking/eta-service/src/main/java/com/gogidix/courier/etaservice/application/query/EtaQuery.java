package com.gogidix.courier.etaservice.application.query;

import com.gogidix.courier.etaservice.domain.entity.EtaCalculation;

import java.time.Instant;
import java.util.List;

/**
 * Query interface for ETA operations.
 */
public interface EtaQuery {

    /**
     * Get ETA by dispatch ID.
     *
     * @param dispatchId the dispatch ID
     * @return the ETA calculation
     * @throws com.gogidix.courier.etaservice.shared.exception.NotFoundException if not found
     */
    EtaCalculation getByDispatchId(String dispatchId);

    /**
     * Get ETA by dispatch ID and tenant ID.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return the ETA calculation
     * @throws com.gogidix.courier.etaservice.shared.exception.NotFoundException if not found
     */
    EtaCalculation getByDispatchIdAndTenantId(String dispatchId, String tenantId);

    /**
     * Get ETA history for a dispatch.
     *
     * @param dispatchId the dispatch ID
     * @param tenantId   the tenant ID
     * @return list of history entries
     */
    List<EtaHistoryDto> getHistory(String dispatchId, String tenantId);

    /**
     * Get active ETAs for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active ETA calculations
     */
    List<EtaCalculation> getActiveByTenantId(String tenantId);

    /**
     * Get ETAs arriving soon (within given minutes).
     *
     * @param tenantId the tenant ID
     * @param withinMinutes the time window in minutes
     * @return list of ETA calculations
     */
    List<EtaCalculation> getArrivingSoon(String tenantId, int withinMinutes);

    /**
     * Get ETAs by status.
     *
     * @param tenantId the tenant ID
     * @param status   the ETA status
     * @return list of ETA calculations
     */
    List<EtaCalculation> getByStatus(String tenantId, EtaCalculation.EtaStatus status);

    /**
     * Get ETA statistics for a tenant.
     *
     * @param tenantId the tenant ID
     * @param fromDate the start date (optional)
     * @param toDate   the end date (optional)
     * @return ETA statistics
     */
    EtaStatisticsDto getStatistics(String tenantId, Instant fromDate, Instant toDate);

    /**
     * DTO for ETA history entries.
     */
    record EtaHistoryDto(
            String id,
            String dispatchId,
            Instant timestamp,
            Integer etaMinutes,
            Integer previousEtaMinutes,
            Integer etaChangeMinutes,
            Double distanceKm,
            String trafficLevel,
            String changeReason,
            String changeType
    ) {}

    /**
     * DTO for ETA statistics.
     */
    record EtaStatisticsDto(
            long totalCalculations,
            long activeDeliveries,
            long completedDeliveries,
            double averageEtaMinutes,
            double averageAccuracyPercentage,
            long onTimeDeliveries,
            long delayedDeliveries,
            long earlyDeliveries,
            String mostCommonTrafficLevel
    ) {}
}
