package com.gogidix.shared.warehousing.quality.application.command;

import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQualityCheckCommand {

    @NotBlank(message = "Reference ID is required")
    private String referenceId;

    @NotNull(message = "Reference type is required")
    private QualityCheck.ReferenceType referenceType;

    @NotBlank(message = "Inspector ID is required")
    private String inspectorId;

    private String inspectorName;

    private String locationId;

    @NotNull(message = "Check type is required")
    private QualityCheck.CheckType checkType;

    private List<QualityCheck.CheckedItem> items;

    private List<QualityCheck.Defect> defects;

    private String notes;

    private List<String> attachments;
}
