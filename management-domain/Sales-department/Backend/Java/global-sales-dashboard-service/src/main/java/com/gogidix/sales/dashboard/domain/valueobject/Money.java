package com.gogidix.sales.dashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * Value Object representing monetary amounts with currency
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money {

    private BigDecimal amount;
    private String currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.getCurrencyCode();
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), currency);
    }

    public static Money usd(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), "USD");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract different currencies");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP), this.currency);
    }

    public Money divide(BigDecimal divisor) {
        return new Money(this.amount.divide(divisor, 2, RoundingMode.HALF_UP), this.currency);
    }

    public Money convert(BigDecimal exchangeRate, String targetCurrency) {
        return new Money(
                this.amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP),
                targetCurrency
        );
    }

    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare different currencies");
        }
        return this.amount.compareTo(other.amount) < 0;
    }
}
