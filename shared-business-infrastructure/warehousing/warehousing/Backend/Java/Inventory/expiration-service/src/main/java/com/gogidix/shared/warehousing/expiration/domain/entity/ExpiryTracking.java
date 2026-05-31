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
 * Expiry Tracking Entity - Multi-tenant with MongoDB
 *
 * Tracks items with expiration dates for monitoring
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "expiry_tracking")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1, 'expiryDate': 1}", name = "idx_tenant_sku_expiry")
@Schema(description = "Expiry tracking record for monitoring items with expiration dates")
public class ExpiryTracking {

    @Id
    @Schema(description = "Unique identifier for the tracking record")
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
    @Schema(description = "Batch/lot ID")
    private String batchLotId;

    @Schema(description = "Batch/lot number")
    private String batchLotNumber;

    @Schema(description = "Serialized item ID")
    private String serializedItemId;

    @Schema(description = "Serial number")
    private String serialNumber;

    @Schema(description = "Quantity being tracked")
    private Integer quantity;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Current tracking status", required = true)
    private TrackingStatus status;

    @Schema(description = "Days until expiration")
    private Integer daysUntilExpiry;

    @Schema(description = "Last check date")
    private LocalDateTime lastCheckDate;

    @Schema(description = "Next check date")
    private LocalDateTime nextCheckDate;

    @Schema(description = "Alert generated")
    private Boolean alertGenerated;

    @Schema(description = "Alert ID")
    private String alertId;

    @Schema(description = "Monitoring active")
    private Boolean monitoringActive;

    @Schema(description = "Check frequency in days")
    private Integer checkFrequencyDays;

    @Schema(description = "Additional attributes")
    private java.util.Map<String, Object> attributes;

    @CreatedDate
    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    /**
     * Tracking status enumeration
     */
    public enum TrackingStatus {
        ACTIVE,
        EXPIRED,
        DISPOSED,
        SOLD,
        RETURNED,
        QUARANTINED
    }
}
