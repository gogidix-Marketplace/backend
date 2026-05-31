package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.LeadCaptureRequest;
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
class LeadCaptureRequestTest {

        @Test
    void testBuilder() {
        LeadCaptureRequest dto = LeadCaptureRequest.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .country("test-country")
            .message("test-message")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .referralUrl("test-referralUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-phone", dto.getPhone());
        assertEquals("test-company", dto.getCompany());
        assertEquals("test-jobTitle", dto.getJobTitle());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-country", dto.getCountry());
        assertEquals("test-message", dto.getMessage());
        assertEquals("test-source", dto.getSource());
        assertEquals("test-medium", dto.getMedium());
        assertEquals("test-campaign", dto.getCampaign());
        assertEquals("test-referralUrl", dto.getReferralUrl());
    }

    @Test
    void testSettersAndGetters() {
        LeadCaptureRequest dto = new LeadCaptureRequest();
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setEmail("val-email");
        dto.setPhone("val-phone");
        dto.setCompany("val-company");
        dto.setJobTitle("val-jobTitle");
        dto.setIndustry("val-industry");
        dto.setCountry("val-country");
        dto.setMessage("val-message");
        dto.setSource("val-source");
        dto.setMedium("val-medium");
        dto.setCampaign("val-campaign");
        dto.setReferralUrl("val-referralUrl");
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-phone", dto.getPhone());
        assertEquals("val-company", dto.getCompany());
        assertEquals("val-jobTitle", dto.getJobTitle());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-country", dto.getCountry());
        assertEquals("val-message", dto.getMessage());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-medium", dto.getMedium());
        assertEquals("val-campaign", dto.getCampaign());
        assertEquals("val-referralUrl", dto.getReferralUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        LeadCaptureRequest dto1 = LeadCaptureRequest.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .country("test-country")
            .message("test-message")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .referralUrl("test-referralUrl")
            .build();
        LeadCaptureRequest dto2 = LeadCaptureRequest.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .country("test-country")
            .message("test-message")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .referralUrl("test-referralUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadCaptureRequest dto = LeadCaptureRequest.builder()
                        .firstName("test-firstName")
            .lastName("test-lastName")
            .email("test-email")
            .phone("test-phone")
            .company("test-company")
            .jobTitle("test-jobTitle")
            .industry("test-industry")
            .country("test-country")
            .message("test-message")
            .source("test-source")
            .medium("test-medium")
            .campaign("test-campaign")
            .referralUrl("test-referralUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}