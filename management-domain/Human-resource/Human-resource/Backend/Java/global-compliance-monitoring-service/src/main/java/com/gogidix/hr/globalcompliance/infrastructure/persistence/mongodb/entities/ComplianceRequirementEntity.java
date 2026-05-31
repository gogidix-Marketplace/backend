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
 * MongoDB Entity for ComplianceRequirement
 * Maps domain model to database representation
 */
@Document(collection = "compliance_requirements")
public class ComplianceRequirementEntity {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("requirement_id")
    private String requirementId;

    @Field("requirement_code")
    private String requirementCode;

    @Field("requirement_name")
    private String requirementName;

    @Field("category")
    private String category;

    @Field("country_code")
    private String countryCode;

    @Field("description")
    private String description;

    @Field("authority")
    private String authority;

    @Field("type")
    private String type;

    @Field("effective_from")
    private LocalDate effectiveFrom;

    @Field("effective_to")
    private LocalDate effectiveTo;

    @Field("review_date")
    private LocalDate reviewDate;

    @Field("frequency")
    private String frequency;

    @Field("severity")
    private String severity;

    @Field("active")
    private Boolean active;

    @Field("owner_department")
    private String ownerDepartment;

    @Field("owner_id")
    private String ownerId;

    @Field("checks")
    private List<ComplianceCheckInfoData> checks;

    @Field("related_requirements")
    private List<String> relatedRequirements;

    @Field("domain_events")
    private List<Object> domainEvents;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("updated_by")
    private String updatedBy;

    // Nested class for ComplianceCheckInfo
    public static class ComplianceCheckInfoData {
        private String checkId;
        private LocalDate scheduledDate;
        private String status;

        public ComplianceCheckInfoData() {
        }

        public ComplianceCheckInfoData(String checkId, LocalDate scheduledDate, String status) {
            this.checkId = checkId;
            this.scheduledDate = scheduledDate;
            this.status = status;
        }

        public String getCheckId() { return checkId; }
        public void setCheckId(String checkId) { this.checkId = checkId; }

        public LocalDate getScheduledDate() { return scheduledDate; }
        public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Default constructor for MongoDB
    public ComplianceRequirementEntity() {
    }

    // Builder pattern for entity creation
    private ComplianceRequirementEntity(Builder builder) {
        this.id = builder.id;
        this.tenantId = builder.tenantId;
        this.requirementId = builder.requirementId;
        this.requirementCode = builder.requirementCode;
        this.requirementName = builder.requirementName;
        this.category = builder.category;
        this.countryCode = builder.countryCode;
        this.description = builder.description;
        this.authority = builder.authority;
        this.type = builder.type;
        this.effectiveFrom = builder.effectiveFrom;
        this.effectiveTo = builder.effectiveTo;
        this.reviewDate = builder.reviewDate;
        this.frequency = builder.frequency;
        this.severity = builder.severity;
        this.active = builder.active;
        this.ownerDepartment = builder.ownerDepartment;
        this.ownerId = builder.ownerId;
        this.checks = builder.checks;
        this.relatedRequirements = builder.relatedRequirements;
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
    public String getRequirementId() { return requirementId; }
    public String getRequirementCode() { return requirementCode; }
    public String getRequirementName() { return requirementName; }
    public String getCategory() { return category; }
    public String getCountryCode() { return countryCode; }
    public String getDescription() { return description; }
    public String getAuthority() { return authority; }
    public String getType() { return type; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public LocalDate getReviewDate() { return reviewDate; }
    public String getFrequency() { return frequency; }
    public String getSeverity() { return severity; }
    public Boolean getActive() { return active; }
    public String getOwnerDepartment() { return ownerDepartment; }
    public String getOwnerId() { return ownerId; }
    public List<ComplianceCheckInfoData> getChecks() { return checks; }
    public List<String> getRelatedRequirements() { return relatedRequirements; }
    public List<Object> getDomainEvents() { return domainEvents; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getUpdatedBy() { return updatedBy; }

    // Setters (for MongoDB mapping)
    public void setId(String id) { this.id = id; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setRequirementId(String requirementId) { this.requirementId = requirementId; }
    public void setRequirementCode(String requirementCode) { this.requirementCode = requirementCode; }
    public void setRequirementName(String requirementName) { this.requirementName = requirementName; }
    public void setCategory(String category) { this.category = category; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setDescription(String description) { this.description = description; }
    public void setAuthority(String authority) { this.authority = authority; }
    public void setType(String type) { this.type = type; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setActive(Boolean active) { this.active = active; }
    public void setOwnerDepartment(String ownerDepartment) { this.ownerDepartment = ownerDepartment; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public void setChecks(List<ComplianceCheckInfoData> checks) { this.checks = checks; }
    public void setRelatedRequirements(List<String> relatedRequirements) { this.relatedRequirements = relatedRequirements; }
    public void setDomainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplianceRequirementEntity that = (ComplianceRequirementEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public static class Builder {
        private String id;
        private String tenantId;
        private String requirementId;
        private String requirementCode;
        private String requirementName;
        private String category;
        private String countryCode;
        private String description;
        private String authority;
        private String type;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
        private LocalDate reviewDate;
        private String frequency;
        private String severity;
        private Boolean active;
        private String ownerDepartment;
        private String ownerId;
        private List<ComplianceCheckInfoData> checks;
        private List<String> relatedRequirements;
        private List<Object> domainEvents;
        private Instant createdAt;
        private Instant updatedAt;
        private String updatedBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder requirementId(String requirementId) { this.requirementId = requirementId; return this; }
        public Builder requirementCode(String requirementCode) { this.requirementCode = requirementCode; return this; }
        public Builder requirementName(String requirementName) { this.requirementName = requirementName; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder authority(String authority) { this.authority = authority; return this; }
        public Builder type(String type) { this.type = type; return this; }
        public Builder effectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; return this; }
        public Builder effectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; return this; }
        public Builder reviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; return this; }
        public Builder frequency(String frequency) { this.frequency = frequency; return this; }
        public Builder severity(String severity) { this.severity = severity; return this; }
        public Builder active(Boolean active) { this.active = active; return this; }
        public Builder ownerDepartment(String ownerDepartment) { this.ownerDepartment = ownerDepartment; return this; }
        public Builder ownerId(String ownerId) { this.ownerId = ownerId; return this; }
        public Builder checks(List<ComplianceCheckInfoData> checks) { this.checks = checks; return this; }
        public Builder relatedRequirements(List<String> relatedRequirements) { this.relatedRequirements = relatedRequirements; return this; }
        public Builder domainEvents(List<Object> domainEvents) { this.domainEvents = domainEvents; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder updatedBy(String updatedBy) { this.updatedBy = updatedBy; return this; }

        public ComplianceRequirementEntity build() {
            return new ComplianceRequirementEntity(this);
        }
    }
}
