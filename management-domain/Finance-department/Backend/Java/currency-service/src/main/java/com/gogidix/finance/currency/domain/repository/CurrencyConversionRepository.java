package com.gogidix.finance.currency.domain.repository;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface - Currency Conversion
 * Port for currency conversion persistence operations
 * Following hexagonal architecture principles
 */
public interface CurrencyConversionRepository {

    /**
     * Save a currency conversion entity
     */
    CurrencyConversion save(CurrencyConversion conversion);

    /**
     * Find conversion by ID and tenant ID
     */
    Optional<CurrencyConversion> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find conversion by conversion ID and tenant ID
     */
    Optional<CurrencyConversion> findByConversionIdAndTenantId(String conversionId, String tenantId);

    /**
     * Find all conversions for a tenant
     */
    List<CurrencyConversion> findByTenantId(String tenantId);

    /**
     * Find conversions by tenant ID and status
     */
    List<CurrencyConversion> findByTenantIdAndStatus(String tenantId, CurrencyConversion.ConversionStatus status);

    /**
     * Find conversions by tenant ID and currency pair
     */
    List<CurrencyConversion> findByTenantIdAndFromCurrencyAndToCurrency(
        String tenantId, String fromCurrency, String toCurrency);

    /**
     * Find conversions by tenant ID within a date range
     */
    List<CurrencyConversion> findByTenantIdAndConvertedAtBetween(
        String tenantId, Instant startDate, Instant endDate);

    /**
     * Find conversions by reference ID and tenant
     */
    List<CurrencyConversion> findByReferenceIdAndTenantId(String referenceId, String tenantId);

    /**
     * Find conversions for a tenant with pagination
     */
    List<CurrencyConversion> findByTenantIdOrderByConvertedAtDesc(
        String tenantId, int page, int size);

    /**
     * Count conversions by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count conversions by tenant and status
     */
    long countByTenantIdAndStatus(String tenantId, CurrencyConversion.ConversionStatus status);

    /**
     * Calculate total converted amount for a tenant and currency pair
     */
    java.math.BigDecimal sumConvertedAmountByTenantAndCurrencies(
        String tenantId, String fromCurrency, String toCurrency);
}
