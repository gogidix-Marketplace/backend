package com.gogidix.finance.currency.domain.repository;

import com.gogidix.finance.currency.domain.model.ExchangeRate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface - Exchange Rate
 * Port for exchange rate persistence operations
 * Following hexagonal architecture principles
 */
public interface ExchangeRateRepository {

    /**
     * Save an exchange rate entity
     */
    ExchangeRate save(ExchangeRate rate);

    /**
     * Find exchange rate by ID and tenant ID
     */
    Optional<ExchangeRate> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find current exchange rate for a currency pair and tenant
     */
    Optional<ExchangeRate> findByTenantIdAndBaseCurrencyAndQuoteCurrencyAndValidToIsNull(
        String tenantId, String baseCurrency, String quoteCurrency);

    /**
     * Find current exchange rate valid at a specific time
     */
    Optional<ExchangeRate> findByTenantIdAndCurrencyPairAndValidAt(
        String tenantId, String baseCurrency, String quoteCurrency, Instant at);

    /**
     * Find all exchange rates for a tenant
     */
    List<ExchangeRate> findByTenantId(String tenantId);

    /**
     * Find all exchange rates for a specific currency (as base or quote)
     */
    List<ExchangeRate> findByTenantIdAndCurrency(String tenantId, String currency);

    /**
     * Find all exchange rates for a currency pair
     */
    List<ExchangeRate> findByTenantIdAndBaseCurrencyAndQuoteCurrency(
        String tenantId, String baseCurrency, String quoteCurrency);

    /**
     * Find all exchange rates updated after a timestamp
     */
    List<ExchangeRate> findByTenantIdAndUpdatedAtAfter(String tenantId, Instant timestamp);

    /**
     * Find latest exchange rates for all pairs for a tenant
     */
    List<ExchangeRate> findLatestRatesByTenantId(String tenantId);

    /**
     * Delete exchange rate by ID and tenant ID
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Count exchange rates by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Invalidate all current rates for a currency pair
     */
    void invalidateCurrentRates(String tenantId, String baseCurrency, String quoteCurrency);
}
