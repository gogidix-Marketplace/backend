package com.gogidix.finance.exchangerate.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object - Rate Value
 * Represents an exchange rate with precision and validation
 * Immutable value object following DDD principles
 */
public final class RateValue {

    private static final int SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final BigDecimal value;
    private final BigDecimal inverse;
    private final int scale;

    private RateValue(BigDecimal value, int scale) {
        if (value == null) {
            throw new IllegalArgumentException("Rate value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate value must be positive");
        }
        this.value = value.setScale(scale, ROUNDING_MODE);
        this.inverse = BigDecimal.ONE.divide(this.value, scale, ROUNDING_MODE);
        this.scale = scale;
    }

    public static RateValue of(BigDecimal value) {
        return new RateValue(value, SCALE);
    }

    public static RateValue of(BigDecimal value, int scale) {
        return new RateValue(value, scale);
    }

    public static RateValue of(String value) {
        return new RateValue(new BigDecimal(value), SCALE);
    }

    public static RateValue of(double value) {
        return new RateValue(BigDecimal.valueOf(value), SCALE);
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal getInverse() {
        return inverse;
    }

    public int getScale() {
        return scale;
    }

    public RateValue withScale(int newScale) {
        return new RateValue(this.value, newScale);
    }

    public BigDecimal convert(BigDecimal amount, boolean isInvert) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        BigDecimal rate = isInvert ? inverse : value;
        return amount.multiply(rate).setScale(scale, ROUNDING_MODE);
    }

    public RateValue multiply(BigDecimal factor) {
        if (factor == null || factor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Factor must be positive");
        }
        return new RateValue(this.value.multiply(factor), scale);
    }

    public RateValue divide(BigDecimal divisor) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Divisor must be positive");
        }
        return new RateValue(this.value.divide(divisor, scale, ROUNDING_MODE), scale);
    }

    public RateValue add(RateValue other) {
        return new RateValue(this.value.add(other.value), Math.max(this.scale, other.scale));
    }

    public RateValue subtract(RateValue other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Result would be non-positive");
        }
        return new RateValue(result, Math.max(this.scale, other.scale));
    }

    public boolean isGreaterThan(RateValue other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(RateValue other) {
        return this.value.compareTo(other.value) < 0;
    }

    public BigDecimal percentageChange(RateValue oldValue) {
        if (oldValue == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal change = this.value.subtract(oldValue.value);
        return change.divide(oldValue.value, 6, RoundingMode.HALF_UP)
                     .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateValue rateValue = (RateValue) o;
        return Objects.equals(value, rateValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.stripTrailingZeros().toPlainString();
    }

    public String toFormattedString(int decimals) {
        return value.setScale(decimals, RoundingMode.HALF_UP).toPlainString();
    }
}
