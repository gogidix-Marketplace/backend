package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_DataFreshnessDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DataFreshnessDto dto = DashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-dataQuality", dto.getDataQuality());
        assertEquals(42, dto.getCompletenessPercentage());
        assertEquals(42, dto.getLagMinutes());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DataFreshnessDto dto = new DashboardResponseDto.DataFreshnessDto();
        dto.setDataQuality("val-dataQuality");
        dto.setCompletenessPercentage(99);
        dto.setLagMinutes(99);
        assertEquals("val-dataQuality", dto.getDataQuality());
        assertEquals(99, dto.getCompletenessPercentage());
        assertEquals(99, dto.getLagMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DataFreshnessDto dto1 = DashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        DashboardResponseDto.DataFreshnessDto dto2 = DashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DataFreshnessDto dto = DashboardResponseDto.DataFreshnessDto.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}