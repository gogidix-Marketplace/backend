package com.gogidix.shared.model.domain.model.common;

import lombok.Data;
import lombok.With;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Domain value object representing monetary amounts.
 * Immutable and thread-safe implementation using BigDecimal for precision.
 */
@Data
public final class Money {
    
    public static final Money ZERO = new Money(BigDecimal.ZERO, "USD");
    
    private final BigDecimal amount;
    private final String currencyCode;
    private final Currency currency;
    
    /**
     * Creates a Money instance with the specified amount and currency.
     */
    public Money(BigDecimal amount, String currencyCode) {
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null")
                            .setScale(2, RoundingMode.HALF_UP);
        this.currencyCode = Objects.requireNonNull(currencyCode, "Currency code cannot be null");
        this.currency = Currency.getInstance(currencyCode);
    }
    
    /**
     * Creates a Money instance with double amount.
     */
    public Money(double amount, String currencyCode) {
        this(BigDecimal.valueOf(amount), currencyCode);
    }
    
    /**
     * Creates a Money instance with long amount (cents).
     */
    public Money(long amountInCents, String currencyCode) {
        this(BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100)), currencyCode);
    }
    
    /**
     * Creates a Money instance with string amount.
     */
    public Money(String amount, String currencyCode) {
        this(new BigDecimal(amount), currencyCode);
    }
    
    /**
     * Adds another Money amount.
     */
    public Money add(Money other) {
        validateCurrency(other);
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }
    
    /**
     * Subtracts another Money amount.
     */
    public Money subtract(Money other) {
        validateCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currencyCode);
    }
    
    /**
     * Multiplies by a factor.
     */
    public Money multiply(double factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currencyCode);
    }
    
    /**
     * Multiplies by a BigDecimal factor.
     */
    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor), this.currencyCode);
    }
    
    /**
     * Multiplies by an integer.
     */
    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currencyCode);
    }
    
    /**
     * Divides by a factor.
     */
    public Money divide(double factor) {
        if (factor == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(BigDecimal.valueOf(factor), MathContext.DECIMAL64), this.currencyCode);
    }
    
    /**
     * Divides by a BigDecimal factor.
     */
    public Money divide(BigDecimal factor) {
        if (factor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(factor, MathContext.DECIMAL64), this.currencyCode);
    }
    
    /**
     * Divides by an integer.
     */
    public Money divide(int factor) {
        if (factor == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(BigDecimal.valueOf(factor), MathContext.DECIMAL64), this.currencyCode);
    }
    
    /**
     * Returns the absolute value.
     */
    public Money abs() {
        return new Money(this.amount.abs(), this.currencyCode);
    }
    
    /**
     * Returns the negated value.
     */
    public Money negate() {
        return new Money(this.amount.negate(), this.currencyCode);
    }
    
    /**
     * Checks if the amount is positive.
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Checks if the amount is negative.
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Checks if the amount is zero.
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Checks if this amount is greater than another.
     */
    public boolean isGreaterThan(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * Checks if this amount is greater than or equal to another.
     */
    public boolean isGreaterThanOrEqual(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    /**
     * Checks if this amount is less than another.
     */
    public boolean isLessThan(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }
    
    /**
     * Checks if this amount is less than or equal to another.
     */
    public boolean isLessThanOrEqual(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) <= 0;
    }
    
    /**
     * Compares with another Money amount.
     */
    public int compareTo(Money other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount);
    }
    
    /**
     * Converts to amount in cents (long).
     */
    public long toAmountInCents() {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }
    
    /**
     * Converts to double value.
     */
    public double toDouble() {
        return amount.doubleValue();
    }
    
    /**
     * Converts to another currency (exchange rate would be fetched from external service).
     */
    public Money convertTo(String targetCurrencyCode, BigDecimal exchangeRate) {
        if (this.currencyCode.equals(targetCurrencyCode)) {
            return this;
        }
        BigDecimal convertedAmount = this.amount.multiply(exchangeRate);
        return new Money(convertedAmount, targetCurrencyCode);
    }
    
    /**
     * Applies a percentage.
     */
    public Money applyPercentage(double percentage) {
        return multiply(percentage / 100.0);
    }
    
    /**
     * Calculates percentage of another amount.
     */
    public double percentageOf(Money total) {
        validateCurrency(total);
        if (total.isZero()) {
            return 0.0;
        }
        return this.amount.divide(total.amount, MathContext.DECIMAL64)
                         .multiply(BigDecimal.valueOf(100))
                         .doubleValue();
    }
    
    /**
     * Rounds to the nearest currency unit.
     */
    public Money round() {
        return new Money(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP), currencyCode);
    }
    
    /**
     * Rounds up to the nearest currency unit.
     */
    public Money roundUp() {
        return new Money(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.CEILING), currencyCode);
    }
    
    /**
     * Rounds down to the nearest currency unit.
     */
    public Money roundDown() {
        return new Money(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.FLOOR), currencyCode);
    }
    
    /**
     * Splits the amount into equal parts.
     */
    public Money[] split(int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Parts must be positive");
        }
        
        Money perPart = divide(parts);
        Money[] result = new Money[parts];
        Money remainder = subtract(perPart.multiply(parts));
        
        for (int i = 0; i < parts; i++) {
            result[i] = perPart;
        }
        
        // Distribute remainder to first few parts
        if (!remainder.isZero()) {
            Money oneUnit = new Money(BigDecimal.ONE.divide(BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits()))), currencyCode);
            int remainderCents = (int) remainder.toAmountInCents();
            for (int i = 0; i < Math.abs(remainderCents) && i < parts; i++) {
                result[i] = remainderCents > 0 ? result[i].add(oneUnit) : result[i].subtract(oneUnit);
            }
        }
        
        return result;
    }
    
    /**
     * Creates a Money from major currency units (e.g., dollars).
     */
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount, currencyCode);
    }
    
    /**
     * Creates a Money from major currency units (double).
     */
    public static Money of(double amount, String currencyCode) {
        return new Money(amount, currencyCode);
    }
    
    /**
     * Creates a Money from minor currency units (e.g., cents).
     */
    public static Money ofMinor(long amountInMinorUnits, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        BigDecimal majorAmount = BigDecimal.valueOf(amountInMinorUnits)
                .divide(BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())));
        return new Money(majorAmount, currencyCode);
    }
    
    /**
     * Sums an array of Money amounts.
     */
    public static Money sum(Money... amounts) {
        if (amounts.length == 0) {
            return ZERO;
        }
        
        Money result = amounts[0];
        for (int i = 1; i < amounts.length; i++) {
            result = result.add(amounts[i]);
        }
        return result;
    }
    
    /**
     * Gets the maximum of two Money amounts.
     */
    public static Money max(Money a, Money b) {
        return a.isGreaterThan(b) ? a : b;
    }
    
    /**
     * Gets the minimum of two Money amounts.
     */
    public static Money min(Money a, Money b) {
        return a.isLessThan(b) ? a : b;
    }
    
    /**
     * Validates that another Money has the same currency.
     */
    private void validateCurrency(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s vs %s", this.currencyCode, other.currencyCode));
        }
    }
    
    /**
     * Formats the money for display.
     */
    public String format() {
        return String.format("%s %.2f", currency.getSymbol(), amount);
    }
    
    /**
     * Formats with explicit currency code.
     */
    public String formatWithCode() {
        return String.format("%.2f %s", amount, currencyCode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Money)) return false;
        Money other = (Money) obj;
        return Objects.equals(amount, other.amount) && 
               Objects.equals(currencyCode, other.currencyCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currencyCode);
    }
    
    @Override
    public String toString() {
        return formatWithCode();
    }
    
    // Explicit getters for reliable compilation without Lombok dependency issues
    public BigDecimal getAmount() {
        return amount;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public Currency getCurrency() {
        return currency;
    }
}