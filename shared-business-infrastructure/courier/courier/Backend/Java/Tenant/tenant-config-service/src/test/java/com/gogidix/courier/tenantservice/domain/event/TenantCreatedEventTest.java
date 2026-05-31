package com.gogidix.courier.tenantservice.domain.event;

import com.gogidix.courier.tenantservice.domain.event.TenantCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TenantCreatedEvent.
 */
@DisplayName("TenantCreatedEvent Tests")
class TenantCreatedEventTest {

    @Test
    @DisplayName("Should create event with all fields")
    void shouldCreateEventWithAllFields() {
        // Given
        String aggregateId = "agg-001";
        String tenantId = "tenant-001";
        String tenantName = "Test Tenant";

        // When
        TenantCreatedEvent event = new TenantCreatedEvent(aggregateId, tenantId, tenantName);

        // Then
        assertNotNull(event.getEventId());
        assertEquals(aggregateId, event.getAggregateId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(tenantName, event.getTenantName());
        assertNotNull(event.getOccurredAt());
        assertEquals("TenantCreatedEvent", event.getEventType());
    }

    @Test
    @DisplayName("Should generate unique event IDs")
    void shouldGenerateUniqueEventIds() {
        // Given
        String aggregateId = "agg-001";
        String tenantId = "tenant-001";
        String tenantName = "Test Tenant";

        // When
        TenantCreatedEvent event1 = new TenantCreatedEvent(aggregateId, tenantId, tenantName);
        TenantCreatedEvent event2 = new TenantCreatedEvent(aggregateId, tenantId, tenantName);

        // Then
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        TenantCreatedEvent event = new TenantCreatedEvent("agg-001", "tenant-001", "Test");

        // Then
        assertEquals(event, event);
        assertNotEquals(event, null);
        assertNotEquals(event, new Object());
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        TenantCreatedEvent event = new TenantCreatedEvent("agg-001", "tenant-001", "Test");

        // When
        int hashCode1 = event.hashCode();
        int hashCode2 = event.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }
}
