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
class Widget_WidgetLocationTest {

        @Test
    void testBuilder() {
        Widget.WidgetLocation dto = Widget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetLocation dto = new Widget.WidgetLocation();
        dto.setRow(99);
        dto.setColumn(99);
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetLocation dto1 = Widget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        Widget.WidgetLocation dto2 = Widget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetLocation dto = Widget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}