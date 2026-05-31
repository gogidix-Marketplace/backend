package com.gogidix.digitalmarketing.leadgeneration.application.dto;

import com.gogidix.digitalmarketing.leadgeneration.application.dto.LeadResponseDTO;
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
class LeadResponseDTOTest {

        @Test
    void testBuilder() {
        LeadResponseDTO dto = LeadResponseDTO.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .region("test-region")
            .source("test-source")
            .sourceDetail("test-sourceDetail")
            .status("test-status")
            .score(42)
            .temperature("test-temperature")
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastActivityAt(Instant.parse("2025-01-15T10:00:00Z"))
            .convertedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lostReason("test-lostReason")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .optOut(true)
            .consentGranted(true)
            .consentGrantedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .engagementCount(42)
            .lastEngagementScore(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .isQualified(true)
            .isHot(true)
            .isWarm(true)
            .isCold(true)
            .isAssigned(true)
            .isConverted(true)
            .isLost(true)
            .isNew(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-fullName", dto.getFullName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-company", dto.getCompany());
        assertEquals("test-jobTitle", dto.getJobTitle());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-companySize", dto.getCompanySize());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-sourceDetail", dto.getSourceDetail());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getScore());
        assertEquals("test-temperature", dto.getTemperature());
        assertEquals("test-budget", dto.getBudget());
        assertEquals("test-timeline", dto.getTimeline());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-lostReason", dto.getLostReason());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getOptOut());
        assertTrue(dto.getConsentGranted());
        assertEquals("test-campaignId", dto.getCampaignId());
        assertEquals("test-referredBy", dto.getReferredBy());
        assertEquals(42, dto.getEngagementCount());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
        assertTrue(dto.getIsQualified());
        assertTrue(dto.getIsHot());
        assertTrue(dto.getIsWarm());
        assertTrue(dto.getIsCold());
        assertTrue(dto.getIsAssigned());
        assertTrue(dto.getIsConverted());
        assertTrue(dto.getIsLost());
        assertTrue(dto.getIsNew());
    }

    @Test
    void testSettersAndGetters() {
        LeadResponseDTO dto = new LeadResponseDTO();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setFullName("val-fullName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setCompany("val-company");
        dto.setJobTitle("val-jobTitle");
        dto.setIndustry("val-industry");
        dto.setCompanySize("val-companySize");
        dto.setCountry("val-country");
        dto.setRegion("val-region");
        dto.setSource("val-source");
        dto.setSourceDetail("val-sourceDetail");
        dto.setStatus("val-status");
        dto.setScore(99);
        dto.setTemperature("val-temperature");
        dto.setBudget("val-budget");
        dto.setTimeline("val-timeline");
        dto.setAssignedTo("val-assignedTo");
        dto.setLostReason("val-lostReason");
        dto.setNotes("val-notes");
        dto.setOptOut(true);
        dto.setConsentGranted(true);
        dto.setCampaignId("val-campaignId");
        dto.setReferredBy("val-referredBy");
        dto.setEngagementCount(99);
        dto.setCreatedBy("val-createdBy");
        dto.setUpdatedBy("val-updatedBy");
        dto.setIsQualified(true);
        dto.setIsHot(true);
        dto.setIsWarm(true);
        dto.setIsCold(true);
        dto.setIsAssigned(true);
        dto.setIsConverted(true);
        dto.setIsLost(true);
        dto.setIsNew(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-fullName", dto.getFullName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-company", dto.getCompany());
        assertEquals("val-jobTitle", dto.getJobTitle());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-companySize", dto.getCompanySize());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-sourceDetail", dto.getSourceDetail());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getScore());
        assertEquals("val-temperature", dto.getTemperature());
        assertEquals("val-budget", dto.getBudget());
        assertEquals("val-timeline", dto.getTimeline());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-lostReason", dto.getLostReason());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getOptOut());
        assertTrue(dto.getConsentGranted());
        assertEquals("val-campaignId", dto.getCampaignId());
        assertEquals("val-referredBy", dto.getReferredBy());
        assertEquals(99, dto.getEngagementCount());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
        assertTrue(dto.getIsQualified());
        assertTrue(dto.getIsHot());
        assertTrue(dto.getIsWarm());
        assertTrue(dto.getIsCold());
        assertTrue(dto.getIsAssigned());
        assertTrue(dto.getIsConverted());
        assertTrue(dto.getIsLost());
        assertTrue(dto.getIsNew());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadResponseDTO dto1 = LeadResponseDTO.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .region("test-region")
            .source("test-source")
            .sourceDetail("test-sourceDetail")
            .status("test-status")
            .score(42)
            .temperature("test-temperature")
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastActivityAt(Instant.parse("2025-01-15T10:00:00Z"))
            .convertedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lostReason("test-lostReason")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .optOut(true)
            .consentGranted(true)
            .consentGrantedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .engagementCount(42)
            .lastEngagementScore(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .isQualified(true)
            .isHot(true)
            .isWarm(true)
            .isCold(true)
            .isAssigned(true)
            .isConverted(true)
            .isLost(true)
            .isNew(true)
            .build();
        LeadResponseDTO dto2 = LeadResponseDTO.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .region("test-region")
            .source("test-source")
            .sourceDetail("test-sourceDetail")
            .status("test-status")
            .score(42)
            .temperature("test-temperature")
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastActivityAt(Instant.parse("2025-01-15T10:00:00Z"))
            .convertedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lostReason("test-lostReason")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .optOut(true)
            .consentGranted(true)
            .consentGrantedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .engagementCount(42)
            .lastEngagementScore(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .isQualified(true)
            .isHot(true)
            .isWarm(true)
            .isCold(true)
            .isAssigned(true)
            .isConverted(true)
            .isLost(true)
            .isNew(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadResponseDTO dto = LeadResponseDTO.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .region("test-region")
            .source("test-source")
            .sourceDetail("test-sourceDetail")
            .status("test-status")
            .score(42)
            .temperature("test-temperature")
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .assignedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lastActivityAt(Instant.parse("2025-01-15T10:00:00Z"))
            .convertedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .lostReason("test-lostReason")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .optOut(true)
            .consentGranted(true)
            .consentGrantedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .engagementCount(42)
            .lastEngagementScore(null)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .isQualified(true)
            .isHot(true)
            .isWarm(true)
            .isCold(true)
            .isAssigned(true)
            .isConverted(true)
            .isLost(true)
            .isNew(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}