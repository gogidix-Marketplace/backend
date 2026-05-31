package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxCalculationCommand;
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
class TaxCalculationCommand_AddDeductionCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationCommand.AddDeductionCommand dto = new TaxCalculationCommand.AddDeductionCommand();
        dto.setTenantId("val-tenantId");
        dto.setCalculationId("val-calculationId");
        dto.setDeductionType("val-deductionType");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setReference("val-reference");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-calculationId", dto.getCalculationId());
        assertEquals("val-deductionType", dto.getDeductionType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-reference", dto.getReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationCommand.AddDeductionCommand dto1 = new TaxCalculationCommand.AddDeductionCommand();
        TaxCalculationCommand.AddDeductionCommand dto2 = new TaxCalculationCommand.AddDeductionCommand();
        dto1.setTenantId("test");
        dto1.setCalculationId("test");
        dto1.setDeductionType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setReference("test");
        dto2.setTenantId("test");
        dto2.setCalculationId("test");
        dto2.setDeductionType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationCommand.AddDeductionCommand dto = new TaxCalculationCommand.AddDeductionCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
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
        TaxCalculationCommand.AddDeductionCommand dto = new TaxCalculationCommand.AddDeductionCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        dto.setDeductionType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}