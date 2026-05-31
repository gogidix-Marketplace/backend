package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_LayoutInfoTest {

        @Test
    void testBuilder() {
        KPIWidget.LayoutInfo dto = KPIWidget.LayoutInfo.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .zIndex(42)
            .isVisible(true)
            .isCollapsed(true)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getRowSpan());
        assertEquals(42, dto.getColumnSpan());
        assertEquals(42, dto.getZIndex());
        assertTrue(dto.getIsVisible());
        assertTrue(dto.getIsCollapsed());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.LayoutInfo dto = new KPIWidget.LayoutInfo();
        dto.setRow(99);
        dto.setColumn(99);
        dto.setRowSpan(99);
        dto.setColumnSpan(99);
        dto.setZIndex(99);
        dto.setIsVisible(true);
        dto.setIsCollapsed(true);
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getRowSpan());
        assertEquals(99, dto.getColumnSpan());
        assertEquals(99, dto.getZIndex());
        assertTrue(dto.getIsVisible());
        assertTrue(dto.getIsCollapsed());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.LayoutInfo dto1 = KPIWidget.LayoutInfo.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .zIndex(42)
            .isVisible(true)
            .isCollapsed(true)
            .build();
        KPIWidget.LayoutInfo dto2 = KPIWidget.LayoutInfo.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .zIndex(42)
            .isVisible(true)
            .isCollapsed(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.LayoutInfo dto = KPIWidget.LayoutInfo.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .zIndex(42)
            .isVisible(true)
            .isCollapsed(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}