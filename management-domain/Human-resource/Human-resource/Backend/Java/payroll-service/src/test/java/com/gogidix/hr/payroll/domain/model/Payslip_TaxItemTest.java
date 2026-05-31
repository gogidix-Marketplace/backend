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
class Payslip_TaxItemTest {

        @Test
    void testBuilder() {
        Payslip.TaxItem dto = Payslip.TaxItem.builder()
                        .taxCode("test-taxCode")
            .taxType("test-taxType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .taxableWages(BigDecimal.TEN)
            .category("test-category")
            .build();
        assertNotNull(dto);
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals("test-taxType", dto.getTaxType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getRate());
        assertEquals(BigDecimal.TEN, dto.getTaxableWages());
        assertEquals("test-category", dto.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        Payslip.TaxItem dto = new Payslip.TaxItem();
        dto.setTaxCode("val-taxCode");
        dto.setTaxType("val-taxType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setRate(BigDecimal.ONE);
        dto.setTaxableWages(BigDecimal.ONE);
        dto.setCategory("val-category");
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals("val-taxType", dto.getTaxType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertEquals(BigDecimal.ONE, dto.getTaxableWages());
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        Payslip.TaxItem dto1 = Payslip.TaxItem.builder()
                        .taxCode("test-taxCode")
            .taxType("test-taxType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .taxableWages(BigDecimal.TEN)
            .category("test-category")
            .build();
        Payslip.TaxItem dto2 = Payslip.TaxItem.builder()
                        .taxCode("test-taxCode")
            .taxType("test-taxType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .taxableWages(BigDecimal.TEN)
            .category("test-category")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Payslip.TaxItem dto = Payslip.TaxItem.builder()
                        .taxCode("test-taxCode")
            .taxType("test-taxType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .rate(BigDecimal.TEN)
            .taxableWages(BigDecimal.TEN)
            .category("test-category")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}