package com.gogidix.management.executive.approval.domain.model;

import com.gogidix.management.executive.approval.domain.model.ExecutiveSummary;
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
class ExecutiveSummary_InsightTest {

        @Test
    void testBuilder() {
        ExecutiveSummary.Insight dto = ExecutiveSummary.Insight.builder()
                        .id("test-id")
            .category("test-category")
            .title("test-title")
            .description("test-description")
            .impact(null)
            .recommendation("test-recommendation")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-recommendation", dto.getRecommendation());
    }

    @Test
    void testSettersAndGetters() {
        ExecutiveSummary.Insight dto = new ExecutiveSummary.Insight();
        dto.setId("val-id");
        dto.setCategory("val-category");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setRecommendation("val-recommendation");
        assertEquals("val-id", dto.getId());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-recommendation", dto.getRecommendation());
    }

    @Test
    void testEqualsAndHashCode() {
        ExecutiveSummary.Insight dto1 = ExecutiveSummary.Insight.builder()
                        .id("test-id")
            .category("test-category")
            .title("test-title")
            .description("test-description")
            .impact(null)
            .recommendation("test-recommendation")
            .build();
        ExecutiveSummary.Insight dto2 = ExecutiveSummary.Insight.builder()
                        .id("test-id")
            .category("test-category")
            .title("test-title")
            .description("test-description")
            .impact(null)
            .recommendation("test-recommendation")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ExecutiveSummary.Insight dto = ExecutiveSummary.Insight.builder()
                        .id("test-id")
            .category("test-category")
            .title("test-title")
            .description("test-description")
            .impact(null)
            .recommendation("test-recommendation")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}