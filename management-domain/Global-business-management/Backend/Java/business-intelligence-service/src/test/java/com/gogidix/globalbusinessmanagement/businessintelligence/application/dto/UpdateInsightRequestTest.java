package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.dto.UpdateInsightRequest;
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
class UpdateInsightRequestTest {

        @Test
    void testBuilder() {
        UpdateInsightRequest dto = UpdateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .status("test-status")
            .isVerified(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-impactLevel", dto.getImpactLevel());
        assertEquals(BigDecimal.TEN, dto.getConfidenceScore());
        assertEquals("test-sentiment", dto.getSentiment());
        assertEquals("test-status", dto.getStatus());
        assertTrue(dto.getIsVerified());
    }

    @Test
    void testSettersAndGetters() {
        UpdateInsightRequest dto = new UpdateInsightRequest();
        dto.setTitle("val-title");
        dto.setSummary("val-summary");
        dto.setDescription("val-description");
        dto.setImpactLevel("val-impactLevel");
        dto.setConfidenceScore(BigDecimal.ONE);
        dto.setSentiment("val-sentiment");
        dto.setStatus("val-status");
        dto.setIsVerified(true);
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-impactLevel", dto.getImpactLevel());
        assertEquals(BigDecimal.ONE, dto.getConfidenceScore());
        assertEquals("val-sentiment", dto.getSentiment());
        assertEquals("val-status", dto.getStatus());
        assertTrue(dto.getIsVerified());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdateInsightRequest dto1 = UpdateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .status("test-status")
            .isVerified(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        UpdateInsightRequest dto2 = UpdateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .status("test-status")
            .isVerified(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdateInsightRequest dto = UpdateInsightRequest.builder()
                        .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .impactLevel("test-impactLevel")
            .confidenceScore(BigDecimal.TEN)
            .sentiment("test-sentiment")
            .status("test-status")
            .isVerified(true)
            .tags(Collections.emptyList())
            .attributes(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}