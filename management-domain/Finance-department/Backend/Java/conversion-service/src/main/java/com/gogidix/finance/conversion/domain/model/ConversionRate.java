package com.gogidix.finance.conversion.domain.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.gogidix.finance.conversion.shared.base.BaseEntity;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Conversion Rate Cache Entity
 * Caches exchange rates to reduce external API calls
 */
@EqualsAndHashCode(callSuper = true)
@Document(collection = "conversion_rates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRate extends BaseEntity {

    @Indexed(unique = true)
    private String rateKey;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private BigDecimal inverseRate;
    private Instant fetchedAt;
    private Instant expiresAt;
    private String provider;
    private Long ttlSeconds;
    private Integer hitCount;
    private BigDecimal bidPrice;
    private BigDecimal askPrice;
    private BigDecimal midPrice;
    private Instant lastUpdatedAt;

    public static ConversionRateBuilder builder() {
        return new ConversionRateBuilder();
    }

    /**
     * Cache TTL in seconds (default 5 minutes)
     */
    public static final long DEFAULT_TTL_SECONDS = 300;

    /**
     * Creates a new cached rate
     */
    public static ConversionRate create(String fromCurrency, String toCurrency,
                                        BigDecimal rate, String provider) {
        String rateKey = fromCurrency + "-" + toCurrency;

        return ConversionRate.builder()
                .rateKey(rateKey)
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .rate(rate)
                .inverseRate(BigDecimal.ONE.divide(rate, 10, java.math.RoundingMode.HALF_UP))
                .fetchedAt(Instant.now())
                .expiresAt(Instant.now().plus(DEFAULT_TTL_SECONDS, ChronoUnit.SECONDS))
                .provider(provider)
                .ttlSeconds(DEFAULT_TTL_SECONDS)
                .hitCount(0)
                .lastUpdatedAt(Instant.now())
                .build();
    }

    /**
     * Checks if the cached rate is still valid
     */
    public boolean isValid() {
        return Instant.now().isBefore(this.expiresAt);
    }

    /**
     * Checks if the rate is expiring soon (within 30 seconds)
     */
    public boolean isExpiringSoon() {
        Instant soon = Instant.now().plusSeconds(30);
        return this.expiresAt.isBefore(soon);
    }

    /**
     * Refreshes the rate with new data
     */
    public void refresh(BigDecimal newRate, String provider) {
        this.rate = newRate;
        this.inverseRate = BigDecimal.ONE.divide(newRate, 10, java.math.RoundingMode.HALF_UP);
        this.provider = provider;
        this.fetchedAt = Instant.now();
        this.expiresAt = Instant.now().plus(this.ttlSeconds, ChronoUnit.SECONDS);
        this.lastUpdatedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Increments the hit counter
     */
    public void incrementHitCount() {
        this.hitCount = (this.hitCount != null ? this.hitCount : 0) + 1;
    }

    /**
     * Sets bid/ask prices for forex trading
     */
    public void setBidAskPrices(BigDecimal bidPrice, BigDecimal askPrice) {
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        if (bidPrice != null && askPrice != null) {
            this.midPrice = bidPrice.add(askPrice)
                    .divide(BigDecimal.valueOf(2), 10, java.math.RoundingMode.HALF_UP);
        }
        this.lastUpdatedAt = Instant.now();
    }

    /**
     * Gets the effective rate (mid price if available, otherwise base rate)
     */
    public BigDecimal getEffectiveRate() {
        return this.midPrice != null ? this.midPrice : this.rate;
    }

    /**
     * Calculates the time remaining until expiry in seconds
     */
    public long getRemainingTtlSeconds() {
        if (this.expiresAt == null) {
            return 0;
        }
        long remaining = this.expiresAt.toEpochMilli() - Instant.now().toEpochMilli();
        return Math.max(0, remaining / 1000);
    }

    /**
     * Checks if this rate needs to be refreshed
     */
    public boolean needsRefresh() {
        return isExpiringSoon() || !isValid();
    }

    /**
     * Custom builder for ConversionRate
     */
    public static class ConversionRateBuilder {
        private String rateKey;
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal rate;
        private BigDecimal inverseRate;
        private Instant fetchedAt;
        private Instant expiresAt;
        private String provider;
        private Long ttlSeconds;
        private Integer hitCount;
        private BigDecimal bidPrice;
        private BigDecimal askPrice;
        private BigDecimal midPrice;
        private Instant lastUpdatedAt;

        public ConversionRateBuilder rateKey(String rateKey) {
            this.rateKey = rateKey;
            return this;
        }

        public ConversionRateBuilder fromCurrency(String fromCurrency) {
            this.fromCurrency = fromCurrency;
            return this;
        }

        public ConversionRateBuilder toCurrency(String toCurrency) {
            this.toCurrency = toCurrency;
            return this;
        }

        public ConversionRateBuilder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public ConversionRateBuilder inverseRate(BigDecimal inverseRate) {
            this.inverseRate = inverseRate;
            return this;
        }

        public ConversionRateBuilder fetchedAt(Instant fetchedAt) {
            this.fetchedAt = fetchedAt;
            return this;
        }

        public ConversionRateBuilder expiresAt(Instant expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public ConversionRateBuilder provider(String provider) {
            this.provider = provider;
            return this;
        }

        public ConversionRateBuilder ttlSeconds(Long ttlSeconds) {
            this.ttlSeconds = ttlSeconds;
            return this;
        }

        public ConversionRateBuilder hitCount(Integer hitCount) {
            this.hitCount = hitCount;
            return this;
        }

        public ConversionRateBuilder bidPrice(BigDecimal bidPrice) {
            this.bidPrice = bidPrice;
            return this;
        }

        public ConversionRateBuilder askPrice(BigDecimal askPrice) {
            this.askPrice = askPrice;
            return this;
        }

        public ConversionRateBuilder midPrice(BigDecimal midPrice) {
            this.midPrice = midPrice;
            return this;
        }

        public ConversionRateBuilder lastUpdatedAt(Instant lastUpdatedAt) {
            this.lastUpdatedAt = lastUpdatedAt;
            return this;
        }

        public ConversionRate build() {
            ConversionRate rate = new ConversionRate();
            rate.rateKey = this.rateKey;
            rate.fromCurrency = this.fromCurrency;
            rate.toCurrency = this.toCurrency;
            rate.rate = this.rate;
            rate.inverseRate = this.inverseRate;
            rate.fetchedAt = this.fetchedAt;
            rate.expiresAt = this.expiresAt;
            rate.provider = this.provider;
            rate.ttlSeconds = this.ttlSeconds;
            rate.hitCount = this.hitCount;
            rate.bidPrice = this.bidPrice;
            rate.askPrice = this.askPrice;
            rate.midPrice = this.midPrice;
            rate.lastUpdatedAt = this.lastUpdatedAt;
            return rate;
        }
    }
}
