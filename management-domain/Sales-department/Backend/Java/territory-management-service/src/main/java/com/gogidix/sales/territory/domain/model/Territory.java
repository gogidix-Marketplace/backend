package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.event.TerritoryCreatedEvent;
import com.gogidix.sales.territory.domain.valueobject.GeographicBoundary;
import com.gogidix.sales.territory.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Territory Domain Entity
 * Represents a sales territory with geographic and product-based boundaries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "territories")
public class Territory extends BaseEntity {

    private String territoryId;
    private String tenantId;
    private String name;
    private String code;
    private String description;
    private TerritoryType type;
    private TerritoryStatus status;

    // Geographic boundaries
    private GeographicBoundary geographicBoundary;

    // Product-based territories
    private List<String> productCategories;
    private List<String> productIds;

    // Customer segment boundaries
    private List<String> customerSegments;
    private List<String> customerTierIds;

    // Hierarchy
    private String parentTerritoryId;
    private List<String> childTerritoryIds;

    // Metadata
    private String regionId;
    private String managerId;
    private Integer priority;

    // Performance tracking
    private TerritoryPerformance performance;

    // Realignment tracking
    private Boolean pendingRealignment;
    private Instant realignmentRequestedAt;
    private String realignmentRequestedBy;

    @Builder.Default
    private List<TerritoryCreatedEvent> domainEvents = new ArrayList<>();

    public enum TerritoryType {
        GEOGRAPHIC,
        PRODUCT_BASED,
        CUSTOMER_SEGMENT,
        HYBRID
    }

    public enum TerritoryStatus {
        ACTIVE,
        INACTIVE,
        PENDING_REALIGNMENT,
        ARCHIVED
    }

    /**
     * Creates a new territory
     */
    public static Territory create(String tenantId, String name, String code,
                                   TerritoryType type, String createdBy) {
        Territory territory = Territory.builder()
                .tenantId(tenantId)
                .name(name)
                .code(code)
                .type(type)
                .status(TerritoryStatus.ACTIVE)
                .productCategories(new ArrayList<>())
                .productIds(new ArrayList<>())
                .customerSegments(new ArrayList<>())
                .customerTierIds(new ArrayList<>())
                .childTerritoryIds(new ArrayList<>())
                .pendingRealignment(false)
                .performance(TerritoryPerformance.builder().build())
                .build();

        territory.addDomainEvent(TerritoryCreatedEvent.builder()
                .territoryId(territory.getTerritoryId())
                .tenantId(tenantId)
                .name(name)
                .code(code)
                .type(type.name())
                .eventType("TERRITORY_CREATED")
                .timestamp(Instant.now())
                .build());

        return territory;
    }

    /**
     * Activates the territory
     */
    public void activate() {
        if (this.status == TerritoryStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot activate an archived territory");
        }
        this.status = TerritoryStatus.ACTIVE;
    }

    /**
     * Deactivates the territory
     */
    public void deactivate() {
        this.status = TerritoryStatus.INACTIVE;
    }

    /**
     * Archives the territory
     */
    public void archive() {
        this.status = TerritoryStatus.ARCHIVED;
    }

    /**
     * Requests realignment
     */
    public void requestRealignment(String requestedBy) {
        this.pendingRealignment = true;
        this.realignmentRequestedAt = Instant.now();
        this.realignmentRequestedBy = requestedBy;
        this.status = TerritoryStatus.PENDING_REALIGNMENT;
    }

    /**
     * Completes realignment
     */
    public void completeRealignment() {
        this.pendingRealignment = false;
        this.realignmentRequestedAt = null;
        this.status = TerritoryStatus.ACTIVE;
    }

    /**
     * Sets geographic boundary
     */
    public void setGeographicBoundaryData(GeographicBoundary boundary) {
        this.geographicBoundary = boundary;
        if (this.type == null) {
            this.type = TerritoryType.GEOGRAPHIC;
        }
    }

    /**
     * Adds product category
     */
    public void addProductCategory(String category) {
        if (this.productCategories == null) {
            this.productCategories = new ArrayList<>();
        }
        if (!this.productCategories.contains(category)) {
            this.productCategories.add(category);
        }
        if (this.type == null) {
            this.type = TerritoryType.PRODUCT_BASED;
        }
    }

    /**
     * Removes product category
     */
    public void removeProductCategory(String category) {
        if (this.productCategories != null) {
            this.productCategories.remove(category);
        }
    }

    /**
     * Adds customer segment
     */
    public void addCustomerSegment(String segment) {
        if (this.customerSegments == null) {
            this.customerSegments = new ArrayList<>();
        }
        if (!this.customerSegments.contains(segment)) {
            this.customerSegments.add(segment);
        }
    }

    /**
     * Adds child territory
     */
    public void addChildTerritory(String childTerritoryId) {
        if (this.childTerritoryIds == null) {
            this.childTerritoryIds = new ArrayList<>();
        }
        if (!this.childTerritoryIds.contains(childTerritoryId)) {
            this.childTerritoryIds.add(childTerritoryId);
        }
    }

    /**
     * Removes child territory
     */
    public void removeChildTerritory(String childTerritoryId) {
        if (this.childTerritoryIds != null) {
            this.childTerritoryIds.remove(childTerritoryId);
        }
    }

    /**
     * Updates performance data
     */
    public void updatePerformance(BigDecimal currentSales, BigDecimal quota,
                                   Integer accountsCount, Integer dealsCount) {
        if (this.performance == null) {
            this.performance = TerritoryPerformance.builder().build();
        }

        this.performance.currentSales = currentSales;
        this.performance.quota = quota;
        this.performance.accountsCount = accountsCount;
        this.performance.dealsCount = dealsCount;
        this.performance.lastUpdated = Instant.now();

        if (quota != null && quota.compareTo(BigDecimal.ZERO) > 0) {
            this.performance.quotaAttainment = currentSales.divide(quota, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Checks if territory overlaps with another territory
     */
    public boolean overlapsWith(Territory other) {
        if (this.type != TerritoryType.GEOGRAPHIC || other.getType() != TerritoryType.GEOGRAPHIC) {
            return false;
        }

        if (this.geographicBoundary == null || other.getGeographicBoundary() == null) {
            return false;
        }

        return this.geographicBoundary.overlapsWith(other.getGeographicBoundary());
    }

    /**
     * Checks if territory is active
     */
    public boolean isActive() {
        return this.status == TerritoryStatus.ACTIVE;
    }

    public void addDomainEvent(TerritoryCreatedEvent event) {
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

    /**
     * Territory Performance nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TerritoryPerformance {
        private BigDecimal currentSales;
        private BigDecimal quota;
        private BigDecimal quotaAttainment;
        private Integer accountsCount;
        private Integer dealsCount;
        private Integer activeLeads;
        private BigDecimal winRate;
        private Instant lastUpdated;
        private LocalDate periodStart;
        private LocalDate periodEnd;
    }
}
