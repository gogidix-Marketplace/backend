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
class TaxCalculationCommand_AddTaxRateCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationCommand.AddTaxRateCommand dto = new TaxCalculationCommand.AddTaxRateCommand();
        dto.setTenantId("val-tenantId");
        dto.setCalculationId("val-calculationId");
        dto.setTaxCode("val-taxCode");
        dto.setRate(BigDecimal.ONE);
        dto.setIsRecoverable(true);
        dto.setDescription("val-description");
        dto.setIsCompound(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-calculationId", dto.getCalculationId());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getRate());
        assertTrue(dto.getIsRecoverable());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsCompound());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationCommand.AddTaxRateCommand dto1 = new TaxCalculationCommand.AddTaxRateCommand();
        TaxCalculationCommand.AddTaxRateCommand dto2 = new TaxCalculationCommand.AddTaxRateCommand();
        dto1.setTenantId("test");
        dto1.setCalculationId("test");
        dto1.setTaxCode("test");
        dto1.setTaxType(null);
        dto1.setRate(BigDecimal.TEN);
        dto1.setIsRecoverable(true);
        dto1.setDescription("test");
        dto1.setIsCompound(true);
        dto2.setTenantId("test");
        dto2.setCalculationId("test");
        dto2.setTaxCode("test");
        dto2.setTaxType(null);
        dto2.setRate(BigDecimal.TEN);
        dto2.setIsRecoverable(true);
        dto2.setDescription("test");
        dto2.setIsCompound(true);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationCommand.AddTaxRateCommand dto = new TaxCalculationCommand.AddTaxRateCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        dto.setTaxCode("test");
        dto.setTaxType(null);
        dto.setRate(BigDecimal.TEN);
        dto.setIsRecoverable(true);
        dto.setDescription("test");
        dto.setIsCompound(true);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationCommand.AddTaxRateCommand dto = new TaxCalculationCommand.AddTaxRateCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        dto.setTaxCode("test");
        dto.setTaxType(null);
        dto.setRate(BigDecimal.TEN);
        dto.setIsRecoverable(true);
        dto.setDescription("test");
        dto.setIsCompound(true);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}