package com.gogidix.sales.crm.application.dto.response;

import com.gogidix.sales.crm.application.dto.response.InteractionResponseDto;
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
class InteractionResponseDtoTest {

        @Test
    void testBuilder() {
        InteractionResponseDto dto = InteractionResponseDto.builder()
                        .id("test-id")
            .interactionId("test-interactionId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .type(InteractionResponseDto.InteractionTypeDto.CALL)
            .direction(InteractionResponseDto.InteractionDirectionDto.INBOUND)
            .interactionDate(LocalDateTime.of(2025,1,15,10,0))
            .durationMinutes(42)
            .subject("test-subject")
            .description("test-description")
            .outcome("test-outcome")
            .notes("test-notes")
            .location("test-location")
            .hasFollowUp(true)
            .followUpDate(LocalDate.of(2025,1,15))
            .followUpNotes("test-followUpNotes")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(InteractionResponseDto.InteractionStatusDto.SCHEDULED)
            .recordingUrl("test-recordingUrl")
            .attachmentUrls(Collections.emptyList())
            .participantContactIds(Collections.emptyList())
            .campaignId("test-campaignId")
            .dealId("test-dealId")
            .dealValue(null)
            .probability(42)
            .nextStep("test-nextStep")
            .nextStepDate(LocalDate.of(2025,1,15))
            .isHighPriority(true)
            .tags("test-tags")
            .relatedInteractions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-interactionId", dto.getInteractionId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-contactId", dto.getContactId());
        assertEquals("test-contactName", dto.getContactName());
        assertEquals(InteractionResponseDto.InteractionTypeDto.CALL, dto.getType());
        assertEquals(InteractionResponseDto.InteractionDirectionDto.INBOUND, dto.getDirection());
        assertEquals(42, dto.getDurationMinutes());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-outcome", dto.getOutcome());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-location", dto.getLocation());
        assertTrue(dto.getHasFollowUp());
        assertEquals(LocalDate.of(2025,1,15), dto.getFollowUpDate());
        assertEquals("test-followUpNotes", dto.getFollowUpNotes());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedToName", dto.getAssignedToName());
        assertEquals(InteractionResponseDto.InteractionStatusDto.SCHEDULED, dto.getStatus());
        assertEquals("test-recordingUrl", dto.getRecordingUrl());
        assertEquals("test-campaignId", dto.getCampaignId());
        assertEquals("test-dealId", dto.getDealId());
        assertEquals(42, dto.getProbability());
        assertEquals("test-nextStep", dto.getNextStep());
        assertEquals(LocalDate.of(2025,1,15), dto.getNextStepDate());
        assertTrue(dto.getIsHighPriority());
        assertEquals("test-tags", dto.getTags());
    }

    @Test
    void testSettersAndGetters() {
        InteractionResponseDto dto = new InteractionResponseDto();
        dto.setId("val-id");
        dto.setInteractionId("val-interactionId");
        dto.setTenantId("val-tenantId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setContactId("val-contactId");
        dto.setContactName("val-contactName");
        dto.setType(InteractionResponseDto.InteractionTypeDto.CALL);
        dto.setDirection(InteractionResponseDto.InteractionDirectionDto.INBOUND);
        dto.setDurationMinutes(99);
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        dto.setOutcome("val-outcome");
        dto.setNotes("val-notes");
        dto.setLocation("val-location");
        dto.setHasFollowUp(true);
        dto.setFollowUpDate(LocalDate.of(2025,6,1));
        dto.setFollowUpNotes("val-followUpNotes");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setStatus(InteractionResponseDto.InteractionStatusDto.SCHEDULED);
        dto.setRecordingUrl("val-recordingUrl");
        dto.setCampaignId("val-campaignId");
        dto.setDealId("val-dealId");
        dto.setProbability(99);
        dto.setNextStep("val-nextStep");
        dto.setNextStepDate(LocalDate.of(2025,6,1));
        dto.setIsHighPriority(true);
        dto.setTags("val-tags");
        assertEquals("val-id", dto.getId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-contactId", dto.getContactId());
        assertEquals("val-contactName", dto.getContactName());
        assertEquals(InteractionResponseDto.InteractionTypeDto.CALL, dto.getType());
        assertEquals(InteractionResponseDto.InteractionDirectionDto.INBOUND, dto.getDirection());
        assertEquals(99, dto.getDurationMinutes());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-outcome", dto.getOutcome());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getHasFollowUp());
        assertEquals(LocalDate.of(2025,6,1), dto.getFollowUpDate());
        assertEquals("val-followUpNotes", dto.getFollowUpNotes());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals(InteractionResponseDto.InteractionStatusDto.SCHEDULED, dto.getStatus());
        assertEquals("val-recordingUrl", dto.getRecordingUrl());
        assertEquals("val-campaignId", dto.getCampaignId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals(99, dto.getProbability());
        assertEquals("val-nextStep", dto.getNextStep());
        assertEquals(LocalDate.of(2025,6,1), dto.getNextStepDate());
        assertTrue(dto.getIsHighPriority());
        assertEquals("val-tags", dto.getTags());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionResponseDto dto1 = InteractionResponseDto.builder()
                        .id("test-id")
            .interactionId("test-interactionId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .type(InteractionResponseDto.InteractionTypeDto.CALL)
            .direction(InteractionResponseDto.InteractionDirectionDto.INBOUND)
            .interactionDate(LocalDateTime.of(2025,1,15,10,0))
            .durationMinutes(42)
            .subject("test-subject")
            .description("test-description")
            .outcome("test-outcome")
            .notes("test-notes")
            .location("test-location")
            .hasFollowUp(true)
            .followUpDate(LocalDate.of(2025,1,15))
            .followUpNotes("test-followUpNotes")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(InteractionResponseDto.InteractionStatusDto.SCHEDULED)
            .recordingUrl("test-recordingUrl")
            .attachmentUrls(Collections.emptyList())
            .participantContactIds(Collections.emptyList())
            .campaignId("test-campaignId")
            .dealId("test-dealId")
            .dealValue(null)
            .probability(42)
            .nextStep("test-nextStep")
            .nextStepDate(LocalDate.of(2025,1,15))
            .isHighPriority(true)
            .tags("test-tags")
            .relatedInteractions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        InteractionResponseDto dto2 = InteractionResponseDto.builder()
                        .id("test-id")
            .interactionId("test-interactionId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .type(InteractionResponseDto.InteractionTypeDto.CALL)
            .direction(InteractionResponseDto.InteractionDirectionDto.INBOUND)
            .interactionDate(LocalDateTime.of(2025,1,15,10,0))
            .durationMinutes(42)
            .subject("test-subject")
            .description("test-description")
            .outcome("test-outcome")
            .notes("test-notes")
            .location("test-location")
            .hasFollowUp(true)
            .followUpDate(LocalDate.of(2025,1,15))
            .followUpNotes("test-followUpNotes")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(InteractionResponseDto.InteractionStatusDto.SCHEDULED)
            .recordingUrl("test-recordingUrl")
            .attachmentUrls(Collections.emptyList())
            .participantContactIds(Collections.emptyList())
            .campaignId("test-campaignId")
            .dealId("test-dealId")
            .dealValue(null)
            .probability(42)
            .nextStep("test-nextStep")
            .nextStepDate(LocalDate.of(2025,1,15))
            .isHighPriority(true)
            .tags("test-tags")
            .relatedInteractions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        InteractionResponseDto dto = InteractionResponseDto.builder()
                        .id("test-id")
            .interactionId("test-interactionId")
            .tenantId("test-tenantId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .contactId("test-contactId")
            .contactName("test-contactName")
            .type(InteractionResponseDto.InteractionTypeDto.CALL)
            .direction(InteractionResponseDto.InteractionDirectionDto.INBOUND)
            .interactionDate(LocalDateTime.of(2025,1,15,10,0))
            .durationMinutes(42)
            .subject("test-subject")
            .description("test-description")
            .outcome("test-outcome")
            .notes("test-notes")
            .location("test-location")
            .hasFollowUp(true)
            .followUpDate(LocalDate.of(2025,1,15))
            .followUpNotes("test-followUpNotes")
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .status(InteractionResponseDto.InteractionStatusDto.SCHEDULED)
            .recordingUrl("test-recordingUrl")
            .attachmentUrls(Collections.emptyList())
            .participantContactIds(Collections.emptyList())
            .campaignId("test-campaignId")
            .dealId("test-dealId")
            .dealValue(null)
            .probability(42)
            .nextStep("test-nextStep")
            .nextStepDate(LocalDate.of(2025,1,15))
            .isHighPriority(true)
            .tags("test-tags")
            .relatedInteractions(Collections.emptyList())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}