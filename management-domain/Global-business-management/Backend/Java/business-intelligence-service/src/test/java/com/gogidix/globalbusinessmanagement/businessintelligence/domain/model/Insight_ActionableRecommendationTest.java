package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Insight;
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
class Insight_ActionableRecommendationTest {

        @Test
    void testBuilder() {
        Insight.ActionableRecommendation dto = Insight.ActionableRecommendation.builder()
                        .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .assignedTo("test-assignedTo")
            .estimatedEffort(42)
            .estimatedImpact(BigDecimal.TEN)
            .timeline("test-timeline")
            .actionItems(Collections.emptyList())
            .status(null)
            .dueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals(42, dto.getEstimatedEffort());
        assertEquals(BigDecimal.TEN, dto.getEstimatedImpact());
        assertEquals("test-timeline", dto.getTimeline());
    }

    @Test
    void testSettersAndGetters() {
        Insight.ActionableRecommendation dto = new Insight.ActionableRecommendation();
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setAssignedTo("val-assignedTo");
        dto.setEstimatedEffort(99);
        dto.setEstimatedImpact(BigDecimal.ONE);
        dto.setTimeline("val-timeline");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals(99, dto.getEstimatedEffort());
        assertEquals(BigDecimal.ONE, dto.getEstimatedImpact());
        assertEquals("val-timeline", dto.getTimeline());
    }

    @Test
    void testEqualsAndHashCode() {
        Insight.ActionableRecommendation dto1 = Insight.ActionableRecommendation.builder()
                        .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .assignedTo("test-assignedTo")
            .estimatedEffort(42)
            .estimatedImpact(BigDecimal.TEN)
            .timeline("test-timeline")
            .actionItems(Collections.emptyList())
            .status(null)
            .dueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        Insight.ActionableRecommendation dto2 = Insight.ActionableRecommendation.builder()
                        .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .assignedTo("test-assignedTo")
            .estimatedEffort(42)
            .estimatedImpact(BigDecimal.TEN)
            .timeline("test-timeline")
            .actionItems(Collections.emptyList())
            .status(null)
            .dueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Insight.ActionableRecommendation dto = Insight.ActionableRecommendation.builder()
                        .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .assignedTo("test-assignedTo")
            .estimatedEffort(42)
            .estimatedImpact(BigDecimal.TEN)
            .timeline("test-timeline")
            .actionItems(Collections.emptyList())
            .status(null)
            .dueBy(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}