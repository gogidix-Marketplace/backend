package com.gogidix.dashboard.aggregation.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Metric Value Object
 * 
 * Represents a metric value with validation and business logic
 */
public class MetricValue {
    private final BigDecimal numericValue;
    private final String unit;
    private final ValueRange normalRange;
    private final boolean available;
    private final int precision;
    
    private MetricValue(BigDecimal numericValue, String unit, ValueRange normalRange, 
                       boolean available, int precision) {
        this.numericValue = numericValue;
        this.unit = unit;
        this.normalRange = normalRange;
        this.available = available;
        this.precision = precision;
    }
    
    public static MetricValue of(Double value, String unit) {
        return of(value, unit, null, 2);
    }
    
    public static MetricValue of(Double value, String unit, ValueRange normalRange, int precision) {
        if (value == null) {
            return unavailable();
        }
        
        validateValue(value);
        BigDecimal bdValue = BigDecimal.valueOf(value).setScale(precision, RoundingMode.HALF_UP);
        return new MetricValue(bdValue, validateUnit(unit), normalRange, true, precision);
    }
    
    public static MetricValue of(Long value, String unit) {
        if (value == null) {
            return unavailable();
        }
        return of(value.doubleValue(), unit, null, 0);
    }
    
    public static MetricValue percentage(Double value) {
        if (value != null && (value < 0 || value > 100)) {
            throw new IllegalArgumentException("Percentage value must be between 0 and 100");
        }
        ValueRange range = new ValueRange(0.0, 100.0);
        return of(value, "%", range, 2);
    }
    
    public static MetricValue count(Long count) {
        if (count != null && count < 0) {
            throw new IllegalArgumentException("Count cannot be negative");
        }
        ValueRange range = new ValueRange(0.0, Double.MAX_VALUE);
        return of(count != null ? count.doubleValue() : null, "count", range, 0);
    }
    
    public static MetricValue currency(Double value, String currencyCode) {
        return of(value, validateCurrencyCode(currencyCode), null, 2);
    }
    
    public static MetricValue duration(Double milliseconds) {
        if (milliseconds != null && milliseconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        return of(milliseconds, "ms", new ValueRange(0.0, Double.MAX_VALUE), 2);
    }
    
    public static MetricValue rate(Double rate, String unit) {
        if (rate != null && rate < 0) {
            throw new IllegalArgumentException("Rate cannot be negative");
        }
        return of(rate, unit + "/s", new ValueRange(0.0, Double.MAX_VALUE), 4);
    }
    
    public static MetricValue unavailable() {
        return new MetricValue(null, null, null, false, 0);
    }
    
    private static void validateValue(Double value) {
        if (value.isInfinite()) {
            throw new IllegalArgumentException("Metric value cannot be infinite");
        }
        if (value.isNaN()) {
            throw new IllegalArgumentException("Metric value cannot be NaN");
        }
    }
    
    private static String validateUnit(String unit) {
        if (unit != null && unit.length() > 20) {
            throw new IllegalArgumentException("Unit cannot exceed 20 characters");
        }
        return unit;
    }
    
    private static String validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || currencyCode.length() != 3) {
            throw new IllegalArgumentException("Currency code must be exactly 3 characters (ISO 4217)");
        }
        return currencyCode.toUpperCase();
    }
    
    /**
     * Check if value is available
     */
    public boolean isAvailable() {
        return available;
    }
    
    /**
     * Check if value is valid (available and finite)
     */
    public boolean isValid() {
        return available && numericValue != null;
    }
    
    /**
     * Get numeric value as double
     */
    public Double getNumericValue() {
        if (!available) {
            throw new IllegalStateException("Metric value is not available");
        }
        return numericValue.doubleValue();
    }
    
    /**
     * Get numeric value as BigDecimal for precise calculations
     */
    public BigDecimal getPreciseValue() {
        if (!available) {
            throw new IllegalStateException("Metric value is not available");
        }
        return numericValue;
    }
    
    /**
     * Get value or default if not available
     */
    public Double getNumericValueOrDefault(Double defaultValue) {
        return available ? numericValue.doubleValue() : defaultValue;
    }
    
    /**
     * Get the unit
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * Check if value is outside normal range
     */
    public boolean isOutsideNormalRange() {
        if (!available || normalRange == null) {
            return false;
        }
        return normalRange.isOutsideRange(numericValue.doubleValue());
    }
    
    /**
     * Check if value is within normal range
     */
    public boolean isWithinNormalRange() {
        if (!available || normalRange == null) {
            return true; // Assume normal if no range defined
        }
        return normalRange.isWithinRange(numericValue.doubleValue());
    }
    
    /**
     * Check if this is zero value
     */
    public boolean isZero() {
        return available && numericValue.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Check if this is positive value
     */
    public boolean isPositive() {
        return available && numericValue.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Check if this is negative value
     */
    public boolean isNegative() {
        return available && numericValue.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Compare with another metric value
     */
    public boolean isGreaterThan(MetricValue other) {
        if (!this.available || !other.available) {
            return false;
        }
        return this.numericValue.compareTo(other.numericValue) > 0;
    }
    
    /**
     * Compare with another metric value
     */
    public boolean isLessThan(MetricValue other) {
        if (!this.available || !other.available) {
            return false;
        }
        return this.numericValue.compareTo(other.numericValue) < 0;
    }
    
    /**
     * Calculate percentage change from another value
     */
    public Double calculatePercentageChange(MetricValue baseline) {
        if (!this.available || !baseline.available || baseline.isZero()) {
            return null;
        }
        
        BigDecimal difference = this.numericValue.subtract(baseline.numericValue);
        BigDecimal percentageChange = difference
            .divide(baseline.numericValue.abs(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
            
        return percentageChange.doubleValue();
    }
    
    /**
     * Add another metric value
     */
    public MetricValue add(MetricValue other) {
        if (!this.available || !other.available) {
            return unavailable();
        }
        
        if (!Objects.equals(this.unit, other.unit)) {
            throw new IllegalArgumentException("Cannot add values with different units");
        }
        
        BigDecimal sum = this.numericValue.add(other.numericValue);
        return new MetricValue(sum, this.unit, this.normalRange, true, this.precision);
    }
    
    /**
     * Subtract another metric value
     */
    public MetricValue subtract(MetricValue other) {
        if (!this.available || !other.available) {
            return unavailable();
        }
        
        if (!Objects.equals(this.unit, other.unit)) {
            throw new IllegalArgumentException("Cannot subtract values with different units");
        }
        
        BigDecimal difference = this.numericValue.subtract(other.numericValue);
        return new MetricValue(difference, this.unit, this.normalRange, true, this.precision);
    }
    
    /**
     * Multiply by a factor
     */
    public MetricValue multiply(double factor) {
        if (!available) {
            return unavailable();
        }
        
        BigDecimal result = numericValue.multiply(BigDecimal.valueOf(factor));
        return new MetricValue(result, unit, normalRange, true, precision);
    }
    
    /**
     * Divide by a factor
     */
    public MetricValue divide(double factor) {
        if (!available || factor == 0) {
            return unavailable();
        }
        
        BigDecimal result = numericValue.divide(BigDecimal.valueOf(factor), precision, RoundingMode.HALF_UP);
        return new MetricValue(result, unit, normalRange, true, precision);
    }
    
    /**
     * Format value for display
     */
    public String formatForDisplay() {
        if (!available) {
            return "N/A";
        }
        
        String formattedValue = numericValue.toPlainString();
        return unit != null ? formattedValue + " " + unit : formattedValue;
    }
    
    /**
     * Get value range distance (how far from normal range)
     */
    public Double getRangeDistance() {
        if (!available || normalRange == null) {
            return null;
        }
        return normalRange.getDistance(numericValue.doubleValue());
    }
    
    // Getters
    public ValueRange getNormalRange() { return normalRange; }
    public int getPrecision() { return precision; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricValue that = (MetricValue) o;
        return available == that.available &&
               precision == that.precision &&
               Objects.equals(numericValue, that.numericValue) &&
               Objects.equals(unit, that.unit);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(numericValue, unit, available, precision);
    }
    
    @Override
    public String toString() {
        return formatForDisplay();
    }
    
    /**
     * Value Range for normal bounds checking
     */
    public static class ValueRange {
        private final Double minValue;
        private final Double maxValue;
        
        public ValueRange(Double minValue, Double maxValue) {
            if (minValue != null && maxValue != null && minValue > maxValue) {
                throw new IllegalArgumentException("Min value cannot be greater than max value");
            }
            this.minValue = minValue;
            this.maxValue = maxValue;
        }
        
        public boolean isWithinRange(Double value) {
            if (value == null) return false;
            if (minValue != null && value < minValue) return false;
            if (maxValue != null && value > maxValue) return false;
            return true;
        }
        
        public boolean isOutsideRange(Double value) {
            return !isWithinRange(value);
        }
        
        public Double getDistance(Double value) {
            if (value == null) return null;
            
            if (minValue != null && value < minValue) {
                return minValue - value;
            }
            if (maxValue != null && value > maxValue) {
                return value - maxValue;
            }
            return 0.0; // Within range
        }
        
        public Double getMinValue() { return minValue; }
        public Double getMaxValue() { return maxValue; }
    }
}