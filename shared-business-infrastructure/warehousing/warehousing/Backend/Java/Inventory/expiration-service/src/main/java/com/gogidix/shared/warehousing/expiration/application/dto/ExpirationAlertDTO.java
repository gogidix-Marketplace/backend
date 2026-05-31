package com.gogidix.shared.warehousing.expiration.application.dto;

import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert.AlertSeverity;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert.AlertStatus;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert.DispositionAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for ExpirationAlert
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for expiration alerts")
public class ExpirationAlertDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "SKU")
    private String sku;

    @Schema(description = "Product name")
    private String productName;

    @Schema(description = "Expiration date")
    private LocalDate expiryDate;

    @Schema(description = "Batch/lot ID")
    private String batchLotId;

    @Schema(description = "Batch/lot number")
    private String batchLotNumber;

    @Schema(description = "Serialized item ID")
    private String serializedItemId;

    @Schema(description = "Serial number")
    private String serialNumber;

    @Schema(description = "Quantity")
    private Integer quantity;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Location name")
    private String locationName;

    @Schema(description = "Alert severity")
    private AlertSeverity severity;

    @Schema(description = "Days until expiration")
    private Integer daysUntilExpiry;

    @Schema(description = "Alert status")
    private AlertStatus status;

    @Schema(description = "Notification sent")
    private Boolean notificationSent;

    @Schema(description = "Notification sent at")
    private LocalDateTime notificationSentAt;

    @Schema(description = "Notification recipients")
    private List<String> notificationRecipients;

    @Schema(description = "Disposition action")
    private DispositionAction dispositionAction;

    @Schema(description = "Disposition date")
    private LocalDate dispositionDate;

    @Schema(description = "Disposition notes")
    private String dispositionNotes;

    @Schema(description = "Cost value")
    private Double costValue;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
