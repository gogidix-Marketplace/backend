package com.gogidix.shared.warehousing.batch.application.dto;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchInventory.InventoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for BatchInventory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for batch inventory")
public class BatchInventoryDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Associated lot ID")
    private String lotId;

    @Schema(description = "SKU")
    private String sku;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Bin location")
    private String binLocation;

    @Schema(description = "Quantity on hand")
    private Integer quantityOnHand;

    @Schema(description = "Quantity allocated")
    private Integer quantityAllocated;

    @Schema(description = "Quantity available")
    private Integer quantityAvailable;

    @Schema(description = "Quantity in transit")
    private Integer quantityInTransit;

    @Schema(description = "Quantity backordered")
    private Integer quantityBackordered;

    @Schema(description = "Unit cost")
    private Double unitCost;

    @Schema(description = "Total value")
    private Double totalValue;

    @Schema(description = "Last counted date")
    private LocalDateTime lastCountedDate;

    @Schema(description = "Last cycle count variance")
    private Double lastCycleCountVariance;

    @Schema(description = "Inventory status")
    private InventoryStatus status;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
