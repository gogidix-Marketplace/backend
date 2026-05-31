package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for assignment statistics.
 */
@Schema(description = "Assignment statistics")
public record AssignmentStats(

        @JsonProperty("total_assignments")
        @Schema(description = "Total number of assignments", example = "1500")
        long totalAssignments,

        @JsonProperty("active_assignments")
        @Schema(description = "Number of active assignments", example = "45")
        long activeAssignments,

        @JsonProperty("completed_assignments")
        @Schema(description = "Number of completed assignments", example = "1400")
        long completedAssignments,

        @JsonProperty("cancelled_assignments")
        @Schema(description = "Number of cancelled assignments", example = "50")
        long cancelledAssignments,

        @JsonProperty("failed_assignments")
        @Schema(description = "Number of failed assignments", example = "5")
        long failedAssignments,

        @JsonProperty("pending_assignments")
        @Schema(description = "Number of pending assignments", example = "10")
        long pendingAssignments,

        @JsonProperty("average_assignment_score")
        @Schema(description = "Average assignment optimization score", example = "0.78")
        Double averageAssignmentScore,

        @JsonProperty("average_completion_time_minutes")
        @Schema(description = "Average completion time in minutes", example = "25.5")
        Double averageCompletionTimeMinutes,

        @JsonProperty("average_distance_km")
        @Schema(description = "Average distance traveled in km", example = "6.2")
        Double averageDistanceKm,

        @JsonProperty("assignments_by_status")
        @Schema(description = "Breakdown of assignments by status")
        Map<String, Long> assignmentsByStatus,

        @JsonProperty("assignments_by_priority")
        @Schema(description = "Breakdown of assignments by priority")
        Map<String, Long> assignmentsByPriority,

        @JsonProperty("driver_stats")
        @Schema(description = "Per-driver assignment statistics")
        Map<String, DriverStatEntry> driverStats,

        @JsonProperty("generated_at")
        @Schema(description = "Timestamp when stats were generated", example = "2023-01-01T12:00:00Z")
        Instant generatedAt
) {
    /**
     * Driver stat entry.
     */
    @Schema(description = "Driver statistics entry")
    public record DriverStatEntry(

            @JsonProperty("total_assignments")
            @Schema(description = "Total assignments for driver", example = "25")
            long totalAssignments,

            @JsonProperty("active_assignments")
            @Schema(description = "Active assignments for driver", example = "2")
            long activeAssignments,

            @JsonProperty("completed_assignments")
            @Schema(description = "Completed assignments for driver", example = "22")
            long completedAssignments,

            @JsonProperty("cancelled_assignments")
            @Schema(description = "Cancelled assignments for driver", example = "1")
            long cancelledAssignments,

            @JsonProperty("average_score")
            @Schema(description = "Average assignment score", example = "0.82")
            Double averageScore,

            @JsonProperty("average_completion_time_minutes")
            @Schema(description = "Average completion time in minutes", example = "22.5")
            Double averageCompletionTimeMinutes
    ) {}
}
