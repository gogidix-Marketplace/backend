package com.gogidix.finance.conversion.infrastructure.provider;

import com.gogidix.finance.conversion.domain.port.out.ExchangeRateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * External Exchange Rate Provider
 * Integrates with external exchange rate APIs
 * Supports multiple providers with fallback capability
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExternalExchangeRateProvider implements ExchangeRateProvider {

    private final RestTemplate restTemplate;

    @Value("${conversion.provider.api-key:}")
    private String apiKey;

    @Value("${conversion.provider.api-url:https://api.exchangerate.host/latest}")
    private String apiUrl;

    @Value("${conversion.provider.name:ExchangeRateHost}")
    private String providerName;

    @Value("${conversion.provider.cache-ttl-seconds:300}")
    private long cacheTtlSeconds;

    @Value("${conversion.provider.fallback-enabled:true}")
    private boolean fallbackEnabled;

    private static final Map<String, BigDecimal> FALLBACK_RATES = new HashMap<>();

    static {
        // Fallback rates for emergency use (base: USD)
        FALLBACK_RATES.put("USD-EUR", new BigDecimal("0.92"));
        FALLBACK_RATES.put("USD-GBP", new BigDecimal("0.79"));
        FALLBACK_RATES.put("USD-JPY", new BigDecimal("149.50"));
        FALLBACK_RATES.put("USD-CHF", new BigDecimal("0.88"));
        FALLBACK_RATES.put("USD-CAD", new BigDecimal("1.36"));
        FALLBACK_RATES.put("USD-AUD", new BigDecimal("1.53"));
        FALLBACK_RATES.put("EUR-USD", new BigDecimal("1.09"));
        FALLBACK_RATES.put("GBP-USD", new BigDecimal("1.27"));
        FALLBACK_RATES.put("JPY-USD", new BigDecimal("0.0067"));
        FALLBACK_RATES.put("CHF-USD", new BigDecimal("1.14"));
        FALLBACK_RATES.put("CAD-USD", new BigDecimal("0.74"));
        FALLBACK_RATES.put("AUD-USD", new BigDecimal("0.65"));
    }

    @Override
    @Cacheable(value = "externalExchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        log.debug("Fetching exchange rate: {} to {} from {}", fromCurrency, toCurrency, providerName);

        try {
            ExchangeRateResponse response = getExchangeRateWithMetadata(fromCurrency, toCurrency);
            return response.getRate();

        } catch (Exception e) {
            log.error("Failed to fetch exchange rate from {}: {}", providerName, e.getMessage());

            if (fallbackEnabled) {
                log.warn("Using fallback rate for: {} to {}", fromCurrency, toCurrency);
                return getFallbackRate(fromCurrency, toCurrency);
            }

            throw new RuntimeException("Unable to fetch exchange rate", e);
        }
    }

    @Override
    public Map<String, BigDecimal> getExchangeRates(String baseCurrency, List<String> targetCurrencies) {
        log.debug("Fetching multiple exchange rates for base: {} from {}", baseCurrency, providerName);

        Map<String, BigDecimal> rates = new HashMap<>();

        try {
            String url = String.format("%s?base=%s&symbols=%s",
                    apiUrl, baseCurrency, String.join(",", targetCurrencies));

            if (!apiKey.isBlank()) {
                url += "&access_key=" + apiKey;
            }

            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && response.getRates() != null) {
                for (String targetCurrency : targetCurrencies) {
                    BigDecimal rate = response.getRates().get(targetCurrency);
                    if (rate != null) {
                        rates.put(baseCurrency + "-" + targetCurrency, rate);
                    }
                }
            }

        } catch (Exception e) {
            log.error("Failed to fetch batch exchange rates: {}", e.getMessage());

            if (fallbackEnabled) {
                for (String targetCurrency : targetCurrencies) {
                    rates.put(baseCurrency + "-" + targetCurrency,
                            getFallbackRate(baseCurrency, targetCurrency));
                }
            }
        }

        return rates;
    }

    @Override
    public ExchangeRateResponse getExchangeRateWithMetadata(String fromCurrency, String toCurrency) {
        log.debug("Fetching exchange rate with metadata: {} to {}", fromCurrency, toCurrency);

        try {
            String url = String.format("%s?base=%s&symbols=%s",
                    apiUrl, fromCurrency, toCurrency);

            if (!apiKey.isBlank()) {
                url += "&access_key=" + apiKey;
            }

            ApiResponse response = restTemplate.getForObject(url, ApiResponse.class);

            if (response != null && response.getRates() != null) {
                BigDecimal rate = response.getRates().get(toCurrency);

                if (rate == null) {
                    throw new IllegalArgumentException("Invalid currency pair: " + fromCurrency + "/" + toCurrency);
                }

                return ExchangeRateResponse.builder()
                        .rate(rate)
                        .provider(providerName)
                        .timestamp(java.time.Instant.now())
                        .ttlSeconds(cacheTtlSeconds)
                        .build();
            }

            throw new RuntimeException("Invalid response from exchange rate provider");

        } catch (Exception e) {
            log.error("Failed to fetch exchange rate with metadata: {}", e.getMessage());

            if (fallbackEnabled) {
                BigDecimal fallbackRate = getFallbackRate(fromCurrency, toCurrency);
                return ExchangeRateResponse.builder()
                        .rate(fallbackRate)
                        .provider(providerName + "-FALLBACK")
                        .timestamp(java.time.Instant.now())
                        .ttlSeconds(60L)
                        .build();
            }

            throw new RuntimeException("Unable to fetch exchange rate with metadata", e);
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            String url = apiUrl + "?base=USD&symbols=EUR";
            if (!apiKey.isBlank()) {
                url += "&access_key=" + apiKey;
            }
            restTemplate.getForObject(url, ApiResponse.class);
            return true;
        } catch (Exception e) {
            log.warn("Exchange rate provider not available: {}", e.getMessage());
            return fallbackEnabled; // Consider available if fallback is enabled
        }
    }

    @Override
    public String getProviderName() {
        return providerName;
    }

    /**
     * Gets fallback rate for emergency use
     */
    private BigDecimal getFallbackRate(String fromCurrency, String toCurrency) {
        String key = fromCurrency + "-" + toCurrency;
        BigDecimal rate = FALLBACK_RATES.get(key);

        if (rate == null) {
            // Try to compute via USD as base
            BigDecimal fromUsd = FALLBACK_RATES.get(fromCurrency + "-USD");
            BigDecimal toUsd = FALLBACK_RATES.get(toCurrency + "-USD");

            if (fromUsd != null && toUsd != null) {
                // Convert: fromCurrency -> USD -> toCurrency
                rate = toUsd.divide(fromUsd, 6, java.math.RoundingMode.HALF_UP);
            } else {
                throw new IllegalArgumentException(
                        "No fallback rate available for: " + fromCurrency + "/" + toCurrency);
            }
        }

        return rate;
    }

    /**
     * API Response wrapper
     */
    private static class ApiResponse {
        private boolean success;
        private String base;
        private java.time.Instant date;
        private Map<String, BigDecimal> rates;
        private Error error;

        public boolean isSuccess() { return success; }
        public String getBase() { return base; }
        public java.time.Instant getDate() { return date; }
        public Map<String, BigDecimal> getRates() { return rates; }
        public Error getError() { return error; }

        public void setSuccess(boolean success) { this.success = success; }
        public void setBase(String base) { this.base = base; }
        public void setDate(java.time.Instant date) { this.date = date; }
        public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }
        public void setError(Error error) { this.error = error; }

        private static class Error {
            private int code;
            private String type;
            private String info;

            public int getCode() { return code; }
            public String getType() { return type; }
            public String getInfo() { return info; }
        }
    }
}
