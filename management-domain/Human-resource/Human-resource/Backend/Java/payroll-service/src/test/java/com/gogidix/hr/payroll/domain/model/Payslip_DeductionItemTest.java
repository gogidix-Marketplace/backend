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
class Payslip_DeductionItemTest {

        @Test
    void testBuilder() {
        Payslip.DeductionItem dto = Payslip.DeductionItem.builder()
                        .deductionCode("test-deductionCode")
            .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .isPreTax(true)
            .isEmployeePaid(true)
            .category("test-category")
            .build();
        assertNotNull(dto);
        assertEquals("test-deductionCode", dto.getDeductionCode());
        assertEquals("test-deductionType", dto.getDeductionType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertTrue(dto.getIsPreTax());
        assertTrue(dto.getIsEmployeePaid());
        assertEquals("test-category", dto.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        Payslip.DeductionItem dto = new Payslip.DeductionItem();
        dto.setDeductionCode("val-deductionCode");
        dto.setDeductionType("val-deductionType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setRate(BigDecimal.ONE);
        dto.setIsPreTax(true);
        dto.setIsEmployeePaid(true);
        dto.setCategory("val-category");
        assertEquals("val-deductionCode", dto.getDeductionCode());
        assertEquals("val-deductionType", dto.getDeductionType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertTrue(dto.getIsPreTax());
        assertTrue(dto.getIsEmployeePaid());
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        Payslip.DeductionItem dto1 = Payslip.DeductionItem.builder()
                        .deductionCode("test-deductionCode")
            .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .isPreTax(true)
            .isEmployeePaid(true)
            .category("test-category")
            .build();
        Payslip.DeductionItem dto2 = Payslip.DeductionItem.builder()
                        .deductionCode("test-deductionCode")
            .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .isPreTax(true)
            .isEmployeePaid(true)
            .category("test-category")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Payslip.DeductionItem dto = Payslip.DeductionItem.builder()
                        .deductionCode("test-deductionCode")
            .deductionType("test-deductionType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .isPreTax(true)
            .isEmployeePaid(true)
            .category("test-category")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}