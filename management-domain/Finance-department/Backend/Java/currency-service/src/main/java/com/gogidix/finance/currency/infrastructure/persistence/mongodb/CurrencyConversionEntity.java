package com.gogidix.finance.currency.infrastructure.persistence.mongodb;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * MongoDB document entity for storing CurrencyConversion domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "currency_conversions")
public class CurrencyConversionEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

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
    private BigDecimal toAmount;

    @Field("exchange_rate")
    private BigDecimal exchangeRate;

    @Field("rate_source")
    private String rateSource;

    @Field("converted_at")
    private Instant convertedAt;

    @Field("reference_id")
    private String referenceId;

    @Field("status")
    private String status;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public CurrencyConversionEntity() {
    }

    // Constructor from domain model
    public CurrencyConversionEntity(CurrencyConversion conversion) {
        this.id = conversion.getId();
        this.tenantId = conversion.getTenantId();
        this.conversionId = conversion.getConversionId();
        this.fromCurrency = conversion.getFromCurrency();
        this.toCurrency = conversion.getToCurrency();
        this.fromAmount = conversion.getFromAmount();
        this.toAmount = conversion.getToAmount();
        this.exchangeRate = conversion.getExchangeRate();
        this.rateSource = conversion.getRateSource();
        this.convertedAt = conversion.getConvertedAt();
        this.referenceId = conversion.getReferenceId();
        this.status = conversion.getStatus() != null ? conversion.getStatus().name() : null;
        this.createdAt = conversion.getCreatedAt();
        this.updatedAt = conversion.getUpdatedAt();
    }

    // Convert to domain model
    public CurrencyConversion toDomainModel() {
        return CurrencyConversion.builder()
                .id(this.id)
                .tenantId(this.tenantId)
                .conversionId(this.conversionId)
                .fromCurrency(this.fromCurrency)
                .toCurrency(this.toCurrency)
                .fromAmount(this.fromAmount)
                .toAmount(this.toAmount)
                .exchangeRate(this.exchangeRate)
                .rateSource(this.rateSource)
                .convertedAt(this.convertedAt)
                .referenceId(this.referenceId)
                .status(this.status != null ? CurrencyConversion.ConversionStatus.valueOf(this.status) : null)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(CurrencyConversion conversion) {
        this.conversionId = conversion.getConversionId();
        this.fromCurrency = conversion.getFromCurrency();
        this.toCurrency = conversion.getToCurrency();
        this.fromAmount = conversion.getFromAmount();
        this.toAmount = conversion.getToAmount();
        this.exchangeRate = conversion.getExchangeRate();
        this.rateSource = conversion.getRateSource();
        this.convertedAt = conversion.getConvertedAt();
        this.referenceId = conversion.getReferenceId();
        this.status = conversion.getStatus() != null ? conversion.getStatus().name() : null;
        this.updatedAt = conversion.getUpdatedAt();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getConversionId() {
        return conversionId;
    }

    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getRateSource() {
        return rateSource;
    }

    public void setRateSource(String rateSource) {
        this.rateSource = rateSource;
    }

    public Instant getConvertedAt() {
        return convertedAt;
    }

    public void setConvertedAt(Instant convertedAt) {
        this.convertedAt = convertedAt;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
