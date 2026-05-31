package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardLayout;
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
class DashboardLayout_WidgetPositionTest {

        @Test
    void testBuilder() {
        DashboardLayout.WidgetPosition dto = DashboardLayout.WidgetPosition.builder()
                        .widgetId("test-widgetId")
            .column(42)
            .row(42)
            .width(42)
            .height(42)
            .zIndex(42)
            .isPinned(true)
            .isHidden(true)
            .customProperties(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-widgetId", dto.getWidgetId());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
        assertEquals(42, dto.getZIndex());
        assertTrue(dto.getIsPinned());
        assertTrue(dto.getIsHidden());
    }

    @Test
    void testSettersAndGetters() {
        DashboardLayout.WidgetPosition dto = new DashboardLayout.WidgetPosition();
        dto.setWidgetId("val-widgetId");
        dto.setColumn(99);
        dto.setRow(99);
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setZIndex(99);
        dto.setIsPinned(true);
        dto.setIsHidden(true);
        assertEquals("val-widgetId", dto.getWidgetId());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
        assertEquals(99, dto.getZIndex());
        assertTrue(dto.getIsPinned());
        assertTrue(dto.getIsHidden());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardLayout.WidgetPosition dto1 = DashboardLayout.WidgetPosition.builder()
                        .widgetId("test-widgetId")
            .column(42)
            .row(42)
            .width(42)
            .height(42)
            .zIndex(42)
            .isPinned(true)
            .isHidden(true)
            .customProperties(Collections.emptyMap())
            .build();
        DashboardLayout.WidgetPosition dto2 = DashboardLayout.WidgetPosition.builder()
                        .widgetId("test-widgetId")
            .column(42)
            .row(42)
            .width(42)
            .height(42)
            .zIndex(42)
            .isPinned(true)
            .isHidden(true)
            .customProperties(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardLayout.WidgetPosition dto = DashboardLayout.WidgetPosition.builder()
                        .widgetId("test-widgetId")
            .column(42)
            .row(42)
            .width(42)
            .height(42)
            .zIndex(42)
            .isPinned(true)
            .isHidden(true)
            .customProperties(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}