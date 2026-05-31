package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
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
class SLAPolicyResponseDto_NotificationConfigDtoTest {

        @Test
    void testBuilder() {
        SLAPolicyResponseDto.NotificationConfigDto dto = SLAPolicyResponseDto.NotificationConfigDto.builder()
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
        SLAPolicyResponseDto.NotificationConfigDto dto = new SLAPolicyResponseDto.NotificationConfigDto();
        dto.setNotifyOnBreach(true);
        dto.setNotifyBeforeBreach(true);
        dto.setNotifyBeforeMinutes(99);
        assertTrue(dto.getNotifyOnBreach());
        assertTrue(dto.getNotifyBeforeBreach());
        assertEquals(99, dto.getNotifyBeforeMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicyResponseDto.NotificationConfigDto dto1 = SLAPolicyResponseDto.NotificationConfigDto.builder()
                        .notifyOnBreach(true)
            .notifyBeforeBreach(true)
            .notifyBeforeMinutes(42)
            .notificationRecipients(Collections.emptyList())
            .build();
        SLAPolicyResponseDto.NotificationConfigDto dto2 = SLAPolicyResponseDto.NotificationConfigDto.builder()
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
        SLAPolicyResponseDto.NotificationConfigDto dto = SLAPolicyResponseDto.NotificationConfigDto.builder()
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