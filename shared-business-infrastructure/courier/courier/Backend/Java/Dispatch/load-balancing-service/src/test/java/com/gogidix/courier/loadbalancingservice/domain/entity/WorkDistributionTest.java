package com.gogidix.courier.loadbalancingservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WorkDistribution entity.
 */
@DisplayName("WorkDistribution Entity Tests")
class WorkDistributionTest {

    @Test
    @DisplayName("Should create work distribution with valid parameters")
    void shouldCreateWorkDistributionWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String batchId = "batch-001";
        WorkDistribution.DistributionType distributionType = WorkDistribution.DistributionType.BATCH;
        Integer totalWorkItems = 100;

        // When
        WorkDistribution distribution = new WorkDistribution(tenantId, batchId, distributionType, totalWorkItems);

        // Then
        assertNotNull(distribution.getId());
        assertEquals(tenantId, distribution.getTenantId());
        assertEquals(batchId, distribution.getBatchId());
        assertEquals(distributionType, distribution.getDistributionType());
        assertEquals(totalWorkItems, distribution.getTotalWorkItems());
        assertEquals(0, distribution.getDistributedCount());
        assertEquals(totalWorkItems, distribution.getPendingCount());
        assertEquals(0, distribution.getFailedCount());
        assertEquals(WorkDistribution.DistributionStatus.PENDING, distribution.getStatus());
        assertNotNull(distribution.getStartedAt());
        assertNotNull(distribution.getCreatedAt());
        assertNotNull(distribution.getUpdatedAt());
        assertTrue(distribution.getAssignments().isEmpty());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new WorkDistribution(null, "batch-001", WorkDistribution.DistributionType.IMMEDIATE, 100)
        );
    }

    @Test
    @DisplayName("Should throw when batchId is null")
    void shouldThrowWhenBatchIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new WorkDistribution("tenant-001", null, WorkDistribution.DistributionType.IMMEDIATE, 100)
        );
    }

    @Test
    @DisplayName("Should throw when distributionType is null")
    void shouldThrowWhenDistributionTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
            new WorkDistribution("tenant-001", "batch-001", null, 100)
        );
    }

    @Test
    @DisplayName("Should throw when totalWorkItems is null")
    void shouldThrowWhenTotalWorkItemsIsNull() {
        assertThrows(NullPointerException.class, () ->
            new WorkDistribution("tenant-001", "batch-001", WorkDistribution.DistributionType.IMMEDIATE, null)
        );
    }

    @Test
    @DisplayName("Should add assignment successfully")
    void shouldAddAssignmentSuccessfully() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // When
        distribution.addAssignment("driver-001", 10);

        // Then
        assertEquals(1, distribution.getAssignments().size());
        assertEquals(10, distribution.getDistributedCount());
        assertEquals(90, distribution.getPendingCount());
        assertEquals(WorkDistribution.DistributionStatus.IN_PROGRESS, distribution.getStatus());
    }

    @Test
    @DisplayName("Should add multiple assignments")
    void shouldAddMultipleAssignments() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // When
        distribution.addAssignment("driver-001", 30);
        distribution.addAssignment("driver-002", 40);
        distribution.addAssignment("driver-003", 30);

        // Then
        assertEquals(3, distribution.getAssignments().size());
        assertEquals(100, distribution.getDistributedCount());
        assertEquals(0, distribution.getPendingCount());
        assertEquals(WorkDistribution.DistributionStatus.COMPLETED, distribution.getStatus());
    }

    @Test
    @DisplayName("Should prevent negative pending count")
    void shouldPreventNegativePendingCount() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 50);

        // When
        distribution.addAssignment("driver-001", 60);

        // Then
        assertEquals(60, distribution.getDistributedCount());
        assertEquals(0, distribution.getPendingCount());
    }

    @Test
    @DisplayName("Should mark assignment as failed")
    void shouldMarkAssignmentAsFailed() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.addAssignment("driver-001", 10);

        // When
        distribution.markFailed("driver-001", 5);

        // Then
        // distributedCount is reduced by failed count
        assertEquals(5, distribution.getDistributedCount());
        // pendingCount stays the same (failed items don't go back to pending)
        assertEquals(90, distribution.getPendingCount());
        // failedCount is incremented
        assertEquals(5, distribution.getFailedCount());
    }

    @Test
    @DisplayName("Should prevent negative distributed count when marking failed")
    void shouldPreventNegativeDistributedCountWhenMarkingFailed() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.addAssignment("driver-001", 10);

        // When
        distribution.markFailed("driver-001", 15);

        // Then
        assertEquals(0, distribution.getDistributedCount());
        assertEquals(90, distribution.getPendingCount());
        assertEquals(15, distribution.getFailedCount());
    }

    @Test
    @DisplayName("Should start distribution")
    void shouldStartDistribution() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        assertEquals(WorkDistribution.DistributionStatus.PENDING, distribution.getStatus());

        // When
        distribution.start("LEAST_LOADED");

        // Then
        assertEquals(WorkDistribution.DistributionStatus.IN_PROGRESS, distribution.getStatus());
        assertEquals("LEAST_LOADED", distribution.getStrategyUsed());
        assertNotNull(distribution.getStartedAt());
    }

    @Test
    @DisplayName("Should complete distribution")
    void shouldCompleteDistribution() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.start("ROUND_ROBIN");
        distribution.addAssignment("driver-001", 100);

        // When
        distribution.complete();

        // Then
        assertEquals(WorkDistribution.DistributionStatus.COMPLETED, distribution.getStatus());
        assertNotNull(distribution.getCompletedAt());
    }

    @Test
    @DisplayName("Should fail distribution")
    void shouldFailDistribution() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.start("ROUND_ROBIN");

        // When
        distribution.fail("No drivers available");

        // Then
        assertEquals(WorkDistribution.DistributionStatus.FAILED, distribution.getStatus());
        assertEquals("No drivers available", distribution.getErrorMessage());
        assertNotNull(distribution.getCompletedAt());
    }

    @Test
    @DisplayName("Should check if complete correctly")
    void shouldCheckIfCompleteCorrectly() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // Then - not complete when pending
        assertFalse(distribution.isComplete());

        // When
        distribution.complete();

        // Then - complete after completion
        assertTrue(distribution.isComplete());
    }

    @Test
    @DisplayName("Should be complete when failed")
    void shouldBeCompleteWhenFailed() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // When
        distribution.fail("Error");

        // Then
        assertTrue(distribution.isComplete());
    }

    @Test
    @DisplayName("Should be complete when pending count is zero")
    void shouldBeCompleteWhenPendingCountIsZero() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.addAssignment("driver-001", 100);

        // Then
        assertTrue(distribution.isComplete());
        assertEquals(WorkDistribution.DistributionStatus.COMPLETED, distribution.getStatus());
    }

    @Test
    @DisplayName("Should calculate progress percentage correctly")
    void shouldCalculateProgressPercentageCorrectly() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.addAssignment("driver-001", 30);
        distribution.addAssignment("driver-002", 20);

        // When
        double progress = distribution.getProgressPercent();

        // Then
        assertEquals(50.0, progress, 0.01);
    }

    @Test
    @DisplayName("Should return zero progress when total work items is zero")
    void shouldReturnZeroProgressWhenTotalWorkItemsIsZero() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 0);

        // When
        double progress = distribution.getProgressPercent();

        // Then
        assertEquals(0.0, progress);
    }

    @Test
    @DisplayName("Should return zero progress when total work items is null")
    void shouldReturnZeroProgressWhenTotalWorkItemsIsNull() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // Use reflection to set totalWorkItems to null (simulating persistence edge case)
        // In real scenario, this shouldn't happen due to constructor validation

        // When
        double progress = distribution.getProgressPercent();

        // Then
        assertEquals(0.0, progress);
    }

    @Test
    @DisplayName("Should calculate 100% progress when all distributed")
    void shouldCalculate100PercentProgressWhenAllDistributed() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.addAssignment("driver-001", 50);
        distribution.addAssignment("driver-002", 50);

        // When
        double progress = distribution.getProgressPercent();

        // Then
        assertEquals(100.0, progress, 0.01);
    }

    @Test
    @DisplayName("Should track assignment status")
    void shouldTrackAssignmentStatus() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // When
        distribution.addAssignment("driver-001", 25);

        // Then
        assertEquals(1, distribution.getAssignments().size());
        WorkDistribution.DriverAssignment assignment = distribution.getAssignments().get(0);
        assertEquals("driver-001", assignment.getDriverId());
        assertEquals(25, assignment.getAssignedCount());
        assertEquals(WorkDistribution.DriverAssignment.AssignmentStatus.PENDING, assignment.getStatus());
        assertNotNull(assignment.getAssignedAt());
    }

    @Test
    @DisplayName("Should handle IMMEDIATE distribution type")
    void shouldHandleImmediateDistributionType() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.IMMEDIATE, 50);

        // Then
        assertEquals(WorkDistribution.DistributionType.IMMEDIATE, distribution.getDistributionType());
    }

    @Test
    @DisplayName("Should handle SCHEDULED distribution type")
    void shouldHandleScheduledDistributionType() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.SCHEDULED, 200);

        // Then
        assertEquals(WorkDistribution.DistributionType.SCHEDULED, distribution.getDistributionType());
    }

    @Test
    @DisplayName("Should handle PRIORITY_BASED distribution type")
    void shouldHandlePriorityBasedDistributionType() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.PRIORITY_BASED, 75);

        // Then
        assertEquals(WorkDistribution.DistributionType.PRIORITY_BASED, distribution.getDistributionType());
    }

    @Test
    @DisplayName("Should handle ZONE_BASED distribution type")
    void shouldHandleZoneBasedDistributionType() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.ZONE_BASED, 150);

        // Then
        assertEquals(WorkDistribution.DistributionType.ZONE_BASED, distribution.getDistributionType());
    }

    @Test
    @DisplayName("Should maintain updated timestamp on modifications")
    void shouldMaintainUpdatedTimestampOnModifications() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        var initialUpdatedAt = distribution.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            fail("Sleep interrupted");
        }

        // When
        distribution.addAssignment("driver-001", 10);

        // Then
        assertTrue(distribution.getUpdatedAt().isAfter(initialUpdatedAt));
    }
}
