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
class ConversationCommand_ResolveCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.ResolveCommand dto = new ConversationCommand.ResolveCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setResolvedBy("val-resolvedBy");
        dto.setResolutionNotes("val-resolutionNotes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-resolvedBy", dto.getResolvedBy());
        assertEquals("val-resolutionNotes", dto.getResolutionNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.ResolveCommand dto1 = new ConversationCommand.ResolveCommand();
        ConversationCommand.ResolveCommand dto2 = new ConversationCommand.ResolveCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setResolvedBy("test");
        dto1.setResolutionNotes("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setResolvedBy("test");
        dto2.setResolutionNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.ResolveCommand dto = new ConversationCommand.ResolveCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setResolvedBy("test");
        dto.setResolutionNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.ResolveCommand dto = new ConversationCommand.ResolveCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setResolvedBy("test");
        dto.setResolutionNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}