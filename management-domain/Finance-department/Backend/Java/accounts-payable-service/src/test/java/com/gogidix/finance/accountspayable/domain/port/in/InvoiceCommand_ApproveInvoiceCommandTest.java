package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.port.in.InvoiceCommand;
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
class InvoiceCommand_ApproveInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.ApproveInvoiceCommand dto = new InvoiceCommand.ApproveInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setApprover("val-approver");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-approver", dto.getApprover());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.ApproveInvoiceCommand dto1 = new InvoiceCommand.ApproveInvoiceCommand();
        InvoiceCommand.ApproveInvoiceCommand dto2 = new InvoiceCommand.ApproveInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setApprover("test");
        dto1.setApprovalLevel(Invoice.ApprovalLevel.NONE);
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setApprover("test");
        dto2.setApprovalLevel(Invoice.ApprovalLevel.NONE);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.ApproveInvoiceCommand dto = new InvoiceCommand.ApproveInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setApprover("test");
        dto.setApprovalLevel(Invoice.ApprovalLevel.NONE);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.ApproveInvoiceCommand dto = new InvoiceCommand.ApproveInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setApprover("test");
        dto.setApprovalLevel(Invoice.ApprovalLevel.NONE);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}