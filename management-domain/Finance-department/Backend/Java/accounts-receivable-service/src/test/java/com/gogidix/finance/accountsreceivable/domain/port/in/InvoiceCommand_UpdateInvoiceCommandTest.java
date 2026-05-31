package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.port.in.InvoiceCommand;
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
class InvoiceCommand_UpdateInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.UpdateInvoiceCommand dto = new InvoiceCommand.UpdateInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setCustomerEmail("val-customerEmail");
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setPaymentTerms("val-paymentTerms");
        dto.setNotes("val-notes");
        dto.setInternalNotes("val-internalNotes");
        dto.setSalesperson("val-salesperson");
        dto.setPurchaseOrderNumber("val-purchaseOrderNumber");
        dto.setCustomerReference("val-customerReference");
        dto.setShippingMethod("val-shippingMethod");
        dto.setTrackingNumber("val-trackingNumber");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalNotes", dto.getInternalNotes());
        assertEquals("val-salesperson", dto.getSalesperson());
        assertEquals("val-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals("val-customerReference", dto.getCustomerReference());
        assertEquals("val-shippingMethod", dto.getShippingMethod());
        assertEquals("val-trackingNumber", dto.getTrackingNumber());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.UpdateInvoiceCommand dto1 = new InvoiceCommand.UpdateInvoiceCommand();
        InvoiceCommand.UpdateInvoiceCommand dto2 = new InvoiceCommand.UpdateInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setCustomerEmail("test");
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setPaymentTerms("test");
        dto1.setNotes("test");
        dto1.setInternalNotes("test");
        dto1.setSalesperson("test");
        dto1.setPurchaseOrderNumber("test");
        dto1.setCustomerReference("test");
        dto1.setTags(Collections.emptyList());
        dto1.setShippingMethod("test");
        dto1.setTrackingNumber("test");
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setCustomerEmail("test");
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setPaymentTerms("test");
        dto2.setNotes("test");
        dto2.setInternalNotes("test");
        dto2.setSalesperson("test");
        dto2.setPurchaseOrderNumber("test");
        dto2.setCustomerReference("test");
        dto2.setTags(Collections.emptyList());
        dto2.setShippingMethod("test");
        dto2.setTrackingNumber("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.UpdateInvoiceCommand dto = new InvoiceCommand.UpdateInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setCustomerEmail("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setPaymentTerms("test");
        dto.setNotes("test");
        dto.setInternalNotes("test");
        dto.setSalesperson("test");
        dto.setPurchaseOrderNumber("test");
        dto.setCustomerReference("test");
        dto.setTags(Collections.emptyList());
        dto.setShippingMethod("test");
        dto.setTrackingNumber("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.UpdateInvoiceCommand dto = new InvoiceCommand.UpdateInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setCustomerEmail("test");
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setPaymentTerms("test");
        dto.setNotes("test");
        dto.setInternalNotes("test");
        dto.setSalesperson("test");
        dto.setPurchaseOrderNumber("test");
        dto.setCustomerReference("test");
        dto.setTags(Collections.emptyList());
        dto.setShippingMethod("test");
        dto.setTrackingNumber("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}