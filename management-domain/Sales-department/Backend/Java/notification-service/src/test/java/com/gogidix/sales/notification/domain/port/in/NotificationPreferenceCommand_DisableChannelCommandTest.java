package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationPreferenceCommand;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
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
class NotificationPreferenceCommand_DisableChannelCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationPreferenceCommand.DisableChannelCommand dto = new NotificationPreferenceCommand.DisableChannelCommand();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceCommand.DisableChannelCommand dto1 = new NotificationPreferenceCommand.DisableChannelCommand();
        NotificationPreferenceCommand.DisableChannelCommand dto2 = new NotificationPreferenceCommand.DisableChannelCommand();
        dto1.setTenantId("test");
        dto1.setUserId("test");
        dto1.setChannel(NotificationChannel.EMAIL);
        dto2.setTenantId("test");
        dto2.setUserId("test");
        dto2.setChannel(NotificationChannel.EMAIL);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationPreferenceCommand.DisableChannelCommand dto = new NotificationPreferenceCommand.DisableChannelCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setChannel(NotificationChannel.EMAIL);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationPreferenceCommand.DisableChannelCommand dto = new NotificationPreferenceCommand.DisableChannelCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setChannel(NotificationChannel.EMAIL);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}