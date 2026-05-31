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
class PayrollConfig_AllowanceTest {

        @Test
    void testBuilder() {
        PayrollConfig.Allowance dto = PayrollConfig.Allowance.builder()
                        .allowanceCode("test-allowanceCode")
            .allowanceName("test-allowanceName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isTaxable(true)
            .requiresProof(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-allowanceCode", dto.getAllowanceCode());
        assertEquals("test-allowanceName", dto.getAllowanceName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getIsTaxable());
        assertTrue(dto.getRequiresProof());
    }

    @Test
    void testSettersAndGetters() {
        PayrollConfig.Allowance dto = new PayrollConfig.Allowance();
        dto.setAllowanceCode("val-allowanceCode");
        dto.setAllowanceName("val-allowanceName");
        dto.setAmount(BigDecimal.ONE);
        dto.setCalculationMethod("val-calculationMethod");
        dto.setIsTaxable(true);
        dto.setRequiresProof(true);
        assertEquals("val-allowanceCode", dto.getAllowanceCode());
        assertEquals("val-allowanceName", dto.getAllowanceName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-calculationMethod", dto.getCalculationMethod());
        assertTrue(dto.getIsTaxable());
        assertTrue(dto.getRequiresProof());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollConfig.Allowance dto1 = PayrollConfig.Allowance.builder()
                        .allowanceCode("test-allowanceCode")
            .allowanceName("test-allowanceName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isTaxable(true)
            .requiresProof(true)
            .build();
        PayrollConfig.Allowance dto2 = PayrollConfig.Allowance.builder()
                        .allowanceCode("test-allowanceCode")
            .allowanceName("test-allowanceName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isTaxable(true)
            .requiresProof(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollConfig.Allowance dto = PayrollConfig.Allowance.builder()
                        .allowanceCode("test-allowanceCode")
            .allowanceName("test-allowanceName")
            .amount(BigDecimal.TEN)
            .calculationMethod("test-calculationMethod")
            .isTaxable(true)
            .requiresProof(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}