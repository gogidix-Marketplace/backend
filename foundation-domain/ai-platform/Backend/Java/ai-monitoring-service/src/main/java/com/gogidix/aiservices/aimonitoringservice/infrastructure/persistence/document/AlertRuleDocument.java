package com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.document;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "alert_rules")
@CompoundIndex(name = "idx_alert_tenant", def = "{'tenantId': 1, 'alertId': 1}")
public class AlertRuleDocument {

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

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public ConditionType getCondition() { return condition; }
    public void setCondition(ConditionType condition) { this.condition = condition; }
    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { this.threshold = threshold; }
    public List<String> getNotificationChannels() { return notificationChannels; }
    public void setNotificationChannels(List<String> notificationChannels) { this.notificationChannels = notificationChannels; }
    public AlertRuleStatus getStatus() { return status; }
    public void setStatus(AlertRuleStatus status) { this.status = status; }
    public Integer getMinAlertInterval() { return minAlertInterval; }
    public void setMinAlertInterval(Integer minAlertInterval) { this.minAlertInterval = minAlertInterval; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public Instant getLastTriggeredAt() { return lastTriggeredAt; }
    public void setLastTriggeredAt(Instant lastTriggeredAt) { this.lastTriggeredAt = lastTriggeredAt; }
    public Long getTriggerCount() { return triggerCount; }
    public void setTriggerCount(Long triggerCount) { this.triggerCount = triggerCount; }
}
