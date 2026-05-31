package com.gogidix.digitalmarketing.leadgeneration.application.dto;

import com.gogidix.digitalmarketing.leadgeneration.application.dto.LeadRequestDTO;
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
class LeadRequestDTOTest {

        @Test
    void testBuilder() {
        LeadRequestDTO dto = LeadRequestDTO.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
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
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .consentGranted(true)
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .utmParameters(Collections.emptyMap())
            .referrerUrl("test-referrerUrl")
            .landingPageUrl("test-landingPageUrl")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
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
        assertEquals("test-budget", dto.getBudget());
        assertEquals("test-timeline", dto.getTimeline());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-notes", dto.getNotes());
        assertTrue(dto.getConsentGranted());
        assertEquals("test-campaignId", dto.getCampaignId());
        assertEquals("test-referredBy", dto.getReferredBy());
        assertEquals("test-referrerUrl", dto.getReferrerUrl());
        assertEquals("test-landingPageUrl", dto.getLandingPageUrl());
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
    }

    @Test
    void testSettersAndGetters() {
        LeadRequestDTO dto = new LeadRequestDTO();
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
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
        dto.setBudget("val-budget");
        dto.setTimeline("val-timeline");
        dto.setAssignedTo("val-assignedTo");
        dto.setNotes("val-notes");
        dto.setConsentGranted(true);
        dto.setCampaignId("val-campaignId");
        dto.setReferredBy("val-referredBy");
        dto.setReferrerUrl("val-referrerUrl");
        dto.setLandingPageUrl("val-landingPageUrl");
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
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
        assertEquals("val-budget", dto.getBudget());
        assertEquals("val-timeline", dto.getTimeline());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-notes", dto.getNotes());
        assertTrue(dto.getConsentGranted());
        assertEquals("val-campaignId", dto.getCampaignId());
        assertEquals("val-referredBy", dto.getReferredBy());
        assertEquals("val-referrerUrl", dto.getReferrerUrl());
        assertEquals("val-landingPageUrl", dto.getLandingPageUrl());
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadRequestDTO dto1 = LeadRequestDTO.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
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
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .consentGranted(true)
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .utmParameters(Collections.emptyMap())
            .referrerUrl("test-referrerUrl")
            .landingPageUrl("test-landingPageUrl")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        LeadRequestDTO dto2 = LeadRequestDTO.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
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
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .consentGranted(true)
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .utmParameters(Collections.emptyMap())
            .referrerUrl("test-referrerUrl")
            .landingPageUrl("test-landingPageUrl")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadRequestDTO dto = LeadRequestDTO.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
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
            .budget("test-budget")
            .timeline("test-timeline")
            .assignedTo("test-assignedTo")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .metadata(Collections.emptyMap())
            .consentGranted(true)
            .campaignId("test-campaignId")
            .referredBy("test-referredBy")
            .utmParameters(Collections.emptyMap())
            .referrerUrl("test-referrerUrl")
            .landingPageUrl("test-landingPageUrl")
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}