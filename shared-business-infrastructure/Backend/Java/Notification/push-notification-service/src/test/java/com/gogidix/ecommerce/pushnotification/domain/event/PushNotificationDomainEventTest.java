package com.gogidix.ecommerce.pushnotification.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PushNotification Domain Event Tests")
class PushNotificationDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        PushNotificationCreatedEvent event = new PushNotificationCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("PushNotification_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        PushNotificationUpdatedEvent event = new PushNotificationUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PushNotification_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        PushNotificationDeletedEvent event = new PushNotificationDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("PushNotification_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        PushNotificationCreatedEvent event = new PushNotificationCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(PushNotificationDomainEvent.class);
    }
}
