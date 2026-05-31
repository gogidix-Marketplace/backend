package com.gogidix.shared.warehousing.serialization.application.command;

import com.gogidix.shared.warehousing.serialization.domain.entity.Batch.BatchStatus;
import com.gogidix.shared.warehousing.serialization.domain.entity.Batch.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * Command to create a new batch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to create a batch")
public class CreateBatchCommand {

    @NotBlank(message = "Batch number is required")
    @Schema(description = "Unique batch number", required = true)
    private String batchNumber;

    @NotBlank(message = "SKU is required")
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Manufacturing date")
    private LocalDate manufacturingDate;

    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Schema(description = "Supplier ID")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Country of origin")
    private String countryOfOrigin;

    @NotNull(message = "Total quantity is required")
    @Schema(description = "Total quantity", required = true)
    private Integer totalQuantity;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Status", defaultValue = "ACTIVE")
    private BatchStatus status;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "QC Status")
    private QCStatus qcStatus;

    @Schema(description = "QC Notes")
    private String qcNotes;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;
}
