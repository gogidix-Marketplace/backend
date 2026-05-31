package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDto_KPIItemDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.KPIItemDto dto = KPIBoardDto.KPIItemDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .dataSource(null)
            .display(null)
            .aggregationType("test-aggregationType")
            .comparisonType("test-comparisonType")
            .comparisonPeriod("test-comparisonPeriod")
            .threshold(null)
            .unit("test-unit")
            .decimalPlaces(42)
            .currentValue(null)
            .previousValue(null)
            .changePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .historicalValues(Collections.emptyList())
            .displayOrder(42)
            .visible(true)
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-kpiId", dto.getKpiId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-type", dto.getType());
        assertEquals("test-aggregationType", dto.getAggregationType());
        assertEquals("test-comparisonType", dto.getComparisonType());
        assertEquals("test-comparisonPeriod", dto.getComparisonPeriod());
        assertEquals("test-unit", dto.getUnit());
        assertEquals(42, dto.getDecimalPlaces());
        assertEquals(BigDecimal.TEN, dto.getChangePercentage());
        assertEquals("test-trend", dto.getTrend());
        assertEquals(42, dto.getDisplayOrder());
        assertTrue(dto.getVisible());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.KPIItemDto dto = new KPIBoardDto.KPIItemDto();
        dto.setKpiId("val-kpiId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setType("val-type");
        dto.setAggregationType("val-aggregationType");
        dto.setComparisonType("val-comparisonType");
        dto.setComparisonPeriod("val-comparisonPeriod");
        dto.setUnit("val-unit");
        dto.setDecimalPlaces(99);
        dto.setChangePercentage(BigDecimal.ONE);
        dto.setTrend("val-trend");
        dto.setDisplayOrder(99);
        dto.setVisible(true);
        assertEquals("val-kpiId", dto.getKpiId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-type", dto.getType());
        assertEquals("val-aggregationType", dto.getAggregationType());
        assertEquals("val-comparisonType", dto.getComparisonType());
        assertEquals("val-comparisonPeriod", dto.getComparisonPeriod());
        assertEquals("val-unit", dto.getUnit());
        assertEquals(99, dto.getDecimalPlaces());
        assertEquals(BigDecimal.ONE, dto.getChangePercentage());
        assertEquals("val-trend", dto.getTrend());
        assertEquals(99, dto.getDisplayOrder());
        assertTrue(dto.getVisible());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.KPIItemDto dto1 = KPIBoardDto.KPIItemDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .dataSource(null)
            .display(null)
            .aggregationType("test-aggregationType")
            .comparisonType("test-comparisonType")
            .comparisonPeriod("test-comparisonPeriod")
            .threshold(null)
            .unit("test-unit")
            .decimalPlaces(42)
            .currentValue(null)
            .previousValue(null)
            .changePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .historicalValues(Collections.emptyList())
            .displayOrder(42)
            .visible(true)
            .metadata(Collections.emptyMap())
            .build();
        KPIBoardDto.KPIItemDto dto2 = KPIBoardDto.KPIItemDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .dataSource(null)
            .display(null)
            .aggregationType("test-aggregationType")
            .comparisonType("test-comparisonType")
            .comparisonPeriod("test-comparisonPeriod")
            .threshold(null)
            .unit("test-unit")
            .decimalPlaces(42)
            .currentValue(null)
            .previousValue(null)
            .changePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .historicalValues(Collections.emptyList())
            .displayOrder(42)
            .visible(true)
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.KPIItemDto dto = KPIBoardDto.KPIItemDto.builder()
                        .kpiId("test-kpiId")
            .name("test-name")
            .description("test-description")
            .type("test-type")
            .dataSource(null)
            .display(null)
            .aggregationType("test-aggregationType")
            .comparisonType("test-comparisonType")
            .comparisonPeriod("test-comparisonPeriod")
            .threshold(null)
            .unit("test-unit")
            .decimalPlaces(42)
            .currentValue(null)
            .previousValue(null)
            .changePercentage(BigDecimal.TEN)
            .trend("test-trend")
            .historicalValues(Collections.emptyList())
            .displayOrder(42)
            .visible(true)
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}