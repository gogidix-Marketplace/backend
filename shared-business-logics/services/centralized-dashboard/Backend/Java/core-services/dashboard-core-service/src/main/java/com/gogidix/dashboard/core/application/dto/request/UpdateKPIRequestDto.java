package com.gogidix.dashboard.core.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating an existing KPI.
 */
@Data
@Schema(description = "Request to update an existing Key Performance Indicator")
public class UpdateKPIRequestDto {

    @Schema(description = "Updated name of the KPI")
    private String name;

    @Schema(description = "Updated description of the KPI")
    private String description;

    @Schema(description = "Updated category of the KPI")
    private String category;

    @Schema(description = "Updated unit of measurement")
    private String unit;

    @Schema(description = "Updated data type of the KPI")
    private String dataType;

    @Schema(description = "Updated aggregation type")
    private String aggregationType;

    @Schema(description = "Updated formula for calculation")
    private String formula;

    @Schema(description = "Updated active status")
    private Boolean isActive;

    @Schema(description = "Updated real-time flag")
    private Boolean isRealTime;

    @Schema(description = "Updated refresh interval in seconds")
    private Integer refreshIntervalSeconds;

    @Schema(description = "Updated warning threshold")
    private Double thresholdWarning;

    @Schema(description = "Updated critical threshold")
    private Double thresholdCritical;

    @Schema(description = "Updated target value")
    private Double targetValue;
}
