package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.entity;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Entity representing threat detection rules and patterns
 * Manages security threat identification, alerting, and response automation
 */
@Document(collection = "threat_detection_rules")
public class ThreatDetectionRule {

    @Indexed(background = true)
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed(background = true)
    private String ruleName;

    private String description;

    @Indexed(background = true)
    private ThreatType threatType;

    private ThreatCategory threatCategory;

    @Indexed(background = true)
    private SeverityLevel severityLevel;

    @Indexed(background = true)
    private RuleStatus ruleStatus;

    private DataSource dataSource;

    private String detectionPattern;

    private String queryCondition;

    private Double thresholdValue;

    private Integer timeWindowMinutes;

    private Integer minimumOccurrences = 1;

    private Double confidenceThreshold = 0.8;

    private Set<String> indicators;

    private Map<String, String> conditions;

    private Set<String> responseActions;

    private Boolean isActive = true;

    private Boolean autoResponseEnabled = false;

    private Boolean quarantineEnabled = false;

    private Boolean notificationEnabled = true;

    private Boolean loggingEnabled = true;

    private Set<String> tags;

    private String createdBy;

    private String lastModifiedBy;

    private Integer ruleVersion = 1;

    private Long detectionCount = 0L;

    private Long falsePositiveCount = 0L;

    private LocalDateTime lastDetectionDate;

    private Double accuracyRate = 0.0;

    private Long executionTimeMs;

    private Double cpuUsagePercent;

    private Double memoryUsageMb;

    private Map<String, String> metadata;

    @Indexed(background = true)
    private LocalDateTime createdAt;

    @Indexed(background = true)
    private LocalDateTime updatedAt;

    private Long version = 0L;

    // Enums
    public enum ThreatType {
        BRUTE_FORCE_ATTACK,
        SQL_INJECTION,
        XSS_ATTACK,
        CSRF_ATTACK,
        DDoS_ATTACK,
        MALWARE_DETECTION,
        SUSPICIOUS_LOGIN,
        PRIVILEGE_ESCALATION,
        DATA_EXFILTRATION,
        UNAUTHORIZED_ACCESS,
        ANOMALOUS_BEHAVIOR,
        INSIDER_THREAT,
        PHISHING_ATTEMPT,
        RANSOMWARE,
        ACCOUNT_TAKEOVER,
        API_ABUSE,
        CREDENTIAL_STUFFING,
        SESSION_HIJACKING,
        DIRECTORY_TRAVERSAL,
        BUFFER_OVERFLOW,
        CUSTOM
    }

    public enum ThreatCategory {
        NETWORK_ATTACK,
        WEB_APPLICATION_ATTACK,
        MALWARE,
        SOCIAL_ENGINEERING,
        INSIDER_THREAT,
        DATA_BREACH,
        COMPLIANCE_VIOLATION,
        OPERATIONAL_RISK,
        ADVANCED_PERSISTENT_THREAT,
        ZERO_DAY_EXPLOIT
    }

    public enum SeverityLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum RuleStatus {
        DRAFT,
        TESTING,
        ACTIVE,
        INACTIVE,
        DEPRECATED,
        SUSPENDED
    }

    public enum DataSource {
        AUDIT_LOGS,
        NETWORK_TRAFFIC,
        APPLICATION_LOGS,
        SYSTEM_LOGS,
        DATABASE_LOGS,
        SECURITY_EVENTS,
        USER_BEHAVIOR,
        API_CALLS,
        FILE_SYSTEM,
        MEMORY_ANALYSIS,
        EMAIL_LOGS,
        WEB_PROXY_LOGS,
        FIREWALL_LOGS,
        IDS_IPS_LOGS,
        ENDPOINT_LOGS
    }

    // Constructors
    public ThreatDetectionRule() {
        this.ruleStatus = RuleStatus.DRAFT;
        this.severityLevel = SeverityLevel.MEDIUM;
    }

    public ThreatDetectionRule(String ruleName, ThreatType threatType, ThreatCategory threatCategory,
                              SeverityLevel severityLevel, DataSource dataSource, String createdBy) {
        this();
        this.ruleName = ruleName;
        this.threatType = threatType;
        this.threatCategory = threatCategory;
        this.severityLevel = severityLevel;
        this.dataSource = dataSource;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ThreatType getThreatType() { return threatType; }
    public void setThreatType(ThreatType threatType) { this.threatType = threatType; }

    public ThreatCategory getThreatCategory() { return threatCategory; }
    public void setThreatCategory(ThreatCategory threatCategory) { this.threatCategory = threatCategory; }

    public SeverityLevel getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(SeverityLevel severityLevel) { this.severityLevel = severityLevel; }

    public RuleStatus getRuleStatus() { return ruleStatus; }
    public void setRuleStatus(RuleStatus ruleStatus) { this.ruleStatus = ruleStatus; }

    public DataSource getDataSource() { return dataSource; }
    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public String getDetectionPattern() { return detectionPattern; }
    public void setDetectionPattern(String detectionPattern) { this.detectionPattern = detectionPattern; }

    public String getQueryCondition() { return queryCondition; }
    public void setQueryCondition(String queryCondition) { this.queryCondition = queryCondition; }

    public Double getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(Double thresholdValue) { this.thresholdValue = thresholdValue; }

    public Integer getTimeWindowMinutes() { return timeWindowMinutes; }
    public void setTimeWindowMinutes(Integer timeWindowMinutes) { this.timeWindowMinutes = timeWindowMinutes; }

    public Integer getMinimumOccurrences() { return minimumOccurrences; }
    public void setMinimumOccurrences(Integer minimumOccurrences) { this.minimumOccurrences = minimumOccurrences; }

    public Double getConfidenceThreshold() { return confidenceThreshold; }
    public void setConfidenceThreshold(Double confidenceThreshold) { this.confidenceThreshold = confidenceThreshold; }

    public Set<String> getIndicators() { return indicators; }
    public void setIndicators(Set<String> indicators) { this.indicators = indicators; }

    public Map<String, String> getConditions() { return conditions; }
    public void setConditions(Map<String, String> conditions) { this.conditions = conditions; }

    public Set<String> getResponseActions() { return responseActions; }
    public void setResponseActions(Set<String> responseActions) { this.responseActions = responseActions; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Boolean getAutoResponseEnabled() { return autoResponseEnabled; }
    public void setAutoResponseEnabled(Boolean autoResponseEnabled) { this.autoResponseEnabled = autoResponseEnabled; }

    public Boolean getQuarantineEnabled() { return quarantineEnabled; }
    public void setQuarantineEnabled(Boolean quarantineEnabled) { this.quarantineEnabled = quarantineEnabled; }

    public Boolean getNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(Boolean notificationEnabled) { this.notificationEnabled = notificationEnabled; }

    public Boolean getLoggingEnabled() { return loggingEnabled; }
    public void setLoggingEnabled(Boolean loggingEnabled) { this.loggingEnabled = loggingEnabled; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Integer getRuleVersion() { return ruleVersion; }
    public void setRuleVersion(Integer ruleVersion) { this.ruleVersion = ruleVersion; }

    public Long getDetectionCount() { return detectionCount; }
    public void setDetectionCount(Long detectionCount) { this.detectionCount = detectionCount; }

    public Long getFalsePositiveCount() { return falsePositiveCount; }
    public void setFalsePositiveCount(Long falsePositiveCount) { this.falsePositiveCount = falsePositiveCount; }

    public LocalDateTime getLastDetectionDate() { return lastDetectionDate; }
    public void setLastDetectionDate(LocalDateTime lastDetectionDate) { this.lastDetectionDate = lastDetectionDate; }

    public Double getAccuracyRate() { return accuracyRate; }
    public void setAccuracyRate(Double accuracyRate) { this.accuracyRate = accuracyRate; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public Double getCpuUsagePercent() { return cpuUsagePercent; }
    public void setCpuUsagePercent(Double cpuUsagePercent) { this.cpuUsagePercent = cpuUsagePercent; }

    public Double getMemoryUsageMb() { return memoryUsageMb; }
    public void setMemoryUsageMb(Double memoryUsageMb) { this.memoryUsageMb = memoryUsageMb; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    // Business methods
    public void updateVersion(String modifiedBy, String changeReason) {
        this.ruleVersion = this.ruleVersion + 1;
        this.lastModifiedBy = modifiedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.ruleStatus = RuleStatus.ACTIVE;
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.ruleStatus = RuleStatus.INACTIVE;
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementDetection() {
        this.detectionCount = (this.detectionCount != null ? this.detectionCount : 0L) + 1;
        this.lastDetectionDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementFalsePositive() {
        this.falsePositiveCount = (this.falsePositiveCount != null ? this.falsePositiveCount : 0L) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend(String suspendedBy) {
        this.ruleStatus = RuleStatus.SUSPENDED;
        this.isActive = false;
        this.lastModifiedBy = suspendedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean requiresImmediateResponse() {
        return this.severityLevel == SeverityLevel.CRITICAL && this.autoResponseEnabled;
    }

    public boolean isWithinTimeWindow(LocalDateTime checkTime) {
        if (timeWindowMinutes == null || timeWindowMinutes <= 0) {
            return true;
        }
        LocalDateTime windowStart = checkTime.minusMinutes(timeWindowMinutes);
        return createdAt != null && createdAt.isAfter(windowStart);
    }

    public boolean matchesPattern(String input) {
        if (detectionPattern == null || input == null) {
            return false;
        }
        // Simple pattern matching - in real implementation would use regex or pattern engine
        return input.contains(detectionPattern);
    }

    public boolean exceedsThreshold(Double value) {
        if (thresholdValue == null) {
            return false;
        }
        return value != null && value > thresholdValue;
    }

    public double getEffectivenessScore() {
        if (detectionCount == null || detectionCount == 0) {
            return 0.0;
        }
        long total = detectionCount + (falsePositiveCount != null ? falsePositiveCount : 0L);
        return Math.round(((double) detectionCount / total) * 100.0) / 100.0;
    }
}
