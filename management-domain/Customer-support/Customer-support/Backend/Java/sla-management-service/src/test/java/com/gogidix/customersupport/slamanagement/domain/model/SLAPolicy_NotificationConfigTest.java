package com.gogidix.customersupport.slamanagement.domain.model;

import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
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
class SLAPolicy_NotificationConfigTest {

        @Test
    void testBuilder() {
        SLAPolicy.NotificationConfig dto = SLAPolicy.NotificationConfig.builder()
                        .notifyOnBreach(true)
            .notifyBeforeBreach(true)
            .notifyBeforeMinutes(42)
            .notificationRecipients(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertTrue(dto.getNotifyOnBreach());
        assertTrue(dto.getNotifyBeforeBreach());
        assertEquals(42, dto.getNotifyBeforeMinutes());
    }

    @Test
    void testSettersAndGetters() {
        SLAPolicy.NotificationConfig dto = new SLAPolicy.NotificationConfig();
        dto.setNotifyOnBreach(true);
        dto.setNotifyBeforeBreach(true);
        dto.setNotifyBeforeMinutes(99);
        assertTrue(dto.getNotifyOnBreach());
        assertTrue(dto.getNotifyBeforeBreach());
        assertEquals(99, dto.getNotifyBeforeMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicy.NotificationConfig dto1 = SLAPolicy.NotificationConfig.builder()
                        .notifyOnBreach(true)
            .notifyBeforeBreach(true)
            .notifyBeforeMinutes(42)
            .notificationRecipients(Collections.emptyList())
            .build();
        SLAPolicy.NotificationConfig dto2 = SLAPolicy.NotificationConfig.builder()
                        .notifyOnBreach(true)
            .notifyBeforeBreach(true)
            .notifyBeforeMinutes(42)
            .notificationRecipients(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLAPolicy.NotificationConfig dto = SLAPolicy.NotificationConfig.builder()
                        .notifyOnBreach(true)
            .notifyBeforeBreach(true)
            .notifyBeforeMinutes(42)
            .notificationRecipients(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}