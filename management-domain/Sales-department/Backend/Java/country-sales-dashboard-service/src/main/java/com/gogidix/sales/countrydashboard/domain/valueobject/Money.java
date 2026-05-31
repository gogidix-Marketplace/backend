package com.gogidix.sales.countrydashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Money Value Object
 * Represents monetary values with currency
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    private BigDecimal amount;
    private String currency;

    public static Money of(BigDecimal amount, String currency) {
        return new Money(
                amount.setScale(2, RoundingMode.HALF_UP),
                currency
        );
    }

    public static Money of(String amount, String currency) {
        return new Money(
                new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP),
                currency
        );
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), currency);
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return Money.of(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return Money.of(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return Money.of(this.amount.multiply(multiplier), this.currency);
    }

    public Money divide(BigDecimal divisor) {
        return Money.of(this.amount.divide(divisor, 2, RoundingMode.HALF_UP), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare money with different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare money with different currencies");
        }
        return this.amount.compareTo(other.amount) < 0;
    }

    public Money convert(BigDecimal exchangeRate, String targetCurrency) {
        return Money.of(this.amount.multiply(exchangeRate), targetCurrency);
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
        return String.format("%s %s", amount, currency);
    }
}
