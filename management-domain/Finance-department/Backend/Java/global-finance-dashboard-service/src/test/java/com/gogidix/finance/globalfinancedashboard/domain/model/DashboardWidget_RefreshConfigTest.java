package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
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
class DashboardWidget_RefreshConfigTest {

        @Test
    void testBuilder() {
        DashboardWidget.RefreshConfig dto = DashboardWidget.RefreshConfig.builder()
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
        DashboardWidget.RefreshConfig dto = new DashboardWidget.RefreshConfig();
        dto.setAutoRefresh(true);
        dto.setIntervalSeconds(99);
        dto.setRefreshOnLoad(true);
        assertTrue(dto.getAutoRefresh());
        assertEquals(99, dto.getIntervalSeconds());
        assertTrue(dto.getRefreshOnLoad());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidget.RefreshConfig dto1 = DashboardWidget.RefreshConfig.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        DashboardWidget.RefreshConfig dto2 = DashboardWidget.RefreshConfig.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidget.RefreshConfig dto = DashboardWidget.RefreshConfig.builder()
                        .autoRefresh(true)
            .intervalSeconds(42)
            .refreshOnLoad(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}