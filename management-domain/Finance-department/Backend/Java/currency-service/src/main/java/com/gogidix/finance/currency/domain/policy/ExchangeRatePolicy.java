package com.gogidix.finance.currency.domain.policy;

import java.math.BigDecimal;

public class ExchangeRatePolicy {

    private String id;

    public ExchangeRatePolicy() {}

    public ExchangeRatePolicy(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public void validateRateValue(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rate must be positive");
        }
    }

    public boolean isRateChangeAcceptable(BigDecimal oldRate, BigDecimal newRate) {
        if (oldRate == null || newRate == null) return true;
        BigDecimal change = oldRate.subtract(newRate).abs();
        BigDecimal threshold = oldRate.multiply(new BigDecimal("0.5"));
        return change.compareTo(threshold) <= 0;
    }
}
