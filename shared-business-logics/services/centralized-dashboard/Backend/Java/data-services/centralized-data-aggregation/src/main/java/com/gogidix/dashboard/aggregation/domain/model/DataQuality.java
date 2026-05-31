package com.gogidix.dashboard.aggregation.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Quality Value Object
 * 
 * Represents data quality metrics and assessment for aggregated metrics
 */
public class DataQuality {
    private final double completeness;      // 0.0 to 1.0 - percentage of expected data points received
    private final double accuracy;         // 0.0 to 1.0 - estimated accuracy based on validation rules
    private final double consistency;      // 0.0 to 1.0 - consistency with historical patterns
    private final double timeliness;       // 0.0 to 1.0 - how timely the data is (freshness)
    private final double validity;         // 0.0 to 1.0 - data conforms to expected format/range
    private final int totalDataPoints;     // Total number of data points in aggregation
    private final int validDataPoints;     // Number of valid data points
    private final int outlierCount;        // Number of detected outliers
    private final LocalDateTime lastUpdated;
    private final QualityLevel overallLevel;
    
    public DataQuality(double completeness, double accuracy, double consistency, 
                      double timeliness, double validity, int totalDataPoints, 
                      int validDataPoints, int outlierCount, LocalDateTime lastUpdated) {
        this.completeness = validateScore(completeness, "Completeness");
        this.accuracy = validateScore(accuracy, "Accuracy");
        this.consistency = validateScore(consistency, "Consistency");
        this.timeliness = validateScore(timeliness, "Timeliness");
        this.validity = validateScore(validity, "Validity");
        this.totalDataPoints = validateCount(totalDataPoints, "Total data points");
        this.validDataPoints = validateCount(validDataPoints, "Valid data points");
        this.outlierCount = validateCount(outlierCount, "Outlier count");
        this.lastUpdated = Objects.requireNonNull(lastUpdated, "Last updated cannot be null");
        this.overallLevel = calculateOverallLevel();
        
        validateDataPointsConsistency();
    }
    
    public static DataQuality excellent(int dataPoints) {
        return new DataQuality(1.0, 0.98, 0.95, 1.0, 0.99, dataPoints, dataPoints, 0, LocalDateTime.now());
    }
    
    public static DataQuality good(int dataPoints, int outliers) {
        double completeness = Math.max(0.85, (double)(dataPoints - outliers) / dataPoints);
        return new DataQuality(completeness, 0.90, 0.85, 0.95, 0.90, dataPoints, dataPoints - outliers, outliers, LocalDateTime.now());
    }
    
    public static DataQuality poor(int dataPoints, int validPoints, int outliers) {
        double completeness = (double) validPoints / dataPoints;
        return new DataQuality(completeness, 0.70, 0.60, 0.70, 0.75, dataPoints, validPoints, outliers, LocalDateTime.now());
    }
    
    public static DataQuality unknown() {
        return new DataQuality(0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, LocalDateTime.now());
    }
    
    private double validateScore(double score, String name) {
        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException(name + " score must be between 0.0 and 1.0");
        }
        return score;
    }
    
    private int validateCount(int count, String name) {
        if (count < 0) {
            throw new IllegalArgumentException(name + " cannot be negative");
        }
        return count;
    }
    
    private void validateDataPointsConsistency() {
        if (validDataPoints > totalDataPoints) {
            throw new IllegalArgumentException("Valid data points cannot exceed total data points");
        }
        if (outlierCount > totalDataPoints) {
            throw new IllegalArgumentException("Outlier count cannot exceed total data points");
        }
    }
    
    private QualityLevel calculateOverallLevel() {
        double overallScore = getOverallScore();
        
        if (overallScore >= 0.90) return QualityLevel.EXCELLENT;
        if (overallScore >= 0.75) return QualityLevel.GOOD;
        if (overallScore >= 0.50) return QualityLevel.FAIR;
        if (overallScore >= 0.25) return QualityLevel.POOR;
        return QualityLevel.UNACCEPTABLE;
    }
    
    /**
     * Calculate overall quality score (weighted average)
     */
    public double getOverallScore() {
        // Weighted scoring: completeness and accuracy are most important
        return (completeness * 0.30) + (accuracy * 0.25) + (validity * 0.20) + 
               (timeliness * 0.15) + (consistency * 0.10);
    }
    
    /**
     * Get confidence level for this data quality
     */
    public double getConfidenceLevel() {
        if (totalDataPoints == 0) return 0.0;
        
        double baseConfidence = getOverallScore();
        
        // Adjust based on data volume
        if (totalDataPoints < 10) {
            baseConfidence *= 0.8; // Lower confidence with less data
        } else if (totalDataPoints > 100) {
            baseConfidence = Math.min(1.0, baseConfidence * 1.1); // Higher confidence with more data
        }
        
        // Adjust based on outliers
        double outlierRatio = (double) outlierCount / totalDataPoints;
        if (outlierRatio > 0.1) { // More than 10% outliers
            baseConfidence *= (1.0 - outlierRatio * 0.5);
        }
        
        return Math.max(0.0, Math.min(1.0, baseConfidence));
    }
    
    /**
     * Check if data quality is high enough for reliable analysis
     */
    public boolean isHighQuality() {
        return overallLevel == QualityLevel.EXCELLENT || overallLevel == QualityLevel.GOOD;
    }
    
    /**
     * Check if data quality is low and should be flagged
     */
    public boolean isLow() {
        return overallLevel == QualityLevel.POOR || overallLevel == QualityLevel.UNACCEPTABLE;
    }
    
    /**
     * Check if data is suitable for alerting
     */
    public boolean isSuitableForAlerting() {
        return isHighQuality() && timeliness >= 0.8 && completeness >= 0.8;
    }
    
    /**
     * Check if data needs quality improvement
     */
    public boolean needsImprovement() {
        return getOverallScore() < 0.75 || completeness < 0.8 || accuracy < 0.8;
    }
    
    /**
     * Get the most problematic quality dimension
     */
    public String getMostProblematicDimension() {
        double[] scores = {completeness, accuracy, consistency, timeliness, validity};
        String[] dimensions = {"Completeness", "Accuracy", "Consistency", "Timeliness", "Validity"};
        
        int minIndex = 0;
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] < scores[minIndex]) {
                minIndex = i;
            }
        }
        
        return dimensions[minIndex];
    }
    
    /**
     * Get quality issues that need attention
     */
    public java.util.List<String> getQualityIssues() {
        java.util.List<String> issues = new java.util.ArrayList<>();
        
        if (completeness < 0.8) {
            issues.add(String.format("Low completeness: %.1f%%", completeness * 100));
        }
        if (accuracy < 0.8) {
            issues.add(String.format("Low accuracy: %.1f%%", accuracy * 100));
        }
        if (consistency < 0.7) {
            issues.add(String.format("Inconsistent with historical patterns: %.1f%%", consistency * 100));
        }
        if (timeliness < 0.8) {
            issues.add(String.format("Data not timely: %.1f%%", timeliness * 100));
        }
        if (validity < 0.8) {
            issues.add(String.format("Invalid data detected: %.1f%%", validity * 100));
        }
        
        double outlierPercentage = totalDataPoints > 0 ? (outlierCount / (double) totalDataPoints) * 100 : 0;
        if (outlierPercentage > 10) {
            issues.add(String.format("High outlier rate: %.1f%%", outlierPercentage));
        }
        
        if (totalDataPoints < 10) {
            issues.add("Insufficient data points for reliable analysis");
        }
        
        return issues;
    }
    
    /**
     * Create updated data quality with new data point
     */
    public DataQuality withNewDataPoint() {
        return withNewDataPoint(true, false);
    }
    
    /**
     * Create updated data quality with new data point
     */
    public DataQuality withNewDataPoint(boolean isValid, boolean isOutlier) {
        int newTotal = totalDataPoints + 1;
        int newValid = validDataPoints + (isValid ? 1 : 0);
        int newOutliers = outlierCount + (isOutlier ? 1 : 0);
        
        // Recalculate completeness and validity based on new data
        double newCompleteness = (double) newValid / newTotal;
        double newValidity = isValid ? Math.min(1.0, validity + 0.01) : Math.max(0.0, validity - 0.05);
        
        return new DataQuality(newCompleteness, accuracy, consistency, timeliness, 
                             newValidity, newTotal, newValid, newOutliers, LocalDateTime.now());
    }
    
    /**
     * Create quality assessment after time passage
     */
    public DataQuality withTimeAging(LocalDateTime currentTime) {
        long minutesSinceUpdate = java.time.temporal.ChronoUnit.MINUTES.between(lastUpdated, currentTime);
        
        // Decrease timeliness based on age
        double newTimeliness = timeliness;
        if (minutesSinceUpdate > 60) { // After 1 hour, start decreasing
            double agingFactor = Math.max(0.1, 1.0 - (minutesSinceUpdate - 60) / 1440.0); // Decay over 24 hours
            newTimeliness = timeliness * agingFactor;
        }
        
        return new DataQuality(completeness, accuracy, consistency, newTimeliness, 
                             validity, totalDataPoints, validDataPoints, outlierCount, currentTime);
    }
    
    /**
     * Combine with another data quality assessment (for merged datasets)
     */
    public DataQuality combineWith(DataQuality other) {
        int combinedTotal = this.totalDataPoints + other.totalDataPoints;
        int combinedValid = this.validDataPoints + other.validDataPoints;
        int combinedOutliers = this.outlierCount + other.outlierCount;
        
        // Weighted average based on data point counts
        double thisWeight = (double) this.totalDataPoints / combinedTotal;
        double otherWeight = (double) other.totalDataPoints / combinedTotal;
        
        double combinedCompleteness = (this.completeness * thisWeight) + (other.completeness * otherWeight);
        double combinedAccuracy = (this.accuracy * thisWeight) + (other.accuracy * otherWeight);
        double combinedConsistency = (this.consistency * thisWeight) + (other.consistency * otherWeight);
        double combinedTimeliness = Math.min(this.timeliness, other.timeliness); // Take worst timeliness
        double combinedValidity = (this.validity * thisWeight) + (other.validity * otherWeight);
        
        LocalDateTime mostRecent = this.lastUpdated.isAfter(other.lastUpdated) ? 
                                  this.lastUpdated : other.lastUpdated;
        
        return new DataQuality(combinedCompleteness, combinedAccuracy, combinedConsistency,
                             combinedTimeliness, combinedValidity, combinedTotal, combinedValid,
                             combinedOutliers, mostRecent);
    }
    
    // Getters
    public double getCompleteness() { return completeness; }
    public double getAccuracy() { return accuracy; }
    public double getConsistency() { return consistency; }
    public double getTimeliness() { return timeliness; }
    public double getValidity() { return validity; }
    public int getTotalDataPoints() { return totalDataPoints; }
    public int getValidDataPoints() { return validDataPoints; }
    public int getOutlierCount() { return outlierCount; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public QualityLevel getOverallLevel() { return overallLevel; }
    
    public double getOutlierPercentage() {
        return totalDataPoints > 0 ? (outlierCount / (double) totalDataPoints) * 100 : 0.0;
    }
    
    public double getValidDataPercentage() {
        return totalDataPoints > 0 ? (validDataPoints / (double) totalDataPoints) * 100 : 0.0;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataQuality that = (DataQuality) o;
        return Double.compare(that.completeness, completeness) == 0 &&
               Double.compare(that.accuracy, accuracy) == 0 &&
               Double.compare(that.consistency, consistency) == 0 &&
               Double.compare(that.timeliness, timeliness) == 0 &&
               Double.compare(that.validity, validity) == 0 &&
               totalDataPoints == that.totalDataPoints &&
               validDataPoints == that.validDataPoints &&
               outlierCount == that.outlierCount;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(completeness, accuracy, consistency, timeliness, validity,
                          totalDataPoints, validDataPoints, outlierCount);
    }
    
    @Override
    public String toString() {
        return String.format("DataQuality{%s, score=%.2f, confidence=%.2f}", 
                           overallLevel, getOverallScore(), getConfidenceLevel());
    }
    
    /**
     * Quality level enumeration
     */
    public enum QualityLevel {
        EXCELLENT("Excellent", "Data is highly reliable and suitable for all uses"),
        GOOD("Good", "Data is reliable and suitable for most uses"),
        FAIR("Fair", "Data has some quality issues but may be usable with caution"),
        POOR("Poor", "Data has significant quality issues and should be used carefully"),
        UNACCEPTABLE("Unacceptable", "Data quality is too poor for reliable use");
        
        private final String displayName;
        private final String description;
        
        QualityLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        
        public boolean isSufficientFor(UsageType usage) {
            switch (usage) {
                case CRITICAL_ALERTING:
                    return this == EXCELLENT;
                case BUSINESS_REPORTING:
                    return this == EXCELLENT || this == GOOD;
                case TREND_ANALYSIS:
                    return this != UNACCEPTABLE;
                case EXPLORATORY_ANALYSIS:
                    return true; // Any quality level can provide insights
                default:
                    return this == EXCELLENT || this == GOOD;
            }
        }
    }
    
    /**
     * Usage type for quality assessment
     */
    public enum UsageType {
        CRITICAL_ALERTING,
        BUSINESS_REPORTING,
        TREND_ANALYSIS,
        EXPLORATORY_ANALYSIS
    }
}