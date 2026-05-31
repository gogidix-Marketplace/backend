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
class OnboardingCommand_AssignOnboardingCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.AssignOnboardingCommand dto = new OnboardingCommand.AssignOnboardingCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setAssignee("val-assignee");
        dto.setAssignedBy("val-assignedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-assignee", dto.getAssignee());
        assertEquals("val-assignedBy", dto.getAssignedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.AssignOnboardingCommand dto1 = new OnboardingCommand.AssignOnboardingCommand();
        OnboardingCommand.AssignOnboardingCommand dto2 = new OnboardingCommand.AssignOnboardingCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setAssignee("test");
        dto1.setAssignedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setAssignee("test");
        dto2.setAssignedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.AssignOnboardingCommand dto = new OnboardingCommand.AssignOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setAssignee("test");
        dto.setAssignedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.AssignOnboardingCommand dto = new OnboardingCommand.AssignOnboardingCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setAssignee("test");
        dto.setAssignedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}