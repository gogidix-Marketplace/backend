package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.InvoiceResponseDto;
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
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .invoiceNumber("test-invoiceNumber")
            .invoiceType(InvoiceResponseDto.InvoiceTypeDto.STANDARD)
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .salesDate(LocalDate.of(2025,1,15))
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .currency("test-currency")
            .subtotal(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .shippingAmount(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceDue(BigDecimal.TEN)
            .lineItems(Collections.emptyList())
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .shippingAddressLine1("test-shippingAddressLine1")
            .shippingAddressLine2("test-shippingAddressLine2")
            .shippingCity("test-shippingCity")
            .shippingState("test-shippingState")
            .shippingPostalCode("test-shippingPostalCode")
            .shippingCountry("test-shippingCountry")
            .paymentTerms("test-paymentTerms")
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .salesperson("test-salesperson")
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .templateId("test-templateId")
            .taxInclusive(true)
            .taxRegistered(true)
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .discountCode("test-discountCode")
            .discountRate(BigDecimal.TEN)
            .shippingMethod("test-shippingMethod")
            .trackingNumber("test-trackingNumber")
            .recurringInvoiceId("test-recurringInvoiceId")
            .isRecurring(true)
            .parentId("test-parentId")
            .isCreditNote(true)
            .originalInvoiceId("test-originalInvoiceId")
            .lastReminderSent(LocalDate.of(2025,1,15))
            .reminderCount(42)
            .payments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .rejectionReason("test-rejectionReason")
            .currencyExchangeRate("test-currencyExchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .customerReference("test-customerReference")
            .groupId("test-groupId")
            .applyFinanceCharge(true)
            .financeChargeRate(BigDecimal.TEN)
            .financeChargeAppliedDate(LocalDate.of(2025,1,15))
            .daysOverdue(42)
            .writeOffAmount(BigDecimal.TEN)
            .writeOffDate(LocalDate.of(2025,1,15))
            .writeOffReason("test-writeOffReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(InvoiceResponseDto.InvoiceTypeDto.STANDARD, dto.getInvoiceType());
        assertEquals(InvoiceResponseDto.InvoiceStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getInvoiceDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getSalesDate());
        assertEquals("test-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getSubtotal());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getDiscountAmount());
        assertEquals(BigDecimal.TEN, dto.getShippingAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(BigDecimal.TEN, dto.getAmountPaid());
        assertEquals(BigDecimal.TEN, dto.getBalanceDue());
        assertEquals("test-billingAddressLine1", dto.getBillingAddressLine1());
        assertEquals("test-billingAddressLine2", dto.getBillingAddressLine2());
        assertEquals("test-billingCity", dto.getBillingCity());
        assertEquals("test-billingState", dto.getBillingState());
        assertEquals("test-billingPostalCode", dto.getBillingPostalCode());
        assertEquals("test-billingCountry", dto.getBillingCountry());
        assertEquals("test-shippingAddressLine1", dto.getShippingAddressLine1());
        assertEquals("test-shippingAddressLine2", dto.getShippingAddressLine2());
        assertEquals("test-shippingCity", dto.getShippingCity());
        assertEquals("test-shippingState", dto.getShippingState());
        assertEquals("test-shippingPostalCode", dto.getShippingPostalCode());
        assertEquals("test-shippingCountry", dto.getShippingCountry());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-internalNotes", dto.getInternalNotes());
        assertEquals("test-salesperson", dto.getSalesperson());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals("test-departmentId", dto.getDepartmentId());
        assertEquals("test-locationId", dto.getLocationId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertTrue(dto.getTaxInclusive());
        assertTrue(dto.getTaxRegistered());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.TEN, dto.getTaxRate());
        assertEquals("test-discountCode", dto.getDiscountCode());
        assertEquals(BigDecimal.TEN, dto.getDiscountRate());
        assertEquals("test-shippingMethod", dto.getShippingMethod());
        assertEquals("test-trackingNumber", dto.getTrackingNumber());
        assertEquals("test-recurringInvoiceId", dto.getRecurringInvoiceId());
        assertTrue(dto.getIsRecurring());
        assertEquals("test-parentId", dto.getParentId());
        assertTrue(dto.getIsCreditNote());
        assertEquals("test-originalInvoiceId", dto.getOriginalInvoiceId());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastReminderSent());
        assertEquals(42, dto.getReminderCount());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-currencyExchangeRate", dto.getCurrencyExchangeRate());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.TEN, dto.getBaseCurrencyAmount());
        assertEquals("test-customerReference", dto.getCustomerReference());
        assertEquals("test-groupId", dto.getGroupId());
        assertTrue(dto.getApplyFinanceCharge());
        assertEquals(BigDecimal.TEN, dto.getFinanceChargeRate());
        assertEquals(LocalDate.of(2025,1,15), dto.getFinanceChargeAppliedDate());
        assertEquals(42, dto.getDaysOverdue());
        assertEquals(BigDecimal.TEN, dto.getWriteOffAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getWriteOffDate());
        assertEquals("test-writeOffReason", dto.getWriteOffReason());
    }

    @Test
    void testSettersAndGetters() {
        InvoiceResponseDto dto = new InvoiceResponseDto();
        dto.setId("val-id");
        dto.setInvoiceId("val-invoiceId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setInvoiceType(InvoiceResponseDto.InvoiceTypeDto.STANDARD);
        dto.setStatus(InvoiceResponseDto.InvoiceStatusDto.DRAFT);
        dto.setInvoiceDate(LocalDate.of(2025,6,1));
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setSalesDate(LocalDate.of(2025,6,1));
        dto.setPurchaseOrderNumber("val-purchaseOrderNumber");
        dto.setCurrency("val-currency");
        dto.setSubtotal(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setShippingAmount(BigDecimal.ONE);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setAmountPaid(BigDecimal.ONE);
        dto.setBalanceDue(BigDecimal.ONE);
        dto.setBillingAddressLine1("val-billingAddressLine1");
        dto.setBillingAddressLine2("val-billingAddressLine2");
        dto.setBillingCity("val-billingCity");
        dto.setBillingState("val-billingState");
        dto.setBillingPostalCode("val-billingPostalCode");
        dto.setBillingCountry("val-billingCountry");
        dto.setShippingAddressLine1("val-shippingAddressLine1");
        dto.setShippingAddressLine2("val-shippingAddressLine2");
        dto.setShippingCity("val-shippingCity");
        dto.setShippingState("val-shippingState");
        dto.setShippingPostalCode("val-shippingPostalCode");
        dto.setShippingCountry("val-shippingCountry");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setNotes("val-notes");
        dto.setInternalNotes("val-internalNotes");
        dto.setSalesperson("val-salesperson");
        dto.setProjectId("val-projectId");
        dto.setDepartmentId("val-departmentId");
        dto.setLocationId("val-locationId");
        dto.setTemplateId("val-templateId");
        dto.setTaxInclusive(true);
        dto.setTaxRegistered(true);
        dto.setTaxCode("val-taxCode");
        dto.setTaxRate(BigDecimal.ONE);
        dto.setDiscountCode("val-discountCode");
        dto.setDiscountRate(BigDecimal.ONE);
        dto.setShippingMethod("val-shippingMethod");
        dto.setTrackingNumber("val-trackingNumber");
        dto.setRecurringInvoiceId("val-recurringInvoiceId");
        dto.setIsRecurring(true);
        dto.setParentId("val-parentId");
        dto.setIsCreditNote(true);
        dto.setOriginalInvoiceId("val-originalInvoiceId");
        dto.setLastReminderSent(LocalDate.of(2025,6,1));
        dto.setReminderCount(99);
        dto.setApprovedBy("val-approvedBy");
        dto.setRejectionReason("val-rejectionReason");
        dto.setCurrencyExchangeRate("val-currencyExchangeRate");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setBaseCurrencyAmount(BigDecimal.ONE);
        dto.setCustomerReference("val-customerReference");
        dto.setGroupId("val-groupId");
        dto.setApplyFinanceCharge(true);
        dto.setFinanceChargeRate(BigDecimal.ONE);
        dto.setFinanceChargeAppliedDate(LocalDate.of(2025,6,1));
        dto.setDaysOverdue(99);
        dto.setWriteOffAmount(BigDecimal.ONE);
        dto.setWriteOffDate(LocalDate.of(2025,6,1));
        dto.setWriteOffReason("val-writeOffReason");
        assertEquals("val-id", dto.getId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(InvoiceResponseDto.InvoiceTypeDto.STANDARD, dto.getInvoiceType());
        assertEquals(InvoiceResponseDto.InvoiceStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getInvoiceDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getSalesDate());
        assertEquals("val-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getSubtotal());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals(BigDecimal.ONE, dto.getShippingAmount());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals(BigDecimal.ONE, dto.getAmountPaid());
        assertEquals(BigDecimal.ONE, dto.getBalanceDue());
        assertEquals("val-billingAddressLine1", dto.getBillingAddressLine1());
        assertEquals("val-billingAddressLine2", dto.getBillingAddressLine2());
        assertEquals("val-billingCity", dto.getBillingCity());
        assertEquals("val-billingState", dto.getBillingState());
        assertEquals("val-billingPostalCode", dto.getBillingPostalCode());
        assertEquals("val-billingCountry", dto.getBillingCountry());
        assertEquals("val-shippingAddressLine1", dto.getShippingAddressLine1());
        assertEquals("val-shippingAddressLine2", dto.getShippingAddressLine2());
        assertEquals("val-shippingCity", dto.getShippingCity());
        assertEquals("val-shippingState", dto.getShippingState());
        assertEquals("val-shippingPostalCode", dto.getShippingPostalCode());
        assertEquals("val-shippingCountry", dto.getShippingCountry());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-internalNotes", dto.getInternalNotes());
        assertEquals("val-salesperson", dto.getSalesperson());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-departmentId", dto.getDepartmentId());
        assertEquals("val-locationId", dto.getLocationId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertTrue(dto.getTaxInclusive());
        assertTrue(dto.getTaxRegistered());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertEquals("val-discountCode", dto.getDiscountCode());
        assertEquals(BigDecimal.ONE, dto.getDiscountRate());
        assertEquals("val-shippingMethod", dto.getShippingMethod());
        assertEquals("val-trackingNumber", dto.getTrackingNumber());
        assertEquals("val-recurringInvoiceId", dto.getRecurringInvoiceId());
        assertTrue(dto.getIsRecurring());
        assertEquals("val-parentId", dto.getParentId());
        assertTrue(dto.getIsCreditNote());
        assertEquals("val-originalInvoiceId", dto.getOriginalInvoiceId());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastReminderSent());
        assertEquals(99, dto.getReminderCount());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-currencyExchangeRate", dto.getCurrencyExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.ONE, dto.getBaseCurrencyAmount());
        assertEquals("val-customerReference", dto.getCustomerReference());
        assertEquals("val-groupId", dto.getGroupId());
        assertTrue(dto.getApplyFinanceCharge());
        assertEquals(BigDecimal.ONE, dto.getFinanceChargeRate());
        assertEquals(LocalDate.of(2025,6,1), dto.getFinanceChargeAppliedDate());
        assertEquals(99, dto.getDaysOverdue());
        assertEquals(BigDecimal.ONE, dto.getWriteOffAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getWriteOffDate());
        assertEquals("val-writeOffReason", dto.getWriteOffReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceResponseDto dto1 = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .invoiceNumber("test-invoiceNumber")
            .invoiceType(InvoiceResponseDto.InvoiceTypeDto.STANDARD)
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .salesDate(LocalDate.of(2025,1,15))
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .currency("test-currency")
            .subtotal(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .shippingAmount(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceDue(BigDecimal.TEN)
            .lineItems(Collections.emptyList())
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .shippingAddressLine1("test-shippingAddressLine1")
            .shippingAddressLine2("test-shippingAddressLine2")
            .shippingCity("test-shippingCity")
            .shippingState("test-shippingState")
            .shippingPostalCode("test-shippingPostalCode")
            .shippingCountry("test-shippingCountry")
            .paymentTerms("test-paymentTerms")
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .salesperson("test-salesperson")
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .templateId("test-templateId")
            .taxInclusive(true)
            .taxRegistered(true)
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .discountCode("test-discountCode")
            .discountRate(BigDecimal.TEN)
            .shippingMethod("test-shippingMethod")
            .trackingNumber("test-trackingNumber")
            .recurringInvoiceId("test-recurringInvoiceId")
            .isRecurring(true)
            .parentId("test-parentId")
            .isCreditNote(true)
            .originalInvoiceId("test-originalInvoiceId")
            .lastReminderSent(LocalDate.of(2025,1,15))
            .reminderCount(42)
            .payments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .rejectionReason("test-rejectionReason")
            .currencyExchangeRate("test-currencyExchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .customerReference("test-customerReference")
            .groupId("test-groupId")
            .applyFinanceCharge(true)
            .financeChargeRate(BigDecimal.TEN)
            .financeChargeAppliedDate(LocalDate.of(2025,1,15))
            .daysOverdue(42)
            .writeOffAmount(BigDecimal.TEN)
            .writeOffDate(LocalDate.of(2025,1,15))
            .writeOffReason("test-writeOffReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        InvoiceResponseDto dto2 = InvoiceResponseDto.builder()
                        .id("test-id")
            .invoiceId("test-invoiceId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .invoiceNumber("test-invoiceNumber")
            .invoiceType(InvoiceResponseDto.InvoiceTypeDto.STANDARD)
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .salesDate(LocalDate.of(2025,1,15))
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .currency("test-currency")
            .subtotal(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .shippingAmount(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceDue(BigDecimal.TEN)
            .lineItems(Collections.emptyList())
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .shippingAddressLine1("test-shippingAddressLine1")
            .shippingAddressLine2("test-shippingAddressLine2")
            .shippingCity("test-shippingCity")
            .shippingState("test-shippingState")
            .shippingPostalCode("test-shippingPostalCode")
            .shippingCountry("test-shippingCountry")
            .paymentTerms("test-paymentTerms")
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .salesperson("test-salesperson")
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .templateId("test-templateId")
            .taxInclusive(true)
            .taxRegistered(true)
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .discountCode("test-discountCode")
            .discountRate(BigDecimal.TEN)
            .shippingMethod("test-shippingMethod")
            .trackingNumber("test-trackingNumber")
            .recurringInvoiceId("test-recurringInvoiceId")
            .isRecurring(true)
            .parentId("test-parentId")
            .isCreditNote(true)
            .originalInvoiceId("test-originalInvoiceId")
            .lastReminderSent(LocalDate.of(2025,1,15))
            .reminderCount(42)
            .payments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .rejectionReason("test-rejectionReason")
            .currencyExchangeRate("test-currencyExchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .customerReference("test-customerReference")
            .groupId("test-groupId")
            .applyFinanceCharge(true)
            .financeChargeRate(BigDecimal.TEN)
            .financeChargeAppliedDate(LocalDate.of(2025,1,15))
            .daysOverdue(42)
            .writeOffAmount(BigDecimal.TEN)
            .writeOffDate(LocalDate.of(2025,1,15))
            .writeOffReason("test-writeOffReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
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
            .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .invoiceNumber("test-invoiceNumber")
            .invoiceType(InvoiceResponseDto.InvoiceTypeDto.STANDARD)
            .status(InvoiceResponseDto.InvoiceStatusDto.DRAFT)
            .invoiceDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .salesDate(LocalDate.of(2025,1,15))
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .currency("test-currency")
            .subtotal(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .shippingAmount(BigDecimal.TEN)
            .totalAmount(BigDecimal.TEN)
            .amountPaid(BigDecimal.TEN)
            .balanceDue(BigDecimal.TEN)
            .lineItems(Collections.emptyList())
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .shippingAddressLine1("test-shippingAddressLine1")
            .shippingAddressLine2("test-shippingAddressLine2")
            .shippingCity("test-shippingCity")
            .shippingState("test-shippingState")
            .shippingPostalCode("test-shippingPostalCode")
            .shippingCountry("test-shippingCountry")
            .paymentTerms("test-paymentTerms")
            .notes("test-notes")
            .internalNotes("test-internalNotes")
            .salesperson("test-salesperson")
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .templateId("test-templateId")
            .taxInclusive(true)
            .taxRegistered(true)
            .taxCode("test-taxCode")
            .taxRate(BigDecimal.TEN)
            .discountCode("test-discountCode")
            .discountRate(BigDecimal.TEN)
            .shippingMethod("test-shippingMethod")
            .trackingNumber("test-trackingNumber")
            .recurringInvoiceId("test-recurringInvoiceId")
            .isRecurring(true)
            .parentId("test-parentId")
            .isCreditNote(true)
            .originalInvoiceId("test-originalInvoiceId")
            .lastReminderSent(LocalDate.of(2025,1,15))
            .reminderCount(42)
            .payments(Collections.emptyList())
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .approvedBy("test-approvedBy")
            .rejectionReason("test-rejectionReason")
            .currencyExchangeRate("test-currencyExchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .tags(Collections.emptyList())
            .customerReference("test-customerReference")
            .groupId("test-groupId")
            .applyFinanceCharge(true)
            .financeChargeRate(BigDecimal.TEN)
            .financeChargeAppliedDate(LocalDate.of(2025,1,15))
            .daysOverdue(42)
            .writeOffAmount(BigDecimal.TEN)
            .writeOffDate(LocalDate.of(2025,1,15))
            .writeOffReason("test-writeOffReason")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}