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
class ScorecardTemplateDto_ScorecardTemplateSummaryDtoTest {

        @Test
    void testBuilder() {
        ScorecardTemplateDto.ScorecardTemplateSummaryDto dto = ScorecardTemplateDto.ScorecardTemplateSummaryDto.builder()
                        .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .maxScore(null)
            .passingScore(null)
            .templateStatus("test-templateStatus")
            .isActive(true)
            .isDefault(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalCriteriaCount(42)
            .hasCriticalCriteria(true)
            .isValid(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-templateName", dto.getTemplateName());
        assertEquals("test-templateCode", dto.getTemplateCode());
        assertEquals("test-templateType", dto.getTemplateType());
        assertEquals("test-channelType", dto.getChannelType());
        assertEquals("test-templateStatus", dto.getTemplateStatus());
        assertTrue(dto.getIsActive());
        assertTrue(dto.getIsDefault());
        assertEquals(42L, dto.getUsageCount());
        assertEquals(42, dto.getTotalCriteriaCount());
        assertTrue(dto.getHasCriticalCriteria());
        assertTrue(dto.getIsValid());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplateDto.ScorecardTemplateSummaryDto dto = new ScorecardTemplateDto.ScorecardTemplateSummaryDto();
        dto.setTemplateId("val-templateId");
        dto.setTemplateName("val-templateName");
        dto.setTemplateCode("val-templateCode");
        dto.setTemplateType("val-templateType");
        dto.setChannelType("val-channelType");
        dto.setTemplateStatus("val-templateStatus");
        dto.setIsActive(true);
        dto.setIsDefault(true);
        dto.setTotalCriteriaCount(99);
        dto.setHasCriticalCriteria(true);
        dto.setIsValid(true);
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-templateName", dto.getTemplateName());
        assertEquals("val-templateCode", dto.getTemplateCode());
        assertEquals("val-templateType", dto.getTemplateType());
        assertEquals("val-channelType", dto.getChannelType());
        assertEquals("val-templateStatus", dto.getTemplateStatus());
        assertTrue(dto.getIsActive());
        assertTrue(dto.getIsDefault());
        assertEquals(99, dto.getTotalCriteriaCount());
        assertTrue(dto.getHasCriticalCriteria());
        assertTrue(dto.getIsValid());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplateDto.ScorecardTemplateSummaryDto dto1 = ScorecardTemplateDto.ScorecardTemplateSummaryDto.builder()
                        .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .maxScore(null)
            .passingScore(null)
            .templateStatus("test-templateStatus")
            .isActive(true)
            .isDefault(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalCriteriaCount(42)
            .hasCriticalCriteria(true)
            .isValid(true)
            .build();
        ScorecardTemplateDto.ScorecardTemplateSummaryDto dto2 = ScorecardTemplateDto.ScorecardTemplateSummaryDto.builder()
                        .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .maxScore(null)
            .passingScore(null)
            .templateStatus("test-templateStatus")
            .isActive(true)
            .isDefault(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalCriteriaCount(42)
            .hasCriticalCriteria(true)
            .isValid(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplateDto.ScorecardTemplateSummaryDto dto = ScorecardTemplateDto.ScorecardTemplateSummaryDto.builder()
                        .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .maxScore(null)
            .passingScore(null)
            .templateStatus("test-templateStatus")
            .isActive(true)
            .isDefault(true)
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .totalCriteriaCount(42)
            .hasCriticalCriteria(true)
            .isValid(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}