package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.port.in.NotificationPreferenceCommand;
import com.gogidix.sales.notification.domain.valueobject.NotificationPriority;
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
class NotificationPreferenceCommand_CreatePreferenceCommandTest {

        @Test
    void testSettersAndGetters() {
        NotificationPreferenceCommand.CreatePreferenceCommand dto = new NotificationPreferenceCommand.CreatePreferenceCommand();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setEnableNotifications(true);
        dto.setEnableDigest(true);
        dto.setDigestFrequency("val-digestFrequency");
        dto.setTimeZone("val-timeZone");
        dto.setQuietHoursEnabled(true);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertTrue(dto.getEnableNotifications());
        assertTrue(dto.getEnableDigest());
        assertEquals("val-digestFrequency", dto.getDigestFrequency());
        assertEquals("val-timeZone", dto.getTimeZone());
        assertTrue(dto.getQuietHoursEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreferenceCommand.CreatePreferenceCommand dto1 = new NotificationPreferenceCommand.CreatePreferenceCommand();
        NotificationPreferenceCommand.CreatePreferenceCommand dto2 = new NotificationPreferenceCommand.CreatePreferenceCommand();
        dto1.setTenantId("test");
        dto1.setUserId("test");
        dto1.setChannelPreferences(Collections.emptyMap());
        dto1.setEnableNotifications(true);
        dto1.setEnableDigest(true);
        dto1.setDigestFrequency("test");
        dto1.setDigestTime(null);
        dto1.setTimeZone("test");
        dto1.setMinPriority(NotificationPriority.LOW);
        dto1.setQuietHoursEnabled(true);
        dto1.setQuietHoursStart(null);
        dto1.setQuietHoursEnd(null);
        dto1.setCategoryPreferences(Collections.emptyMap());
        dto2.setTenantId("test");
        dto2.setUserId("test");
        dto2.setChannelPreferences(Collections.emptyMap());
        dto2.setEnableNotifications(true);
        dto2.setEnableDigest(true);
        dto2.setDigestFrequency("test");
        dto2.setDigestTime(null);
        dto2.setTimeZone("test");
        dto2.setMinPriority(NotificationPriority.LOW);
        dto2.setQuietHoursEnabled(true);
        dto2.setQuietHoursStart(null);
        dto2.setQuietHoursEnd(null);
        dto2.setCategoryPreferences(Collections.emptyMap());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationPreferenceCommand.CreatePreferenceCommand dto = new NotificationPreferenceCommand.CreatePreferenceCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setChannelPreferences(Collections.emptyMap());
        dto.setEnableNotifications(true);
        dto.setEnableDigest(true);
        dto.setDigestFrequency("test");
        dto.setDigestTime(null);
        dto.setTimeZone("test");
        dto.setMinPriority(NotificationPriority.LOW);
        dto.setQuietHoursEnabled(true);
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        dto.setCategoryPreferences(Collections.emptyMap());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationPreferenceCommand.CreatePreferenceCommand dto = new NotificationPreferenceCommand.CreatePreferenceCommand();
        dto.setTenantId("test");
        dto.setUserId("test");
        dto.setChannelPreferences(Collections.emptyMap());
        dto.setEnableNotifications(true);
        dto.setEnableDigest(true);
        dto.setDigestFrequency("test");
        dto.setDigestTime(null);
        dto.setTimeZone("test");
        dto.setMinPriority(NotificationPriority.LOW);
        dto.setQuietHoursEnabled(true);
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        dto.setCategoryPreferences(Collections.emptyMap());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}