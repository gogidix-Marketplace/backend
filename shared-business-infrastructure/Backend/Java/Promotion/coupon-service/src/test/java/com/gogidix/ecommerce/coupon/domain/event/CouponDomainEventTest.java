package com.gogidix.ecommerce.coupon.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Coupon Domain Event Tests")
class CouponDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        CouponCreatedEvent event = new CouponCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Coupon_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        CouponUpdatedEvent event = new CouponUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Coupon_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        CouponDeletedEvent event = new CouponDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Coupon_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        CouponCreatedEvent event = new CouponCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(CouponDomainEvent.class);
    }
}
