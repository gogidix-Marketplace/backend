package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a KPI (Key Performance Indicator) board configuration and data.
 * This model defines customizable dashboards for tracking business performance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "kpi_boards")
public class KPIBoard {

    @Id
    private String id;

    @NotBlank(message = "Board name is required")
    private String name;

    private String description;

    @NotBlank(message = "Owner is required")
    private String owner;

    private List<String> viewers;

    @NotBlank(message = "Scope is required")
    private BoardScope scope;

    private String scopeId;

    @NotNull(message = "KPIs are required")
    @NotEmpty(message = "At least one KPI is required")
    @Valid
    private List<KPIItem> kpis;

    @NotNull(message = "Layout is required")
    @Valid
    private BoardLayout layout;

    @Valid
    private BoardPreferences preferences;

    @Valid
    private RefreshSchedule refreshSchedule;

    @Builder.Default
    private BoardStatus status = BoardStatus.ACTIVE;

    private Integer version;

    private String templateId;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIItem {
        @NotBlank(message = "KPI ID is required")
        private String kpiId;

        @NotBlank(message = "KPI name is required")
        private String name;

        private String description;

        @NotNull(message = "KPI type is required")
        private KPIType type;

        @NotNull(message = "DataSource is required")
        private DataSource dataSource;

        @NotNull(message = "Display configuration is required")
        private DisplayConfig display;

        private AggregationType aggregationType;

        private ComparisonType comparisonType;

        private String comparisonPeriod;

        @Valid
        private ThresholdConfig threshold;

        private String unit;

        private Integer decimalPlaces;

        @Valid
        private KPIValue currentValue;

        @Valid
        private KPIValue previousValue;

        private BigDecimal changePercentage;

        private String trend;

        @Valid
        private List<KPIValue> historicalValues;

        private Integer displayOrder;

        private Boolean visible;

        private Map<String, Object> metadata;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DataSource {
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
        public static class DisplayConfig {
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
        public static class ThresholdConfig {
            private ThresholdType type;
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
        public static class KPIValue {
            private BigDecimal value;
            private Instant timestamp;
            private String period;
            private Map<String, Object> attributes;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardLayout {
        private String layoutType;
        private Integer columns;
        private Integer rows;
        @Valid
        private List<LayoutItem> items;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class LayoutItem {
            private String kpiId;
            private Integer column;
            private Integer row;
            private Integer columnSpan;
            private Integer rowSpan;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardPreferences {
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
    public static class RefreshSchedule {
        private String scheduleType;
        private String cronExpression;
        private Integer intervalMinutes;
        private String timeZone;
        private Boolean includeWeekends;
    }

    public enum BoardScope {
        GLOBAL,
        REGIONAL,
        COUNTRY,
        BUSINESS_UNIT,
        DEPARTMENT,
        CUSTOM
    }

    public enum BoardStatus {
        ACTIVE,
        INACTIVE,
        ARCHIVED,
        DRAFT
    }

    public enum KPIType {
        FINANCIAL,
        OPERATIONAL,
        CUSTOMER,
        SALES,
        MARKETING,
        HR,
        CUSTOM
    }

    public enum AggregationType {
        SUM,
        AVERAGE,
        COUNT,
        MIN,
        MAX,
        MEDIAN,
        PERCENTAGE,
        RATIO
    }

    public enum ComparisonType {
        PERIOD_OVER_PERIOD,
        YEAR_OVER_YEAR,
        TARGET,
        BENCHMARK
    }

    public enum ThresholdType {
        ABSOLUTE,
        PERCENTAGE,
        STANDARD_DEVIATION
    }

    public void updateKPIValue(String kpiId, BigDecimal value, Instant timestamp) {
        kpis.stream()
            .filter(kpi -> kpi.getKpiId().equals(kpiId))
            .findFirst()
            .ifPresent(kpi -> {
                kpi.setCurrentValue(KPIItem.KPIValue.builder()
                    .value(value)
                    .timestamp(timestamp)
                    .build());
            });
    }

    public BigDecimal calculateKPITrend(String kpiId) {
        return kpis.stream()
            .filter(kpi -> kpi.getKpiId().equals(kpiId))
            .findFirst()
            .map(kpi -> {
                if (kpi.getCurrentValue() != null && kpi.getPreviousValue() != null &&
                    kpi.getCurrentValue().getValue() != null && kpi.getPreviousValue().getValue() != null &&
                    kpi.getPreviousValue().getValue().compareTo(BigDecimal.ZERO) != 0) {
                    return kpi.getCurrentValue().getValue()
                        .subtract(kpi.getPreviousValue().getValue())
                        .divide(kpi.getPreviousValue().getValue(), 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                }
                return BigDecimal.ZERO;
            })
            .orElse(BigDecimal.ZERO);
    }

    public boolean isKPIAboveThreshold(String kpiId, BigDecimal threshold) {
        return kpis.stream()
            .filter(kpi -> kpi.getKpiId().equals(kpiId))
            .findFirst()
            .map(kpi -> kpi.getCurrentValue() != null &&
                       kpi.getCurrentValue().getValue() != null &&
                       kpi.getCurrentValue().getValue().compareTo(threshold) > 0)
            .orElse(false);
    }
}
