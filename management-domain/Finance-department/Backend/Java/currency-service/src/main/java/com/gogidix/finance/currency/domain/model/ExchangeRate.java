package com.gogidix.finance.currency.domain.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity - Exchange Rate
 * Multi-tenant SaaS entity with mandatory tenantId field
 */
@Document(collection = "exchange_rates")
@CompoundIndex(name = "tenant_currency_pair_idx", def = "{'tenantId': 1, 'baseCurrency': 1, 'quoteCurrency': 1}", unique = true)
public class ExchangeRate extends BaseEntity {

    @Indexed
    @Field("base_currency")
    private String baseCurrency;

    @Indexed
    @Field("quote_currency")
    private String quoteCurrency;

    @Field("rate")
    private BigDecimal rate;

    @Field("inverse_rate")
    private BigDecimal inverseRate;

    @Field("valid_from")
    private Instant validFrom;

    @Field("valid_to")
    private Instant validTo;

    @Field("source")
    private String source;

    @Field("quality")
    private RateQuality quality;

    public enum RateQuality {
        REAL_TIME,
        DAILY,
        ESTIMATED,
        HISTORICAL
    }

    protected ExchangeRate() {
    }

    private ExchangeRate(Builder builder) {
        super(builder.tenantId);
        this.baseCurrency = Objects.requireNonNull(builder.baseCurrency, "baseCurrency is required");
        this.quoteCurrency = Objects.requireNonNull(builder.quoteCurrency, "quoteCurrency is required");
        setRate(builder.rate);
        this.validFrom = builder.validFrom != null ? builder.validFrom : Instant.now();
        this.validTo = builder.validTo;
        this.source = builder.source;
        this.quality = builder.quality != null ? builder.quality : RateQuality.DAILY;
    }

    public void setRate(BigDecimal newRate) {
        if (newRate == null || newRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
        this.rate = newRate.setScale(10, RoundingMode.HALF_UP);
        this.inverseRate = BigDecimal.ONE.divide(this.rate, 10, RoundingMode.HALF_UP);
        this.updateTimestamp();
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (fromCurrency.equals(baseCurrency)) {
            return amount.multiply(rate).setScale(10, RoundingMode.HALF_UP);
        } else if (fromCurrency.equals(quoteCurrency)) {
            return amount.multiply(inverseRate).setScale(10, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Currency mismatch: " + fromCurrency);
        }
    }

    public boolean isValid() {
        Instant now = Instant.now();
        return (validFrom == null || !validFrom.isAfter(now)) &&
               (validTo == null || !validTo.isBefore(now));
    }

    public void invalidate() {
        this.validTo = Instant.now();
        this.updateTimestamp();
    }

    public String getCurrencyPair() {
        return baseCurrency + quoteCurrency;
    }

    public static ExchangeRate create(String tenantId, String fromCurrency, String toCurrency, BigDecimal rate, String source) {
        return ExchangeRate.builder()
                .tenantId(tenantId)
                .baseCurrency(fromCurrency)
                .quoteCurrency(toCurrency)
                .rate(rate)
                .source(source)
                .build();
    }

    public String getFromCurrency() {
        return baseCurrency;
    }

    public String getToCurrency() {
        return quoteCurrency;
    }

    public String getExchangeRateId() {
        return id;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getInverseRate() {
        return inverseRate;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public String getSource() {
        return source;
    }

    public RateQuality getQuality() {
        return quality;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String baseCurrency;
        private String quoteCurrency;
        private BigDecimal rate;
        private Instant validFrom;
        private Instant validTo;
        private String source;
        private RateQuality quality;
        private String id;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder baseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder quoteCurrency(String quoteCurrency) {
            this.quoteCurrency = quoteCurrency;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public Builder validFrom(Instant validFrom) {
            this.validFrom = validFrom;
            return this;
        }

        public Builder validTo(Instant validTo) {
            this.validTo = validTo;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder quality(RateQuality quality) {
            this.quality = quality;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ExchangeRate build() {
            ExchangeRate rate = new ExchangeRate(this);
            rate.id = this.id;
            rate.createdAt = this.createdAt;
            rate.updatedAt = this.updatedAt;
            return rate;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
               Objects.equals(quoteCurrency, that.quoteCurrency) &&
               Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseCurrency, quoteCurrency, tenantId);
    }
}
