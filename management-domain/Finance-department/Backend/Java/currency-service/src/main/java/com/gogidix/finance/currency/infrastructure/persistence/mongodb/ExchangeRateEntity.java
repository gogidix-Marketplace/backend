package com.gogidix.finance.currency.infrastructure.persistence.mongodb;

import com.gogidix.finance.currency.domain.model.ExchangeRate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * MongoDB document entity for storing ExchangeRate domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "exchange_rates")
@CompoundIndex(name = "tenant_currency_pair_idx", def = "{'tenantId': 1, 'baseCurrency': 1, 'quoteCurrency': 1}", unique = true)
public class ExchangeRateEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

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
    private String quality;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public ExchangeRateEntity() {
    }

    // Constructor from domain model
    public ExchangeRateEntity(ExchangeRate exchangeRate) {
        this.id = exchangeRate.getId();
        this.tenantId = exchangeRate.getTenantId();
        this.baseCurrency = exchangeRate.getBaseCurrency();
        this.quoteCurrency = exchangeRate.getQuoteCurrency();
        this.rate = exchangeRate.getRate();
        this.inverseRate = exchangeRate.getInverseRate();
        this.validFrom = exchangeRate.getValidFrom();
        this.validTo = exchangeRate.getValidTo();
        this.source = exchangeRate.getSource();
        this.quality = exchangeRate.getQuality() != null ? exchangeRate.getQuality().name() : null;
        this.createdAt = exchangeRate.getCreatedAt();
        this.updatedAt = exchangeRate.getUpdatedAt();
    }

    // Convert to domain model
    public ExchangeRate toDomainModel() {
        return ExchangeRate.builder()
                .id(this.id)
                .tenantId(this.tenantId)
                .baseCurrency(this.baseCurrency)
                .quoteCurrency(this.quoteCurrency)
                .rate(this.rate)
                .validFrom(this.validFrom)
                .validTo(this.validTo)
                .source(this.source)
                .quality(this.quality != null ? ExchangeRate.RateQuality.valueOf(this.quality) : null)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(ExchangeRate exchangeRate) {
        this.baseCurrency = exchangeRate.getBaseCurrency();
        this.quoteCurrency = exchangeRate.getQuoteCurrency();
        this.rate = exchangeRate.getRate();
        this.inverseRate = exchangeRate.getInverseRate();
        this.validFrom = exchangeRate.getValidFrom();
        this.validTo = exchangeRate.getValidTo();
        this.source = exchangeRate.getSource();
        this.quality = exchangeRate.getQuality() != null ? exchangeRate.getQuality().name() : null;
        this.updatedAt = exchangeRate.getUpdatedAt();
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

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getInverseRate() {
        return inverseRate;
    }

    public void setInverseRate(BigDecimal inverseRate) {
        this.inverseRate = inverseRate;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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
