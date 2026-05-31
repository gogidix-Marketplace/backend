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
class ConversationCommand_MarkAsReadCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.MarkAsReadCommand dto = new ConversationCommand.MarkAsReadCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setUserId("val-userId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.MarkAsReadCommand dto1 = new ConversationCommand.MarkAsReadCommand();
        ConversationCommand.MarkAsReadCommand dto2 = new ConversationCommand.MarkAsReadCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setUserId("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setUserId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.MarkAsReadCommand dto = new ConversationCommand.MarkAsReadCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setUserId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.MarkAsReadCommand dto = new ConversationCommand.MarkAsReadCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setUserId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}