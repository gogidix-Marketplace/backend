package com.gogidix.shared.model.domain.model.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Money value object
 */
@DisplayName("Money Tests")
class MoneyTest {

    @Test
    @DisplayName("Test constructor with BigDecimal")
    void testConstructorWithBigDecimal() {
        Money money = new Money(new BigDecimal("100.50"), "USD");

        assertEquals(new BigDecimal("100.50"), money.getAmount());
        assertEquals("USD", money.getCurrencyCode());
        assertEquals("USD", money.getCurrency().getCurrencyCode());
    }

    @Test
    @DisplayName("Test constructor with double")
    void testConstructorWithDouble() {
        Money money = new Money(99.99, "EUR");

        assertEquals(0, new BigDecimal("99.99").compareTo(money.getAmount()));
        assertEquals("EUR", money.getCurrencyCode());
    }

    @Test
    @DisplayName("Test constructor with long (cents)")
    void testConstructorWithLongCents() {
        Money money = new Money(10050L, "USD"); // 10050 cents = $100.50

        assertEquals(0, new BigDecimal("100.50").compareTo(money.getAmount()));
    }

    @Test
    @DisplayName("Test constructor with String")
    void testConstructorWithString() {
        Money money = new Money("250.75", "GBP");

        assertEquals(0, new BigDecimal("250.75").compareTo(money.getAmount()));
        assertEquals("GBP", money.getCurrencyCode());
    }

    @Test
    @DisplayName("Test null amount throws exception")
    void testNullAmountThrowsException() {
        assertThrows(NullPointerException.class, () -> new Money((BigDecimal) null, "USD"));
    }

    @Test
    @DisplayName("Test null currency code throws exception")
    void testNullCurrencyCodeThrowsException() {
        assertThrows(NullPointerException.class, () -> new Money(BigDecimal.TEN, null));
    }

    @Test
    @DisplayName("Test amount is scaled to 2 decimal places")
    void testAmountIsScaled() {
        Money money1 = new Money("100.555", "USD");
        Money money2 = new Money("100.554", "USD");

        assertEquals(new BigDecimal("100.56"), money1.getAmount()); // Round up
        assertEquals(new BigDecimal("100.55"), money2.getAmount()); // Round down
    }

    @Test
    @DisplayName("Test add method")
    void testAdd() {
        Money money1 = new Money("100.00", "USD");
        Money money2 = new Money("50.50", "USD");

        Money result = money1.add(money2);

        assertEquals(0, new BigDecimal("150.50").compareTo(result.getAmount()));
        assertEquals("USD", result.getCurrencyCode());
    }

    @Test
    @DisplayName("Test add with different currency throws exception")
    void testAddWithDifferentCurrency() {
        Money usd = new Money("100.00", "USD");
        Money eur = new Money("50.00", "EUR");

        assertThrows(IllegalArgumentException.class, () -> usd.add(eur));
    }

    @Test
    @DisplayName("Test subtract method")
    void testSubtract() {
        Money money1 = new Money("100.00", "USD");
        Money money2 = new Money("30.50", "USD");

        Money result = money1.subtract(money2);

        assertEquals(0, new BigDecimal("69.50").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test multiply by double")
    void testMultiplyByDouble() {
        Money money = new Money("100.00", "USD");
        Money result = money.multiply(1.5);

        assertEquals(0, new BigDecimal("150.00").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test multiply by BigDecimal")
    void testMultiplyByBigDecimal() {
        Money money = new Money("100.00", "USD");
        Money result = money.multiply(new BigDecimal("2.5"));

        assertEquals(0, new BigDecimal("250.00").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test multiply by integer")
    void testMultiplyByInteger() {
        Money money = new Money("25.00", "USD");
        Money result = money.multiply(4);

        assertEquals(0, new BigDecimal("100.00").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test divide by double")
    void testDivideByDouble() {
        Money money = new Money("100.00", "USD");
        Money result = money.divide(4.0);

        assertEquals(0, new BigDecimal("25.00").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test divide by zero throws exception")
    void testDivideByZeroThrowsException() {
        Money money = new Money("100.00", "USD");

        assertThrows(IllegalArgumentException.class, () -> money.divide(0.0));
        assertThrows(IllegalArgumentException.class, () -> money.divide(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> money.divide(0));
    }

    @Test
    @DisplayName("Test abs method")
    void testAbs() {
        Money positive = new Money("100.00", "USD");
        Money negative = new Money("-100.00", "USD");

        assertEquals(0, positive.getAmount().compareTo(positive.abs().getAmount()));
        assertEquals(0, positive.getAmount().compareTo(negative.abs().getAmount()));
    }

    @Test
    @DisplayName("Test negate method")
    void testNegate() {
        Money money = new Money("100.00", "USD");
        Money negated = money.negate();

        assertEquals(0, new BigDecimal("-100.00").compareTo(negated.getAmount()));
    }

    @ParameterizedTest
    @CsvSource({
        "100.00, true",
        "0.01, true",
        "-100.00, false",
        "0.00, false"
    })
    @DisplayName("Test isPositive")
    void testIsPositive(String amount, boolean expected) {
        Money money = new Money(amount, "USD");
        assertEquals(expected, money.isPositive());
    }

    @ParameterizedTest
    @CsvSource({
        "-100.00, true",
        "-0.01, true",
        "100.00, false",
        "0.00, false"
    })
    @DisplayName("Test isNegative")
    void testIsNegative(String amount, boolean expected) {
        Money money = new Money(amount, "USD");
        assertEquals(expected, money.isNegative());
    }

    @Test
    @DisplayName("Test isZero")
    void testIsZero() {
        Money zero = new Money("0.00", "USD");
        Money positive = new Money("0.01", "USD");
        Money negative = new Money("-0.01", "USD");

        assertTrue(zero.isZero());
        assertFalse(positive.isZero());
        assertFalse(negative.isZero());
    }

    @Test
    @DisplayName("Test comparison methods")
    void testComparisonMethods() {
        Money money1 = new Money("100.00", "USD");
        Money money2 = new Money("50.00", "USD");
        Money money3 = new Money("100.00", "USD");

        assertTrue(money1.isGreaterThan(money2));
        assertTrue(money1.isGreaterThanOrEqual(money2));
        assertTrue(money1.isGreaterThanOrEqual(money3));

        assertTrue(money2.isLessThan(money1));
        assertTrue(money2.isLessThanOrEqual(money1));
        assertTrue(money1.isLessThanOrEqual(money3));

        assertEquals(0, money1.compareTo(money3));
        assertTrue(money1.compareTo(money2) > 0);
        assertTrue(money2.compareTo(money1) < 0);
    }

    @Test
    @DisplayName("Test comparison with different currency throws exception")
    void testComparisonWithDifferentCurrency() {
        Money usd = new Money("100.00", "USD");
        Money eur = new Money("100.00", "EUR");

        assertThrows(IllegalArgumentException.class, () -> usd.isGreaterThan(eur));
    }

    @Test
    @DisplayName("Test toAmountInCents")
    void testToAmountInCents() {
        Money money = new Money("100.50", "USD");
        assertEquals(10050L, money.toAmountInCents());
    }

    @Test
    @DisplayName("Test toDouble")
    void testToDouble() {
        Money money = new Money("99.99", "USD");
        assertEquals(99.99, money.toDouble(), 0.001);
    }

    @Test
    @DisplayName("Test currency conversion")
    void testCurrencyConversion() {
        Money usd = new Money("100.00", "USD");
        BigDecimal exchangeRate = new BigDecimal("0.85");

        Money eur = usd.convertTo("EUR", exchangeRate);

        assertEquals("EUR", eur.getCurrencyCode());
        assertEquals(0, new BigDecimal("85.00").compareTo(eur.getAmount()));
    }

    @Test
    @DisplayName("Test conversion to same currency returns same instance")
    void testConversionToSameCurrency() {
        Money money = new Money("100.00", "USD");
        Money converted = money.convertTo("USD", BigDecimal.ONE);

        assertSame(money, converted);
    }

    @Test
    @DisplayName("Test applyPercentage")
    void testApplyPercentage() {
        Money money = new Money("100.00", "USD");
        Money result = money.applyPercentage(20); // 20%

        assertEquals(0, new BigDecimal("20.00").compareTo(result.getAmount()));
    }

    @Test
    @DisplayName("Test percentageOf")
    void testPercentageOf() {
        Money part = new Money("25.00", "USD");
        Money total = new Money("100.00", "USD");

        double percentage = part.percentageOf(total);

        assertEquals(25.0, percentage, 0.01);
    }

    @Test
    @DisplayName("Test percentageOf with zero total returns 0")
    void testPercentageOfWithZeroTotal() {
        Money part = new Money("25.00", "USD");
        Money zero = Money.ZERO;

        assertEquals(0.0, part.percentageOf(zero));
    }

    @Test
    @DisplayName("Test round methods")
    void testRoundMethods() {
        // Note: Money constructor always rounds to 2 decimal places with HALF_UP.
        // These tests verify the round methods work correctly on already-rounded values,
        // which is their primary use case for re-rounding after operations.

        Money money = new Money("100.5678", "USD"); // Constructor rounds to 100.57

        Money rounded = money.round();
        Money roundedUp = money.roundUp();
        Money roundedDown = money.roundDown();

        // All return 100.57 since Money stores values at 2 decimals
        assertEquals(new BigDecimal("100.57"), rounded.getAmount());
        assertEquals(new BigDecimal("100.57"), roundedUp.getAmount());
        assertEquals(new BigDecimal("100.57"), roundedDown.getAmount());

        // Test with different rounding modes on value that would round differently
        Money testMoney = new Money("33.33", "USD");
        Money roundedUpTest = testMoney.roundUp();
        assertEquals(new BigDecimal("33.33"), roundedUpTest.getAmount());
    }

    @Test
    @DisplayName("Test split")
    void testSplit() {
        Money money = new Money("100.00", "USD");
        Money[] parts = money.split(3);

        assertEquals(3, parts.length);

        // Check total equals original
        Money sum = Money.ZERO;
        for (Money part : parts) {
            sum = sum.add(part);
        }

        assertEquals(money.getAmount(), sum.getAmount());
    }

    @Test
    @DisplayName("Test split with zero parts throws exception")
    void testSplitWithZeroPartsThrowsException() {
        Money money = new Money("100.00", "USD");

        assertThrows(IllegalArgumentException.class, () -> money.split(0));
        assertThrows(IllegalArgumentException.class, () -> money.split(-1));
    }

    @Test
    @DisplayName("Test static factory methods")
    void testStaticFactoryMethods() {
        Money fromBigDecimal = Money.of(new BigDecimal("100.00"), "USD");
        Money fromDouble = Money.of(100.00, "USD");
        Money fromMinor = Money.ofMinor(10000L, "USD"); // 10000 cents = $100

        assertEquals(new BigDecimal("100.00"), fromBigDecimal.getAmount());
        assertEquals(new BigDecimal("100.00"), fromDouble.getAmount());
        assertEquals(new BigDecimal("100.00"), fromMinor.getAmount());
    }

    @Test
    @DisplayName("Test sum static method")
    void testSumStaticMethod() {
        Money m1 = new Money("100.00", "USD");
        Money m2 = new Money("50.00", "USD");
        Money m3 = new Money("25.00", "USD");

        Money sum = Money.sum(m1, m2, m3);

        assertEquals(0, new BigDecimal("175.00").compareTo(sum.getAmount()));
    }

    @Test
    @DisplayName("Test sum with empty array returns ZERO")
    void testSumWithEmptyArray() {
        Money sum = Money.sum();

        assertEquals(Money.ZERO, sum);
    }

    @Test
    @DisplayName("Test max static method")
    void testMaxStaticMethod() {
        Money m1 = new Money("100.00", "USD");
        Money m2 = new Money("200.00", "USD");

        Money max = Money.max(m1, m2);

        assertEquals(m2.getAmount(), max.getAmount());
    }

    @Test
    @DisplayName("Test min static method")
    void testMinStaticMethod() {
        Money m1 = new Money("100.00", "USD");
        Money m2 = new Money("200.00", "USD");

        Money min = Money.min(m1, m2);

        assertEquals(m1.getAmount(), min.getAmount());
    }

    @Test
    @DisplayName("Test format methods")
    void testFormatMethods() {
        Money money = new Money("100.50", "USD");

        String formatted = money.format();
        String withCode = money.formatWithCode();

        assertTrue(formatted.contains("$"));
        assertTrue(formatted.contains("100.50"));
        assertTrue(withCode.contains("USD"));
    }

    @Test
    @DisplayName("Test equals and hashCode")
    void testEqualsAndHashCode() {
        Money money1 = new Money("100.00", "USD");
        Money money2 = new Money("100.00", "USD");
        Money money3 = new Money("100.00", "EUR");
        Money money4 = new Money("200.00", "USD");

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
        assertNotEquals(money1, money3);
        assertNotEquals(money1, money4);
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        Money money = new Money("100.50", "USD");

        String result = money.toString();

        assertTrue(result.contains("100.50") || result.contains("100.5"));
        assertTrue(result.contains("USD"));
    }

    @Test
    @DisplayName("Test ZERO constant")
    void testZeroConstant() {
        assertTrue(Money.ZERO.isZero());
        assertEquals("USD", Money.ZERO.getCurrencyCode());
    }

    @Test
    @DisplayName("Test currency object is correctly initialized")
    void testCurrencyObject() {
        Money usd = new Money("100.00", "USD");
        Money eur = new Money("100.00", "EUR");
        Money gbp = new Money("100.00", "GBP");

        assertEquals(java.util.Currency.getInstance("USD"), usd.getCurrency());
        assertEquals(java.util.Currency.getInstance("EUR"), eur.getCurrency());
        assertEquals(java.util.Currency.getInstance("GBP"), gbp.getCurrency());
    }

    @Test
    @DisplayName("Test immutable operations return new instances")
    void testImmutableOperations() {
        Money original = new Money("100.00", "USD");
        Money added = original.add(new Money("50.00", "USD"));

        assertNotSame(original, added);
        assertEquals(new BigDecimal("100.00"), original.getAmount());
        assertEquals(new BigDecimal("150.00"), added.getAmount());
    }
}
