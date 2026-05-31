package com.gogidix.courier.assignmentservice.domain.event;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AssignmentCancelledEvent domain event.
 */
@DisplayName("AssignmentCancelledEvent Domain Event Tests")
class AssignmentCancelledEventTest {

    @Test
    @DisplayName("Should create assignment cancelled event successfully")
    void shouldCreateAssignmentCancelledEvent() {
        String aggregateId = "assignment-001";
        String tenantId = "tenant-001";
        String dispatchId = "dispatch-001";
        String driverId = "driver-001";
        String reason = "Driver not available";
        String cancelledBy = "dispatcher";

        AssignmentCancelledEvent event = new AssignmentCancelledEvent(
                aggregateId, tenantId, dispatchId, driverId, reason, cancelledBy
        );

        assertNotNull(event.getEventId());
        assertEquals(aggregateId, event.getAggregateId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(dispatchId, event.getDispatchId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(reason, event.getCancellationReason());
        assertEquals(cancelledBy, event.getCancelledBy());
        assertNotNull(event.getOccurredAt());
    }

    @Test
    @DisplayName("Should handle null cancellation reason")
    void shouldHandleNullCancellationReason() {
        AssignmentCancelledEvent event = new AssignmentCancelledEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001", null, "system"
        );

        assertNull(event.getCancellationReason());
    }

    @Test
    @DisplayName("Should handle null cancelledBy")
    void shouldHandleNullCancelledBy() {
        AssignmentCancelledEvent event = new AssignmentCancelledEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001", "reason", null
        );

        assertNull(event.getCancelledBy());
    }

    @Test
    @DisplayName("Should throw on null aggregateId")
    void shouldThrowOnNullAggregateId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCancelledEvent(null, "tenant-001", "dispatch-001", "driver-001", "reason", "system")
        );
    }

    @Test
    @DisplayName("Should throw on null tenantId")
    void shouldThrowOnNullTenantId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCancelledEvent("aggregate-001", null, "dispatch-001", "driver-001", "reason", "system")
        );
    }

    @Test
    @DisplayName("Should throw on null dispatchId")
    void shouldThrowOnNullDispatchId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCancelledEvent("aggregate-001", "tenant-001", null, "driver-001", "reason", "system")
        );
    }

    @Test
    @DisplayName("Should throw on null driverId")
    void shouldThrowOnNullDriverId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCancelledEvent("aggregate-001", "tenant-001", "dispatch-001", null, "reason", "system")
        );
    }

    @Test
    @DisplayName("Should generate unique event IDs")
    void shouldGenerateUniqueEventIds() {
        AssignmentCancelledEvent event1 = new AssignmentCancelledEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001", "reason", "system"
        );
        AssignmentCancelledEvent event2 = new AssignmentCancelledEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001", "reason", "system"
        );

        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        AssignmentCancelledEvent event = new AssignmentCancelledEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001", "Customer cancelled", "customer"
        );

        String toString = event.toString();

        assertTrue(toString.contains("AssignmentCancelledEvent"));
        assertTrue(toString.contains("Customer cancelled"));
        assertTrue(toString.contains("customer"));
    }
}
