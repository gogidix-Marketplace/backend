package com.gogidix.shared.warehousing.batch.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Batch Inventory Entity - Multi-tenant with MongoDB
 *
 * Tracks inventory at batch/lot level
 * Links batch lots with inventory quantities
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "batch_inventory")
@CompoundIndex(def = "{'tenantId': 1, 'lotId': 1, 'sku': 1}", name = "idx_tenant_lot_sku")
@Schema(description = "Batch inventory tracking quantity per SKU per lot")
public class BatchInventory {

    @Id
    @Schema(description = "Unique identifier for the batch inventory record")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Associated batch lot ID", required = true)
    private String lotId;

    @Indexed
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Indexed
    @Schema(description = "Location ID", required = true)
    private String locationId;

    @Schema(description = "Bin location within warehouse")
    private String binLocation;

    @Schema(description = "Quantity on hand")
    private Integer quantityOnHand;

    @Schema(description = "Quantity allocated to orders")
    private Integer quantityAllocated;

    @Schema(description = "Quantity available for allocation")
    private Integer quantityAvailable;

    @Schema(description = "Quantity in transit")
    private Integer quantityInTransit;

    @Schema(description = "Quantity on backorder")
    private Integer quantityBackordered;

    @Schema(description = "Unit cost")
    private Double unitCost;

    @Schema(description = "Total value of inventory")
    private Double totalValue;

    @Schema(description = "Last counted date")
    private LocalDateTime lastCountedDate;

    @Schema(description = "Last cycle count variance")
    private Double lastCycleCountVariance;

    @Schema(description = "Inventory status", required = true)
    private InventoryStatus status;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Inventory status enumeration
     */
    public enum InventoryStatus {
        AVAILABLE,
        ALLOCATED,
        BACKORDERED,
        IN_TRANSIT,
        DAMAGED,
        EXPIRED,
        COUNT_REQUIRED,
        ADJUSTMENT_PENDING
    }

    /**
     * Calculate available quantity
     */
    public void calculateAvailableQuantity() {
        int available = quantityOnHand - quantityAllocated;
        this.quantityAvailable = Math.max(0, available);
    }

    /**
     * Calculate total value
     */
    public void calculateTotalValue() {
        if (unitCost != null && quantityOnHand != null) {
            this.totalValue = unitCost * quantityOnHand;
        }
    }
}
