package com.gogidix.sales.dashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Value Object for currency conversion
 * Handles multi-currency conversions for global sales reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversion {

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal exchangeRate;
    private Instant rateTimestamp;
    private String rateSource;

    private static final Map<String, BigDecimal> MOCK_RATES = new ConcurrentHashMap<>();

    static {
        // Mock exchange rates relative to USD
        MOCK_RATES.put("USD-USD", BigDecimal.ONE);
        MOCK_RATES.put("USD-EUR", new BigDecimal("0.92"));
        MOCK_RATES.put("USD-GBP", new BigDecimal("0.79"));
        MOCK_RATES.put("USD-JPY", new BigDecimal("149.50"));
        MOCK_RATES.put("USD-CNY", new BigDecimal("7.24"));
        MOCK_RATES.put("USD-AUD", new BigDecimal("1.53"));
        MOCK_RATES.put("USD-CAD", new BigDecimal("1.36"));
        MOCK_RATES.put("USD-INR", new BigDecimal("83.12"));
        MOCK_RATES.put("USD-BRL", new BigDecimal("4.97"));
        MOCK_RATES.put("USD-SGD", new BigDecimal("1.34"));
        MOCK_RATES.put("USD-AED", new BigDecimal("3.67"));

        // Reverse rates
        MOCK_RATES.put("EUR-USD", new BigDecimal("1.09"));
        MOCK_RATES.put("GBP-USD", new BigDecimal("1.27"));
        MOCK_RATES.put("JPY-USD", new BigDecimal("0.0067"));
        MOCK_RATES.put("CNY-USD", new BigDecimal("0.14"));
        MOCK_RATES.put("AUD-USD", new BigDecimal("0.65"));
        MOCK_RATES.put("CAD-USD", new BigDecimal("0.74"));
        MOCK_RATES.put("INR-USD", new BigDecimal("0.012"));
        MOCK_RATES.put("BRL-USD", new BigDecimal("0.20"));
        MOCK_RATES.put("SGD-USD", new BigDecimal("0.75"));
        MOCK_RATES.put("AED-USD", new BigDecimal("0.27"));
    }

    /**
     * Converts amount from one currency to another
     */
    public Money convert(Money amount, String targetCurrency) {
        if (amount.getCurrency().equals(targetCurrency)) {
            return amount;
        }

        String rateKey = amount.getCurrency() + "-" + targetCurrency;
        BigDecimal rate = MOCK_RATES.getOrDefault(rateKey, BigDecimal.ONE);

        BigDecimal convertedAmount = amount.getAmount()
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);

        return Money.builder()
                .amount(convertedAmount)
                .currency(targetCurrency)
                .build();
    }

    /**
     * Converts amount to base currency
     */
    public Money convertToBase(Money amount, String baseCurrency) {
        return convert(amount, baseCurrency);
    }

    /**
     * Gets exchange rate between two currencies
     */
    public BigDecimal getExchangeRate(String from, String to) {
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }

        String rateKey = from + "-" + to;
        return MOCK_RATES.getOrDefault(rateKey, BigDecimal.ONE);
    }

    /**
     * Creates a currency conversion with current rate
     */
    public static CurrencyConversion create(String fromCurrency, String toCurrency) {
        String rateKey = fromCurrency + "-" + toCurrency;
        BigDecimal rate = MOCK_RATES.getOrDefault(rateKey, BigDecimal.ONE);

        return CurrencyConversion.builder()
                .fromCurrency(fromCurrency)
                .toCurrency(toCurrency)
                .exchangeRate(rate)
                .rateTimestamp(Instant.now())
                .rateSource("INTERNAL")
                .build();
    }

    /**
     * Validates currency code
     */
    public static boolean isValidCurrency(String currencyCode) {
        try {
            Currency.getInstance(currencyCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Gets all supported currencies
     */
    public static String[] getSupportedCurrencies() {
        return new String[]{"USD", "EUR", "GBP", "JPY", "CNY", "AUD", "CAD", "INR", "BRL", "SGD", "AED"};
    }

    /**
     * Updates exchange rate
     */
    public void updateRate(BigDecimal newRate, String source) {
        this.exchangeRate = newRate;
        this.rateTimestamp = Instant.now();
        this.rateSource = source;

        String rateKey = this.fromCurrency + "-" + this.toCurrency;
        MOCK_RATES.put(rateKey, newRate);
    }

    /**
     * Money inner class for conversions
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Money {
        private BigDecimal amount;
        private String currency;

        public static Money zero(String currency) {
            return new Money(BigDecimal.ZERO, currency);
        }

        public static Money of(BigDecimal amount, String currency) {
            return new Money(amount.setScale(2, RoundingMode.HALF_UP), currency);
        }
    }
}
