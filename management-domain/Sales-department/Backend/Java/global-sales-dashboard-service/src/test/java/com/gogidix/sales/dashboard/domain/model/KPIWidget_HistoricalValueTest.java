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
class KPIWidget_HistoricalValueTest {

        @Test
    void testBuilder() {
        KPIWidget.HistoricalValue dto = KPIWidget.HistoricalValue.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(null)
            .label("test-label")
            .build();
        assertNotNull(dto);
        assertEquals("test-label", dto.getLabel());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.HistoricalValue dto = new KPIWidget.HistoricalValue();
        dto.setLabel("val-label");
        assertEquals("val-label", dto.getLabel());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.HistoricalValue dto1 = KPIWidget.HistoricalValue.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(null)
            .label("test-label")
            .build();
        KPIWidget.HistoricalValue dto2 = KPIWidget.HistoricalValue.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(null)
            .label("test-label")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.HistoricalValue dto = KPIWidget.HistoricalValue.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(null)
            .label("test-label")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}