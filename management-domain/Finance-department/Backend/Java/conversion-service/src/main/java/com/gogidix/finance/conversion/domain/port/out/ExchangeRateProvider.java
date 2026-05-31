package com.gogidix.finance.conversion.domain.port.out;

import java.util.List;

import java.time.Instant;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Exchange Rate Provider Interface (Port)
 * Defines the contract for fetching exchange rates from external sources
 */
public interface ExchangeRateProvider {

    /**
     * Gets the current exchange rate between two currencies
     *
     * @param fromCurrency Source currency code (e.g., "USD")
     * @param toCurrency   Target currency code (e.g., "EUR")
     * @return The exchange rate
     */
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);

    /**
     * Gets multiple exchange rates at once
     *
     * @param baseCurrency Base currency code
     * @param targetCurrencies List of target currency codes
     * @return Map of currency pairs to rates
     */
    Map<String, BigDecimal> getExchangeRates(String baseCurrency, java.util.List<String> targetCurrencies);

    /**
     * Gets the exchange rate with additional metadata
     *
     * @param fromCurrency Source currency code
     * @param toCurrency   Target currency code
     * @return Exchange rate response with metadata
     */
    ExchangeRateResponse getExchangeRateWithMetadata(String fromCurrency, String toCurrency);

    /**
     * Checks if the provider is available
     *
     * @return true if the provider is available
     */
    boolean isAvailable();

    /**
     * Gets the provider name
     *
     * @return Provider name
     */
    String getProviderName();

    /**
     * Exchange Rate Response with metadata
     */
    class ExchangeRateResponse {
        private final BigDecimal rate;
        private final BigDecimal bidPrice;
        private final BigDecimal askPrice;
        private final String provider;
        private final java.time.Instant timestamp;
        private final Long ttlSeconds;

        public ExchangeRateResponse(BigDecimal rate, BigDecimal bidPrice, BigDecimal askPrice,
                                    String provider, java.time.Instant timestamp, Long ttlSeconds) {
            this.rate = rate;
            this.bidPrice = bidPrice;
            this.askPrice = askPrice;
            this.provider = provider;
            this.timestamp = timestamp;
            this.ttlSeconds = ttlSeconds;
        }

        public BigDecimal getRate() { return rate; }
        public BigDecimal getBidPrice() { return bidPrice; }
        public BigDecimal getAskPrice() { return askPrice; }
        public String getProvider() { return provider; }
        public java.time.Instant getTimestamp() { return timestamp; }
        public Long getTtlSeconds() { return ttlSeconds; }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private BigDecimal rate;
            private BigDecimal bidPrice;
            private BigDecimal askPrice;
            private String provider;
            private java.time.Instant timestamp;
            private Long ttlSeconds;

            public Builder rate(BigDecimal rate) { this.rate = rate; return this; }
            public Builder bidPrice(BigDecimal bidPrice) { this.bidPrice = bidPrice; return this; }
            public Builder askPrice(BigDecimal askPrice) { this.askPrice = askPrice; return this; }
            public Builder provider(String provider) { this.provider = provider; return this; }
            public Builder timestamp(java.time.Instant timestamp) { this.timestamp = timestamp; return this; }
            public Builder ttlSeconds(Long ttlSeconds) { this.ttlSeconds = ttlSeconds; return this; }

            public ExchangeRateResponse build() {
                return new ExchangeRateResponse(rate, bidPrice, askPrice, provider, timestamp, ttlSeconds);
            }
        }
    }
}
