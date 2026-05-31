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
class OnboardingCommand_CompleteOnboardingCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.CompleteOnboardingCommand dto = new OnboardingCommand.CompleteOnboardingCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setCompletedBy("val-completedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-completedBy", dto.getCompletedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.CompleteOnboardingCommand dto1 = new OnboardingCommand.CompleteOnboardingCommand();
        OnboardingCommand.CompleteOnboardingCommand dto2 = new OnboardingCommand.CompleteOnboardingCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setCompletedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setCompletedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.CompleteOnboardingCommand dto = new OnboardingCommand.CompleteOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setCompletedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.CompleteOnboardingCommand dto = new OnboardingCommand.CompleteOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setCompletedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}