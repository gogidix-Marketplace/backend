package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.TrendAnalysis;
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
class TrendAnalysis_DataPointTest {

        @Test
    void testBuilder() {
        TrendAnalysis.DataPoint dto = TrendAnalysis.DataPoint.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(BigDecimal.TEN)
            .label("test-label")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getValue());
        assertEquals("test-label", dto.getLabel());
    }

    @Test
    void testSettersAndGetters() {
        TrendAnalysis.DataPoint dto = new TrendAnalysis.DataPoint();
        dto.setValue(BigDecimal.ONE);
        dto.setLabel("val-label");
        assertEquals(BigDecimal.ONE, dto.getValue());
        assertEquals("val-label", dto.getLabel());
    }

    @Test
    void testEqualsAndHashCode() {
        TrendAnalysis.DataPoint dto1 = TrendAnalysis.DataPoint.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(BigDecimal.TEN)
            .label("test-label")
            .build();
        TrendAnalysis.DataPoint dto2 = TrendAnalysis.DataPoint.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(BigDecimal.TEN)
            .label("test-label")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TrendAnalysis.DataPoint dto = TrendAnalysis.DataPoint.builder()
                        .timestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .value(BigDecimal.TEN)
            .label("test-label")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}