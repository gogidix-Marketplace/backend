package com.gogidix.sales.revenue.domain.repository;

import com.gogidix.sales.revenue.domain.model.RevenueRecognitionSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Revenue Recognition Schedule Repository Interface (Port)
 * Defines the contract for revenue recognition schedule persistence operations
 */
public interface RevenueRecognitionScheduleRepository {

    RevenueRecognitionSchedule save(RevenueRecognitionSchedule schedule);

    List<RevenueRecognitionSchedule> saveAll(List<RevenueRecognitionSchedule> schedules);

    Optional<RevenueRecognitionSchedule> findById(String id);

    Optional<RevenueRecognitionSchedule> findByScheduleIdAndTenantId(String scheduleId, String tenantId);

    List<RevenueRecognitionSchedule> findByTenantId(String tenantId);

    List<RevenueRecognitionSchedule> findByTenantIdAndRevenueId(String tenantId, String revenueId);

    List<RevenueRecognitionSchedule> findByTenantIdAndContractId(String tenantId, String contractId);

    List<RevenueRecognitionSchedule> findByTenantIdAndStatus(String tenantId,
                                                               RevenueRecognitionSchedule.ScheduleStatus status);

    List<RevenueRecognitionSchedule> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<RevenueRecognitionSchedule> findActiveSchedulesByTenantId(String tenantId);

    List<RevenueRecognitionSchedule> findSchedulesDueForProcessing(String tenantId, LocalDate processingDate);

    boolean existsByScheduleIdAndTenantId(String scheduleId, String tenantId);

    void deleteById(String id);

    void deleteByScheduleIdAndTenantId(String scheduleId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, RevenueRecognitionSchedule.ScheduleStatus status);
}
