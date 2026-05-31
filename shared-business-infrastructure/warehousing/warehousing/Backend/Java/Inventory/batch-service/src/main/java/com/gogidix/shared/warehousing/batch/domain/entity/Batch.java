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
import java.util.List;

/**
 * Batch Entity - Multi-tenant with MongoDB
 *
 * Main batch entity for grouping batch lots together
 * Supports batch-level operations and reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "batches")
@CompoundIndex(def = "{'tenantId': 1, 'batchNumber': 1}", name = "idx_tenant_batch")
@Schema(description = "Batch representing a group of related lots")
public class Batch {

    @Id
    @Schema(description = "Unique identifier for the batch")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Unique batch number", required = true)
    private String batchNumber;

    @Schema(description = "Batch name/description")
    private String batchName;

    @Schema(description = "Product SKU associated with batch")
    private String sku;

    @Schema(description = "List of lot IDs in this batch")
    private List<String> lotIds;

    @Schema(description = "Total quantity across all lots")
    private Integer totalQuantity;

    @Schema(description = "Available quantity across all lots")
    private Integer availableQuantity;

    @Schema(description = "Earliest expiration date in batch")
    private LocalDate earliestExpirationDate;

    @Schema(description = "Latest expiration date in batch")
    private LocalDate latestExpirationDate;

    @Schema(description = "Primary supplier ID")
    private String supplierId;

    @Schema(description = "Primary supplier name")
    private String supplierName;

    @Schema(description = "Receiving date")
    private LocalDate receivingDate;

    @Schema(description = "Batch status", required = true)
    private BatchStatus status;

    @Indexed
    @Schema(description = "Default warehouse location ID")
    private String locationId;

    @Schema(description = "Batch type")
    private BatchType batchType;

    @Schema(description = "Priority level (1-10, 10 being highest)")
    private Integer priorityLevel;

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
        PARTIALLY_ALLOCATED,
        EXPIRED,
        QUARANTINED,
        DISPOSED,
        RECEIVED,
        CLOSED
    }

    /**
     * Batch type enumeration
     */
    public enum BatchType {
        PRODUCTION,
        PURCHASE,
        TRANSFER,
        RETURN,
        ADJUSTMENT
    }
}
