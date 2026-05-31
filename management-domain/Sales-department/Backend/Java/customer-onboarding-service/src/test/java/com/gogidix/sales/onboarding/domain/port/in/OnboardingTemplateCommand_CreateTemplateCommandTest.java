package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingTemplateCommand;
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
class OnboardingTemplateCommand_CreateTemplateCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingTemplateCommand.CreateTemplateCommand dto = new OnboardingTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("val-tenantId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setEstimatedDurationHours(99);
        dto.setWelcomeEmailTemplate("val-welcomeEmailTemplate");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("val-defaultAssigneeRole");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getEstimatedDurationHours());
        assertEquals("val-welcomeEmailTemplate", dto.getWelcomeEmailTemplate());
        assertTrue(dto.getAutoAssign());
        assertEquals("val-defaultAssigneeRole", dto.getDefaultAssigneeRole());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateCommand.CreateTemplateCommand dto1 = new OnboardingTemplateCommand.CreateTemplateCommand();
        OnboardingTemplateCommand.CreateTemplateCommand dto2 = new OnboardingTemplateCommand.CreateTemplateCommand();
        dto1.setTenantId("test");
        dto1.setName("test");
        dto1.setDescription("test");
        dto1.setCustomerType(CustomerType.INDIVIDUAL);
        dto1.setSteps(Collections.emptyList());
        dto1.setRequiredDocumentTypes(Collections.emptyList());
        dto1.setEstimatedDurationHours(42);
        dto1.setWelcomeEmailTemplate("test");
        dto1.setAutoAssign(true);
        dto1.setDefaultAssigneeRole("test");
        dto1.setCreatedBy("test");
        dto2.setTenantId("test");
        dto2.setName("test");
        dto2.setDescription("test");
        dto2.setCustomerType(CustomerType.INDIVIDUAL);
        dto2.setSteps(Collections.emptyList());
        dto2.setRequiredDocumentTypes(Collections.emptyList());
        dto2.setEstimatedDurationHours(42);
        dto2.setWelcomeEmailTemplate("test");
        dto2.setAutoAssign(true);
        dto2.setDefaultAssigneeRole("test");
        dto2.setCreatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingTemplateCommand.CreateTemplateCommand dto = new OnboardingTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setCustomerType(CustomerType.INDIVIDUAL);
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        dto.setCreatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingTemplateCommand.CreateTemplateCommand dto = new OnboardingTemplateCommand.CreateTemplateCommand();
        dto.setTenantId("test");
        dto.setName("test");
        dto.setDescription("test");
        dto.setCustomerType(CustomerType.INDIVIDUAL);
        dto.setSteps(Collections.emptyList());
        dto.setRequiredDocumentTypes(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        dto.setWelcomeEmailTemplate("test");
        dto.setAutoAssign(true);
        dto.setDefaultAssigneeRole("test");
        dto.setCreatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}