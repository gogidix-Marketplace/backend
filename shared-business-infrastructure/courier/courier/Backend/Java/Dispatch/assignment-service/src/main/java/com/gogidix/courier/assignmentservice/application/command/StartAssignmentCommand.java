package com.gogidix.courier.assignmentservice.application.command;

import java.util.Objects;

/**
 * Command to start a driver assignment (driver en route to pickup).
 */
public record StartAssignmentCommand(
        String assignmentId,
        String startedBy
) {
    public StartAssignmentCommand {
        assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        startedBy = Objects.requireNonNull(startedBy, "startedBy is required");
        if (assignmentId.isBlank()) {
            throw new IllegalArgumentException("assignmentId cannot be blank");
        }
        if (startedBy.isBlank()) {
            throw new IllegalArgumentException("startedBy cannot be blank");
        }
    }
}
