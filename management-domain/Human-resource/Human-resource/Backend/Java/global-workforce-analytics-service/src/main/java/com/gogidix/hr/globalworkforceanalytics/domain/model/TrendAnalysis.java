package com.gogidix.hr.globalworkforceanalytics.domain.model;

import com.gogidix.hr.globalworkforceanalytics.domain.enums.ComparisonType;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.TrendType;
import com.gogidix.hr.globalworkforceanalytics.domain.enums.TimePeriod;
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
 * TrendAnalysis Domain Entity
 * Represents trend analysis for workforce metrics over time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "trend_analyses")
public class TrendAnalysis extends BaseEntity {

    @Indexed(unique = true)
    private String analysisCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String countryCode;

    private String analysisName;
    private String description;
    private String category;

    @Indexed
    private String metricId;

    private String metricName;
    private String metricCode;

    @Indexed
    private TrendType trendType;

    @Indexed
    private TimePeriod timePeriod;

    @Indexed
    private List<YearMonth> periods;

    @Builder.Default
    private List<TrendDataPoint> dataPoints = new ArrayList<>();

    @Builder.Default
    private Map<String, BigDecimal> periodValues = new HashMap<>();

    private BigDecimal startValue;
    private BigDecimal endValue;
    private BigDecimal totalChange;
    private BigDecimal averageChange;
    private BigDecimal changePercentage;

    private TrendDirection direction;
    private BigDecimal trendSlope;
    private String trendStrength;

    private Boolean isSeasonal;
    @Builder.Default
    private Map<String, BigDecimal> seasonalityFactors = new HashMap<>();

    private Boolean isSignificant;
    private BigDecimal confidenceLevel;
    private String significanceLevel;

    @Indexed
    private ComparisonType comparisonType;

    private String forecastType;
    @Builder.Default
    private List<ForecastDataPoint> forecastPoints = new ArrayList<>();

    private BigDecimal forecastAccuracy;
    private String lastForecastDate;

    @Indexed
    private String analysisScope;

    @Builder.Default
    private Map<String, Object> dimensions = new HashMap<>();

    @Builder.Default
    private List<String> dimensionValues = new ArrayList<>();

    @Builder.Default
    private List<String> anomalyPeriods = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> insights = new HashMap<>();

    @Builder.Default
    private List<String> recommendations = new ArrayList<>();

    private String visualizationType;
    @Builder.Default
    private Map<String, Object> chartConfig = new HashMap<>();

    @Indexed
    private String createdBy;

    private LocalDate analysisDate;
    private YearMonth analysisPeriod;

    @Indexed
    private Boolean isPublished;

    @Indexed
    private Boolean isArchived;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    public enum TrendDirection {
        UPWARD,
        DOWNWARD,
        STABLE,
        VOLATILE,
        UNKNOWN
    }

    /**
     * Trend data point
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendDataPoint {
        private YearMonth period;
        private LocalDate date;
        private BigDecimal value;
        private BigDecimal changeFromPrevious;
        private BigDecimal changePercentage;
        private Boolean isAnomaly;
        private String notes;
        private Map<String, Object> attributes;
    }

    /**
     * Forecast data point
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastDataPoint {
        private YearMonth period;
        private LocalDate date;
        private BigDecimal forecastValue;
        private BigDecimal lowerBound;
        private BigDecimal upperBound;
        private BigDecimal confidenceInterval;
        private String forecastMethod;
    }

    /**
     * Creates a new trend analysis
     */
    public static TrendAnalysis create(String tenantId, String countryCode,
                                       String metricId, String metricName,
                                       TrendType trendType, TimePeriod timePeriod,
                                       List<YearMonth> periods, String createdBy) {
        String analysisCode = generateAnalysisCode(tenantId, trendType);

        TrendAnalysis analysis = new TrendAnalysis();
        analysis.setTenantId(tenantId);
        analysis.setCountryCode(countryCode);
        analysis.setMetricId(metricId);
        analysis.setMetricName(metricName);
        analysis.setAnalysisCode(analysisCode);
        analysis.setTrendType(trendType);
        analysis.setTimePeriod(timePeriod);
        analysis.setPeriods(periods);
        analysis.setAnalysisDate(LocalDate.now());
        analysis.setCreatedBy(createdBy);
        analysis.setIsPublished(false);
        analysis.setIsArchived(false);
        analysis.setIsSeasonal(false);
        analysis.setIsSignificant(false);
        analysis.setDataPoints(new ArrayList<>());
        analysis.setPeriodValues(new HashMap<>());
        analysis.setSeasonalityFactors(new HashMap<>());
        analysis.setForecastPoints(new ArrayList<>());
        analysis.setDimensions(new HashMap<>());
        analysis.setDimensionValues(new ArrayList<>());
        analysis.setAnomalyPeriods(new ArrayList<>());
        analysis.setInsights(new HashMap<>());
        analysis.setRecommendations(new ArrayList<>());
        analysis.setChartConfig(new HashMap<>());
        analysis.setTags(new ArrayList<>());
        analysis.setMetadata(new HashMap<>());

        return analysis;
    }

    /**
     * Adds a data point
     */
    public void addDataPoint(TrendDataPoint dataPoint) {
        if (this.dataPoints == null) {
            this.dataPoints = new ArrayList<>();
        }
        this.dataPoints.add(dataPoint);
        if (dataPoint.getPeriod() != null && dataPoint.getValue() != null) {
            this.periodValues.put(dataPoint.getPeriod().toString(), dataPoint.getValue());
        }
        recalculateTrend();
    }

    /**
     * Adds multiple data points
     */
    public void addDataPoints(List<TrendDataPoint> dataPoints) {
        if (this.dataPoints == null) {
            this.dataPoints = new ArrayList<>();
        }
        this.dataPoints.addAll(dataPoints);
        for (TrendDataPoint dp : dataPoints) {
            if (dp.getPeriod() != null && dp.getValue() != null) {
                this.periodValues.put(dp.getPeriod().toString(), dp.getValue());
            }
        }
        recalculateTrend();
    }

    /**
     * Recalculates trend statistics
     */
    public void recalculateTrend() {
        if (this.dataPoints == null || this.dataPoints.isEmpty()) {
            return;
        }

        // Sort by period
        this.dataPoints.sort((a, b) -> a.getPeriod().compareTo(b.getPeriod()));

        // Set start and end values
        this.startValue = this.dataPoints.get(0).getValue();
        this.endValue = this.dataPoints.get(this.dataPoints.size() - 1).getValue();

        // Calculate total change
        if (this.startValue != null && this.endValue != null) {
            this.totalChange = this.endValue.subtract(this.startValue);
            if (this.startValue.compareTo(BigDecimal.ZERO) != 0) {
                this.changePercentage = this.totalChange
                        .divide(this.startValue, 4, java.math.RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }
        }

        // Calculate average change
        BigDecimal totalChangeSum = BigDecimal.ZERO;
        int changeCount = 0;
        for (int i = 1; i < this.dataPoints.size(); i++) {
            BigDecimal prev = this.dataPoints.get(i - 1).getValue();
            BigDecimal curr = this.dataPoints.get(i).getValue();
            if (prev != null && curr != null) {
                totalChangeSum = totalChangeSum.add(curr.subtract(prev));
                changeCount++;
            }
        }
        if (changeCount > 0) {
            this.averageChange = totalChangeSum.divide(
                    BigDecimal.valueOf(changeCount), 4, java.math.RoundingMode.HALF_UP);
        }

        // Determine trend direction
        determineTrendDirection();
    }

    /**
     * Determines trend direction
     */
    private void determineTrendDirection() {
        if (this.changePercentage == null) {
            this.direction = TrendDirection.UNKNOWN;
            return;
        }

        double threshold = 5.0; // 5% threshold
        double change = this.changePercentage.doubleValue();

        if (Math.abs(change) < threshold) {
            this.direction = TrendDirection.STABLE;
        } else if (change > threshold) {
            this.direction = TrendDirection.UPWARD;
        } else {
            this.direction = TrendDirection.DOWNWARD;
        }

        // Check for volatility
        if (this.dataPoints.size() > 2) {
            int directionChanges = 0;
            for (int i = 2; i < this.dataPoints.size(); i++) {
                BigDecimal prevChange = this.dataPoints.get(i - 1).getValue()
                        .subtract(this.dataPoints.get(i - 2).getValue());
                BigDecimal currChange = this.dataPoints.get(i).getValue()
                        .subtract(this.dataPoints.get(i - 1).getValue());
                if (prevChange.multiply(currChange).compareTo(BigDecimal.ZERO) < 0) {
                    directionChanges++;
                }
            }
            if (directionChanges > this.dataPoints.size() / 3) {
                this.direction = TrendDirection.VOLATILE;
            }
        }
    }

    /**
     * Sets trend strength
     */
    public void setTrendStrength(String strength) {
        this.trendStrength = strength;
    }

    /**
     * Calculates trend slope (linear regression)
     */
    public void calculateTrendSlope() {
        if (this.dataPoints == null || this.dataPoints.size() < 2) {
            return;
        }

        int n = this.dataPoints.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i;
            double y = this.dataPoints.get(i).getValue() != null ?
                    this.dataPoints.get(i).getValue().doubleValue() : 0;
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        this.trendSlope = BigDecimal.valueOf(slope);
    }

    /**
     * Detects anomalies in data
     */
    public void detectAnomalies(double threshold) {
        if (this.dataPoints == null || this.dataPoints.size() < 3) {
            return;
        }

        // Calculate mean and standard deviation
        double sum = 0;
        int count = 0;
        for (TrendDataPoint dp : this.dataPoints) {
            if (dp.getValue() != null) {
                sum += dp.getValue().doubleValue();
                count++;
            }
        }
        double mean = sum / count;

        double sumSquaredDiff = 0;
        for (TrendDataPoint dp : this.dataPoints) {
            if (dp.getValue() != null) {
                double diff = dp.getValue().doubleValue() - mean;
                sumSquaredDiff += diff * diff;
            }
        }
        double stdDev = Math.sqrt(sumSquaredDiff / count);

        // Mark anomalies
        this.anomalyPeriods.clear();
        for (TrendDataPoint dp : this.dataPoints) {
            if (dp.getValue() != null) {
                double zScore = Math.abs((dp.getValue().doubleValue() - mean) / stdDev);
                dp.setIsAnomaly(zScore > threshold);
                if (dp.getIsAnomaly() && dp.getPeriod() != null) {
                    this.anomalyPeriods.add(dp.getPeriod().toString());
                }
            }
        }
    }

    /**
     * Sets seasonality
     */
    public void setSeasonality(Boolean isSeasonal, Map<String, BigDecimal> factors) {
        this.isSeasonal = isSeasonal;
        if (factors != null) {
            this.seasonalityFactors = factors;
        }
    }

    /**
     * Sets significance
     */
    public void setSignificance(Boolean isSignificant, BigDecimal confidenceLevel, String significanceLevel) {
        this.isSignificant = isSignificant;
        this.confidenceLevel = confidenceLevel;
        this.significanceLevel = significanceLevel;
    }

    /**
     * Adds forecast point
     */
    public void addForecastPoint(ForecastDataPoint forecastPoint) {
        if (this.forecastPoints == null) {
            this.forecastPoints = new ArrayList<>();
        }
        this.forecastPoints.add(forecastPoint);
    }

    /**
     * Sets forecast accuracy
     */
    public void setForecastAccuracy(BigDecimal accuracy) {
        this.forecastAccuracy = accuracy;
        this.lastForecastDate = LocalDate.now().toString();
    }

    /**
     * Adds an insight
     */
    public void addInsight(String key, Object value) {
        if (this.insights == null) {
            this.insights = new HashMap<>();
        }
        this.insights.put(key, value);
    }

    /**
     * Adds a recommendation
     */
    public void addRecommendation(String recommendation) {
        if (this.recommendations == null) {
            this.recommendations = new ArrayList<>();
        }
        if (!this.recommendations.contains(recommendation)) {
            this.recommendations.add(recommendation);
        }
    }

    /**
     * Sets dimension
     */
    public void setDimension(String key, Object value) {
        if (this.dimensions == null) {
            this.dimensions = new HashMap<>();
        }
        this.dimensions.put(key, value);
    }

    /**
     * Adds dimension value
     */
    public void addDimensionValue(String value) {
        if (this.dimensionValues == null) {
            this.dimensionValues = new ArrayList<>();
        }
        if (!this.dimensionValues.contains(value)) {
            this.dimensionValues.add(value);
        }
    }

    /**
     * Publishes analysis
     */
    public void publish() {
        this.isPublished = true;
    }

    /**
     * Archives analysis
     */
    public void archive() {
        this.isArchived = true;
    }

    /**
     * Adds a tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Gets formatted trend summary
     */
    public String getTrendSummary() {
        if (this.direction == null) {
            return "Insufficient data";
        }
        StringBuilder summary = new StringBuilder();
        summary.append(this.direction.name());
        if (this.changePercentage != null) {
            summary.append(" (").append(this.changePercentage.setScale(2, java.math.RoundingMode.HALF_UP)).append("%)");
        }
        if (this.trendStrength != null) {
            summary.append(" - ").append(this.trendStrength).append(" trend");
        }
        return summary.toString();
    }

    /**
     * Checks if trend is positive
     */
    public boolean isPositiveTrend() {
        return this.direction == TrendDirection.UPWARD;
    }

    /**
     * Checks if trend is negative
     */
    public boolean isNegativeTrend() {
        return this.direction == TrendDirection.DOWNWARD;
    }

    /**
     * Checks if trend is stable
     */
    public boolean isStableTrend() {
        return this.direction == TrendDirection.STABLE;
    }

    /**
     * Generates analysis code
     */
    private static String generateAnalysisCode(String tenantId, TrendType trendType) {
        String prefix = trendType.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "TRN-" + prefix + "-" + uniqueId;
    }
}
