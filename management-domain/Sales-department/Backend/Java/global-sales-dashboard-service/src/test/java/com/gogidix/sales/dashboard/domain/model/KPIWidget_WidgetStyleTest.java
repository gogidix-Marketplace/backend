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
class KPIWidget_WidgetStyleTest {

        @Test
    void testBuilder() {
        KPIWidget.WidgetStyle dto = KPIWidget.WidgetStyle.builder()
                        .colorScheme("test-colorScheme")
            .primaryColor("test-primaryColor")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .font("test-font")
            .fontSize(42)
            .bold(true)
            .customStyles(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-colorScheme", dto.getColorScheme());
        assertEquals("test-primaryColor", dto.getPrimaryColor());
        assertEquals("test-backgroundColor", dto.getBackgroundColor());
        assertEquals("test-borderColor", dto.getBorderColor());
        assertEquals(42, dto.getBorderWidth());
        assertEquals("test-font", dto.getFont());
        assertEquals(42, dto.getFontSize());
        assertTrue(dto.getBold());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.WidgetStyle dto = new KPIWidget.WidgetStyle();
        dto.setColorScheme("val-colorScheme");
        dto.setPrimaryColor("val-primaryColor");
        dto.setBackgroundColor("val-backgroundColor");
        dto.setBorderColor("val-borderColor");
        dto.setBorderWidth(99);
        dto.setFont("val-font");
        dto.setFontSize(99);
        dto.setBold(true);
        assertEquals("val-colorScheme", dto.getColorScheme());
        assertEquals("val-primaryColor", dto.getPrimaryColor());
        assertEquals("val-backgroundColor", dto.getBackgroundColor());
        assertEquals("val-borderColor", dto.getBorderColor());
        assertEquals(99, dto.getBorderWidth());
        assertEquals("val-font", dto.getFont());
        assertEquals(99, dto.getFontSize());
        assertTrue(dto.getBold());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.WidgetStyle dto1 = KPIWidget.WidgetStyle.builder()
                        .colorScheme("test-colorScheme")
            .primaryColor("test-primaryColor")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .font("test-font")
            .fontSize(42)
            .bold(true)
            .customStyles(Collections.emptyMap())
            .build();
        KPIWidget.WidgetStyle dto2 = KPIWidget.WidgetStyle.builder()
                        .colorScheme("test-colorScheme")
            .primaryColor("test-primaryColor")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .font("test-font")
            .fontSize(42)
            .bold(true)
            .customStyles(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.WidgetStyle dto = KPIWidget.WidgetStyle.builder()
                        .colorScheme("test-colorScheme")
            .primaryColor("test-primaryColor")
            .backgroundColor("test-backgroundColor")
            .borderColor("test-borderColor")
            .borderWidth(42)
            .font("test-font")
            .fontSize(42)
            .bold(true)
            .customStyles(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}