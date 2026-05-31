package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
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
class CheckCommand_RescheduleCheckCommandTest {

        @Test
    void testSettersAndGetters() {
        CheckCommand.RescheduleCheckCommand dto = new CheckCommand.RescheduleCheckCommand();
        dto.setTenantId("val-tenantId");
        dto.setCheckId("val-checkId");
        dto.setNewScheduledDate(LocalDate.of(2025,6,1));
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-checkId", dto.getCheckId());
        assertEquals(LocalDate.of(2025,6,1), dto.getNewScheduledDate());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CheckCommand.RescheduleCheckCommand dto1 = new CheckCommand.RescheduleCheckCommand();
        CheckCommand.RescheduleCheckCommand dto2 = new CheckCommand.RescheduleCheckCommand();
        dto1.setTenantId("test");
        dto1.setCheckId("test");
        dto1.setNewScheduledDate(LocalDate.of(2025,1,1));
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setCheckId("test");
        dto2.setNewScheduledDate(LocalDate.of(2025,1,1));
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CheckCommand.RescheduleCheckCommand dto = new CheckCommand.RescheduleCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setNewScheduledDate(LocalDate.of(2025,1,1));
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CheckCommand.RescheduleCheckCommand dto = new CheckCommand.RescheduleCheckCommand();
        dto.setTenantId("test");
        dto.setCheckId("test");
        dto.setNewScheduledDate(LocalDate.of(2025,1,1));
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}