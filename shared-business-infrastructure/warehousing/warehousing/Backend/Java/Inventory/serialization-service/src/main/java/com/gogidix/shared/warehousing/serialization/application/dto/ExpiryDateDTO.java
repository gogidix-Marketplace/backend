package com.gogidix.shared.warehousing.serialization.application.dto;

import com.gogidix.shared.warehousing.serialization.domain.entity.ExpiryDate.AlertStatus;
import com.gogidix.shared.warehousing.serialization.domain.entity.ExpiryDate.DispositionAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for ExpiryDate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for expiry dates")
public class ExpiryDateDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "SKU")
    private String sku;

    @Schema(description = "Expiry date")
    private LocalDate expiryDate;

    @Schema(description = "Batch ID")
    private String batchId;

    @Schema(description = "Serialized item ID")
    private String serializedItemId;

    @Schema(description = "Serial number")
    private String serialNumber;

    @Schema(description = "Quantity")
    private Integer quantity;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Alert status")
    private AlertStatus alertStatus;

    @Schema(description = "Days until expiry")
    private Integer daysUntilExpiry;

    @Schema(description = "Notification sent")
    private Boolean notificationSent;

    @Schema(description = "Notification sent at")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Disposition action")
    private DispositionAction dispositionAction;

    @Schema(description = "Disposition date")
    private LocalDate dispositionDate;

    @Schema(description = "Disposition notes")
    private String dispositionNotes;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
