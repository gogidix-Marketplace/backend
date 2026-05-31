package com.gogidix.hr.payroll.domain.model;

import com.gogidix.hr.payroll.domain.model.PayrollEntry;
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
class PayrollEntry_EarningDetailTest {

        @Test
    void testBuilder() {
        PayrollEntry.EarningDetail dto = PayrollEntry.EarningDetail.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .category("test-category")
            .referenceId("test-referenceId")
            .build();
        assertNotNull(dto);
        assertEquals("test-earningType", dto.getEarningType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(42, dto.getHours());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(42, dto.getQuantity());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-referenceId", dto.getReferenceId());
    }

    @Test
    void testSettersAndGetters() {
        PayrollEntry.EarningDetail dto = new PayrollEntry.EarningDetail();
        dto.setEarningType("val-earningType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setHours(99);
        dto.setRate(BigDecimal.ONE);
        dto.setQuantity(99);
        dto.setCategory("val-category");
        dto.setReferenceId("val-referenceId");
        assertEquals("val-earningType", dto.getEarningType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(99, dto.getHours());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(99, dto.getQuantity());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-referenceId", dto.getReferenceId());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntry.EarningDetail dto1 = PayrollEntry.EarningDetail.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .category("test-category")
            .referenceId("test-referenceId")
            .build();
        PayrollEntry.EarningDetail dto2 = PayrollEntry.EarningDetail.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .category("test-category")
            .referenceId("test-referenceId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollEntry.EarningDetail dto = PayrollEntry.EarningDetail.builder()
                        .earningType("test-earningType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .hours(42)
            .rate(BigDecimal.TEN)
            .quantity(42)
            .category("test-category")
            .referenceId("test-referenceId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}