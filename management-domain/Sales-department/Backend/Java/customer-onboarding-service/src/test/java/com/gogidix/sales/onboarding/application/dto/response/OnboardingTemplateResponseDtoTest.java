package com.gogidix.sales.onboarding.application.dto.response;

import com.gogidix.sales.onboarding.application.dto.response.OnboardingTemplateResponseDto;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
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
class OnboardingTemplateResponseDtoTest {

        @Test
    void testBuilder() {
        OnboardingTemplateResponseDto dto = OnboardingTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .customerType(CustomerType.INDIVIDUAL)
            .active(true)
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .steps(Collections.emptyList())
            .requiredDocumentTypes(Collections.emptyList())
            .estimatedDurationHours(42)
            .welcomeEmailTemplate("test-welcomeEmailTemplate")
            .autoAssign(true)
            .defaultAssigneeRole("test-defaultAssigneeRole")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals(42, dto.getVersion());
        assertEquals("test-parentTemplateId", dto.getParentTemplateId());
        assertEquals(42, dto.getEstimatedDurationHours());
        assertEquals("test-welcomeEmailTemplate", dto.getWelcomeEmailTemplate());
        assertTrue(dto.getAutoAssign());
        assertEquals("test-defaultAssigneeRole", dto.getDefaultAssigneeRole());
    }

    @Test
    void testSettersAndGetters() {
        OnboardingTemplateResponseDto dto = new OnboardingTemplateResponseDto();
        dto.setId("val-id");
        dto.setTemplateId("val-templateId");
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setActive(true);
        dto.setVersion(99);
        dto.setParentTemplateId("val-parentTemplateId");
        dto.setEstimatedDurationHours(99);
        dto.setWelcomeEmailTemplate("val-welcomeEmailTemplate");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("val-defaultAssigneeRole");
        assertEquals("val-id", dto.getId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals(99, dto.getVersion());
        assertEquals("val-parentTemplateId", dto.getParentTemplateId());
        assertEquals(99, dto.getEstimatedDurationHours());
        assertEquals("val-welcomeEmailTemplate", dto.getWelcomeEmailTemplate());
        assertTrue(dto.getAutoAssign());
        assertEquals("val-defaultAssigneeRole", dto.getDefaultAssigneeRole());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateResponseDto dto1 = OnboardingTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .customerType(CustomerType.INDIVIDUAL)
            .active(true)
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .steps(Collections.emptyList())
            .requiredDocumentTypes(Collections.emptyList())
            .estimatedDurationHours(42)
            .welcomeEmailTemplate("test-welcomeEmailTemplate")
            .autoAssign(true)
            .defaultAssigneeRole("test-defaultAssigneeRole")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        OnboardingTemplateResponseDto dto2 = OnboardingTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .customerType(CustomerType.INDIVIDUAL)
            .active(true)
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .steps(Collections.emptyList())
            .requiredDocumentTypes(Collections.emptyList())
            .estimatedDurationHours(42)
            .welcomeEmailTemplate("test-welcomeEmailTemplate")
            .autoAssign(true)
            .defaultAssigneeRole("test-defaultAssigneeRole")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        OnboardingTemplateResponseDto dto = OnboardingTemplateResponseDto.builder()
                        .id("test-id")
            .templateId("test-templateId")
            .tenantId("test-tenantId")
            .name("test-name")
            .description("test-description")
            .customerType(CustomerType.INDIVIDUAL)
            .active(true)
            .version(42)
            .parentTemplateId("test-parentTemplateId")
            .steps(Collections.emptyList())
            .requiredDocumentTypes(Collections.emptyList())
            .estimatedDurationHours(42)
            .welcomeEmailTemplate("test-welcomeEmailTemplate")
            .autoAssign(true)
            .defaultAssigneeRole("test-defaultAssigneeRole")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}