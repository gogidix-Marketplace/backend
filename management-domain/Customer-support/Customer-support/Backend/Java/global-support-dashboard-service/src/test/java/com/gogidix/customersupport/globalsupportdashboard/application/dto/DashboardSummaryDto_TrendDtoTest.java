package com.gogidix.customersupport.globalsupportdashboard.application.dto;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.DashboardSummaryDto;
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
class DashboardSummaryDto_TrendDtoTest {

        @Test
    void testBuilder() {
        DashboardSummaryDto.TrendDto dto = DashboardSummaryDto.TrendDto.builder()
                        .ticketVolumeChange(null)
            .csatChange(null)
            .slaChange(null)
            .period("test-period")
            .build();
        assertNotNull(dto);
        assertEquals("test-period", dto.getPeriod());
    }

    @Test
    void testSettersAndGetters() {
        DashboardSummaryDto.TrendDto dto = new DashboardSummaryDto.TrendDto();
        dto.setPeriod("val-period");
        assertEquals("val-period", dto.getPeriod());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardSummaryDto.TrendDto dto1 = DashboardSummaryDto.TrendDto.builder()
                        .ticketVolumeChange(null)
            .csatChange(null)
            .slaChange(null)
            .period("test-period")
            .build();
        DashboardSummaryDto.TrendDto dto2 = DashboardSummaryDto.TrendDto.builder()
                        .ticketVolumeChange(null)
            .csatChange(null)
            .slaChange(null)
            .period("test-period")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardSummaryDto.TrendDto dto = DashboardSummaryDto.TrendDto.builder()
                        .ticketVolumeChange(null)
            .csatChange(null)
            .slaChange(null)
            .period("test-period")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}