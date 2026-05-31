package com.gogidix.aiservices.aimonitoringservice.domain.model;

import com.gogidix.aiservices.aimonitoringservice.shared.exception.ValidationException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing an Alert Rule.
 */
@Document(collection = "alert_rules")
@CompoundIndex(name = "idx_alert_tenant", def = "{'tenantId': 1, 'alertId': 1}")
public class AlertRule {

    @Id
    private String id;

    @Indexed
    private String alertId;

    @Indexed
    private String tenantId;

    private String name;

    private String metric;

    private ConditionType condition;

    private Double threshold;

    private List<String> notificationChannels;

    private AlertRuleStatus status;

    private Integer minAlertInterval;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastTriggeredAt;

    private Long triggerCount;

    private AlertRule() {
        this.notificationChannels = new ArrayList<>();
        this.triggerCount = 0L;
    }

    public AlertRule(String tenantId, String name, String metric, ConditionType condition, Double threshold) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.alertId = "alert_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.metric = Objects.requireNonNull(metric, "metric is required");
        this.condition = Objects.requireNonNull(condition, "condition is required");
        this.threshold = Objects.requireNonNull(threshold, "threshold is required");
        this.status = AlertRuleStatus.ACTIVE;
        this.minAlertInterval = 60;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = AlertRuleStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = AlertRuleStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void pause() {
        this.status = AlertRuleStatus.PAUSED;
        this.updatedAt = Instant.now();
    }

    public void addNotificationChannel(String channel) {
        Objects.requireNonNull(channel, "channel is required");
        if (!this.notificationChannels.contains(channel)) {
            this.notificationChannels.add(channel);
            this.updatedAt = Instant.now();
        }
    }

    public void removeNotificationChannel(String channel) {
        this.notificationChannels.remove(channel);
        this.updatedAt = Instant.now();
    }

    public void updateThreshold(Double threshold) {
        this.threshold = Objects.requireNonNull(threshold, "threshold is required");
        this.updatedAt = Instant.now();
    }

    public void updateMinAlertInterval(Integer interval) {
        if (interval == null || interval < 1) {
            throw new ValidationException("minAlertInterval", "Interval must be at least 1 minute");
        }
        this.minAlertInterval = interval;
        this.updatedAt = Instant.now();
    }

    public boolean canTrigger() {
        if (this.status != AlertRuleStatus.ACTIVE) {
            return false;
        }
        if (this.lastTriggeredAt == null) {
            return true;
        }
        return Instant.now().isAfter(this.lastTriggeredAt.plusSeconds(this.minAlertInterval * 60L));
    }

    public void recordTrigger() {
        this.lastTriggeredAt = Instant.now();
        this.triggerCount++;
        this.updatedAt = Instant.now();
    }

    public void validate() {
        if (tenantId == null || tenantId.isBlank()) {
            throw new ValidationException("tenantId is required");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("name is required");
        }
        if (metric == null || metric.isBlank()) {
            throw new ValidationException("metric is required");
        }
        if (threshold == null) {
            throw new ValidationException("threshold is required");
        }
    }

    // Getters
    public String getId() { return id; }
    public String getAlertId() { return alertId; }
    public String getTenantId() { return tenantId; }
    public String getName() { return name; }
    public String getMetric() { return metric; }
    public ConditionType getCondition() { return condition; }
    public Double getThreshold() { return threshold; }
    public List<String> getNotificationChannels() { return Collections.unmodifiableList(notificationChannels); }
    public AlertRuleStatus getStatus() { return status; }
    public Integer getMinAlertInterval() { return minAlertInterval; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getLastTriggeredAt() { return lastTriggeredAt; }
    public Long getTriggerCount() { return triggerCount; }

    // Setters for persistence
    protected void setId(String id) { this.id = id; }
    protected void setAlertId(String alertId) { this.alertId = alertId; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setName(String name) { this.name = name; }
    protected void setMetric(String metric) { this.metric = metric; }
    protected void setCondition(ConditionType condition) { this.condition = condition; }
    protected void setThreshold(Double threshold) { this.threshold = threshold; }
    protected void setNotificationChannels(List<String> notificationChannels) { this.notificationChannels = notificationChannels; }
    protected void setStatus(AlertRuleStatus status) { this.status = status; }
    protected void setMinAlertInterval(Integer minAlertInterval) { this.minAlertInterval = minAlertInterval; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    protected void setLastTriggeredAt(Instant lastTriggeredAt) { this.lastTriggeredAt = lastTriggeredAt; }
    protected void setTriggerCount(Long triggerCount) { this.triggerCount = triggerCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertRule alertRule = (AlertRule) o;
        return Objects.equals(alertId, alertRule.alertId) && Objects.equals(tenantId, alertRule.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alertId, tenantId);
    }

    @Override
    public String toString() {
        return "AlertRule{" + "alertId='" + alertId + '\'' + ", tenantId='" + tenantId + '\'' + ", name='" + name + '\'' + ", status=" + status + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AlertRule instance = new AlertRule();

        public Builder id(String id) { instance.id = id; return this; }
        public Builder alertId(String alertId) { instance.alertId = alertId; return this; }
        public Builder tenantId(String tenantId) { instance.tenantId = tenantId; return this; }
        public Builder name(String name) { instance.name = name; return this; }
        public Builder metric(String metric) { instance.metric = metric; return this; }
        public Builder condition(ConditionType condition) { instance.condition = condition; return this; }
        public Builder threshold(Double threshold) { instance.threshold = threshold; return this; }
        public Builder notificationChannels(List<String> channels) { instance.notificationChannels = channels; return this; }
        public Builder status(AlertRuleStatus status) { instance.status = status; return this; }
        public Builder minAlertInterval(Integer interval) { instance.minAlertInterval = interval; return this; }

        public AlertRule build() {
            if (instance.tenantId == null) throw new IllegalArgumentException("tenantId required");
            if (instance.name == null) throw new IllegalArgumentException("name required");
            if (instance.status == null) instance.status = AlertRuleStatus.ACTIVE;
            if (instance.minAlertInterval == null) instance.minAlertInterval = 60;
            if (instance.createdAt == null) instance.createdAt = Instant.now();
            if (instance.updatedAt == null) instance.updatedAt = Instant.now();
            return instance;
        }
    }
}
