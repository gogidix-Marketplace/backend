package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.ExchangeRate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Input Port - Exchange Rate Query
 * Defines queries for exchange rate lookups
 * Following hexagonal architecture principles
 */
public interface ExchangeRateQuery {

    /**
     * Get current exchange rate for a currency pair
     */
    Optional<ExchangeRate> getCurrentRate(GetCurrentRateQuery query);

    /**
     * Get exchange rate at a specific point in time
     */
    Optional<ExchangeRate> getRateAtTime(GetRateAtTimeQuery query);

    /**
     * Get all exchange rates for a tenant
     */
    List<ExchangeRate> getAllForTenant(GetAllRatesQuery query);

    /**
     * Get all rates for a specific currency
     */
    List<ExchangeRate> getRatesForCurrency(GetRatesForCurrencyQuery query);

    /**
     * Get all rates for a currency pair
     */
    List<ExchangeRate> getRatesForPair(GetRatesForPairQuery query);

    /**
     * Get latest rates for all pairs
     */
    List<ExchangeRate> getLatestRates(GetLatestRatesQuery query);

    /**
     * Query object for getting current rate
     */
    record GetCurrentRateQuery(
        String tenantId,
        String baseCurrency,
        String quoteCurrency
    ) {}

    /**
     * Query object for getting rate at time
     */
    record GetRateAtTimeQuery(
        String tenantId,
        String baseCurrency,
        String quoteCurrency,
        Instant at
    ) {}

    /**
     * Query object for getting all rates
     */
    record GetAllRatesQuery(
        String tenantId
    ) {}

    /**
     * Query object for getting rates for currency
     */
    record GetRatesForCurrencyQuery(
        String tenantId,
        String currency
    ) {}

    /**
     * Query object for getting rates for pair
     */
    record GetRatesForPairQuery(
        String tenantId,
        String baseCurrency,
        String quoteCurrency
    ) {}

    /**
     * Query object for getting latest rates
     */
    record GetLatestRatesQuery(
        String tenantId
    ) {}
}
