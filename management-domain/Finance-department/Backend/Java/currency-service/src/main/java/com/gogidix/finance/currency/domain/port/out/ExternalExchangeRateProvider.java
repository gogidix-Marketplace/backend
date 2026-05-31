package com.gogidix.finance.currency.domain.port.out;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Output Port - External Exchange Rate Provider
 * Interface for fetching exchange rates from external sources
 * Following hexagonal architecture principles
 */
public interface ExternalExchangeRateProvider {

    /**
     * Get current exchange rate for a currency pair
     */
    Optional<ExchangeRateDto> getExchangeRate(String baseCurrency, String quoteCurrency);

    /**
     * Get multiple exchange rates
     */
    Map<String, BigDecimal> getExchangeRates(String baseCurrency, String[] quoteCurrencies);

    /**
     * Check if provider is available
     */
    boolean isAvailable();

    /**
     * Get provider name
     */
    String getProviderName();

    /**
     * DTO for exchange rate data
     */
    record ExchangeRateDto(
        String baseCurrency,
        String quoteCurrency,
        BigDecimal rate,
        Instant timestamp,
        String source
    ) {}
}
