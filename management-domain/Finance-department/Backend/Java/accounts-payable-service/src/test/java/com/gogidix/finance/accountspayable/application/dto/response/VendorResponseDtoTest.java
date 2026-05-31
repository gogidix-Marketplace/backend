package com.gogidix.finance.accountspayable.application.dto.response;

import com.gogidix.finance.accountspayable.application.dto.response.VendorResponseDto;
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
class VendorResponseDtoTest {

        @Test
    void testBuilder() {
        VendorResponseDto dto = VendorResponseDto.builder()
                        .id("test-id")
            .vendorId("test-vendorId")
            .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(VendorResponseDto.VendorTypeDto.INDIVIDUAL)
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
            .status(VendorResponseDto.VendorStatusDto.ACTIVE)
            .createdBy("test-createdBy")
            .activatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivationReason("test-deactivationReason")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .isPreferredVendor(true)
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-vendorId", dto.getVendorId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-vendorCode", dto.getVendorCode());
        assertEquals("test-vendorName", dto.getVendorName());
        assertEquals(VendorResponseDto.VendorTypeDto.INDIVIDUAL, dto.getVendorType());
        assertEquals("test-taxId", dto.getTaxId());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-paymentTerms", dto.getPaymentTerms());
        assertEquals(42, dto.getPaymentDays());
        assertEquals("test-contactPerson", dto.getContactPerson());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-website", dto.getWebsite());
        assertEquals(VendorResponseDto.VendorStatusDto.ACTIVE, dto.getStatus());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-deactivationReason", dto.getDeactivationReason());
        assertEquals("test-bankName", dto.getBankName());
        assertEquals("test-bankAccountType", dto.getBankAccountType());
        assertEquals(LocalDate.of(2025,1,15), dto.getCreditLimit());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-parentVendorId", dto.getParentVendorId());
        assertTrue(dto.getIsPreferredVendor());
        assertEquals(LocalDate.of(2025,1,15), dto.getValidFrom());
        assertEquals(LocalDate.of(2025,1,15), dto.getValidUntil());
    }

    @Test
    void testSettersAndGetters() {
        VendorResponseDto dto = new VendorResponseDto();
        dto.setId("val-id");
        dto.setVendorId("val-vendorId");
        dto.setTenantId("val-tenantId");
        dto.setVendorCode("val-vendorCode");
        dto.setVendorName("val-vendorName");
        dto.setVendorType(VendorResponseDto.VendorTypeDto.INDIVIDUAL);
        dto.setTaxId("val-taxId");
        dto.setCurrency("val-currency");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setPaymentDays(99);
        dto.setContactPerson("val-contactPerson");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setWebsite("val-website");
        dto.setStatus(VendorResponseDto.VendorStatusDto.ACTIVE);
        dto.setCreatedBy("val-createdBy");
        dto.setDeactivationReason("val-deactivationReason");
        dto.setBankName("val-bankName");
        dto.setBankAccountType("val-bankAccountType");
        dto.setCreditLimit(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setParentVendorId("val-parentVendorId");
        dto.setIsPreferredVendor(true);
        dto.setValidFrom(LocalDate.of(2025,6,1));
        dto.setValidUntil(LocalDate.of(2025,6,1));
        assertEquals("val-id", dto.getId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorCode", dto.getVendorCode());
        assertEquals("val-vendorName", dto.getVendorName());
        assertEquals(VendorResponseDto.VendorTypeDto.INDIVIDUAL, dto.getVendorType());
        assertEquals("val-taxId", dto.getTaxId());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals(99, dto.getPaymentDays());
        assertEquals("val-contactPerson", dto.getContactPerson());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-website", dto.getWebsite());
        assertEquals(VendorResponseDto.VendorStatusDto.ACTIVE, dto.getStatus());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-deactivationReason", dto.getDeactivationReason());
        assertEquals("val-bankName", dto.getBankName());
        assertEquals("val-bankAccountType", dto.getBankAccountType());
        assertEquals(LocalDate.of(2025,6,1), dto.getCreditLimit());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-parentVendorId", dto.getParentVendorId());
        assertTrue(dto.getIsPreferredVendor());
        assertEquals(LocalDate.of(2025,6,1), dto.getValidFrom());
        assertEquals(LocalDate.of(2025,6,1), dto.getValidUntil());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorResponseDto dto1 = VendorResponseDto.builder()
                        .id("test-id")
            .vendorId("test-vendorId")
            .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(VendorResponseDto.VendorTypeDto.INDIVIDUAL)
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
            .status(VendorResponseDto.VendorStatusDto.ACTIVE)
            .createdBy("test-createdBy")
            .activatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivationReason("test-deactivationReason")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .isPreferredVendor(true)
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        VendorResponseDto dto2 = VendorResponseDto.builder()
                        .id("test-id")
            .vendorId("test-vendorId")
            .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(VendorResponseDto.VendorTypeDto.INDIVIDUAL)
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
            .status(VendorResponseDto.VendorStatusDto.ACTIVE)
            .createdBy("test-createdBy")
            .activatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivationReason("test-deactivationReason")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .isPreferredVendor(true)
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        VendorResponseDto dto = VendorResponseDto.builder()
                        .id("test-id")
            .vendorId("test-vendorId")
            .tenantId("test-tenantId")
            .vendorCode("test-vendorCode")
            .vendorName("test-vendorName")
            .vendorType(VendorResponseDto.VendorTypeDto.INDIVIDUAL)
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
            .status(VendorResponseDto.VendorStatusDto.ACTIVE)
            .createdBy("test-createdBy")
            .activatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .deactivationReason("test-deactivationReason")
            .bankName("test-bankName")
            .bankAccountType("test-bankAccountType")
            .creditLimit(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .tags(Collections.emptyList())
            .parentVendorId("test-parentVendorId")
            .isPreferredVendor(true)
            .discountPercentage(null)
            .validFrom(LocalDate.of(2025,1,15))
            .validUntil(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}