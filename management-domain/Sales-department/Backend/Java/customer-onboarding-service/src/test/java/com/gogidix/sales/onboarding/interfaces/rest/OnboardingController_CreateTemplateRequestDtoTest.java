package com.gogidix.sales.onboarding.interfaces.rest;

import com.gogidix.sales.onboarding.interfaces.rest.OnboardingController;
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
class OnboardingController_CreateTemplateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        OnboardingController.CreateTemplateRequestDto dto = new OnboardingController.CreateTemplateRequestDto();
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setEstimatedDurationHours(99);
        dto.setWelcomeEmailTemplate("val-welcomeEmailTemplate");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("val-defaultAssigneeRole");
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getEstimatedDurationHours());
        assertEquals("val-welcomeEmailTemplate", dto.getWelcomeEmailTemplate());
        assertTrue(dto.getAutoAssign());
        assertEquals("val-defaultAssigneeRole", dto.getDefaultAssigneeRole());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingController.CreateTemplateRequestDto dto1 = new OnboardingController.CreateTemplateRequestDto();
        OnboardingController.CreateTemplateRequestDto dto2 = new OnboardingController.CreateTemplateRequestDto();
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setCustomerType(null);
        dto1.setSteps(Collections.emptyList());
        dto1.setRequiredDocumentTypes(Collections.emptyList());
        dto1.setEstimatedDurationHours(42);
        dto1.setWelcomeEmailTemplate("test");
        dto1.setAutoAssign(true);
        dto1.setDefaultAssigneeRole("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setCustomerType(null);
        dto2.setSteps(Collections.emptyList());
        dto2.setRequiredDocumentTypes(Collections.emptyList());
        dto2.setEstimatedDurationHours(42);
        dto2.setWelcomeEmailTemplate("test");
        dto2.setAutoAssign(true);
        dto2.setDefaultAssigneeRole("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingController.CreateTemplateRequestDto dto = new OnboardingController.CreateTemplateRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setCustomerType(null);
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingController.CreateTemplateRequestDto dto = new OnboardingController.CreateTemplateRequestDto();
        dto.setName("test");
        dto.setDescription("test");
        dto.setCustomerType(null);
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}