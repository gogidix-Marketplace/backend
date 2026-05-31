package com.gogidix.courier.assignmentservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AssignmentMetadata value object.
 */
@DisplayName("AssignmentMetadata Value Object Tests")
class AssignmentMetadataTest {

    private DriverAssignment.AssignmentMetadata metadata;

    @BeforeEach
    void setUp() {
        metadata = new DriverAssignment.AssignmentMetadata();
    }

    @Test
    @DisplayName("Should create metadata with default values")
    void shouldCreateMetadataWithDefaults() {
        assertNotNull(metadata);
        assertNotNull(metadata.getReassignmentHistory());
        assertTrue(metadata.getReassignmentHistory().isEmpty());
        assertEquals(1, metadata.getAttemptCount());
        assertNull(metadata.getAssignedBy());
        assertNull(metadata.getAssignmentAlgorithm());
    }

    @Test
    @DisplayName("Should set and get assignedBy")
    void shouldSetAndGetAssignedBy() {
        metadata.setAssignedBy("system");

        assertEquals("system", metadata.getAssignedBy());
    }

    @Test
    @DisplayName("Should set and get assignmentAlgorithm")
    void shouldSetAndGetAssignmentAlgorithm() {
        metadata.setAssignmentAlgorithm("proximity-based");

        assertEquals("proximity-based", metadata.getAssignmentAlgorithm());
    }

    @Test
    @DisplayName("Should add reassignment record")
    void shouldAddReassignmentRecord() {
        String fromDriverId = "driver-001";
        String toDriverId = "driver-002";
        Instant timestamp = Instant.now();
        String reason = "Driver declined";

        metadata.addReassignment(fromDriverId, toDriverId, timestamp, reason);

        assertEquals(1, metadata.getReassignmentHistory().size());
        assertEquals(2, metadata.getAttemptCount());
        assertEquals(fromDriverId, metadata.getPreviousDriverId());

        DriverAssignment.AssignmentMetadata.ReassignmentRecord record =
                metadata.getReassignmentHistory().get(0);
        assertEquals(fromDriverId, record.getFromDriverId());
        assertEquals(toDriverId, record.getToDriverId());
        assertEquals(timestamp, record.getTimestamp());
        assertEquals(reason, record.getReason());
    }

    @Test
    @DisplayName("Should add multiple reassignment records")
    void shouldAddMultipleReassignmentRecords() {
        Instant now = Instant.now();

        metadata.addReassignment("driver-001", "driver-002", now, "First reassignment");
        metadata.addReassignment("driver-002", "driver-003", now.plusSeconds(60), "Second reassignment");

        assertEquals(2, metadata.getReassignmentHistory().size());
        assertEquals(3, metadata.getAttemptCount());
        assertEquals("driver-002", metadata.getPreviousDriverId());
    }

    @Test
    @DisplayName("Should create reassignment history if null when adding")
    void shouldCreateHistoryIfNullWhenAdding() {
        metadata.setReassignmentHistory(null);

        metadata.addReassignment("driver-001", "driver-002", Instant.now(), "Reason");

        assertNotNull(metadata.getReassignmentHistory());
        assertEquals(1, metadata.getReassignmentHistory().size());
    }

    @Test
    @DisplayName("Should set and get reassignment history")
    void shouldSetAndGetReassignmentHistory() {
        List<DriverAssignment.AssignmentMetadata.ReassignmentRecord> history =
                List.of(new DriverAssignment.AssignmentMetadata.ReassignmentRecord(
                        "driver-001", "driver-002", Instant.now(), "Reason"));

        metadata.setReassignmentHistory(history);

        assertEquals(history, metadata.getReassignmentHistory());
        assertEquals(1, metadata.getReassignmentHistory().size());
    }

    @Test
    @DisplayName("Should set and get attempt count")
    void shouldSetAndGetAttemptCount() {
        metadata.setAttemptCount(5);

        assertEquals(5, metadata.getAttemptCount());
    }

    @Test
    @DisplayName("Should set and get previous driver ID")
    void shouldSetAndGetPreviousDriverId() {
        metadata.setPreviousDriverId("driver-123");

        assertEquals("driver-123", metadata.getPreviousDriverId());
    }

    @Test
    @DisplayName("Should increment attempt count on reassignment")
    void shouldIncrementAttemptCountOnReassignment() {
        int initialCount = metadata.getAttemptCount();

        metadata.addReassignment("driver-001", "driver-002", Instant.now(), "Reason");

        assertEquals(initialCount + 1, metadata.getAttemptCount());
    }

    @Test
    @DisplayName("Should update previous driver ID on reassignment")
    void shouldUpdatePreviousDriverIdOnReassignment() {
        metadata.addReassignment("driver-001", "driver-002", Instant.now(), "Reason");
        assertEquals("driver-001", metadata.getPreviousDriverId());

        metadata.addReassignment("driver-002", "driver-003", Instant.now(), "Reason");
        assertEquals("driver-002", metadata.getPreviousDriverId());
    }

    @Test
    @DisplayName("Should handle reassignment with null reason")
    void shouldHandleReassignmentWithNullReason() {
        metadata.addReassignment("driver-001", "driver-002", Instant.now(), null);

        assertEquals(1, metadata.getReassignmentHistory().size());
        assertNull(metadata.getReassignmentHistory().get(0).getReason());
    }
}
