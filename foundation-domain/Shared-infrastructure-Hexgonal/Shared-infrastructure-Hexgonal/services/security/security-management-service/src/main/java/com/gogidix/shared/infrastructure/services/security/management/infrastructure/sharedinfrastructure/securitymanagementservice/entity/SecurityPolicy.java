package com.gogidix.shared.infrastructure.services.security.management.infrastructure.sharedinfrastructure.securitymanagementservice.entity;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Entity representing comprehensive security policies
 * Supports RBAC, ABAC, and PBAC policy models with fine-grained access control
 */
@Document(collection = "security_policies")
public class SecurityPolicy {

    @Indexed(background = true)
    private TenantId tenantId;

    @Id
    private String id;

    @Indexed(background = true)
    private String policyName;

    private String description;

    @Indexed(background = true)
    private PolicyType policyType;

    private PolicyModel policyModel;

    @Indexed(background = true)
    private String domain;

    private String environment;

    private Integer priority = 0;

    private Boolean isActive = true;

    private Integer policyVersion = 1;

    // Policy Definition
    private String policyDefinition;

    private PolicyLanguage policyLanguage = PolicyLanguage.JSON;

    // Access Control
    private Set<String> subjects;

    private Set<String> resources;

    private Set<String> actions;

    private Map<String, String> conditions;

    // Time-based Controls
    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    private Map<String, String> timeRestrictions;

    // Location-based Controls
    private Set<String> allowedLocations;

    private Set<String> ipRestrictions;

    // Risk and Compliance
    private RiskLevel riskLevel = RiskLevel.MEDIUM;

    private Set<String> complianceStandards;

    // Enforcement
    private EnforcementMode enforcementMode = EnforcementMode.ENFORCING;

    private Decision defaultDecision = Decision.DENY;

    // Monitoring and Alerting
    private Boolean enableMonitoring = true;

    private Boolean enableAlerting = true;

    private Integer alertThreshold;

    // Metadata
    private Map<String, String> metadata;

    private Map<String, String> tags;

    // Audit Information
    private String createdBy;

    private String updatedBy;

    private String approvedBy;

    private LocalDateTime approvalDate;

    @Indexed(background = true)
    private LocalDateTime createdAt;

    @Indexed(background = true)
    private LocalDateTime updatedAt;

    private Long version = 0L;

    // Additional fields for missing methods
    private PolicyStatus policyStatus = PolicyStatus.DRAFT;

    private PriorityLevel priorityLevel = PriorityLevel.MEDIUM;

    private PolicyCategory policyCategory;

    private String domainScope;

    private Set<String> applicableServices;

    private Set<String> applicableRoles;

    private Set<String> policyRules;

    private Set<String> enforcementActions;

    private LocalDateTime effectiveDate;

    private LocalDateTime expiryDate;

    private Map<String, String> complianceMapping;

    private Set<String> exceptions;

    private Boolean autoRemediationEnabled = false;

    private Boolean notificationEnabled = true;

    private Boolean loggingEnabled = true;

    private Boolean monitoringEnabled = true;

    private String changeReason;

    private Long violationCount = 0L;

    private Long executionCount = 0L;

    private Double successRate = 100.0;

    // Enums
    public enum PolicyType {
        AUTHENTICATION,
        AUTHORIZATION,
        ACCESS_CONTROL,
        DATA_PROTECTION,
        NETWORK_SECURITY,
        ENDPOINT_SECURITY,
        COMPLIANCE,
        INCIDENT_RESPONSE,
        THREAT_DETECTION,
        CUSTOM
    }

    public enum PolicyModel {
        RBAC,    // Role-Based Access Control
        ABAC,    // Attribute-Based Access Control
        PBAC,    // Policy-Based Access Control
        MAC,     // Mandatory Access Control
        DAC,     // Discretionary Access Control
        HYBRID
    }

    public enum PolicyLanguage {
        JSON,
        YAML,
        XACML,
        OPA_REGO,
        CUSTOM_DSL
    }

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum EnforcementMode {
        ENFORCING,
        PERMISSIVE,
        DISABLED,
        AUDIT_ONLY
    }

    public enum Decision {
        ALLOW,
        DENY,
        NOT_APPLICABLE,
        INDETERMINATE
    }

    public enum PolicyStatus {
        DRAFT,
        PENDING_APPROVAL,
        ACTIVE,
        INACTIVE,
        EXPIRED,
        SUSPENDED,
        UNDER_REVIEW,
        DEPRECATED
    }

    public enum PriorityLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL,
        URGENT
    }

    public enum PolicyCategory {
        ACCESS_CONTROL,
        AUTHENTICATION,
        AUTHORIZATION,
        DATA_PROTECTION,
        NETWORK_SECURITY,
        ENDPOINT_SECURITY,
        COMPLIANCE,
        INCIDENT_RESPONSE,
        THREAT_PREVENTION,
        MONITORING,
        AUDIT,
        ENCRYPTION,
        IDENTITY_MANAGEMENT,
        PRIVACY,
        BUSINESS_CONTINUITY
    }

    // Constructors
    public SecurityPolicy() {
    }

    public SecurityPolicy(String policyName, PolicyType policyType, PolicyModel policyModel, String policyDefinition) {
        this.policyName = policyName;
        this.policyType = policyType;
        this.policyModel = policyModel;
        this.policyDefinition = policyDefinition;
    }

    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public PolicyType getPolicyType() { return policyType; }
    public void setPolicyType(PolicyType policyType) { this.policyType = policyType; }

    public PolicyModel getPolicyModel() { return policyModel; }
    public void setPolicyModel(PolicyModel policyModel) { this.policyModel = policyModel; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getPolicyVersion() { return policyVersion; }
    public void setPolicyVersion(Integer policyVersion) { this.policyVersion = policyVersion; }

    public String getPolicyDefinition() { return policyDefinition; }
    public void setPolicyDefinition(String policyDefinition) { this.policyDefinition = policyDefinition; }

    public PolicyLanguage getPolicyLanguage() { return policyLanguage; }
    public void setPolicyLanguage(PolicyLanguage policyLanguage) { this.policyLanguage = policyLanguage; }

    public Set<String> getSubjects() { return subjects; }
    public void setSubjects(Set<String> subjects) { this.subjects = subjects; }

    public Set<String> getResources() { return resources; }
    public void setResources(Set<String> resources) { this.resources = resources; }

    public Set<String> getActions() { return actions; }
    public void setActions(Set<String> actions) { this.actions = actions; }

    public Map<String, String> getConditions() { return conditions; }
    public void setConditions(Map<String, String> conditions) { this.conditions = conditions; }

    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }

    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }

    public Map<String, String> getTimeRestrictions() { return timeRestrictions; }
    public void setTimeRestrictions(Map<String, String> timeRestrictions) { this.timeRestrictions = timeRestrictions; }

    public Set<String> getAllowedLocations() { return allowedLocations; }
    public void setAllowedLocations(Set<String> allowedLocations) { this.allowedLocations = allowedLocations; }

    public Set<String> getIpRestrictions() { return ipRestrictions; }
    public void setIpRestrictions(Set<String> ipRestrictions) { this.ipRestrictions = ipRestrictions; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public Set<String> getComplianceStandards() { return complianceStandards; }
    public void setComplianceStandards(Set<String> complianceStandards) { this.complianceStandards = complianceStandards; }

    public EnforcementMode getEnforcementMode() { return enforcementMode; }
    public void setEnforcementMode(EnforcementMode enforcementMode) { this.enforcementMode = enforcementMode; }

    public Decision getDefaultDecision() { return defaultDecision; }
    public void setDefaultDecision(Decision defaultDecision) { this.defaultDecision = defaultDecision; }

    public Boolean getEnableMonitoring() { return enableMonitoring; }
    public void setEnableMonitoring(Boolean enableMonitoring) { this.enableMonitoring = enableMonitoring; }

    public Boolean getEnableAlerting() { return enableAlerting; }
    public void setEnableAlerting(Boolean enableAlerting) { this.enableAlerting = enableAlerting; }

    public Integer getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Integer alertThreshold) { this.alertThreshold = alertThreshold; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public PolicyStatus getPolicyStatus() { return policyStatus; }
    public void setPolicyStatus(PolicyStatus policyStatus) { this.policyStatus = policyStatus; }

    public PriorityLevel getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }

    public PolicyCategory getPolicyCategory() { return policyCategory; }
    public void setPolicyCategory(PolicyCategory policyCategory) { this.policyCategory = policyCategory; }

    public String getDomainScope() { return domainScope; }
    public void setDomainScope(String domainScope) { this.domainScope = domainScope; }

    public Set<String> getApplicableServices() { return applicableServices; }
    public void setApplicableServices(Set<String> applicableServices) { this.applicableServices = applicableServices; }

    public Set<String> getApplicableRoles() { return applicableRoles; }
    public void setApplicableRoles(Set<String> applicableRoles) { this.applicableRoles = applicableRoles; }

    public Set<String> getPolicyRules() { return policyRules; }
    public void setPolicyRules(Set<String> policyRules) { this.policyRules = policyRules; }

    public Set<String> getEnforcementActions() { return enforcementActions; }
    public void setEnforcementActions(Set<String> enforcementActions) { this.enforcementActions = enforcementActions; }

    public LocalDateTime getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDateTime effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public Map<String, String> getComplianceMapping() { return complianceMapping; }
    public void setComplianceMapping(Map<String, String> complianceMapping) { this.complianceMapping = complianceMapping; }

    public Set<String> getExceptions() { return exceptions; }
    public void setExceptions(Set<String> exceptions) { this.exceptions = exceptions; }

    public Boolean getAutoRemediationEnabled() { return autoRemediationEnabled; }
    public void setAutoRemediationEnabled(Boolean autoRemediationEnabled) { this.autoRemediationEnabled = autoRemediationEnabled; }

    public Boolean getNotificationEnabled() { return notificationEnabled; }
    public void setNotificationEnabled(Boolean notificationEnabled) { this.notificationEnabled = notificationEnabled; }

    public Boolean getLoggingEnabled() { return loggingEnabled; }
    public void setLoggingEnabled(Boolean loggingEnabled) { this.loggingEnabled = loggingEnabled; }

    public Boolean getMonitoringEnabled() { return monitoringEnabled; }
    public void setMonitoringEnabled(Boolean monitoringEnabled) { this.monitoringEnabled = monitoringEnabled; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public Long getViolationCount() { return violationCount; }
    public void setViolationCount(Long violationCount) { this.violationCount = violationCount; }

    public Long getExecutionCount() { return executionCount; }
    public void setExecutionCount(Long executionCount) { this.executionCount = executionCount; }

    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }

    // Business methods
    public void updateVersion(String version, String updatedBy) {
        this.policyVersion = Integer.parseInt(version);
        this.updatedBy = updatedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean requiresApproval() {
        return this.policyStatus == PolicyStatus.PENDING_APPROVAL;
    }

    public void approve(String approvedBy) {
        this.policyStatus = PolicyStatus.ACTIVE;
        this.approvedBy = approvedBy;
        this.approvalDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.policyStatus = PolicyStatus.ACTIVE;
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.policyStatus = PolicyStatus.INACTIVE;
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementViolation() {
        this.violationCount = (this.violationCount != null ? this.violationCount : 0L) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementExecution() {
        this.executionCount = (this.executionCount != null ? this.executionCount : 0L) + 1;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return this.expiryDate != null && this.expiryDate.isBefore(LocalDateTime.now());
    }

    public boolean isActivePolicy() {
        return this.isActive && this.policyStatus == PolicyStatus.ACTIVE &&
               (this.expiryDate == null || this.expiryDate.isAfter(LocalDateTime.now()));
    }
}
