package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxCalculationController;
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
class TaxCalculationController_AddDeductionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.AddDeductionRequestDto dto = new TaxCalculationController.AddDeductionRequestDto();
        dto.setDeductionType("val-deductionType");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        assertEquals("val-deductionType", dto.getDeductionType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.AddDeductionRequestDto dto1 = new TaxCalculationController.AddDeductionRequestDto();
        TaxCalculationController.AddDeductionRequestDto dto2 = new TaxCalculationController.AddDeductionRequestDto();
        dto1.setDeductionType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setReference("test");
        dto2.setDeductionType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setDeductionType(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.AddDeductionRequestDto dto = new TaxCalculationController.AddDeductionRequestDto();
        dto.setDeductionType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.AddDeductionRequestDto dto = new TaxCalculationController.AddDeductionRequestDto();
        dto.setDeductionType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}