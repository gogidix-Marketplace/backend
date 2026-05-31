package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.port.in.InteractionCommand;
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
class InteractionCommand_AddParticipantCommandTest {

        @Test
    void testSettersAndGetters() {
        InteractionCommand.AddParticipantCommand dto = new InteractionCommand.AddParticipantCommand();
        dto.setTenantId("val-tenantId");
        dto.setInteractionId("val-interactionId");
        dto.setContactId("val-contactId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-contactId", dto.getContactId());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionCommand.AddParticipantCommand dto1 = new InteractionCommand.AddParticipantCommand();
        InteractionCommand.AddParticipantCommand dto2 = new InteractionCommand.AddParticipantCommand();
        dto1.setTenantId("test");
        dto1.setInteractionId("test");
        dto1.setContactId("test");
        dto2.setTenantId("test");
        dto2.setInteractionId("test");
        dto2.setContactId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionCommand.AddParticipantCommand dto = new InteractionCommand.AddParticipantCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setContactId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionCommand.AddParticipantCommand dto = new InteractionCommand.AddParticipantCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setContactId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}