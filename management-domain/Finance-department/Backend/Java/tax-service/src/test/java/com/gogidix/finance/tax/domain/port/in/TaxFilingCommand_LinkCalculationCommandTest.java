package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxFilingCommand;
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
class TaxFilingCommand_LinkCalculationCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.LinkCalculationCommand dto = new TaxFilingCommand.LinkCalculationCommand();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        dto.setCalculationId("val-calculationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-calculationId", dto.getCalculationId());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.LinkCalculationCommand dto1 = new TaxFilingCommand.LinkCalculationCommand();
        TaxFilingCommand.LinkCalculationCommand dto2 = new TaxFilingCommand.LinkCalculationCommand();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto1.setCalculationId("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        dto2.setCalculationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.LinkCalculationCommand dto = new TaxFilingCommand.LinkCalculationCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setCalculationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.LinkCalculationCommand dto = new TaxFilingCommand.LinkCalculationCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setCalculationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}