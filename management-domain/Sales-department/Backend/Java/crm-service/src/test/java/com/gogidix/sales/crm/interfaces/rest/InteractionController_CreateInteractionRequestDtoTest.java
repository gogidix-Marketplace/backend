package com.gogidix.sales.crm.interfaces.rest;

import com.gogidix.sales.crm.domain.model.Interaction;
import com.gogidix.sales.crm.interfaces.rest.InteractionController;
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
class InteractionController_CreateInteractionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        InteractionController.CreateInteractionRequestDto dto = new InteractionController.CreateInteractionRequestDto();
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setContactId("val-contactId");
        dto.setContactName("val-contactName");
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        dto.setLocation("val-location");
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setCampaignId("val-campaignId");
        dto.setDealId("val-dealId");
        dto.setProbability(99);
        dto.setIsHighPriority(true);
        dto.setNotes("val-notes");
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-contactId", dto.getContactId());
        assertEquals("val-contactName", dto.getContactName());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-location", dto.getLocation());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertEquals("val-campaignId", dto.getCampaignId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals(99, dto.getProbability());
        assertTrue(dto.getIsHighPriority());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionController.CreateInteractionRequestDto dto1 = new InteractionController.CreateInteractionRequestDto();
        InteractionController.CreateInteractionRequestDto dto2 = new InteractionController.CreateInteractionRequestDto();
        dto1.setCustomerId("test");
        dto1.setCustomerName("test");
        dto1.setContactId("test");
        dto1.setContactName("test");
        dto1.setType(Interaction.InteractionType.CALL);
        dto1.setDirection(Interaction.InteractionDirection.INBOUND);
        dto1.setInteractionDate(LocalDateTime.of(2025,1,1,10,0));
        dto1.setSubject("test");
        dto1.setDescription("test");
        dto1.setLocation("test");
        dto1.setAssignedTo("test");
        dto1.setAssignedToName("test");
        dto1.setCampaignId("test");
        dto1.setDealId("test");
        dto1.setDealValue(null);
        dto1.setProbability(42);
        dto1.setIsHighPriority(true);
        dto1.setNotes("test");
        dto1.setParticipantContactIds(Collections.emptyList());
        dto2.setCustomerId("test");
        dto2.setCustomerName("test");
        dto2.setContactId("test");
        dto2.setContactName("test");
        dto2.setType(Interaction.InteractionType.CALL);
        dto2.setDirection(Interaction.InteractionDirection.INBOUND);
        dto2.setInteractionDate(LocalDateTime.of(2025,1,1,10,0));
        dto2.setSubject("test");
        dto2.setDescription("test");
        dto2.setLocation("test");
        dto2.setAssignedTo("test");
        dto2.setAssignedToName("test");
        dto2.setCampaignId("test");
        dto2.setDealId("test");
        dto2.setDealValue(null);
        dto2.setProbability(42);
        dto2.setIsHighPriority(true);
        dto2.setNotes("test");
        dto2.setParticipantContactIds(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCustomerId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionController.CreateInteractionRequestDto dto = new InteractionController.CreateInteractionRequestDto();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setContactId("test");
        dto.setContactName("test");
        dto.setType(Interaction.InteractionType.CALL);
        dto.setDirection(Interaction.InteractionDirection.INBOUND);
        dto.setInteractionDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setLocation("test");
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        dto.setCampaignId("test");
        dto.setDealId("test");
        dto.setDealValue(null);
        dto.setProbability(42);
        dto.setIsHighPriority(true);
        dto.setNotes("test");
        dto.setParticipantContactIds(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionController.CreateInteractionRequestDto dto = new InteractionController.CreateInteractionRequestDto();
        dto.setCustomerId("test");
        dto.setCustomerName("test");
        dto.setContactId("test");
        dto.setContactName("test");
        dto.setType(Interaction.InteractionType.CALL);
        dto.setDirection(Interaction.InteractionDirection.INBOUND);
        dto.setInteractionDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setSubject("test");
        dto.setDescription("test");
        dto.setLocation("test");
        dto.setAssignedTo("test");
        dto.setAssignedToName("test");
        dto.setCampaignId("test");
        dto.setDealId("test");
        dto.setDealValue(null);
        dto.setProbability(42);
        dto.setIsHighPriority(true);
        dto.setNotes("test");
        dto.setParticipantContactIds(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}