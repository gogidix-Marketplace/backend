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
class Widget_WidgetStyleTest {

        @Test
    void testBuilder() {
        Widget.WidgetStyle dto = Widget.WidgetStyle.builder()
                        .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .borderRadius("test-borderRadius")
            .padding(42)
            .margin(42)
            .height(42)
            .width(42)
            .titleColor("test-titleColor")
            .titleSize(42)
            .fontFamily("test-fontFamily")
            .customStyles(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-backgroundColor", dto.getBackgroundColor());
        assertEquals("test-borderColor", dto.getBorderColor());
        assertEquals(42, dto.getBorderWidth());
        assertEquals("test-borderRadius", dto.getBorderRadius());
        assertEquals(42, dto.getPadding());
        assertEquals(42, dto.getMargin());
        assertEquals(42, dto.getHeight());
        assertEquals(42, dto.getWidth());
        assertEquals("test-titleColor", dto.getTitleColor());
        assertEquals(42, dto.getTitleSize());
        assertEquals("test-fontFamily", dto.getFontFamily());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetStyle dto = new Widget.WidgetStyle();
        dto.setBackgroundColor("val-backgroundColor");
        dto.setBorderColor("val-borderColor");
        dto.setBorderWidth(99);
        dto.setBorderRadius("val-borderRadius");
        dto.setPadding(99);
        dto.setMargin(99);
        dto.setHeight(99);
        dto.setWidth(99);
        dto.setTitleColor("val-titleColor");
        dto.setTitleSize(99);
        dto.setFontFamily("val-fontFamily");
        assertEquals("val-backgroundColor", dto.getBackgroundColor());
        assertEquals("val-borderColor", dto.getBorderColor());
        assertEquals(99, dto.getBorderWidth());
        assertEquals("val-borderRadius", dto.getBorderRadius());
        assertEquals(99, dto.getPadding());
        assertEquals(99, dto.getMargin());
        assertEquals(99, dto.getHeight());
        assertEquals(99, dto.getWidth());
        assertEquals("val-titleColor", dto.getTitleColor());
        assertEquals(99, dto.getTitleSize());
        assertEquals("val-fontFamily", dto.getFontFamily());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetStyle dto1 = Widget.WidgetStyle.builder()
                        .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .borderRadius("test-borderRadius")
            .padding(42)
            .margin(42)
            .height(42)
            .width(42)
            .titleColor("test-titleColor")
            .titleSize(42)
            .fontFamily("test-fontFamily")
            .customStyles(Collections.emptyMap())
            .build();
        Widget.WidgetStyle dto2 = Widget.WidgetStyle.builder()
                        .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .borderRadius("test-borderRadius")
            .padding(42)
            .margin(42)
            .height(42)
            .width(42)
            .titleColor("test-titleColor")
            .titleSize(42)
            .fontFamily("test-fontFamily")
            .customStyles(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetStyle dto = Widget.WidgetStyle.builder()
                        .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .borderRadius("test-borderRadius")
            .padding(42)
            .margin(42)
            .height(42)
            .width(42)
            .titleColor("test-titleColor")
            .titleSize(42)
            .fontFamily("test-fontFamily")
            .customStyles(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}