package com.gogidix.sales.leadmanagement.interfaces.rest;

import com.gogidix.sales.leadmanagement.interfaces.rest.LeadController;
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
class LeadController_UpdateLeadRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        LeadController.UpdateLeadRequestDto dto = new LeadController.UpdateLeadRequestDto();
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setMobilePhone("val-mobilePhone");
        dto.setCompany("val-company");
        dto.setTitle("val-title");
        dto.setIndustry("val-industry");
        dto.setCompanySize("val-companySize");
        dto.setWebsite("val-website");
        dto.setLinkedInUrl("val-linkedInUrl");
        dto.setTerritory("val-territory");
        dto.setRegion("val-region");
        dto.setSegment("val-segment");
        dto.setBudget(99);
        dto.setAuthority(99);
        dto.setNeed(99);
        dto.setTimeline(99);
        dto.setCurrency("val-currency");
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-mobilePhone", dto.getMobilePhone());
        assertEquals("val-company", dto.getCompany());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-companySize", dto.getCompanySize());
        assertEquals("val-website", dto.getWebsite());
        assertEquals("val-linkedInUrl", dto.getLinkedInUrl());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-segment", dto.getSegment());
        assertEquals(99, dto.getBudget());
        assertEquals(99, dto.getAuthority());
        assertEquals(99, dto.getNeed());
        assertEquals(99, dto.getTimeline());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadController.UpdateLeadRequestDto dto1 = new LeadController.UpdateLeadRequestDto();
        LeadController.UpdateLeadRequestDto dto2 = new LeadController.UpdateLeadRequestDto();
        dto1.setFirstName("test");
        dto1.setLastName("test");
        dto1.setEmail("test");
        dto1.setPhone("test");
        dto1.setMobilePhone("test");
        dto1.setCompany("test");
        dto1.setTitle("test");
        dto1.setIndustry("test");
        dto1.setCompanySize("test");
        dto1.setWebsite("test");
        dto1.setLinkedInUrl("test");
        dto1.setTerritory("test");
        dto1.setRegion("test");
        dto1.setSegment("test");
        dto1.setBudget(42);
        dto1.setAuthority(42);
        dto1.setNeed(42);
        dto1.setTimeline(42);
        dto1.setEstimatedValue(null);
        dto1.setCurrency("test");
        dto1.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto1.setNotes("test");
        dto1.setTags(Collections.emptyList());
        dto2.setFirstName("test");
        dto2.setLastName("test");
        dto2.setEmail("test");
        dto2.setPhone("test");
        dto2.setMobilePhone("test");
        dto2.setCompany("test");
        dto2.setTitle("test");
        dto2.setIndustry("test");
        dto2.setCompanySize("test");
        dto2.setWebsite("test");
        dto2.setLinkedInUrl("test");
        dto2.setTerritory("test");
        dto2.setRegion("test");
        dto2.setSegment("test");
        dto2.setBudget(42);
        dto2.setAuthority(42);
        dto2.setNeed(42);
        dto2.setTimeline(42);
        dto2.setEstimatedValue(null);
        dto2.setCurrency("test");
        dto2.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto2.setNotes("test");
        dto2.setTags(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFirstName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        LeadController.UpdateLeadRequestDto dto = new LeadController.UpdateLeadRequestDto();
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setCompany("test");
        dto.setTitle("test");
        dto.setIndustry("test");
        dto.setCompanySize("test");
        dto.setWebsite("test");
        dto.setLinkedInUrl("test");
        dto.setTerritory("test");
        dto.setRegion("test");
        dto.setSegment("test");
        dto.setBudget(42);
        dto.setAuthority(42);
        dto.setNeed(42);
        dto.setTimeline(42);
        dto.setEstimatedValue(null);
        dto.setCurrency("test");
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        LeadController.UpdateLeadRequestDto dto = new LeadController.UpdateLeadRequestDto();
        dto.setFirstName("test");
        dto.setLastName("test");
        dto.setEmail("test");
        dto.setPhone("test");
        dto.setMobilePhone("test");
        dto.setCompany("test");
        dto.setTitle("test");
        dto.setIndustry("test");
        dto.setCompanySize("test");
        dto.setWebsite("test");
        dto.setLinkedInUrl("test");
        dto.setTerritory("test");
        dto.setRegion("test");
        dto.setSegment("test");
        dto.setBudget(42);
        dto.setAuthority(42);
        dto.setNeed(42);
        dto.setTimeline(42);
        dto.setEstimatedValue(null);
        dto.setCurrency("test");
        dto.setExpectedCloseDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        dto.setTags(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}