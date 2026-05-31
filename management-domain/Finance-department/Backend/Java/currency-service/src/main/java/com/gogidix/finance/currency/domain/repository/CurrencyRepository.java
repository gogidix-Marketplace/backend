package com.gogidix.finance.currency.domain.repository;

import com.gogidix.finance.currency.domain.model.Currency;

import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface - Currency
 * Port for currency persistence operations
 * Following hexagonal architecture principles
 */
public interface CurrencyRepository {

    /**
     * Save a currency entity
     */
    Currency save(Currency currency);

    /**
     * Find currency by ID and tenant ID
     * Mandatory tenant filtering for multi-tenancy
     */
    Optional<Currency> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find currency by code and tenant ID
     */
    Optional<Currency> findByCurrencyCodeAndTenantId(String currencyCode, String tenantId);

    /**
     * Find all currencies for a tenant
     */
    List<Currency> findByTenantId(String tenantId);

    /**
     * Find all currencies by status for a tenant
     */
    List<Currency> findByTenantIdAndStatus(String tenantId, Currency.CurrencyStatus status);

    /**
     * Find all active currencies for a tenant
     */
    List<Currency> findByTenantIdAndActive(String tenantId, boolean active);

    /**
     * Check if currency exists by code and tenant
     */
    boolean existsByCurrencyCodeAndTenantId(String currencyCode, String tenantId);

    /**
     * Delete currency by ID and tenant ID
     */
    void deleteByIdAndTenantId(String id, String tenantId);

    /**
     * Find all currencies (admin only - requires tenant filter in implementation)
     */
    List<Currency> findAll();

    /**
     * Count currencies by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Find currencies by country code
     */
    List<Currency> findByTenantIdAndCountryCodesContaining(String tenantId, String countryCode);
}
