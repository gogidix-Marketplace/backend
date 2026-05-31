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
class MessageCommand_AddAttachmentCommandTest {

        @Test
    void testSettersAndGetters() {
        MessageCommand.AddAttachmentCommand dto = new MessageCommand.AddAttachmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setMessageId("val-messageId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-messageId", dto.getMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageCommand.AddAttachmentCommand dto1 = new MessageCommand.AddAttachmentCommand();
        MessageCommand.AddAttachmentCommand dto2 = new MessageCommand.AddAttachmentCommand();
        dto1.setTenantId("test");
        dto1.setMessageId("test");
        dto1.setAttachment(null);
        dto2.setTenantId("test");
        dto2.setMessageId("test");
        dto2.setAttachment(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageCommand.AddAttachmentCommand dto = new MessageCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        dto.setAttachment(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageCommand.AddAttachmentCommand dto = new MessageCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        dto.setAttachment(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}