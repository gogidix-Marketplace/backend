package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.port.in.VendorCommand;
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
class VendorCommand_CreateVendorCommandTest {

        @Test
    void testBuilder() {
        VendorCommand.CreateVendorCommand dto = VendorCommand.CreateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(Vendor.VendorType.INDIVIDUAL)
            .taxId("test-taxId")
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .paymentDays(42)
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .billingAddress(null)
            .shippingAddress(null)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .createdBy("test-createdBy")
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorCode", dto.getVendorCode());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals("test-taxId", dto.getTaxId());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertEquals(42, dto.getPaymentDays());
        assertEquals("test-contactPerson", dto.getContactPerson());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("test-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("test-bankName", dto.getBankName());
        assertEquals("test-bankAccountType", dto.getBankAccountType());
        assertEquals(LocalDate.of(2025,1,15), dto.getCreditLimit());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-parentVendorId", dto.getParentVendorId());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getValidFrom());
        assertEquals(LocalDate.of(2025,1,15), dto.getValidUntil());
    }

    @Test
    void testSettersAndGetters() {
        VendorCommand.CreateVendorCommand dto = new VendorCommand.CreateVendorCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorCode("val-vendorCode");
        dto.setVendorName("val-vendorName");
        dto.setTaxId("val-taxId");
        dto.setCurrency("val-currency");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setPaymentDays(99);
        dto.setContactPerson("val-contactPerson");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setWebsite("val-website");
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setBankName("val-bankName");
        dto.setBankAccountType("val-bankAccountType");
        dto.setCreditLimit(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setParentVendorId("val-parentVendorId");
        dto.setCreatedBy("val-createdBy");
        dto.setValidFrom(LocalDate.of(2025,6,1));
        dto.setValidUntil(LocalDate.of(2025,6,1));
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorCode", dto.getVendorCode());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals("val-taxId", dto.getTaxId());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals(99, dto.getPaymentDays());
        assertEquals("val-contactPerson", dto.getContactPerson());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-bankName", dto.getBankName());
        assertEquals("val-bankAccountType", dto.getBankAccountType());
        assertEquals(LocalDate.of(2025,6,1), dto.getCreditLimit());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-parentVendorId", dto.getParentVendorId());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getValidFrom());
        assertEquals(LocalDate.of(2025,6,1), dto.getValidUntil());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.CreateVendorCommand dto1 = VendorCommand.CreateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(Vendor.VendorType.INDIVIDUAL)
            .taxId("test-taxId")
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .paymentDays(42)
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .billingAddress(null)
            .shippingAddress(null)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .createdBy("test-createdBy")
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .build();
        VendorCommand.CreateVendorCommand dto2 = VendorCommand.CreateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(Vendor.VendorType.INDIVIDUAL)
            .taxId("test-taxId")
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .paymentDays(42)
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .billingAddress(null)
            .shippingAddress(null)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .createdBy("test-createdBy")
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        VendorCommand.CreateVendorCommand dto = VendorCommand.CreateVendorCommand.builder()
                        .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(Vendor.VendorType.INDIVIDUAL)
            .taxId("test-taxId")
            .currency("test-currency")
            .paymentTerms("test-paymentTerms")
            .paymentDays(42)
            .contactPerson("test-contactPerson")
            .email("test-email")
            .phone("test-phone")
            .website("test-website")
            .billingAddress(null)
            .shippingAddress(null)
            .bankAccountNumber("test-bankAccountNumber")
            .bankRoutingNumber("test-bankRoutingNumber")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .createdBy("test-createdBy")
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}