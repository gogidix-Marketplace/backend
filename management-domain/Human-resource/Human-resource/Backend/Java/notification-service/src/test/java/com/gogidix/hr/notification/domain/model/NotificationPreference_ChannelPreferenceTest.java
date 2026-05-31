package com.gogidix.hr.notification.domain.model;

import com.gogidix.hr.notification.domain.model.NotificationPreference;
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
class NotificationPreference_ChannelPreferenceTest {

        @Test
    void testSettersAndGetters() {
        NotificationPreference.ChannelPreference dto = new NotificationPreference.ChannelPreference();
        dto.setEnabled(true);
        dto.setIsPrimary(true);
        dto.setDestination("val-destination");
        assertTrue(dto.getEnabled());
        assertTrue(dto.getIsPrimary());
        assertEquals("val-destination", dto.getDestination());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreference.ChannelPreference dto1 = new NotificationPreference.ChannelPreference();
        NotificationPreference.ChannelPreference dto2 = new NotificationPreference.ChannelPreference();
        dto1.setEnabled(true);
        dto1.setIsPrimary(true);
        dto1.setDestination("test");
        dto1.setQuietHoursStart(null);
        dto1.setQuietHoursEnd(null);
        dto2.setEnabled(true);
        dto2.setIsPrimary(true);
        dto2.setDestination("test");
        dto2.setQuietHoursStart(null);
        dto2.setQuietHoursEnd(null);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setEnabled(false);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        NotificationPreference.ChannelPreference dto = new NotificationPreference.ChannelPreference();
        dto.setEnabled(true);
        dto.setIsPrimary(true);
        dto.setDestination("test");
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        NotificationPreference.ChannelPreference dto = new NotificationPreference.ChannelPreference();
        dto.setEnabled(true);
        dto.setIsPrimary(true);
        dto.setDestination("test");
        dto.setQuietHoursStart(null);
        dto.setQuietHoursEnd(null);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}