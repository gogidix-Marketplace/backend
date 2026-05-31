package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
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
class CountrySpecificMetricsRequestDto_BusinessHoursDtoTest {

        @Test
    void testBuilder() {
        CountrySpecificMetricsRequestDto.BusinessHoursDto dto = CountrySpecificMetricsRequestDto.BusinessHoursDto.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-startTime", dto.getStartTime());
        assertEquals("test-endTime", dto.getEndTime());
        assertEquals("test-timezone", dto.getTimezone());
    }

    @Test
    void testSettersAndGetters() {
        CountrySpecificMetricsRequestDto.BusinessHoursDto dto = new CountrySpecificMetricsRequestDto.BusinessHoursDto();
        dto.setStartTime("val-startTime");
        dto.setEndTime("val-endTime");
        dto.setTimezone("val-timezone");
        assertEquals("val-startTime", dto.getStartTime());
        assertEquals("val-endTime", dto.getEndTime());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySpecificMetricsRequestDto.BusinessHoursDto dto1 = CountrySpecificMetricsRequestDto.BusinessHoursDto.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(Collections.emptyList())
            .build();
        CountrySpecificMetricsRequestDto.BusinessHoursDto dto2 = CountrySpecificMetricsRequestDto.BusinessHoursDto.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySpecificMetricsRequestDto.BusinessHoursDto dto = CountrySpecificMetricsRequestDto.BusinessHoursDto.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}