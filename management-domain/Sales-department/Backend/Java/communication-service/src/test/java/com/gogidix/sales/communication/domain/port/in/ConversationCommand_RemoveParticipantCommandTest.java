package com.gogidix.sales.communication.domain.port.in;

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
class ConversationCommand_RemoveParticipantCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.RemoveParticipantCommand dto = new ConversationCommand.RemoveParticipantCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setParticipantId("val-participantId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-participantId", dto.getParticipantId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.RemoveParticipantCommand dto1 = new ConversationCommand.RemoveParticipantCommand();
        ConversationCommand.RemoveParticipantCommand dto2 = new ConversationCommand.RemoveParticipantCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setParticipantId("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setParticipantId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.RemoveParticipantCommand dto = new ConversationCommand.RemoveParticipantCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setParticipantId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.RemoveParticipantCommand dto = new ConversationCommand.RemoveParticipantCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setParticipantId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}