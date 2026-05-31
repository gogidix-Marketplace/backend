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
class DashboardLayout_LayoutConfigTest {

        @Test
    void testBuilder() {
        DashboardLayout.LayoutConfig dto = DashboardLayout.LayoutConfig.builder()
                        .columns(42)
            .rows(42)
            .rowHeight(42)
            .margin(42)
            .padding(42)
            .gap("test-gap")
            .isDraggable(true)
            .isResizable(true)
            .autoArrange(true)
            .alignment("test-alignment")
            .direction("test-direction")
            .wrapContent(true)
            .minWidth(42)
            .maxWidth(42)
            .minHeight(42)
            .maxHeight(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getColumns());
        assertEquals(42, dto.getRows());
        assertEquals(42, dto.getRowHeight());
        assertEquals(42, dto.getMargin());
        assertEquals(42, dto.getPadding());
        assertEquals("test-gap", dto.getGap());
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
        assertTrue(dto.getAutoArrange());
        assertEquals("test-alignment", dto.getAlignment());
        assertEquals("test-direction", dto.getDirection());
        assertTrue(dto.getWrapContent());
        assertEquals(42, dto.getMinWidth());
        assertEquals(42, dto.getMaxWidth());
        assertEquals(42, dto.getMinHeight());
        assertEquals(42, dto.getMaxHeight());
    }

    @Test
    void testSettersAndGetters() {
        DashboardLayout.LayoutConfig dto = new DashboardLayout.LayoutConfig();
        dto.setColumns(99);
        dto.setRows(99);
        dto.setRowHeight(99);
        dto.setMargin(99);
        dto.setPadding(99);
        dto.setGap("val-gap");
        dto.setIsDraggable(true);
        dto.setIsResizable(true);
        dto.setAutoArrange(true);
        dto.setAlignment("val-alignment");
        dto.setDirection("val-direction");
        dto.setWrapContent(true);
        dto.setMinWidth(99);
        dto.setMaxWidth(99);
        dto.setMinHeight(99);
        dto.setMaxHeight(99);
        assertEquals(99, dto.getColumns());
        assertEquals(99, dto.getRows());
        assertEquals(99, dto.getRowHeight());
        assertEquals(99, dto.getMargin());
        assertEquals(99, dto.getPadding());
        assertEquals("val-gap", dto.getGap());
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
        assertTrue(dto.getAutoArrange());
        assertEquals("val-alignment", dto.getAlignment());
        assertEquals("val-direction", dto.getDirection());
        assertTrue(dto.getWrapContent());
        assertEquals(99, dto.getMinWidth());
        assertEquals(99, dto.getMaxWidth());
        assertEquals(99, dto.getMinHeight());
        assertEquals(99, dto.getMaxHeight());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardLayout.LayoutConfig dto1 = DashboardLayout.LayoutConfig.builder()
                        .columns(42)
            .rows(42)
            .rowHeight(42)
            .margin(42)
            .padding(42)
            .gap("test-gap")
            .isDraggable(true)
            .isResizable(true)
            .autoArrange(true)
            .alignment("test-alignment")
            .direction("test-direction")
            .wrapContent(true)
            .minWidth(42)
            .maxWidth(42)
            .minHeight(42)
            .maxHeight(42)
            .build();
        DashboardLayout.LayoutConfig dto2 = DashboardLayout.LayoutConfig.builder()
                        .columns(42)
            .rows(42)
            .rowHeight(42)
            .margin(42)
            .padding(42)
            .gap("test-gap")
            .isDraggable(true)
            .isResizable(true)
            .autoArrange(true)
            .alignment("test-alignment")
            .direction("test-direction")
            .wrapContent(true)
            .minWidth(42)
            .maxWidth(42)
            .minHeight(42)
            .maxHeight(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardLayout.LayoutConfig dto = DashboardLayout.LayoutConfig.builder()
                        .columns(42)
            .rows(42)
            .rowHeight(42)
            .margin(42)
            .padding(42)
            .gap("test-gap")
            .isDraggable(true)
            .isResizable(true)
            .autoArrange(true)
            .alignment("test-alignment")
            .direction("test-direction")
            .wrapContent(true)
            .minWidth(42)
            .maxWidth(42)
            .minHeight(42)
            .maxHeight(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}