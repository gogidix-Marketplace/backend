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
class ScorecardTemplateDtoTest {

        @Test
    void testBuilder() {
        ScorecardTemplateDto dto = ScorecardTemplateDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .version("test-version")
            .isActive(true)
            .isDefault(true)
            .maxScore(null)
            .passingScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .totalCriteriaCount(42)
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .templateStatus("test-templateStatus")
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .linkedCalibrationSessions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-templateId", dto.getTemplateId());
        assertEquals("test-templateName", dto.getTemplateName());
        assertEquals("test-templateCode", dto.getTemplateCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-templateType", dto.getTemplateType());
        assertEquals("test-channelType", dto.getChannelType());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-version", dto.getVersion());
        assertTrue(dto.getIsActive());
        assertTrue(dto.getIsDefault());
        assertTrue(dto.getAllowPartialCredit());
        assertEquals(42, dto.getTotalCriteriaCount());
        assertTrue(dto.getCriticalFailureEnabled());
        assertEquals(42, dto.getCriticalFailureThreshold());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-createdByName", dto.getCreatedByName());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals("test-templateStatus", dto.getTemplateStatus());
        assertEquals(42L, dto.getUsageCount());
    }

    @Test
    void testSettersAndGetters() {
        ScorecardTemplateDto dto = new ScorecardTemplateDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setTemplateId("val-templateId");
        dto.setTemplateName("val-templateName");
        dto.setTemplateCode("val-templateCode");
        dto.setDescription("val-description");
        dto.setTemplateType("val-templateType");
        dto.setChannelType("val-channelType");
        dto.setCategory("val-category");
        dto.setVersion("val-version");
        dto.setIsActive(true);
        dto.setIsDefault(true);
        dto.setAllowPartialCredit(true);
        dto.setTotalCriteriaCount(99);
        dto.setCriticalFailureEnabled(true);
        dto.setCriticalFailureThreshold(99);
        dto.setCreatedBy("val-createdBy");
        dto.setCreatedByName("val-createdByName");
        dto.setApprovedBy("val-approvedBy");
        dto.setTemplateStatus("val-templateStatus");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals("val-templateName", dto.getTemplateName());
        assertEquals("val-templateCode", dto.getTemplateCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-templateType", dto.getTemplateType());
        assertEquals("val-channelType", dto.getChannelType());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-version", dto.getVersion());
        assertTrue(dto.getIsActive());
        assertTrue(dto.getIsDefault());
        assertTrue(dto.getAllowPartialCredit());
        assertEquals(99, dto.getTotalCriteriaCount());
        assertTrue(dto.getCriticalFailureEnabled());
        assertEquals(99, dto.getCriticalFailureThreshold());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-createdByName", dto.getCreatedByName());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals("val-templateStatus", dto.getTemplateStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        ScorecardTemplateDto dto1 = ScorecardTemplateDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .version("test-version")
            .isActive(true)
            .isDefault(true)
            .maxScore(null)
            .passingScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .totalCriteriaCount(42)
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .templateStatus("test-templateStatus")
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .linkedCalibrationSessions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ScorecardTemplateDto dto2 = ScorecardTemplateDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .version("test-version")
            .isActive(true)
            .isDefault(true)
            .maxScore(null)
            .passingScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .totalCriteriaCount(42)
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .templateStatus("test-templateStatus")
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .linkedCalibrationSessions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ScorecardTemplateDto dto = ScorecardTemplateDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .templateId("test-templateId")
            .templateName("test-templateName")
            .templateCode("test-templateCode")
            .description("test-description")
            .templateType("test-templateType")
            .channelType("test-channelType")
            .category("test-category")
            .version("test-version")
            .isActive(true)
            .isDefault(true)
            .maxScore(null)
            .passingScore(null)
            .passingPercentage(null)
            .weight(null)
            .allowPartialCredit(true)
            .criteriaSections(Collections.emptyList())
            .totalCriteriaCount(42)
            .criticalFailureEnabled(true)
            .criticalFailureThreshold(42)
            .createdBy("test-createdBy")
            .createdByName("test-createdByName")
            .approvedBy("test-approvedBy")
            .approvedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .templateStatus("test-templateStatus")
            .usageCount(42L)
            .lastUsedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveFrom(Instant.parse("2025-01-15T10:00:00Z"))
            .effectiveUntil(Instant.parse("2025-01-15T10:00:00Z"))
            .tags(Collections.emptyList())
            .linkedCalibrationSessions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}