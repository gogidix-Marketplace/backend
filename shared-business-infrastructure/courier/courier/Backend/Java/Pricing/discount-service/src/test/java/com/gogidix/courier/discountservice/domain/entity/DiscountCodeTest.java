package com.gogidix.courier.discountservice.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCodeTest {

    @Test
    void testCreateDiscountCode() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "SAVE20",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(20)
        );

        assertNotNull(code.getId());
        assertEquals("tenant1", code.getTenantId());
        assertEquals("SAVE20", code.getCode());
        assertEquals(DiscountCode.DiscountType.PERCENTAGE, code.getDiscountType());
        assertEquals(BigDecimal.valueOf(20), code.getDiscountValue());
        assertEquals(DiscountCode.DiscountStatus.ACTIVE, code.getStatus());
        assertEquals(0, code.getCurrentUses());
    }

    @Test
    void testCalculateDiscountPercentage() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "SAVE20",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(20)
        );

        BigDecimal discount = code.calculateDiscount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(20), discount);
    }

    @Test
    void testCalculateDiscountFixed() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "SAVE10",
                DiscountCode.DiscountType.FIXED,
                BigDecimal.valueOf(10)
        );

        BigDecimal discount = code.calculateDiscount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(10), discount);
    }

    @Test
    void testMaxDiscountAmount() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "SAVE50",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(50)
        );
        code.setMaxDiscountAmount(BigDecimal.valueOf(25));

        BigDecimal discount = code.calculateDiscount(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(25), discount);
    }

    @Test
    void testIsExpired() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "EXPIRED",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10)
        );
        code.setEndDate(LocalDate.now().minusDays(1));

        assertTrue(code.isExpired());
    }

    @Test
    void testIsMaxUsageReached() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "LIMITED",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10)
        );
        code.setMaxUses(5);
        code.setCurrentUses(5);

        assertTrue(code.isMaxUsageReached());
    }

    @Test
    void testRecordUsage() {
        DiscountCode code = new DiscountCode(
                "tenant1",
                "TRACKER",
                DiscountCode.DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10)
        );

        code.recordUsage();
        assertEquals(1, code.getCurrentUses());
    }
}
