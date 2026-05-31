package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.event.ComplianceRequirementCreatedEvent;
import com.gogidix.hr.globalcompliance.domain.model.enums.*;
import com.gogidix.hr.globalcompliance.shared.base.BaseEntity;
import com.gogidix.hr.globalcompliance.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Compliance Requirement Domain Entity
 * Multi-tenant compliance requirement management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "compliance_requirements")
public class ComplianceRequirement extends BaseEntity {

    private String requirementId;

    private String tenantId;

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

    private List<ComplianceCheckInfo> checks;

    private List<String> relatedRequirements;

    @Builder.Default
    private List<ComplianceRequirementCreatedEvent> domainEvents = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComplianceCheckInfo {
        private String checkId;
        private LocalDate scheduledDate;
        private String status;
    }

    /**
     * Creates a new compliance requirement
     */
    public static ComplianceRequirement create(String tenantId, String requirementCode,
                                                String requirementName, String category,
                                                String countryCode, String description,
                                                String authority, String type,
                                                LocalDate effectiveFrom, String frequency,
                                                String severity, String ownerDepartment,
                                                String ownerId, String createdBy) {
        ComplianceRequirement requirement = new ComplianceRequirement();
        requirement.setTenantId(tenantId);
        requirement.setRequirementCode(requirementCode);
        requirement.setRequirementName(requirementName);
        requirement.setCategory(category);
        requirement.setCountryCode(countryCode);
        requirement.setDescription(description);
        requirement.setAuthority(authority);
        requirement.setType(type);
        requirement.setEffectiveFrom(effectiveFrom);
        requirement.setFrequency(frequency);
        requirement.setSeverity(severity);
        requirement.setActive(true);
        requirement.setOwnerDepartment(ownerDepartment);
        requirement.setOwnerId(ownerId);
        requirement.setChecks(new ArrayList<>());
        requirement.setRelatedRequirements(new ArrayList<>());

        requirement.generateRequirementId();
        requirement.calculateReviewDate();

        requirement.addDomainEvent(ComplianceRequirementCreatedEvent.builder()
                .requirementId(requirement.getRequirementId())
                .tenantId(tenantId)
                .requirementCode(requirementCode)
                .requirementName(requirementName)
                .category(category)
                .countryCode(countryCode)
                .type(type)
                .timestamp(java.time.Instant.now())
                .eventType("REQUIREMENT_CREATED")
                .triggeredBy(createdBy)
                .build());

        return requirement;
    }

    /**
     * Updates the requirement details
     */
    public void updateDetails(String requirementName, String description,
                              String authority, String ownerDepartment,
                              String ownerId, LocalDate effectiveTo) {
        if (this.active && Boolean.FALSE.equals(this.active)) {
            throw new IllegalStateException("Cannot update inactive requirement");
        }

        if (requirementName != null && !requirementName.isBlank()) {
            this.requirementName = requirementName;
        }
        if (description != null) {
            this.description = description;
        }
        if (authority != null && !authority.isBlank()) {
            this.authority = authority;
        }
        if (ownerDepartment != null) {
            this.ownerDepartment = ownerDepartment;
        }
        if (ownerId != null) {
            this.ownerId = ownerId;
        }
        if (effectiveTo != null) {
            this.effectiveTo = effectiveTo;
        }
    }

    /**
     * Activates the requirement
     */
    public void activate() {
        if (this.effectiveTo != null && LocalDate.now().isAfter(this.effectiveTo)) {
            throw new ValidationException("effectiveTo", "Cannot activate expired requirement");
        }
        this.active = true;
    }

    /**
     * Deactivates the requirement
     */
    public void deactivate(String reason) {
        this.active = false;
        this.description = this.description + " [DEACTIVATED: " + reason + "]";
    }

    /**
     * Schedules a compliance check
     */
    public void scheduleCheck(String checkId, LocalDate scheduledDate) {
        if (!this.active) {
            throw new IllegalStateException("Cannot schedule check for inactive requirement");
        }

        ComplianceCheckInfo checkInfo = ComplianceCheckInfo.builder()
                .checkId(checkId)
                .scheduledDate(scheduledDate)
                .status(CheckStatus.PENDING.name())
                .build();

        if (this.checks == null) {
            this.checks = new ArrayList<>();
        }
        this.checks.add(checkInfo);
    }

    /**
     * Updates check status
     */
    public void updateCheckStatus(String checkId, String status) {
        if (this.checks != null) {
            this.checks.stream()
                    .filter(check -> check.getCheckId().equals(checkId))
                    .findFirst()
                    .ifPresent(check -> check.setStatus(status));
        }
    }

    /**
     * Adds a related requirement
     */
    public void addRelatedRequirement(String requirementId) {
        if (this.relatedRequirements == null) {
            this.relatedRequirements = new ArrayList<>();
        }
        if (!this.relatedRequirements.contains(requirementId)) {
            this.relatedRequirements.add(requirementId);
        }
    }

    /**
     * Removes a related requirement
     */
    public void removeRelatedRequirement(String requirementId) {
        if (this.relatedRequirements != null) {
            this.relatedRequirements.remove(requirementId);
        }
    }

    /**
     * Updates review date based on frequency
     */
    public void updateReviewDate() {
        calculateReviewDate();
    }

    /**
     * Checks if the requirement is due for review
     */
    public boolean isDueForReview() {
        return this.reviewDate != null && LocalDate.now().isAfter(this.reviewDate.minusDays(30));
    }

    /**
     * Checks if the requirement is currently active
     */
    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        boolean effectiveStarted = this.effectiveFrom == null || !now.isBefore(this.effectiveFrom);
        boolean notExpired = this.effectiveTo == null || !now.isAfter(this.effectiveTo);
        return Boolean.TRUE.equals(this.active) && effectiveStarted && notExpired;
    }

    /**
     * Checks if the requirement is mandatory
     */
    public boolean isMandatory() {
        return ComplianceType.MANDATORY.name().equals(this.type) ||
               ComplianceType.REGULATORY.name().equals(this.type);
    }

    /**
     * Gets the priority based on severity
     */
    public int getPriority() {
        return switch (this.severity) {
            case "CRITICAL" -> 1;
            case "HIGH" -> 2;
            case "MEDIUM" -> 3;
            case "LOW" -> 4;
            default -> 5;
        };
    }

    /**
     * Generates unique requirement ID
     */
    private void generateRequirementId() {
        this.requirementId = "REQ-" + this.countryCode + "-" +
                             java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Calculates review date based on frequency
     */
    private void calculateReviewDate() {
        if (this.frequency == null) {
            this.reviewDate = this.effectiveFrom != null ?
                    this.effectiveFrom.plusYears(1) : LocalDate.now().plusYears(1);
            return;
        }

        LocalDate baseDate = this.effectiveFrom != null ? this.effectiveFrom : LocalDate.now();
        this.reviewDate = switch (this.frequency) {
            case "DAILY" -> baseDate.plusDays(1);
            case "WEEKLY" -> baseDate.plusWeeks(1);
            case "MONTHLY" -> baseDate.plusMonths(1);
            case "QUARTERLY" -> baseDate.plusMonths(3);
            case "ANNUALLY" -> baseDate.plusYears(1);
            default -> baseDate.plusYears(1);
        };
    }

    /**
     * Gets days until review date
     */
    public long getDaysUntilReview() {
        return this.reviewDate != null ?
                Period.between(LocalDate.now(), this.reviewDate).getDays() : 0;
    }

    public void addDomainEvent(ComplianceRequirementCreatedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
