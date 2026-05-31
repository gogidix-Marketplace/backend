package com.gogidix.hr.countryhrmanagement.domain.model;

import com.gogidix.hr.countryhrmanagement.domain.model.WorkingHoursConfig;
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
class WorkingHoursConfig_PublicHolidayTest {

        @Test
    void testBuilder() {
        WorkingHoursConfig.PublicHoliday dto = WorkingHoursConfig.PublicHoliday.builder()
                        .holidayCode("test-holidayCode")
            .holidayName("test-holidayName")
            .date(LocalDate.of(2025,1,15))
            .isRecurring(true)
            .recurrencePattern("test-recurrencePattern")
            .isPaid(true)
            .payRate(null)
            .observanceRules("test-observanceRules")
            .build();
        assertNotNull(dto);
        assertEquals("test-holidayCode", dto.getHolidayCode());
        assertEquals("test-holidayName", dto.getHolidayName());
        assertEquals(LocalDate.of(2025,1,15), dto.getDate());
        assertTrue(dto.getIsRecurring());
        assertEquals("test-recurrencePattern", dto.getRecurrencePattern());
        assertTrue(dto.getIsPaid());
        assertEquals("test-observanceRules", dto.getObservanceRules());
    }

    @Test
    void testSettersAndGetters() {
        WorkingHoursConfig.PublicHoliday dto = new WorkingHoursConfig.PublicHoliday();
        dto.setHolidayCode("val-holidayCode");
        dto.setHolidayName("val-holidayName");
        dto.setDate(LocalDate.of(2025,6,1));
        dto.setIsRecurring(true);
        dto.setRecurrencePattern("val-recurrencePattern");
        dto.setIsPaid(true);
        dto.setObservanceRules("val-observanceRules");
        assertEquals("val-holidayCode", dto.getHolidayCode());
        assertEquals("val-holidayName", dto.getHolidayName());
        assertEquals(LocalDate.of(2025,6,1), dto.getDate());
        assertTrue(dto.getIsRecurring());
        assertEquals("val-recurrencePattern", dto.getRecurrencePattern());
        assertTrue(dto.getIsPaid());
        assertEquals("val-observanceRules", dto.getObservanceRules());
    }

    @Test
    void testEqualsAndHashCode() {
        WorkingHoursConfig.PublicHoliday dto1 = WorkingHoursConfig.PublicHoliday.builder()
                        .holidayCode("test-holidayCode")
            .holidayName("test-holidayName")
            .date(LocalDate.of(2025,1,15))
            .isRecurring(true)
            .recurrencePattern("test-recurrencePattern")
            .isPaid(true)
            .payRate(null)
            .observanceRules("test-observanceRules")
            .build();
        WorkingHoursConfig.PublicHoliday dto2 = WorkingHoursConfig.PublicHoliday.builder()
                        .holidayCode("test-holidayCode")
            .holidayName("test-holidayName")
            .date(LocalDate.of(2025,1,15))
            .isRecurring(true)
            .recurrencePattern("test-recurrencePattern")
            .isPaid(true)
            .payRate(null)
            .observanceRules("test-observanceRules")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        WorkingHoursConfig.PublicHoliday dto = WorkingHoursConfig.PublicHoliday.builder()
                        .holidayCode("test-holidayCode")
            .holidayName("test-holidayName")
            .date(LocalDate.of(2025,1,15))
            .isRecurring(true)
            .recurrencePattern("test-recurrencePattern")
            .isPaid(true)
            .payRate(null)
            .observanceRules("test-observanceRules")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}