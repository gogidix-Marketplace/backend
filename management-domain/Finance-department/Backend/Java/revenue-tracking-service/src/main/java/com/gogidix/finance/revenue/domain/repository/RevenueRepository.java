package com.gogidix.finance.revenue.domain.repository;

import com.gogidix.finance.revenue.domain.model.Revenue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Revenue Repository Interface (Port)
 * Defines the contract for revenue persistence operations
 */
public interface RevenueRepository {

    Revenue save(Revenue revenue);

    List<Revenue> saveAll(List<Revenue> revenues);

    Optional<Revenue> findById(String id);

    Optional<Revenue> findByRevenueIdAndTenantId(String revenueId, String tenantId);

    List<Revenue> findByTenantId(String tenantId);

    List<Revenue> findByTenantIdAndStatus(String tenantId, Revenue.RevenueStatus status);

    List<Revenue> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Revenue> findByTenantIdAndType(String tenantId, Revenue.RevenueType type);

    List<Revenue> findByTenantIdAndRecognitionMethod(String tenantId, Revenue.RecognitionMethod method);

    List<Revenue> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Revenue> findByTenantIdAndRecognitionStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Revenue> findByTenantIdAndNextRecognitionDateBefore(String tenantId, LocalDate date);

    List<Revenue> findByTenantIdAndStatusIn(String tenantId, List<Revenue.RevenueStatus> statuses);

    List<Revenue> findByTenantIdAndDepartment(String tenantId, String department);

    List<Revenue> findByTenantIdAndRegion(String tenantId, String region);

    List<Revenue> findByTenantIdAndSalespersonId(String tenantId, String salespersonId);

    List<Revenue> findByTenantIdAndProjectId(String tenantId, String projectId);

    List<Revenue> findByTenantIdAndContractId(String tenantId, String contractId);

    List<Revenue> findByTenantIdAndIsRecurring(String tenantId, Boolean isRecurring);

    boolean existsByRevenueIdAndTenantId(String revenueId, String tenantId);

    void deleteById(String id);

    void deleteByRevenueIdAndTenantId(String revenueId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Revenue.RevenueStatus status);

    java.math.BigDecimal sumTotalAmountByTenantId(String tenantId);

    java.math.BigDecimal sumRecognizedAmountByTenantId(String tenantId);

    java.math.BigDecimal sumDeferredAmountByTenantId(String tenantId);

    java.math.BigDecimal sumRecognizedAmountByTenantIdAndStatus(String tenantId, Revenue.RevenueStatus status);

    List<Revenue> findByTenantIdAndTagsContaining(String tenantId, String tag);
}
