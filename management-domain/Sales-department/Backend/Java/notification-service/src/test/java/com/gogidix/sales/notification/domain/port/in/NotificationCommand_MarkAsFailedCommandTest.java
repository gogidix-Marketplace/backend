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
class NotificationCommand_MarkAsFailedCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationCommand.MarkAsFailedCommand dto = new NotificationCommand.MarkAsFailedCommand();
        dto.setTenantId("val-tenantId");
        dto.setNotificationId("val-notificationId");
        dto.setErrorMessage("val-errorMessage");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-notificationId", dto.getNotificationId());
        assertEquals("val-errorMessage", dto.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationCommand.MarkAsFailedCommand dto1 = new NotificationCommand.MarkAsFailedCommand();
        NotificationCommand.MarkAsFailedCommand dto2 = new NotificationCommand.MarkAsFailedCommand();
        dto1.setTenantId("test");
        dto1.setNotificationId("test");
        dto1.setErrorMessage("test");
        dto2.setTenantId("test");
        dto2.setNotificationId("test");
        dto2.setErrorMessage("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationCommand.MarkAsFailedCommand dto = new NotificationCommand.MarkAsFailedCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        dto.setErrorMessage("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationCommand.MarkAsFailedCommand dto = new NotificationCommand.MarkAsFailedCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        dto.setErrorMessage("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}