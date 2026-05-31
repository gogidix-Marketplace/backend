package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for KPIBoard domain model.
 * Used for API request/response operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPIBoardDto {

    private String id;

    @NotBlank(message = "Board name is required")
    private String name;

    private String description;

    @NotBlank(message = "Owner is required")
    private String owner;

    private List<String> viewers;

    @NotBlank(message = "Scope is required")
    private String scope;

    private String scopeId;

    @NotNull(message = "KPIs are required")
    @NotEmpty(message = "At least one KPI is required")
    @Valid
    private List<KPIItemDto> kpis;

    @NotNull(message = "Layout is required")
    @Valid
    private BoardLayoutDto layout;

    private BoardPreferencesDto preferences;

    private RefreshScheduleDto refreshSchedule;

    private String status;

    private Integer version;

    private String templateId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Instant updatedAt;

    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIItemDto {
        @NotBlank(message = "KPI ID is required")
        private String kpiId;

        @NotBlank(message = "KPI name is required")
        private String name;

        private String description;

        @NotNull(message = "KPI type is required")
        private String type;

        @NotNull(message = "DataSource is required")
        private DataSourceDto dataSource;

        @NotNull(message = "Display configuration is required")
        private DisplayConfigDto display;

        private String aggregationType;

        private String comparisonType;

        private String comparisonPeriod;

        private ThresholdConfigDto threshold;

        private String unit;

        private Integer decimalPlaces;

        private KPIValueDto currentValue;

        private KPIValueDto previousValue;

        private BigDecimal changePercentage;

        private String trend;

        private List<KPIValueDto> historicalValues;

        private Integer displayOrder;

        private Boolean visible;

        private Map<String, Object> metadata;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSourceDto {
        private String sourceType;
        private String endpoint;
        private String query;
        private String collection;
        private Map<String, Object> parameters;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisplayConfigDto {
        private String visualizationType;
        private String color;
        private Integer width;
        private Integer height;
        private Boolean showSparkline;
        private Boolean showTrend;
        private Boolean showComparison;
        private String chartType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThresholdConfigDto {
        private String type;
        private BigDecimal warningThreshold;
        private BigDecimal criticalThreshold;
        private BigDecimal targetThreshold;
        private String warningColor;
        private String criticalColor;
        private String targetColor;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIValueDto {
        private BigDecimal value;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private Instant timestamp;
        private String period;
        private Map<String, Object> attributes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardLayoutDto {
        private String layoutType;
        private Integer columns;
        private Integer rows;
        private List<LayoutItemDto> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LayoutItemDto {
        private String kpiId;
        private Integer column;
        private Integer row;
        private Integer columnSpan;
        private Integer rowSpan;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardPreferencesDto {
        private String theme;
        private Boolean autoRefresh;
        private Integer refreshInterval;
        private String timeZone;
        private String dateFormat;
        private String numberFormat;
        private Boolean showAnnotations;
        private Boolean enableDrillDown;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshScheduleDto {
        private String scheduleType;
        private String cronExpression;
        private Integer intervalMinutes;
        private String timeZone;
        private Boolean includeWeekends;
    }
}
