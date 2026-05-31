package com.gogidix.sales.leadmanagement.application.dto.response;

import com.gogidix.sales.leadmanagement.application.dto.response.LeadResponseDto;
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
class LeadResponseDtoTest {

        @Test
    void testBuilder() {
        LeadResponseDto dto = LeadResponseDto.builder()
                        .id("test-id")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .company("test-company")
            .title("test-title")
            .industry("test-industry")
            .companySize("test-companySize")
            .website("test-website")
            .linkedInUrl("test-linkedInUrl")
            .source("test-source")
            .sourceDetails("test-sourceDetails")
            .campaign("test-campaign")
            .stage("test-stage")
            .status("test-status")
            .quality("test-quality")
            .score(42)
            .probability(42)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .territory("test-territory")
            .region("test-region")
            .segment("test-segment")
            .budget(42)
            .authority(42)
            .need(42)
            .timeline(42)
            .bantScore(42)
            .emailOpens(42)
            .emailClicks(42)
            .webVisits(42)
            .formSubmissions(42)
            .lastActivityDate(LocalDate.of(2025,1,15))
            .firstContactDate(LocalDate.of(2025,1,15))
            .convertedDealId("test-convertedDealId")
            .convertedDate(LocalDate.of(2025,1,15))
            .conversionReason("test-conversionReason")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .lostDate(LocalDate.of(2025,1,15))
            .isDuplicate(true)
            .duplicateOfLeadId("test-duplicateOfLeadId")
            .duplicateMatchScore(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .estimatedValue(BigDecimal.TEN)
            .currency("test-currency")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .recentActivities(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-leadId", dto.getLeadId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-fullName", dto.getFullName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-mobilePhone", dto.getMobilePhone());
        assertEquals("test-company", dto.getCompany());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-companySize", dto.getCompanySize());
        assertEquals("test-website", dto.getWebsite());
        assertEquals("test-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-sourceDetails", dto.getSourceDetails());
        assertEquals("test-campaign", dto.getCampaign());
        assertEquals("test-stage", dto.getStage());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-quality", dto.getQuality());
        assertEquals(42, dto.getScore());
        assertEquals(42, dto.getProbability());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals("test-ownerName", dto.getOwnerName());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-segment", dto.getSegment());
        assertEquals(42, dto.getBudget());
        assertEquals(42, dto.getAuthority());
        assertEquals(42, dto.getNeed());
        assertEquals(42, dto.getTimeline());
        assertEquals(42, dto.getBantScore());
        assertEquals(42, dto.getEmailOpens());
        assertEquals(42, dto.getEmailClicks());
        assertEquals(42, dto.getWebVisits());
        assertEquals(42, dto.getFormSubmissions());
        assertEquals(LocalDate.of(2025,1,15), dto.getLastActivityDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getFirstContactDate());
        assertEquals("test-convertedDealId", dto.getConvertedDealId());
        assertEquals(LocalDate.of(2025,1,15), dto.getConvertedDate());
        assertEquals("test-conversionReason", dto.getConversionReason());
        assertEquals("test-lossReason", dto.getLossReason());
        assertEquals("test-lossReasonDetails", dto.getLossReasonDetails());
        assertEquals(LocalDate.of(2025,1,15), dto.getLostDate());
        assertTrue(dto.getIsDuplicate());
        assertEquals("test-duplicateOfLeadId", dto.getDuplicateOfLeadId());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(BigDecimal.TEN, dto.getEstimatedValue());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpectedCloseDate());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeadResponseDto dto = new LeadResponseDto();
        dto.setId("val-id");
        dto.setLeadId("val-leadId");
        dto.setTenantId("val-tenantId");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setFullName("val-fullName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setMobilePhone("val-mobilePhone");
        dto.setCompany("val-company");
        dto.setTitle("val-title");
        dto.setIndustry("val-industry");
        dto.setCompanySize("val-companySize");
        dto.setWebsite("val-website");
        dto.setLinkedInUrl("val-linkedInUrl");
        dto.setSource("val-source");
        dto.setSourceDetails("val-sourceDetails");
        dto.setCampaign("val-campaign");
        dto.setStage("val-stage");
        dto.setStatus("val-status");
        dto.setQuality("val-quality");
        dto.setScore(99);
        dto.setProbability(99);
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setTerritory("val-territory");
        dto.setRegion("val-region");
        dto.setSegment("val-segment");
        dto.setBudget(99);
        dto.setAuthority(99);
        dto.setNeed(99);
        dto.setTimeline(99);
        dto.setBantScore(99);
        dto.setEmailOpens(99);
        dto.setEmailClicks(99);
        dto.setWebVisits(99);
        dto.setFormSubmissions(99);
        dto.setLastActivityDate(LocalDate.of(2025,6,1));
        dto.setFirstContactDate(LocalDate.of(2025,6,1));
        dto.setConvertedDealId("val-convertedDealId");
        dto.setConvertedDate(LocalDate.of(2025,6,1));
        dto.setConversionReason("val-conversionReason");
        dto.setLossReason("val-lossReason");
        dto.setLossReasonDetails("val-lossReasonDetails");
        dto.setLostDate(LocalDate.of(2025,6,1));
        dto.setIsDuplicate(true);
        dto.setDuplicateOfLeadId("val-duplicateOfLeadId");
        dto.setNotes("val-notes");
        dto.setEstimatedValue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-leadId", dto.getLeadId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-fullName", dto.getFullName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-mobilePhone", dto.getMobilePhone());
        assertEquals("val-company", dto.getCompany());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-companySize", dto.getCompanySize());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-sourceDetails", dto.getSourceDetails());
        assertEquals("val-campaign", dto.getCampaign());
        assertEquals("val-stage", dto.getStage());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-quality", dto.getQuality());
        assertEquals(99, dto.getScore());
        assertEquals(99, dto.getProbability());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-segment", dto.getSegment());
        assertEquals(99, dto.getBudget());
        assertEquals(99, dto.getAuthority());
        assertEquals(99, dto.getNeed());
        assertEquals(99, dto.getTimeline());
        assertEquals(99, dto.getBantScore());
        assertEquals(99, dto.getEmailOpens());
        assertEquals(99, dto.getEmailClicks());
        assertEquals(99, dto.getWebVisits());
        assertEquals(99, dto.getFormSubmissions());
        assertEquals(LocalDate.of(2025,6,1), dto.getLastActivityDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getFirstContactDate());
        assertEquals("val-convertedDealId", dto.getConvertedDealId());
        assertEquals(LocalDate.of(2025,6,1), dto.getConvertedDate());
        assertEquals("val-conversionReason", dto.getConversionReason());
        assertEquals("val-lossReason", dto.getLossReason());
        assertEquals("val-lossReasonDetails", dto.getLossReasonDetails());
        assertEquals(LocalDate.of(2025,6,1), dto.getLostDate());
        assertTrue(dto.getIsDuplicate());
        assertEquals("val-duplicateOfLeadId", dto.getDuplicateOfLeadId());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(BigDecimal.ONE, dto.getEstimatedValue());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadResponseDto dto1 = LeadResponseDto.builder()
                        .id("test-id")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .company("test-company")
            .title("test-title")
            .industry("test-industry")
            .companySize("test-companySize")
            .website("test-website")
            .linkedInUrl("test-linkedInUrl")
            .source("test-source")
            .sourceDetails("test-sourceDetails")
            .campaign("test-campaign")
            .stage("test-stage")
            .status("test-status")
            .quality("test-quality")
            .score(42)
            .probability(42)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .territory("test-territory")
            .region("test-region")
            .segment("test-segment")
            .budget(42)
            .authority(42)
            .need(42)
            .timeline(42)
            .bantScore(42)
            .emailOpens(42)
            .emailClicks(42)
            .webVisits(42)
            .formSubmissions(42)
            .lastActivityDate(LocalDate.of(2025,1,15))
            .firstContactDate(LocalDate.of(2025,1,15))
            .convertedDealId("test-convertedDealId")
            .convertedDate(LocalDate.of(2025,1,15))
            .conversionReason("test-conversionReason")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .lostDate(LocalDate.of(2025,1,15))
            .isDuplicate(true)
            .duplicateOfLeadId("test-duplicateOfLeadId")
            .duplicateMatchScore(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .estimatedValue(BigDecimal.TEN)
            .currency("test-currency")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .recentActivities(Collections.emptyList())
            .build();
        LeadResponseDto dto2 = LeadResponseDto.builder()
                        .id("test-id")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .company("test-company")
            .title("test-title")
            .industry("test-industry")
            .companySize("test-companySize")
            .website("test-website")
            .linkedInUrl("test-linkedInUrl")
            .source("test-source")
            .sourceDetails("test-sourceDetails")
            .campaign("test-campaign")
            .stage("test-stage")
            .status("test-status")
            .quality("test-quality")
            .score(42)
            .probability(42)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .territory("test-territory")
            .region("test-region")
            .segment("test-segment")
            .budget(42)
            .authority(42)
            .need(42)
            .timeline(42)
            .bantScore(42)
            .emailOpens(42)
            .emailClicks(42)
            .webVisits(42)
            .formSubmissions(42)
            .lastActivityDate(LocalDate.of(2025,1,15))
            .firstContactDate(LocalDate.of(2025,1,15))
            .convertedDealId("test-convertedDealId")
            .convertedDate(LocalDate.of(2025,1,15))
            .conversionReason("test-conversionReason")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .lostDate(LocalDate.of(2025,1,15))
            .isDuplicate(true)
            .duplicateOfLeadId("test-duplicateOfLeadId")
            .duplicateMatchScore(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .estimatedValue(BigDecimal.TEN)
            .currency("test-currency")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .recentActivities(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadResponseDto dto = LeadResponseDto.builder()
                        .id("test-id")
            .leadId("test-leadId")
            .tenantId("test-tenantId")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .fullName("test-fullName")
            .email("test-email")
            .phone("test-phone")
            .mobilePhone("test-mobilePhone")
            .company("test-company")
            .title("test-title")
            .industry("test-industry")
            .companySize("test-companySize")
            .website("test-website")
            .linkedInUrl("test-linkedInUrl")
            .source("test-source")
            .sourceDetails("test-sourceDetails")
            .campaign("test-campaign")
            .stage("test-stage")
            .status("test-status")
            .quality("test-quality")
            .score(42)
            .probability(42)
            .ownerId("test-ownerId")
            .ownerName("test-ownerName")
            .teamMemberIds(Collections.emptyList())
            .territory("test-territory")
            .region("test-region")
            .segment("test-segment")
            .budget(42)
            .authority(42)
            .need(42)
            .timeline(42)
            .bantScore(42)
            .emailOpens(42)
            .emailClicks(42)
            .webVisits(42)
            .formSubmissions(42)
            .lastActivityDate(LocalDate.of(2025,1,15))
            .firstContactDate(LocalDate.of(2025,1,15))
            .convertedDealId("test-convertedDealId")
            .convertedDate(LocalDate.of(2025,1,15))
            .conversionReason("test-conversionReason")
            .lossReason("test-lossReason")
            .lossReasonDetails("test-lossReasonDetails")
            .lostDate(LocalDate.of(2025,1,15))
            .isDuplicate(true)
            .duplicateOfLeadId("test-duplicateOfLeadId")
            .duplicateMatchScore(null)
            .notes("test-notes")
            .tags(Collections.emptyList())
            .estimatedValue(BigDecimal.TEN)
            .currency("test-currency")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedBy("test-updatedBy")
            .recentActivities(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}