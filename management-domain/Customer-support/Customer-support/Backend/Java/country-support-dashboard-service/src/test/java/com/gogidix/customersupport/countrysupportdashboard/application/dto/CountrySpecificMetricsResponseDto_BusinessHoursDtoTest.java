package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
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
class CountrySpecificMetricsResponseDto_BusinessHoursDtoTest {

        @Test
    void testBuilder() {
        CountrySpecificMetricsResponseDto.BusinessHoursDto dto = CountrySpecificMetricsResponseDto.BusinessHoursDto.builder()
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
        CountrySpecificMetricsResponseDto.BusinessHoursDto dto = new CountrySpecificMetricsResponseDto.BusinessHoursDto();
        dto.setStartTime("val-startTime");
        dto.setEndTime("val-endTime");
        dto.setTimezone("val-timezone");
        assertEquals("val-startTime", dto.getStartTime());
        assertEquals("val-endTime", dto.getEndTime());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySpecificMetricsResponseDto.BusinessHoursDto dto1 = CountrySpecificMetricsResponseDto.BusinessHoursDto.builder()
                        .startTime("test-startTime")
            .endTime("test-endTime")
            .timezone("test-timezone")
            .workingDays(null)
            .build();
        CountrySpecificMetricsResponseDto.BusinessHoursDto dto2 = CountrySpecificMetricsResponseDto.BusinessHoursDto.builder()
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
        CountrySpecificMetricsResponseDto.BusinessHoursDto dto = CountrySpecificMetricsResponseDto.BusinessHoursDto.builder()
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