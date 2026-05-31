package com.gogidix.courier.assignmentservice.domain.policy;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.util.List;

public final class AssignmentValidationPolicy {

    private static final double MAX_DISTANCE_KM = 500.0;
    private static final int MAX_DURATION_MINUTES = 480;
    private static final int MAX_ACTIVE_ASSIGNMENTS_PER_DRIVER = 3;

    private AssignmentValidationPolicy() {
    }

    public static void validateCreation(DriverAssignment assignment) {
        if (assignment.getEstimatedDistanceKm() != null
                && assignment.getEstimatedDistanceKm() > MAX_DISTANCE_KM) {
            throw new IllegalArgumentException(
                    "Estimated distance exceeds maximum allowed: " + MAX_DISTANCE_KM + " km");
        }
        if (assignment.getEstimatedDurationMinutes() != null
                && assignment.getEstimatedDurationMinutes() > MAX_DURATION_MINUTES) {
            throw new IllegalArgumentException(
                    "Estimated duration exceeds maximum allowed: " + MAX_DURATION_MINUTES + " minutes");
        }
    }

    public static boolean canDriverAcceptMoreAssignments(
            String driverId, List<DriverAssignment> activeAssignments) {
        long activeCount = activeAssignments.stream()
                .filter(a -> a.getDriverId().equals(driverId))
                .filter(a -> !a.isTerminal())
                .count();
        return activeCount < MAX_ACTIVE_ASSIGNMENTS_PER_DRIVER;
    }

    public static void validateCompletion(Double actualDistanceKm, Integer actualDurationMinutes) {
        if (actualDistanceKm != null && actualDistanceKm < 0) {
            throw new IllegalArgumentException("Actual distance cannot be negative");
        }
        if (actualDurationMinutes != null && actualDurationMinutes < 0) {
            throw new IllegalArgumentException("Actual duration cannot be negative");
        }
    }

    public static double getMaxDistanceKm() { return MAX_DISTANCE_KM; }
    public static int getMaxDurationMinutes() { return MAX_DURATION_MINUTES; }
    public static int getMaxActiveAssignmentsPerDriver() { return MAX_ACTIVE_ASSIGNMENTS_PER_DRIVER; }
}
