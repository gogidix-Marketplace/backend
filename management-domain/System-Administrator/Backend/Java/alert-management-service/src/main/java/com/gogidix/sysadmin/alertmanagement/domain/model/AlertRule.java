package com.gogidix.sysadmin.alertmanagement.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "alert_rules")
public class AlertRule {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String name;
    private String description;
    private Alert.AlertSeverity severity;
    private Boolean enabled;
    private String sourceType;
    private List<Condition> conditions;
    private AlertEvaluationType evaluationType;
    private Integer evaluationFrequencySeconds;
    private Integer thresholdOccurrences;
    private Integer timeWindowSeconds;
    private List<NotificationConfig> notificationConfigs;
    private List<String> suppressionWindows;
    private Map<String, String> tags;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;

    public enum AlertEvaluationType {
        ANY, ALL, COMPLEX
    }

    public static class Condition {
        private String metric;
        private String operator;
        private Double threshold;
        private String aggregationType;
        private Integer timeWindowSeconds;

        public String getMetric() { return metric; }
        public void setMetric(String metric) { this.metric = metric; }
        public String getOperator() { return operator; }
        public void setOperator(String operator) { this.operator = operator; }
        public Double getThreshold() { return threshold; }
        public void setThreshold(Double threshold) { this.threshold = threshold; }
        public String getAggregationType() { return aggregationType; }
        public void setAggregationType(String aggregationType) { this.aggregationType = aggregationType; }
        public Integer getTimeWindowSeconds() { return timeWindowSeconds; }
        public void setTimeWindowSeconds(Integer timeWindowSeconds) { this.timeWindowSeconds = timeWindowSeconds; }
    }

    public static class NotificationConfig {
        private String type;
        private String target;
        private Map<String, String> config;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
        public Map<String, String> getConfig() { return config; }
        public void setConfig(Map<String, String> config) { this.config = config; }
    }

    public AlertRule() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.enabled = true;
        this.evaluationFrequencySeconds = 60;
        this.thresholdOccurrences = 1;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Alert.AlertSeverity getSeverity() { return severity; }
    public void setSeverity(Alert.AlertSeverity severity) { this.severity = severity; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public List<Condition> getConditions() { return conditions; }
    public void setConditions(List<Condition> conditions) { this.conditions = conditions; }

    public AlertEvaluationType getEvaluationType() { return evaluationType; }
    public void setEvaluationType(AlertEvaluationType evaluationType) { this.evaluationType = evaluationType; }

    public Integer getEvaluationFrequencySeconds() { return evaluationFrequencySeconds; }
    public void setEvaluationFrequencySeconds(Integer evaluationFrequencySeconds) { this.evaluationFrequencySeconds = evaluationFrequencySeconds; }

    public Integer getThresholdOccurrences() { return thresholdOccurrences; }
    public void setThresholdOccurrences(Integer thresholdOccurrences) { this.thresholdOccurrences = thresholdOccurrences; }

    public Integer getTimeWindowSeconds() { return timeWindowSeconds; }
    public void setTimeWindowSeconds(Integer timeWindowSeconds) { this.timeWindowSeconds = timeWindowSeconds; }

    public List<NotificationConfig> getNotificationConfigs() { return notificationConfigs; }
    public void setNotificationConfigs(List<NotificationConfig> notificationConfigs) { this.notificationConfigs = notificationConfigs; }

    public List<String> getSuppressionWindows() { return suppressionWindows; }
    public void setSuppressionWindows(List<String> suppressionWindows) { this.suppressionWindows = suppressionWindows; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertRule alertRule = (AlertRule) o;
        return Objects.equals(id, alertRule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
