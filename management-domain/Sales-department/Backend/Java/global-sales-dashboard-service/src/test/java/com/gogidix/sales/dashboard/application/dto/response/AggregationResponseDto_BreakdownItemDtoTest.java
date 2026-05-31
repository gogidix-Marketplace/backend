package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.AggregationResponseDto;
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
class AggregationResponseDto_BreakdownItemDtoTest {

        @Test
    void testBuilder() {
        AggregationResponseDto.BreakdownItemDto dto = AggregationResponseDto.BreakdownItemDto.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(null)
            .color("test-color")
            .build();
        assertNotNull(dto);
        assertEquals("test-key", dto.getKey());
        assertEquals("test-label", dto.getLabel());
        assertEquals(42, dto.getCount());
        assertEquals("test-color", dto.getColor());
    }

    @Test
    void testSettersAndGetters() {
        AggregationResponseDto.BreakdownItemDto dto = new AggregationResponseDto.BreakdownItemDto();
        dto.setKey("val-key");
        dto.setLabel("val-label");
        dto.setCount(99);
        dto.setColor("val-color");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-label", dto.getLabel());
        assertEquals(99, dto.getCount());
        assertEquals("val-color", dto.getColor());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregationResponseDto.BreakdownItemDto dto1 = AggregationResponseDto.BreakdownItemDto.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(null)
            .color("test-color")
            .build();
        AggregationResponseDto.BreakdownItemDto dto2 = AggregationResponseDto.BreakdownItemDto.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(null)
            .color("test-color")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregationResponseDto.BreakdownItemDto dto = AggregationResponseDto.BreakdownItemDto.builder()
                        .key("test-key")
            .label("test-label")
            .value(null)
            .count(42)
            .percentage(null)
            .color("test-color")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}