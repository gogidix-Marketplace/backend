package com.gogidix.shared.warehousing.quality.application.command;

import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQualityCheckCommand {

    private QualityCheck.QualityStatus status;
    private String inspectorId;
    private String inspectorName;
    private String locationId;
    private List<QualityCheck.CheckedItem> items;
    private List<QualityCheck.Defect> defects;
    private String notes;
    private List<String> attachments;
    private List<String> correctiveActions;
}
