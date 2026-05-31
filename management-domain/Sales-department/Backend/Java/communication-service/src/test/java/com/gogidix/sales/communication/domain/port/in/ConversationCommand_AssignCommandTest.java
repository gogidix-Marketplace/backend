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
class ConversationCommand_AssignCommandTest {

        @Test
    void testSettersAndGetters() {
        ConversationCommand.AssignCommand dto = new ConversationCommand.AssignCommand();
        dto.setTenantId("val-tenantId");
        dto.setConversationId("val-conversationId");
        dto.setAssignedTo("val-assignedTo");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-conversationId", dto.getConversationId());
        assertEquals("val-assignedTo", dto.getAssignedTo());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationCommand.AssignCommand dto1 = new ConversationCommand.AssignCommand();
        ConversationCommand.AssignCommand dto2 = new ConversationCommand.AssignCommand();
        dto1.setTenantId("test");
        dto1.setConversationId("test");
        dto1.setAssignedTo("test");
        dto2.setTenantId("test");
        dto2.setConversationId("test");
        dto2.setAssignedTo("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConversationCommand.AssignCommand dto = new ConversationCommand.AssignCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setAssignedTo("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConversationCommand.AssignCommand dto = new ConversationCommand.AssignCommand();
        dto.setTenantId("test");
        dto.setConversationId("test");
        dto.setAssignedTo("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}