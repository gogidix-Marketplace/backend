package com.gogidix.finance.globalfinancedashboard.application.dto.response;

import com.gogidix.finance.globalfinancedashboard.application.dto.response.DashboardWidgetResponseDto;
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
class DashboardWidgetResponseDto_DataSourceDtoTest {

        @Test
    void testBuilder() {
        DashboardWidgetResponseDto.DataSourceDto dto = DashboardWidgetResponseDto.DataSourceDto.builder()
                        .type("test-type")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .fields(Collections.emptyList())
            .aggregation("test-aggregation")
            .cacheDuration(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-type", dto.getType());
        assertEquals("test-endpoint", dto.getEndpoint());
        assertEquals("test-query", dto.getQuery());
        assertEquals("test-collection", dto.getCollection());
        assertEquals("test-aggregation", dto.getAggregation());
        assertEquals(42L, dto.getCacheDuration());
    }

    @Test
    void testSettersAndGetters() {
        DashboardWidgetResponseDto.DataSourceDto dto = new DashboardWidgetResponseDto.DataSourceDto();
        dto.setType("val-type");
        dto.setEndpoint("val-endpoint");
        dto.setQuery("val-query");
        dto.setCollection("val-collection");
        dto.setAggregation("val-aggregation");
        assertEquals("val-type", dto.getType());
        assertEquals("val-endpoint", dto.getEndpoint());
        assertEquals("val-query", dto.getQuery());
        assertEquals("val-collection", dto.getCollection());
        assertEquals("val-aggregation", dto.getAggregation());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardWidgetResponseDto.DataSourceDto dto1 = DashboardWidgetResponseDto.DataSourceDto.builder()
                        .type("test-type")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .fields(Collections.emptyList())
            .aggregation("test-aggregation")
            .cacheDuration(42L)
            .build();
        DashboardWidgetResponseDto.DataSourceDto dto2 = DashboardWidgetResponseDto.DataSourceDto.builder()
                        .type("test-type")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .fields(Collections.emptyList())
            .aggregation("test-aggregation")
            .cacheDuration(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardWidgetResponseDto.DataSourceDto dto = DashboardWidgetResponseDto.DataSourceDto.builder()
                        .type("test-type")
            .endpoint("test-endpoint")
            .query("test-query")
            .collection("test-collection")
            .parameters(Collections.emptyMap())
            .fields(Collections.emptyList())
            .aggregation("test-aggregation")
            .cacheDuration(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}