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
class Payslip_YearToDateItemTest {

        @Test
    void testBuilder() {
        Payslip.YearToDateItem dto = Payslip.YearToDateItem.builder()
                        .itemType("test-itemType")
            .itemCode("test-itemCode")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .category("test-category")
            .build();
        assertNotNull(dto);
        assertEquals("test-itemType", dto.getItemType());
        assertEquals("test-itemCode", dto.getItemCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-category", dto.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        Payslip.YearToDateItem dto = new Payslip.YearToDateItem();
        dto.setItemType("val-itemType");
        dto.setItemCode("val-itemCode");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        assertEquals("val-itemType", dto.getItemType());
        assertEquals("val-itemCode", dto.getItemCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        Payslip.YearToDateItem dto1 = Payslip.YearToDateItem.builder()
                        .itemType("test-itemType")
            .itemCode("test-itemCode")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .category("test-category")
            .build();
        Payslip.YearToDateItem dto2 = Payslip.YearToDateItem.builder()
                        .itemType("test-itemType")
            .itemCode("test-itemCode")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .category("test-category")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Payslip.YearToDateItem dto = Payslip.YearToDateItem.builder()
                        .itemType("test-itemType")
            .itemCode("test-itemCode")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .category("test-category")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}