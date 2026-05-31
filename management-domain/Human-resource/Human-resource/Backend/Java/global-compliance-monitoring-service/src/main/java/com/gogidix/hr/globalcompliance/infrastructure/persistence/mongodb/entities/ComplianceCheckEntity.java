package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * MongoDB Entity for ComplianceCheck
 * Maps domain model to database representation
 */
@Document(collection = "compliance_checks")
public class ComplianceCheckEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("check_id")
    private String checkId;

    @Field("check_number")
    private String checkNumber;

    @Field("requirement_id")
    private String requirementId;

    @Field("requirement_name")
    private String requirementName;

    @Field("country_code")
    private String countryCode;

    @Field("status")
    private String status;

    @Field("scheduled_date")
    private LocalDate scheduledDate;

    @Field("completed_date")
    private LocalDate completedDate;

    @Field("checked_by")
    private String checkedBy;

    @Field("checked_by_name")
    private String checkedByName;

    @Field("result")
    private String result;

    @Field("findings")
    private String findings;

    @Field("corrective_action")
    private String correctiveAction;

    @Field("target_completion_date")
    private LocalDate targetCompletionDate;

    @Field("actual_completion_date")
    private LocalDate actualCompletionDate;

    @Field("supporting_documents")
    private List<String> supportingDocuments;

    @Field("issues")
    private List<IssueReferenceData> issues;

    @Field("comments")
    private String comments;

    @Field("frequency")
    private String frequency;

    @Field("domain_events")
    private List<Object> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;

    // Nested class for IssueReference
    public static class IssueReferenceData {
        private String issueId;
        private String issueNumber;
        private String severity;

        public IssueReferenceData() {
        }

        public IssueReferenceData(String issueId, String issueNumber, String severity) {
            this.issueId = issueId;
            this.issueNumber = issueNumber;
            this.severity = severity;
        }

        public String getIssueId() { return issueId; }
        public void setIssueId(String issueId) { this.issueId = issueId; }

        public String getIssueNumber() { return issueNumber; }
        public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
    }

    // Default constructor for MongoDB
    public ComplianceCheckEntity() {
    }

    // Builder pattern for entity creation
    private ComplianceCheckEntity(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.checkId = builder.checkId;
        this.checkNumber = builder.checkNumber;
        this.requirementId = builder.requirementId;
        this.requirementName = builder.requirementName;
        this.countryCode = builder.countryCode;
        this.status = builder.status;
        this.scheduledDate = builder.scheduledDate;
        this.completedDate = builder.completedDate;
        this.checkedBy = builder.checkedBy;
        this.checkedByName = builder.checkedByName;
        this.result = builder.result;
        this.findings = builder.findings;
        this.correctiveAction = builder.correctiveAction;
        this.targetCompletionDate = builder.targetCompletionDate;
        this.actualCompletionDate = builder.actualCompletionDate;
        this.supportingDocuments = builder.supportingDocuments;
        this.issues = builder.issues;
        this.comments = builder.comments;
        this.frequency = builder.frequency;
        this.domainEvents = builder.domainEvents;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.updatedBy = builder.updatedBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getCheckId() { return checkId; }
    public String getCheckNumber() { return checkNumber; }
    public String getRequirementId() { return requirementId; }
    public String getRequirementName() { return requirementName; }
    public String getCountryCode() { return countryCode; }
    public String getStatus() { return status; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public LocalDate getCompletedDate() { return completedDate; }
    public String getCheckedBy() { return checkedBy; }
    public String getCheckedByName() { return checkedByName; }
    public String getResult() { return result; }
    public String getFindings() { return findings; }
    public String getCorrectiveAction() { return correctiveAction; }
    public LocalDate getTargetCompletionDate() { return targetCompletionDate; }
    public LocalDate getActualCompletionDate() { return actualCompletionDate; }
    public List<String> getSupportingDocuments() { return supportingDocuments; }
    public List<IssueReferenceData> getIssues() { return issues; }
    public String getComments() { return comments; }
    public String getFrequency() { return frequency; }
    public List<Object> getDomainEvents() { return domainEvents; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }

    // Setters (for MongoDB mapping)
    public void setId(String id) { this.id = id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setCheckId(String checkId) { this.checkId = checkId; }
    public void setCheckNumber(String checkNumber) { this.checkNumber = checkNumber; }
    public void setRequirementId(String requirementId) { this.requirementId = requirementId; }
    public void setRequirementName(String requirementName) { this.requirementName = requirementName; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setStatus(String status) { this.status = status; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
    public void setCompletedDate(LocalDate completedDate) { this.completedDate = completedDate; }
    public void setCheckedBy(String checkedBy) { this.checkedBy = checkedBy; }
    public void setCheckedByName(String checkedByName) { this.checkedByName = checkedByName; }
    public void setResult(String result) { this.result = result; }
    public void setFindings(String findings) { this.findings = findings; }
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    public void setTargetCompletionDate(LocalDate targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; }
    public void setActualCompletionDate(LocalDate actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; }
    public void setSupportingDocuments(List<String> supportingDocuments) { this.supportingDocuments = supportingDocuments; }
    public void setIssues(List<IssueReferenceData> issues) { this.issues = issues; }
    public void setComments(String comments) { this.comments = comments; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setDomainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplianceCheckEntity that = (ComplianceCheckEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public static class Builder {
        private String id;
        private String tenantId;
        private String checkId;
        private String checkNumber;
        private String requirementId;
        private String requirementName;
        private String countryCode;
        private String status;
        private LocalDate scheduledDate;
        private LocalDate completedDate;
        private String checkedBy;
        private String checkedByName;
        private String result;
        private String findings;
        private String correctiveAction;
        private LocalDate targetCompletionDate;
        private LocalDate actualCompletionDate;
        private List<String> supportingDocuments;
        private List<IssueReferenceData> issues;
        private String comments;
        private String frequency;
        private List<Object> domainEvents;
        private Instant createdAt;
        private Instant updatedAt;
        private String updatedBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder checkId(String checkId) { this.checkId = checkId; return this; }
        public Builder checkNumber(String checkNumber) { this.checkNumber = checkNumber; return this; }
        public Builder requirementId(String requirementId) { this.requirementId = requirementId; return this; }
        public Builder requirementName(String requirementName) { this.requirementName = requirementName; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder scheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; return this; }
        public Builder completedDate(LocalDate completedDate) { this.completedDate = completedDate; return this; }
        public Builder checkedBy(String checkedBy) { this.checkedBy = checkedBy; return this; }
        public Builder checkedByName(String checkedByName) { this.checkedByName = checkedByName; return this; }
        public Builder result(String result) { this.result = result; return this; }
        public Builder findings(String findings) { this.findings = findings; return this; }
        public Builder correctiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; return this; }
        public Builder targetCompletionDate(LocalDate targetCompletionDate) { this.targetCompletionDate = targetCompletionDate; return this; }
        public Builder actualCompletionDate(LocalDate actualCompletionDate) { this.actualCompletionDate = actualCompletionDate; return this; }
        public Builder supportingDocuments(List<String> supportingDocuments) { this.supportingDocuments = supportingDocuments; return this; }
        public Builder issues(List<IssueReferenceData> issues) { this.issues = issues; return this; }
        public Builder comments(String comments) { this.comments = comments; return this; }
        public Builder frequency(String frequency) { this.frequency = frequency; return this; }
        public Builder domainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }

        public ComplianceCheckEntity build() {
            return new ComplianceCheckEntity(this);
        }
    }
}
