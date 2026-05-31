package com.gogidix.shared.warehousing.quality.application.dto;

import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Quality check data transfer object")
public class QualityCheckDTO {

    @Schema(description = "Quality check ID")
    private String id;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Reference ID")
    private String referenceId;

    @Schema(description = "Reference type")
    private QualityCheck.ReferenceType referenceType;

    @Schema(description = "Status")
    private QualityCheck.QualityStatus status;

    @Schema(description = "Inspector ID")
    private String inspectorId;

    @Schema(description = "Inspector name")
    private String inspectorName;

    @Schema(description = "Inspection date")
    private LocalDateTime inspectionDate;

    @Schema(description = "Location ID")
    private String locationId;

    @Schema(description = "Check type")
    private QualityCheck.CheckType checkType;

    @Schema(description = "Items checked")
    private List<QualityCheck.CheckedItem> items;

    @Schema(description = "Defects found")
    private List<QualityCheck.Defect> defects;

    @Schema(description = "Passed")
    private Boolean passed;

    @Schema(description = "Quality score")
    private Integer qualityScore;

    @Schema(description = "Notes")
    private String notes;

    @Schema(description = "Attachments")
    private List<String> attachments;

    @Schema(description = "Corrective actions")
    private List<String> correctiveActions;

    @Schema(description = "Created at")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
