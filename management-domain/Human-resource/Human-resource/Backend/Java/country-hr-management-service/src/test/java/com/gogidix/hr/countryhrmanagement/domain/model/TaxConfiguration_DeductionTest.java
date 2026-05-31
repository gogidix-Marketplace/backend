package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.TaxConfiguration;
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
class TaxConfiguration_DeductionTest {

        @Test
    void testBuilder() {
        TaxConfiguration.Deduction dto = TaxConfiguration.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .maxAmount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .requiresDocumentation(true)
            .applicableConditions("test-applicableConditions")
            .build();
        assertNotNull(dto);
        assertEquals("test-deductionCode", dto.getDeductionCode());
        assertEquals("test-deductionName", dto.getDeductionName());
        assertEquals(BigDecimal.TEN, dto.getMaxAmount());
        assertEquals("test-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getRequiresDocumentation());
        assertEquals("test-applicableConditions", dto.getApplicableConditions());
    }

    @Test
    void testSettersAndGetters() {
        TaxConfiguration.Deduction dto = new TaxConfiguration.Deduction();
        dto.setDeductionCode("val-deductionCode");
        dto.setDeductionName("val-deductionName");
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setCalculationMethod("val-calculationMethod");
        dto.setRequiresDocumentation(true);
        dto.setApplicableConditions("val-applicableConditions");
        assertEquals("val-deductionCode", dto.getDeductionCode());
        assertEquals("val-deductionName", dto.getDeductionName());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals("val-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getRequiresDocumentation());
        assertEquals("val-applicableConditions", dto.getApplicableConditions());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxConfiguration.Deduction dto1 = TaxConfiguration.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .maxAmount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .requiresDocumentation(true)
            .applicableConditions("test-applicableConditions")
            .build();
        TaxConfiguration.Deduction dto2 = TaxConfiguration.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .maxAmount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .requiresDocumentation(true)
            .applicableConditions("test-applicableConditions")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxConfiguration.Deduction dto = TaxConfiguration.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .maxAmount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .requiresDocumentation(true)
            .applicableConditions("test-applicableConditions")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}