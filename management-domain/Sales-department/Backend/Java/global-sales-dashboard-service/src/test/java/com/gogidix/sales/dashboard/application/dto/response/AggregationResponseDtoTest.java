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
class AggregationResponseDtoTest {

        @Test
    void testBuilder() {
        AggregationResponseDto dto = AggregationResponseDto.builder()
                        .id("test-id")
            .aggregationId("test-aggregationId")
            .tenantId("test-tenantId")
            .aggregationType("test-aggregationType")
            .dimension("test-dimension")
            .dimensionValue("test-dimensionValue")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .breakdown(Collections.emptyList())
            .comparison(null)
            .dataSource("test-dataSource")
            .aggregationTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .isComplete(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-aggregationId", dto.getAggregationId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-aggregationType", dto.getAggregationType());
        assertEquals("test-dimension", dto.getDimension());
        assertEquals("test-dimensionValue", dto.getDimensionValue());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-periodType", dto.getPeriodType());
        assertEquals(42, dto.getPeriodValue());
        assertEquals(42, dto.getYear());
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(42, dto.getNewOpportunities());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals(42, dto.getDataVersion());
        assertTrue(dto.getIsComplete());
    }

    @Test
    void testSettersAndGetters() {
        AggregationResponseDto dto = new AggregationResponseDto();
        dto.setId("val-id");
        dto.setAggregationId("val-aggregationId");
        dto.setTenantId("val-tenantId");
        dto.setAggregationType("val-aggregationType");
        dto.setDimension("val-dimension");
        dto.setDimensionValue("val-dimensionValue");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPeriodType("val-periodType");
        dto.setPeriodValue(99);
        dto.setYear(99);
        dto.setTotalDeals(99);
        dto.setWonDeals(99);
        dto.setNewOpportunities(99);
        dto.setDataSource("val-dataSource");
        dto.setDataVersion(99);
        dto.setIsComplete(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-aggregationId", dto.getAggregationId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-aggregationType", dto.getAggregationType());
        assertEquals("val-dimension", dto.getDimension());
        assertEquals("val-dimensionValue", dto.getDimensionValue());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-periodType", dto.getPeriodType());
        assertEquals(99, dto.getPeriodValue());
        assertEquals(99, dto.getYear());
        assertEquals(99, dto.getTotalDeals());
        assertEquals(99, dto.getWonDeals());
        assertEquals(99, dto.getNewOpportunities());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals(99, dto.getDataVersion());
        assertTrue(dto.getIsComplete());
    }

    @Test
    void testEqualsAndHashCode() {
        AggregationResponseDto dto1 = AggregationResponseDto.builder()
                        .id("test-id")
            .aggregationId("test-aggregationId")
            .tenantId("test-tenantId")
            .aggregationType("test-aggregationType")
            .dimension("test-dimension")
            .dimensionValue("test-dimensionValue")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .breakdown(Collections.emptyList())
            .comparison(null)
            .dataSource("test-dataSource")
            .aggregationTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .isComplete(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AggregationResponseDto dto2 = AggregationResponseDto.builder()
                        .id("test-id")
            .aggregationId("test-aggregationId")
            .tenantId("test-tenantId")
            .aggregationType("test-aggregationType")
            .dimension("test-dimension")
            .dimensionValue("test-dimensionValue")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .breakdown(Collections.emptyList())
            .comparison(null)
            .dataSource("test-dataSource")
            .aggregationTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .isComplete(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AggregationResponseDto dto = AggregationResponseDto.builder()
                        .id("test-id")
            .aggregationId("test-aggregationId")
            .tenantId("test-tenantId")
            .aggregationType("test-aggregationType")
            .dimension("test-dimension")
            .dimensionValue("test-dimensionValue")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .totalRevenue(null)
            .targetRevenue(null)
            .achievementPercentage(null)
            .totalDeals(42)
            .wonDeals(42)
            .winRate(null)
            .averageDealSize(null)
            .averageDiscount(null)
            .newOpportunities(42)
            .pipelineValue(null)
            .weightedPipeline(null)
            .breakdown(Collections.emptyList())
            .comparison(null)
            .dataSource("test-dataSource")
            .aggregationTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .isComplete(true)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}