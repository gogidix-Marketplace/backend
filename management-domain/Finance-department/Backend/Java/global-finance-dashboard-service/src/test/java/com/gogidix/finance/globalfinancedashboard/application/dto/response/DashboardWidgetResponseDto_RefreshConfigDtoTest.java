package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardWidgetResponseDto;
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
class DashboardWidgetResponseDto_RefreshConfigDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.RefreshConfigDto dto = DashboardWidgetResponseDto.RefreshConfigDto.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getAutoRefresh());
        assertEquals(42, dto.getIntervalSeconds());
        assertTrue(dto.getRefreshOnLoad());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.RefreshConfigDto dto = new DashboardWidgetResponseDto.RefreshConfigDto();
        dto.setAutoRefresh(true);
        dto.setIntervalSeconds(99);
        dto.setRefreshOnLoad(true);
        assertTrue(dto.getAutoRefresh());
        assertEquals(99, dto.getIntervalSeconds());
        assertTrue(dto.getRefreshOnLoad());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.RefreshConfigDto dto1 = DashboardWidgetResponseDto.RefreshConfigDto.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        DashboardWidgetResponseDto.RefreshConfigDto dto2 = DashboardWidgetResponseDto.RefreshConfigDto.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.RefreshConfigDto dto = DashboardWidgetResponseDto.RefreshConfigDto.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}