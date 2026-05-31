package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingTemplateCommand;
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
class OnboardingTemplateCommand_UpdateTemplateCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingTemplateCommand.UpdateTemplateCommand dto = new OnboardingTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setEstimatedDurationHours(99);
        dto.setWelcomeEmailTemplate("val-welcomeEmailTemplate");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("val-defaultAssigneeRole");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getEstimatedDurationHours());
        assertEquals("val-welcomeEmailTemplate", dto.getWelcomeEmailTemplate());
        assertTrue(dto.getAutoAssign());
        assertEquals("val-defaultAssigneeRole", dto.getDefaultAssigneeRole());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateCommand.UpdateTemplateCommand dto1 = new OnboardingTemplateCommand.UpdateTemplateCommand();
        OnboardingTemplateCommand.UpdateTemplateCommand dto2 = new OnboardingTemplateCommand.UpdateTemplateCommand();
        dto1.setTenantId("test");
        dto1.setTemplateId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setSteps(Collections.emptyList());
        dto1.setRequiredDocumentTypes(Collections.emptyList());
        dto1.setEstimatedDurationHours(42);
        dto1.setWelcomeEmailTemplate("test");
        dto1.setAutoAssign(true);
        dto1.setDefaultAssigneeRole("test");
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setTemplateId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setSteps(Collections.emptyList());
        dto2.setRequiredDocumentTypes(Collections.emptyList());
        dto2.setEstimatedDurationHours(42);
        dto2.setWelcomeEmailTemplate("test");
        dto2.setAutoAssign(true);
        dto2.setDefaultAssigneeRole("test");
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingTemplateCommand.UpdateTemplateCommand dto = new OnboardingTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingTemplateCommand.UpdateTemplateCommand dto = new OnboardingTemplateCommand.UpdateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}