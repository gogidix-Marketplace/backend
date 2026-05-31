package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.enums.AggregationLevel;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.ComparisonType;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.MetricType;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.TimePeriod;
import com.gogidix.hr.globalworkforceanalytics.domain.event.MetricCreatedEvent;
import com.gogidix.hr.globalworkforceanalytics.domain.event.MetricUpdatedEvent;
import com.gogidix.hr.globalworkforceanalytics.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * WorkforceMetric Domain Entity
 * Represents workforce metrics for analytics and reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "workforce_metrics")
public class WorkforceMetric extends BaseEntity {

    @Indexed(unique = true)
    private String metricCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String countryCode;

    @Indexed
    private MetricType metricType;

    private String metricName;
    private String description;
    private String category;

    @Indexed
    private TimePeriod timePeriod;

    private YearMonth period;
    private LocalDate startDate;
    private LocalDate endDate;

    @Indexed
    private AggregationLevel aggregationLevel;

    private BigDecimal value;
    private String displayValue;
    private String unit;

    private BigDecimal previousValue;
    private BigDecimal changeValue;
    private BigDecimal changePercentage;

    @Indexed
    private ComparisonType comparisonType;

    private String dataSource;
    private String calculationMethod;
    private Integer sampleSize;

    @Indexed
    private List<String> dimensionValues;

    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    @Builder.Default
    private Map<String, String> tags = new HashMap<>();

    private Boolean isBenchmark;
    private BigDecimal benchmarkValue;
    private String benchmarkSource;

    private String targetValue;
    private String thresholdMin;
    private String thresholdMax;

    private String status;
    private String confidence;
    private String notes;

    @Indexed
    private String createdBy;

    @Indexed
    private String lastModifiedBy;

    @Indexed
    private Boolean isPublished;

    @Indexed
    private Boolean isArchived;

    private String parentMetricId;
    @Builder.Default
    private List<String> childMetricIds = new ArrayList<>();

    @Builder.Default
    private List<String> trendAnalysisIds = new ArrayList<>();

    @Builder.Default
    private List<String> reportIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Creates a new workforce metric
     */
    public static WorkforceMetric create(String tenantId, String countryCode,
                                         MetricType metricType, String metricName,
                                         TimePeriod timePeriod, YearMonth period,
                                         AggregationLevel aggregationLevel,
                                         BigDecimal value, String unit, String createdBy) {
        String metricCode = generateMetricCode(tenantId, metricType, period);

        WorkforceMetric metric = new WorkforceMetric();
        metric.setTenantId(tenantId);
        metric.setCountryCode(countryCode);
        metric.setMetricType(metricType);
        metric.setMetricName(metricName);
        metric.setMetricCode(metricCode);
        metric.setTimePeriod(timePeriod);
        metric.setPeriod(period);
        metric.setAggregationLevel(aggregationLevel);
        metric.setValue(value);
        metric.setUnit(unit);
        metric.setStatus("DRAFT");
        metric.setIsPublished(false);
        metric.setIsArchived(false);
        metric.setIsBenchmark(false);
        metric.setCreatedBy(createdBy);
        metric.setLastModifiedBy(createdBy);
        metric.setDimensionValues(new ArrayList<>());
        metric.setAttributes(new HashMap<>());
        metric.setTags(new HashMap<>());
        metric.setChildMetricIds(new ArrayList<>());
        metric.setTrendAnalysisIds(new ArrayList<>());
        metric.setReportIds(new ArrayList<>());
        metric.setMetadata(new HashMap<>());

        metric.addDomainEvent(MetricCreatedEvent.builder()
                .metricId(metric.getId())
                .tenantId(tenantId)
                .metricCode(metricCode)
                .metricName(metricName)
                .eventType("METRIC_CREATED")
                .build());

        return metric;
    }

    /**
     * Updates the metric value
     */
    public void updateValue(BigDecimal newValue, String modifiedBy) {
        if (this.isPublished) {
            throw new IllegalStateException("Cannot update published metric");
        }

        this.previousValue = this.value;
        this.value = newValue;
        this.lastModifiedBy = modifiedBy;

        if (this.previousValue != null && this.value != null) {
            this.changeValue = this.value.subtract(this.previousValue);
            if (this.previousValue.compareTo(BigDecimal.ZERO) != 0) {
                this.changePercentage = this.changeValue
                        .divide(this.previousValue, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }
        }

        this.addDomainEvent(MetricUpdatedEvent.builder()
                .metricId(this.getId())
                .tenantId(this.tenantId)
                .metricCode(this.metricCode)
                .previousValue(this.previousValue)
                .newValue(newValue)
                .eventType("METRIC_UPDATED")
                .build());
    }

    /**
     * Publishes the metric
     */
    public void publish(String publishedBy) {
        if (this.value == null) {
            throw new IllegalStateException("Cannot publish metric without value");
        }
        this.isPublished = true;
        this.status = "PUBLISHED";
        this.lastModifiedBy = publishedBy;
    }

    /**
     * Archives the metric
     */
    public void archive(String archivedBy) {
        this.isArchived = true;
        this.status = "ARCHIVED";
        this.lastModifiedBy = archivedBy;
    }

    /**
     * Sets benchmark data
     */
    public void setBenchmark(BigDecimal benchmarkValue, String benchmarkSource) {
        this.isBenchmark = true;
        this.benchmarkValue = benchmarkValue;
        this.benchmarkSource = benchmarkSource;
    }

    /**
     * Adds a dimension value
     */
    public void addDimensionValue(String dimension) {
        if (this.dimensionValues == null) {
            this.dimensionValues = new ArrayList<>();
        }
        if (!this.dimensionValues.contains(dimension)) {
            this.dimensionValues.add(dimension);
        }
    }

    /**
     * Adds a tag
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new HashMap<>();
        }
        this.tags.put(key, value);
    }

    /**
     * Adds an attribute
     */
    public void addAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
    }

    /**
     * Sets the comparison type
     */
    public void setComparisonType(ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    /**
     * Calculates change from previous period
     */
    public void calculateChange(BigDecimal previousPeriodValue) {
        this.previousValue = previousPeriodValue;
        if (this.value != null && this.previousValue != null) {
            this.changeValue = this.value.subtract(this.previousValue);
            if (this.previousValue.compareTo(BigDecimal.ZERO) != 0) {
                this.changePercentage = this.changeValue
                        .divide(this.previousValue, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }
        }
    }

    /**
     * Checks if metric meets target
     */
    public boolean meetsTarget() {
        if (targetValue == null || value == null) {
            return false;
        }
        try {
            BigDecimal target = new BigDecimal(targetValue);
            return value.compareTo(target) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if metric is within threshold
     */
    public boolean isWithinThreshold() {
        if (value == null) {
            return false;
        }
        boolean aboveMin = thresholdMin == null || value.compareTo(new BigDecimal(thresholdMin)) >= 0;
        boolean belowMax = thresholdMax == null || value.compareTo(new BigDecimal(thresholdMax)) <= 0;
        return aboveMin && belowMax;
    }

    /**
     * Adds a child metric
     */
    public void addChildMetric(String metricId) {
        if (this.childMetricIds == null) {
            this.childMetricIds = new ArrayList<>();
        }
        if (!this.childMetricIds.contains(metricId)) {
            this.childMetricIds.add(metricId);
        }
    }

    /**
     * Adds a trend analysis
     */
    public void addTrendAnalysis(String trendAnalysisId) {
        if (this.trendAnalysisIds == null) {
            this.trendAnalysisIds = new ArrayList<>();
        }
        if (!this.trendAnalysisIds.contains(trendAnalysisId)) {
            this.trendAnalysisIds.add(trendAnalysisId);
        }
    }

    /**
     * Adds a report
     */
    public void addReport(String reportId) {
        if (this.reportIds == null) {
            this.reportIds = new ArrayList<>();
        }
        if (!this.reportIds.contains(reportId)) {
            this.reportIds.add(reportId);
        }
    }

    /**
     * Gets display value with unit
     */
    public String getDisplayValueWithUnit() {
        if (displayValue != null) {
            return unit != null ? displayValue + " " + unit : displayValue;
        }
        if (value != null) {
            return unit != null ? value + " " + unit : value.toString();
        }
        return "N/A";
    }

    /**
     * Checks if metric is positive trend
     */
    public boolean isPositiveTrend() {
        if (changePercentage == null) {
            return false;
        }
        return changePercentage.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if metric is negative trend
     */
    public boolean isNegativeTrend() {
        if (changePercentage == null) {
            return false;
        }
        return changePercentage.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Gets formatted change percentage
     */
    public String getFormattedChangePercentage() {
        if (changePercentage == null) {
            return "N/A";
        }
        String sign = changePercentage.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "";
        return sign + changePercentage.setScale(2, java.math.RoundingMode.HALF_UP) + "%";
    }

    /**
     * Generates metric code
     */
    private static String generateMetricCode(String tenantId, MetricType metricType, YearMonth period) {
        String prefix = metricType.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String periodStr = period != null ? period.toString() : "CURRENT";
        return prefix + "-" + periodStr + "-" + uniqueId;
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
