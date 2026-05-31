package com.gogidix.ecommerce.promotion.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Promotion Domain Event Tests")
class PromotionDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        PromotionCreatedEvent event = new PromotionCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Promotion_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        PromotionUpdatedEvent event = new PromotionUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Promotion_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        PromotionDeletedEvent event = new PromotionDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Promotion_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        PromotionCreatedEvent event = new PromotionCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(PromotionDomainEvent.class);
    }
}
