package com.gogidix.hr.globalhrdashboard.domain.model;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity - Regional Metric
 * Represents HR metrics aggregated at the regional level
 */
@Document(collection = "regional_metrics")
public class RegionalMetric extends BaseEntity {

    public enum RegionCode {
        EUROPE("Europe", "European region"),
        AFRICA("Africa", "African region"),
        ASIA("Asia", "Asian region"),
        NORTH_AMERICA("North America", "North American region"),
        SOUTH_AMERICA("South America", "South American region"),
        OCEANIA("Oceania", "Australia and Pacific region"),
        MIDDLE_EAST("Middle East", "Middle Eastern region");

        private final String displayName;
        private final String description;

        RegionCode(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }
    }

    @Indexed
    private String regionCode;

    @Indexed
    private String regionName;

    @Indexed
    private String metricName;

    @Indexed
    private MetricCategory metricCategory;

    private Double value;

    private Double previousValue;

    @Indexed
    private String period;

    private MetricTrend trend;

    @Indexed
    private Instant lastUpdated;

    private List<CountryMetric> countries;

    private Integer totalCountries;

    private Double targetValue;

    private Boolean isActive;

    protected RegionalMetric() {
        super();
        this.countries = new ArrayList<>();
        this.isActive = true;
    }

    public RegionalMetric(String tenantId, String regionCode, String metricName,
                           MetricCategory metricCategory, Double value, String period) {
        super(tenantId);
        setRegionCode(regionCode);
        setRegionName(regionCode);
        setMetricName(metricName);
        setMetricCategory(metricCategory);
        setValue(value);
        setPeriod(period);
        this.countries = new ArrayList<>();
        this.isActive = true;
        this.lastUpdated = Instant.now();
    }

    public void setRegionCode(String regionCode) {
        if (regionCode == null || regionCode.isBlank()) {
            throw new ValidationException("regionCode", "Region code cannot be null or blank");
        }
        this.regionCode = regionCode;
        updateTimestamp();
    }

    public void setRegionName(String regionName) {
        if (regionName == null || regionName.isBlank()) {
            throw new ValidationException("regionName", "Region name cannot be null or blank");
        }
        this.regionName = regionName;
        updateTimestamp();
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

    public void setValue(Double value) {
        if (value == null) {
            throw new ValidationException("value", "Value cannot be null");
        }
        if (this.value != null) {
            this.previousValue = this.value;
        }
        this.value = value;
        calculateTrend();
        this.lastUpdated = Instant.now();
        updateTimestamp();
    }

    public void setPreviousValue(Double previousValue) {
        this.previousValue = previousValue;
        calculateTrend();
        updateTimestamp();
    }

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setTrend(MetricTrend trend) {
        this.trend = trend;
        updateTimestamp();
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setCountries(List<CountryMetric> countries) {
        this.countries = countries != null ? countries : new ArrayList<>();
        this.totalCountries = this.countries.size();
        updateTimestamp();
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
        updateTimestamp();
    }

    public void setActive(Boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getMetricName() {
        return metricName;
    }

    public MetricCategory getMetricCategory() {
        return metricCategory;
    }

    public Double getValue() {
        return value;
    }

    public Double getPreviousValue() {
        return previousValue;
    }

    public String getPeriod() {
        return period;
    }

    public MetricTrend getTrend() {
        return trend;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public List<CountryMetric> getCountries() {
        return countries;
    }

    public Integer getTotalCountries() {
        return totalCountries;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void addCountry(CountryMetric country) {
        if (this.countries == null) {
            this.countries = new ArrayList<>();
        }
        this.countries.add(country);
        this.totalCountries = this.countries.size();
        updateTimestamp();
    }

    public void removeCountry(String countryCode) {
        if (this.countries != null) {
            this.countries.removeIf(c -> c.getCountryCode().equals(countryCode));
            this.totalCountries = this.countries.size();
            updateTimestamp();
        }
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

    public void recordUpdate() {
        this.lastUpdated = Instant.now();
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegionalMetric that = (RegionalMetric) o;
        return Objects.equals(regionCode, that.regionCode) &&
                Objects.equals(metricName, that.metricName) &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), regionCode, metricName, period);
    }

    @Override
    public String toString() {
        return "RegionalMetric{" +
                "id='" + id + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", metricName='" + metricName + '\'' +
                ", value=" + value +
                ", period='" + period + '\'' +
                '}';
    }

    /**
     * Embedded class for country-level metrics within a region
     */
    public static class CountryMetric {
        private String countryCode;
        private String countryName;
        private Double value;
        private MetricTrend trend;

        public CountryMetric() {
        }

        public CountryMetric(String countryCode, String countryName, Double value) {
            this.countryCode = countryCode;
            this.countryName = countryName;
            this.value = value;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public MetricTrend getTrend() {
            return trend;
        }

        public void setTrend(MetricTrend trend) {
            this.trend = trend;
        }
    }
}
