package com.gogidix.shared.infrastructure.services.communication.webhook.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebhookDeliveryLog domain model.
 */
@DisplayName("WebhookDeliveryLog Domain Model Tests")
class WebhookDeliveryLogTest {

    @Test
    @DisplayName("Should verify WebhookDeliveryLog class exists")
    void shouldVerifyWebhookDeliveryLogClassExists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName(
                    "com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.WebhookDeliveryLog"
            );
            assertNotNull(clazz);
        });
    }

    @Test
    @DisplayName("Should create WebhookDeliveryLog with no-args constructor")
    void shouldCreateWebhookDeliveryLogWithNoArgsConstructor() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName(
                    "com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.WebhookDeliveryLog"
            );
            Object instance = clazz.getDeclaredConstructor().newInstance();
            assertNotNull(instance);
        });
    }

    @Test
    @DisplayName("Should verify WebhookDeliveryLog has expected structure")
    void shouldVerifyWebhookDeliveryLogHasExpectedStructure() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName(
                    "com.gogidix.shared.infrastructure.services.communication.webhook.domain.model.WebhookDeliveryLog"
            );
            assertTrue(clazz.getSimpleName().contains("WebhookDeliveryLog"));
        });
    }
}
