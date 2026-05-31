package com.gogidix.sales.communication.interfaces.rest;

import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.interfaces.rest.ConversationController;
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
class ConversationController_CreateConversationRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ConversationController.CreateConversationRequestDto dto = new ConversationController.CreateConversationRequestDto();
        dto.setTitle("val-title");
        dto.setOwnerName("val-ownerName");
        dto.setDescription("val-description");
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationController.CreateConversationRequestDto dto1 = new ConversationController.CreateConversationRequestDto();
        ConversationController.CreateConversationRequestDto dto2 = new ConversationController.CreateConversationRequestDto();
        dto1.setTitle("test");
        dto1.setType(Conversation.ConversationType.DIRECT);
        dto1.setOwnerName("test");
        dto1.setParticipants(Collections.emptyList());
        dto1.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto1.setDescription("test");
        dto1.setRelatedEntityType("test");
        dto1.setRelatedEntityId("test");
        dto2.setTitle("test");
        dto2.setType(Conversation.ConversationType.DIRECT);
        dto2.setOwnerName("test");
        dto2.setParticipants(Collections.emptyList());
        dto2.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto2.setDescription("test");
        dto2.setRelatedEntityType("test");
        dto2.setRelatedEntityId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTitle(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationController.CreateConversationRequestDto dto = new ConversationController.CreateConversationRequestDto();
        dto.setTitle("test");
        dto.setType(Conversation.ConversationType.DIRECT);
        dto.setOwnerName("test");
        dto.setParticipants(Collections.emptyList());
        dto.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto.setDescription("test");
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationController.CreateConversationRequestDto dto = new ConversationController.CreateConversationRequestDto();
        dto.setTitle("test");
        dto.setType(Conversation.ConversationType.DIRECT);
        dto.setOwnerName("test");
        dto.setParticipants(Collections.emptyList());
        dto.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto.setDescription("test");
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}