package com.gogidix.shared.warehousing.reorder.domain.entity;

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
 * Reorder Point Entity - Multi-tenant with MongoDB
 *
 * Tracks reorder points for inventory items
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reorder_points")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1}", name = "idx_tenant_sku")
@Schema(description = "Reorder point configuration for inventory items")
public class ReorderPoint {

    @Id
    @Schema(description = "Unique identifier for the reorder point")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Schema(description = "Product name")
    private String productName;

    @Indexed
    @Schema(description = "Supplier ID", required = true)
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Reorder when quantity falls below this level", required = true)
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

    @Schema(description = "Reorder point status", required = true)
    private ReorderStatus status;

    @Schema(description = "Current stock level")
    private Integer currentStockLevel;

    @Schema(description = "Last stock check date")
    private LocalDateTime lastStockCheckDate;

    @Schema(description = "Last reorder date")
    private LocalDateTime lastReorderDate;

    @Schema(description = "Automatic reorder enabled")
    private Boolean autoReorderEnabled;

    @Schema(description = "Minimum order quantity")
    private Integer minimumOrderQuantity;

    @Schema(description = "Order multiple (must order in multiples of this)")
    private Integer orderMultiple;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Standard cost per unit")
    private Double standardCost;

    @Schema(description = "Currency code")
    private String currencyCode;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Reorder status enumeration
     */
    public enum ReorderStatus {
        ACTIVE,
        BELOW_REORDER_POINT,
        REORDER_PENDING,
        REORDERED,
        SUSPENDED,
        INACTIVE
    }
}
