package com.gogidix.shared.warehousing.batch.application.command;

import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.LotStatus;
import com.gogidix.shared.warehousing.batch.domain.entity.BatchLot.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * Command to update a batch lot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Command to update a batch lot")
public class UpdateBatchLotCommand {

    @Schema(description = "Description")
    private String description;

    @Schema(description = "Expiration date")
    private LocalDate expirationDate;

    @Schema(description = "Total quantity")
    private Integer totalQuantity;

    @Schema(description = "Available quantity")
    private Integer availableQuantity;

    @Schema(description = "Reserved quantity")
    private Integer reservedQuantity;

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
