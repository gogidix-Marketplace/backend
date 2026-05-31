package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.RequirementCommand;
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
class RequirementCommand_ScheduleCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        RequirementCommand.ScheduleCheckCommand dto = new RequirementCommand.ScheduleCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequirementId("val-requirementId");
        dto.setScheduledDate(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requirementId", dto.getRequirementId());
        assertEquals(LocalDate.of(2025,6,1), dto.getScheduledDate());
        assertEquals("val-frequency", dto.getFrequency());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementCommand.ScheduleCheckCommand dto1 = new RequirementCommand.ScheduleCheckCommand();
        RequirementCommand.ScheduleCheckCommand dto2 = new RequirementCommand.ScheduleCheckCommand();
        dto1.setTenantId("test");
        dto1.setRequirementId("test");
        dto1.setScheduledDate(LocalDate.of(2025,1,1));
        dto1.setFrequency("test");
        dto2.setTenantId("test");
        dto2.setRequirementId("test");
        dto2.setScheduledDate(LocalDate.of(2025,1,1));
        dto2.setFrequency("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementCommand.ScheduleCheckCommand dto = new RequirementCommand.ScheduleCheckCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementCommand.ScheduleCheckCommand dto = new RequirementCommand.ScheduleCheckCommand();
        dto.setTenantId("test");
        dto.setRequirementId("test");
        dto.setScheduledDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}