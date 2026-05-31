package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.port.in.OnboardingCommand;
import com.gogidix.sales.onboarding.domain.valueobject.StepStatus;
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
class OnboardingCommand_UpdateStepCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.UpdateStepCommand dto = new OnboardingCommand.UpdateStepCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setStepId("val-stepId");
        dto.setUpdatedBy("val-updatedBy");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-stepId", dto.getStepId());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.UpdateStepCommand dto1 = new OnboardingCommand.UpdateStepCommand();
        OnboardingCommand.UpdateStepCommand dto2 = new OnboardingCommand.UpdateStepCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setStepId("test");
        dto1.setStatus(StepStatus.PENDING);
        dto1.setUpdatedBy("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setStepId("test");
        dto2.setStatus(StepStatus.PENDING);
        dto2.setUpdatedBy("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.UpdateStepCommand dto = new OnboardingCommand.UpdateStepCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStepId("test");
        dto.setStatus(StepStatus.PENDING);
        dto.setUpdatedBy("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.UpdateStepCommand dto = new OnboardingCommand.UpdateStepCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setStepId("test");
        dto.setStatus(StepStatus.PENDING);
        dto.setUpdatedBy("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}