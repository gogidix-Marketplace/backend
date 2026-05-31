package com.gogidix.finance.currency.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object - Money
 * Represents a monetary amount with a specific currency
 * Immutable value object following DDD principles
 */
public final class Money {

    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.amount = Objects.requireNonNull(amount, "amount is required");
        this.currency = Objects.requireNonNull(currency, "currency is required");
        validate();
    }

    private void validate() {
        if (amount.scale() > 10) {
            throw new IllegalArgumentException("Amount cannot have more than 10 decimal places");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be a valid ISO 4217 code");
        }
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Result would be negative");
        }
        return new Money(result, this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        if (multiplier == null || multiplier.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Multiplier must be non-negative");
        }
        return new Money(this.amount.multiply(multiplier).setScale(10, RoundingMode.HALF_UP), this.currency);
    }

    public Money divide(BigDecimal divisor) {
        if (divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Divisor cannot be null or zero");
        }
        return new Money(this.amount.divide(divisor, 10, RoundingMode.HALF_UP), this.currency);
    }

    public Money convert(String targetCurrency, BigDecimal exchangeRate) {
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        BigDecimal convertedAmount = this.amount.multiply(exchangeRate).setScale(10, RoundingMode.HALF_UP);
        return new Money(convertedAmount, targetCurrency);
    }

    public boolean isGreaterThan(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    public boolean equalsInValue(Money other) {
        ensureSameCurrency(other);
        return this.amount.compareTo(other.amount) == 0;
    }

    private void ensureSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
