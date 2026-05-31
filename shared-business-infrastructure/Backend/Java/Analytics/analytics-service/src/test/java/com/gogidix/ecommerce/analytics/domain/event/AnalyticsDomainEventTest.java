package com.gogidix.ecommerce.analytics.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Analytics Domain Event Tests")
class AnalyticsDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        AnalyticsCreatedEvent event = new AnalyticsCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Analytics_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        AnalyticsUpdatedEvent event = new AnalyticsUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Analytics_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        AnalyticsDeletedEvent event = new AnalyticsDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Analytics_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        AnalyticsCreatedEvent event = new AnalyticsCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(AnalyticsDomainEvent.class);
    }
}
