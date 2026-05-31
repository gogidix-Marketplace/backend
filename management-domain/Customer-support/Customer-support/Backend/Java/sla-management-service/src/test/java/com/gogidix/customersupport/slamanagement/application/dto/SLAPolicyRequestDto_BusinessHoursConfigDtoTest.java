package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyRequestDto;
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
class SLAPolicyRequestDto_BusinessHoursConfigDtoTest {

        @Test
    void testBuilder() {
        SLAPolicyRequestDto.BusinessHoursConfigDto dto = SLAPolicyRequestDto.BusinessHoursConfigDto.builder()
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
        SLAPolicyRequestDto.BusinessHoursConfigDto dto = new SLAPolicyRequestDto.BusinessHoursConfigDto();
        dto.setStartHour(99);
        dto.setEndHour(99);
        dto.setTimezone("val-timezone");
        assertEquals(99, dto.getStartHour());
        assertEquals(99, dto.getEndHour());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicyRequestDto.BusinessHoursConfigDto dto1 = SLAPolicyRequestDto.BusinessHoursConfigDto.builder()
                        .startHour(42)
            .endHour(42)
            .workingDays(Collections.emptyList())
            .timezone("test-timezone")
            .build();
        SLAPolicyRequestDto.BusinessHoursConfigDto dto2 = SLAPolicyRequestDto.BusinessHoursConfigDto.builder()
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
        SLAPolicyRequestDto.BusinessHoursConfigDto dto = SLAPolicyRequestDto.BusinessHoursConfigDto.builder()
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