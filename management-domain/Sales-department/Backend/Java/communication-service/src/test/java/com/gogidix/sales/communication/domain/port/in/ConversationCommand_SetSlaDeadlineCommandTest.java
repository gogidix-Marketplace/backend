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
class ConversationCommand_SetSlaDeadlineCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.SetSlaDeadlineCommand dto = new ConversationCommand.SetSlaDeadlineCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.SetSlaDeadlineCommand dto1 = new ConversationCommand.SetSlaDeadlineCommand();
        ConversationCommand.SetSlaDeadlineCommand dto2 = new ConversationCommand.SetSlaDeadlineCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setDeadline(null);
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setDeadline(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.SetSlaDeadlineCommand dto = new ConversationCommand.SetSlaDeadlineCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setDeadline(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.SetSlaDeadlineCommand dto = new ConversationCommand.SetSlaDeadlineCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setDeadline(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}