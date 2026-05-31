package com.gogidix.globalbusinessmanagement.datavalidation.domain.model;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
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
class DataQualityReport_QualityRecommendationTest {

        @Test
    void testBuilder() {
        DataQualityReport.QualityRecommendation dto = DataQualityReport.QualityRecommendation.builder()
                        .recommendationId("test-recommendationId")
            .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .estimatedImpact(42)
            .action("test-action")
            .build();
        assertNotNull(dto);
        assertEquals("test-recommendationId", dto.getRecommendationId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getEstimatedImpact());
        assertEquals("test-action", dto.getAction());
    }

    @Test
    void testSettersAndGetters() {
        DataQualityReport.QualityRecommendation dto = new DataQualityReport.QualityRecommendation();
        dto.setRecommendationId("val-recommendationId");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setEstimatedImpact(99);
        dto.setAction("val-action");
        assertEquals("val-recommendationId", dto.getRecommendationId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getEstimatedImpact());
        assertEquals("val-action", dto.getAction());
    }

    @Test
    void testEqualsAndHashCode() {
        DataQualityReport.QualityRecommendation dto1 = DataQualityReport.QualityRecommendation.builder()
                        .recommendationId("test-recommendationId")
            .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .estimatedImpact(42)
            .action("test-action")
            .build();
        DataQualityReport.QualityRecommendation dto2 = DataQualityReport.QualityRecommendation.builder()
                        .recommendationId("test-recommendationId")
            .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .estimatedImpact(42)
            .action("test-action")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DataQualityReport.QualityRecommendation dto = DataQualityReport.QualityRecommendation.builder()
                        .recommendationId("test-recommendationId")
            .title("test-title")
            .description("test-description")
            .priority(null)
            .category("test-category")
            .estimatedImpact(42)
            .action("test-action")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}