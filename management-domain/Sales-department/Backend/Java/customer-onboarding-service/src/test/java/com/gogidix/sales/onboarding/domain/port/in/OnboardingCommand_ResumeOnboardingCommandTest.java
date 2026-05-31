package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
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
class OnboardingCommand_ResumeOnboardingCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.ResumeOnboardingCommand dto = new OnboardingCommand.ResumeOnboardingCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setResumedBy("val-resumedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-resumedBy", dto.getResumedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.ResumeOnboardingCommand dto1 = new OnboardingCommand.ResumeOnboardingCommand();
        OnboardingCommand.ResumeOnboardingCommand dto2 = new OnboardingCommand.ResumeOnboardingCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setResumedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setResumedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.ResumeOnboardingCommand dto = new OnboardingCommand.ResumeOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setResumedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.ResumeOnboardingCommand dto = new OnboardingCommand.ResumeOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setResumedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}