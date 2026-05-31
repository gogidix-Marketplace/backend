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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Batch Lot Entity - Multi-tenant with MongoDB
 *
 * Tracks batch/lot information for perishable goods
 * Supports FEFO (First Expired First Out) inventory management
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "batch_lots")
@CompoundIndex(def = "{'tenantId': 1, 'lotNumber': 1}", name = "idx_tenant_lot")
@Schema(description = "Batch lot representing a production or receipt lot")
public class BatchLot {

    @Id
    @Schema(description = "Unique identifier for the batch lot")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Unique lot number", required = true)
    private String lotNumber;

    @Indexed
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Schema(description = "Lot description")
    private String description;

    @Schema(description = "Production date")
    private LocalDate productionDate;

    @Indexed
    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Indexed
    @Schema(description = "Supplier/vendor identifier")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Country of origin")
    private String countryOfOrigin;

    @Schema(description = "Manufacturing batch number")
    private String manufacturingBatchNumber;

    @Schema(description = "Total quantity in lot")
    private Integer totalQuantity;

    @Schema(description = "Available quantity")
    private Integer availableQuantity;

    @Schema(description = "Reserved quantity")
    private Integer reservedQuantity;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Cost per unit")
    private Double costPerUnit;

    @Schema(description = "Lot status", required = true)
    private LotStatus status;

    @Indexed
    @Schema(description = "Warehouse location ID")
    private String locationId;

    @Schema(description = "Quality control status")
    private QCStatus qcStatus;

    @Schema(description = "Quality control notes")
    private String qcNotes;

    @Schema(description = "Storage requirements (e.g., temperature, humidity)")
    private String storageRequirements;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Lot status enumeration
     */
    public enum LotStatus {
        ACTIVE,
        FULLY_ALLOCATED,
        PARTIALLY_ALLOCATED,
        EXPIRED,
        QUARANTINED,
        DISPOSED,
        RECEIVED
    }

    /**
     * Quality control status enumeration
     */
    public enum QCStatus {
        PENDING,
        PASSED,
        FAILED,
        CONDITIONAL
    }
}
