package com.gogidix.finance.conversion.domain.repository;

import com.gogidix.finance.conversion.domain.model.ConversionRate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Conversion Rate Repository Interface (Port)
 * Defines the contract for conversion rate cache persistence operations
 */
public interface ConversionRateRepository {

    ConversionRate save(ConversionRate rate);

    List<ConversionRate> saveAll(List<ConversionRate> rates);

    Optional<ConversionRate> findById(String id);

    Optional<ConversionRate> findByRateKey(String rateKey);

    Optional<ConversionRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);

    List<ConversionRate> findByFromCurrency(String fromCurrency);

    List<ConversionRate> findByToCurrency(String toCurrency);

    List<ConversionRate> findByProvider(String provider);

    List<ConversionRate> findExpiredRates(Instant now);

    List<ConversionRate> findRatesExpiringBefore(Instant threshold);

    List<ConversionRate> findAll();

    boolean existsByRateKey(String rateKey);

    void deleteById(String id);

    void deleteByRateKey(String rateKey);

    void deleteExpiredRates(Instant before);

    void deleteAll();

    long count();

    long countByProvider(String provider);

    List<ConversionRate> findByFromCurrencyAndToCurrencyIn(
            String fromCurrency, List<String> toCurrencies);

    /**
     * Finds rates that need to be refreshed (expiring soon or expired)
     */
    List<ConversionRate> findRatesNeedingRefresh(Instant threshold);

    /**
     * Updates the hit count for a rate
     */
    void incrementHitCount(String rateKey);

    /**
     * Finds all rates for a given currency pair
     */
    List<ConversionRate> findAllByCurrencyPair(String fromCurrency, String toCurrency);
}
