package com.gogidix.courier.assignmentservice.domain.policy;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.time.Duration;
import java.time.Instant;

public final class AssignmentTimeoutPolicy {

    private static final Duration DEFAULT_PENDING_TIMEOUT = Duration.ofMinutes(15);
    private static final Duration DEFAULT_ACCEPTED_TIMEOUT = Duration.ofMinutes(30);
    private static final int MAX_REASSIGNMENT_ATTEMPTS = 3;

    private AssignmentTimeoutPolicy() {
    }

    public static boolean isPendingTimedOut(DriverAssignment assignment) {
        if (assignment.getStatus() != DriverAssignment.AssignmentStatus.PENDING) {
            return false;
        }
        Instant cutoff = Instant.now().minus(DEFAULT_PENDING_TIMEOUT);
        return assignment.getAssignedAt() != null && assignment.getAssignedAt().isBefore(cutoff);
    }

    public static boolean isAcceptedTimedOut(DriverAssignment assignment) {
        if (assignment.getStatus() != DriverAssignment.AssignmentStatus.ACCEPTED) {
            return false;
        }
        Instant cutoff = Instant.now().minus(DEFAULT_ACCEPTED_TIMEOUT);
        return assignment.getAcceptedAt() != null && assignment.getAcceptedAt().isBefore(cutoff);
    }

    public static boolean canReassign(DriverAssignment assignment) {
        if (assignment.getMetadata() == null) {
            return true;
        }
        return assignment.getMetadata().getAttemptCount() < MAX_REASSIGNMENT_ATTEMPTS;
    }

    public static Duration getPendingTimeout() {
        return DEFAULT_PENDING_TIMEOUT;
    }

    public static Duration getAcceptedTimeout() {
        return DEFAULT_ACCEPTED_TIMEOUT;
    }

    public static int getMaxReassignmentAttempts() {
        return MAX_REASSIGNMENT_ATTEMPTS;
    }
}
