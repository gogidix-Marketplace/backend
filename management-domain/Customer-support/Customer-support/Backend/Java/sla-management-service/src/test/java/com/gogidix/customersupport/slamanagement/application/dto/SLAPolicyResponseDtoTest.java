package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
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
class SLAPolicyResponseDtoTest {

        @Test
    void testBuilder() {
        SLAPolicyResponseDto dto = SLAPolicyResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .policyName("test-policyName")
            .policyCode("test-policyCode")
            .description("test-description")
            .isActive(true)
            .responseTimeTargetMinutes(42)
            .resolutionTimeTargetMinutes(42)
            .businessHoursOnly(true)
            .businessHoursConfig(null)
            .applicableCategories(Collections.emptyList())
            .applicablePriorities(Collections.emptyList())
            .applicableChannels(Collections.emptyList())
            .escalationRules(Collections.emptyList())
            .timezone("test-timezone")
            .gracePeriodMinutes(42)
            .penaltyConfig(null)
            .notificationConfig(null)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-policyName", dto.getPolicyName());
        assertEquals("test-policyCode", dto.getPolicyCode());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsActive());
        assertEquals(42, dto.getResponseTimeTargetMinutes());
        assertEquals(42, dto.getResolutionTimeTargetMinutes());
        assertTrue(dto.getBusinessHoursOnly());
        assertEquals("test-timezone", dto.getTimezone());
        assertEquals(42, dto.getGracePeriodMinutes());
    }

    @Test
    void testSettersAndGetters() {
        SLAPolicyResponseDto dto = new SLAPolicyResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setPolicyName("val-policyName");
        dto.setPolicyCode("val-policyCode");
        dto.setDescription("val-description");
        dto.setIsActive(true);
        dto.setResponseTimeTargetMinutes(99);
        dto.setResolutionTimeTargetMinutes(99);
        dto.setBusinessHoursOnly(true);
        dto.setTimezone("val-timezone");
        dto.setGracePeriodMinutes(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-policyName", dto.getPolicyName());
        assertEquals("val-policyCode", dto.getPolicyCode());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsActive());
        assertEquals(99, dto.getResponseTimeTargetMinutes());
        assertEquals(99, dto.getResolutionTimeTargetMinutes());
        assertTrue(dto.getBusinessHoursOnly());
        assertEquals("val-timezone", dto.getTimezone());
        assertEquals(99, dto.getGracePeriodMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicyResponseDto dto1 = SLAPolicyResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .policyName("test-policyName")
            .policyCode("test-policyCode")
            .description("test-description")
            .isActive(true)
            .responseTimeTargetMinutes(42)
            .resolutionTimeTargetMinutes(42)
            .businessHoursOnly(true)
            .businessHoursConfig(null)
            .applicableCategories(Collections.emptyList())
            .applicablePriorities(Collections.emptyList())
            .applicableChannels(Collections.emptyList())
            .escalationRules(Collections.emptyList())
            .timezone("test-timezone")
            .gracePeriodMinutes(42)
            .penaltyConfig(null)
            .notificationConfig(null)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        SLAPolicyResponseDto dto2 = SLAPolicyResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .policyName("test-policyName")
            .policyCode("test-policyCode")
            .description("test-description")
            .isActive(true)
            .responseTimeTargetMinutes(42)
            .resolutionTimeTargetMinutes(42)
            .businessHoursOnly(true)
            .businessHoursConfig(null)
            .applicableCategories(Collections.emptyList())
            .applicablePriorities(Collections.emptyList())
            .applicableChannels(Collections.emptyList())
            .escalationRules(Collections.emptyList())
            .timezone("test-timezone")
            .gracePeriodMinutes(42)
            .penaltyConfig(null)
            .notificationConfig(null)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLAPolicyResponseDto dto = SLAPolicyResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .policyName("test-policyName")
            .policyCode("test-policyCode")
            .description("test-description")
            .isActive(true)
            .responseTimeTargetMinutes(42)
            .resolutionTimeTargetMinutes(42)
            .businessHoursOnly(true)
            .businessHoursConfig(null)
            .applicableCategories(Collections.emptyList())
            .applicablePriorities(Collections.emptyList())
            .applicableChannels(Collections.emptyList())
            .escalationRules(Collections.emptyList())
            .timezone("test-timezone")
            .gracePeriodMinutes(42)
            .penaltyConfig(null)
            .notificationConfig(null)
            .tags(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}