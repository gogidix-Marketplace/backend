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
 * MongoDB Entity for NonComplianceIssue
 * Maps domain model to database representation
 */
@Document(collection = "non_compliance_issues")
public class NonComplianceIssueEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("issue_id")
    private String issueId;

    @Field("issue_number")
    private String issueNumber;

    @Field("requirement_id")
    private String requirementId;

    @Field("check_id")
    private String checkId;

    @Field("country_code")
    private String countryCode;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("severity")
    private String severity;

    @Field("status")
    private String status;

    @Field("identified_date")
    private LocalDate identifiedDate;

    @Field("identified_by")
    private String identifiedBy;

    @Field("identified_by_name")
    private String identifiedByName;

    @Field("assigned_to")
    private String assignedTo;

    @Field("assigned_to_name")
    private String assignedToName;

    @Field("due_date")
    private LocalDate dueDate;

    @Field("resolved_date")
    private LocalDate resolvedDate;

    @Field("resolution")
    private String resolution;

    @Field("root_cause")
    private String rootCause;

    @Field("affected_employees")
    private List<String> affectedEmployees;

    @Field("financial_impact")
    private Double financialImpact;

    @Field("currency")
    private String currency;

    @Field("actions")
    private List<String> actions;

    @Field("department")
    private String department;

    @Field("location")
    private String location;

    @Field("domain_events")
    private List<Object> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;

    // Default constructor for MongoDB
    public NonComplianceIssueEntity() {
    }

    // Builder pattern for entity creation
    private NonComplianceIssueEntity(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.issueId = builder.issueId;
        this.issueNumber = builder.issueNumber;
        this.requirementId = builder.requirementId;
        this.checkId = builder.checkId;
        this.countryCode = builder.countryCode;
        this.title = builder.title;
        this.description = builder.description;
        this.severity = builder.severity;
        this.status = builder.status;
        this.identifiedDate = builder.identifiedDate;
        this.identifiedBy = builder.identifiedBy;
        this.identifiedByName = builder.identifiedByName;
        this.assignedTo = builder.assignedTo;
        this.assignedToName = builder.assignedToName;
        this.dueDate = builder.dueDate;
        this.resolvedDate = builder.resolvedDate;
        this.resolution = builder.resolution;
        this.rootCause = builder.rootCause;
        this.affectedEmployees = builder.affectedEmployees;
        this.financialImpact = builder.financialImpact;
        this.currency = builder.currency;
        this.actions = builder.actions;
        this.department = builder.department;
        this.location = builder.location;
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
    public String getIssueId() { return issueId; }
    public String getIssueNumber() { return issueNumber; }
    public String getRequirementId() { return requirementId; }
    public String getCheckId() { return checkId; }
    public String getCountryCode() { return countryCode; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public String getStatus() { return status; }
    public LocalDate getIdentifiedDate() { return identifiedDate; }
    public String getIdentifiedBy() { return identifiedBy; }
    public String getIdentifiedByName() { return identifiedByName; }
    public String getAssignedTo() { return assignedTo; }
    public String getAssignedToName() { return assignedToName; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getResolvedDate() { return resolvedDate; }
    public String getResolution() { return resolution; }
    public String getRootCause() { return rootCause; }
    public List<String> getAffectedEmployees() { return affectedEmployees; }
    public Double getFinancialImpact() { return financialImpact; }
    public String getCurrency() { return currency; }
    public List<String> getActions() { return actions; }
    public String getDepartment() { return department; }
    public String getLocation() { return location; }
    public List<Object> getDomainEvents() { return domainEvents; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }

    // Setters (for MongoDB mapping)
    public void setId(String id) { this.id = id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setIssueId(String issueId) { this.issueId = issueId; }
    public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }
    public void setRequirementId(String requirementId) { this.requirementId = requirementId; }
    public void setCheckId(String checkId) { this.checkId = checkId; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setStatus(String status) { this.status = status; }
    public void setIdentifiedDate(LocalDate identifiedDate) { this.identifiedDate = identifiedDate; }
    public void setIdentifiedBy(String identifiedBy) { this.identifiedBy = identifiedBy; }
    public void setIdentifiedByName(String identifiedByName) { this.identifiedByName = identifiedByName; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setResolvedDate(LocalDate resolvedDate) { this.resolvedDate = resolvedDate; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public void setRootCause(String rootCause) { this.rootCause = rootCause; }
    public void setAffectedEmployees(List<String> affectedEmployees) { this.affectedEmployees = affectedEmployees; }
    public void setFinancialImpact(Double financialImpact) { this.financialImpact = financialImpact; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setActions(List<String> actions) { this.actions = actions; }
    public void setDepartment(String department) { this.department = department; }
    public void setLocation(String location) { this.location = location; }
    public void setDomainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonComplianceIssueEntity that = (NonComplianceIssueEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public static class Builder {
        private String id;
        private String tenantId;
        private String issueId;
        private String issueNumber;
        private String requirementId;
        private String checkId;
        private String countryCode;
        private String title;
        private String description;
        private String severity;
        private String status;
        private LocalDate identifiedDate;
        private String identifiedBy;
        private String identifiedByName;
        private String assignedTo;
        private String assignedToName;
        private LocalDate dueDate;
        private LocalDate resolvedDate;
        private String resolution;
        private String rootCause;
        private List<String> affectedEmployees;
        private Double financialImpact;
        private String currency;
        private List<String> actions;
        private String department;
        private String location;
        private List<Object> domainEvents;
        private Instant createdAt;
        private Instant updatedAt;
        private String updatedBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder issueId(String issueId) { this.issueId = issueId; return this; }
        public Builder issueNumber(String issueNumber) { this.issueNumber = issueNumber; return this; }
        public Builder requirementId(String requirementId) { this.requirementId = requirementId; return this; }
        public Builder checkId(String checkId) { this.checkId = checkId; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder severity(String severity) { this.severity = severity; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder identifiedDate(LocalDate identifiedDate) { this.identifiedDate = identifiedDate; return this; }
        public Builder identifiedBy(String identifiedBy) { this.identifiedBy = identifiedBy; return this; }
        public Builder identifiedByName(String identifiedByName) { this.identifiedByName = identifiedByName; return this; }
        public Builder assignedTo(String assignedTo) { this.assignedTo = assignedTo; return this; }
        public Builder assignedToName(String assignedToName) { this.assignedToName = assignedToName; return this; }
        public Builder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public Builder resolvedDate(LocalDate resolvedDate) { this.resolvedDate = resolvedDate; return this; }
        public Builder resolution(String resolution) { this.resolution = resolution; return this; }
        public Builder rootCause(String rootCause) { this.rootCause = rootCause; return this; }
        public Builder affectedEmployees(List<String> affectedEmployees) { this.affectedEmployees = affectedEmployees; return this; }
        public Builder financialImpact(Double financialImpact) { this.financialImpact = financialImpact; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Builder actions(List<String> actions) { this.actions = actions; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder domainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }

        public NonComplianceIssueEntity build() {
            return new NonComplianceIssueEntity(this);
        }
    }
}
