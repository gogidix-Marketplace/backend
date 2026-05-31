package com.gogidix.monitoring.alertmanagementservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlertRule Domain Model Tests")
class AlertRuleTest {

    @Test
    @DisplayName("Should create alert rule with builder")
    void shouldCreateAlertRuleWithBuilder() {
        // Given
        Instant now = Instant.now();

        // When
        AlertRule rule = AlertRule.builder()
                .id("rule-123")
                .tenantId("tenant-1")
                .name("High CPU Usage")
                .description("Alert when CPU usage exceeds threshold")
                .enabled(true)
                .serviceName("order-service")
                .metricName("cpu.usage")
                .conditionType(AlertRule.ConditionType.THRESHOLD)
                .threshold(80.0)
                .operator(AlertRule.ComparisonOperator.GREATER_THAN)
                .durationSeconds(300)
                .severity(AlertRule.AlertSeverity.HIGH)
                .notificationChannels(List.of(
                        AlertRule.NotificationChannel.EMAIL,
                        AlertRule.NotificationChannel.SLACK
                ))
                .recipients(List.of("admin@example.com", "#alerts"))
                .cooldownSeconds(600)
                .messageTemplate("CPU usage is {value}% on {service}")
                .metadata(Map.of("team", "platform"))
                .tags(Map.of("environment", "production"))
                .createdAt(now)
                .createdBy("user-1")
                .build();

        // Then
        assertEquals("rule-123", rule.getId());
        assertEquals("tenant-1", rule.getTenantId());
        assertEquals("High CPU Usage", rule.getName());
        assertEquals("Alert when CPU usage exceeds threshold", rule.getDescription());
        assertTrue(rule.getEnabled());
        assertEquals("order-service", rule.getServiceName());
        assertEquals("cpu.usage", rule.getMetricName());
        assertEquals(AlertRule.ConditionType.THRESHOLD, rule.getConditionType());
        assertEquals(80.0, rule.getThreshold());
        assertEquals(AlertRule.ComparisonOperator.GREATER_THAN, rule.getOperator());
        assertEquals(300, rule.getDurationSeconds());
        assertEquals(AlertRule.AlertSeverity.HIGH, rule.getSeverity());
        assertEquals(2, rule.getNotificationChannels().size());
        assertEquals(2, rule.getRecipients().size());
        assertEquals(600, rule.getCooldownSeconds());
        assertEquals("CPU usage is {value}% on {service}", rule.getMessageTemplate());
        assertEquals(Map.of("team", "platform"), rule.getMetadata());
        assertEquals(Map.of("environment", "production"), rule.getTags());
        assertEquals(now, rule.getCreatedAt());
        assertEquals("user-1", rule.getCreatedBy());
    }

    @Test
    @DisplayName("Should evaluate greater than operator correctly")
    void shouldEvaluateGreaterThanOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.GREATER_THAN)
                .threshold(80.0)
                .build();

        // Then
        assertTrue(rule.evaluate(85.0));
        assertFalse(rule.evaluate(80.0));
        assertFalse(rule.evaluate(75.0));
    }

    @Test
    @DisplayName("Should evaluate less than operator correctly")
    void shouldEvaluateLessThanOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.LESS_THAN)
                .threshold(20.0)
                .build();

        // Then
        assertTrue(rule.evaluate(15.0));
        assertFalse(rule.evaluate(20.0));
        assertFalse(rule.evaluate(25.0));
    }

    @Test
    @DisplayName("Should evaluate equal to operator correctly")
    void shouldEvaluateEqualToOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.EQUAL_TO)
                .threshold(100.0)
                .build();

        // Then
        assertTrue(rule.evaluate(100.0));
        assertFalse(rule.evaluate(100.0001));
        assertFalse(rule.evaluate(100.1));
        assertFalse(rule.evaluate(99.0));
    }

    @Test
    @DisplayName("Should evaluate not equal to operator correctly")
    void shouldEvaluateNotEqualToOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.NOT_EQUAL_TO)
                .threshold(100.0)
                .build();

        // Then
        assertFalse(rule.evaluate(100.0));
        assertTrue(rule.evaluate(100.0001));
        assertTrue(rule.evaluate(100.1));
        assertTrue(rule.evaluate(99.0));
    }

    @Test
    @DisplayName("Should evaluate greater than or equal operator correctly")
    void shouldEvaluateGreaterThanOrEqualToOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.GREATER_THAN_OR_EQUAL)
                .threshold(80.0)
                .build();

        // Then
        assertTrue(rule.evaluate(85.0));
        assertTrue(rule.evaluate(80.0));
        assertFalse(rule.evaluate(75.0));
    }

    @Test
    @DisplayName("Should evaluate less than or equal operator correctly")
    void shouldEvaluateLessThanOrEqualToOperatorCorrectly() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.LESS_THAN_OR_EQUAL)
                .threshold(20.0)
                .build();

        // Then
        assertTrue(rule.evaluate(15.0));
        assertTrue(rule.evaluate(20.0));
        assertFalse(rule.evaluate(25.0));
    }

    @Test
    @DisplayName("Should return false when evaluating null value")
    void shouldReturnFalseWhenEvaluatingNullValue() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.GREATER_THAN)
                .threshold(80.0)
                .build();

        // Then
        assertFalse(rule.evaluate(null));
    }

    @Test
    @DisplayName("Should return false when evaluating with null operator")
    void shouldReturnFalseWhenEvaluatingWithNullOperator() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(null)
                .threshold(80.0)
                .build();

        // Then
        assertFalse(rule.evaluate(85.0));
    }

    @Test
    @DisplayName("Should return false when evaluating with null threshold")
    void shouldReturnFalseWhenEvaluatingWithNullThreshold() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.GREATER_THAN)
                .threshold(null)
                .build();

        // Then
        assertFalse(rule.evaluate(85.0));
    }

    @Test
    @DisplayName("Should be applicable for service when service name is null")
    void shouldBeApplicableForServiceWhenServiceNameIsNull() {
        // Given
        AlertRule rule = AlertRule.builder()
                .serviceName(null)
                .build();

        // Then
        assertTrue(rule.isApplicableForService("any-service"));
    }

    @Test
    @DisplayName("Should be applicable for service when service name is blank")
    void shouldBeApplicableForServiceWhenServiceNameIsBlank() {
        // Given
        AlertRule rule = AlertRule.builder()
                .serviceName("   ")
                .build();

        // Then
        assertTrue(rule.isApplicableForService("any-service"));
    }

    @Test
    @DisplayName("Should be applicable for service when service name matches")
    void shouldBeApplicableForServiceWhenServiceNameMatches() {
        // Given
        AlertRule rule = AlertRule.builder()
                .serviceName("order-service")
                .build();

        // Then
        assertTrue(rule.isApplicableForService("order-service"));
    }

    @Test
    @DisplayName("Should not be applicable for service when service name does not match")
    void shouldNotBeApplicableForServiceWhenServiceNameDoesNotMatch() {
        // Given
        AlertRule rule = AlertRule.builder()
                .serviceName("order-service")
                .build();

        // Then
        assertFalse(rule.isApplicableForService("payment-service"));
    }

    @Test
    @DisplayName("Should set default values with no-args constructor")
    void shouldSetDefaultValuesWithNoArgsConstructor() {
        // When
        AlertRule rule = new AlertRule();

        // Then
        assertTrue(rule.getEnabled());
        assertEquals(300, rule.getCooldownSeconds());
    }

    @Test
    @DisplayName("Should create alert rule with all fields using all-args constructor")
    void shouldCreateAlertRuleWithAllFieldsUsingAllArgsConstructor() {
        // Given
        Instant now = Instant.now();

        // When
        AlertRule rule = new AlertRule(
                "rule-123",
                "tenant-1",
                "High CPU",
                "Description",
                true,
                "order-service",
                "cpu.usage",
                AlertRule.ConditionType.THRESHOLD,
                80.0,
                AlertRule.ComparisonOperator.GREATER_THAN,
                300,
                AlertRule.AlertSeverity.HIGH,
                List.of(AlertRule.NotificationChannel.EMAIL),
                List.of("admin@example.com"),
                600,
                "Template",
                Map.of("key", "value"),
                Map.of("tag", "value"),
                now,
                now,
                "user-1",
                "user-2"
        );

        // Then
        assertEquals("rule-123", rule.getId());
        assertTrue(rule.getEnabled());
        assertEquals(600, rule.getCooldownSeconds());
    }

    @ParameterizedTest
    @EnumSource(AlertRule.ConditionType.class)
    @DisplayName("Should accept all condition types")
    void shouldAcceptAllConditionTypes(AlertRule.ConditionType conditionType) {
        // Given
        AlertRule rule = AlertRule.builder()
                .conditionType(conditionType)
                .build();

        // Then
        assertEquals(conditionType, rule.getConditionType());
    }

    @ParameterizedTest
    @EnumSource(AlertRule.AlertSeverity.class)
    @DisplayName("Should accept all severity levels")
    void shouldAcceptAllSeverityLevels(AlertRule.AlertSeverity severity) {
        // Given
        AlertRule rule = AlertRule.builder()
                .severity(severity)
                .build();

        // Then
        assertEquals(severity, rule.getSeverity());
    }

    @ParameterizedTest
    @EnumSource(AlertRule.NotificationChannel.class)
    @DisplayName("Should accept all notification channels")
    void shouldAcceptAllNotificationChannels(AlertRule.NotificationChannel channel) {
        // Given
        AlertRule rule = AlertRule.builder()
                .notificationChannels(List.of(channel))
                .build();

        // Then
        assertEquals(1, rule.getNotificationChannels().size());
        assertEquals(channel, rule.getNotificationChannels().get(0));
    }

    @Test
    @DisplayName("Should handle negative threshold values")
    void shouldHandleNegativeThresholdValues() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.LESS_THAN)
                .threshold(-10.0)
                .build();

        // Then
        assertTrue(rule.evaluate(-15.0));
        assertFalse(rule.evaluate(-5.0));
    }

    @Test
    @DisplayName("Should handle zero threshold values")
    void shouldHandleZeroThresholdValues() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.GREATER_THAN)
                .threshold(0.0)
                .build();

        // Then
        assertTrue(rule.evaluate(0.1));
        assertFalse(rule.evaluate(0.0));
        assertFalse(rule.evaluate(-0.1));
    }

    @Test
    @DisplayName("Should handle very large threshold values")
    void shouldHandleVeryLargeThresholdValues() {
        // Given
        AlertRule rule = AlertRule.builder()
                .operator(AlertRule.ComparisonOperator.LESS_THAN)
                .threshold(Double.MAX_VALUE)
                .build();

        // Then
        assertTrue(rule.evaluate(0.0));
        assertTrue(rule.evaluate(Double.MAX_VALUE / 2.0));
    }

    @Test
    @DisplayName("Should update rule fields")
    void shouldUpdateRuleFields() {
        // Given
        AlertRule rule = AlertRule.builder()
                .name("Old Name")
                .enabled(true)
                .severity(AlertRule.AlertSeverity.LOW)
                .build();

        // When
        rule.setName("New Name");
        rule.setEnabled(false);
        rule.setSeverity(AlertRule.AlertSeverity.CRITICAL);

        // Then
        assertEquals("New Name", rule.getName());
        assertFalse(rule.getEnabled());
        assertEquals(AlertRule.AlertSeverity.CRITICAL, rule.getSeverity());
    }
}
