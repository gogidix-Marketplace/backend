package com.gogidix.shared.warehousing.serialization.application.command;

import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem.SerializedItemStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * Command to create a new serialized item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to create a serialized item")
public class CreateSerializedItemCommand {

    @NotBlank(message = "Serial number is required")
    @Schema(description = "Unique serial number", required = true)
    private String serialNumber;

    @NotBlank(message = "SKU is required")
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Schema(description = "Initial status", defaultValue = "AVAILABLE")
    private SerializedItemStatus status;

    @Schema(description = "Batch ID if part of a batch")
    private String batchId;

    @Schema(description = "Expiry date")
    private LocalDate expiryDate;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Bin location")
    private String binLocation;

    @Schema(description = "Cost value")
    private Double costValue;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;

    @Schema(description = "Notes")
    private String notes;
}
