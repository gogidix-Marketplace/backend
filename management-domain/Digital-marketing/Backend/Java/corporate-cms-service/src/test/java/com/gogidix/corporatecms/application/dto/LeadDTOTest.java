package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.LeadDTO;
import com.gogidix.corporatecms.domain.enums.LeadStatus;
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
class LeadDTOTest {

        @Test
    void testBuilder() {
        LeadDTO dto = LeadDTO.builder()
                        .id("test-id")
            .status(LeadStatus.NEW)
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .contentId("test-contentId")
            .formId("test-formId")
            .leadMagnet("test-leadMagnet")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .score(42)
            .convertedAt(LocalDateTime.of(2025,1,15,10,0))
            .customerId("test-customerId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-company", dto.getCompany());
        assertEquals("test-jobTitle", dto.getJobTitle());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-companySize", dto.getCompanySize());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-medium", dto.getMedium());
        assertEquals("test-campaign", dto.getCampaign());
        assertEquals("test-contentId", dto.getContentId());
        assertEquals("test-formId", dto.getFormId());
        assertEquals("test-leadMagnet", dto.getLeadMagnet());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedToName", dto.getAssignedToName());
        assertEquals("test-notes", dto.getNotes());
        assertEquals(42, dto.getScore());
        assertEquals("test-customerId", dto.getCustomerId());
    }

    @Test
    void testSettersAndGetters() {
        LeadDTO dto = new LeadDTO();
        dto.setId("val-id");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setCompany("val-company");
        dto.setJobTitle("val-jobTitle");
        dto.setIndustry("val-industry");
        dto.setCompanySize("val-companySize");
        dto.setCountry("val-country");
        dto.setSource("val-source");
        dto.setMedium("val-medium");
        dto.setCampaign("val-campaign");
        dto.setContentId("val-contentId");
        dto.setFormId("val-formId");
        dto.setLeadMagnet("val-leadMagnet");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setNotes("val-notes");
        dto.setScore(99);
        dto.setCustomerId("val-customerId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-company", dto.getCompany());
        assertEquals("val-jobTitle", dto.getJobTitle());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-companySize", dto.getCompanySize());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-medium", dto.getMedium());
        assertEquals("val-campaign", dto.getCampaign());
        assertEquals("val-contentId", dto.getContentId());
        assertEquals("val-formId", dto.getFormId());
        assertEquals("val-leadMagnet", dto.getLeadMagnet());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals("val-notes", dto.getNotes());
        assertEquals(99, dto.getScore());
        assertEquals("val-customerId", dto.getCustomerId());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadDTO dto1 = LeadDTO.builder()
                        .id("test-id")
            .status(LeadStatus.NEW)
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .contentId("test-contentId")
            .formId("test-formId")
            .leadMagnet("test-leadMagnet")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .score(42)
            .convertedAt(LocalDateTime.of(2025,1,15,10,0))
            .customerId("test-customerId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        LeadDTO dto2 = LeadDTO.builder()
                        .id("test-id")
            .status(LeadStatus.NEW)
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .contentId("test-contentId")
            .formId("test-formId")
            .leadMagnet("test-leadMagnet")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .score(42)
            .convertedAt(LocalDateTime.of(2025,1,15,10,0))
            .customerId("test-customerId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadDTO dto = LeadDTO.builder()
                        .id("test-id")
            .status(LeadStatus.NEW)
            .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .companySize("test-companySize")
            .country("test-country")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .contentId("test-contentId")
            .formId("test-formId")
            .leadMagnet("test-leadMagnet")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .notes("test-notes")
            .customFields(Collections.emptyMap())
            .score(42)
            .convertedAt(LocalDateTime.of(2025,1,15,10,0))
            .customerId("test-customerId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}