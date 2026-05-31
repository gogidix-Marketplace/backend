package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.AgentQualityProfile;
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
class AgentQualityProfile_CategoryPerformanceTest {

        @Test
    void testBuilder() {
        AgentQualityProfile.CategoryPerformance dto = AgentQualityProfile.CategoryPerformance.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend(null)
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getReviewCount());
        assertEquals(42, dto.getRank());
        assertEquals("test-performanceLevel", dto.getPerformanceLevel());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfile.CategoryPerformance dto = new AgentQualityProfile.CategoryPerformance();
        dto.setCategory("val-category");
        dto.setReviewCount(99);
        dto.setRank(99);
        dto.setPerformanceLevel("val-performanceLevel");
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getReviewCount());
        assertEquals(99, dto.getRank());
        assertEquals("val-performanceLevel", dto.getPerformanceLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfile.CategoryPerformance dto1 = AgentQualityProfile.CategoryPerformance.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend(null)
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        AgentQualityProfile.CategoryPerformance dto2 = AgentQualityProfile.CategoryPerformance.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend(null)
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfile.CategoryPerformance dto = AgentQualityProfile.CategoryPerformance.builder()
                        .category("test-category")
            .averageScore(null)
            .maxScore(null)
            .reviewCount(42)
            .trend(null)
            .rank(42)
            .performanceLevel("test-performanceLevel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}