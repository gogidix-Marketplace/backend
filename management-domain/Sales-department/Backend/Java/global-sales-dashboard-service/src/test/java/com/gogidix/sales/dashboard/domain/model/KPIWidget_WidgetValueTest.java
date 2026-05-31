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
class KPIWidget_WidgetValueTest {

        @Test
    void testBuilder() {
        KPIWidget.WidgetValue dto = KPIWidget.WidgetValue.builder()
                        .value(null)
            .displayValue("test-displayValue")
            .moneyValue(null)
            .numberValue(BigDecimal.TEN)
            .stringValue("test-stringValue")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .trendInfo(null)
            .targetInfo(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-displayValue", dto.getDisplayValue());
        assertEquals(BigDecimal.TEN, dto.getNumberValue());
        assertEquals("test-stringValue", dto.getStringValue());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.WidgetValue dto = new KPIWidget.WidgetValue();
        dto.setDisplayValue("val-displayValue");
        dto.setNumberValue(BigDecimal.ONE);
        dto.setStringValue("val-stringValue");
        assertEquals("val-displayValue", dto.getDisplayValue());
        assertEquals(BigDecimal.ONE, dto.getNumberValue());
        assertEquals("val-stringValue", dto.getStringValue());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.WidgetValue dto1 = KPIWidget.WidgetValue.builder()
                        .value(null)
            .displayValue("test-displayValue")
            .moneyValue(null)
            .numberValue(BigDecimal.TEN)
            .stringValue("test-stringValue")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .trendInfo(null)
            .targetInfo(null)
            .build();
        KPIWidget.WidgetValue dto2 = KPIWidget.WidgetValue.builder()
                        .value(null)
            .displayValue("test-displayValue")
            .moneyValue(null)
            .numberValue(BigDecimal.TEN)
            .stringValue("test-stringValue")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .trendInfo(null)
            .targetInfo(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.WidgetValue dto = KPIWidget.WidgetValue.builder()
                        .value(null)
            .displayValue("test-displayValue")
            .moneyValue(null)
            .numberValue(BigDecimal.TEN)
            .stringValue("test-stringValue")
            .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .trendInfo(null)
            .targetInfo(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}