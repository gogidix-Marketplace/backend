package com.gogidix.shared.warehousing.serialization.domain.entity;

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
 * Batch Entity - Multi-tenant with MongoDB
 *
 * Tracks batch/lot information for grouped items
 * Supports perishable goods tracking and lot control
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "batches")
@CompoundIndex(def = "{'tenantId': 1, 'batchNumber': 1}", name = "idx_tenant_batch")
@Schema(description = "Batch representing a group of items produced/received together")
public class Batch {

    @Id
    @Schema(description = "Unique identifier for the batch")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Unique batch/lot number", required = true)
    private String batchNumber;

    @Indexed
    @Schema(description = "SKU of products in this batch", required = true)
    private String sku;

    @Schema(description = "Batch description")
    private String description;

    @Schema(description = "Manufacturing date")
    private LocalDate manufacturingDate;

    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Indexed
    @Schema(description = "Supplier/vendor identifier")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Country of origin")
    private String countryOfOrigin;

    @Schema(description = "Total quantity in batch")
    private Integer totalQuantity;

    @Schema(description = "Current available quantity")
    private Integer availableQuantity;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Batch status", required = true)
    private BatchStatus status;

    @Indexed
    @Schema(description = "Location identifier")
    private String locationId;

    @Schema(description = "Quality control status")
    private QCStatus qcStatus;

    @Schema(description = "Quality control notes")
    private String qcNotes;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Batch status enumeration
     */
    public enum BatchStatus {
        ACTIVE,
        FULLY_ALLOCATED,
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
