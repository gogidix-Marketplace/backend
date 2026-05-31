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
class InteractionCommand_AddAttachmentCommandTest {

        @Test
    void testSettersAndGetters() {
        InteractionCommand.AddAttachmentCommand dto = new InteractionCommand.AddAttachmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setInteractionId("val-interactionId");
        dto.setAttachmentUrl("val-attachmentUrl");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-attachmentUrl", dto.getAttachmentUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionCommand.AddAttachmentCommand dto1 = new InteractionCommand.AddAttachmentCommand();
        InteractionCommand.AddAttachmentCommand dto2 = new InteractionCommand.AddAttachmentCommand();
        dto1.setTenantId("test");
        dto1.setInteractionId("test");
        dto1.setAttachmentUrl("test");
        dto2.setTenantId("test");
        dto2.setInteractionId("test");
        dto2.setAttachmentUrl("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionCommand.AddAttachmentCommand dto = new InteractionCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setAttachmentUrl("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionCommand.AddAttachmentCommand dto = new InteractionCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setAttachmentUrl("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}