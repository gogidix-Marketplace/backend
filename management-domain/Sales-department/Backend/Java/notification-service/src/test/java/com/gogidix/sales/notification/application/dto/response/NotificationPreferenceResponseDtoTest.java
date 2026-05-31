package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.application.dto.response.NotificationPreferenceResponseDto;
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
class NotificationPreferenceResponseDtoTest {

        @Test
    void testBuilder() {
        NotificationPreferenceResponseDto dto = NotificationPreferenceResponseDto.builder()
                        .id("test-id")
            .preferenceId("test-preferenceId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .channelPreferences(Collections.emptyMap())
            .enableNotifications(true)
            .enableDigest(true)
            .digestFrequency("test-digestFrequency")
            .digestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .timeZone("test-timeZone")
            .minPriority(NotificationPriority.LOW)
            .quietHoursEnabled(true)
            .quietHoursStart(Instant.parse("2025-01-15T10:00:00Z"))
            .quietHoursEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .categoryPreferences(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-preferenceId", dto.getPreferenceId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertTrue(dto.getEnableNotifications());
        assertTrue(dto.getEnableDigest());
        assertEquals("test-digestFrequency", dto.getDigestFrequency());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertTrue(dto.getQuietHoursEnabled());
    }

    @Test
    void testSettersAndGetters() {
        NotificationPreferenceResponseDto dto = new NotificationPreferenceResponseDto();
        dto.setId("val-id");
        dto.setPreferenceId("val-preferenceId");
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setEnableNotifications(true);
        dto.setEnableDigest(true);
        dto.setDigestFrequency("val-digestFrequency");
        dto.setTimeZone("val-timeZone");
        dto.setQuietHoursEnabled(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-preferenceId", dto.getPreferenceId());
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
        NotificationPreferenceResponseDto dto1 = NotificationPreferenceResponseDto.builder()
                        .id("test-id")
            .preferenceId("test-preferenceId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .channelPreferences(Collections.emptyMap())
            .enableNotifications(true)
            .enableDigest(true)
            .digestFrequency("test-digestFrequency")
            .digestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .timeZone("test-timeZone")
            .minPriority(NotificationPriority.LOW)
            .quietHoursEnabled(true)
            .quietHoursStart(Instant.parse("2025-01-15T10:00:00Z"))
            .quietHoursEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .categoryPreferences(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        NotificationPreferenceResponseDto dto2 = NotificationPreferenceResponseDto.builder()
                        .id("test-id")
            .preferenceId("test-preferenceId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .channelPreferences(Collections.emptyMap())
            .enableNotifications(true)
            .enableDigest(true)
            .digestFrequency("test-digestFrequency")
            .digestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .timeZone("test-timeZone")
            .minPriority(NotificationPriority.LOW)
            .quietHoursEnabled(true)
            .quietHoursStart(Instant.parse("2025-01-15T10:00:00Z"))
            .quietHoursEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .categoryPreferences(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationPreferenceResponseDto dto = NotificationPreferenceResponseDto.builder()
                        .id("test-id")
            .preferenceId("test-preferenceId")
            .tenantId("test-tenantId")
            .userId("test-userId")
            .channelPreferences(Collections.emptyMap())
            .enableNotifications(true)
            .enableDigest(true)
            .digestFrequency("test-digestFrequency")
            .digestTime(Instant.parse("2025-01-15T10:00:00Z"))
            .timeZone("test-timeZone")
            .minPriority(NotificationPriority.LOW)
            .quietHoursEnabled(true)
            .quietHoursStart(Instant.parse("2025-01-15T10:00:00Z"))
            .quietHoursEnd(Instant.parse("2025-01-15T10:00:00Z"))
            .categoryPreferences(Collections.emptyMap())
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}