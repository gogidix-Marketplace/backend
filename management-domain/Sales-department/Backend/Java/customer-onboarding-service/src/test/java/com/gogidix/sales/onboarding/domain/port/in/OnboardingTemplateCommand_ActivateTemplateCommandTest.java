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
class OnboardingTemplateCommand_ActivateTemplateCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingTemplateCommand.ActivateTemplateCommand dto = new OnboardingTemplateCommand.ActivateTemplateCommand();
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateCommand.ActivateTemplateCommand dto1 = new OnboardingTemplateCommand.ActivateTemplateCommand();
        OnboardingTemplateCommand.ActivateTemplateCommand dto2 = new OnboardingTemplateCommand.ActivateTemplateCommand();
        dto1.setTenantId("test");
        dto1.setTemplateId("test");
        dto2.setTenantId("test");
        dto2.setTemplateId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingTemplateCommand.ActivateTemplateCommand dto = new OnboardingTemplateCommand.ActivateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingTemplateCommand.ActivateTemplateCommand dto = new OnboardingTemplateCommand.ActivateTemplateCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}