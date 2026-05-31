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
class OnboardingTemplateCommand_RemoveStepCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingTemplateCommand.RemoveStepCommand dto = new OnboardingTemplateCommand.RemoveStepCommand();
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setStepId("val-stepId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-stepId", dto.getStepId());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateCommand.RemoveStepCommand dto1 = new OnboardingTemplateCommand.RemoveStepCommand();
        OnboardingTemplateCommand.RemoveStepCommand dto2 = new OnboardingTemplateCommand.RemoveStepCommand();
        dto1.setTenantId("test");
        dto1.setTemplateId("test");
        dto1.setStepId("test");
        dto2.setTenantId("test");
        dto2.setTemplateId("test");
        dto2.setStepId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingTemplateCommand.RemoveStepCommand dto = new OnboardingTemplateCommand.RemoveStepCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setStepId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingTemplateCommand.RemoveStepCommand dto = new OnboardingTemplateCommand.RemoveStepCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setStepId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}