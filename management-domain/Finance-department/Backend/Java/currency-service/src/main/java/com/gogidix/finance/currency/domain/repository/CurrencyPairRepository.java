package com.gogidix.finance.currency.domain.repository;

import com.gogidix.finance.currency.domain.model.CurrencyPair;

import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface - Currency Pair
 * Port for currency pair persistence operations
 * Following hexagonal architecture principles
 */
public interface CurrencyPairRepository {

    /**
     * Save a currency pair entity
     */
    CurrencyPair save(CurrencyPair pair);

    /**
     * Find currency pair by ID and tenant ID
     * Mandatory tenant filtering for multi-tenancy
     */
    Optional<CurrencyPair> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find currency pair by base and quote currencies
     */
    Optional<CurrencyPair> findByBaseAndQuote(String baseCurrency, String quoteCurrency, String tenantId);

    /**
     * Find all currency pairs for a tenant
     */
    List<CurrencyPair> findByTenantId(String tenantId);

    /**
     * Find all pairs involving a specific currency
     */
    List<CurrencyPair> findByTenantIdAndCurrency(String tenantId, String currencyCode);

    /**
     * Check if currency pair exists
     */
    boolean existsByBaseAndQuote(String baseCurrency, String quoteCurrency, String tenantId);

    /**
     * Delete currency pair by ID and tenant ID
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Count currency pairs by tenant
     */
    long countByTenantId(String tenantId);
}
