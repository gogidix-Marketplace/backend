package com.gogidix.customersupport.countrysupportdashboard.domain.model;

import com.gogidix.customersupport.countrysupportdashboard.domain.model.CountrySpecificMetrics;
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
class CountrySpecificMetrics_BusinessHoursTest {

        @Test
    void testBuilder() {
        CountrySpecificMetrics.BusinessHours dto = CountrySpecificMetrics.BusinessHours.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-startTime", dto.getStartTime());
        assertEquals("test-endTime", dto.getEndTime());
        assertEquals("test-timezone", dto.getTimezone());
    }

    @Test
    void testSettersAndGetters() {
        CountrySpecificMetrics.BusinessHours dto = new CountrySpecificMetrics.BusinessHours();
        dto.setStartTime("val-startTime");
        dto.setEndTime("val-endTime");
        dto.setTimezone("val-timezone");
        assertEquals("val-startTime", dto.getStartTime());
        assertEquals("val-endTime", dto.getEndTime());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySpecificMetrics.BusinessHours dto1 = CountrySpecificMetrics.BusinessHours.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(null)
            .build();
        CountrySpecificMetrics.BusinessHours dto2 = CountrySpecificMetrics.BusinessHours.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySpecificMetrics.BusinessHours dto = CountrySpecificMetrics.BusinessHours.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}