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
class InteractionCommand_CancelInteractionCommandTest {

        @Test
    void testSettersAndGetters() {
        InteractionCommand.CancelInteractionCommand dto = new InteractionCommand.CancelInteractionCommand();
        dto.setTenantId("val-tenantId");
        dto.setInteractionId("val-interactionId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionCommand.CancelInteractionCommand dto1 = new InteractionCommand.CancelInteractionCommand();
        InteractionCommand.CancelInteractionCommand dto2 = new InteractionCommand.CancelInteractionCommand();
        dto1.setTenantId("test");
        dto1.setInteractionId("test");
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setInteractionId("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionCommand.CancelInteractionCommand dto = new InteractionCommand.CancelInteractionCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionCommand.CancelInteractionCommand dto = new InteractionCommand.CancelInteractionCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}