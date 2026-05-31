package com.gogidix.shared.warehousing.serialization.application.dto;

import com.gogidix.shared.warehousing.serialization.domain.entity.Batch.BatchStatus;
import com.gogidix.shared.warehousing.serialization.domain.entity.Batch.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Batch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data transfer object for batches")
public class BatchDTO {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Batch number")
    private String batchNumber;

    @Schema(description = "SKU")
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

    @Schema(description = "Total quantity")
    private Integer totalQuantity;

    @Schema(description = "Available quantity")
    private Integer availableQuantity;

    @Schema(description = "Unit of measure")
    private String unitOfMeasure;

    @Schema(description = "Status")
    private BatchStatus status;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "QC Status")
    private QCStatus qcStatus;

    @Schema(description = "QC Notes")
    private String qcNotes;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
