package com.gogidix.dashboard.core.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for KPI response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Key Performance Indicator response")
public class KPIResponseDto {

    @Schema(description = "KPI ID")
    private UUID id;

    @Schema(description = "KPI name")
    private String name;

    @Schema(description = "KPI description")
    private String description;

    @Schema(description = "KPI code")
    private String code;

    @Schema(description = "KPI category")
    private String category;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Source domain")
    private String sourceDomain;

    @Schema(description = "Unit of measurement")
    private String unit;

    @Schema(description = "Data type")
    private String dataType;

    @Schema(description = "Aggregation type")
    private String aggregationType;

    @Schema(description = "Calculation formula")
    private String formula;

    @Schema(description = "Is KPI active")
    private Boolean isActive;

    @Schema(description = "Is real-time KPI")
    private Boolean isRealTime;

    @Schema(description = "Refresh interval in seconds")
    private Integer refreshIntervalSeconds;

    @Schema(description = "Warning threshold")
    private Double thresholdWarning;

    @Schema(description = "Critical threshold")
    private Double thresholdCritical;

    @Schema(description = "Target value")
    private Double targetValue;

    @Schema(description = "Current value")
    private Double currentValue;

    @Schema(description = "Previous value")
    private Double previousValue;

    @Schema(description = "Trend direction")
    private String trend;

    @Schema(description = "Progress percentage")
    private Double progressPercentage;

    @Schema(description = "Status based on thresholds")
    private String status;

    @Schema(description = "Last calculated timestamp")
    private LocalDateTime lastCalculatedAt;

    @Schema(description = "Created timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Updated timestamp")
    private LocalDateTime updatedAt;

    @Schema(description = "Historical values")
    private List<KPIValueDto> historicalValues;

    @Schema(description = "KPI targets")
    private List<KPITargetDto> targets;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIValueDto {
        private UUID id;
        private Double value;
        private LocalDateTime recordedAt;
        private String metadata;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPITargetDto {
        private UUID id;
        private Double targetValue;
        private String targetPeriod;
        private LocalDate targetDate;
        private LocalDate startDate;
        private LocalDate endDate;
        private Double minimumAcceptable;
        private Double stretchTarget;
        private String owner;
    }
}
