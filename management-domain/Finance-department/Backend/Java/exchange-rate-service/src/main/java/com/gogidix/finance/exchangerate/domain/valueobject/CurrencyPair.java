package com.gogidix.finance.exchangerate.domain.valueobject;

import java.util.Objects;

/**
 * Value Object - Currency Pair
 * Represents a trading pair of currencies (e.g., EUR/USD)
 * Immutable value object following DDD principles
 */
public final class CurrencyPair {

    private final String baseCurrency;
    private final String quoteCurrency;
    private final String pair;

    private CurrencyPair(String baseCurrency, String quoteCurrency) {
        this.baseCurrency = validateCurrencyCode(baseCurrency, "baseCurrency");
        this.quoteCurrency = validateCurrencyCode(quoteCurrency, "quoteCurrency");
        if (baseCurrency.equals(quoteCurrency)) {
            throw new IllegalArgumentException("Base and quote currencies cannot be the same");
        }
        this.pair = baseCurrency + quoteCurrency;
    }

    private String validateCurrencyCode(String code, String fieldName) {
        if (code == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
        if (code.length() != 3) {
            throw new IllegalArgumentException(fieldName + " must be a valid ISO 4217 code (3 characters)");
        }
        if (!code.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(fieldName + " must contain only uppercase letters");
        }
        return code;
    }

    public static CurrencyPair of(String baseCurrency, String quoteCurrency) {
        return new CurrencyPair(baseCurrency, quoteCurrency);
    }

    public static CurrencyPair fromString(String pair) {
        if (pair == null || pair.length() != 6) {
            throw new IllegalArgumentException("Currency pair must be 6 characters (e.g., EURUSD)");
        }
        return new CurrencyPair(pair.substring(0, 3), pair.substring(3, 6));
    }

    public CurrencyPair invert() {
        return new CurrencyPair(quoteCurrency, baseCurrency);
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public String getPair() {
        return pair;
    }

    public String getPairWithSlash() {
        return baseCurrency + "/" + quoteCurrency;
    }

    public boolean involves(String currency) {
        return baseCurrency.equals(currency) || quoteCurrency.equals(currency);
    }

    public boolean isInverse(CurrencyPair other) {
        return this.baseCurrency.equals(other.quoteCurrency) &&
               this.quoteCurrency.equals(other.baseCurrency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(baseCurrency, that.baseCurrency) &&
               Objects.equals(quoteCurrency, that.quoteCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quoteCurrency);
    }

    @Override
    public String toString() {
        return getPairWithSlash();
    }
}
