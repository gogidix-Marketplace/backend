package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.CustomerResponseDto;
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
class CustomerResponseDtoTest {

        @Test
    void testBuilder() {
        CustomerResponseDto dto = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .customerCode("test-customerCode")
            .customerName("test-customerName")
            .customerType(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL)
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .taxId("test-taxId")
            .taxRegistrationNumber("test-taxRegistrationNumber")
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
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .creditLimit(42)
            .creditDays(42)
            .salesRepresentative("test-salesRepresentative")
            .customerSince(Instant.parse("2025-01-15T10:00:00Z"))
            .status(CustomerResponseDto.CustomerStatusDto.ACTIVE)
            .industry("test-industry")
            .notes("test-notes")
            .defaultPaymentMethod("test-defaultPaymentMethod")
            .allowCredit(true)
            .sendElectronicInvoices(true)
            .invoiceDeliveryEmail("test-invoiceDeliveryEmail")
            .parentCustomerId("test-parentCustomerId")
            .isParentCustomer(true)
            .outstandingBalance(BigDecimal.TEN)
            .creditUsed(BigDecimal.TEN)
            .availableCredit(BigDecimal.TEN)
            .overdueInvoicesCount(42)
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastInvoiceDate(Instant.parse("2025-01-15T10:00:00Z"))
            .totalPurchases(BigDecimal.TEN)
            .totalInvoicesIssued(42)
            .assignedCollector("test-assignedCollector")
            .collectionStage(CustomerResponseDto.CollectionStageDto.CURRENT)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerCode", dto.getCustomerCode());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL, dto.getCustomerType());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-taxId", dto.getTaxId());
        assertEquals("test-taxRegistrationNumber", dto.getTaxRegistrationNumber());
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
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertEquals(42, dto.getCreditLimit());
        assertEquals(42, dto.getCreditDays());
        assertEquals("test-salesRepresentative", dto.getSalesRepresentative());
        assertEquals(CustomerResponseDto.CustomerStatusDto.ACTIVE, dto.getStatus());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-defaultPaymentMethod", dto.getDefaultPaymentMethod());
        assertTrue(dto.getAllowCredit());
        assertTrue(dto.getSendElectronicInvoices());
        assertEquals("test-invoiceDeliveryEmail", dto.getInvoiceDeliveryEmail());
        assertEquals("test-parentCustomerId", dto.getParentCustomerId());
        assertTrue(dto.getIsParentCustomer());
        assertEquals(BigDecimal.TEN, dto.getOutstandingBalance());
        assertEquals(BigDecimal.TEN, dto.getCreditUsed());
        assertEquals(BigDecimal.TEN, dto.getAvailableCredit());
        assertEquals(42, dto.getOverdueInvoicesCount());
        assertEquals(BigDecimal.TEN, dto.getTotalPurchases());
        assertEquals(42, dto.getTotalInvoicesIssued());
        assertEquals("test-assignedCollector", dto.getAssignedCollector());
        assertEquals(CustomerResponseDto.CollectionStageDto.CURRENT, dto.getCollectionStage());
    }

    @Test
    void testSettersAndGetters() {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId("val-id");
        dto.setCustomerId("val-customerId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerCode("val-customerCode");
        dto.setCustomerName("val-customerName");
        dto.setCustomerType(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL);
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setWebsite("val-website");
        dto.setTaxId("val-taxId");
        dto.setTaxRegistrationNumber("val-taxRegistrationNumber");
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
        dto.setCurrency("val-currency");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setCreditLimit(99);
        dto.setCreditDays(99);
        dto.setSalesRepresentative("val-salesRepresentative");
        dto.setStatus(CustomerResponseDto.CustomerStatusDto.ACTIVE);
        dto.setIndustry("val-industry");
        dto.setNotes("val-notes");
        dto.setDefaultPaymentMethod("val-defaultPaymentMethod");
        dto.setAllowCredit(true);
        dto.setSendElectronicInvoices(true);
        dto.setInvoiceDeliveryEmail("val-invoiceDeliveryEmail");
        dto.setParentCustomerId("val-parentCustomerId");
        dto.setIsParentCustomer(true);
        dto.setOutstandingBalance(BigDecimal.ONE);
        dto.setCreditUsed(BigDecimal.ONE);
        dto.setAvailableCredit(BigDecimal.ONE);
        dto.setOverdueInvoicesCount(99);
        dto.setTotalPurchases(BigDecimal.ONE);
        dto.setTotalInvoicesIssued(99);
        dto.setAssignedCollector("val-assignedCollector");
        dto.setCollectionStage(CustomerResponseDto.CollectionStageDto.CURRENT);
        assertEquals("val-id", dto.getId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerCode", dto.getCustomerCode());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL, dto.getCustomerType());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-taxId", dto.getTaxId());
        assertEquals("val-taxRegistrationNumber", dto.getTaxRegistrationNumber());
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
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals(99, dto.getCreditLimit());
        assertEquals(99, dto.getCreditDays());
        assertEquals("val-salesRepresentative", dto.getSalesRepresentative());
        assertEquals(CustomerResponseDto.CustomerStatusDto.ACTIVE, dto.getStatus());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-defaultPaymentMethod", dto.getDefaultPaymentMethod());
        assertTrue(dto.getAllowCredit());
        assertTrue(dto.getSendElectronicInvoices());
        assertEquals("val-invoiceDeliveryEmail", dto.getInvoiceDeliveryEmail());
        assertEquals("val-parentCustomerId", dto.getParentCustomerId());
        assertTrue(dto.getIsParentCustomer());
        assertEquals(BigDecimal.ONE, dto.getOutstandingBalance());
        assertEquals(BigDecimal.ONE, dto.getCreditUsed());
        assertEquals(BigDecimal.ONE, dto.getAvailableCredit());
        assertEquals(99, dto.getOverdueInvoicesCount());
        assertEquals(BigDecimal.ONE, dto.getTotalPurchases());
        assertEquals(99, dto.getTotalInvoicesIssued());
        assertEquals("val-assignedCollector", dto.getAssignedCollector());
        assertEquals(CustomerResponseDto.CollectionStageDto.CURRENT, dto.getCollectionStage());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerResponseDto dto1 = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .customerCode("test-customerCode")
            .customerName("test-customerName")
            .customerType(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL)
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .taxId("test-taxId")
            .taxRegistrationNumber("test-taxRegistrationNumber")
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
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .creditLimit(42)
            .creditDays(42)
            .salesRepresentative("test-salesRepresentative")
            .customerSince(Instant.parse("2025-01-15T10:00:00Z"))
            .status(CustomerResponseDto.CustomerStatusDto.ACTIVE)
            .industry("test-industry")
            .notes("test-notes")
            .defaultPaymentMethod("test-defaultPaymentMethod")
            .allowCredit(true)
            .sendElectronicInvoices(true)
            .invoiceDeliveryEmail("test-invoiceDeliveryEmail")
            .parentCustomerId("test-parentCustomerId")
            .isParentCustomer(true)
            .outstandingBalance(BigDecimal.TEN)
            .creditUsed(BigDecimal.TEN)
            .availableCredit(BigDecimal.TEN)
            .overdueInvoicesCount(42)
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastInvoiceDate(Instant.parse("2025-01-15T10:00:00Z"))
            .totalPurchases(BigDecimal.TEN)
            .totalInvoicesIssued(42)
            .assignedCollector("test-assignedCollector")
            .collectionStage(CustomerResponseDto.CollectionStageDto.CURRENT)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CustomerResponseDto dto2 = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .customerCode("test-customerCode")
            .customerName("test-customerName")
            .customerType(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL)
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .taxId("test-taxId")
            .taxRegistrationNumber("test-taxRegistrationNumber")
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
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .creditLimit(42)
            .creditDays(42)
            .salesRepresentative("test-salesRepresentative")
            .customerSince(Instant.parse("2025-01-15T10:00:00Z"))
            .status(CustomerResponseDto.CustomerStatusDto.ACTIVE)
            .industry("test-industry")
            .notes("test-notes")
            .defaultPaymentMethod("test-defaultPaymentMethod")
            .allowCredit(true)
            .sendElectronicInvoices(true)
            .invoiceDeliveryEmail("test-invoiceDeliveryEmail")
            .parentCustomerId("test-parentCustomerId")
            .isParentCustomer(true)
            .outstandingBalance(BigDecimal.TEN)
            .creditUsed(BigDecimal.TEN)
            .availableCredit(BigDecimal.TEN)
            .overdueInvoicesCount(42)
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastInvoiceDate(Instant.parse("2025-01-15T10:00:00Z"))
            .totalPurchases(BigDecimal.TEN)
            .totalInvoicesIssued(42)
            .assignedCollector("test-assignedCollector")
            .collectionStage(CustomerResponseDto.CollectionStageDto.CURRENT)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CustomerResponseDto dto = CustomerResponseDto.builder()
                        .id("test-id")
            .customerId("test-customerId")
            .tenantId("test-tenantId")
            .customerCode("test-customerCode")
            .customerName("test-customerName")
            .customerType(CustomerResponseDto.CustomerTypeDto.INDIVIDUAL)
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .taxId("test-taxId")
            .taxRegistrationNumber("test-taxRegistrationNumber")
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
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .creditLimit(42)
            .creditDays(42)
            .salesRepresentative("test-salesRepresentative")
            .customerSince(Instant.parse("2025-01-15T10:00:00Z"))
            .status(CustomerResponseDto.CustomerStatusDto.ACTIVE)
            .industry("test-industry")
            .notes("test-notes")
            .defaultPaymentMethod("test-defaultPaymentMethod")
            .allowCredit(true)
            .sendElectronicInvoices(true)
            .invoiceDeliveryEmail("test-invoiceDeliveryEmail")
            .parentCustomerId("test-parentCustomerId")
            .isParentCustomer(true)
            .outstandingBalance(BigDecimal.TEN)
            .creditUsed(BigDecimal.TEN)
            .availableCredit(BigDecimal.TEN)
            .overdueInvoicesCount(42)
            .lastPaymentDate(Instant.parse("2025-01-15T10:00:00Z"))
            .lastInvoiceDate(Instant.parse("2025-01-15T10:00:00Z"))
            .totalPurchases(BigDecimal.TEN)
            .totalInvoicesIssued(42)
            .assignedCollector("test-assignedCollector")
            .collectionStage(CustomerResponseDto.CollectionStageDto.CURRENT)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}