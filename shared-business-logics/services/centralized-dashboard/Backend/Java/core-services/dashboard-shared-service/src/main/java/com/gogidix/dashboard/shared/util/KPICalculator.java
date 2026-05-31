package com.gogidix.dashboard.shared.util;

import com.gogidix.dashboard.shared.constants.DashboardConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class for KPI calculations.
 * Provides common aggregation functions for KPI data processing.
 */
@Slf4j
public class KPICalculator {

    /**
     * Calculate sum of values
     */
    public static Double sum(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        return values.stream()
                .mapToDouble(Number::doubleValue)
                .sum();
    }

    /**
     * Calculate average of values
     */
    public static Double average(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        return values.stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Find minimum value
     */
    public static Double min(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        return values.stream()
                .mapToDouble(Number::doubleValue)
                .min()
                .orElse(0.0);
    }

    /**
     * Find maximum value
     */
    public static Double max(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        return values.stream()
                .mapToDouble(Number::doubleValue)
                .max()
                .orElse(0.0);
    }

    /**
     * Count values
     */
    public static Long count(List<?> values) {
        if (values == null) {
            return 0L;
        }
        return (long) values.size();
    }

    /**
     * Count distinct values
     */
    public static Long distinctCount(List<?> values) {
        if (values == null || values.isEmpty()) {
            return 0L;
        }
        return values.stream()
                .distinct()
                .count();
    }

    /**
     * Calculate percentage change between two values
     */
    public static Double percentageChange(Double oldValue, Double newValue) {
        if (oldValue == null || newValue == null || oldValue == 0) {
            return null;
        }
        return ((newValue - oldValue) / oldValue) * 100;
    }

    /**
     * Calculate value as percentage of target
     */
    public static Double percentageOfTarget(Double value, Double target) {
        if (value == null || target == null || target == 0) {
            return null;
        }
        return (value / target) * 100;
    }

    /**
     * Apply aggregation based on type
     */
    public static Double aggregate(String aggregationType, List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        switch (aggregationType.toUpperCase()) {
            case DashboardConstants.AGG_SUM:
                return sum(values);
            case DashboardConstants.AGG_AVG:
                return average(values);
            case DashboardConstants.AGG_MIN:
                return min(values);
            case DashboardConstants.AGG_MAX:
                return max(values);
            case DashboardConstants.AGG_COUNT:
                return (double) count(values);
            case DashboardConstants.AGG_DISTINCT_COUNT:
                return (double) distinctCount(values);
            default:
                log.warn("Unknown aggregation type: {}, defaulting to SUM", aggregationType);
                return sum(values);
        }
    }

    /**
     * Calculate trend based on current and previous values
     */
    public static String calculateTrend(Double currentValue, Double previousValue) {
        if (currentValue == null || previousValue == null) {
            return "UNKNOWN";
        }
        if (currentValue > previousValue) {
            return "UP";
        } else if (currentValue < previousValue) {
            return "DOWN";
        } else {
            return "STABLE";
        }
    }

    /**
     * Group values by key and aggregate
     */
    public static Map<String, Double> groupAndAggregate(
            Map<String, List<? extends Number>> groupedValues,
            String aggregationType) {
        return groupedValues.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> aggregate(aggregationType, e.getValue())
                ));
    }

    /**
     * Calculate moving average
     */
    public static List<Double> movingAverage(List<Double> values, int windowSize) {
        if (values == null || values.isEmpty() || windowSize <= 0) {
            return List.of();
        }

        return IntStream.range(0, values.size())
                .mapToObj(i -> {
                    int start = Math.max(0, i - windowSize + 1);
                    return values.subList(start, i + 1);
                })
                .map(KPICalculator::average)
                .collect(Collectors.toList());
    }

    /**
     * Calculate compound annual growth rate (CAGR)
     */
    public static Double calculateCAGR(Double startValue, Double endValue, int periods) {
        if (startValue == null || endValue == null || startValue <= 0 || periods <= 0) {
            return null;
        }
        return (Math.pow(endValue / startValue, 1.0 / periods) - 1) * 100;
    }
}
