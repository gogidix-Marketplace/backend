package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard;
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
class KPIBoard_RefreshScheduleTest {

        @Test
    void testBuilder() {
        KPIBoard.RefreshSchedule dto = KPIBoard.RefreshSchedule.builder()
                        .scheduleType("test-scheduleType")
            .cronExpression("test-cronExpression")
            .intervalMinutes(42)
            .timeZone("test-timeZone")
            .includeWeekends(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-scheduleType", dto.getScheduleType());
        assertEquals("test-cronExpression", dto.getCronExpression());
        assertEquals(42, dto.getIntervalMinutes());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertTrue(dto.getIncludeWeekends());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoard.RefreshSchedule dto = new KPIBoard.RefreshSchedule();
        dto.setScheduleType("val-scheduleType");
        dto.setCronExpression("val-cronExpression");
        dto.setIntervalMinutes(99);
        dto.setTimeZone("val-timeZone");
        dto.setIncludeWeekends(true);
        assertEquals("val-scheduleType", dto.getScheduleType());
        assertEquals("val-cronExpression", dto.getCronExpression());
        assertEquals(99, dto.getIntervalMinutes());
        assertEquals("val-timeZone", dto.getTimeZone());
        assertTrue(dto.getIncludeWeekends());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoard.RefreshSchedule dto1 = KPIBoard.RefreshSchedule.builder()
                        .scheduleType("test-scheduleType")
            .cronExpression("test-cronExpression")
            .intervalMinutes(42)
            .timeZone("test-timeZone")
            .includeWeekends(true)
            .build();
        KPIBoard.RefreshSchedule dto2 = KPIBoard.RefreshSchedule.builder()
                        .scheduleType("test-scheduleType")
            .cronExpression("test-cronExpression")
            .intervalMinutes(42)
            .timeZone("test-timeZone")
            .includeWeekends(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoard.RefreshSchedule dto = KPIBoard.RefreshSchedule.builder()
                        .scheduleType("test-scheduleType")
            .cronExpression("test-cronExpression")
            .intervalMinutes(42)
            .timeZone("test-timeZone")
            .includeWeekends(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}