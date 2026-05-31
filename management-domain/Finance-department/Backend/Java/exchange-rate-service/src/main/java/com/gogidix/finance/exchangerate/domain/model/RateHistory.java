package com.gogidix.finance.exchangerate.domain.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity - Rate History
 * Tracks historical exchange rate changes
 * Multi-tenant SaaS entity with mandatory tenantId field
 */
@Document(collection = "rate_history")
@CompoundIndex(name = "tenant_pair_date_idx", def = "{'tenantId': 1, 'baseCurrency': 1, 'quoteCurrency': 1, 'timestamp': -1}")
public class RateHistory extends BaseEntity {

    @Indexed
    @Field("base_currency")
    private String baseCurrency;

    @Indexed
    @Field("quote_currency")
    private String quoteCurrency;

    @Field("rate")
    private BigDecimal rate;

    @Field("previous_rate")
    private BigDecimal previousRate;

    @Field("change_amount")
    private BigDecimal changeAmount;

    @Field("change_percentage")
    private BigDecimal changePercentage;

    @Indexed
    @Field("timestamp")
    private Instant timestamp;

    @Field("source")
    private String source;

    @Field("quality")
    private ExchangeRate.RateQuality quality;

    @Field("operation_type")
    private OperationType operationType;

    @Field("triggered_by")
    private String triggeredBy;

    public enum OperationType {
        INITIAL_LOAD,
        SCHEDULED_UPDATE,
        MANUAL_UPDATE,
        RATE_EXPIRED,
        CACHE_MISS,
        FALLBACK_FETCH
    }

    protected RateHistory() {
    }

    private RateHistory(Builder builder) {
        super(builder.tenantId);
        this.baseCurrency = Objects.requireNonNull(builder.baseCurrency, "baseCurrency is required");
        this.quoteCurrency = Objects.requireNonNull(builder.quoteCurrency, "quoteCurrency is required");
        this.rate = Objects.requireNonNull(builder.rate, "rate is required");
        this.previousRate = builder.previousRate;
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.source = builder.source;
        this.quality = builder.quality;
        this.operationType = builder.operationType != null ? builder.operationType : OperationType.MANUAL_UPDATE;
        this.triggeredBy = builder.triggeredBy;

        // Calculate change
        if (previousRate != null && previousRate.compareTo(BigDecimal.ZERO) > 0) {
            this.changeAmount = rate.subtract(previousRate);
            this.changePercentage = changeAmount
                .divide(previousRate, 6, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
    }

    public String getCurrencyPair() {
        return baseCurrency + quoteCurrency;
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

    public BigDecimal getPreviousRate() {
        return previousRate;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public BigDecimal getChangePercentage() {
        return changePercentage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public ExchangeRate.RateQuality getQuality() {
        return quality;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public boolean isSignificantChange(BigDecimal threshold) {
        return changePercentage != null &&
               changePercentage.abs().compareTo(threshold) > 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String baseCurrency;
        private String quoteCurrency;
        private BigDecimal rate;
        private BigDecimal previousRate;
        private Instant timestamp;
        private String source;
        private ExchangeRate.RateQuality quality;
        private OperationType operationType;
        private String triggeredBy;

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

        public Builder previousRate(BigDecimal previousRate) {
            this.previousRate = previousRate;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder quality(ExchangeRate.RateQuality quality) {
            this.quality = quality;
            return this;
        }

        public Builder operationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public Builder triggeredBy(String triggeredBy) {
            this.triggeredBy = triggeredBy;
            return this;
        }

        public RateHistory build() {
            return new RateHistory(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateHistory that = (RateHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
