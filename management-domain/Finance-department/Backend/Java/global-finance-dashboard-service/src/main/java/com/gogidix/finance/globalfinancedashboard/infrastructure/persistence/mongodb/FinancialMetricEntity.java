package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongodb;

import com.gogidix.finance.globalfinancedashboard.domain.model.FinancialMetric;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * MongoDB document entity for storing FinancialMetric domain model.
 */
@Document(collection = "financial_metrics")
public class FinancialMetricEntity {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String metricType;
    private String region;
    private String country;
    private BigDecimal amount;
    private String currency;
    private String period;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> metadata;

    // Default constructor for MongoDB
    public FinancialMetricEntity() {
    }

    // Constructor from domain model
    public FinancialMetricEntity(FinancialMetric metric) {
        this.id = metric.getId();
        this.tenantId = metric.getTenantId();
        this.metricType = metric.getMetricType() != null ? metric.getMetricType().name() : null;
        this.region = metric.getRegion();
        this.country = metric.getCountry();
        this.amount = metric.getAmount();
        this.currency = metric.getCurrency();
        this.period = metric.getPeriod();
        this.createdAt = metric.getCreatedAt();
        this.updatedAt = metric.getUpdatedAt();
        this.metadata = metric.getMetadata() != null ? new HashMap<>(metric.getMetadata()) : new HashMap<>();
    }

    // Convert to domain model
    public FinancialMetric toDomainModel() {
        FinancialMetric metric = new FinancialMetric(
            this.tenantId,
            this.metricType != null ? FinancialMetric.MetricType.valueOf(this.metricType) : null,
            this.amount,
            this.currency,
            this.period
        );
        // Use reflection or add setters to set remaining fields
        // For now, we'll need to add this via the existing methods or add setters
        return metric;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getMetricType() { return metricType; }
    public void setMetricType(String metricType) { this.metricType = metricType; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}
