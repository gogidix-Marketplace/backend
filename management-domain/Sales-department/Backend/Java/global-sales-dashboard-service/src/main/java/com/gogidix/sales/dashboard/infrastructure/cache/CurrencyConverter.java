package com.gogidix.sales.dashboard.infrastructure.cache;

import com.gogidix.sales.dashboard.domain.valueobject.CurrencyConversion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Currency Converter Service
 * Handles currency conversion for global sales reporting
 */
@Service
@Slf4j
public class CurrencyConverter {

    @Value("${global-sales-dashboard-service.currency.exchange-rate-api:https://api.exchangerate.host/latest}")
    private String exchangeRateApi;

    @Value("${global-sales-dashboard-service.currency.cache-duration:3600}")
    private long cacheDurationSeconds;

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, ExchangeRate> rateCache = new HashMap<>();

    /**
     * Converts amount from one currency to another
     */
    @Cacheable(value = "currencyRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Gets exchange rate between two currencies
     */
    @Cacheable(value = "currencyRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        // Check cache first
        String cacheKey = fromCurrency + "-" + toCurrency;
        ExchangeRate cachedRate = rateCache.get(cacheKey);

        if (cachedRate != null && !cachedRate.isExpired()) {
            return cachedRate.getRate();
        }

        // Fetch from API or use mock rates
        BigDecimal rate = fetchExchangeRate(fromCurrency, toCurrency);

        // Cache the rate
        rateCache.put(cacheKey, new ExchangeRate(rate, Instant.now().plusSeconds(cacheDurationSeconds)));

        return rate;
    }

    /**
     * Gets all exchange rates for a base currency
     */
    public Map<String, BigDecimal> getAllExchangeRates(String baseCurrency) {
        Map<String, BigDecimal> rates = new HashMap<>();

        for (String currency : CurrencyConversion.getSupportedCurrencies()) {
            if (!currency.equals(baseCurrency)) {
                try {
                    BigDecimal rate = getExchangeRate(baseCurrency, currency);
                    rates.put(currency, rate);
                } catch (Exception e) {
                    log.warn("Failed to get exchange rate for {} to {}", baseCurrency, currency);
                }
            }
        }

        return rates;
    }

    /**
     * Refreshes exchange rates from external API
     */
    public void refreshExchangeRates() {
        log.info("Refreshing exchange rates");

        try {
            // Clear cache to force fresh lookup
            rateCache.clear();

            // Pre-warm cache for common currencies
            String[] commonCurrencies = {"USD", "EUR", "GBP", "JPY"};
            for (String from : commonCurrencies) {
                for (String to : commonCurrencies) {
                    if (!from.equals(to)) {
                        getExchangeRate(from, to);
                    }
                }
            }

            log.info("Exchange rates refreshed successfully");
        } catch (Exception e) {
            log.error("Failed to refresh exchange rates", e);
        }
    }

    /**
     * Converts Money object to different currency
     */
    public CurrencyConversion.Money convertCurrency(CurrencyConversion.Money money, String targetCurrency) {
        if (money.getCurrency().equals(targetCurrency)) {
            return money;
        }

        BigDecimal convertedAmount = convert(money.getAmount(), money.getCurrency(), targetCurrency);

        return CurrencyConversion.Money.builder()
                .amount(convertedAmount)
                .currency(targetCurrency)
                .build();
    }

    /**
     * Formats money amount with currency symbol
     */
    public String formatMoney(BigDecimal amount, String currency) {
        return getCurrencySymbol(currency) + " " + amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Gets currency symbol
     */
    public String getCurrencySymbol(String currencyCode) {
        return switch (currencyCode) {
            case "USD" -> "$";
            case "EUR" -> "\u20AC";
            case "GBP" -> "\u00A3";
            case "JPY" -> "\u00A5";
            case "CNY" -> "\u00A5";
            case "INR" -> "\u20B9";
            case "KRW" -> "\u20A9";
            default -> currencyCode + " ";
        };
    }

    private BigDecimal fetchExchangeRate(String fromCurrency, String toCurrency) {
        try {
            // Try to get from API
            String url = exchangeRateApi + "?base=" + fromCurrency + "&symbols=" + toCurrency;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("rates")) {
                Map<String, Object> rates = (Map<String, Object>) response.get("rates");
                if (rates.containsKey(toCurrency)) {
                    Object rate = rates.get(toCurrency);
                    if (rate instanceof Number) {
                        return new BigDecimal(rate.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch exchange rate from API, using mock rates: {}", e.getMessage());
        }

        // Fallback to mock rates
        String rateKey = fromCurrency + "-" + toCurrency;
        return CurrencyConversion.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .build()
                .getExchangeRate(fromCurrency, toCurrency);
    }

    private static class ExchangeRate {
        private final BigDecimal rate;
        private final Instant expiresAt;

        public ExchangeRate(BigDecimal rate, Instant expiresAt) {
            this.rate = rate;
            this.expiresAt = expiresAt;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
