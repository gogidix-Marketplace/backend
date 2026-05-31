package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDto_KPIValueDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.KPIValueDto dto = KPIBoardDto.KPIValueDto.builder()
                        .value(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .period("test-period")
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals("test-period", dto.getPeriod());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.KPIValueDto dto = new KPIBoardDto.KPIValueDto();
        dto.setValue(BigDecimal.ONE);
        dto.setPeriod("val-period");
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals("val-period", dto.getPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.KPIValueDto dto1 = KPIBoardDto.KPIValueDto.builder()
                        .value(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .period("test-period")
            .attributes(Collections.emptyMap())
            .build();
        KPIBoardDto.KPIValueDto dto2 = KPIBoardDto.KPIValueDto.builder()
                        .value(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .period("test-period")
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.KPIValueDto dto = KPIBoardDto.KPIValueDto.builder()
                        .value(BigDecimal.TEN)
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .period("test-period")
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}