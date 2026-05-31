package com.gogidix.shared.warehousing.cyclecounting.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Count Discrepancy Entity
 *
 * Records discrepancies found during cycle counting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "count_discrepancies")
@CompoundIndex(def = "{'tenantId': 1, 'cycleCountId': 1, 'resolved': 1}", name = "idx_discrepancy_tenant_resolved")
public class CountDiscrepancy {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String cycleCountId;

    private String countSessionId;

    @Indexed
    private String sku;

    private String itemName;

    private String location;

    @Indexed
    private DiscrepancyType discrepancyType;

    private Integer systemQuantity;

    private Integer countedQuantity;

    private Integer variance;

    private BigDecimal unitValue;

    private BigDecimal totalValue;

    private DiscrepancyStatus status;

    private Boolean resolved;

    private String resolvedBy;

    private LocalDateTime resolvedAt;

    private String resolutionNotes;

    private AdjustmentAction adjustmentAction;

    private String referenceType;

    private String referenceId;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum DiscrepancyType {
        SHORTAGE,
        OVERAGE,
        DAMAGED,
        EXPIRED,
        WRONG_LOCATION,
        QUALITY_ISSUE,
        MISMATCH
    }

    public enum DiscrepancyStatus {
        PENDING_REVIEW,
        UNDER_INVESTIGATION,
        APPROVED,
        REJECTED,
        ADJUSTED,
        CLOSED
    }

    public enum AdjustmentAction {
        INVENTORY_ADJUSTMENT,
        RETURN_TO_VENDOR,
        WRITE_OFF,
        HOLD,
        NO_ACTION,
        TRANSFER
    }

    /**
     * Calculate variance
     */
    public void calculateVariance() {
        if (systemQuantity != null && countedQuantity != null) {
            this.variance = countedQuantity - systemQuantity;
        }
    }

    /**
     * Calculate total value
     */
    public void calculateTotalValue() {
        if (unitValue != null && variance != null) {
            this.totalValue = unitValue.multiply(BigDecimal.valueOf(Math.abs(variance)));
        }
    }

    /**
     * Resolve discrepancy
     */
    public void resolve(String resolvedBy, AdjustmentAction action, String notes) {
        this.resolved = true;
        this.resolvedBy = resolvedBy;
        this.resolvedAt = LocalDateTime.now();
        this.adjustmentAction = action;
        this.resolutionNotes = notes;
        this.status = DiscrepancyStatus.ADJUSTED;
    }
}
