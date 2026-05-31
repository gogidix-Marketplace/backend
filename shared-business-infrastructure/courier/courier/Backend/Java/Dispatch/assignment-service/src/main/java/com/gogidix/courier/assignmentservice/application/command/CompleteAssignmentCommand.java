package com.gogidix.courier.assignmentservice.application.command;

import java.util.Objects;

/**
 * Command to complete a driver assignment.
 */
public record CompleteAssignmentCommand(
        String assignmentId,
        Double actualDistanceKm,
        Integer actualDurationMinutes,
        String completedBy
) {
    public CompleteAssignmentCommand {
        assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        if (assignmentId.isBlank()) {
            throw new IllegalArgumentException("assignmentId cannot be blank");
        }
        if (actualDistanceKm != null && actualDistanceKm < 0) {
            throw new IllegalArgumentException("actualDistanceKm must be non-negative");
        }
        if (actualDurationMinutes != null && actualDurationMinutes < 0) {
            throw new IllegalArgumentException("actualDurationMinutes must be non-negative");
        }
    }
}
