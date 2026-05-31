package com.gogidix.sales.revenue.domain.repository;

import com.gogidix.sales.revenue.domain.model.Revenue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
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

    List<Revenue> findByTenantIdAndProductId(String tenantId, String productId);

    List<Revenue> findByTenantIdAndContractId(String tenantId, String contractId);

    List<Revenue> findByTenantIdAndTerritory(String tenantId, String territory);

    List<Revenue> findByTenantIdAndRegion(String tenantId, String region);

    List<Revenue> findByTenantIdAndRevenueType(String tenantId, Revenue.RevenueType revenueType);

    List<Revenue> findByTenantIdAndRecognitionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Revenue> findByTenantIdAndBookingDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Revenue> findByTenantIdAndSalespersonId(String tenantId, String salespersonId);

    List<Revenue> findUnrecognizedRevenueByTenantId(String tenantId);

    List<Revenue> findDeferredRevenueByTenantId(String tenantId);

    List<Revenue> findByTenantIdAndInvoiceId(String tenantId, String invoiceId);

    List<Revenue> findRecurringRevenueByTenantId(String tenantId);

    BigDecimal sumRecognizedAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumTotalAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumRemainingAmountByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Revenue.RevenueStatus status);

    BigDecimal calculateRevenueByProduct(String tenantId, String productId, LocalDate startDate, LocalDate endDate);

    BigDecimal calculateRevenueByTerritory(String tenantId, String territory, LocalDate startDate, LocalDate endDate);

    boolean existsByRevenueIdAndTenantId(String revenueId, String tenantId);

    void deleteById(String id);

    void deleteByRevenueIdAndTenantId(String revenueId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    List<Revenue> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Revenue> findByTenantIdAndProjectId(String tenantId, String projectId);
}
