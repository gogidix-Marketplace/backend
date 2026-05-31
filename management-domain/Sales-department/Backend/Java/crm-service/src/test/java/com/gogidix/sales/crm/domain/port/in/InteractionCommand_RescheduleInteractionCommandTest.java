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
class InteractionCommand_RescheduleInteractionCommandTest {

        @Test
    void testSettersAndGetters() {
        InteractionCommand.RescheduleInteractionCommand dto = new InteractionCommand.RescheduleInteractionCommand();
        dto.setTenantId("val-tenantId");
        dto.setInteractionId("val-interactionId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionCommand.RescheduleInteractionCommand dto1 = new InteractionCommand.RescheduleInteractionCommand();
        InteractionCommand.RescheduleInteractionCommand dto2 = new InteractionCommand.RescheduleInteractionCommand();
        dto1.setTenantId("test");
        dto1.setInteractionId("test");
        dto1.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setInteractionId("test");
        dto2.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionCommand.RescheduleInteractionCommand dto = new InteractionCommand.RescheduleInteractionCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionCommand.RescheduleInteractionCommand dto = new InteractionCommand.RescheduleInteractionCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setNewDate(LocalDateTime.of(2025,1,1,10,0));
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}