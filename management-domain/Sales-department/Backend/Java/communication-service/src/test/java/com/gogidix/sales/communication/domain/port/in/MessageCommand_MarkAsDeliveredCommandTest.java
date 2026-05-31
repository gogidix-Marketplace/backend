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
class MessageCommand_MarkAsDeliveredCommandTest {

        @Test
    void testSettersAndGetters() {
        MessageCommand.MarkAsDeliveredCommand dto = new MessageCommand.MarkAsDeliveredCommand();
        dto.setTenantId("val-tenantId");
        dto.setMessageId("val-messageId");
        dto.setExternalMessageId("val-externalMessageId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-messageId", dto.getMessageId());
        assertEquals("val-externalMessageId", dto.getExternalMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageCommand.MarkAsDeliveredCommand dto1 = new MessageCommand.MarkAsDeliveredCommand();
        MessageCommand.MarkAsDeliveredCommand dto2 = new MessageCommand.MarkAsDeliveredCommand();
        dto1.setTenantId("test");
        dto1.setMessageId("test");
        dto1.setExternalMessageId("test");
        dto2.setTenantId("test");
        dto2.setMessageId("test");
        dto2.setExternalMessageId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        MessageCommand.MarkAsDeliveredCommand dto = new MessageCommand.MarkAsDeliveredCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        dto.setExternalMessageId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        MessageCommand.MarkAsDeliveredCommand dto = new MessageCommand.MarkAsDeliveredCommand();
        dto.setTenantId("test");
        dto.setMessageId("test");
        dto.setExternalMessageId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}