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
class Insight_InsightEvidenceTest {

        @Test
    void testBuilder() {
        Insight.InsightEvidence dto = Insight.InsightEvidence.builder()
                        .dataSources(Collections.emptyList())
            .supportingDocuments(Collections.emptyList())
            .references(Collections.emptyList())
            .evidenceScore(BigDecimal.TEN)
            .methodology("test-methodology")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getEvidenceScore());
        assertEquals("test-methodology", dto.getMethodology());
    }

    @Test
    void testSettersAndGetters() {
        Insight.InsightEvidence dto = new Insight.InsightEvidence();
        dto.setEvidenceScore(BigDecimal.ONE);
        dto.setMethodology("val-methodology");
        assertEquals(BigDecimal.ONE, dto.getEvidenceScore());
        assertEquals("val-methodology", dto.getMethodology());
    }

    @Test
    void testEqualsAndHashCode() {
        Insight.InsightEvidence dto1 = Insight.InsightEvidence.builder()
                        .dataSources(Collections.emptyList())
            .supportingDocuments(Collections.emptyList())
            .references(Collections.emptyList())
            .evidenceScore(BigDecimal.TEN)
            .methodology("test-methodology")
            .build();
        Insight.InsightEvidence dto2 = Insight.InsightEvidence.builder()
                        .dataSources(Collections.emptyList())
            .supportingDocuments(Collections.emptyList())
            .references(Collections.emptyList())
            .evidenceScore(BigDecimal.TEN)
            .methodology("test-methodology")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Insight.InsightEvidence dto = Insight.InsightEvidence.builder()
                        .dataSources(Collections.emptyList())
            .supportingDocuments(Collections.emptyList())
            .references(Collections.emptyList())
            .evidenceScore(BigDecimal.TEN)
            .methodology("test-methodology")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}