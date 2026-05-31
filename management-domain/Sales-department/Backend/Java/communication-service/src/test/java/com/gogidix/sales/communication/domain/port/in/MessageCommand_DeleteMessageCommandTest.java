package com.gogidix.sales.communication.domain.port.in;

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
class MessageCommand_DeleteMessageCommandTest {

        @Test
    void testSettersAndGetters() {
        MessageCommand.DeleteMessageCommand dto = new MessageCommand.DeleteMessageCommand();
        dto.setTenantId("val-tenantId");
        dto.setMessageId("val-messageId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-messageId", dto.getMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageCommand.DeleteMessageCommand dto1 = new MessageCommand.DeleteMessageCommand();
        MessageCommand.DeleteMessageCommand dto2 = new MessageCommand.DeleteMessageCommand();
        dto1.setTenantId("test");
        dto1.setMessageId("test");
        dto2.setTenantId("test");
        dto2.setMessageId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageCommand.DeleteMessageCommand dto = new MessageCommand.DeleteMessageCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageCommand.DeleteMessageCommand dto = new MessageCommand.DeleteMessageCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}