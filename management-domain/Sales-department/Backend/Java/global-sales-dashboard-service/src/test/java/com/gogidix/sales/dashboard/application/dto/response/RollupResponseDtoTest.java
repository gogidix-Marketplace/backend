package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
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
class RollupResponseDtoTest {

        @Test
    void testBuilder() {
        RollupResponseDto dto = RollupResponseDto.builder()
                        .id("test-id")
            .rollupId("test-rollupId")
            .tenantId("test-tenantId")
            .rollupType("test-rollupType")
            .rollupKey("test-rollupKey")
            .rollupName("test-rollupName")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .revenue(null)
            .deals(42)
            .winRate(null)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(null)
            .newCustomers(42)
            .margin(null)
            .marginPercentage(null)
            .overallStatus("test-overallStatus")
            .score(null)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .targets(Collections.emptyMap())
            .isRealtime(true)
            .lagSeconds(42)
            .rollupTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-rollupId", dto.getRollupId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-rollupType", dto.getRollupType());
        assertEquals("test-rollupKey", dto.getRollupKey());
        assertEquals("test-rollupName", dto.getRollupName());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-periodType", dto.getPeriodType());
        assertEquals(42, dto.getPeriodValue());
        assertEquals(42, dto.getYear());
        assertEquals("test-quarter", dto.getQuarter());
        assertEquals(42, dto.getDeals());
        assertEquals(42, dto.getOpportunities());
        assertEquals(42, dto.getNewCustomers());
        assertEquals("test-overallStatus", dto.getOverallStatus());
        assertEquals("test-trend", dto.getTrend());
        assertEquals("test-recommendation", dto.getRecommendation());
        assertEquals(42, dto.getRiskLevel());
        assertTrue(dto.getIsRealtime());
        assertEquals(42, dto.getLagSeconds());
        assertEquals(42, dto.getDataVersion());
    }

    @Test
    void testSettersAndGetters() {
        RollupResponseDto dto = new RollupResponseDto();
        dto.setId("val-id");
        dto.setRollupId("val-rollupId");
        dto.setTenantId("val-tenantId");
        dto.setRollupType("val-rollupType");
        dto.setRollupKey("val-rollupKey");
        dto.setRollupName("val-rollupName");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPeriodType("val-periodType");
        dto.setPeriodValue(99);
        dto.setYear(99);
        dto.setQuarter("val-quarter");
        dto.setDeals(99);
        dto.setOpportunities(99);
        dto.setNewCustomers(99);
        dto.setOverallStatus("val-overallStatus");
        dto.setTrend("val-trend");
        dto.setRecommendation("val-recommendation");
        dto.setRiskLevel(99);
        dto.setIsRealtime(true);
        dto.setLagSeconds(99);
        dto.setDataVersion(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-rollupId", dto.getRollupId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-rollupType", dto.getRollupType());
        assertEquals("val-rollupKey", dto.getRollupKey());
        assertEquals("val-rollupName", dto.getRollupName());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-periodType", dto.getPeriodType());
        assertEquals(99, dto.getPeriodValue());
        assertEquals(99, dto.getYear());
        assertEquals("val-quarter", dto.getQuarter());
        assertEquals(99, dto.getDeals());
        assertEquals(99, dto.getOpportunities());
        assertEquals(99, dto.getNewCustomers());
        assertEquals("val-overallStatus", dto.getOverallStatus());
        assertEquals("val-trend", dto.getTrend());
        assertEquals("val-recommendation", dto.getRecommendation());
        assertEquals(99, dto.getRiskLevel());
        assertTrue(dto.getIsRealtime());
        assertEquals(99, dto.getLagSeconds());
        assertEquals(99, dto.getDataVersion());
    }

    @Test
    void testEqualsAndHashCode() {
        RollupResponseDto dto1 = RollupResponseDto.builder()
                        .id("test-id")
            .rollupId("test-rollupId")
            .tenantId("test-tenantId")
            .rollupType("test-rollupType")
            .rollupKey("test-rollupKey")
            .rollupName("test-rollupName")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .revenue(null)
            .deals(42)
            .winRate(null)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(null)
            .newCustomers(42)
            .margin(null)
            .marginPercentage(null)
            .overallStatus("test-overallStatus")
            .score(null)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .targets(Collections.emptyMap())
            .isRealtime(true)
            .lagSeconds(42)
            .rollupTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RollupResponseDto dto2 = RollupResponseDto.builder()
                        .id("test-id")
            .rollupId("test-rollupId")
            .tenantId("test-tenantId")
            .rollupType("test-rollupType")
            .rollupKey("test-rollupKey")
            .rollupName("test-rollupName")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .revenue(null)
            .deals(42)
            .winRate(null)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(null)
            .newCustomers(42)
            .margin(null)
            .marginPercentage(null)
            .overallStatus("test-overallStatus")
            .score(null)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .targets(Collections.emptyMap())
            .isRealtime(true)
            .lagSeconds(42)
            .rollupTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RollupResponseDto dto = RollupResponseDto.builder()
                        .id("test-id")
            .rollupId("test-rollupId")
            .tenantId("test-tenantId")
            .rollupType("test-rollupType")
            .rollupKey("test-rollupKey")
            .rollupName("test-rollupName")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .periodType("test-periodType")
            .periodValue(42)
            .year(42)
            .quarter("test-quarter")
            .revenue(null)
            .deals(42)
            .winRate(null)
            .pipelineValue(null)
            .opportunities(42)
            .averageDealSize(null)
            .growthRate(null)
            .newCustomers(42)
            .margin(null)
            .marginPercentage(null)
            .overallStatus("test-overallStatus")
            .score(null)
            .trend("test-trend")
            .strengths(Collections.emptyList())
            .weaknesses(Collections.emptyList())
            .recommendation("test-recommendation")
            .riskLevel(42)
            .targets(Collections.emptyMap())
            .isRealtime(true)
            .lagSeconds(42)
            .rollupTime(Instant.parse("2025-01-15T10:00:00Z"))
            .dataVersion(42)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}