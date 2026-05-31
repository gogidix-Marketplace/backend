package com.gogidix.sales.communication.domain.port.in;

import com.gogidix.sales.communication.domain.model.Conversation;
import com.gogidix.sales.communication.domain.port.in.ConversationCommand;
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
class ConversationCommand_CreateConversationCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.CreateConversationCommand dto = new ConversationCommand.CreateConversationCommand();
        dto.setTenantId("val-tenantId");
        dto.setTitle("val-title");
        dto.setOwnerId("val-ownerId");
        dto.setOwnerName("val-ownerName");
        dto.setDescription("val-description");
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.CreateConversationCommand dto1 = new ConversationCommand.CreateConversationCommand();
        ConversationCommand.CreateConversationCommand dto2 = new ConversationCommand.CreateConversationCommand();
        dto1.setTenantId("test");
        dto1.setTitle("test");
        dto1.setType(Conversation.ConversationType.DIRECT);
        dto1.setOwnerId("test");
        dto1.setOwnerName("test");
        dto1.setParticipants(Collections.emptyList());
        dto1.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto1.setDescription("test");
        dto1.setRelatedEntityType("test");
        dto1.setRelatedEntityId("test");
        dto2.setTenantId("test");
        dto2.setTitle("test");
        dto2.setType(Conversation.ConversationType.DIRECT);
        dto2.setOwnerId("test");
        dto2.setOwnerName("test");
        dto2.setParticipants(Collections.emptyList());
        dto2.setDefaultChannel(Conversation.ChannelType.EMAIL);
        dto2.setDescription("test");
        dto2.setRelatedEntityType("test");
        dto2.setRelatedEntityId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.CreateConversationCommand dto = new ConversationCommand.CreateConversationCommand();
        dto.setTenantId("test");
        dto.setTitle("test");
        dto.setType(Conversation.ConversationType.DIRECT);
        dto.setOwnerId("test");
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
        ConversationCommand.CreateConversationCommand dto = new ConversationCommand.CreateConversationCommand();
        dto.setTenantId("test");
        dto.setTitle("test");
        dto.setType(Conversation.ConversationType.DIRECT);
        dto.setOwnerId("test");
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