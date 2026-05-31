package com.gogidix.finance.conversion.domain.repository;

import com.gogidix.finance.conversion.domain.model.CurrencyConversion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Currency Conversion Repository Interface (Port)
 * Defines the contract for currency conversion persistence operations
 */
public interface CurrencyConversionRepository {

    CurrencyConversion save(CurrencyConversion conversion);

    List<CurrencyConversion> saveAll(List<CurrencyConversion> conversions);

    Optional<CurrencyConversion> findById(String id);

    Optional<CurrencyConversion> findByConversionIdAndTenantId(String conversionId, String tenantId);

    List<CurrencyConversion> findByTenantId(String tenantId);

    List<CurrencyConversion> findByTenantIdAndRequestedBy(String tenantId, String requestedBy);

    List<CurrencyConversion> findByTenantIdAndFromCurrencyAndToCurrency(
            String tenantId, String fromCurrency, String toCurrency);

    List<CurrencyConversion> findByTenantIdAndConversionDateBetween(
            String tenantId, Instant startDate, Instant endDate);

    List<CurrencyConversion> findByTenantIdAndStatus(
            String tenantId, CurrencyConversion.ConversionStatus status);

    List<CurrencyConversion> findByTenantIdAndStatusIn(
            String tenantId, List<CurrencyConversion.ConversionStatus> statuses);

    List<CurrencyConversion> findByTenantIdAndProvider(
            String tenantId, String provider);

    boolean existsByConversionIdAndTenantId(String conversionId, String tenantId);

    void deleteById(String id);

    void deleteByConversionIdAndTenantId(String conversionId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CurrencyConversion.ConversionStatus status);

    java.math.BigDecimal sumAmountByTenantIdAndStatus(
            String tenantId, CurrencyConversion.ConversionStatus status);

    java.math.BigDecimal sumConvertedAmountByTenantIdAndCurrenciesAndDateBetween(
            String tenantId, String fromCurrency, String toCurrency, Instant startDate, Instant endDate);

    List<CurrencyConversion> findByTenantIdAndCorrelationId(
            String tenantId, String correlationId);

    Optional<CurrencyConversion> findLatestByTenantIdAndCurrencies(
            String tenantId, String fromCurrency, String toCurrency);
}
