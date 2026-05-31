package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.ScorecardTemplateDto;
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
class ScorecardTemplateDto_UpdateScorecardTemplateRequestTest {

        @Test
    void testBuilder() {
        ScorecardTemplateDto.UpdateScorecardTemplateRequest dto = ScorecardTemplateDto.UpdateScorecardTemplateRequest.builder()
                        .templateName("test-templateName")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .maxScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-templateName", dto.getTemplateName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-templateType", dto.getTemplateType());
        assertEquals("test-channelType", dto.getChannelType());
        assertEquals("test-category", dto.getCategory());
        assertTrue(dto.getAllowPartialCredit());
        assertTrue(dto.getCriticalFailureEnabled());
        assertEquals(42, dto.getCriticalFailureThreshold());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplateDto.UpdateScorecardTemplateRequest dto = new ScorecardTemplateDto.UpdateScorecardTemplateRequest();
        dto.setTemplateName("val-templateName");
        dto.setDescription("val-description");
        dto.setTemplateType("val-templateType");
        dto.setChannelType("val-channelType");
        dto.setCategory("val-category");
        dto.setAllowPartialCredit(true);
        dto.setCriticalFailureEnabled(true);
        dto.setCriticalFailureThreshold(99);
        assertEquals("val-templateName", dto.getTemplateName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-templateType", dto.getTemplateType());
        assertEquals("val-channelType", dto.getChannelType());
        assertEquals("val-category", dto.getCategory());
        assertTrue(dto.getAllowPartialCredit());
        assertTrue(dto.getCriticalFailureEnabled());
        assertEquals(99, dto.getCriticalFailureThreshold());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplateDto.UpdateScorecardTemplateRequest dto1 = ScorecardTemplateDto.UpdateScorecardTemplateRequest.builder()
                        .templateName("test-templateName")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .maxScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .build();
        ScorecardTemplateDto.UpdateScorecardTemplateRequest dto2 = ScorecardTemplateDto.UpdateScorecardTemplateRequest.builder()
                        .templateName("test-templateName")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .maxScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplateDto.UpdateScorecardTemplateRequest dto = ScorecardTemplateDto.UpdateScorecardTemplateRequest.builder()
                        .templateName("test-templateName")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .maxScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}