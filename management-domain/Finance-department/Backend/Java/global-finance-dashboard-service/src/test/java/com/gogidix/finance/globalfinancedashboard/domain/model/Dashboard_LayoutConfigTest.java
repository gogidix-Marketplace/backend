package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
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
class Dashboard_LayoutConfigTest {

        @Test
    void testBuilder() {
        Dashboard.LayoutConfig dto = Dashboard.LayoutConfig.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rowHeight(42)
            .margin(42)
            .padding("test-padding")
            .isDraggable(true)
            .isResizable(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-layoutType", dto.getLayoutType());
        assertEquals(42, dto.getColumns());
        assertEquals(42, dto.getRowHeight());
        assertEquals(42, dto.getMargin());
        assertEquals("test-padding", dto.getPadding());
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
    }

    @Test
    void testSettersAndGetters() {
        Dashboard.LayoutConfig dto = new Dashboard.LayoutConfig();
        dto.setLayoutType("val-layoutType");
        dto.setColumns(99);
        dto.setRowHeight(99);
        dto.setMargin(99);
        dto.setPadding("val-padding");
        dto.setIsDraggable(true);
        dto.setIsResizable(true);
        assertEquals("val-layoutType", dto.getLayoutType());
        assertEquals(99, dto.getColumns());
        assertEquals(99, dto.getRowHeight());
        assertEquals(99, dto.getMargin());
        assertEquals("val-padding", dto.getPadding());
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
    }

    @Test
    void testEqualsAndHashCode() {
        Dashboard.LayoutConfig dto1 = Dashboard.LayoutConfig.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rowHeight(42)
            .margin(42)
            .padding("test-padding")
            .isDraggable(true)
            .isResizable(true)
            .build();
        Dashboard.LayoutConfig dto2 = Dashboard.LayoutConfig.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rowHeight(42)
            .margin(42)
            .padding("test-padding")
            .isDraggable(true)
            .isResizable(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Dashboard.LayoutConfig dto = Dashboard.LayoutConfig.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rowHeight(42)
            .margin(42)
            .padding("test-padding")
            .isDraggable(true)
            .isResizable(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}