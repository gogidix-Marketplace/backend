package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.port.in.LeadCommand;
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
class LeadCommand_CompleteActivityCommandTest {

        @Test
    void testSettersAndGetters() {
        LeadCommand.CompleteActivityCommand dto = new LeadCommand.CompleteActivityCommand();
        dto.setTenantId("val-tenantId");
        dto.setActivityId("val-activityId");
        dto.setOutcome("val-outcome");
        dto.setDurationMinutes(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-activityId", dto.getActivityId());
        assertEquals("val-outcome", dto.getOutcome());
        assertEquals(99, dto.getDurationMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCommand.CompleteActivityCommand dto1 = new LeadCommand.CompleteActivityCommand();
        LeadCommand.CompleteActivityCommand dto2 = new LeadCommand.CompleteActivityCommand();
        dto1.setTenantId("test");
        dto1.setActivityId("test");
        dto1.setOutcome("test");
        dto1.setDurationMinutes(42);
        dto2.setTenantId("test");
        dto2.setActivityId("test");
        dto2.setOutcome("test");
        dto2.setDurationMinutes(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadCommand.CompleteActivityCommand dto = new LeadCommand.CompleteActivityCommand();
        dto.setTenantId("test");
        dto.setActivityId("test");
        dto.setOutcome("test");
        dto.setDurationMinutes(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadCommand.CompleteActivityCommand dto = new LeadCommand.CompleteActivityCommand();
        dto.setTenantId("test");
        dto.setActivityId("test");
        dto.setOutcome("test");
        dto.setDurationMinutes(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}