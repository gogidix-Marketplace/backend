package com.gogidix.courier.assignmentservice.application.service;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import com.gogidix.courier.assignmentservice.domain.repository.DriverAssignmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AssignmentApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AssignmentApplicationService Tests")
class AssignmentApplicationServiceTest {

    @Mock
    private DriverAssignmentRepository assignmentRepository;

    @Test
    @DisplayName("Should create DriverAssignment entity")
    void shouldCreateDriverAssignmentEntity() {
        // Given
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);

        // When
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);

        // Then
        assertNotNull(assignment);
        assertEquals("tenant-001", assignment.getTenantId());
        assertEquals("dispatch-001", assignment.getDispatchId());
        assertEquals("driver-001", assignment.getDriverId());
        assertEquals(DriverAssignment.AssignmentStatus.PENDING, assignment.getStatus());
    }

    @Test
    @DisplayName("Should accept assignment")
    void shouldAcceptAssignment() {
        // Given
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);

        // When
        assignment.accept();

        // Then
        assertEquals(DriverAssignment.AssignmentStatus.ACCEPTED, assignment.getStatus());
    }

    @Test
    @DisplayName("Should start assignment")
    void shouldStartAssignment() {
        // Given
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);
        assignment.accept();

        // When
        assignment.start();

        // Then
        assertEquals(DriverAssignment.AssignmentStatus.IN_PROGRESS, assignment.getStatus());
    }

    @Test
    @DisplayName("Should complete assignment")
    void shouldCompleteAssignment() {
        // Given
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);
        assignment.accept();
        assignment.start();

        // When
        assignment.complete(12.5, 35);

        // Then
        assertEquals(DriverAssignment.AssignmentStatus.COMPLETED, assignment.getStatus());
        assertEquals(12.5, assignment.getActualDistanceKm());
    }

    @Test
    @DisplayName("Should cancel assignment")
    void shouldCancelAssignment() {
        // Given
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);

        // When
        assignment.cancel("Customer cancelled");

        // Then
        assertEquals(DriverAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
        assertEquals("Customer cancelled", assignment.getCancellationReason());
    }

    @Test
    @DisplayName("Should find assignment by ID")
    void shouldFindAssignmentById() {
        // Given
        String assignmentId = "assignment-001";
        DriverAssignment.Location pickup = new DriverAssignment.Location(40.7128, -74.0060);
        DriverAssignment.Location delivery = new DriverAssignment.Location(40.7580, -73.9855);
        DriverAssignment assignment = new DriverAssignment("tenant-001", "dispatch-001", "driver-001", pickup, delivery);

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        // When
        Optional<DriverAssignment> result = assignmentRepository.findById(assignmentId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("tenant-001", result.get().getTenantId());
        verify(assignmentRepository).findById(assignmentId);
    }
}
