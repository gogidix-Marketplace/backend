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
class ConversationCommand_AddTagCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.AddTagCommand dto = new ConversationCommand.AddTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setTag("val-tag");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.AddTagCommand dto1 = new ConversationCommand.AddTagCommand();
        ConversationCommand.AddTagCommand dto2 = new ConversationCommand.AddTagCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setTag("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.AddTagCommand dto = new ConversationCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.AddTagCommand dto = new ConversationCommand.AddTagCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}