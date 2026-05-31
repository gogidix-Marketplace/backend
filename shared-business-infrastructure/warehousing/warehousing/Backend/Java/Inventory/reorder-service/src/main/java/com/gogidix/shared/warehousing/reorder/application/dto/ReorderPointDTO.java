package com.gogidix.shared.warehousing.reorder.application.dto;

import com.gogidix.shared.warehousing.reorder.domain.entity.ReorderPoint.ReorderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for ReorderPoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for reorder points")
public class ReorderPointDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Product SKU")
    private String sku;

    @Schema(description = "Product name")
    private String productName;

    @Schema(description = "Supplier ID")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Reorder level")
    private Integer reorderLevel;

    @Schema(description = "Maximum stock level")
    private Integer maxStockLevel;

    @Schema(description = "Economic order quantity")
    private Integer economicOrderQuantity;

    @Schema(description = "Safety stock level")
    private Integer safetyStockLevel;

    @Schema(description = "Lead time in days")
    private Integer leadTimeDays;

    @Schema(description = "Review period in days")
    private Integer reviewPeriodDays;

    @Schema(description = "Reorder status")
    private ReorderStatus status;

    @Schema(description = "Current stock level")
    private Integer currentStockLevel;

    @Schema(description = "Last stock check date")
    private LocalDateTime lastStockCheckDate;

    @Schema(description = "Last reorder date")
    private LocalDateTime lastReorderDate;

    @Schema(description = "Auto reorder enabled")
    private Boolean autoReorderEnabled;

    @Schema(description = "Minimum order quantity")
    private Integer minimumOrderQuantity;

    @Schema(description = "Order multiple")
    private Integer orderMultiple;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Standard cost per unit")
    private Double standardCost;

    @Schema(description = "Currency code")
    private String currencyCode;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
