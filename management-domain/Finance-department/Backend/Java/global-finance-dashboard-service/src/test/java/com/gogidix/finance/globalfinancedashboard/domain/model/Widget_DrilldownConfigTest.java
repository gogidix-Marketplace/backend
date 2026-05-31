package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;
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
class Widget_DrilldownConfigTest {

        @Test
    void testBuilder() {
        Widget.DrilldownConfig dto = Widget.DrilldownConfig.builder()
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
        Widget.DrilldownConfig dto = new Widget.DrilldownConfig();
        dto.setTargetDashboard("val-targetDashboard");
        dto.setTargetWidget("val-targetWidget");
        dto.setOpenInNewTab(true);
        assertEquals("val-targetDashboard", dto.getTargetDashboard());
        assertEquals("val-targetWidget", dto.getTargetWidget());
        assertTrue(dto.getOpenInNewTab());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.DrilldownConfig dto1 = Widget.DrilldownConfig.builder()
                        .targetDashboard("test-targetDashboard")
            .targetWidget("test-targetWidget")
            .parameterMapping(Collections.emptyMap())
            .openInNewTab(true)
            .build();
        Widget.DrilldownConfig dto2 = Widget.DrilldownConfig.builder()
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
        Widget.DrilldownConfig dto = Widget.DrilldownConfig.builder()
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