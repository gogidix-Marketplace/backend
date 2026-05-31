package com.gogidix.sales.revenue.domain.port.in;

import com.gogidix.sales.revenue.domain.model.Revenue;
import com.gogidix.sales.revenue.domain.model.dto.MRRSummaryDto;
import com.gogidix.sales.revenue.domain.model.dto.ARRSummaryDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Revenue Query (Input Port)
 * Defines the query operations for revenue
 */
public interface RevenueQuery {

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

    BigDecimal sumRecognizedAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumTotalAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumRemainingAmountByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Revenue.RevenueStatus status);

    MRRSummaryDto calculateMRR(String tenantId, YearMonth period);

    ARRSummaryDto calculateARR(String tenantId, YearMonth period);

    List<Revenue> findRecurringRevenueByTenantId(String tenantId);

    BigDecimal calculateRevenueByProduct(String tenantId, String productId, LocalDate startDate, LocalDate endDate);

    BigDecimal calculateRevenueByTerritory(String tenantId, String territory, LocalDate startDate, LocalDate endDate);
}
