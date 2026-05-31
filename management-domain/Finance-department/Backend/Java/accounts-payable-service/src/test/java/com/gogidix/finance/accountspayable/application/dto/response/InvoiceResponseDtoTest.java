package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.InvoiceResponseDto;
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
class InvoiceResponseDtoTest {

        @Test
    void testBuilder() {
        InvoiceResponseDto dto = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorCode("test-vendorCode")
            .invoiceNumber("test-invoiceNumber")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .receivedDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .currency("test-currency")
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .submittedBy("test-submittedBy")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .paymentReference("test-paymentReference")
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .notes("test-notes")
            .internalReference("test-internalReference")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .lineItems(Collections.emptyList())
            .attachments(Collections.emptyList())
            .tags(Collections.emptyList())
            .requiresApproval(true)
            .approvalLevel(InvoiceResponseDto.ApprovalLevelDto.NONE)
            .glAccount("test-glAccount")
            .taxCode("test-taxCode")
            .taxIncluded(true)
            .discountValidUntil(LocalDate.of(2025,1,15))
            .discountPercentage(BigDecimal.TEN)
            .paymentTerms("test-paymentTerms")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .daysUntilDue(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-vendorCode", dto.getVendorCode());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals("test-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals(LocalDate.of(2025,1,15), dto.getInvoiceDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getReceivedDate());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getDiscountAmount());
        assertEquals(BigDecimal.TEN, dto.getNetAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(InvoiceResponseDto.InvoiceStatusDto.DRAFT, dto.getStatus());
        assertEquals("test-submittedBy", dto.getSubmittedBy());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-paymentReference", dto.getPaymentReference());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-internalReference", dto.getInternalReference());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-costCenter", dto.getCostCenter());
        assertEquals("test-projectId", dto.getProjectId());
        assertTrue(dto.getRequiresApproval());
        assertEquals(InvoiceResponseDto.ApprovalLevelDto.NONE, dto.getApprovalLevel());
        assertEquals("test-glAccount", dto.getGlAccount());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertTrue(dto.getTaxIncluded());
        assertEquals(LocalDate.of(2025,1,15), dto.getDiscountValidUntil());
        assertEquals(BigDecimal.TEN, dto.getDiscountPercentage());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertTrue(dto.getIsOverdue());
        assertEquals(42L, dto.getDaysUntilDue());
    }

    @Test
    void testSettersAndGetters() {
        InvoiceResponseDto dto = new InvoiceResponseDto();
        dto.setId("val-id");
        dto.setInvoiceId("val-invoiceId");
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setVendorName("val-vendorName");
        dto.setVendorCode("val-vendorCode");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setPurchaseOrderNumber("val-purchaseOrderNumber");
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setReceivedDate(LocalDate.of(2025,6,1));
        dto.setAmount(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setNetAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setStatus(InvoiceResponseDto.InvoiceStatusDto.DRAFT);
        dto.setSubmittedBy("val-submittedBy");
        dto.setApprovedBy("val-approvedBy");
        dto.setRejectionReason("val-rejectionReason");
        dto.setPaymentReference("val-paymentReference");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setInternalReference("val-internalReference");
        dto.setDepartment("val-department");
        dto.setCostCenter("val-costCenter");
        dto.setProjectId("val-projectId");
        dto.setRequiresApproval(true);
        dto.setApprovalLevel(InvoiceResponseDto.ApprovalLevelDto.NONE);
        dto.setGlAccount("val-glAccount");
        dto.setTaxCode("val-taxCode");
        dto.setTaxIncluded(true);
        dto.setDiscountValidUntil(LocalDate.of(2025,6,1));
        dto.setDiscountPercentage(BigDecimal.ONE);
        dto.setPaymentTerms("val-paymentTerms");
        dto.setIsOverdue(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-vendorCode", dto.getVendorCode());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals("val-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getReceivedDate());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals(BigDecimal.ONE, dto.getNetAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(InvoiceResponseDto.InvoiceStatusDto.DRAFT, dto.getStatus());
        assertEquals("val-submittedBy", dto.getSubmittedBy());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-paymentReference", dto.getPaymentReference());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalReference", dto.getInternalReference());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-costCenter", dto.getCostCenter());
        assertEquals("val-projectId", dto.getProjectId());
        assertTrue(dto.getRequiresApproval());
        assertEquals(InvoiceResponseDto.ApprovalLevelDto.NONE, dto.getApprovalLevel());
        assertEquals("val-glAccount", dto.getGlAccount());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertTrue(dto.getTaxIncluded());
        assertEquals(LocalDate.of(2025,6,1), dto.getDiscountValidUntil());
        assertEquals(BigDecimal.ONE, dto.getDiscountPercentage());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertTrue(dto.getIsOverdue());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceResponseDto dto1 = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorCode("test-vendorCode")
            .invoiceNumber("test-invoiceNumber")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .receivedDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .currency("test-currency")
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .submittedBy("test-submittedBy")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .paymentReference("test-paymentReference")
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .notes("test-notes")
            .internalReference("test-internalReference")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .lineItems(Collections.emptyList())
            .attachments(Collections.emptyList())
            .tags(Collections.emptyList())
            .requiresApproval(true)
            .approvalLevel(InvoiceResponseDto.ApprovalLevelDto.NONE)
            .glAccount("test-glAccount")
            .taxCode("test-taxCode")
            .taxIncluded(true)
            .discountValidUntil(LocalDate.of(2025,1,15))
            .discountPercentage(BigDecimal.TEN)
            .paymentTerms("test-paymentTerms")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .daysUntilDue(42L)
            .build();
        InvoiceResponseDto dto2 = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorCode("test-vendorCode")
            .invoiceNumber("test-invoiceNumber")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .receivedDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .currency("test-currency")
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .submittedBy("test-submittedBy")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .paymentReference("test-paymentReference")
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .notes("test-notes")
            .internalReference("test-internalReference")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .lineItems(Collections.emptyList())
            .attachments(Collections.emptyList())
            .tags(Collections.emptyList())
            .requiresApproval(true)
            .approvalLevel(InvoiceResponseDto.ApprovalLevelDto.NONE)
            .glAccount("test-glAccount")
            .taxCode("test-taxCode")
            .taxIncluded(true)
            .discountValidUntil(LocalDate.of(2025,1,15))
            .discountPercentage(BigDecimal.TEN)
            .paymentTerms("test-paymentTerms")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .daysUntilDue(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        InvoiceResponseDto dto = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .vendorId("test-vendorId")
            .vendorName("test-vendorName")
            .vendorCode("test-vendorCode")
            .invoiceNumber("test-invoiceNumber")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .receivedDate(LocalDate.of(2025,1,15))
            .amount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .netAmount(BigDecimal.TEN)
            .currency("test-currency")
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .submittedBy("test-submittedBy")
            .submittedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .rejectionReason("test-rejectionReason")
            .paymentReference("test-paymentReference")
            .paidAt(Instant.parse("2025-01-15T10:00:00Z"))
            .description("test-description")
            .notes("test-notes")
            .internalReference("test-internalReference")
            .department("test-department")
            .costCenter("test-costCenter")
            .projectId("test-projectId")
            .lineItems(Collections.emptyList())
            .attachments(Collections.emptyList())
            .tags(Collections.emptyList())
            .requiresApproval(true)
            .approvalLevel(InvoiceResponseDto.ApprovalLevelDto.NONE)
            .glAccount("test-glAccount")
            .taxCode("test-taxCode")
            .taxIncluded(true)
            .discountValidUntil(LocalDate.of(2025,1,15))
            .discountPercentage(BigDecimal.TEN)
            .paymentTerms("test-paymentTerms")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .daysUntilDue(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}