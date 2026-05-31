package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDto_DataFreshnessDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto.DataFreshnessDto dto = CountryDashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .lagMinutes(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-dataQuality", dto.getDataQuality());
        assertEquals(42, dto.getCompletenessPercentage());
        assertEquals(42, dto.getLagMinutes());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto.DataFreshnessDto dto = new CountryDashboardResponseDto.DataFreshnessDto();
        dto.setDataQuality("val-dataQuality");
        dto.setCompletenessPercentage(99);
        dto.setLagMinutes(99);
        assertEquals("val-dataQuality", dto.getDataQuality());
        assertEquals(99, dto.getCompletenessPercentage());
        assertEquals(99, dto.getLagMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto.DataFreshnessDto dto1 = CountryDashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .lagMinutes(42)
            .build();
        CountryDashboardResponseDto.DataFreshnessDto dto2 = CountryDashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .lagMinutes(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto.DataFreshnessDto dto = CountryDashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .lagMinutes(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}