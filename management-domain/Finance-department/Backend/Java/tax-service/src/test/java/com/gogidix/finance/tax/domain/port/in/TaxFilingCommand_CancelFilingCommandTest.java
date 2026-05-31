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
class TaxFilingCommand_CancelFilingCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.CancelFilingCommand dto = new TaxFilingCommand.CancelFilingCommand();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.CancelFilingCommand dto1 = new TaxFilingCommand.CancelFilingCommand();
        TaxFilingCommand.CancelFilingCommand dto2 = new TaxFilingCommand.CancelFilingCommand();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.CancelFilingCommand dto = new TaxFilingCommand.CancelFilingCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.CancelFilingCommand dto = new TaxFilingCommand.CancelFilingCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}