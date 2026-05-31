package com.gogidix.ecommerce.sms.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Sms Domain Event Tests")
class SmsDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        SmsCreatedEvent event = new SmsCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Sms_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        SmsUpdatedEvent event = new SmsUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Sms_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        SmsDeletedEvent event = new SmsDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Sms_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        SmsCreatedEvent event = new SmsCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(SmsDomainEvent.class);
    }
}
