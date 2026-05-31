package com.gogidix.courier.assignmentservice.application.command;

import java.util.Objects;

/**
 * Command to cancel a driver assignment.
 */
public record CancelAssignmentCommand(
        String assignmentId,
        String cancellationReason,
        String cancelledBy
) {
    public CancelAssignmentCommand {
        assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        cancelledBy = Objects.requireNonNull(cancelledBy, "cancelledBy is required");
        if (assignmentId.isBlank()) {
            throw new IllegalArgumentException("assignmentId cannot be blank");
        }
        if (cancelledBy.isBlank()) {
            throw new IllegalArgumentException("cancelledBy cannot be blank");
        }
    }
}
