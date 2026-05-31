package com.gogidix.courier.assignmentservice.application.query;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Query for filtering driver assignments.
 */
@Schema(description = "Query parameters for assignment filtering")
public record AssignmentQuery(

        @Schema(description = "Filter by dispatch ID")
        String dispatchId,

        @Schema(description = "Filter by driver ID")
        String driverId,

        @Schema(description = "Filter by status")
        DriverAssignment.AssignmentStatus status,

        @Schema(description = "Filter by priority")
        DriverAssignment.AssignmentPriority priority,

        @Schema(description = "Filter by created after timestamp")
        Instant createdAfter,

        @Schema(description = "Filter by created before timestamp")
        Instant createdBefore,

        @Schema(description = "Filter by assigned after timestamp")
        Instant assignedAfter,

        @Schema(description = "Filter by assigned before timestamp")
        Instant assignedBefore,

        @Schema(description = "Include only active assignments")
        Boolean activeOnly,

        @Schema(description = "Page number (0-based)")
        int page,

        @Schema(description = "Page size")
        int size,

        @Schema(description = "Sort field")
        String sortBy,

        @Schema(description = "Sort direction (ASC/DESC)")
        String sortDirection
) {
    public AssignmentQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
    }

    public static AssignmentQuery create() {
        return new AssignmentQuery(
                null, null, null, null, null, null, null, null,
                false, 0, 20, "createdAt", "DESC"
        );
    }

    public AssignmentQuery withDispatchId(String dispatchId) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withDriverId(String driverId) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withStatus(DriverAssignment.AssignmentStatus status) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withPriority(DriverAssignment.AssignmentPriority priority) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withPagination(int page, int size) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withSort(String sortBy, String sortDirection) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }

    public AssignmentQuery withActiveOnly(boolean activeOnly) {
        return new AssignmentQuery(
                dispatchId, driverId, status, priority, createdAfter, createdBefore,
                assignedAfter, assignedBefore, activeOnly, page, size, sortBy, sortDirection
        );
    }
}
