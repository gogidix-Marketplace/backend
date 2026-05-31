package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.Widget;
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
class Widget_WidgetBehaviorTest {

        @Test
    void testBuilder() {
        Widget.WidgetBehavior dto = Widget.WidgetBehavior.builder()
                        .isDraggable(true)
            .isResizable(true)
            .isCollapsible(true)
            .isClosable(true)
            .isEditable(true)
            .isExportable(true)
            .exportFormats(Collections.emptyList())
            .enableDrillDown(true)
            .drillDownTarget("test-drillDownTarget")
            .enableZoom(true)
            .enablePan(true)
            .customBehavior(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
        assertTrue(dto.getIsCollapsible());
        assertTrue(dto.getIsClosable());
        assertTrue(dto.getIsEditable());
        assertTrue(dto.getIsExportable());
        assertTrue(dto.getEnableDrillDown());
        assertEquals("test-drillDownTarget", dto.getDrillDownTarget());
        assertTrue(dto.getEnableZoom());
        assertTrue(dto.getEnablePan());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetBehavior dto = new Widget.WidgetBehavior();
        dto.setIsDraggable(true);
        dto.setIsResizable(true);
        dto.setIsCollapsible(true);
        dto.setIsClosable(true);
        dto.setIsEditable(true);
        dto.setIsExportable(true);
        dto.setEnableDrillDown(true);
        dto.setDrillDownTarget("val-drillDownTarget");
        dto.setEnableZoom(true);
        dto.setEnablePan(true);
        assertTrue(dto.getIsDraggable());
        assertTrue(dto.getIsResizable());
        assertTrue(dto.getIsCollapsible());
        assertTrue(dto.getIsClosable());
        assertTrue(dto.getIsEditable());
        assertTrue(dto.getIsExportable());
        assertTrue(dto.getEnableDrillDown());
        assertEquals("val-drillDownTarget", dto.getDrillDownTarget());
        assertTrue(dto.getEnableZoom());
        assertTrue(dto.getEnablePan());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetBehavior dto1 = Widget.WidgetBehavior.builder()
                        .isDraggable(true)
            .isResizable(true)
            .isCollapsible(true)
            .isClosable(true)
            .isEditable(true)
            .isExportable(true)
            .exportFormats(Collections.emptyList())
            .enableDrillDown(true)
            .drillDownTarget("test-drillDownTarget")
            .enableZoom(true)
            .enablePan(true)
            .customBehavior(Collections.emptyMap())
            .build();
        Widget.WidgetBehavior dto2 = Widget.WidgetBehavior.builder()
                        .isDraggable(true)
            .isResizable(true)
            .isCollapsible(true)
            .isClosable(true)
            .isEditable(true)
            .isExportable(true)
            .exportFormats(Collections.emptyList())
            .enableDrillDown(true)
            .drillDownTarget("test-drillDownTarget")
            .enableZoom(true)
            .enablePan(true)
            .customBehavior(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetBehavior dto = Widget.WidgetBehavior.builder()
                        .isDraggable(true)
            .isResizable(true)
            .isCollapsible(true)
            .isClosable(true)
            .isEditable(true)
            .isExportable(true)
            .exportFormats(Collections.emptyList())
            .enableDrillDown(true)
            .drillDownTarget("test-drillDownTarget")
            .enableZoom(true)
            .enablePan(true)
            .customBehavior(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}