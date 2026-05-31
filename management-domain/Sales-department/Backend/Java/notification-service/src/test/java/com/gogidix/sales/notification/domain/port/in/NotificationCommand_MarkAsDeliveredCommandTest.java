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
class NotificationCommand_MarkAsDeliveredCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationCommand.MarkAsDeliveredCommand dto = new NotificationCommand.MarkAsDeliveredCommand();
        dto.setTenantId("val-tenantId");
        dto.setNotificationId("val-notificationId");
        dto.setExternalMessageId("val-externalMessageId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-notificationId", dto.getNotificationId());
        assertEquals("val-externalMessageId", dto.getExternalMessageId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationCommand.MarkAsDeliveredCommand dto1 = new NotificationCommand.MarkAsDeliveredCommand();
        NotificationCommand.MarkAsDeliveredCommand dto2 = new NotificationCommand.MarkAsDeliveredCommand();
        dto1.setTenantId("test");
        dto1.setNotificationId("test");
        dto1.setExternalMessageId("test");
        dto2.setTenantId("test");
        dto2.setNotificationId("test");
        dto2.setExternalMessageId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationCommand.MarkAsDeliveredCommand dto = new NotificationCommand.MarkAsDeliveredCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        dto.setExternalMessageId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationCommand.MarkAsDeliveredCommand dto = new NotificationCommand.MarkAsDeliveredCommand();
        dto.setTenantId("test");
        dto.setNotificationId("test");
        dto.setExternalMessageId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}