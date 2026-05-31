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
class TaxFilingCommand_AddAdjustmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.AddAdjustmentCommand dto = new TaxFilingCommand.AddAdjustmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        dto.setAdjustmentType("val-adjustmentType");
        dto.setAmount(BigDecimal.ONE);
        dto.setReason("val-reason");
        dto.setReference("val-reference");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-adjustmentType", dto.getAdjustmentType());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-reference", dto.getReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.AddAdjustmentCommand dto1 = new TaxFilingCommand.AddAdjustmentCommand();
        TaxFilingCommand.AddAdjustmentCommand dto2 = new TaxFilingCommand.AddAdjustmentCommand();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto1.setAdjustmentType("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setReason("test");
        dto1.setReference("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        dto2.setAdjustmentType("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setReason("test");
        dto2.setReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.AddAdjustmentCommand dto = new TaxFilingCommand.AddAdjustmentCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setAdjustmentType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.AddAdjustmentCommand dto = new TaxFilingCommand.AddAdjustmentCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setAdjustmentType("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setReason("test");
        dto.setReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}