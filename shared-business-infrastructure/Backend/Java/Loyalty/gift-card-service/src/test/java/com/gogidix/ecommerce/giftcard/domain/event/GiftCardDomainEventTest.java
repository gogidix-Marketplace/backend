package com.gogidix.ecommerce.giftcard.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("GiftCard Domain Event Tests")
class GiftCardDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        GiftCardCreatedEvent event = new GiftCardCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("GiftCard_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        GiftCardUpdatedEvent event = new GiftCardUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("GiftCard_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        GiftCardDeletedEvent event = new GiftCardDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("GiftCard_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        GiftCardCreatedEvent event = new GiftCardCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(GiftCardDomainEvent.class);
    }
}
