package com.gogidix.shared.warehousing.expiration.domain.entity;

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
 * Expiration Alert Entity - Multi-tenant with MongoDB
 *
 * Tracks alerts for items approaching expiration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expiration_alerts")
@CompoundIndex(def = "{'tenantId': 1, 'expiryDate': 1}", name = "idx_tenant_expiry")
@Schema(description = "Expiration alert for items approaching or past expiration")
public class ExpirationAlert {

    @Id
    @Schema(description = "Unique identifier for the alert")
    private String id;

    @Indexed
    @Schema(description = "Tenant identifier for multi-tenancy")
    private String tenantId;

    @Indexed
    @Schema(description = "Associated SKU", required = true)
    private String sku;

    @Schema(description = "Product name")
    private String productName;

    @Indexed
    @Schema(description = "Expiration date", required = true)
    private LocalDate expiryDate;

    @Indexed
    @Schema(description = "Associated batch/lot ID")
    private String batchLotId;

    @Schema(description = "Batch/lot number")
    private String batchLotNumber;

    @Schema(description = "Serialized item ID")
    private String serializedItemId;

    @Schema(description = "Serial number")
    private String serialNumber;

    @Schema(description = "Quantity of items expiring")
    private Integer quantity;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Location name")
    private String locationName;

    @Indexed
    @Schema(description = "Alert severity", required = true)
    private AlertSeverity severity;

    @Schema(description = "Days until expiration (negative if expired)")
    private Integer daysUntilExpiry;

    @Schema(description = "Alert status", required = true)
    private AlertStatus status;

    @Schema(description = "Notification sent flag")
    private Boolean notificationSent;

    @Schema(description = "Notification sent date")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Notification recipients")
    private java.util.List<String> notificationRecipients;

    @Schema(description = "Disposition action")
    private DispositionAction dispositionAction;

    @Schema(description = "Disposition date")
    private LocalDate dispositionDate;

    @Schema(description = "Disposition notes")
    private String dispositionNotes;

    @Schema(description = "Cost value of expiring items")
    private Double costValue;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the alert was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the alert was last updated")
    private LocalDateTime updatedAt;

    /**
     * Alert severity enumeration
     */
    public enum AlertSeverity {
        INFO,
        WARNING,
        CRITICAL,
        EXPIRED
    }

    /**
     * Alert status enumeration
     */
    public enum AlertStatus {
        PENDING,
        NOTIFIED,
        ACKNOWLEDGED,
        RESOLVED,
        DISMISSED
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
        QUARANTINE,
        RESELL
    }
}
