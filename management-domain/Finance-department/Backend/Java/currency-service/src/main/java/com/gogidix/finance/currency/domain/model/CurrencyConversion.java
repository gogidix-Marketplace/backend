package com.gogidix.finance.currency.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain Entity - Currency Conversion
 * Represents a conversion transaction between two currencies
 * Multi-tenant SaaS entity with mandatory tenantId field
 */
@Document(collection = "currency_conversions")
public class CurrencyConversion extends BaseEntity {

    @Field("conversion_id")
    private String conversionId;

    @Indexed
    @Field("from_currency")
    private String fromCurrency;

    @Indexed
    @Field("to_currency")
    private String toCurrency;

    @Field("from_amount")
    private BigDecimal fromAmount;

    @Field("to_amount")
    BigDecimal toAmount;

    @Field("exchange_rate")
    private BigDecimal exchangeRate;

    @Field("rate_source")
    private String rateSource;

    @Field("converted_at")
    private Instant convertedAt;

    @Field("reference_id")
    private String referenceId;

    @Field("status")
    private ConversionStatus status;

    public enum ConversionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REVERSED
    }

    protected CurrencyConversion() {
    }

    private CurrencyConversion(Builder builder) {
        super(builder.tenantId);
        this.conversionId = builder.conversionId != null ? builder.conversionId : UUID.randomUUID().toString();
        this.fromCurrency = Objects.requireNonNull(builder.fromCurrency, "fromCurrency is required");
        this.toCurrency = Objects.requireNonNull(builder.toCurrency, "toCurrency is required");
        this.fromAmount = Objects.requireNonNull(builder.fromAmount, "fromAmount is required");
        this.exchangeRate = Objects.requireNonNull(builder.exchangeRate, "exchangeRate is required");
        this.toAmount = calculateToAmount();
        this.rateSource = builder.rateSource;
        this.convertedAt = builder.convertedAt != null ? builder.convertedAt : Instant.now();
        this.referenceId = builder.referenceId;
        this.status = builder.status != null ? builder.status : ConversionStatus.COMPLETED;
    }

    private BigDecimal calculateToAmount() {
        return fromAmount.multiply(exchangeRate).setScale(10, RoundingMode.HALF_UP);
    }

    public void markAsFailed(String reason) {
        this.status = ConversionStatus.FAILED;
        this.updateTimestamp();
    }

    public void reverse() {
        if (this.status != ConversionStatus.COMPLETED) {
            throw new IllegalStateException("Can only reverse completed conversions");
        }
        this.status = ConversionStatus.REVERSED;
        this.updateTimestamp();
    }

    public BigDecimal getConvertedAmount() {
        return toAmount;
    }

    public String getConversionId() {
        return conversionId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public String getRateSource() {
        return rateSource;
    }

    public Instant getConvertedAt() {
        return convertedAt;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public ConversionStatus getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String conversionId;
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal fromAmount;
        private BigDecimal toAmount;
        private BigDecimal exchangeRate;
        private String rateSource;
        private Instant convertedAt;
        private String referenceId;
        private ConversionStatus status;
        private String id;
        private Instant createdAt;
        private Instant updatedAt;

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder conversionId(String conversionId) {
            this.conversionId = conversionId;
            return this;
        }

        public Builder fromCurrency(String fromCurrency) {
            this.fromCurrency = fromCurrency;
            return this;
        }

        public Builder toCurrency(String toCurrency) {
            this.toCurrency = toCurrency;
            return this;
        }

        public Builder fromAmount(BigDecimal fromAmount) {
            this.fromAmount = fromAmount;
            return this;
        }

        public Builder toAmount(BigDecimal toAmount) {
            this.toAmount = toAmount;
            return this;
        }

        public Builder exchangeRate(BigDecimal exchangeRate) {
            this.exchangeRate = exchangeRate;
            return this;
        }

        public Builder rateSource(String rateSource) {
            this.rateSource = rateSource;
            return this;
        }

        public Builder convertedAt(Instant convertedAt) {
            this.convertedAt = convertedAt;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder status(ConversionStatus status) {
            this.status = status;
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

        public CurrencyConversion build() {
            CurrencyConversion conversion = new CurrencyConversion(this);
            if (this.toAmount != null) {
                conversion.toAmount = this.toAmount;
            }
            conversion.id = this.id;
            conversion.createdAt = this.createdAt;
            conversion.updatedAt = this.updatedAt;
            return conversion;
        }
    }
}
