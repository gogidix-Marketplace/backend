package com.gogidix.customersupport.notification.application.service;

import com.gogidix.customersupport.notification.application.service.NotificationService;
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
class NotificationService_NotificationStatisticsDtoTest {

        @Test
    void testBuilder() {
        NotificationService.NotificationStatisticsDto dto = NotificationService.NotificationStatisticsDto.builder()
                        .totalNotifications(42L)
            .sentNotifications(42L)
            .deliveredNotifications(42L)
            .readNotifications(42L)
            .failedNotifications(42L)
            .pendingNotifications(42L)
            .successRate(null)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalNotifications());
        assertEquals(42L, dto.getSentNotifications());
        assertEquals(42L, dto.getDeliveredNotifications());
        assertEquals(42L, dto.getReadNotifications());
        assertEquals(42L, dto.getFailedNotifications());
        assertEquals(42L, dto.getPendingNotifications());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationService.NotificationStatisticsDto dto1 = NotificationService.NotificationStatisticsDto.builder()
                        .totalNotifications(42L)
            .sentNotifications(42L)
            .deliveredNotifications(42L)
            .readNotifications(42L)
            .failedNotifications(42L)
            .pendingNotifications(42L)
            .successRate(null)
            .build();
        NotificationService.NotificationStatisticsDto dto2 = NotificationService.NotificationStatisticsDto.builder()
                        .totalNotifications(42L)
            .sentNotifications(42L)
            .deliveredNotifications(42L)
            .readNotifications(42L)
            .failedNotifications(42L)
            .pendingNotifications(42L)
            .successRate(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationService.NotificationStatisticsDto dto = NotificationService.NotificationStatisticsDto.builder()
                        .totalNotifications(42L)
            .sentNotifications(42L)
            .deliveredNotifications(42L)
            .readNotifications(42L)
            .failedNotifications(42L)
            .pendingNotifications(42L)
            .successRate(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}