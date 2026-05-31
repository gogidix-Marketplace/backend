package com.gogidix.finance.compliance.infrastructure.persistence.mongodb;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
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
 * MongoDB document entity for storing ComplianceCheck domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "compliance_checks")
public class ComplianceCheckEntity {

    @Id
    private String id;

    @Indexed
    @Field("check_id")
    private String checkId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("rule_id")
    private String ruleId;

    @Field("rule_name")
    private String ruleName;

    @Field("entity_type")
    private String entityType;

    @Field("entity_id")
    private String entityId;

    @Field("reference_number")
    private String referenceNumber;

    @Indexed
    @Field("status")
    private String status;

    @Indexed
    @Field("result")
    private String result;

    @Field("severity")
    private String severity;

    @Field("violation_description")
    private String violationDescription;

    @Field("evaluated_context")
    private Map<String, Object> evaluatedContext;

    @Field("evaluated_amount")
    private BigDecimal evaluatedAmount;

    @Field("evaluated_currency")
    private String evaluatedCurrency;

    @Field("threshold_amount")
    private BigDecimal thresholdAmount;

    @Field("threshold_currency")
    private String thresholdCurrency;

    @Field("variance")
    private BigDecimal variance;

    @Indexed
    @Field("department")
    private String department;

    @Indexed
    @Field("cost_center")
    private String costCenter;

    @Field("expense_category")
    private String expenseCategory;

    @Field("evaluated_by")
    private String evaluatedBy;

    @Field("evaluated_by_user_id")
    private String evaluatedByUserId;

    @Field("evaluated_at")
    private Instant evaluatedAt;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("approval_notes")
    private String approvalNotes;

    @Field("waived")
    private Boolean waived;

    @Field("waived_by")
    private String waivedBy;

    @Field("waived_at")
    private Instant waivedAt;

    @Field("waiver_reason")
    private String waiverReason;

    @Field("remediation_required")
    private Boolean remediationRequired;

    @Field("remediation_action")
    private String remediationAction;

    @Field("remediation_assigned_to")
    private String remediationAssignedTo;

    @Field("remediation_due_date")
    private Instant remediationDueDate;

    @Field("remediation_completed_at")
    private Instant remediationCompletedAt;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("correlation_id")
    private String correlationId;

    @Field("violation_details")
    private List<ComplianceCheck.CheckViolationDetail> violationDetails;

    // Default constructor for MongoDB
    public ComplianceCheckEntity() {
    }

    // Constructor from domain model
    public ComplianceCheckEntity(ComplianceCheck check) {
        this.id = check.getCheckId(); // Use checkId as the MongoDB _id
        this.checkId = check.getCheckId();
        this.tenantId = check.getTenantId();
        this.ruleId = check.getRuleId();
        this.ruleName = check.getRuleName();
        this.entityType = check.getEntityType();
        this.entityId = check.getEntityId();
        this.referenceNumber = check.getReferenceNumber();
        this.status = check.getStatus() != null ? check.getStatus().name() : null;
        this.result = check.getResult() != null ? check.getResult().name() : null;
        this.severity = check.getSeverity() != null ? check.getSeverity().name() : null;
        this.violationDescription = check.getViolationDescription();
        this.evaluatedContext = check.getEvaluatedContext();
        this.evaluatedAmount = check.getEvaluatedAmount();
        this.evaluatedCurrency = check.getEvaluatedCurrency();
        this.thresholdAmount = check.getThresholdAmount();
        this.thresholdCurrency = check.getThresholdCurrency();
        this.variance = check.getVariance();
        this.department = check.getDepartment();
        this.costCenter = check.getCostCenter();
        this.expenseCategory = check.getExpenseCategory();
        this.evaluatedBy = check.getEvaluatedBy();
        this.evaluatedByUserId = check.getEvaluatedByUserId();
        this.evaluatedAt = check.getEvaluatedAt();
        this.approvedBy = check.getApprovedBy();
        this.approvedAt = check.getApprovedAt();
        this.approvalNotes = check.getApprovalNotes();
        this.waived = check.getWaived();
        this.waivedBy = check.getWaivedBy();
        this.waivedAt = check.getWaivedAt();
        this.waiverReason = check.getWaiverReason();
        this.remediationRequired = check.getRemediationRequired();
        this.remediationAction = check.getRemediationAction();
        this.remediationAssignedTo = check.getRemediationAssignedTo();
        this.remediationDueDate = check.getRemediationDueDate();
        this.remediationCompletedAt = check.getRemediationCompletedAt();
        this.tags = check.getTags();
        this.notes = check.getNotes();
        this.correlationId = check.getCorrelationId();
        this.violationDetails = check.getViolationDetails();
    }

    public ComplianceCheck toDomainModel() {
        return ComplianceCheck.builder()
            .checkId(checkId)
            .tenantId(tenantId)
            .ruleId(ruleId)
            .ruleName(ruleName)
            .entityType(entityType)
            .entityId(entityId)
            .referenceNumber(referenceNumber)
            .status(status != null ? ComplianceCheck.CheckStatus.valueOf(status) : null)
            .result(result != null ? ComplianceCheck.CheckResult.valueOf(result) : null)
            .severity(severity != null ? ComplianceCheck.SeverityLevel.valueOf(severity) : null)
            .violationDescription(violationDescription)
            .evaluatedContext(evaluatedContext)
            .evaluatedAmount(evaluatedAmount)
            .evaluatedCurrency(evaluatedCurrency)
            .thresholdAmount(thresholdAmount)
            .thresholdCurrency(thresholdCurrency)
            .variance(variance)
            .department(department)
            .costCenter(costCenter)
            .expenseCategory(expenseCategory)
            .evaluatedBy(evaluatedBy)
            .evaluatedByUserId(evaluatedByUserId)
            .evaluatedAt(evaluatedAt)
            .approvedBy(approvedBy)
            .approvedAt(approvedAt)
            .approvalNotes(approvalNotes)
            .waived(waived)
            .waivedBy(waivedBy)
            .waivedAt(waivedAt)
            .waiverReason(waiverReason)
            .remediationRequired(remediationRequired)
            .remediationAction(remediationAction)
            .remediationAssignedTo(remediationAssignedTo)
            .remediationDueDate(remediationDueDate)
            .remediationCompletedAt(remediationCompletedAt)
            .tags(tags)
            .notes(notes)
            .correlationId(correlationId)
            .violationDetails(violationDetails)
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

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getViolationDescription() {
        return violationDescription;
    }

    public void setViolationDescription(String violationDescription) {
        this.violationDescription = violationDescription;
    }

    public Map<String, Object> getEvaluatedContext() {
        return evaluatedContext;
    }

    public void setEvaluatedContext(Map<String, Object> evaluatedContext) {
        this.evaluatedContext = evaluatedContext;
    }

    public BigDecimal getEvaluatedAmount() {
        return evaluatedAmount;
    }

    public void setEvaluatedAmount(BigDecimal evaluatedAmount) {
        this.evaluatedAmount = evaluatedAmount;
    }

    public String getEvaluatedCurrency() {
        return evaluatedCurrency;
    }

    public void setEvaluatedCurrency(String evaluatedCurrency) {
        this.evaluatedCurrency = evaluatedCurrency;
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

    public BigDecimal getVariance() {
        return variance;
    }

    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getEvaluatedBy() {
        return evaluatedBy;
    }

    public void setEvaluatedBy(String evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }

    public String getEvaluatedByUserId() {
        return evaluatedByUserId;
    }

    public void setEvaluatedByUserId(String evaluatedByUserId) {
        this.evaluatedByUserId = evaluatedByUserId;
    }

    public Instant getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(Instant evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getApprovalNotes() {
        return approvalNotes;
    }

    public void setApprovalNotes(String approvalNotes) {
        this.approvalNotes = approvalNotes;
    }

    public Boolean getWaived() {
        return waived;
    }

    public void setWaived(Boolean waived) {
        this.waived = waived;
    }

    public String getWaivedBy() {
        return waivedBy;
    }

    public void setWaivedBy(String waivedBy) {
        this.waivedBy = waivedBy;
    }

    public Instant getWaivedAt() {
        return waivedAt;
    }

    public void setWaivedAt(Instant waivedAt) {
        this.waivedAt = waivedAt;
    }

    public String getWaiverReason() {
        return waiverReason;
    }

    public void setWaiverReason(String waiverReason) {
        this.waiverReason = waiverReason;
    }

    public Boolean getRemediationRequired() {
        return remediationRequired;
    }

    public void setRemediationRequired(Boolean remediationRequired) {
        this.remediationRequired = remediationRequired;
    }

    public String getRemediationAction() {
        return remediationAction;
    }

    public void setRemediationAction(String remediationAction) {
        this.remediationAction = remediationAction;
    }

    public String getRemediationAssignedTo() {
        return remediationAssignedTo;
    }

    public void setRemediationAssignedTo(String remediationAssignedTo) {
        this.remediationAssignedTo = remediationAssignedTo;
    }

    public Instant getRemediationDueDate() {
        return remediationDueDate;
    }

    public void setRemediationDueDate(Instant remediationDueDate) {
        this.remediationDueDate = remediationDueDate;
    }

    public Instant getRemediationCompletedAt() {
        return remediationCompletedAt;
    }

    public void setRemediationCompletedAt(Instant remediationCompletedAt) {
        this.remediationCompletedAt = remediationCompletedAt;
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

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public List<ComplianceCheck.CheckViolationDetail> getViolationDetails() {
        return violationDetails;
    }

    public void setViolationDetails(List<ComplianceCheck.CheckViolationDetail> violationDetails) {
        this.violationDetails = violationDetails;
    }
}
