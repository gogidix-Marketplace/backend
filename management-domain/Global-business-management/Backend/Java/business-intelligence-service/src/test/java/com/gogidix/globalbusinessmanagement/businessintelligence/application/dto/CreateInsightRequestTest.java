package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.CreateInsightRequest;
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
class CreateInsightRequestTest {

        @Test
    void testBuilder() {
        CreateInsightRequest dto = CreateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .insightType("test-insightType")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .periodId("test-periodId")
            .source("test-source")
            .createdBy("test-createdBy")
            .isAiGenerated(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-insightType", dto.getInsightType());
        assertEquals("test-impactLevel", dto.getImpactLevel());
        assertEquals(BigDecimal.TEN, dto.getConfidenceScore());
        assertEquals("test-sentiment", dto.getSentiment());
        assertEquals("test-entityCode", dto.getEntityCode());
        assertEquals("test-entityType", dto.getEntityType());
        assertEquals("test-periodId", dto.getPeriodId());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertTrue(dto.getIsAiGenerated());
    }

    @Test
    void testSettersAndGetters() {
        CreateInsightRequest dto = new CreateInsightRequest();
        dto.setTitle("val-title");
        dto.setSummary("val-summary");
        dto.setDescription("val-description");
        dto.setInsightType("val-insightType");
        dto.setImpactLevel("val-impactLevel");
        dto.setConfidenceScore(BigDecimal.ONE);
        dto.setSentiment("val-sentiment");
        dto.setEntityCode("val-entityCode");
        dto.setEntityType("val-entityType");
        dto.setPeriodId("val-periodId");
        dto.setSource("val-source");
        dto.setCreatedBy("val-createdBy");
        dto.setIsAiGenerated(true);
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-insightType", dto.getInsightType());
        assertEquals("val-impactLevel", dto.getImpactLevel());
        assertEquals(BigDecimal.ONE, dto.getConfidenceScore());
        assertEquals("val-sentiment", dto.getSentiment());
        assertEquals("val-entityCode", dto.getEntityCode());
        assertEquals("val-entityType", dto.getEntityType());
        assertEquals("val-periodId", dto.getPeriodId());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertTrue(dto.getIsAiGenerated());
    }

    @Test
    void testEqualsAndHashCode() {
        CreateInsightRequest dto1 = CreateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .insightType("test-insightType")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .periodId("test-periodId")
            .source("test-source")
            .createdBy("test-createdBy")
            .isAiGenerated(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        CreateInsightRequest dto2 = CreateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .insightType("test-insightType")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .periodId("test-periodId")
            .source("test-source")
            .createdBy("test-createdBy")
            .isAiGenerated(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreateInsightRequest dto = CreateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .insightType("test-insightType")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .entityCode("test-entityCode")
            .entityType("test-entityType")
            .periodId("test-periodId")
            .source("test-source")
            .createdBy("test-createdBy")
            .isAiGenerated(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}