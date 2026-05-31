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
class OnboardingCommand_PutOnHoldCommandTest {

        @Test
    void testSettersAndGetters() {
        OnboardingCommand.PutOnHoldCommand dto = new OnboardingCommand.PutOnHoldCommand();
        dto.setTenantId("val-tenantId");
        dto.setOnboardingId("val-onboardingId");
        dto.setReason("val-reason");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-onboardingId", dto.getOnboardingId());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        OnboardingCommand.PutOnHoldCommand dto1 = new OnboardingCommand.PutOnHoldCommand();
        OnboardingCommand.PutOnHoldCommand dto2 = new OnboardingCommand.PutOnHoldCommand();
        dto1.setTenantId("test");
        dto1.setOnboardingId("test");
        dto1.setReason("test");
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setOnboardingId("test");
        dto2.setReason("test");
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        OnboardingCommand.PutOnHoldCommand dto = new OnboardingCommand.PutOnHoldCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setReason("test");
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        OnboardingCommand.PutOnHoldCommand dto = new OnboardingCommand.PutOnHoldCommand();
        dto.setTenantId("test");
        dto.setOnboardingId("test");
        dto.setReason("test");
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}