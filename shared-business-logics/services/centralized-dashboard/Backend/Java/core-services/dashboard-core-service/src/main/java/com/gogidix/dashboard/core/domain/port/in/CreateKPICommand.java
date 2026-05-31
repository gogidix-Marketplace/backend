package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Input port command for creating a new KPI.
 */
@Data
@Builder
public class CreateKPICommand {

    @NotBlank(message = "KPI name is required")
    private String name;

    private String description;

    @NotBlank(message = "KPI code is required")
    private String code;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotNull(message = "Source domain is required")
    private String sourceDomain;

    private String unit;

    @NotBlank(message = "Data type is required")
    private String dataType;

    private String aggregationType;

    private String formula;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isRealTime = false;

    private Integer refreshIntervalSeconds;

    private Double thresholdWarning;

    private Double thresholdCritical;

    private Double targetValue;

    private String createdBy;
}
