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
class OnboardingTemplateCommand_CreateNewVersionCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingTemplateCommand.CreateNewVersionCommand dto = new OnboardingTemplateCommand.CreateNewVersionCommand();
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingTemplateCommand.CreateNewVersionCommand dto1 = new OnboardingTemplateCommand.CreateNewVersionCommand();
        OnboardingTemplateCommand.CreateNewVersionCommand dto2 = new OnboardingTemplateCommand.CreateNewVersionCommand();
        dto1.setTenantId("test");
        dto1.setTemplateId("test");
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setTemplateId("test");
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingTemplateCommand.CreateNewVersionCommand dto = new OnboardingTemplateCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingTemplateCommand.CreateNewVersionCommand dto = new OnboardingTemplateCommand.CreateNewVersionCommand();
        dto.setTenantId("test");
        dto.setTemplateId("test");
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}