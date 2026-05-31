package com.gogidix.finance.accountspayable.domain.port.in;

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
class InvoiceCommand_UpdateInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.UpdateInvoiceCommand dto = new InvoiceCommand.UpdateInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setInvoiceId("val-invoiceId");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setInternalReference("val-internalReference");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setTaxRate(BigDecimal.ONE);
        dto.setDiscountPercentage(BigDecimal.ONE);
        dto.setDiscountValidUntil(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalReference", dto.getInternalReference());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
        assertEquals(LocalDate.of(2025,6,1), dto.getDiscountValidUntil());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.UpdateInvoiceCommand dto1 = new InvoiceCommand.UpdateInvoiceCommand();
        InvoiceCommand.UpdateInvoiceCommand dto2 = new InvoiceCommand.UpdateInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setInvoiceId("test");
        dto1.setDescription("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setNotes("test");
        dto1.setInternalReference("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setProjectId("test");
        dto1.setTags(Collections.emptyList());
        dto1.setAttachments(Collections.emptyList());
        dto1.setTaxRate(BigDecimal.TEN);
        dto1.setDiscountPercentage(BigDecimal.TEN);
        dto1.setDiscountValidUntil(LocalDate.of(2025,1,1));
        dto2.setTenantId("test");
        dto2.setInvoiceId("test");
        dto2.setDescription("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setNotes("test");
        dto2.setInternalReference("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setProjectId("test");
        dto2.setTags(Collections.emptyList());
        dto2.setAttachments(Collections.emptyList());
        dto2.setTaxRate(BigDecimal.TEN);
        dto2.setDiscountPercentage(BigDecimal.TEN);
        dto2.setDiscountValidUntil(LocalDate.of(2025,1,1));
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
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setInternalReference("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setTags(Collections.emptyList());
        dto.setAttachments(Collections.emptyList());
        dto.setTaxRate(BigDecimal.TEN);
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setDiscountValidUntil(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.UpdateInvoiceCommand dto = new InvoiceCommand.UpdateInvoiceCommand();
        dto.setTenantId("test");
        dto.setInvoiceId("test");
        dto.setDescription("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setInternalReference("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setTags(Collections.emptyList());
        dto.setAttachments(Collections.emptyList());
        dto.setTaxRate(BigDecimal.TEN);
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setDiscountValidUntil(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}