package com.gogidix.ecommerce.wishlist.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Wishlist Domain Event Tests")
class WishlistDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        WishlistCreatedEvent event = new WishlistCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Wishlist_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        WishlistUpdatedEvent event = new WishlistUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Wishlist_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        WishlistDeletedEvent event = new WishlistDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Wishlist_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        WishlistCreatedEvent event = new WishlistCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(WishlistDomainEvent.class);
    }
}
