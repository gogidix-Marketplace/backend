package com.gogidix.aiservices.aifrauddetectionservice.shared.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Utility class for metric calculations and score normalization.
 * <p>
 * Provides methods for calculating fraud scores, normalizing values,
 * and performing common metric operations.
 */
@Slf4j
@UtilityClass
public class MetricUtil {

    /**
     * Default scale for BigDecimal calculations.
     */
    private static final int DEFAULT_SCALE = 6;

    /**
     * Scale for percentage calculations.
     */
    private static final int PERCENTAGE_SCALE = 2;

    /**
     * Calculates a weighted fraud score from multiple risk factors.
     *
     * @param riskFactors array of risk factor values (0.0 to 1.0)
     * @param weights     array of corresponding weights
     * @return the calculated fraud score (0.0 to 100.0)
     */
    public double calculateWeightedScore(double[] riskFactors, double[] weights) {
        if (riskFactors == null || weights == null || riskFactors.length != weights.length) {
            return 0.0;
        }

        double totalWeight = 0.0;
        double weightedSum = 0.0;

        for (int i = 0; i < riskFactors.length; i++) {
            weightedSum += riskFactors[i] * weights[i];
            totalWeight += weights[i];
        }

        if (totalWeight == 0.0) {
            return 0.0;
        }

        return (weightedSum / totalWeight) * 100.0;
    }

    /**
     * Normalizes a value to a 0-1 range using min-max normalization.
     *
     * @param value the value to normalize
     * @param min   the minimum value of the range
     * @param max   the maximum value of the range
     * @return the normalized value (0.0 to 1.0)
     */
    public double normalize(double value, double min, double max) {
        if (max == min) {
            return 0.0;
        }
        double normalized = (value - min) / (max - min);
        return clamp(normalized, 0.0, 1.0);
    }

    /**
     * Normalizes a value to a specified range.
     *
     * @param value        the value to normalize
     * @param min          the minimum value of the range
     * @param max          the maximum value of the range
     * @param targetMin    the target minimum
     * @param targetMax    the target maximum
     * @return the normalized value
     */
    public double normalizeToRange(double value, double min, double max, double targetMin, double targetMax) {
        double normalized = normalize(value, min, max);
        return targetMin + normalized * (targetMax - targetMin);
    }

    /**
     * Converts a score to a risk level category.
     *
     * @param score the fraud score (0.0 to 100.0)
     * @return the risk level as a string (LOW, MEDIUM, HIGH, CRITICAL)
     */
    public String scoreToRiskLevel(double score) {
        if (score < 30) {
            return "LOW";
        } else if (score < 60) {
            return "MEDIUM";
        } else if (score < 80) {
            return "HIGH";
        } else {
            return "CRITICAL";
        }
    }

    /**
     * Converts a score to a numeric risk level.
     *
     * @param score the fraud score (0.0 to 100.0)
     * @return the numeric risk level (1=LOW, 2=MEDIUM, 3=HIGH, 4=CRITICAL)
     */
    public int scoreToRiskLevelInt(double score) {
        if (score < 30) {
            return 1;
        } else if (score < 60) {
            return 2;
        } else if (score < 80) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * Clamps a value between a minimum and maximum.
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value
     */
    public double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Calculates the percentage change between two values.
     *
     * @param oldValue the original value
     * @param newValue the new value
     * @return the percentage change
     */
    public double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0.0) {
            return newValue == 0.0 ? 0.0 : 100.0;
        }
        return ((newValue - oldValue) / oldValue) * 100.0;
    }

    /**
     * Calculates the percentile rank of a value in a dataset.
     *
     * @param value   the value
     * @param dataset the dataset
     * @return the percentile rank (0.0 to 100.0)
     */
    public double calculatePercentile(double value, List<Double> dataset) {
        if (dataset == null || dataset.isEmpty()) {
            return 0.0;
        }

        long countLessThan = dataset.stream().filter(d -> d < value).count();
        long countEqual = dataset.stream().filter(d -> d == value).count();

        return ((countLessThan + 0.5 * countEqual) / dataset.size()) * 100.0;
    }

    /**
     * Calculates the z-score (standard score) of a value.
     *
     * @param value    the value
     * @param mean     the mean of the dataset
     * @param stdDev   the standard deviation of the dataset
     * @return the z-score
     */
    public double calculateZScore(double value, double mean, double stdDev) {
        if (stdDev == 0.0) {
            return 0.0;
        }
        return (value - mean) / stdDev;
    }

    /**
     * Applies sigmoid function to a value.
     *
     * @param x the input value
     * @return the sigmoid result (0.0 to 1.0)
     */
    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * Calculates a moving average.
     *
     * @param values    the list of values
     * @param windowSize the window size
     * @return the moving average
     */
    public double calculateMovingAverage(List<Double> values, int windowSize) {
        if (values == null || values.isEmpty() || windowSize <= 0) {
            return 0.0;
        }

        int size = Math.min(windowSize, values.size());
        double sum = 0.0;
        int start = Math.max(0, values.size() - size);

        for (int i = start; i < values.size(); i++) {
            sum += values.get(i);
        }

        return sum / size;
    }

    /**
     * Calculates the exponential moving average.
     *
     * @param currentEma the current EMA value
     * @param newValue   the new value
     * @param alpha      the smoothing factor (0.0 to 1.0)
     * @return the new EMA value
     */
    public double calculateEma(double currentEma, double newValue, double alpha) {
        return alpha * newValue + (1 - alpha) * currentEma;
    }

    /**
     * Rounds a decimal value to a specified scale.
     *
     * @param value the value to round
     * @param scale the number of decimal places
     * @return the rounded value
     */
    public double round(double value, int scale) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return value;
        }
        return BigDecimal.valueOf(value)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Rounds a decimal value to the default scale.
     *
     * @param value the value to round
     * @return the rounded value
     */
    public double round(double value) {
        return round(value, DEFAULT_SCALE);
    }

    /**
     * Converts a raw score to a probability using logistic function.
     *
     * @param score the raw score
     * @param scalingFactor the scaling factor (higher makes the curve steeper)
     * @return the probability (0.0 to 1.0)
     */
    public double scoreToProbability(double score, double scalingFactor) {
        return sigmoid((score - 50.0) / scalingFactor);
    }

    /**
     * Calculates the distance between two scores.
     *
     * @param score1 the first score
     * @param score2 the second score
     * @return the absolute difference
     */
    public double scoreDistance(double score1, double score2) {
        return Math.abs(score1 - score2);
    }

    /**
     * Determines if a score change is significant.
     *
     * @param oldScore      the old score
     * @param newScore      the new score
     * @param threshold     the significance threshold
     * @return true if the change is significant, false otherwise
     */
    public boolean isSignificantChange(double oldScore, double newScore, double threshold) {
        return scoreDistance(oldScore, newScore) >= threshold;
    }

    /**
     * Calculates a composite risk score from multiple dimensions.
     *
     * @param velocityScore     the velocity risk score (0-100)
     * @param patternScore      the pattern risk score (0-100)
     * @param amountScore       the amount risk score (0-100)
     * @param behaviorScore     the behavior risk score (0-100)
     * @return the composite risk score (0-100)
     */
    public double calculateCompositeRiskScore(double velocityScore, double patternScore,
                                               double amountScore, double behaviorScore) {
        // Weights can be adjusted based on business requirements
        return calculateWeightedScore(
                new double[]{velocityScore, patternScore, amountScore, behaviorScore},
                new double[]{0.25, 0.30, 0.20, 0.25}
        );
    }

    /**
     * Applies a decay factor to a score over time.
     *
     * @param score     the original score
     * @param timeDelta the time elapsed (in the same unit as halfLife)
     * @param halfLife  the half-life for decay
     * @return the decayed score
     */
    public double applyDecay(double score, double timeDelta, double halfLife) {
        if (halfLife <= 0) {
            return score;
        }
        double decayFactor = Math.exp(-Math.log(2) * timeDelta / halfLife);
        return score * decayFactor;
    }

    /**
     * Calculates a confidence score based on sample size and consistency.
     *
     * @param sampleSize    the number of samples
     * @param consistency   the consistency score (0.0 to 1.0)
     * @return the confidence score (0.0 to 1.0)
     */
    public double calculateConfidence(int sampleSize, double consistency) {
        // Use diminishing returns for sample size
        double sizeFactor = 1.0 - Math.exp(-sampleSize / 100.0);
        return (sizeFactor + consistency) / 2.0;
    }
}
