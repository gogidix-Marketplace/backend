package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.model.RevenueForecast;
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
class RevenueForecast_ForecastEntryTest {

        @Test
    void testBuilder() {
        RevenueForecast.ForecastEntry dto = RevenueForecast.ForecastEntry.builder()
                        .entryId("test-entryId")
            .period(null)
            .pessimistic(BigDecimal.TEN)
            .realistic(BigDecimal.TEN)
            .optimistic(BigDecimal.TEN)
            .weighted(BigDecimal.TEN)
            .currency("test-currency")
            .selectedConfidence(null)
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-entryId", dto.getEntryId());
        assertEquals(BigDecimal.TEN, dto.getPessimistic());
        assertEquals(BigDecimal.TEN, dto.getRealistic());
        assertEquals(BigDecimal.TEN, dto.getOptimistic());
        assertEquals(BigDecimal.TEN, dto.getWeighted());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        RevenueForecast.ForecastEntry dto = new RevenueForecast.ForecastEntry();
        dto.setEntryId("val-entryId");
        dto.setPessimistic(BigDecimal.ONE);
        dto.setRealistic(BigDecimal.ONE);
        dto.setOptimistic(BigDecimal.ONE);
        dto.setWeighted(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setNotes("val-notes");
        assertEquals("val-entryId", dto.getEntryId());
        assertEquals(BigDecimal.ONE, dto.getPessimistic());
        assertEquals(BigDecimal.ONE, dto.getRealistic());
        assertEquals(BigDecimal.ONE, dto.getOptimistic());
        assertEquals(BigDecimal.ONE, dto.getWeighted());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueForecast.ForecastEntry dto1 = RevenueForecast.ForecastEntry.builder()
                        .entryId("test-entryId")
            .period(null)
            .pessimistic(BigDecimal.TEN)
            .realistic(BigDecimal.TEN)
            .optimistic(BigDecimal.TEN)
            .weighted(BigDecimal.TEN)
            .currency("test-currency")
            .selectedConfidence(null)
            .notes("test-notes")
            .build();
        RevenueForecast.ForecastEntry dto2 = RevenueForecast.ForecastEntry.builder()
                        .entryId("test-entryId")
            .period(null)
            .pessimistic(BigDecimal.TEN)
            .realistic(BigDecimal.TEN)
            .optimistic(BigDecimal.TEN)
            .weighted(BigDecimal.TEN)
            .currency("test-currency")
            .selectedConfidence(null)
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueForecast.ForecastEntry dto = RevenueForecast.ForecastEntry.builder()
                        .entryId("test-entryId")
            .period(null)
            .pessimistic(BigDecimal.TEN)
            .realistic(BigDecimal.TEN)
            .optimistic(BigDecimal.TEN)
            .weighted(BigDecimal.TEN)
            .currency("test-currency")
            .selectedConfidence(null)
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}