package com.gogidix.courier.assignmentservice.application.command;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.util.Objects;

/**
 * Command to create a new driver assignment.
 */
public record CreateAssignmentCommand(
        String tenantId,
        String dispatchId,
        String driverId,
        DriverAssignment.Location pickupLocation,
        DriverAssignment.Location deliveryLocation,
        DriverAssignment.AssignmentPriority priority,
        String assignedBy,
        String assignmentAlgorithm,
        Double assignmentScore
) {
    public CreateAssignmentCommand {
        tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        dispatchId = Objects.requireNonNull(dispatchId, "dispatchId is required");
        driverId = Objects.requireNonNull(driverId, "driverId is required");
        pickupLocation = Objects.requireNonNull(pickupLocation, "pickupLocation is required");
        deliveryLocation = Objects.requireNonNull(deliveryLocation, "deliveryLocation is required");
        if (tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId cannot be blank");
        }
        if (dispatchId.isBlank()) {
            throw new IllegalArgumentException("dispatchId cannot be blank");
        }
        if (driverId.isBlank()) {
            throw new IllegalArgumentException("driverId cannot be blank");
        }
    }
}
