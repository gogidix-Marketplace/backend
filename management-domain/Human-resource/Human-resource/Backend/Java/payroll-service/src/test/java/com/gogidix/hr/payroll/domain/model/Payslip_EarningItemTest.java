package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.model.Payslip;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class Payslip_EarningItemTest {

        @Test
    void testBuilder() {
        Payslip.EarningItem dto = Payslip.EarningItem.builder()
                        .earningCode("test-earningCode")
            .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .isTaxable(true)
            .category("test-category")
            .build();
        assertNotNull(dto);
        assertEquals("test-earningCode", dto.getEarningCode());
        assertEquals("test-earningType", dto.getEarningType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(42, dto.getHours());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(42, dto.getQuantity());
        assertTrue(dto.getIsTaxable());
        assertEquals("test-category", dto.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        Payslip.EarningItem dto = new Payslip.EarningItem();
        dto.setEarningCode("val-earningCode");
        dto.setEarningType("val-earningType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setHours(99);
        dto.setRate(BigDecimal.ONE);
        dto.setQuantity(99);
        dto.setIsTaxable(true);
        dto.setCategory("val-category");
        assertEquals("val-earningCode", dto.getEarningCode());
        assertEquals("val-earningType", dto.getEarningType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(99, dto.getHours());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(99, dto.getQuantity());
        assertTrue(dto.getIsTaxable());
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        Payslip.EarningItem dto1 = Payslip.EarningItem.builder()
                        .earningCode("test-earningCode")
            .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .isTaxable(true)
            .category("test-category")
            .build();
        Payslip.EarningItem dto2 = Payslip.EarningItem.builder()
                        .earningCode("test-earningCode")
            .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .isTaxable(true)
            .category("test-category")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Payslip.EarningItem dto = Payslip.EarningItem.builder()
                        .earningCode("test-earningCode")
            .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .isTaxable(true)
            .category("test-category")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}