package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.AgentQualityProfileDto;
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
class AgentQualityProfileDto_CategoryPerformanceDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.CategoryPerformanceDto dto = AgentQualityProfileDto.CategoryPerformanceDto.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend("test-trend")
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getReviewCount());
        assertEquals("test-trend", dto.getTrend());
        assertEquals(42, dto.getRank());
        assertEquals("test-performanceLevel", dto.getPerformanceLevel());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.CategoryPerformanceDto dto = new AgentQualityProfileDto.CategoryPerformanceDto();
        dto.setCategory("val-category");
        dto.setReviewCount(99);
        dto.setTrend("val-trend");
        dto.setRank(99);
        dto.setPerformanceLevel("val-performanceLevel");
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getReviewCount());
        assertEquals("val-trend", dto.getTrend());
        assertEquals(99, dto.getRank());
        assertEquals("val-performanceLevel", dto.getPerformanceLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.CategoryPerformanceDto dto1 = AgentQualityProfileDto.CategoryPerformanceDto.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend("test-trend")
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        AgentQualityProfileDto.CategoryPerformanceDto dto2 = AgentQualityProfileDto.CategoryPerformanceDto.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend("test-trend")
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.CategoryPerformanceDto dto = AgentQualityProfileDto.CategoryPerformanceDto.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend("test-trend")
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}