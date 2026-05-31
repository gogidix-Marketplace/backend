package com.gogidix.courier.loadbalancingservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoadBalancingRule entity.
 */
@DisplayName("LoadBalancingRule Entity Tests")
class LoadBalancingRuleTest {

    @Test
    @DisplayName("Should create rule with valid parameters")
    void shouldCreateRuleWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String name = "Peak Hours Rule";
        LoadBalancingRule.RuleType ruleType = LoadBalancingRule.RuleType.TIME_BASED;
        LoadBalancingRule.BalancingStrategy strategy = LoadBalancingRule.BalancingStrategy.LEAST_LOADED;
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
        assertTrue(rule.getActive());
        assertTrue(rule.getConditions().isEmpty());
        assertTrue(rule.getZoneIds().isEmpty());
        assertEquals(0L, rule.getVersion());
        assertNotNull(rule.getCreatedAt());
        assertNotNull(rule.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LoadBalancingRule(null, "Rule", LoadBalancingRule.RuleType.ZONE_BASED,
                LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50)
        );
    }

    @Test
    @DisplayName("Should throw when name is null")
    void shouldThrowWhenNameIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LoadBalancingRule("tenant-001", null, LoadBalancingRule.RuleType.ZONE_BASED,
                LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50)
        );
    }

    @Test
    @DisplayName("Should throw when ruleType is null")
    void shouldThrowWhenRuleTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LoadBalancingRule("tenant-001", "Rule", null,
                LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50)
        );
    }

    @Test
    @DisplayName("Should throw when strategy is null")
    void shouldThrowWhenStrategyIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LoadBalancingRule("tenant-001", "Rule", LoadBalancingRule.RuleType.ZONE_BASED,
                null, 50)
        );
    }

    @Test
    @DisplayName("Should throw when priority is null")
    void shouldThrowWhenPriorityIsNull() {
        assertThrows(NullPointerException.class, () ->
            new LoadBalancingRule("tenant-001", "Rule", LoadBalancingRule.RuleType.ZONE_BASED,
                LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, null)
        );
    }

    @Test
    @DisplayName("Should activate rule")
    void shouldActivateRule() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50);
        rule.deactivate();
        assertFalse(rule.getActive());

        // When
        rule.activate();

        // Then
        assertTrue(rule.getActive());
        // After deactivate (v=1) then activate (v=2)
        assertEquals(2L, rule.getVersion());
    }

    @Test
    @DisplayName("Should deactivate rule")
    void shouldDeactivateRule() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.ROUND_ROBIN, 50);
        assertTrue(rule.getActive());

        // When
        rule.deactivate();

        // Then
        assertFalse(rule.getActive());
        assertEquals(1L, rule.getVersion());
    }

    @Test
    @DisplayName("Should add condition to rule")
    void shouldAddConditionToRule() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.LOAD_THRESHOLD, "lt", 80.0
        );

        // When
        rule.addCondition(condition);

        // Then
        assertEquals(1, rule.getConditions().size());
        assertTrue(rule.getConditions().contains(condition));
        assertEquals(1L, rule.getVersion());
    }

    @Test
    @DisplayName("Should not add null condition")
    void shouldNotAddNullCondition() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        long initialVersion = rule.getVersion();

        // When
        rule.addCondition(null);

        // Then
        assertTrue(rule.getConditions().isEmpty());
        assertEquals(initialVersion, rule.getVersion());
    }

    @Test
    @DisplayName("Should add zone to rule")
    void shouldAddZoneToRule() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);

        // When
        rule.addZone("zone-001");

        // Then
        assertEquals(1, rule.getZoneIds().size());
        assertTrue(rule.getZoneIds().contains("zone-001"));
    }

    @Test
    @DisplayName("Should not add duplicate zone")
    void shouldNotAddDuplicateZone() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);

        // When
        rule.addZone("zone-001");
        rule.addZone("zone-001");

        // Then
        assertEquals(1, rule.getZoneIds().size());
    }

    @Test
    @DisplayName("Should not add blank zone")
    void shouldNotAddBlankZone() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);

        // When
        rule.addZone("");
        rule.addZone("   ");

        // Then
        assertTrue(rule.getZoneIds().isEmpty());
    }

    @Test
    @DisplayName("Should not add null zone")
    void shouldNotAddNullZone() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);

        // When
        rule.addZone(null);

        // Then
        assertTrue(rule.getZoneIds().isEmpty());
    }

    @Test
    @DisplayName("Should update configuration")
    void shouldUpdateConfiguration() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CUSTOM, LoadBalancingRule.BalancingStrategy.CUSTOM, 50);
        Map<String, Object> config = new HashMap<>();
        config.put("max_assignments", 15);
        config.put("min_rating", 4.5);

        // When
        rule.updateConfig(config);

        // Then
        assertEquals(config, rule.getConfig());
        assertEquals(1L, rule.getVersion());
    }

    @Test
    @DisplayName("Should match evaluation context when active")
    void shouldMatchEvaluationContextWhenActive() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);
        rule.addZone("zone-001");
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            "zone-001", 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should not match when inactive")
    void shouldNotMatchWhenInactive() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);
        rule.deactivate();
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            "zone-001", 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should not match when zone not in rule")
    void shouldNotMatchWhenZoneNotInRule() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);
        rule.addZone("zone-001");
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            "zone-002", 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should match when zones are empty")
    void shouldMatchWhenZonesAreEmpty() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.PERFORMANCE_BASED, LoadBalancingRule.BalancingStrategy.PERFORMANCE_BASED, 50);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            "zone-001", 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should match when context zone is null")
    void shouldMatchWhenContextZoneIsNull() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.ZONE_BASED, LoadBalancingRule.BalancingStrategy.GEOGRAPHIC, 50);
        rule.addZone("zone-001");
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should evaluate driver count condition correctly")
    void shouldEvaluateDriverCountConditionCorrectly() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.DRIVER_COUNT, "gt", 3
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should fail driver count condition when not met")
    void shouldFailDriverCountConditionWhenNotMet() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.DRIVER_COUNT, "gt", 10
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should evaluate load threshold condition correctly")
    void shouldEvaluateLoadThresholdConditionCorrectly() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.LOAD_THRESHOLD, "lt", 80.0
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should evaluate min rating condition correctly")
    void shouldEvaluateMinRatingConditionCorrectly() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.PERFORMANCE_BASED, LoadBalancingRule.BalancingStrategy.PERFORMANCE_BASED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.MIN_RATING, "gte", 4.0
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertTrue(matches);
    }

    @Test
    @DisplayName("Should fail min rating condition when not met")
    void shouldFailMinRatingConditionWhenNotMet() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.PERFORMANCE_BASED, LoadBalancingRule.BalancingStrategy.PERFORMANCE_BASED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.MIN_RATING, "gte", 4.8
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should increment version on each modification")
    void shouldIncrementVersionOnEachModification() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CUSTOM, LoadBalancingRule.BalancingStrategy.CUSTOM, 50);
        long initialVersion = rule.getVersion();

        // When
        rule.activate();          // version +1
        rule.addZone("zone-001"); // version unchanged (addZone doesn't increment)
        rule.updateConfig(Map.of("key", "value")); // version +1
        rule.deactivate();        // version +1

        // Then: initial(0) + 3 increments = 3
        assertEquals(initialVersion + 3, rule.getVersion());
    }

    @Test
    @DisplayName("Should handle rule condition evaluation with null driver count")
    void shouldHandleConditionEvaluationWithNullDriverCount() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.DRIVER_COUNT, "gt", 3
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, null, 50.0, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should handle rule condition evaluation with null load")
    void shouldHandleConditionEvaluationWithNullLoad() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.CAPACITY_BASED, LoadBalancingRule.BalancingStrategy.LEAST_LOADED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.LOAD_THRESHOLD, "lt", 80.0
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, null, 4.5, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }

    @Test
    @DisplayName("Should handle rule condition evaluation with null rating")
    void shouldHandleConditionEvaluationWithNullRating() {
        // Given
        LoadBalancingRule rule = new LoadBalancingRule("tenant-001", "Rule",
            LoadBalancingRule.RuleType.PERFORMANCE_BASED, LoadBalancingRule.BalancingStrategy.PERFORMANCE_BASED, 50);
        LoadBalancingRule.RuleCondition condition = new LoadBalancingRule.RuleCondition(
            LoadBalancingRule.RuleCondition.ConditionType.MIN_RATING, "gte", 4.0
        );
        rule.addCondition(condition);
        LoadBalancingRule.EvaluationContext context = new LoadBalancingRule.EvaluationContext(
            null, 5, 50.0, null, "morning", "Monday"
        );

        // When
        boolean matches = rule.matches(context);

        // Then
        assertFalse(matches);
    }
}
