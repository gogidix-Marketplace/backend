package com.gogidix.ecommerce.storecredit.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StoreCredit Domain Event Tests")
class StoreCreditDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        StoreCreditCreatedEvent event = new StoreCreditCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("StoreCredit_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        StoreCreditUpdatedEvent event = new StoreCreditUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("StoreCredit_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        StoreCreditDeletedEvent event = new StoreCreditDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("StoreCredit_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        StoreCreditCreatedEvent event = new StoreCreditCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(StoreCreditDomainEvent.class);
    }
}
