package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxCalculation;
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
class TaxCalculation_DeductionTest {

        @Test
    void testBuilder() {
        TaxCalculation.Deduction dto = TaxCalculation.Deduction.builder()
                        .deductionType("test-deductionType")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .reference("test-reference")
            .build();
        assertNotNull(dto);
        assertEquals("test-deductionType", dto.getDeductionType());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-reference", dto.getReference());
    }

    @Test
    void testSettersAndGetters() {
        TaxCalculation.Deduction dto = new TaxCalculation.Deduction();
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
        TaxCalculation.Deduction dto1 = TaxCalculation.Deduction.builder()
                        .deductionType("test-deductionType")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .reference("test-reference")
            .build();
        TaxCalculation.Deduction dto2 = TaxCalculation.Deduction.builder()
                        .deductionType("test-deductionType")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .reference("test-reference")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxCalculation.Deduction dto = TaxCalculation.Deduction.builder()
                        .deductionType("test-deductionType")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .reference("test-reference")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}