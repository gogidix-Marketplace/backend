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
class DashboardWidget_DrilldownConfigTest {

        @Test
    void testBuilder() {
        DashboardWidget.DrilldownConfig dto = DashboardWidget.DrilldownConfig.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-targetDashboard", dto.getTargetDashboard());
        assertEquals("test-targetWidget", dto.getTargetWidget());
        assertTrue(dto.getOpenInNewTab());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidget.DrilldownConfig dto = new DashboardWidget.DrilldownConfig();
        dto.setTargetDashboard("val-targetDashboard");
        dto.setTargetWidget("val-targetWidget");
        dto.setOpenInNewTab(true);
        assertEquals("val-targetDashboard", dto.getTargetDashboard());
        assertEquals("val-targetWidget", dto.getTargetWidget());
        assertTrue(dto.getOpenInNewTab());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidget.DrilldownConfig dto1 = DashboardWidget.DrilldownConfig.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        DashboardWidget.DrilldownConfig dto2 = DashboardWidget.DrilldownConfig.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidget.DrilldownConfig dto = DashboardWidget.DrilldownConfig.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}