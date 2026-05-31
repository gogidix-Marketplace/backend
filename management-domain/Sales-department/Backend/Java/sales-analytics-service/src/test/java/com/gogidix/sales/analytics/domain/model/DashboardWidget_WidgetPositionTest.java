package com.gogidix.sales.analytics.domain.model;

import com.gogidix.sales.analytics.domain.model.DashboardWidget;
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
class DashboardWidget_WidgetPositionTest {

        @Test
    void testBuilder() {
        DashboardWidget.WidgetPosition dto = DashboardWidget.WidgetPosition.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getRowSpan());
        assertEquals(42, dto.getColumnSpan());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidget.WidgetPosition dto = new DashboardWidget.WidgetPosition();
        dto.setRow(99);
        dto.setColumn(99);
        dto.setRowSpan(99);
        dto.setColumnSpan(99);
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getRowSpan());
        assertEquals(99, dto.getColumnSpan());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidget.WidgetPosition dto1 = DashboardWidget.WidgetPosition.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .build();
        DashboardWidget.WidgetPosition dto2 = DashboardWidget.WidgetPosition.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidget.WidgetPosition dto = DashboardWidget.WidgetPosition.builder()
                        .row(42)
            .column(42)
            .rowSpan(42)
            .columnSpan(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}