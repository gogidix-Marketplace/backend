package com.gogidix.shared.warehousing.serialization.application.dto;

import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem.SerializedItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for SerializedItem
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for serialized items")
public class SerializedItemDTO {

    @Schema(description = "Unique identifier for the serialized item")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Unique serial number")
    private String serialNumber;

    @Schema(description = "SKU of the product")
    private String sku;

    @Schema(description = "Current status")
    private SerializedItemStatus status;

    @Schema(description = "Batch identifier")
    private String batchId;

    @Schema(description = "Expiry date")
    private LocalDate expiryDate;

    @Schema(description = "Location identifier")
    private String locationId;

    @Schema(description = "Bin location")
    private String binLocation;

    @Schema(description = "Cost value")
    private Double costValue;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Status change timestamp")
    private LocalDateTime statusChangedAt;

    @Schema(description = "User who changed status")
    private String statusChangedBy;

    @Schema(description = "Notes")
    private String notes;
}
