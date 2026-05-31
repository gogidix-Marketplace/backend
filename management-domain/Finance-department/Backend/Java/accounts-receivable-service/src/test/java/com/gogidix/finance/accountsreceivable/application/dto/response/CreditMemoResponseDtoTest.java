package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.CreditMemoResponseDto;
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
class CreditMemoResponseDtoTest {

        @Test
    void testBuilder() {
        CreditMemoResponseDto dto = CreditMemoResponseDto.builder()
                        .id("test-id")
            .creditMemoId("test-creditMemoId")
            .tenantId("test-tenantId")
            .creditMemoNumber("test-creditMemoNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .creditMemoType(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN)
            .status(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT)
            .creditMemoDate(LocalDate.of(2025,1,15))
            .referenceInvoiceId("test-referenceInvoiceId")
            .referenceInvoiceNumber("test-referenceInvoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountUsed(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .reason("test-reason")
            .description("test-description")
            .notes("test-notes")
            .salesperson("test-salesperson")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expirationDate(LocalDate.of(2025,1,15))
            .autoApply(true)
            .taxCode("test-taxCode")
            .taxAmount(BigDecimal.TEN)
            .taxInclusive(true)
            .lineItems(Collections.emptyList())
            .applications(Collections.emptyList())
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .customerIdRef("test-customerIdRef")
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .vendorCreditNumber("test-vendorCreditNumber")
            .keepDiscount(true)
            .discountAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-creditMemoId", dto.getCreditMemoId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-creditMemoNumber", dto.getCreditMemoNumber());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN, dto.getCreditMemoType());
        assertEquals(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,1,15), dto.getCreditMemoDate());
        assertEquals("test-referenceInvoiceId", dto.getReferenceInvoiceId());
        assertEquals("test-referenceInvoiceNumber", dto.getReferenceInvoiceNumber());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(BigDecimal.TEN, dto.getAmountUsed());
        assertEquals(BigDecimal.TEN, dto.getBalanceRemaining());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-salesperson", dto.getSalesperson());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpirationDate());
        assertTrue(dto.getAutoApply());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertTrue(dto.getTaxInclusive());
        assertEquals("test-parentId", dto.getParentId());
        assertTrue(dto.getIsReversal());
        assertEquals("test-projectId", dto.getProjectId());
        assertEquals("test-departmentId", dto.getDepartmentId());
        assertEquals("test-locationId", dto.getLocationId());
        assertEquals("test-customerIdRef", dto.getCustomerIdRef());
        assertEquals("test-billingAddressLine1", dto.getBillingAddressLine1());
        assertEquals("test-billingAddressLine2", dto.getBillingAddressLine2());
        assertEquals("test-billingCity", dto.getBillingCity());
        assertEquals("test-billingState", dto.getBillingState());
        assertEquals("test-billingPostalCode", dto.getBillingPostalCode());
        assertEquals("test-billingCountry", dto.getBillingCountry());
        assertEquals("test-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals("test-vendorCreditNumber", dto.getVendorCreditNumber());
        assertTrue(dto.getKeepDiscount());
        assertEquals(BigDecimal.TEN, dto.getDiscountAmount());
        assertEquals("test-exchangeRate", dto.getExchangeRate());
        assertEquals("test-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.TEN, dto.getBaseCurrencyAmount());
    }

    @Test
    void testSettersAndGetters() {
        CreditMemoResponseDto dto = new CreditMemoResponseDto();
        dto.setId("val-id");
        dto.setCreditMemoId("val-creditMemoId");
        dto.setTenantId("val-tenantId");
        dto.setCreditMemoNumber("val-creditMemoNumber");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCreditMemoType(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN);
        dto.setStatus(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT);
        dto.setCreditMemoDate(LocalDate.of(2025,6,1));
        dto.setReferenceInvoiceId("val-referenceInvoiceId");
        dto.setReferenceInvoiceNumber("val-referenceInvoiceNumber");
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setAmountUsed(BigDecimal.ONE);
        dto.setBalanceRemaining(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setReason("val-reason");
        dto.setDescription("val-description");
        dto.setNotes("val-notes");
        dto.setSalesperson("val-salesperson");
        dto.setApprovedBy("val-approvedBy");
        dto.setExpirationDate(LocalDate.of(2025,6,1));
        dto.setAutoApply(true);
        dto.setTaxCode("val-taxCode");
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setTaxInclusive(true);
        dto.setParentId("val-parentId");
        dto.setIsReversal(true);
        dto.setProjectId("val-projectId");
        dto.setDepartmentId("val-departmentId");
        dto.setLocationId("val-locationId");
        dto.setCustomerIdRef("val-customerIdRef");
        dto.setBillingAddressLine1("val-billingAddressLine1");
        dto.setBillingAddressLine2("val-billingAddressLine2");
        dto.setBillingCity("val-billingCity");
        dto.setBillingState("val-billingState");
        dto.setBillingPostalCode("val-billingPostalCode");
        dto.setBillingCountry("val-billingCountry");
        dto.setPurchaseOrderNumber("val-purchaseOrderNumber");
        dto.setVendorCreditNumber("val-vendorCreditNumber");
        dto.setKeepDiscount(true);
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setExchangeRate("val-exchangeRate");
        dto.setBaseCurrency("val-baseCurrency");
        dto.setBaseCurrencyAmount(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-creditMemoId", dto.getCreditMemoId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-creditMemoNumber", dto.getCreditMemoNumber());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN, dto.getCreditMemoType());
        assertEquals(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT, dto.getStatus());
        assertEquals(LocalDate.of(2025,6,1), dto.getCreditMemoDate());
        assertEquals("val-referenceInvoiceId", dto.getReferenceInvoiceId());
        assertEquals("val-referenceInvoiceNumber", dto.getReferenceInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals(BigDecimal.ONE, dto.getAmountUsed());
        assertEquals(BigDecimal.ONE, dto.getBalanceRemaining());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-salesperson", dto.getSalesperson());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpirationDate());
        assertTrue(dto.getAutoApply());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertTrue(dto.getTaxInclusive());
        assertEquals("val-parentId", dto.getParentId());
        assertTrue(dto.getIsReversal());
        assertEquals("val-projectId", dto.getProjectId());
        assertEquals("val-departmentId", dto.getDepartmentId());
        assertEquals("val-locationId", dto.getLocationId());
        assertEquals("val-customerIdRef", dto.getCustomerIdRef());
        assertEquals("val-billingAddressLine1", dto.getBillingAddressLine1());
        assertEquals("val-billingAddressLine2", dto.getBillingAddressLine2());
        assertEquals("val-billingCity", dto.getBillingCity());
        assertEquals("val-billingState", dto.getBillingState());
        assertEquals("val-billingPostalCode", dto.getBillingPostalCode());
        assertEquals("val-billingCountry", dto.getBillingCountry());
        assertEquals("val-purchaseOrderNumber", dto.getPurchaseOrderNumber());
        assertEquals("val-vendorCreditNumber", dto.getVendorCreditNumber());
        assertTrue(dto.getKeepDiscount());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals("val-exchangeRate", dto.getExchangeRate());
        assertEquals("val-baseCurrency", dto.getBaseCurrency());
        assertEquals(BigDecimal.ONE, dto.getBaseCurrencyAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        CreditMemoResponseDto dto1 = CreditMemoResponseDto.builder()
                        .id("test-id")
            .creditMemoId("test-creditMemoId")
            .tenantId("test-tenantId")
            .creditMemoNumber("test-creditMemoNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .creditMemoType(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN)
            .status(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT)
            .creditMemoDate(LocalDate.of(2025,1,15))
            .referenceInvoiceId("test-referenceInvoiceId")
            .referenceInvoiceNumber("test-referenceInvoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountUsed(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .reason("test-reason")
            .description("test-description")
            .notes("test-notes")
            .salesperson("test-salesperson")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expirationDate(LocalDate.of(2025,1,15))
            .autoApply(true)
            .taxCode("test-taxCode")
            .taxAmount(BigDecimal.TEN)
            .taxInclusive(true)
            .lineItems(Collections.emptyList())
            .applications(Collections.emptyList())
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .customerIdRef("test-customerIdRef")
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .vendorCreditNumber("test-vendorCreditNumber")
            .keepDiscount(true)
            .discountAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CreditMemoResponseDto dto2 = CreditMemoResponseDto.builder()
                        .id("test-id")
            .creditMemoId("test-creditMemoId")
            .tenantId("test-tenantId")
            .creditMemoNumber("test-creditMemoNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .creditMemoType(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN)
            .status(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT)
            .creditMemoDate(LocalDate.of(2025,1,15))
            .referenceInvoiceId("test-referenceInvoiceId")
            .referenceInvoiceNumber("test-referenceInvoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountUsed(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .reason("test-reason")
            .description("test-description")
            .notes("test-notes")
            .salesperson("test-salesperson")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expirationDate(LocalDate.of(2025,1,15))
            .autoApply(true)
            .taxCode("test-taxCode")
            .taxAmount(BigDecimal.TEN)
            .taxInclusive(true)
            .lineItems(Collections.emptyList())
            .applications(Collections.emptyList())
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .customerIdRef("test-customerIdRef")
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .vendorCreditNumber("test-vendorCreditNumber")
            .keepDiscount(true)
            .discountAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreditMemoResponseDto dto = CreditMemoResponseDto.builder()
                        .id("test-id")
            .creditMemoId("test-creditMemoId")
            .tenantId("test-tenantId")
            .creditMemoNumber("test-creditMemoNumber")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .creditMemoType(CreditMemoResponseDto.CreditMemoTypeDto.SALES_RETURN)
            .status(CreditMemoResponseDto.CreditMemoStatusDto.DRAFT)
            .creditMemoDate(LocalDate.of(2025,1,15))
            .referenceInvoiceId("test-referenceInvoiceId")
            .referenceInvoiceNumber("test-referenceInvoiceNumber")
            .totalAmount(BigDecimal.TEN)
            .amountUsed(BigDecimal.TEN)
            .balanceRemaining(BigDecimal.TEN)
            .currency("test-currency")
            .reason("test-reason")
            .description("test-description")
            .notes("test-notes")
            .salesperson("test-salesperson")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .expirationDate(LocalDate.of(2025,1,15))
            .autoApply(true)
            .taxCode("test-taxCode")
            .taxAmount(BigDecimal.TEN)
            .taxInclusive(true)
            .lineItems(Collections.emptyList())
            .applications(Collections.emptyList())
            .parentId("test-parentId")
            .isReversal(true)
            .tags(Collections.emptyList())
            .projectId("test-projectId")
            .departmentId("test-departmentId")
            .locationId("test-locationId")
            .sentAt(Instant.parse("2025-01-15T10:00:00Z"))
            .viewedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .customerIdRef("test-customerIdRef")
            .billingAddressLine1("test-billingAddressLine1")
            .billingAddressLine2("test-billingAddressLine2")
            .billingCity("test-billingCity")
            .billingState("test-billingState")
            .billingPostalCode("test-billingPostalCode")
            .billingCountry("test-billingCountry")
            .purchaseOrderNumber("test-purchaseOrderNumber")
            .vendorCreditNumber("test-vendorCreditNumber")
            .keepDiscount(true)
            .discountAmount(BigDecimal.TEN)
            .exchangeRate("test-exchangeRate")
            .baseCurrency("test-baseCurrency")
            .baseCurrencyAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}