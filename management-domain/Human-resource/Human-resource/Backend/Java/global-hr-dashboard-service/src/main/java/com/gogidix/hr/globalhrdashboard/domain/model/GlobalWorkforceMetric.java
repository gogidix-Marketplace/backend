package com.gogidix.hr.globalhrdashboard.domain.model;

import lombok.Builder;
import lombok.AllArgsConstructor;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Domain Entity - Global Workforce Metric
 * Represents high-level HR metrics aggregated across the entire organization
 */
@Document(collection = "global_workforce_metrics")
@Builder
@AllArgsConstructor
public class GlobalWorkforceMetric extends BaseEntity {

    @Indexed
    private String metricName;

    @Indexed
    private MetricCategory metricCategory;

    @Indexed
    private ExecutiveLevel executiveLevel;

    private Double value;

    private Double previousValue;

    private Double targetValue;

    @Indexed
    private String period;

    private MetricTrend trend;

    @Indexed
    private AggregationLevel aggregationLevel;

    private Map<String, RegionalMetric> regionalBreakdown;

    private Map<String, Object> metadata;

    @Indexed
    private Boolean isActive;

    @Indexed
    private Instant lastAggregated;

    protected GlobalWorkforceMetric() {
        super();
        this.isActive = true;
        this.regionalBreakdown = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    public GlobalWorkforceMetric(String tenantId, String metricName, MetricCategory metricCategory,
                                  ExecutiveLevel executiveLevel, Double value, String period) {
        super(tenantId);
        setMetricName(metricName);
        setMetricCategory(metricCategory);
        setExecutiveLevel(executiveLevel);
        setValue(value);
        setPeriod(period);
        this.aggregationLevel = AggregationLevel.GLOBAL;
        this.isActive = true;
        this.regionalBreakdown = new HashMap<>();
        this.metadata = new HashMap<>();
        this.lastAggregated = Instant.now();
    }

    public void setMetricName(String metricName) {
        if (metricName == null || metricName.isBlank()) {
            throw new ValidationException("metricName", "Metric name cannot be null or blank");
        }
        this.metricName = metricName;
        updateTimestamp();
    }

    public void setMetricCategory(MetricCategory metricCategory) {
        this.metricCategory = Objects.requireNonNull(metricCategory, "metricCategory is required");
        updateTimestamp();
    }

    public void setExecutiveLevel(ExecutiveLevel executiveLevel) {
        this.executiveLevel = Objects.requireNonNull(executiveLevel, "executiveLevel is required");
        updateTimestamp();
    }

    public void setValue(Double value) {
        if (value == null) {
            throw new ValidationException("value", "Value cannot be null");
        }
        if (this.value != null) {
            this.previousValue = this.value;
        }
        this.value = value;
        calculateTrend();
        updateTimestamp();
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
        updateTimestamp();
    }

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setAggregationLevel(AggregationLevel aggregationLevel) {
        this.aggregationLevel = Objects.requireNonNull(aggregationLevel, "aggregationLevel is required");
        updateTimestamp();
    }

    public void setRegionalBreakdown(Map<String, RegionalMetric> regionalBreakdown) {
        this.regionalBreakdown = regionalBreakdown != null ? regionalBreakdown : new HashMap<>();
        updateTimestamp();
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
        updateTimestamp();
    }

    public void setActive(Boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public void setLastAggregated(Instant lastAggregated) {
        this.lastAggregated = lastAggregated;
    }

    public String getMetricName() {
        return metricName;
    }

    public MetricCategory getMetricCategory() {
        return metricCategory;
    }

    public ExecutiveLevel getExecutiveLevel() {
        return executiveLevel;
    }

    public Double getValue() {
        return value;
    }

    public Double getPreviousValue() {
        return previousValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public String getPeriod() {
        return period;
    }

    public MetricTrend getTrend() {
        return trend;
    }

    public AggregationLevel getAggregationLevel() {
        return aggregationLevel;
    }

    public Map<String, RegionalMetric> getRegionalBreakdown() {
        return regionalBreakdown;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Instant getLastAggregated() {
        return lastAggregated;
    }

    public void addRegionalMetric(String regionCode, RegionalMetric regionalMetric) {
        if (this.regionalBreakdown == null) {
            this.regionalBreakdown = new HashMap<>();
        }
        this.regionalBreakdown.put(regionCode, regionalMetric);
        updateTimestamp();
    }

    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
        updateTimestamp();
    }

    public void calculateTrend() {
        if (previousValue != null && value != null) {
            this.trend = MetricTrend.fromValueComparison(value, previousValue);
        } else {
            this.trend = MetricTrend.STABLE;
        }
    }

    public Double getVariance() {
        if (value != null && previousValue != null) {
            return value - previousValue;
        }
        return 0.0;
    }

    public Double getVariancePercent() {
        if (value != null && previousValue != null && previousValue != 0) {
            return ((value - previousValue) / previousValue) * 100;
        }
        return 0.0;
    }

    public Double getTargetAchievement() {
        if (targetValue != null && targetValue != 0 && value != null) {
            return (value / targetValue) * 100;
        }
        return null;
    }

    public boolean isTargetAchieved() {
        Double achievement = getTargetAchievement();
        return achievement != null && achievement >= 100;
    }

    public boolean isOnTrack() {
        Double achievement = getTargetAchievement();
        return achievement != null && achievement >= 80;
    }

    public void recordAggregation() {
        this.lastAggregated = Instant.now();
        updateTimestamp();
    }

    public void deactivate() {
        this.isActive = false;
        updateTimestamp();
    }

    public void activate() {
        this.isActive = true;
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GlobalWorkforceMetric that = (GlobalWorkforceMetric) o;
        return Objects.equals(metricName, that.metricName) &&
                metricCategory == that.metricCategory &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metricName, metricCategory, period);
    }

    @Override
    public String toString() {
        return "GlobalWorkforceMetric{" +
                "id='" + id + '\'' +
                ", metricName='" + metricName + '\'' +
                ", metricCategory=" + metricCategory +
                ", value=" + value +
                ", period='" + period + '\'' +
                ", trend=" + trend +
                '}';
    }
}
