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
class NotificationPreferenceCommand_SetQuietHoursCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationPreferenceCommand.SetQuietHoursCommand dto = new NotificationPreferenceCommand.SetQuietHoursCommand();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceCommand.SetQuietHoursCommand dto1 = new NotificationPreferenceCommand.SetQuietHoursCommand();
        NotificationPreferenceCommand.SetQuietHoursCommand dto2 = new NotificationPreferenceCommand.SetQuietHoursCommand();
        dto1.setTenantId("test");
        dto1.setUserId("test");
        dto1.setQuietHoursStart(null);
        dto1.setQuietHoursEnd(null);
        dto2.setTenantId("test");
        dto2.setUserId("test");
        dto2.setQuietHoursStart(null);
        dto2.setQuietHoursEnd(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationPreferenceCommand.SetQuietHoursCommand dto = new NotificationPreferenceCommand.SetQuietHoursCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationPreferenceCommand.SetQuietHoursCommand dto = new NotificationPreferenceCommand.SetQuietHoursCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}