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
class OnboardingCommand_StartOnboardingCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.StartOnboardingCommand dto = new OnboardingCommand.StartOnboardingCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setStartedBy("val-startedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-startedBy", dto.getStartedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.StartOnboardingCommand dto1 = new OnboardingCommand.StartOnboardingCommand();
        OnboardingCommand.StartOnboardingCommand dto2 = new OnboardingCommand.StartOnboardingCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setStartedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setStartedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.StartOnboardingCommand dto = new OnboardingCommand.StartOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStartedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.StartOnboardingCommand dto = new OnboardingCommand.StartOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStartedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}