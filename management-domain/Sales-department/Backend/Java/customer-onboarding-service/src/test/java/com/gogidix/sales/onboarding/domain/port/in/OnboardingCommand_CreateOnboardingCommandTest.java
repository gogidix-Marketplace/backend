package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import com.gogidix.sales.onboarding.domain.valueobject.Priority;
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
class OnboardingCommand_CreateOnboardingCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.CreateOnboardingCommand dto = new OnboardingCommand.CreateOnboardingCommand();
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setTemplateId("val-templateId");
        dto.setTemplateName("val-templateName");
        dto.setInitiatedBy("val-initiatedBy");
        dto.setEstimatedDurationHours(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-templateName", dto.getTemplateName());
        assertEquals("val-initiatedBy", dto.getInitiatedBy());
        assertEquals(99, dto.getEstimatedDurationHours());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.CreateOnboardingCommand dto1 = new OnboardingCommand.CreateOnboardingCommand();
        OnboardingCommand.CreateOnboardingCommand dto2 = new OnboardingCommand.CreateOnboardingCommand();
        dto1.setTenantId("test");
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setCustomerEmail("test");
        dto1.setCustomerType(CustomerType.INDIVIDUAL);
        dto1.setTemplateId("test");
        dto1.setTemplateName("test");
        dto1.setInitiatedBy("test");
        dto1.setPriority(Priority.LOW);
        dto1.setSteps(Collections.emptyList());
        dto1.setEstimatedDurationHours(42);
        dto2.setTenantId("test");
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setCustomerEmail("test");
        dto2.setCustomerType(CustomerType.INDIVIDUAL);
        dto2.setTemplateId("test");
        dto2.setTemplateName("test");
        dto2.setInitiatedBy("test");
        dto2.setPriority(Priority.LOW);
        dto2.setSteps(Collections.emptyList());
        dto2.setEstimatedDurationHours(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.CreateOnboardingCommand dto = new OnboardingCommand.CreateOnboardingCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setCustomerEmail("test");
        dto.setCustomerType(CustomerType.INDIVIDUAL);
        dto.setTemplateId("test");
        dto.setTemplateName("test");
        dto.setInitiatedBy("test");
        dto.setPriority(Priority.LOW);
        dto.setSteps(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.CreateOnboardingCommand dto = new OnboardingCommand.CreateOnboardingCommand();
        dto.setTenantId("test");
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setCustomerEmail("test");
        dto.setCustomerType(CustomerType.INDIVIDUAL);
        dto.setTemplateId("test");
        dto.setTemplateName("test");
        dto.setInitiatedBy("test");
        dto.setPriority(Priority.LOW);
        dto.setSteps(Collections.emptyList());
        dto.setEstimatedDurationHours(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}