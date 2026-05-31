package com.gogidix.sales.communication.domain.port.in;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.port.in.MessageCommand;
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
class MessageCommand_CreateMessageCommandTest {

        @Test
    void testSettersAndGetters() {
        MessageCommand.CreateMessageCommand dto = new MessageCommand.CreateMessageCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setSenderId("val-senderId");
        dto.setSenderName("val-senderName");
        dto.setSubject("val-subject");
        dto.setContent("val-content");
        dto.setTemplateId("val-templateId");
        dto.setPriority(99);
        dto.setParentMessageId("val-parentMessageId");
        dto.setRelatedEntityType("val-relatedEntityType");
        dto.setRelatedEntityId("val-relatedEntityId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-senderName", dto.getSenderName());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-templateId", dto.getTemplateId());
        assertEquals(99, dto.getPriority());
        assertEquals("val-parentMessageId", dto.getParentMessageId());
        assertEquals("val-relatedEntityType", dto.getRelatedEntityType());
        assertEquals("val-relatedEntityId", dto.getRelatedEntityId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageCommand.CreateMessageCommand dto1 = new MessageCommand.CreateMessageCommand();
        MessageCommand.CreateMessageCommand dto2 = new MessageCommand.CreateMessageCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setSenderId("test");
        dto1.setSenderName("test");
        dto1.setRecipients(Collections.emptyList());
        dto1.setChannel(Message.ChannelType.EMAIL);
        dto1.setSubject("test");
        dto1.setContent("test");
        dto1.setTemplateId("test");
        dto1.setAttachments(Collections.emptyList());
        dto1.setPriority(42);
        dto1.setScheduledAt(null);
        dto1.setParentMessageId("test");
        dto1.setRelatedEntityType("test");
        dto1.setRelatedEntityId("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setSenderId("test");
        dto2.setSenderName("test");
        dto2.setRecipients(Collections.emptyList());
        dto2.setChannel(Message.ChannelType.EMAIL);
        dto2.setSubject("test");
        dto2.setContent("test");
        dto2.setTemplateId("test");
        dto2.setAttachments(Collections.emptyList());
        dto2.setPriority(42);
        dto2.setScheduledAt(null);
        dto2.setParentMessageId("test");
        dto2.setRelatedEntityType("test");
        dto2.setRelatedEntityId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageCommand.CreateMessageCommand dto = new MessageCommand.CreateMessageCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setSenderId("test");
        dto.setSenderName("test");
        dto.setRecipients(Collections.emptyList());
        dto.setChannel(Message.ChannelType.EMAIL);
        dto.setSubject("test");
        dto.setContent("test");
        dto.setTemplateId("test");
        dto.setAttachments(Collections.emptyList());
        dto.setPriority(42);
        dto.setScheduledAt(null);
        dto.setParentMessageId("test");
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageCommand.CreateMessageCommand dto = new MessageCommand.CreateMessageCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setSenderId("test");
        dto.setSenderName("test");
        dto.setRecipients(Collections.emptyList());
        dto.setChannel(Message.ChannelType.EMAIL);
        dto.setSubject("test");
        dto.setContent("test");
        dto.setTemplateId("test");
        dto.setAttachments(Collections.emptyList());
        dto.setPriority(42);
        dto.setScheduledAt(null);
        dto.setParentMessageId("test");
        dto.setRelatedEntityType("test");
        dto.setRelatedEntityId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}