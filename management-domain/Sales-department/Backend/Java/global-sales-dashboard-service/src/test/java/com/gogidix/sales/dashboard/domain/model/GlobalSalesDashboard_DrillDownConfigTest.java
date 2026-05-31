package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_DrillDownConfigTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.DrillDownConfig dto = GlobalSalesDashboard.DrillDownConfig.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-targetDashboardId", dto.getTargetDashboardId());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.DrillDownConfig dto = new GlobalSalesDashboard.DrillDownConfig();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setTargetDashboardId("val-targetDashboardId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-targetDashboardId", dto.getTargetDashboardId());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.DrillDownConfig dto1 = GlobalSalesDashboard.DrillDownConfig.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type(null)
            .build();
        GlobalSalesDashboard.DrillDownConfig dto2 = GlobalSalesDashboard.DrillDownConfig.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.DrillDownConfig dto = GlobalSalesDashboard.DrillDownConfig.builder()
                        .id("test-id")
            .name("test-name")
            .targetDashboardId("test-targetDashboardId")
            .filters(Collections.emptyList())
            .type(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}