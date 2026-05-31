package com.gogidix.dashboard.core.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO for creating a new KPI.
 */
@Data
@Schema(description = "Request to create a new Key Performance Indicator")
public class CreateKPIRequestDto {

    @NotBlank(message = "KPI name is required")
    @Schema(description = "Name of the KPI", example = "Total Orders")
    private String name;

    @Schema(description = "Description of the KPI", example = "Total number of orders across all channels")
    private String description;

    @NotBlank(message = "KPI code is required")
    @Schema(description = "Unique code for the KPI", example = "TOTAL_ORDERS")
    private String code;

    @NotBlank(message = "Category is required")
    @Schema(description = "Category of the KPI", example = "Sales")
    private String category;

    @NotBlank(message = "Source domain is required")
    @Schema(description = "Source domain of the KPI", example = "COURIER_SERVICE")
    private String sourceDomain;

    @Schema(description = "Unit of measurement", example = "count")
    private String unit;

    @NotBlank(message = "Data type is required")
    @Schema(description = "Data type of the KPI", example = "NUMERIC")
    private String dataType;

    @Schema(description = "Aggregation type", example = "SUM")
    private String aggregationType;

    @Schema(description = "Formula for calculation", example = "SUM(order_amount)")
    private String formula;

    @Schema(description = "Whether the KPI is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Whether the KPI is real-time", example = "false")
    private Boolean isRealTime;

    @Schema(description = "Refresh interval in seconds", example = "300")
    private Integer refreshIntervalSeconds;

    @Schema(description = "Warning threshold", example = "1000.0")
    private Double thresholdWarning;

    @Schema(description = "Critical threshold", example = "500.0")
    private Double thresholdCritical;

    @Schema(description = "Target value", example = "5000.0")
    private Double targetValue;
}
