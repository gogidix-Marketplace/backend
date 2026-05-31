package com.gogidix.courier.assignmentservice.application.command;

import java.util.Objects;

/**
 * Command to assign a driver to a dispatch.
 */
public record AssignDriverCommand(
        String assignmentId,
        String driverId,
        String reason,
        Double assignmentScore,
        String assignedBy
) {
    public AssignDriverCommand {
        assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        driverId = Objects.requireNonNull(driverId, "driverId is required");
        if (assignmentId.isBlank()) {
            throw new IllegalArgumentException("assignmentId cannot be blank");
        }
        if (driverId.isBlank()) {
            throw new IllegalArgumentException("driverId cannot be blank");
        }
        if (assignmentScore != null && assignmentScore < 0) {
            throw new IllegalArgumentException("assignmentScore must be non-negative");
        }
    }
}
