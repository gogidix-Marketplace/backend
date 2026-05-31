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
class TaxFilingCommand_MarkAsPaidCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.MarkAsPaidCommand dto = new TaxFilingCommand.MarkAsPaidCommand();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        dto.setPaymentReference("val-paymentReference");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-paymentReference", dto.getPaymentReference());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.MarkAsPaidCommand dto1 = new TaxFilingCommand.MarkAsPaidCommand();
        TaxFilingCommand.MarkAsPaidCommand dto2 = new TaxFilingCommand.MarkAsPaidCommand();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto1.setPaymentReference("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        dto2.setPaymentReference("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.MarkAsPaidCommand dto = new TaxFilingCommand.MarkAsPaidCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setPaymentReference("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.MarkAsPaidCommand dto = new TaxFilingCommand.MarkAsPaidCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setPaymentReference("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}