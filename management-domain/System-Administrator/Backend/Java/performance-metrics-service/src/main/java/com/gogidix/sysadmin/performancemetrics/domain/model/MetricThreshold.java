package com.gogidix.sysadmin.performancemetrics.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "metric_thresholds")
public class MetricThreshold {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String resourceId;
    private String resourceType;
    @Indexed
    private String metricName;
    private ThresholdType thresholdType;
    private Double warningThreshold;
    private Double criticalThreshold;
    private String aggregationType;
    private Integer evaluationWindowSeconds;
    private Boolean enabled;
    private String alertRecipient;
    private Map<String, String> tags;
    private Instant createdAt;
    private Instant updatedAt;

    public enum ThresholdType {
        UPPER_BOUND, LOWER_BOUND, EXACT_MATCH
    }

    public enum AggregationType {
        AVERAGE, MAX, MIN, SUM, PERCENTILE_95, PERCENTILE_99
    }

    public MetricThreshold() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.enabled = true;
        this.evaluationWindowSeconds = 300; // 5 minutes default
        this.aggregationType = "AVERAGE";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }

    public ThresholdType getThresholdType() { return thresholdType; }
    public void setThresholdType(ThresholdType thresholdType) { this.thresholdType = thresholdType; }

    public Double getWarningThreshold() { return warningThreshold; }
    public void setWarningThreshold(Double warningThreshold) { this.warningThreshold = warningThreshold; }

    public Double getCriticalThreshold() { return criticalThreshold; }
    public void setCriticalThreshold(Double criticalThreshold) { this.criticalThreshold = criticalThreshold; }

    public String getAggregationType() { return aggregationType; }
    public void setAggregationType(String aggregationType) { this.aggregationType = aggregationType; }

    public Integer getEvaluationWindowSeconds() { return evaluationWindowSeconds; }
    public void setEvaluationWindowSeconds(Integer evaluationWindowSeconds) { this.evaluationWindowSeconds = evaluationWindowSeconds; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getAlertRecipient() { return alertRecipient; }
    public void setAlertRecipient(String alertRecipient) { this.alertRecipient = alertRecipient; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricThreshold that = (MetricThreshold) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MetricThreshold instance = new MetricThreshold();

        public Builder id(String id) { instance.setId(id); return this; }
        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder resourceId(String resourceId) { instance.setResourceId(resourceId); return this; }
        public Builder resourceType(String resourceType) { instance.setResourceType(resourceType); return this; }
        public Builder metricName(String metricName) { instance.setMetricName(metricName); return this; }
        public Builder thresholdType(ThresholdType thresholdType) { instance.setThresholdType(thresholdType); return this; }
        public Builder warningThreshold(Double warningThreshold) { instance.setWarningThreshold(warningThreshold); return this; }
        public Builder criticalThreshold(Double criticalThreshold) { instance.setCriticalThreshold(criticalThreshold); return this; }
        public Builder aggregationType(String aggregationType) { instance.setAggregationType(aggregationType); return this; }
        public Builder evaluationWindowSeconds(Integer evaluationWindowSeconds) { instance.setEvaluationWindowSeconds(evaluationWindowSeconds); return this; }
        public Builder enabled(Boolean enabled) { instance.setEnabled(enabled); return this; }
        public Builder alertRecipient(String alertRecipient) { instance.setAlertRecipient(alertRecipient); return this; }
        public Builder tags(Map<String, String> tags) { instance.setTags(tags); return this; }

        public MetricThreshold build() {
            return instance;
        }
    }
}
