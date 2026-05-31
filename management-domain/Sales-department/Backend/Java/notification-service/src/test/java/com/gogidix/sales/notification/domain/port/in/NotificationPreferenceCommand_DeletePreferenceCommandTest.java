package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationPreferenceCommand;
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
class NotificationPreferenceCommand_DeletePreferenceCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationPreferenceCommand.DeletePreferenceCommand dto = new NotificationPreferenceCommand.DeletePreferenceCommand();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceCommand.DeletePreferenceCommand dto1 = new NotificationPreferenceCommand.DeletePreferenceCommand();
        NotificationPreferenceCommand.DeletePreferenceCommand dto2 = new NotificationPreferenceCommand.DeletePreferenceCommand();
        dto1.setTenantId("test");
        dto1.setUserId("test");
        dto2.setTenantId("test");
        dto2.setUserId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationPreferenceCommand.DeletePreferenceCommand dto = new NotificationPreferenceCommand.DeletePreferenceCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationPreferenceCommand.DeletePreferenceCommand dto = new NotificationPreferenceCommand.DeletePreferenceCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}