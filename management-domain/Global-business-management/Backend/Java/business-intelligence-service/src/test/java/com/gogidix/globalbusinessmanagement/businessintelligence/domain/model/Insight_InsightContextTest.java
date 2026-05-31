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
class Insight_InsightContextTest {

        @Test
    void testBuilder() {
        Insight.InsightContext dto = Insight.InsightContext.builder()
                        .businessContext("test-businessContext")
            .marketContext("test-marketContext")
            .competitorContext("test-competitorContext")
            .regulatoryContext("test-regulatoryContext")
            .environmentalFactors(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-businessContext", dto.getBusinessContext());
        assertEquals("test-marketContext", dto.getMarketContext());
        assertEquals("test-competitorContext", dto.getCompetitorContext());
        assertEquals("test-regulatoryContext", dto.getRegulatoryContext());
    }

    @Test
    void testSettersAndGetters() {
        Insight.InsightContext dto = new Insight.InsightContext();
        dto.setBusinessContext("val-businessContext");
        dto.setMarketContext("val-marketContext");
        dto.setCompetitorContext("val-competitorContext");
        dto.setRegulatoryContext("val-regulatoryContext");
        assertEquals("val-businessContext", dto.getBusinessContext());
        assertEquals("val-marketContext", dto.getMarketContext());
        assertEquals("val-competitorContext", dto.getCompetitorContext());
        assertEquals("val-regulatoryContext", dto.getRegulatoryContext());
    }

    @Test
    void testEqualsAndHashCode() {
        Insight.InsightContext dto1 = Insight.InsightContext.builder()
                        .businessContext("test-businessContext")
            .marketContext("test-marketContext")
            .competitorContext("test-competitorContext")
            .regulatoryContext("test-regulatoryContext")
            .environmentalFactors(Collections.emptyMap())
            .build();
        Insight.InsightContext dto2 = Insight.InsightContext.builder()
                        .businessContext("test-businessContext")
            .marketContext("test-marketContext")
            .competitorContext("test-competitorContext")
            .regulatoryContext("test-regulatoryContext")
            .environmentalFactors(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Insight.InsightContext dto = Insight.InsightContext.builder()
                        .businessContext("test-businessContext")
            .marketContext("test-marketContext")
            .competitorContext("test-competitorContext")
            .regulatoryContext("test-regulatoryContext")
            .environmentalFactors(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}