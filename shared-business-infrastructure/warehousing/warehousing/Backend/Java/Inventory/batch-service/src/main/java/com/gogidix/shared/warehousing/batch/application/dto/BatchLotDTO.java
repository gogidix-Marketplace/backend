package com.gogidix.shared.warehousing.batch.application.dto;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.LotStatus;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for BatchLot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for batch lots")
public class BatchLotDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Lot number")
    private String lotNumber;

    @Schema(description = "SKU")
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

    @Schema(description = "Total quantity")
    private Integer totalQuantity;

    @Schema(description = "Available quantity")
    private Integer availableQuantity;

    @Schema(description = "Reserved quantity")
    private Integer reservedQuantity;

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

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
