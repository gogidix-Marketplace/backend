package com.gogidix.courier.assignmentservice.application.command;

import java.util.Objects;

/**
 * Command to accept a driver assignment.
 */
public record AcceptAssignmentCommand(
        String assignmentId,
        String acceptedBy
) {
    public AcceptAssignmentCommand {
        assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        acceptedBy = Objects.requireNonNull(acceptedBy, "acceptedBy is required");
        if (assignmentId.isBlank()) {
            throw new IllegalArgumentException("assignmentId cannot be blank");
        }
        if (acceptedBy.isBlank()) {
            throw new IllegalArgumentException("acceptedBy cannot be blank");
        }
    }
}
