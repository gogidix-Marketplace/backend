package com.gogidix.ecommerce.search.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Search Domain Event Tests")
class SearchDomainEventTest {

    @Test
    @DisplayName("Should create created event")
    void shouldCreateCreatedEvent() {
        SearchCreatedEvent event = new SearchCreatedEvent("tenant-1", "entity-1");
        assertThat(event.eventId()).isNotNull().isNotEmpty();
        assertThat(event.tenantId()).isEqualTo("tenant-1");
        assertThat(event.entityId()).isEqualTo("entity-1");
        assertThat(event.timestamp()).isNotNull();
        assertThat(event.eventType()).isEqualTo("Search_CREATED");
    }

    @Test
    @DisplayName("Should create updated event")
    void shouldCreateUpdatedEvent() {
        SearchUpdatedEvent event = new SearchUpdatedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Search_UPDATED");
        assertThat(event.tenantId()).isEqualTo("tenant-1");
    }

    @Test
    @DisplayName("Should create deleted event")
    void shouldCreateDeletedEvent() {
        SearchDeletedEvent event = new SearchDeletedEvent("tenant-1", "entity-1");
        assertThat(event.eventType()).isEqualTo("Search_DELETED");
        assertThat(event.entityId()).isEqualTo("entity-1");
    }

    @Test
    @DisplayName("Events should implement domain event interface")
    void shouldImplementInterface() {
        SearchCreatedEvent event = new SearchCreatedEvent("t", "e");
        assertThat(event).isInstanceOf(SearchDomainEvent.class);
    }
}
