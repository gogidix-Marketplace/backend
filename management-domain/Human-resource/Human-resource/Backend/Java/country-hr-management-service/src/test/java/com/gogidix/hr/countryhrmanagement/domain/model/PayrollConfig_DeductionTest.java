package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.PayrollConfig;
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
class PayrollConfig_DeductionTest {

        @Test
    void testBuilder() {
        PayrollConfig.Deduction dto = PayrollConfig.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isMandatory(true)
            .priority("test-priority")
            .build();
        assertNotNull(dto);
        assertEquals("test-deductionCode", dto.getDeductionCode());
        assertEquals("test-deductionName", dto.getDeductionName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getIsMandatory());
        assertEquals("test-priority", dto.getPriority());
    }

    @Test
    void testSettersAndGetters() {
        PayrollConfig.Deduction dto = new PayrollConfig.Deduction();
        dto.setDeductionCode("val-deductionCode");
        dto.setDeductionName("val-deductionName");
        dto.setAmount(BigDecimal.ONE);
        dto.setCalculationMethod("val-calculationMethod");
        dto.setIsMandatory(true);
        dto.setPriority("val-priority");
        assertEquals("val-deductionCode", dto.getDeductionCode());
        assertEquals("val-deductionName", dto.getDeductionName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getIsMandatory());
        assertEquals("val-priority", dto.getPriority());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollConfig.Deduction dto1 = PayrollConfig.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isMandatory(true)
            .priority("test-priority")
            .build();
        PayrollConfig.Deduction dto2 = PayrollConfig.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isMandatory(true)
            .priority("test-priority")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollConfig.Deduction dto = PayrollConfig.Deduction.builder()
                        .deductionCode("test-deductionCode")
            .deductionName("test-deductionName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isMandatory(true)
            .priority("test-priority")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}