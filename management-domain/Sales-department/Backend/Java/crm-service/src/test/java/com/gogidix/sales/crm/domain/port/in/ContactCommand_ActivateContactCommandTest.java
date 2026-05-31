package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.port.in.ContactCommand;
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
class ContactCommand_ActivateContactCommandTest {

        @Test
    void testSettersAndGetters() {
        ContactCommand.ActivateContactCommand dto = new ContactCommand.ActivateContactCommand();
        dto.setTenantId("val-tenantId");
        dto.setContactId("val-contactId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-contactId", dto.getContactId());
    }

    @Test
    void testEqualsAndHashCode() {
        ContactCommand.ActivateContactCommand dto1 = new ContactCommand.ActivateContactCommand();
        ContactCommand.ActivateContactCommand dto2 = new ContactCommand.ActivateContactCommand();
        dto1.setTenantId("test");
        dto1.setContactId("test");
        dto2.setTenantId("test");
        dto2.setContactId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ContactCommand.ActivateContactCommand dto = new ContactCommand.ActivateContactCommand();
        dto.setTenantId("test");
        dto.setContactId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ContactCommand.ActivateContactCommand dto = new ContactCommand.ActivateContactCommand();
        dto.setTenantId("test");
        dto.setContactId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}