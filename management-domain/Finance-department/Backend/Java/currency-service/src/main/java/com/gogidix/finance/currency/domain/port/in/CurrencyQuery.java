package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.Currency;

import java.util.List;
import java.util.Optional;

/**
 * Input Port - Currency Query
 * Defines queries for currency lookups
 * Following hexagonal architecture principles
 */
public interface CurrencyQuery {

    /**
     * Get currency by ID
     */
    Optional<Currency> getById(GetCurrencyByIdQuery query);

    /**
     * Get currency by code
     */
    Optional<Currency> getByCode(GetCurrencyByCodeQuery query);

    /**
     * Get all currencies for a tenant
     */
    List<Currency> getAllForTenant(GetAllCurrenciesQuery query);

    /**
     * Get active currencies for a tenant
     */
    List<Currency> getActiveForTenant(GetActiveCurrenciesQuery query);

    /**
     * Check if currency exists
     */
    boolean exists(CurrencyExistsQuery query);

    /**
     * Query object for getting currency by ID
     */
    record GetCurrencyByIdQuery(
        String tenantId,
        String currencyId
    ) {}

    /**
     * Query object for getting currency by code
     */
    record GetCurrencyByCodeQuery(
        String tenantId,
        String currencyCode
    ) {}

    /**
     * Query object for getting all currencies
     */
    record GetAllCurrenciesQuery(
        String tenantId
    ) {}

    /**
     * Query object for getting active currencies
     */
    record GetActiveCurrenciesQuery(
        String tenantId
    ) {}

    /**
     * Query object for checking currency existence
     */
    record CurrencyExistsQuery(
        String tenantId,
        String currencyCode
    ) {}
}
