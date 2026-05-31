package com.gogidix.customersupport.phonesupport.domain.model;

import com.gogidix.customersupport.phonesupport.domain.model.CallQueue;
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
class CallQueue_OperatingHoursTest {

        @Test
    void testBuilder() {
        CallQueue.OperatingHours dto = CallQueue.OperatingHours.builder()
                        .startHour(42)
            .endHour(42)
            .workingDays(Collections.emptyList())
            .timezone("test-timezone")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getStartHour());
        assertEquals(42, dto.getEndHour());
        assertEquals("test-timezone", dto.getTimezone());
    }

    @Test
    void testSettersAndGetters() {
        CallQueue.OperatingHours dto = new CallQueue.OperatingHours();
        dto.setStartHour(99);
        dto.setEndHour(99);
        dto.setTimezone("val-timezone");
        assertEquals(99, dto.getStartHour());
        assertEquals(99, dto.getEndHour());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        CallQueue.OperatingHours dto1 = CallQueue.OperatingHours.builder()
                        .startHour(42)
            .endHour(42)
            .workingDays(Collections.emptyList())
            .timezone("test-timezone")
            .build();
        CallQueue.OperatingHours dto2 = CallQueue.OperatingHours.builder()
                        .startHour(42)
            .endHour(42)
            .workingDays(Collections.emptyList())
            .timezone("test-timezone")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CallQueue.OperatingHours dto = CallQueue.OperatingHours.builder()
                        .startHour(42)
            .endHour(42)
            .workingDays(Collections.emptyList())
            .timezone("test-timezone")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}