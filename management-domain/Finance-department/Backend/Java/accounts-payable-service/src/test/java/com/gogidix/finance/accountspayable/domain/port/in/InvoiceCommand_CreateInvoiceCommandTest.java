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
class InvoiceCommand_CreateInvoiceCommandTest {

        @Test
    void testSettersAndGetters() {
        InvoiceCommand.CreateInvoiceCommand dto = new InvoiceCommand.CreateInvoiceCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setPurchaseOrderNumber("val-purchaseOrderNumber");
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setInternalReference("val-internalReference");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setRequiresApproval(true);
        dto.setGlAccount("val-glAccount");
        dto.setTaxCode("val-taxCode");
        dto.setTaxRate(BigDecimal.ONE);
        dto.setTaxIncluded(true);
        dto.setDiscountValidUntil(LocalDate.of(2025,6,1));
        dto.setDiscountPercentage(BigDecimal.ONE);
        dto.setPaymentTerms("val-paymentTerms");
        dto.setSubmittedBy("val-submittedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals("val-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalReference", dto.getInternalReference());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertTrue(dto.getRequiresApproval());
        assertEquals("val-glAccount", dto.getGlAccount());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertTrue(dto.getTaxIncluded());
        assertEquals(LocalDate.of(2025,6,1), dto.getDiscountValidUntil());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceCommand.CreateInvoiceCommand dto1 = new InvoiceCommand.CreateInvoiceCommand();
        InvoiceCommand.CreateInvoiceCommand dto2 = new InvoiceCommand.CreateInvoiceCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setVendorName("test");
        dto1.setInvoiceNumber("test");
        dto1.setPurchaseOrderNumber("test");
        dto1.setInvoiceDate(LocalDate.of(2025,1,1));
        dto1.setDueDate(LocalDate.of(2025,1,1));
        dto1.setAmount(BigDecimal.TEN);
        dto1.setCurrency("test");
        dto1.setDescription("test");
        dto1.setNotes("test");
        dto1.setInternalReference("test");
        dto1.setDepartment("test");
        dto1.setCostCenter("test");
        dto1.setProjectId("test");
        dto1.setLineItems(Collections.emptyList());
        dto1.setAttachments(Collections.emptyList());
        dto1.setTags(Collections.emptyList());
        dto1.setRequiresApproval(true);
        dto1.setGlAccount("test");
        dto1.setTaxCode("test");
        dto1.setTaxRate(BigDecimal.TEN);
        dto1.setTaxIncluded(true);
        dto1.setDiscountValidUntil(LocalDate.of(2025,1,1));
        dto1.setDiscountPercentage(BigDecimal.TEN);
        dto1.setPaymentTerms("test");
        dto1.setSubmittedBy("test");
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setVendorName("test");
        dto2.setInvoiceNumber("test");
        dto2.setPurchaseOrderNumber("test");
        dto2.setInvoiceDate(LocalDate.of(2025,1,1));
        dto2.setDueDate(LocalDate.of(2025,1,1));
        dto2.setAmount(BigDecimal.TEN);
        dto2.setCurrency("test");
        dto2.setDescription("test");
        dto2.setNotes("test");
        dto2.setInternalReference("test");
        dto2.setDepartment("test");
        dto2.setCostCenter("test");
        dto2.setProjectId("test");
        dto2.setLineItems(Collections.emptyList());
        dto2.setAttachments(Collections.emptyList());
        dto2.setTags(Collections.emptyList());
        dto2.setRequiresApproval(true);
        dto2.setGlAccount("test");
        dto2.setTaxCode("test");
        dto2.setTaxRate(BigDecimal.TEN);
        dto2.setTaxIncluded(true);
        dto2.setDiscountValidUntil(LocalDate.of(2025,1,1));
        dto2.setDiscountPercentage(BigDecimal.TEN);
        dto2.setPaymentTerms("test");
        dto2.setSubmittedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InvoiceCommand.CreateInvoiceCommand dto = new InvoiceCommand.CreateInvoiceCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setVendorName("test");
        dto.setInvoiceNumber("test");
        dto.setPurchaseOrderNumber("test");
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setInternalReference("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setLineItems(Collections.emptyList());
        dto.setAttachments(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setRequiresApproval(true);
        dto.setGlAccount("test");
        dto.setTaxCode("test");
        dto.setTaxRate(BigDecimal.TEN);
        dto.setTaxIncluded(true);
        dto.setDiscountValidUntil(LocalDate.of(2025,1,1));
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setPaymentTerms("test");
        dto.setSubmittedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InvoiceCommand.CreateInvoiceCommand dto = new InvoiceCommand.CreateInvoiceCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setVendorName("test");
        dto.setInvoiceNumber("test");
        dto.setPurchaseOrderNumber("test");
        dto.setInvoiceDate(LocalDate.of(2025,1,1));
        dto.setDueDate(LocalDate.of(2025,1,1));
        dto.setAmount(BigDecimal.TEN);
        dto.setCurrency("test");
        dto.setDescription("test");
        dto.setNotes("test");
        dto.setInternalReference("test");
        dto.setDepartment("test");
        dto.setCostCenter("test");
        dto.setProjectId("test");
        dto.setLineItems(Collections.emptyList());
        dto.setAttachments(Collections.emptyList());
        dto.setTags(Collections.emptyList());
        dto.setRequiresApproval(true);
        dto.setGlAccount("test");
        dto.setTaxCode("test");
        dto.setTaxRate(BigDecimal.TEN);
        dto.setTaxIncluded(true);
        dto.setDiscountValidUntil(LocalDate.of(2025,1,1));
        dto.setDiscountPercentage(BigDecimal.TEN);
        dto.setPaymentTerms("test");
        dto.setSubmittedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}