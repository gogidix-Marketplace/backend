package com.gogidix.ecommerce.notification.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NotificationUtils Tests")
class NotificationUtilsTest {

    @Test
    @DisplayName("Should generate non-null ID")
    void shouldGenerateId() {
        assertThat(NotificationUtils.generateId()).isNotNull().isNotEmpty();
    }
}
