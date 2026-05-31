package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationCommand;
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
class NotificationCommand_ArchiveNotificationCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationCommand.ArchiveNotificationCommand dto = new NotificationCommand.ArchiveNotificationCommand();
        dto.setTenantId("val-tenantId");
        dto.setNotificationId("val-notificationId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-notificationId", dto.getNotificationId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationCommand.ArchiveNotificationCommand dto1 = new NotificationCommand.ArchiveNotificationCommand();
        NotificationCommand.ArchiveNotificationCommand dto2 = new NotificationCommand.ArchiveNotificationCommand();
        dto1.setTenantId("test");
        dto1.setNotificationId("test");
        dto2.setTenantId("test");
        dto2.setNotificationId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationCommand.ArchiveNotificationCommand dto = new NotificationCommand.ArchiveNotificationCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationCommand.ArchiveNotificationCommand dto = new NotificationCommand.ArchiveNotificationCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}