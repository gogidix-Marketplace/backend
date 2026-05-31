package com.gogidix.courier.assignmentservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DriverAssignment domain entity.
 */
@DisplayName("DriverAssignment Domain Entity Tests")
class DriverAssignmentTest {

    private DriverAssignment.Location pickupLocation;
    private DriverAssignment.Location deliveryLocation;
    private DriverAssignment assignment;

    @BeforeEach
    void setUp() {
        pickupLocation = new DriverAssignment.Location(40.7128, -74.0060, "123 Pickup St");
        deliveryLocation = new DriverAssignment.Location(40.7580, -73.9855, "456 Delivery Ave");
        assignment = new DriverAssignment(
                "tenant-001",
                "dispatch-001",
                "driver-001",
                pickupLocation,
                deliveryLocation
        );
    }

    @Test
    @DisplayName("Should create a new driver assignment successfully")
    void shouldCreateNewDriverAssignment() {
        assertNotNull(assignment.getId());
        assertEquals("tenant-001", assignment.getTenantId());
        assertEquals("dispatch-001", assignment.getDispatchId());
        assertEquals("driver-001", assignment.getDriverId());
        assertEquals(DriverAssignment.AssignmentStatus.PENDING, assignment.getStatus());
        assertEquals(DriverAssignment.AssignmentPriority.NORMAL, assignment.getPriority());
        assertEquals(0.0, assignment.getAssignmentScore());
        assertNotNull(assignment.getCreatedAt());
        assertNotNull(assignment.getUpdatedAt());
        assertEquals(0L, assignment.getVersion());
    }

    @Test
    @DisplayName("Should accept a pending assignment")
    void shouldAcceptPendingAssignment() {
        assignment.accept();

        assertEquals(DriverAssignment.AssignmentStatus.ACCEPTED, assignment.getStatus());
        assertNotNull(assignment.getAcceptedAt());
        assertEquals(1L, assignment.getVersion());
    }

    @Test
    @DisplayName("Should not accept an already accepted assignment")
    void shouldNotAcceptAcceptedAssignment() {
        assignment.accept();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                assignment::accept
        );

        assertTrue(exception.getMessage().contains("can only be accepted in PENDING status"));
    }

    @Test
    @DisplayName("Should not accept an in-progress assignment")
    void shouldNotAcceptInProgressAssignment() {
        assignment.accept();
        assignment.start();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                assignment::accept
        );

        assertTrue(exception.getMessage().contains("can only be accepted in PENDING status"));
    }

    @Test
    @DisplayName("Should start an accepted assignment")
    void shouldStartAcceptedAssignment() {
        assignment.accept();
        assignment.start();

        assertEquals(DriverAssignment.AssignmentStatus.IN_PROGRESS, assignment.getStatus());
        assertNotNull(assignment.getStartedAt());
        assertEquals(2L, assignment.getVersion());
    }

    @Test
    @DisplayName("Should not start a pending assignment")
    void shouldNotStartPendingAssignment() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                assignment::start
        );

        assertTrue(exception.getMessage().contains("can only be started in ACCEPTED status"));
    }

    @Test
    @DisplayName("Should complete an in-progress assignment")
    void shouldCompleteInProgressAssignment() {
        assignment.accept();
        assignment.start();
        assignment.complete(12.5, 35);

        assertEquals(DriverAssignment.AssignmentStatus.COMPLETED, assignment.getStatus());
        assertNotNull(assignment.getCompletedAt());
        assertEquals(12.5, assignment.getActualDistanceKm());
        assertEquals(35, assignment.getActualDurationMinutes());
        assertEquals(3L, assignment.getVersion());
    }

    @Test
    @DisplayName("Should not complete a pending assignment")
    void shouldNotCompletePendingAssignment() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> assignment.complete(10.0, 30)
        );

        assertTrue(exception.getMessage().contains("can only be completed in IN_PROGRESS status"));
    }

    @Test
    @DisplayName("Should cancel a pending assignment")
    void shouldCancelPendingAssignment() {
        assignment.cancel("Driver not available");

        assertEquals(DriverAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
        assertNotNull(assignment.getCancelledAt());
        assertEquals("Driver not available", assignment.getCancellationReason());
        assertEquals(1L, assignment.getVersion());
    }

    @Test
    @DisplayName("Should cancel an accepted assignment")
    void shouldCancelAcceptedAssignment() {
        assignment.accept();
        assignment.cancel("Customer requested cancellation");

        assertEquals(DriverAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
        assertEquals("Customer requested cancellation", assignment.getCancellationReason());
    }

    @Test
    @DisplayName("Should cancel an in-progress assignment")
    void shouldCancelInProgressAssignment() {
        assignment.accept();
        assignment.start();
        assignment.cancel("Emergency");

        assertEquals(DriverAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
    }

    @Test
    @DisplayName("Should not cancel a completed assignment")
    void shouldNotCancelCompletedAssignment() {
        assignment.accept();
        assignment.start();
        assignment.complete(10.0, 30);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> assignment.cancel("Reason")
        );

        assertTrue(exception.getMessage().contains("Cannot cancel a completed assignment"));
    }

    @Test
    @DisplayName("Should not cancel an already cancelled assignment")
    void shouldNotCancelAlreadyCancelledAssignment() {
        assignment.cancel("Reason");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> assignment.cancel("Another reason")
        );

        assertTrue(exception.getMessage().contains("already cancelled"));
    }

    @Test
    @DisplayName("Should reassign a pending assignment")
    void shouldReassignPendingAssignment() {
        assignment.reassign("driver-002", "Original driver declined");

        assertEquals("driver-002", assignment.getDriverId());
        assertEquals(DriverAssignment.AssignmentStatus.PENDING, assignment.getStatus());
        assertEquals("Original driver declined", assignment.getAssignmentReason());
        assertNull(assignment.getAcceptedAt());
        assertEquals(1L, assignment.getVersion());
        assertEquals("driver-001", assignment.getMetadata().getPreviousDriverId());
    }

    @Test
    @DisplayName("Should reassign an accepted assignment")
    void shouldReassignAcceptedAssignment() {
        assignment.accept();
        assignment.reassign("driver-002", "Driver timeout");

        assertEquals("driver-002", assignment.getDriverId());
        assertEquals(DriverAssignment.AssignmentStatus.PENDING, assignment.getStatus());
        assertNull(assignment.getAcceptedAt());
    }

    @Test
    @DisplayName("Should not reassign a completed assignment")
    void shouldNotReassignCompletedAssignment() {
        assignment.accept();
        assignment.start();
        assignment.complete(10.0, 30);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> assignment.reassign("driver-002", "Reason")
        );

        assertTrue(exception.getMessage().contains("Cannot reassign a COMPLETED assignment"));
    }

    @Test
    @DisplayName("Should not reassign a cancelled assignment")
    void shouldNotReassignCancelledAssignment() {
        assignment.cancel("Reason");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> assignment.reassign("driver-002", "Reason")
        );

        assertTrue(exception.getMessage().contains("Cannot reassign a CANCELLED assignment"));
    }

    @Test
    @DisplayName("Should update assignment score")
    void shouldUpdateAssignmentScore() {
        assignment.updateScore(85.5);

        assertEquals(85.5, assignment.getAssignmentScore());
    }

    @Test
    @DisplayName("Should set estimates")
    void shouldSetEstimates() {
        assignment.setEstimates(15.5, 45);

        assertEquals(15.5, assignment.getEstimatedDistanceKm());
        assertEquals(45, assignment.getEstimatedDurationMinutes());
    }

    @Test
    @DisplayName("Should set priority")
    void shouldSetPriority() {
        assignment.setPriority(DriverAssignment.AssignmentPriority.URGENT);

        assertEquals(DriverAssignment.AssignmentPriority.URGENT, assignment.getPriority());
    }

    @Test
    @DisplayName("Should return true when assignment is terminal - completed")
    void shouldReturnTrueWhenTerminalCompleted() {
        assignment.accept();
        assignment.start();
        assignment.complete(10.0, 30);

        assertTrue(assignment.isTerminal());
    }

    @Test
    @DisplayName("Should return true when assignment is terminal - cancelled")
    void shouldReturnTrueWhenTerminalCancelled() {
        assignment.cancel("Reason");

        assertTrue(assignment.isTerminal());
    }

    @Test
    @DisplayName("Should return false when assignment is not terminal")
    void shouldReturnFalseWhenNotTerminal() {
        assertFalse(assignment.isTerminal());
    }

    @Test
    @DisplayName("Should return true when assignment is reassignable - pending")
    void shouldReturnTrueWhenReassignablePending() {
        assertTrue(assignment.isReassignable());
    }

    @Test
    @DisplayName("Should return true when assignment is reassignable - accepted")
    void shouldReturnTrueWhenReassignableAccepted() {
        assignment.accept();

        assertTrue(assignment.isReassignable());
    }

    @Test
    @DisplayName("Should return false when assignment is not reassignable - in progress")
    void shouldReturnFalseWhenNotReassignableInProgress() {
        assignment.accept();
        assignment.start();

        assertFalse(assignment.isReassignable());
    }

    @Test
    @DisplayName("Should calculate efficiency ratio")
    void shouldCalculateEfficiencyRatio() {
        assignment.setEstimates(10.0, 30);
        assignment.accept();
        assignment.start();
        assignment.complete(12.5, 35);

        Double efficiency = assignment.calculateEfficiencyRatio();

        assertNotNull(efficiency);
        assertEquals(10.0 / 12.5, efficiency);
    }

    @Test
    @DisplayName("Should return null efficiency ratio when not completed")
    void shouldReturnNullEfficiencyRatioWhenNotCompleted() {
        assignment.setEstimates(10.0, 30);

        assertNull(assignment.calculateEfficiencyRatio());
    }

    @Test
    @DisplayName("Should return null efficiency ratio when no estimates")
    void shouldReturnNullEfficiencyRatioWhenNoEstimates() {
        assignment.accept();
        assignment.start();
        assignment.complete(12.5, 35);

        assertNull(assignment.calculateEfficiencyRatio());
    }

    @Test
    @DisplayName("Should validate valid assignment")
    void shouldValidateValidAssignment() {
        assertDoesNotThrow(assignment::validate);
    }

    @Test
    @DisplayName("Should throw on blank tenantId")
    void shouldThrowOnBlankTenantId() {
        assignment.setTenantId(" ");

        assertThrows(IllegalArgumentException.class, assignment::validate);
    }

    @Test
    @DisplayName("Should throw on blank dispatchId")
    void shouldThrowOnBlankDispatchId() {
        assignment.setDispatchId("");

        assertThrows(IllegalArgumentException.class, assignment::validate);
    }

    @Test
    @DisplayName("Should throw on blank driverId")
    void shouldThrowOnBlankDriverId() {
        assignment.setDriverId(null);

        assertThrows(IllegalArgumentException.class, assignment::validate);
    }

    @Test
    @DisplayName("Should throw on null pickup location")
    void shouldThrowOnNullPickupLocation() {
        assignment.setPickupLocation(null);

        assertThrows(IllegalArgumentException.class, assignment::validate);
    }

    @Test
    @DisplayName("Should throw on null delivery location")
    void shouldThrowOnNullDeliveryLocation() {
        assignment.setDeliveryLocation(null);

        assertThrows(IllegalArgumentException.class, assignment::validate);
    }

    @Test
    @DisplayName("Should maintain version on state changes")
    void shouldMaintainVersionOnStateChanges() {
        long initialVersion = assignment.getVersion();

        assignment.accept();
        assertEquals(initialVersion + 1, assignment.getVersion());

        assignment.start();
        assertEquals(initialVersion + 2, assignment.getVersion());

        assignment.complete(10.0, 30);
        assertEquals(initialVersion + 3, assignment.getVersion());
    }

    @Test
    @DisplayName("Should track reassignment in metadata")
    void shouldTrackReassignmentInMetadata() {
        assignment.reassign("driver-002", "First reassignment");

        DriverAssignment.AssignmentMetadata metadata = assignment.getMetadata();
        assertNotNull(metadata);
        assertEquals(2, metadata.getAttemptCount());
        assertEquals("driver-001", metadata.getPreviousDriverId());
        assertEquals(1, metadata.getReassignmentHistory().size());

        DriverAssignment.AssignmentMetadata.ReassignmentRecord record =
                metadata.getReassignmentHistory().get(0);
        assertEquals("driver-001", record.getFromDriverId());
        assertEquals("driver-002", record.getToDriverId());
        assertEquals("First reassignment", record.getReason());
        assertNotNull(record.getTimestamp());
    }

    @Test
    @DisplayName("Should handle multiple reassignments")
    void shouldHandleMultipleReassignments() {
        assignment.reassign("driver-002", "First reassignment");
        assignment.reassign("driver-003", "Second reassignment");

        assertEquals(3, assignment.getMetadata().getAttemptCount());
        assertEquals(2, assignment.getMetadata().getReassignmentHistory().size());
    }

    @Test
    @DisplayName("Should set and retrieve metadata")
    void shouldSetAndRetrieveMetadata() {
        DriverAssignment.AssignmentMetadata metadata = new DriverAssignment.AssignmentMetadata();
        metadata.setAssignedBy("system");
        metadata.setAssignmentAlgorithm("proximity-score");

        assignment.setMetadata(metadata);

        assertEquals("system", assignment.getMetadata().getAssignedBy());
        assertEquals("proximity-score", assignment.getMetadata().getAssignmentAlgorithm());
    }

    @Test
    @DisplayName("Should handle notes")
    void shouldHandleNotes() {
        assignment.setNotes("Customer requested careful handling");

        assertEquals("Customer requested careful handling", assignment.getNotes());
    }

    @Test
    @DisplayName("Should create assignment with assigned timestamp")
    void shouldCreateWithAssignedTimestamp() {
        Instant assignedAt = Instant.now();
        assignment.setAssignedAt(assignedAt);

        assertEquals(assignedAt, assignment.getAssignedAt());
    }
}
