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
 * Serialized Item Entity - Multi-tenant with MongoDB
 *
 * Tracks individual serialized items with unique identifiers
 * Supports serialization tracking for high-value or regulated items
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "serialized_items")
@CompoundIndex(def = "{'tenantId': 1, 'serialNumber': 1}", name = "idx_tenant_serial")
@Schema(description = "Serialized item representing an individually tracked inventory unit")
public class SerializedItem {

    @Id
    @Schema(description = "Unique identifier for the serialized item")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Unique serial number for the item", required = true)
    private String serialNumber;

    @Indexed
    @Schema(description = "SKU of the product", required = true)
    private String sku;

    @Schema(description = "Current status of the serialized item", required = true)
    private SerializedItemStatus status;

    @Indexed
    @Schema(description = "Batch identifier if part of a batch")
    private String batchId;

    @Schema(description = "Expiry date of the item")
    private LocalDate expiryDate;

    @Indexed
    @Schema(description = "Current location identifier")
    private String locationId;

    @Schema(description = "Warehouse/bin location")
    private String binLocation;

    @Schema(description = "Cost value of the item")
    private Double costValue;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    @Schema(description = "Date when the status was last changed")
    private LocalDateTime statusChangedAt;

    @Schema(description = "User who last changed the status")
    private String statusChangedBy;

    @Schema(description = "Notes about the item")
    private String notes;

    /**
     * Status enumeration for serialized items
     */
    public enum SerializedItemStatus {
        AVAILABLE,
        RESERVED,
        PICKED,
        SHIPPED,
        DELIVERED,
        RETURNED,
        DAMAGED,
        EXPIRED,
        DISPOSED,
        IN_TRANSIT,
        RECEIVED
    }
}
