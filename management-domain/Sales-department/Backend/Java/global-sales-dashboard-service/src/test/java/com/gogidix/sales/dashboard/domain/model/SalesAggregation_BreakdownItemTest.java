package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
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
class SalesAggregation_BreakdownItemTest {

        @Test
    void testBuilder() {
        SalesAggregation.BreakdownItem dto = SalesAggregation.BreakdownItem.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(BigDecimal.TEN)
            .color("test-color")
            .build();
        assertNotNull(dto);
        assertEquals("test-key", dto.getKey());
        assertEquals("test-label", dto.getLabel());
        assertEquals(42, dto.getCount());
        assertEquals(BigDecimal.TEN, dto.getPercentage());
        assertEquals("test-color", dto.getColor());
    }

    @Test
    void testSettersAndGetters() {
        SalesAggregation.BreakdownItem dto = new SalesAggregation.BreakdownItem();
        dto.setKey("val-key");
        dto.setLabel("val-label");
        dto.setCount(99);
        dto.setPercentage(BigDecimal.ONE);
        dto.setColor("val-color");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-label", dto.getLabel());
        assertEquals(99, dto.getCount());
        assertEquals(BigDecimal.ONE, dto.getPercentage());
        assertEquals("val-color", dto.getColor());
    }

    @Test
    void testEqualsAndHashCode() {
        SalesAggregation.BreakdownItem dto1 = SalesAggregation.BreakdownItem.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(BigDecimal.TEN)
            .color("test-color")
            .build();
        SalesAggregation.BreakdownItem dto2 = SalesAggregation.BreakdownItem.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(BigDecimal.TEN)
            .color("test-color")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SalesAggregation.BreakdownItem dto = SalesAggregation.BreakdownItem.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(BigDecimal.TEN)
            .color("test-color")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}