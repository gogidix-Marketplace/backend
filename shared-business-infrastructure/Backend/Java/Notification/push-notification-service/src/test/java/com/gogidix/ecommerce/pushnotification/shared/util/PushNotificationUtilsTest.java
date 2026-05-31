package com.gogidix.ecommerce.pushnotification.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PushNotificationUtils Tests")
class PushNotificationUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(PushNotificationUtils.generateId()).isNotNull().isNotEmpty();
    }
}
