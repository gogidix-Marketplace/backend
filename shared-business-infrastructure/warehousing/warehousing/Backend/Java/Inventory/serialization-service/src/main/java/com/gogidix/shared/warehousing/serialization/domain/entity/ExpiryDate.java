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
 * Expiry Date Entity - Multi-tenant with MongoDB
 *
 * Tracks expiry dates for serialized items and batches
 * Supports expiration monitoring and alerting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expiry_dates")
@CompoundIndex(def = "{'tenantId': 1, 'expiryDate': 1}", name = "idx_tenant_expiry")
@Schema(description = "Expiry date information for tracking perishable items")
public class ExpiryDate {

    @Id
    @Schema(description = "Unique identifier for the expiry record")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Associated SKU", required = true)
    private String sku;

    @Indexed
    @Schema(description = "Expiry date", required = true)
    private LocalDate expiryDate;

    @Indexed
    @Schema(description = "Associated batch ID")
    private String batchId;

    @Schema(description = "Associated serialized item ID")
    private String serializedItemId;

    @Schema(description = "Serial number (if tracking single item)")
    private String serialNumber;

    @Schema(description = "Quantity of items with this expiry")
    private Integer quantity;

    @Schema(description = "Location identifier")
    private String locationId;

    @Schema(description = "Alert status", required = true)
    private AlertStatus alertStatus;

    @Schema(description = "Days until expiry")
    private Integer daysUntilExpiry;

    @Schema(description = "Notification sent flag")
    private Boolean notificationSent;

    @Schema(description = "Notification sent date")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Disposition action")
    private DispositionAction dispositionAction;

    @Schema(description = "Disposition date")
    private LocalDate dispositionDate;

    @Schema(description = "Disposition notes")
    private String dispositionNotes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Alert status enumeration
     */
    public enum AlertStatus {
        OK,
        WARNING,
        CRITICAL,
        EXPIRED,
        DISPOSED
    }

    /**
     * Disposition action enumeration
     */
    public enum DispositionAction {
        NONE,
        RETURN_TO_SUPPLIER,
        DONATE,
        DISPOSE,
        MARK_DOWN,
        QUARANTINE
    }
}
