package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain Entity - Financial Metric
 * ZERO framework dependencies - pure domain logic
 */
@Document(collection = "financial_metrics")
public class FinancialMetric {

    @Id
    private String id;

    /**
     * MANDATORY: Tenant ID for multi-tenancy
     * All queries MUST filter by this field
     */
    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("metric_type")
    private MetricType metricType;

    @Field("region")
    private String region;

    @Field("country")
    private String country;

    @Field("amount")
    private BigDecimal amount;

    @Field("currency")
    private String currency;

    @Field("period")
    private String period; // YYYY-MM format

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("metadata")
    private java.util.Map<String, Object> metadata;

    public enum MetricType {
        REVENUE,
        EXPENSE,
        PROFIT,
        CASH_FLOW,
        BUDGET_VARIANCE,
        RECEIVABLES,
        PAYABLES
    }

    /**
     * Create new financial metric with tenant context
     */
    public FinancialMetric(String tenantId, MetricType metricType, BigDecimal amount, String currency, String period) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.metricType = Objects.requireNonNull(metricType, "metricType is required");
        this.amount = Objects.requireNonNull(amount, "amount is required");
        this.currency = Objects.requireNonNull(currency, "currency is required");
        this.period = Objects.requireNonNull(period, "period is required");
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Default constructor for persistence
    protected FinancialMetric() {}

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public MetricType getMetricType() { return metricType; }
    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPeriod() { return period; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public java.util.Map<String, Object> getMetadata() { return metadata; }

    // Setters
    public void setRegion(String region) { this.region = region; }
    public void setCountry(String country) { this.country = country; }
    public void setMetadata(java.util.Map<String, Object> metadata) { this.metadata = metadata; }

    /**
     * Domain logic: update amount
     */
    public void updateAmount(BigDecimal newAmount) {
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("amount", "must be non-negative");
        }
        this.amount = newAmount;
        this.updatedAt = Instant.now();
    }

    /**
     * Domain logic: add metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.put(key, value);
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialMetric that = (FinancialMetric) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FinancialMetric{" +
                "id='" + id + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", metricType=" + metricType +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}
