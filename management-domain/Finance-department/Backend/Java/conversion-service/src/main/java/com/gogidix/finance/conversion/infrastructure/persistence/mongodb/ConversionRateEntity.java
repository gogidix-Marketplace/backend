package com.gogidix.finance.conversion.infrastructure.persistence.mongodb;

import com.gogidix.finance.conversion.domain.model.ConversionRate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * MongoDB document entity for storing ConversionRate domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "conversion_rates")
public class ConversionRateEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed(unique = true)
    @Field("rate_key")
    private String rateKey;

    @Field("from_currency")
    private String fromCurrency;

    @Field("to_currency")
    private String toCurrency;

    private BigDecimal rate;

    @Field("inverse_rate")
    private BigDecimal inverseRate;

    @Field("fetched_at")
    private Instant fetchedAt;

    @Field("expires_at")
    private Instant expiresAt;

    private String provider;

    @Field("ttl_seconds")
    private Long ttlSeconds;

    @Field("hit_count")
    private Integer hitCount;

    @Field("bid_price")
    private BigDecimal bidPrice;

    @Field("ask_price")
    private BigDecimal askPrice;

    @Field("mid_price")
    private BigDecimal midPrice;

    @Field("last_updated_at")
    private Instant lastUpdatedAt;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public ConversionRateEntity() {
    }

    // Constructor from domain model
    public ConversionRateEntity(ConversionRate domain) {
        this.id = domain.getId();
        this.tenantId = null; // ConversionRate is not tenant-aware
        this.rateKey = domain.getRateKey();
        this.fromCurrency = domain.getFromCurrency();
        this.toCurrency = domain.getToCurrency();
        this.rate = domain.getRate();
        this.inverseRate = domain.getInverseRate();
        this.fetchedAt = domain.getFetchedAt();
        this.expiresAt = domain.getExpiresAt();
        this.provider = domain.getProvider();
        this.ttlSeconds = domain.getTtlSeconds();
        this.hitCount = domain.getHitCount();
        this.bidPrice = domain.getBidPrice();
        this.askPrice = domain.getAskPrice();
        this.midPrice = domain.getMidPrice();
        this.lastUpdatedAt = domain.getLastUpdatedAt();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public ConversionRate toDomainModel() {
        ConversionRate.ConversionRateBuilder builder = ConversionRate.builder()
                .rateKey(this.rateKey)
                .fromCurrency(this.fromCurrency)
                .toCurrency(this.toCurrency)
                .rate(this.rate)
                .inverseRate(this.inverseRate)
                .fetchedAt(this.fetchedAt)
                .expiresAt(this.expiresAt)
                .provider(this.provider)
                .ttlSeconds(this.ttlSeconds)
                .hitCount(this.hitCount)
                .bidPrice(this.bidPrice)
                .askPrice(this.askPrice)
                .midPrice(this.midPrice)
                .lastUpdatedAt(this.lastUpdatedAt);

        ConversionRate rate = builder.build();
        // Set ID, created and updated via reflection since builder doesn't support them
        try {
            java.lang.reflect.Field idField = ConversionRate.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(rate, this.id);

            java.lang.reflect.Field createdAtField = ConversionRate.class.getSuperclass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(rate, this.createdAt);

            java.lang.reflect.Field updatedAtField = ConversionRate.class.getSuperclass().getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(rate, this.updatedAt);
        } catch (Exception e) {
            // Ignore reflection errors
        }

        return rate;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getRateKey() { return rateKey; }
    public void setRateKey(String rateKey) { this.rateKey = rateKey; }

    public String getFromCurrency() { return fromCurrency; }
    public void setFromCurrency(String fromCurrency) { this.fromCurrency = fromCurrency; }

    public String getToCurrency() { return toCurrency; }
    public void setToCurrency(String toCurrency) { this.toCurrency = toCurrency; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public BigDecimal getInverseRate() { return inverseRate; }
    public void setInverseRate(BigDecimal inverseRate) { this.inverseRate = inverseRate; }

    public Instant getFetchedAt() { return fetchedAt; }
    public void setFetchedAt(Instant fetchedAt) { this.fetchedAt = fetchedAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public Long getTtlSeconds() { return ttlSeconds; }
    public void setTtlSeconds(Long ttlSeconds) { this.ttlSeconds = ttlSeconds; }

    public Integer getHitCount() { return hitCount; }
    public void setHitCount(Integer hitCount) { this.hitCount = hitCount; }

    public BigDecimal getBidPrice() { return bidPrice; }
    public void setBidPrice(BigDecimal bidPrice) { this.bidPrice = bidPrice; }

    public BigDecimal getAskPrice() { return askPrice; }
    public void setAskPrice(BigDecimal askPrice) { this.askPrice = askPrice; }

    public BigDecimal getMidPrice() { return midPrice; }
    public void setMidPrice(BigDecimal midPrice) { this.midPrice = midPrice; }

    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionRateEntity that = (ConversionRateEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
