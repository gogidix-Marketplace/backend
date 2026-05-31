package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.event.TerritoryAssignedEvent;
import com.gogidix.sales.territory.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Territory Assignment Domain Entity
 * Represents the assignment of a sales representative to a territory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "territory-assignments")
public class TerritoryAssignment extends BaseEntity {

    private String assignmentId;
    private String tenantId;
    private String territoryId;
    private String salesRepresentativeId;
    private String salesRepresentativeName;
    private AssignmentStatus status;
    private AssignmentType type;

    // Assignment period
    private Instant assignedAt;
    private String assignedBy;
    private LocalDate effectiveDate;
    private LocalDate endDate;

    // Assignment details
    private Boolean primaryAssignment;
    private Integer priority;
    private String notes;

    // Performance tracking
    private AssignmentPerformance performance;

    @Builder.Default
    private java.util.List<TerritoryAssignedEvent> domainEvents = new java.util.ArrayList<>();

    public enum AssignmentStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        REVOKED,
        EXPIRED
    }

    public enum AssignmentType {
        FULL_TIME,
        PART_TIME,
        SHARED,
        TEMPORARY,
        COVER
    }

    /**
     * Creates a new territory assignment
     */
    public static TerritoryAssignment create(String tenantId, String territoryId,
                                              String salesRepresentativeId,
                                              String salesRepresentativeName,
                                              String assignedBy) {
        TerritoryAssignment assignment = TerritoryAssignment.builder()
                .tenantId(tenantId)
                .territoryId(territoryId)
                .salesRepresentativeId(salesRepresentativeId)
                .salesRepresentativeName(salesRepresentativeName)
                .status(AssignmentStatus.ACTIVE)
                .type(AssignmentType.FULL_TIME)
                .assignedAt(Instant.now())
                .assignedBy(assignedBy)
                .effectiveDate(LocalDate.now())
                .primaryAssignment(false)
                .priority(0)
                .performance(AssignmentPerformance.builder().build())
                .build();

        assignment.addDomainEvent(TerritoryAssignedEvent.builder()
                .assignmentId(assignment.getAssignmentId())
                .tenantId(tenantId)
                .territoryId(territoryId)
                .salesRepresentativeId(salesRepresentativeId)
                .eventType("TERRITORY_ASSIGNED")
                .timestamp(Instant.now())
                .build());

        return assignment;
    }

    /**
     * Activates the assignment
     */
    public void activate(String activatedBy) {
        this.status = AssignmentStatus.ACTIVE;
        this.assignedBy = activatedBy;
        this.assignedAt = Instant.now();
    }

    /**
     * Deactivates the assignment
     */
    public void deactivate() {
        this.status = AssignmentStatus.INACTIVE;
    }

    /**
     * Revokes the assignment
     */
    public void revoke() {
        this.status = AssignmentStatus.REVOKED;
    }

    /**
     * Marks assignment as expired
     */
    public void markExpired() {
        this.status = AssignmentStatus.EXPIRED;
    }

    /**
     * Checks if assignment is currently active
     */
    public boolean isCurrentlyActive() {
        if (this.status != AssignmentStatus.ACTIVE) {
            return false;
        }
        LocalDate now = LocalDate.now();
        if (this.effectiveDate != null && now.isBefore(this.effectiveDate)) {
            return false;
        }
        if (this.endDate != null && now.isAfter(this.endDate)) {
            return false;
        }
        return true;
    }

    /**
     * Sets as primary assignment
     */
    public void setAsPrimary() {
        this.primaryAssignment = true;
        this.priority = 1;
    }

    /**
     * Updates performance data
     */
    public void updatePerformance(java.math.BigDecimal salesGenerated,
                                   Integer accountsManaged,
                                   Integer dealsClosed) {
        if (this.performance == null) {
            this.performance = AssignmentPerformance.builder().build();
        }

        this.performance.salesGenerated = salesGenerated;
        this.performance.accountsManaged = accountsManaged;
        this.performance.dealsClosed = dealsClosed;
        this.performance.lastUpdated = Instant.now();
    }

    public void addDomainEvent(TerritoryAssignedEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new java.util.ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Assignment Performance nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssignmentPerformance {
        private java.math.BigDecimal salesGenerated;
        private Integer accountsManaged;
        private Integer dealsClosed;
        private java.math.BigDecimal quotaAttainment;
        private Instant lastUpdated;
    }
}
