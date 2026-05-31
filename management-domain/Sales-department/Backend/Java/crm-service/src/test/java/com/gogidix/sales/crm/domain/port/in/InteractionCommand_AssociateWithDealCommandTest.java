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
class InteractionCommand_AssociateWithDealCommandTest {

        @Test
    void testSettersAndGetters() {
        InteractionCommand.AssociateWithDealCommand dto = new InteractionCommand.AssociateWithDealCommand();
        dto.setTenantId("val-tenantId");
        dto.setInteractionId("val-interactionId");
        dto.setDealId("val-dealId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-interactionId", dto.getInteractionId());
        assertEquals("val-dealId", dto.getDealId());
    }

    @Test
    void testEqualsAndHashCode() {
        InteractionCommand.AssociateWithDealCommand dto1 = new InteractionCommand.AssociateWithDealCommand();
        InteractionCommand.AssociateWithDealCommand dto2 = new InteractionCommand.AssociateWithDealCommand();
        dto1.setTenantId("test");
        dto1.setInteractionId("test");
        dto1.setDealId("test");
        dto1.setDealValue(null);
        dto2.setTenantId("test");
        dto2.setInteractionId("test");
        dto2.setDealId("test");
        dto2.setDealValue(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        InteractionCommand.AssociateWithDealCommand dto = new InteractionCommand.AssociateWithDealCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setDealId("test");
        dto.setDealValue(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        InteractionCommand.AssociateWithDealCommand dto = new InteractionCommand.AssociateWithDealCommand();
        dto.setTenantId("test");
        dto.setInteractionId("test");
        dto.setDealId("test");
        dto.setDealValue(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}