package com.gogidix.customersupport.customerportal.application.dto;

import com.gogidix.customersupport.customerportal.application.dto.CustomerProfileResponseDto;
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
class CustomerProfileResponseDtoTest {

        @Test
    void testBuilder() {
        CustomerProfileResponseDto dto = CustomerProfileResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .userId("test-userId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .secondaryPhone("test-secondaryPhone")
            .companyName("test-companyName")
            .companyId("test-companyId")
            .customerType("test-customerType")
            .tier("test-tier")
            .preferredLanguage("test-preferredLanguage")
            .timezone("test-timezone")
            .country("test-country")
            .address(null)
            .preferences(null)
            .communicationChannels(Collections.emptyList())
            .tags(Collections.emptyList())
            .customFields(Collections.emptyMap())
            .isActive(true)
            .lastLoginAt(Instant.parse("2025-01-15T10:00:00Z"))
            .accountCreatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-secondaryPhone", dto.getSecondaryPhone());
        assertEquals("test-companyName", dto.getCompanyName());
        assertEquals("test-companyId", dto.getCompanyId());
        assertEquals("test-customerType", dto.getCustomerType());
        assertEquals("test-tier", dto.getTier());
        assertEquals("test-preferredLanguage", dto.getPreferredLanguage());
        assertEquals("test-timezone", dto.getTimezone());
        assertEquals("test-country", dto.getCountry());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        CustomerProfileResponseDto dto = new CustomerProfileResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setUserId("val-userId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setSecondaryPhone("val-secondaryPhone");
        dto.setCompanyName("val-companyName");
        dto.setCompanyId("val-companyId");
        dto.setCustomerType("val-customerType");
        dto.setTier("val-tier");
        dto.setPreferredLanguage("val-preferredLanguage");
        dto.setTimezone("val-timezone");
        dto.setCountry("val-country");
        dto.setIsActive(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-secondaryPhone", dto.getSecondaryPhone());
        assertEquals("val-companyName", dto.getCompanyName());
        assertEquals("val-companyId", dto.getCompanyId());
        assertEquals("val-customerType", dto.getCustomerType());
        assertEquals("val-tier", dto.getTier());
        assertEquals("val-preferredLanguage", dto.getPreferredLanguage());
        assertEquals("val-timezone", dto.getTimezone());
        assertEquals("val-country", dto.getCountry());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        CustomerProfileResponseDto dto1 = CustomerProfileResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .userId("test-userId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .secondaryPhone("test-secondaryPhone")
            .companyName("test-companyName")
            .companyId("test-companyId")
            .customerType("test-customerType")
            .tier("test-tier")
            .preferredLanguage("test-preferredLanguage")
            .timezone("test-timezone")
            .country("test-country")
            .address(null)
            .preferences(null)
            .communicationChannels(Collections.emptyList())
            .tags(Collections.emptyList())
            .customFields(Collections.emptyMap())
            .isActive(true)
            .lastLoginAt(Instant.parse("2025-01-15T10:00:00Z"))
            .accountCreatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CustomerProfileResponseDto dto2 = CustomerProfileResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .userId("test-userId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .secondaryPhone("test-secondaryPhone")
            .companyName("test-companyName")
            .companyId("test-companyId")
            .customerType("test-customerType")
            .tier("test-tier")
            .preferredLanguage("test-preferredLanguage")
            .timezone("test-timezone")
            .country("test-country")
            .address(null)
            .preferences(null)
            .communicationChannels(Collections.emptyList())
            .tags(Collections.emptyList())
            .customFields(Collections.emptyMap())
            .isActive(true)
            .lastLoginAt(Instant.parse("2025-01-15T10:00:00Z"))
            .accountCreatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CustomerProfileResponseDto dto = CustomerProfileResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .userId("test-userId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .secondaryPhone("test-secondaryPhone")
            .companyName("test-companyName")
            .companyId("test-companyId")
            .customerType("test-customerType")
            .tier("test-tier")
            .preferredLanguage("test-preferredLanguage")
            .timezone("test-timezone")
            .country("test-country")
            .address(null)
            .preferences(null)
            .communicationChannels(Collections.emptyList())
            .tags(Collections.emptyList())
            .customFields(Collections.emptyMap())
            .isActive(true)
            .lastLoginAt(Instant.parse("2025-01-15T10:00:00Z"))
            .accountCreatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}