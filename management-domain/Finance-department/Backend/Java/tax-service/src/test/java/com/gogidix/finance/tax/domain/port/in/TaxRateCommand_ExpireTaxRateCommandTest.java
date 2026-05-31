package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxRateCommand;
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
class TaxRateCommand_ExpireTaxRateCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxRateCommand.ExpireTaxRateCommand dto = new TaxRateCommand.ExpireTaxRateCommand();
        dto.setTenantId("val-tenantId");
        dto.setTaxRateId("val-taxRateId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-taxRateId", dto.getTaxRateId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateCommand.ExpireTaxRateCommand dto1 = new TaxRateCommand.ExpireTaxRateCommand();
        TaxRateCommand.ExpireTaxRateCommand dto2 = new TaxRateCommand.ExpireTaxRateCommand();
        dto1.setTenantId("test");
        dto1.setTaxRateId("test");
        dto2.setTenantId("test");
        dto2.setTaxRateId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateCommand.ExpireTaxRateCommand dto = new TaxRateCommand.ExpireTaxRateCommand();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateCommand.ExpireTaxRateCommand dto = new TaxRateCommand.ExpireTaxRateCommand();
        dto.setTenantId("test");
        dto.setTaxRateId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}