package com.gogidix.ecommerce.email.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Email Domain Event Tests")
class EmailDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        EmailCreatedEvent event = new EmailCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Email_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        EmailUpdatedEvent event = new EmailUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Email_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        EmailDeletedEvent event = new EmailDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Email_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        EmailCreatedEvent event = new EmailCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(EmailDomainEvent.class);
    }
}
