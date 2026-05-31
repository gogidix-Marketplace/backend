package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
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
class DashboardWidget_WidgetLocationTest {

        @Test
    void testBuilder() {
        DashboardWidget.WidgetLocation dto = DashboardWidget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidget.WidgetLocation dto = new DashboardWidget.WidgetLocation();
        dto.setRow(99);
        dto.setColumn(99);
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidget.WidgetLocation dto1 = DashboardWidget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        DashboardWidget.WidgetLocation dto2 = DashboardWidget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidget.WidgetLocation dto = DashboardWidget.WidgetLocation.builder()
                        .row(42)
            .column(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}