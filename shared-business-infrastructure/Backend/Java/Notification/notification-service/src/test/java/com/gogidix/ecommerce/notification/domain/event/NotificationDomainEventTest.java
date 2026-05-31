package com.gogidix.ecommerce.notification.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Notification Domain Event Tests")
class NotificationDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        NotificationCreatedEvent event = new NotificationCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Notification_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        NotificationUpdatedEvent event = new NotificationUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Notification_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        NotificationDeletedEvent event = new NotificationDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Notification_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        NotificationCreatedEvent event = new NotificationCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(NotificationDomainEvent.class);
    }
}
