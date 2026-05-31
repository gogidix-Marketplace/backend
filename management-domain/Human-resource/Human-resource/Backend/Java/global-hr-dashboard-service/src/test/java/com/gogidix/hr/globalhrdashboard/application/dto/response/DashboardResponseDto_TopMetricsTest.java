package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_TopMetricsTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.TopMetrics dto = DashboardResponseDto.TopMetrics.builder()
                        .topCountriesByHeadcount(Collections.emptyMap())
            .topCountriesByRetention(Collections.emptyMap())
            .topCountriesByDiversity(Collections.emptyMap())
            .topCountriesByCompliance(Collections.emptyMap())
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.TopMetrics dto = new DashboardResponseDto.TopMetrics();


    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.TopMetrics dto1 = DashboardResponseDto.TopMetrics.builder()
                        .topCountriesByHeadcount(Collections.emptyMap())
            .topCountriesByRetention(Collections.emptyMap())
            .topCountriesByDiversity(Collections.emptyMap())
            .topCountriesByCompliance(Collections.emptyMap())
            .build();
        DashboardResponseDto.TopMetrics dto2 = DashboardResponseDto.TopMetrics.builder()
                        .topCountriesByHeadcount(Collections.emptyMap())
            .topCountriesByRetention(Collections.emptyMap())
            .topCountriesByDiversity(Collections.emptyMap())
            .topCountriesByCompliance(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.TopMetrics dto = DashboardResponseDto.TopMetrics.builder()
                        .topCountriesByHeadcount(Collections.emptyMap())
            .topCountriesByRetention(Collections.emptyMap())
            .topCountriesByDiversity(Collections.emptyMap())
            .topCountriesByCompliance(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}