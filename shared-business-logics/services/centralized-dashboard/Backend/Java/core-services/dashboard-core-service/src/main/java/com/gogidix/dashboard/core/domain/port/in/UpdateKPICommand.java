package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Input port command for updating an existing KPI.
 */
@Data
@Builder
public class UpdateKPICommand {

    @NotBlank(message = "KPI ID is required")
    private String kpiId;

    private String name;

    private String description;

    private String category;

    private String unit;

    private String dataType;

    private String aggregationType;

    private String formula;

    private Boolean isActive;

    private Boolean isRealTime;

    private Integer refreshIntervalSeconds;

    private Double thresholdWarning;

    private Double thresholdCritical;

    private Double targetValue;

    private String updatedBy;
}
