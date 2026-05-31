package com.gogidix.shared.warehousing.batch.application.command;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.LotStatus;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * Command to create a new batch lot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to create a batch lot")
public class CreateBatchLotCommand {

    @NotBlank(message = "Lot number is required")
    @Schema(description = "Unique lot number", required = true)
    private String lotNumber;

    @NotBlank(message = "SKU is required")
    @Schema(description = "Product SKU", required = true)
    private String sku;

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Production date")
    private LocalDate productionDate;

    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Schema(description = "Supplier ID")
    private String supplierId;

    @Schema(description = "Supplier name")
    private String supplierName;

    @Schema(description = "Country of origin")
    private String countryOfOrigin;

    @Schema(description = "Manufacturing batch number")
    private String manufacturingBatchNumber;

    @NotNull(message = "Total quantity is required")
    @Positive(message = "Total quantity must be positive")
    @Schema(description = "Total quantity", required = true)
    private Integer totalQuantity;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Cost per unit")
    private Double costPerUnit;

    @Schema(description = "Status")
    private LotStatus status;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "QC Status")
    private QCStatus qcStatus;

    @Schema(description = "QC Notes")
    private String qcNotes;

    @Schema(description = "Storage requirements")
    private String storageRequirements;

    @Schema(description = "Additional attributes")
    private Map<String, Object> attributes;
}
