package com.gogidix.finance.compliance.infrastructure.persistence.mongodb;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MongoDB document entity for storing ComplianceRule domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "compliance_rules")
public class ComplianceRuleEntity {

    @Id
    private String id;

    @Indexed
    @Field("rule_id")
    private String ruleId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Indexed
    @Field("rule_type")
    private String ruleType;

    @Indexed
    @Field("category")
    private String category;

    @Field("severity")
    private String severity;

    @Indexed
    @Field("enabled")
    private Boolean enabled;

    @Field("parameters")
    private Map<String, Object> parameters;

    @Field("threshold_amount")
    private BigDecimal thresholdAmount;

    @Field("threshold_currency")
    private String thresholdCurrency;

    @Field("condition_expression")
    private String conditionExpression;

    @Field("applicable_departments")
    private List<String> applicableDepartments;

    @Field("applicable_cost_centers")
    private List<String> applicableCostCenters;

    @Field("applicable_expense_categories")
    private List<String> applicableExpenseCategories;

    @Field("created_by_user_id")
    private String createdByUserId;

    @Field("last_modified_by_user_id")
    private String lastModifiedByUserId;

    @Field("effective_from")
    private Instant effectiveFrom;

    @Field("effective_to")
    private Instant effectiveTo;

    @Indexed
    @Field("status")
    private String status;

    @Field("approval_required_by")
    private String approvalRequiredBy;

    @Field("auto_approve_threshold")
    private Boolean autoApproveThreshold;

    @Field("priority")
    private Integer priority;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    // Default constructor for MongoDB
    public ComplianceRuleEntity() {
    }

    // Constructor from domain model
    public ComplianceRuleEntity(ComplianceRule rule) {
        this.id = rule.getRuleId(); // Use ruleId as the MongoDB _id
        this.ruleId = rule.getRuleId();
        this.tenantId = rule.getTenantId();
        this.name = rule.getName();
        this.description = rule.getDescription();
        this.ruleType = rule.getRuleType() != null ? rule.getRuleType().name() : null;
        this.category = rule.getCategory() != null ? rule.getCategory().name() : null;
        this.severity = rule.getSeverity() != null ? rule.getSeverity().name() : null;
        this.enabled = rule.getEnabled();
        this.parameters = rule.getParameters();
        this.thresholdAmount = rule.getThresholdAmount();
        this.thresholdCurrency = rule.getThresholdCurrency();
        this.conditionExpression = rule.getConditionExpression();
        this.applicableDepartments = rule.getApplicableDepartments();
        this.applicableCostCenters = rule.getApplicableCostCenters();
        this.applicableExpenseCategories = rule.getApplicableExpenseCategories();
        this.createdByUserId = rule.getCreatedByUserId();
        this.lastModifiedByUserId = rule.getLastModifiedByUserId();
        this.effectiveFrom = rule.getEffectiveFrom();
        this.effectiveTo = rule.getEffectiveTo();
        this.status = rule.getStatus() != null ? rule.getStatus().name() : null;
        this.approvalRequiredBy = rule.getApprovalRequiredBy();
        this.autoApproveThreshold = rule.getAutoApproveThreshold();
        this.priority = rule.getPriority();
        this.tags = rule.getTags();
        this.notes = rule.getNotes();
    }

    public ComplianceRule toDomainModel() {
        return ComplianceRule.builder()
            .ruleId(ruleId)
            .tenantId(tenantId)
            .name(name)
            .description(description)
            .ruleType(ruleType != null ? ComplianceRule.RuleType.valueOf(ruleType) : null)
            .category(category != null ? ComplianceRule.RuleCategory.valueOf(category) : null)
            .severity(severity != null ? ComplianceRule.SeverityLevel.valueOf(severity) : null)
            .enabled(enabled)
            .parameters(parameters)
            .thresholdAmount(thresholdAmount)
            .thresholdCurrency(thresholdCurrency)
            .conditionExpression(conditionExpression)
            .applicableDepartments(applicableDepartments)
            .applicableCostCenters(applicableCostCenters)
            .applicableExpenseCategories(applicableExpenseCategories)
            .createdByUserId(createdByUserId)
            .lastModifiedByUserId(lastModifiedByUserId)
            .effectiveFrom(effectiveFrom)
            .effectiveTo(effectiveTo)
            .status(status != null ? ComplianceRule.RuleStatus.valueOf(status) : null)
            .approvalRequiredBy(approvalRequiredBy)
            .autoApproveThreshold(autoApproveThreshold)
            .priority(priority)
            .tags(tags)
            .notes(notes)
            .domainEvents(new ArrayList<>()) // Domain events not persisted
            .build();
    }

    // Getters and setters for MongoDB
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public BigDecimal getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(BigDecimal thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

    public String getThresholdCurrency() {
        return thresholdCurrency;
    }

    public void setThresholdCurrency(String thresholdCurrency) {
        this.thresholdCurrency = thresholdCurrency;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public List<String> getApplicableDepartments() {
        return applicableDepartments;
    }

    public void setApplicableDepartments(List<String> applicableDepartments) {
        this.applicableDepartments = applicableDepartments;
    }

    public List<String> getApplicableCostCenters() {
        return applicableCostCenters;
    }

    public void setApplicableCostCenters(List<String> applicableCostCenters) {
        this.applicableCostCenters = applicableCostCenters;
    }

    public List<String> getApplicableExpenseCategories() {
        return applicableExpenseCategories;
    }

    public void setApplicableExpenseCategories(List<String> applicableExpenseCategories) {
        this.applicableExpenseCategories = applicableExpenseCategories;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public Instant getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Instant effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Instant getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(Instant effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalRequiredBy() {
        return approvalRequiredBy;
    }

    public void setApprovalRequiredBy(String approvalRequiredBy) {
        this.approvalRequiredBy = approvalRequiredBy;
    }

    public Boolean getAutoApproveThreshold() {
        return autoApproveThreshold;
    }

    public void setAutoApproveThreshold(Boolean autoApproveThreshold) {
        this.autoApproveThreshold = autoApproveThreshold;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
