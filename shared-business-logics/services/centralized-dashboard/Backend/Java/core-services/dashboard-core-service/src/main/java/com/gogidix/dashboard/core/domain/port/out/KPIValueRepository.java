package com.gogidix.dashboard.core.domain.port.out;

import com.gogidix.dashboard.core.domain.model.KPIValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for KPIValue entity.
 * Output port in hexagonal architecture.
 */
public interface KPIValueRepository {

    /**
     * Save a KPI value
     */
    KPIValue save(KPIValue value);

    /**
     * Find KPI value by ID
     */
    Optional<KPIValue> findById(UUID id);

    /**
     * Find KPI values by KPI ID
     */
    List<KPIValue> findByKpiId(UUID kpiId);

    /**
     * Find KPI values by KPI ID within date range
     */
    List<KPIValue> findByKpiIdAndRecordedAtBetween(UUID kpiId, LocalDateTime start, LocalDateTime end);

    /**
     * Find latest KPI value for a KPI
     */
    Optional<KPIValue> findFirstByKpiIdOrderByRecordedAtDesc(UUID kpiId);

    /**
     * Find KPI values by KPI ID with limit
     */
    List<KPIValue> findByKpiIdOrderByRecordedAtDesc(UUID kpiId, int limit);

    /**
     * Delete KPI values by KPI ID
     */
    void deleteByKpiId(UUID kpiId);

    /**
     * Delete KPI values older than specified date
     */
    void deleteByRecordedAtBefore(LocalDateTime date);
}
