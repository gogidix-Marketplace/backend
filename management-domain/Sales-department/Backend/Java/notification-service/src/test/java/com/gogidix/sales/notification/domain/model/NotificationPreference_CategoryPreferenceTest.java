package com.gogidix.sales.notification.domain.model;

import com.gogidix.sales.notification.domain.model.NotificationPreference;
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
class NotificationPreference_CategoryPreferenceTest {

        @Test
    void testBuilder() {
        NotificationPreference.CategoryPreference dto = NotificationPreference.CategoryPreference.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getImmediate());
    }

    @Test
    void testSettersAndGetters() {
        NotificationPreference.CategoryPreference dto = new NotificationPreference.CategoryPreference();
        dto.setEnabled(true);
        dto.setImmediate(true);
        assertTrue(dto.getEnabled());
        assertTrue(dto.getImmediate());
    }

    @Test
    void testEqualsAndHashCode() {
        NotificationPreference.CategoryPreference dto1 = NotificationPreference.CategoryPreference.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        NotificationPreference.CategoryPreference dto2 = NotificationPreference.CategoryPreference.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        NotificationPreference.CategoryPreference dto = NotificationPreference.CategoryPreference.builder()
                        .enabled(true)
            .channels(null)
            .immediate(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}