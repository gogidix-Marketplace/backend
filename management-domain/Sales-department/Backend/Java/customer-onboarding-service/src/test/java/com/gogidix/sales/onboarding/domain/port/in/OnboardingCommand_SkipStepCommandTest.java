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
class OnboardingCommand_SkipStepCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.SkipStepCommand dto = new OnboardingCommand.SkipStepCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setStepId("val-stepId");
        dto.setSkippedBy("val-skippedBy");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-stepId", dto.getStepId());
        assertEquals("val-skippedBy", dto.getSkippedBy());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.SkipStepCommand dto1 = new OnboardingCommand.SkipStepCommand();
        OnboardingCommand.SkipStepCommand dto2 = new OnboardingCommand.SkipStepCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setStepId("test");
        dto1.setSkippedBy("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setStepId("test");
        dto2.setSkippedBy("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.SkipStepCommand dto = new OnboardingCommand.SkipStepCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStepId("test");
        dto.setSkippedBy("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.SkipStepCommand dto = new OnboardingCommand.SkipStepCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStepId("test");
        dto.setSkippedBy("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}