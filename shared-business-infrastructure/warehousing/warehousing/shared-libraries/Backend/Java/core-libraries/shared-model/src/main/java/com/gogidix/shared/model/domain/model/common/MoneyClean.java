package com.gogidix.shared.model.domain.model.common;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;
import java.util.Arrays;
import java.util.List;

/**
 * Pure domain value object representing monetary amounts with zero external dependencies.
 * Immutable and thread-safe implementation using BigDecimal for precision.
 * Contains comprehensive business logic for financial calculations.
 */
public final class MoneyClean implements Comparable<MoneyClean> {
    
    public static final MoneyClean ZERO = new MoneyClean(BigDecimal.ZERO, "USD");
    public static final MoneyClean ONE_USD = new MoneyClean(BigDecimal.ONE, "USD");
    public static final MoneyClean ONE_EUR = new MoneyClean(BigDecimal.ONE, "EUR");
    public static final MoneyClean ONE_GBP = new MoneyClean(BigDecimal.ONE, "GBP");
    
    private final BigDecimal amount;
    private final String currencyCode;
    private final Currency currency;
    
    /**
     * Creates a Money instance with the specified amount and currency.
     */
    public MoneyClean(BigDecimal amount, String currencyCode) {
        this.currencyCode = validateCurrencyCode(currencyCode);
        this.currency = Currency.getInstance(this.currencyCode);
        this.amount = validateAndScaleAmount(amount);
    }
    
    /**
     * Creates a Money instance with double amount.
     */
    public MoneyClean(double amount, String currencyCode) {
        this(BigDecimal.valueOf(amount), currencyCode);
    }
    
    /**
     * Creates a Money instance with long amount (cents).
     */
    public MoneyClean(long amountInCents, String currencyCode) {
        this(BigDecimal.valueOf(amountInCents).divide(BigDecimal.valueOf(100)), currencyCode);
    }
    
    /**
     * Creates a Money instance with string amount.
     */
    public MoneyClean(String amount, String currencyCode) {
        this(new BigDecimal(amount), currencyCode);
    }
    
    // ==============================================
    // MATHEMATICAL OPERATIONS - Business Logic
    // ==============================================
    
    /**
     * Business Logic: Adds another Money amount with currency validation
     */
    public MoneyClean add(MoneyClean other) {
        validateCurrency(other);
        return new MoneyClean(this.amount.add(other.amount), this.currencyCode);
    }
    
    /**
     * Business Logic: Subtracts another Money amount with currency validation
     */
    public MoneyClean subtract(MoneyClean other) {
        validateCurrency(other);
        return new MoneyClean(this.amount.subtract(other.amount), this.currencyCode);
    }
    
    /**
     * Business Logic: Multiplies by a factor with precision handling
     */
    public MoneyClean multiply(double factor) {
        validateMultiplier(factor);
        return new MoneyClean(this.amount.multiply(BigDecimal.valueOf(factor)), this.currencyCode);
    }
    
    /**
     * Business Logic: Multiplies by a BigDecimal factor
     */
    public MoneyClean multiply(BigDecimal factor) {
        validateBigDecimalMultiplier(factor);
        return new MoneyClean(this.amount.multiply(factor), this.currencyCode);
    }
    
    /**
     * Business Logic: Multiplies by an integer
     */
    public MoneyClean multiply(int factor) {
        return new MoneyClean(this.amount.multiply(BigDecimal.valueOf(factor)), this.currencyCode);
    }
    
    /**
     * Business Logic: Divides by a factor with zero division protection
     */
    public MoneyClean divide(double factor) {
        validateDivisor(factor);
        return new MoneyClean(this.amount.divide(BigDecimal.valueOf(factor), MathContext.DECIMAL64), this.currencyCode);
    }
    
    /**
     * Business Logic: Divides by a BigDecimal factor
     */
    public MoneyClean divide(BigDecimal factor) {
        validateBigDecimalDivisor(factor);
        return new MoneyClean(this.amount.divide(factor, MathContext.DECIMAL64), this.currencyCode);
    }
    
    /**
     * Business Logic: Divides by an integer
     */
    public MoneyClean divide(int factor) {
        validateIntegerDivisor(factor);
        return new MoneyClean(this.amount.divide(BigDecimal.valueOf(factor), MathContext.DECIMAL64), this.currencyCode);
    }
    
    // ==============================================
    // FINANCIAL BUSINESS RULES
    // ==============================================
    
    /**
     * Business Rule: Returns the absolute value
     */
    public MoneyClean abs() {
        return new MoneyClean(this.amount.abs(), this.currencyCode);
    }
    
    /**
     * Business Rule: Returns the negated value
     */
    public MoneyClean negate() {
        return new MoneyClean(this.amount.negate(), this.currencyCode);
    }
    
    /**
     * Business Rule: Checks if the amount is positive
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Business Rule: Checks if the amount is negative
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Business Rule: Checks if the amount is zero
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Business Rule: Checks if this amount is greater than another
     */
    public boolean isGreaterThan(MoneyClean other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * Business Rule: Checks if this amount is greater than or equal to another
     */
    public boolean isGreaterThanOrEqual(MoneyClean other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) >= 0;
    }
    
    /**
     * Business Rule: Checks if this amount is less than another
     */
    public boolean isLessThan(MoneyClean other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }
    
    /**
     * Business Rule: Checks if this amount is less than or equal to another
     */
    public boolean isLessThanOrEqual(MoneyClean other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount) <= 0;
    }
    
    /**
     * Business Rule: Checks if amount is within a range
     */
    public boolean isBetween(MoneyClean min, MoneyClean max) {
        validateCurrency(min);
        validateCurrency(max);
        return this.isGreaterThanOrEqual(min) && this.isLessThanOrEqual(max);
    }
    
    /**
     * Business Rule: Checks if this is a significant amount (more than currency's smallest unit)
     */
    public boolean isSignificant() {
        BigDecimal smallestUnit = BigDecimal.ONE.divide(
            BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())));
        return amount.abs().compareTo(smallestUnit) >= 0;
    }
    
    /**
     * Business Rule: Checks if this is a round amount (no fractional part)
     */
    public boolean isRoundAmount() {
        return amount.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }
    
    // ==============================================
    // CURRENCY CONVERSION AND EXCHANGE
    // ==============================================
    
    /**
     * Business Logic: Converts to another currency with exchange rate validation
     */
    public MoneyClean convertTo(String targetCurrencyCode, BigDecimal exchangeRate) {
        if (this.currencyCode.equals(validateCurrencyCode(targetCurrencyCode))) {
            return this;
        }
        validateExchangeRate(exchangeRate);
        BigDecimal convertedAmount = this.amount.multiply(exchangeRate);
        return new MoneyClean(convertedAmount, targetCurrencyCode);
    }
    
    /**
     * Business Logic: Applies a percentage with validation
     */
    public MoneyClean applyPercentage(double percentage) {
        validatePercentage(percentage);
        return multiply(percentage / 100.0);
    }
    
    /**
     * Business Logic: Applies a discount percentage
     */
    public MoneyClean applyDiscount(double discountPercentage) {
        validatePercentage(discountPercentage);
        if (discountPercentage > 100.0) {
            throw new IllegalArgumentException("Discount percentage cannot exceed 100%");
        }
        return multiply((100.0 - discountPercentage) / 100.0);
    }
    
    /**
     * Business Logic: Applies tax rate
     */
    public MoneyClean applyTax(double taxRate) {
        validateTaxRate(taxRate);
        return multiply(1.0 + (taxRate / 100.0));
    }
    
    /**
     * Business Logic: Calculates percentage of another amount
     */
    public double percentageOf(MoneyClean total) {
        validateCurrency(total);
        if (total.isZero()) {
            return 0.0;
        }
        return this.amount.divide(total.amount, MathContext.DECIMAL64)
                         .multiply(BigDecimal.valueOf(100))
                         .doubleValue();
    }
    
    /**
     * Business Logic: Calculates the difference as percentage
     */
    public double percentageDifference(MoneyClean other) {
        validateCurrency(other);
        if (other.isZero()) {
            throw new IllegalArgumentException("Cannot calculate percentage difference from zero");
        }
        return this.subtract(other).percentageOf(other.abs());
    }
    
    // ==============================================
    // ROUNDING AND PRECISION BUSINESS RULES
    // ==============================================
    
    /**
     * Business Rule: Rounds to the nearest currency unit
     */
    public MoneyClean round() {
        return new MoneyClean(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP), currencyCode);
    }
    
    /**
     * Business Rule: Rounds up to the nearest currency unit
     */
    public MoneyClean roundUp() {
        return new MoneyClean(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.CEILING), currencyCode);
    }
    
    /**
     * Business Rule: Rounds down to the nearest currency unit
     */
    public MoneyClean roundDown() {
        return new MoneyClean(amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.FLOOR), currencyCode);
    }
    
    /**
     * Business Rule: Rounds to nearest specified unit (e.g., 0.05 for nickel rounding)
     */
    public MoneyClean roundToNearest(BigDecimal unit) {
        validateRoundingUnit(unit);
        BigDecimal factor = BigDecimal.ONE.divide(unit, MathContext.DECIMAL64);
        BigDecimal rounded = amount.multiply(factor).setScale(0, RoundingMode.HALF_UP).divide(factor, MathContext.DECIMAL64);
        return new MoneyClean(rounded, currencyCode);
    }
    
    // ==============================================
    // SPLITTING AND ALLOCATION BUSINESS LOGIC
    // ==============================================
    
    /**
     * Business Logic: Splits the amount into equal parts with remainder distribution
     */
    public MoneyClean[] split(int parts) {
        validateSplitParts(parts);
        
        MoneyClean perPart = divide(parts);
        MoneyClean[] result = new MoneyClean[parts];
        MoneyClean totalAllocated = perPart.multiply(parts);
        MoneyClean remainder = subtract(totalAllocated);
        
        // Initialize all parts with base amount
        Arrays.fill(result, perPart);
        
        // Distribute remainder to first few parts
        if (!remainder.isZero()) {
            MoneyClean oneUnit = getSmallestUnit();
            long remainderUnits = remainder.toAmountInMinorUnits();
            
            for (int i = 0; i < Math.abs(remainderUnits) && i < parts; i++) {
                result[i] = remainderUnits > 0 ? result[i].add(oneUnit) : result[i].subtract(oneUnit);
            }
        }
        
        return result;
    }
    
    /**
     * Business Logic: Allocates amount based on ratios
     */
    public MoneyClean[] allocate(double... ratios) {
        validateAllocationRatios(ratios);
        
        double totalRatio = Arrays.stream(ratios).sum();
        if (totalRatio == 0.0) {
            throw new IllegalArgumentException("Total ratio cannot be zero");
        }
        
        MoneyClean[] result = new MoneyClean[ratios.length];
        MoneyClean allocated = ZERO.withCurrency(currencyCode);
        
        // Allocate based on ratios
        for (int i = 0; i < ratios.length - 1; i++) {
            result[i] = multiply(ratios[i] / totalRatio).round();
            allocated = allocated.add(result[i]);
        }
        
        // Last allocation gets remainder to ensure exact total
        result[ratios.length - 1] = subtract(allocated);
        
        return result;
    }
    
    /**
     * Business Logic: Allocates amount proportionally based on weights
     */
    public MoneyClean[] allocateProportionally(MoneyClean... targets) {
        validateAllocationTargets(targets);
        
        MoneyClean totalTarget = sum(targets);
        if (totalTarget.isZero()) {
            throw new IllegalArgumentException("Total target amount cannot be zero");
        }
        
        MoneyClean[] result = new MoneyClean[targets.length];
        MoneyClean allocated = ZERO.withCurrency(currencyCode);
        
        // Allocate proportionally
        for (int i = 0; i < targets.length - 1; i++) {
            result[i] = multiply(targets[i].percentageOf(totalTarget) / 100.0).round();
            allocated = allocated.add(result[i]);
        }
        
        // Last allocation gets remainder
        result[targets.length - 1] = subtract(allocated);
        
        return result;
    }
    
    // ==============================================
    // UTILITY AND CONVERSION METHODS
    // ==============================================
    
    /**
     * Converts to amount in minor units (e.g., cents)
     */
    public long toAmountInMinorUnits() {
        return amount.multiply(BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())))
                    .setScale(0, RoundingMode.HALF_UP)
                    .longValue();
    }
    
    /**
     * Converts to double value (use with caution for precision)
     */
    public double toDouble() {
        return amount.doubleValue();
    }
    
    /**
     * Gets the smallest unit for this currency
     */
    public MoneyClean getSmallestUnit() {
        BigDecimal smallestAmount = BigDecimal.ONE.divide(
            BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())));
        return new MoneyClean(smallestAmount, currencyCode);
    }
    
    /**
     * Creates a new Money with the same amount but different currency
     */
    public MoneyClean withCurrency(String newCurrencyCode) {
        return new MoneyClean(this.amount, newCurrencyCode);
    }
    
    /**
     * Creates a new Money with the same currency but different amount
     */
    public MoneyClean withAmount(BigDecimal newAmount) {
        return new MoneyClean(newAmount, this.currencyCode);
    }
    
    /**
     * Creates a new Money with the same currency but different amount
     */
    public MoneyClean withAmount(double newAmount) {
        return new MoneyClean(newAmount, this.currencyCode);
    }
    
    // ==============================================
    // STATIC FACTORY METHODS
    // ==============================================
    
    /**
     * Creates a Money from major currency units (e.g., dollars)
     */
    public static MoneyClean of(BigDecimal amount, String currencyCode) {
        return new MoneyClean(amount, currencyCode);
    }
    
    /**
     * Creates a Money from major currency units (double)
     */
    public static MoneyClean of(double amount, String currencyCode) {
        return new MoneyClean(amount, currencyCode);
    }
    
    /**
     * Creates a Money from string representation
     */
    public static MoneyClean parse(String amountString, String currencyCode) {
        try {
            return new MoneyClean(new BigDecimal(amountString.trim()), currencyCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + amountString, e);
        }
    }
    
    /**
     * Creates a Money from minor currency units (e.g., cents)
     */
    public static MoneyClean ofMinor(long amountInMinorUnits, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        BigDecimal majorAmount = BigDecimal.valueOf(amountInMinorUnits)
                .divide(BigDecimal.valueOf(Math.pow(10, currency.getDefaultFractionDigits())));
        return new MoneyClean(majorAmount, currencyCode);
    }
    
    /**
     * Sums an array of Money amounts
     */
    public static MoneyClean sum(MoneyClean... amounts) {
        if (amounts.length == 0) {
            return ZERO;
        }
        
        MoneyClean result = amounts[0];
        for (int i = 1; i < amounts.length; i++) {
            result = result.add(amounts[i]);
        }
        return result;
    }
    
    /**
     * Sums a list of Money amounts
     */
    public static MoneyClean sum(List<MoneyClean> amounts) {
        return sum(amounts.toArray(new MoneyClean[0]));
    }
    
    /**
     * Gets the maximum of two Money amounts
     */
    public static MoneyClean max(MoneyClean a, MoneyClean b) {
        return a.isGreaterThan(b) ? a : b;
    }
    
    /**
     * Gets the minimum of two Money amounts
     */
    public static MoneyClean min(MoneyClean a, MoneyClean b) {
        return a.isLessThan(b) ? a : b;
    }
    
    /**
     * Creates zero amount for specified currency
     */
    public static MoneyClean zero(String currencyCode) {
        return new MoneyClean(BigDecimal.ZERO, currencyCode);
    }
    
    // ==============================================
    // DOMAIN VALIDATION METHODS
    // ==============================================
    
    private String validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || currencyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be null or empty");
        }
        String trimmed = currencyCode.trim().toUpperCase();
        if (trimmed.length() != 3) {
            throw new IllegalArgumentException("Currency code must be exactly 3 characters");
        }
        // This will throw if currency is invalid
        try {
            Currency.getInstance(trimmed);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + trimmed, e);
        }
        return trimmed;
    }
    
    private BigDecimal validateAndScaleAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        
        // Check for extreme values
        if (amount.compareTo(new BigDecimal("999999999999999.99")) > 0) {
            throw new IllegalArgumentException("Amount too large");
        }
        if (amount.compareTo(new BigDecimal("-999999999999999.99")) < 0) {
            throw new IllegalArgumentException("Amount too small");
        }
        
        return amount.setScale(currency.getDefaultFractionDigits(), RoundingMode.HALF_UP);
    }
    
    private void validateCurrency(MoneyClean other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException(
                String.format("Currency mismatch: %s vs %s", this.currencyCode, other.currencyCode));
        }
    }
    
    private void validateMultiplier(double factor) {
        if (Double.isNaN(factor) || Double.isInfinite(factor)) {
            throw new IllegalArgumentException("Multiplier cannot be NaN or infinite");
        }
    }
    
    private void validateBigDecimalMultiplier(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Multiplier cannot be null");
        }
    }
    
    private void validateDivisor(double factor) {
        if (factor == 0.0 || Double.isNaN(factor) || Double.isInfinite(factor)) {
            throw new IllegalArgumentException("Cannot divide by zero, NaN, or infinite value");
        }
    }
    
    private void validateBigDecimalDivisor(BigDecimal factor) {
        if (factor == null || factor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by null or zero");
        }
    }
    
    private void validateIntegerDivisor(int factor) {
        if (factor == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
    }
    
    private void validateExchangeRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
    }
    
    private void validatePercentage(double percentage) {
        if (Double.isNaN(percentage) || Double.isInfinite(percentage)) {
            throw new IllegalArgumentException("Percentage cannot be NaN or infinite");
        }
    }
    
    private void validateTaxRate(double taxRate) {
        if (taxRate < 0.0 || taxRate > 1000.0) { // Max 1000% tax rate
            throw new IllegalArgumentException("Tax rate must be between 0% and 1000%");
        }
    }
    
    private void validateSplitParts(int parts) {
        if (parts <= 0) {
            throw new IllegalArgumentException("Split parts must be positive");
        }
        if (parts > 1000) {
            throw new IllegalArgumentException("Cannot split into more than 1000 parts");
        }
    }
    
    private void validateRoundingUnit(BigDecimal unit) {
        if (unit == null || unit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rounding unit must be positive");
        }
    }
    
    private void validateAllocationRatios(double[] ratios) {
        if (ratios == null || ratios.length == 0) {
            throw new IllegalArgumentException("Allocation ratios cannot be null or empty");
        }
        for (double ratio : ratios) {
            if (ratio < 0 || Double.isNaN(ratio) || Double.isInfinite(ratio)) {
                throw new IllegalArgumentException("Allocation ratios must be non-negative finite numbers");
            }
        }
    }
    
    private void validateAllocationTargets(MoneyClean[] targets) {
        if (targets == null || targets.length == 0) {
            throw new IllegalArgumentException("Allocation targets cannot be null or empty");
        }
        for (MoneyClean target : targets) {
            if (target == null) {
                throw new IllegalArgumentException("Allocation target cannot be null");
            }
            validateCurrency(target);
        }
    }
    
    // ==============================================
    // FORMATTING AND DISPLAY
    // ==============================================
    
    /**
     * Formats the money for display with currency symbol
     */
    public String format() {
        return String.format("%s %.2f", currency.getSymbol(), amount);
    }
    
    /**
     * Formats with explicit currency code
     */
    public String formatWithCode() {
        return String.format("%.2f %s", amount, currencyCode);
    }
    
    /**
     * Formats for accounting display with parentheses for negative amounts
     */
    public String formatAccounting() {
        if (isNegative()) {
            return String.format("(%s %.2f)", currency.getSymbol(), amount.abs());
        }
        return format();
    }
    
    /**
     * Formats with custom decimal places
     */
    public String format(int decimalPlaces) {
        if (decimalPlaces < 0 || decimalPlaces > 10) {
            throw new IllegalArgumentException("Decimal places must be between 0 and 10");
        }
        return String.format("%s %." + decimalPlaces + "f", currency.getSymbol(), amount);
    }
    
    // ==============================================
    // GETTERS (NO LOMBOK DEPENDENCY)
    // ==============================================
    
    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public String getCurrencyCode() { 
        return currencyCode; 
    }
    
    public Currency getCurrency() { 
        return currency; 
    }
    
    // ==============================================
    // OBJECT METHODS
    // ==============================================
    
    @Override
    public int compareTo(MoneyClean other) {
        validateCurrency(other);
        return this.amount.compareTo(other.amount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MoneyClean)) return false;
        MoneyClean other = (MoneyClean) obj;
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
}