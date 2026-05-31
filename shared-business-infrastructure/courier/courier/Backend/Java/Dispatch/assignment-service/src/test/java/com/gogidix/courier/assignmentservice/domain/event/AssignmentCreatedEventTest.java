package com.gogidix.courier.assignmentservice.domain.event;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AssignmentCreatedEvent domain event.
 */
@DisplayName("AssignmentCreatedEvent Domain Event Tests")
class AssignmentCreatedEventTest {

    @Test
    @DisplayName("Should create assignment created event successfully")
    void shouldCreateAssignmentCreatedEvent() {
        String aggregateId = "assignment-001";
        String tenantId = "tenant-001";
        String dispatchId = "dispatch-001";
        String driverId = "driver-001";
        DriverAssignment.AssignmentStatus status = DriverAssignment.AssignmentStatus.PENDING;
        DriverAssignment.AssignmentPriority priority = DriverAssignment.AssignmentPriority.NORMAL;

        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                aggregateId,
                tenantId,
                dispatchId,
                driverId,
                status,
                priority
        );

        assertNotNull(event.getEventId());
        assertEquals(aggregateId, event.getAggregateId());
        assertEquals(tenantId, event.getTenantId());
        assertEquals(dispatchId, event.getDispatchId());
        assertEquals(driverId, event.getDriverId());
        assertEquals(status, event.getStatus());
        assertEquals(priority, event.getPriority());
        assertNotNull(event.getOccurredAt());
    }

    @Test
    @DisplayName("Should generate unique event IDs")
    void shouldGenerateUniqueEventIds() {
        AssignmentCreatedEvent event1 = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );
        AssignmentCreatedEvent event2 = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    @DisplayName("Should throw on null aggregateId")
    void shouldThrowOnNullAggregateId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCreatedEvent(
                        null, "tenant-001", "dispatch-001", "driver-001",
                        DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
                )
        );
    }

    @Test
    @DisplayName("Should throw on null tenantId")
    void shouldThrowOnNullTenantId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCreatedEvent(
                        "aggregate-001", null, "dispatch-001", "driver-001",
                        DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
                )
        );
    }

    @Test
    @DisplayName("Should throw on null dispatchId")
    void shouldThrowOnNullDispatchId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCreatedEvent(
                        "aggregate-001", "tenant-001", null, "driver-001",
                        DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
                )
        );
    }

    @Test
    @DisplayName("Should throw on null driverId")
    void shouldThrowOnNullDriverId() {
        assertThrows(NullPointerException.class, () ->
                new AssignmentCreatedEvent(
                        "aggregate-001", "tenant-001", "dispatch-001", null,
                        DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
                )
        );
    }

    @Test
    @DisplayName("Should allow null status")
    void shouldAllowNullStatus() {
        assertDoesNotThrow(() ->
                new AssignmentCreatedEvent(
                        "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                        null, DriverAssignment.AssignmentPriority.NORMAL
                )
        );
    }

    @Test
    @DisplayName("Should allow null priority")
    void shouldAllowNullPriority() {
        assertDoesNotThrow(() ->
                new AssignmentCreatedEvent(
                        "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                        DriverAssignment.AssignmentStatus.PENDING, null
                )
        );
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        String aggregateId = "aggregate-001";
        AssignmentCreatedEvent event1 = new AssignmentCreatedEvent(
                aggregateId, "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        // Same instance
        assertEquals(event1, event1);

        // Different instance, same eventId (not possible with UUID generation, but testing equals logic)
        AssignmentCreatedEvent event2 = new AssignmentCreatedEvent(
                aggregateId, "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        assertNotEquals(event1, event2);
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        int hashCode1 = event.hashCode();
        int hashCode2 = event.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Should generate event timestamp at creation")
    void shouldGenerateEventTimestampAtCreation() {
        Instant beforeCreation = Instant.now();

        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        Instant afterCreation = Instant.now();

        assertNotNull(event.getOccurredAt());
        assertFalse(event.getOccurredAt().isBefore(beforeCreation));
        assertFalse(event.getOccurredAt().isAfter(afterCreation));
    }

    @Test
    @DisplayName("Should create event with urgent priority")
    void shouldCreateEventWithUrgentPriority() {
        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.ACCEPTED, DriverAssignment.AssignmentPriority.URGENT
        );

        assertEquals(DriverAssignment.AssignmentPriority.URGENT, event.getPriority());
    }

    @Test
    @DisplayName("Should create event with emergency priority")
    void shouldCreateEventWithEmergencyPriority() {
        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.IN_PROGRESS, DriverAssignment.AssignmentPriority.EMERGENCY
        );

        assertEquals(DriverAssignment.AssignmentPriority.EMERGENCY, event.getPriority());
    }

    @Test
    @DisplayName("Should create event with different statuses")
    void shouldCreateEventWithDifferentStatuses() {
        DriverAssignment.AssignmentStatus[] statuses = {
                DriverAssignment.AssignmentStatus.PENDING,
                DriverAssignment.AssignmentStatus.ACCEPTED,
                DriverAssignment.AssignmentStatus.IN_PROGRESS,
                DriverAssignment.AssignmentStatus.COMPLETED,
                DriverAssignment.AssignmentStatus.CANCELLED,
                DriverAssignment.AssignmentStatus.FAILED
        };

        for (DriverAssignment.AssignmentStatus status : statuses) {
            AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                    "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                    status, DriverAssignment.AssignmentPriority.NORMAL
            );

            assertEquals(status, event.getStatus());
        }
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
                "aggregate-001", "tenant-001", "dispatch-001", "driver-001",
                DriverAssignment.AssignmentStatus.PENDING, DriverAssignment.AssignmentPriority.NORMAL
        );

        String toString = event.toString();

        assertTrue(toString.contains("AssignmentCreatedEvent"));
        assertTrue(toString.contains("tenant-001"));
        assertTrue(toString.contains("dispatch-001"));
        assertTrue(toString.contains("driver-001"));
    }
}
