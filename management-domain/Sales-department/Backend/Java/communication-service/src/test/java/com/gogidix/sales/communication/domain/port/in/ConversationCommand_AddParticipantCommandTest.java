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
class ConversationCommand_AddParticipantCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.AddParticipantCommand dto = new ConversationCommand.AddParticipantCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.AddParticipantCommand dto1 = new ConversationCommand.AddParticipantCommand();
        ConversationCommand.AddParticipantCommand dto2 = new ConversationCommand.AddParticipantCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setParticipant(null);
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setParticipant(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.AddParticipantCommand dto = new ConversationCommand.AddParticipantCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setParticipant(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.AddParticipantCommand dto = new ConversationCommand.AddParticipantCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setParticipant(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}