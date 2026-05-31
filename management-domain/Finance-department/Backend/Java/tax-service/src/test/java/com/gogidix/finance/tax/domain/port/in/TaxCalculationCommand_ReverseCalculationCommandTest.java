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
class TaxCalculationCommand_ReverseCalculationCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationCommand.ReverseCalculationCommand dto = new TaxCalculationCommand.ReverseCalculationCommand();
        dto.setTenantId("val-tenantId");
        dto.setCalculationId("val-calculationId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-calculationId", dto.getCalculationId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationCommand.ReverseCalculationCommand dto1 = new TaxCalculationCommand.ReverseCalculationCommand();
        TaxCalculationCommand.ReverseCalculationCommand dto2 = new TaxCalculationCommand.ReverseCalculationCommand();
        dto1.setTenantId("test");
        dto1.setCalculationId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setCalculationId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationCommand.ReverseCalculationCommand dto = new TaxCalculationCommand.ReverseCalculationCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationCommand.ReverseCalculationCommand dto = new TaxCalculationCommand.ReverseCalculationCommand();
        dto.setTenantId("test");
        dto.setCalculationId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}