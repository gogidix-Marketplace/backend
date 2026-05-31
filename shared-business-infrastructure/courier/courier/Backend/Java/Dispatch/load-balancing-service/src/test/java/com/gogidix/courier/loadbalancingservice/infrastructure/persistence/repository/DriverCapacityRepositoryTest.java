package com.gogidix.courier.loadbalancingservice.infrastructure.persistence.repository;

import com.gogidix.courier.loadbalancingservice.domain.entity.DriverCapacity;
import com.gogidix.courier.loadbalancingservice.domain.entity.LoadBalancingRule;
import com.gogidix.courier.loadbalancingservice.domain.entity.WorkDistribution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Load Balancing domain entities.
 * Note: Testing domain entity behavior without Spring context.
 */
@DisplayName("Load Balancing Domain Entity Tests")
class DriverCapacityRepositoryTest {

    // Note: These tests would use actual repository interfaces
    // For now, we're testing the domain entities can be serialized/deserialized

    @Test
    @DisplayName("Should create DriverCapacity entity")
    void shouldCreateDriverCapacityEntity() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String zoneId = "zone-001";
        Integer maxCapacity = 10;

        // When
        DriverCapacity capacity = new DriverCapacity(tenantId, driverId, zoneId, maxCapacity);

        // Then
        assertNotNull(capacity.getId());
        assertEquals(tenantId, capacity.getTenantId());
        assertEquals(driverId, capacity.getDriverId());
        assertEquals(zoneId, capacity.getZoneId());
        assertEquals(maxCapacity, capacity.getMaxCapacity());
    }

    @Test
    @DisplayName("Should create LoadBalancingRule entity")
    void shouldCreateLoadBalancingRuleEntity() {
        // Given
        String tenantId = "tenant-001";
        String name = "Test Rule";
        LoadBalancingRule.RuleType ruleType = LoadBalancingRule.RuleType.ZONE_BASED;
        LoadBalancingRule.BalancingStrategy strategy = LoadBalancingRule.BalancingStrategy.ROUND_ROBIN;
        Integer priority = 100;

        // When
        LoadBalancingRule rule = new LoadBalancingRule(tenantId, name, ruleType, strategy, priority);

        // Then
        assertNotNull(rule.getId());
        assertEquals(tenantId, rule.getTenantId());
        assertEquals(name, rule.getName());
        assertEquals(ruleType, rule.getRuleType());
        assertEquals(strategy, rule.getStrategy());
        assertEquals(priority, rule.getPriority());
    }

    @Test
    @DisplayName("Should create WorkDistribution entity")
    void shouldCreateWorkDistributionEntity() {
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
    }

    @Test
    @DisplayName("Should handle DriverCapacity state transitions")
    void shouldHandleDriverCapacityStateTransitions() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);

        // When - assign work
        capacity.assign();
        capacity.assign();
        capacity.assign();

        // Then
        assertEquals(3, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.AVAILABLE, capacity.getStatus());

        // When - assign more to reach high load
        for (int i = 0; i < 6; i++) {
            capacity.assign();
        }

        // Then
        assertEquals(9, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.HIGH_LOAD, capacity.getStatus());

        // When - assign last item
        capacity.assign();

        // Then
        assertEquals(10, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.FULL, capacity.getStatus());
    }

    @Test
    @DisplayName("Should handle LoadBalancingRule condition matching")
    void shouldHandleLoadBalancingRuleConditionMatching() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Test Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 100);
        rule.addZone("zone-001");

        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.DRIVER_COUNT, "gt", 3
        );
        rule.addCondition(condition);

        LoadBalancingRule.EvaluationContext matchingContext = new LoadBalancingRule.EvaluationContext(
            "zone-001", 5, 50.0, 4.5, "morning", "Monday"
        );

        LoadBalancingRule.EvaluationContext nonMatchingContext = new LoadBalancingRule.EvaluationContext(
            "zone-001", 2, 50.0, 4.5, "morning", "Monday"
        );

        // When & Then
        assertTrue(rule.matches(matchingContext));
        assertFalse(rule.matches(nonMatchingContext));
    }

    @Test
    @DisplayName("Should handle WorkDistribution lifecycle")
    void shouldHandleWorkDistributionLifecycle() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);

        // Then - initial state
        assertEquals(WorkDistribution.DistributionStatus.PENDING, distribution.getStatus());
        assertEquals(0, distribution.getDistributedCount());
        assertEquals(100, distribution.getPendingCount());

        // When - start
        distribution.start("LEAST_LOADED");
        assertEquals(WorkDistribution.DistributionStatus.IN_PROGRESS, distribution.getStatus());

        // When - add assignments
        distribution.addAssignment("driver-001", 30);
        distribution.addAssignment("driver-002", 40);
        distribution.addAssignment("driver-003", 30);

        // Then - after assignments
        assertEquals(100, distribution.getDistributedCount());
        assertEquals(0, distribution.getPendingCount());
        assertEquals(100.0, distribution.getProgressPercent(), 0.01);

        // When - complete
        distribution.complete();

        // Then
        assertEquals(WorkDistribution.DistributionStatus.COMPLETED, distribution.getStatus());
        assertTrue(distribution.isComplete());
    }

    @Test
    @DisplayName("Should handle WorkDistribution failure scenario")
    void shouldHandleWorkDistributionFailureScenario() {
        // Given
        WorkDistribution distribution = new WorkDistribution("tenant-001", "batch-001",
            WorkDistribution.DistributionType.BATCH, 100);
        distribution.start("ROUND_ROBIN");
        distribution.addAssignment("driver-001", 50);

        // When - some assignments fail
        distribution.markFailed("driver-001", 10);

        // Then
        assertEquals(40, distribution.getDistributedCount());
        assertEquals(10, distribution.getFailedCount());
        // pendingCount stays at 50 (failed items don't go back to pending)
        assertEquals(50, distribution.getPendingCount());

        // When - fail entire distribution
        distribution.fail("System error");

        // Then
        assertEquals(WorkDistribution.DistributionStatus.FAILED, distribution.getStatus());
        assertEquals("System error", distribution.getErrorMessage());
        assertTrue(distribution.isComplete());
    }

    @Test
    @DisplayName("Should handle DriverCapacity release and reassign")
    void shouldHandleDriverCapacityReleaseAndReassign() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);

        // When - assign to capacity
        for (int i = 0; i < 10; i++) {
            capacity.assign();
        }

        // Then
        assertEquals(DriverCapacity.CapacityStatus.FULL, capacity.getStatus());
        assertFalse(capacity.assign());

        // When - release some
        capacity.release();
        capacity.release();

        // Then
        assertEquals(8, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.HIGH_LOAD, capacity.getStatus());
        assertTrue(capacity.assign());
    }

    @Test
    @DisplayName("Should handle LoadBalancingRule version increment")
    void shouldHandleLoadBalancingRuleVersionIncrement() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50);
        long initialVersion = rule.getVersion();

        // When - multiple modifications
        rule.addZone("zone-001");   // no version increment
        rule.addZone("zone-002");   // no version increment
        rule.activate();            // version +1
        rule.updateConfig(java.util.Map.of("key", "value")); // version +1

        // Then: initial(0) + 2 increments = 2
        assertEquals(initialVersion + 2, rule.getVersion());
    }
}
