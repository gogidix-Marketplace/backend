package com.gogidix.shared.model.domain.model.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MoneyClean Tests")
class MoneyCleanTest {

    @Test
    void testCreateWithBigDecimal() {
        MoneyClean m = new MoneyClean(new BigDecimal("100.50"), "USD");
        assertEquals(new BigDecimal("100.50"), m.getAmount());
        assertEquals("USD", m.getCurrencyCode());
    }

    @Test
    void testCreateWithDouble() {
        MoneyClean m = new MoneyClean(50.25, "EUR");
        assertEquals(new BigDecimal("50.25"), m.getAmount());
        assertEquals("EUR", m.getCurrencyCode());
    }

    @Test
    void testCreateWithString() {
        MoneyClean m = new MoneyClean("200.75", "GBP");
        assertEquals(new BigDecimal("200.75"), m.getAmount());
    }

    @Test
    void testCreateWithLongCents() {
        MoneyClean m = new MoneyClean(10050L, "USD");
        assertNotNull(m.getAmount());
    }

    @Test
    void testAdd() {
        MoneyClean a = new MoneyClean("100.00", "USD");
        MoneyClean b = new MoneyClean("50.00", "USD");
        MoneyClean result = a.add(b);
        assertEquals(new BigDecimal("150.00"), result.getAmount());
    }

    @Test
    void testSubtract() {
        MoneyClean a = new MoneyClean("100.00", "USD");
        MoneyClean b = new MoneyClean("30.00", "USD");
        MoneyClean result = a.subtract(b);
        assertEquals(new BigDecimal("70.00"), result.getAmount());
    }

    @Test
    void testMultiply() {
        MoneyClean m = new MoneyClean("100.00", "USD");
        MoneyClean result = m.multiply(2.0);
        assertEquals(new BigDecimal("200.00"), result.getAmount());
    }

    @Test
    void testNegate() {
        MoneyClean m = new MoneyClean("100.00", "USD");
        MoneyClean result = m.negate();
        assertEquals(new BigDecimal("-100.00"), result.getAmount());
    }

    @Test
    void testAbs() {
        MoneyClean m = new MoneyClean("-50.00", "USD");
        MoneyClean result = m.abs();
        assertEquals(new BigDecimal("50.00"), result.getAmount());
    }

    @Test
    void testIsZero() {
        assertTrue(MoneyClean.ZERO.isZero());
        assertFalse(new MoneyClean("1.00", "USD").isZero());
    }

    @Test
    void testIsPositive() {
        assertTrue(new MoneyClean("1.00", "USD").isPositive());
        assertFalse(new MoneyClean("-1.00", "USD").isPositive());
    }

    @Test
    void testIsNegative() {
        assertTrue(new MoneyClean("-1.00", "USD").isNegative());
        assertFalse(new MoneyClean("1.00", "USD").isNegative());
    }

    @Test
    void testCompareTo() {
        MoneyClean small = new MoneyClean("50.00", "USD");
        MoneyClean big = new MoneyClean("100.00", "USD");
        assertTrue(small.compareTo(big) < 0);
        assertTrue(big.compareTo(small) > 0);
        assertEquals(0, small.compareTo(small));
    }

    @Test
    void testStatics() {
        assertNotNull(MoneyClean.ZERO);
        assertNotNull(MoneyClean.ONE_USD);
        assertNotNull(MoneyClean.ONE_EUR);
        assertNotNull(MoneyClean.ONE_GBP);
    }

    @Test
    void testNullAmountThrows() {
        assertThrows(Exception.class, () -> new MoneyClean((BigDecimal) null, "USD"));
    }

    @Test
    void testGetCurrency() {
        MoneyClean m = new MoneyClean("10.00", "USD");
        assertNotNull(m.getCurrency());
        assertEquals("USD", m.getCurrency().getCurrencyCode());
    }
}
