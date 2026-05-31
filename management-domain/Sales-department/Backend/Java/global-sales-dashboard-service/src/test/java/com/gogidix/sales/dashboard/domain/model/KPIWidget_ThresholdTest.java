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
class KPIWidget_ThresholdTest {

        @Test
    void testBuilder() {
        KPIWidget.Threshold dto = KPIWidget.Threshold.builder()
                        .label("test-label")
            .minValue(BigDecimal.TEN)
            .maxValue(BigDecimal.TEN)
            .color("test-color")
            .icon("test-icon")
            .severity("test-severity")
            .build();
        assertNotNull(dto);
        assertEquals("test-label", dto.getLabel());
        assertEquals(BigDecimal.TEN, dto.getMinValue());
        assertEquals(BigDecimal.TEN, dto.getMaxValue());
        assertEquals("test-color", dto.getColor());
        assertEquals("test-icon", dto.getIcon());
        assertEquals("test-severity", dto.getSeverity());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.Threshold dto = new KPIWidget.Threshold();
        dto.setLabel("val-label");
        dto.setMinValue(BigDecimal.ONE);
        dto.setMaxValue(BigDecimal.ONE);
        dto.setColor("val-color");
        dto.setIcon("val-icon");
        dto.setSeverity("val-severity");
        assertEquals("val-label", dto.getLabel());
        assertEquals(BigDecimal.ONE, dto.getMinValue());
        assertEquals(BigDecimal.ONE, dto.getMaxValue());
        assertEquals("val-color", dto.getColor());
        assertEquals("val-icon", dto.getIcon());
        assertEquals("val-severity", dto.getSeverity());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.Threshold dto1 = KPIWidget.Threshold.builder()
                        .label("test-label")
            .minValue(BigDecimal.TEN)
            .maxValue(BigDecimal.TEN)
            .color("test-color")
            .icon("test-icon")
            .severity("test-severity")
            .build();
        KPIWidget.Threshold dto2 = KPIWidget.Threshold.builder()
                        .label("test-label")
            .minValue(BigDecimal.TEN)
            .maxValue(BigDecimal.TEN)
            .color("test-color")
            .icon("test-icon")
            .severity("test-severity")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.Threshold dto = KPIWidget.Threshold.builder()
                        .label("test-label")
            .minValue(BigDecimal.TEN)
            .maxValue(BigDecimal.TEN)
            .color("test-color")
            .icon("test-icon")
            .severity("test-severity")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}