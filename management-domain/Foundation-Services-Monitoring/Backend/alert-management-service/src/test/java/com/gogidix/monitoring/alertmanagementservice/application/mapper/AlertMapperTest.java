package com.gogidix.monitoring.alertmanagementservice.application.mapper;

import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertHistoryResponseDto;
import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertResponseDto;
import com.gogidix.monitoring.alertmanagementservice.application.dto.AlertRuleResponseDto;
import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertHistory;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlertMapper Tests")
class AlertMapperTest {

    private AlertMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AlertMapper();
    }

    @Test
    @DisplayName("Should map AlertRule domain to AlertRuleResponseDto")
    void shouldMapAlertRuleDomainToResponseDto() {
        // Given
        Instant now = Instant.now();
        AlertRule domain = AlertRule.builder()
                .id("rule-123")
                .tenantId("tenant-1")
                .name("High CPU Usage")
                .description("Alert when CPU exceeds 80%")
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
                .messageTemplate("CPU is {value}%")
                .metadata(Map.of("team", "platform"))
                .tags(Map.of("env", "prod"))
                .createdAt(now)
                .updatedAt(now)
                .createdBy("user-1")
                .updatedBy("user-2")
                .build();

        // When
        AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domain);

        // Then
        assertEquals("rule-123", dto.getId());
        assertEquals("tenant-1", dto.getTenantId());
        assertEquals("High CPU Usage", dto.getName());
        assertEquals("Alert when CPU exceeds 80%", dto.getDescription());
        assertTrue(dto.getEnabled());
        assertEquals("order-service", dto.getServiceName());
        assertEquals("cpu.usage", dto.getMetricName());
        assertEquals("THRESHOLD", dto.getConditionType());
        assertEquals(80.0, dto.getThreshold());
        assertEquals("GREATER_THAN", dto.getOperator());
        assertEquals(300, dto.getDurationSeconds());
        assertEquals("HIGH", dto.getSeverity());
        assertEquals(2, dto.getNotificationChannels().size());
        assertTrue(dto.getNotificationChannels().contains("EMAIL"));
        assertTrue(dto.getNotificationChannels().contains("SLACK"));
        assertEquals(2, dto.getRecipients().size());
        assertEquals(600, dto.getCooldownSeconds());
        assertEquals("CPU is {value}%", dto.getMessageTemplate());
        assertEquals(Map.of("team", "platform"), dto.getMetadata());
        assertEquals(Map.of("env", "prod"), dto.getTags());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
        assertEquals("user-1", dto.getCreatedBy());
        assertEquals("user-2", dto.getUpdatedBy());
    }

    @Test
    @DisplayName("Should map AlertRule domain with null values")
    void shouldMapAlertRuleDomainWithNullValues() {
        // Given
        AlertRule domain = AlertRule.builder()
                .id("rule-123")
                .name("Test Rule")
                .build();

        // When
        AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domain);

        // Then
        assertEquals("rule-123", dto.getId());
        assertEquals("Test Rule", dto.getName());
        assertNull(dto.getConditionType());
        assertNull(dto.getOperator());
        assertNull(dto.getSeverity());
        assertNull(dto.getNotificationChannels());
        assertNull(dto.getRecipients());
    }

    @Test
    @DisplayName("Should map Alert domain to AlertResponseDto")
    void shouldMapAlertDomainToResponseDto() {
        // Given
        Instant now = Instant.now();
        Alert domain = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .ruleId("rule-456")
                .ruleName("High CPU")
                .serviceName("order-service")
                .metricName("cpu.usage")
                .severity(AlertRule.AlertSeverity.HIGH)
                .status(Alert.AlertStatus.OPEN)
                .message("CPU usage is high")
                .triggerValue(85.5)
                .threshold(80.0)
                .triggeredAt(now)
                .acknowledgedAt(now)
                .acknowledgedBy("user-1")
                .acknowledgmentComment("Looking into it")
                .resolvedAt(now)
                .resolvedBy("user-2")
                .resolutionComment("Fixed")
                .notificationStatus(Alert.NotificationStatus.SENT)
                .notificationAttempts(2)
                .context(Map.of("host", "server-1"))
                .tags(Map.of("env", "prod"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        AlertResponseDto dto = mapper.toAlertResponseDto(domain);

        // Then
        assertEquals("alert-123", dto.getId());
        assertEquals("tenant-1", dto.getTenantId());
        assertEquals("rule-456", dto.getRuleId());
        assertEquals("High CPU", dto.getRuleName());
        assertEquals("order-service", dto.getServiceName());
        assertEquals("cpu.usage", dto.getMetricName());
        assertEquals("HIGH", dto.getSeverity());
        assertEquals("OPEN", dto.getStatus());
        assertEquals("CPU usage is high", dto.getMessage());
        assertEquals(85.5, dto.getTriggerValue());
        assertEquals(80.0, dto.getThreshold());
        assertEquals(now, dto.getTriggeredAt());
        assertEquals(now, dto.getAcknowledgedAt());
        assertEquals("user-1", dto.getAcknowledgedBy());
        assertEquals("Looking into it", dto.getAcknowledgmentComment());
        assertEquals(now, dto.getResolvedAt());
        assertEquals("user-2", dto.getResolvedBy());
        assertEquals("Fixed", dto.getResolutionComment());
        assertEquals("SENT", dto.getNotificationStatus());
        assertEquals(2, dto.getNotificationAttempts());
        assertEquals(Map.of("host", "server-1"), dto.getContext());
        assertEquals(Map.of("env", "prod"), dto.getTags());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Should map Alert domain with null severity and status")
    void shouldMapAlertDomainWithNullSeverityAndStatus() {
        // Given
        Alert domain = Alert.builder()
                .id("alert-123")
                .severity(null)
                .status(null)
                .notificationStatus(null)
                .build();

        // When
        AlertResponseDto dto = mapper.toAlertResponseDto(domain);

        // Then
        assertEquals("alert-123", dto.getId());
        assertNull(dto.getSeverity());
        assertNull(dto.getStatus());
        assertNull(dto.getNotificationStatus());
    }

    @Test
    @DisplayName("Should map AlertHistory domain to AlertHistoryResponseDto")
    void shouldMapAlertHistoryDomainToResponseDto() {
        // Given
        Instant now = Instant.now();
        AlertHistory domain = AlertHistory.builder()
                .id("history-123")
                .alertId("alert-456")
                .tenantId("tenant-1")
                .stateChangeType(AlertHistory.StateChangeType.ACKNOWLEDGED)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.ACKNOWLEDGED.name())
                .changedBy("user-1")
                .comment("Investigating")
                .context(Map.of("source", "api"))
                .changedAt(now)
                .build();

        // When
        AlertHistoryResponseDto dto = mapper.toAlertHistoryResponseDto(domain);

        // Then
        assertEquals("history-123", dto.getId());
        assertEquals("alert-456", dto.getAlertId());
        assertEquals("tenant-1", dto.getTenantId());
        assertEquals("ACKNOWLEDGED", dto.getStateChangeType());
        assertEquals("OPEN", dto.getPreviousState());
        assertEquals("ACKNOWLEDGED", dto.getNewState());
        assertEquals("user-1", dto.getChangedBy());
        assertEquals("Investigating", dto.getComment());
        assertEquals(Map.of("source", "api"), dto.getContext());
        assertEquals(now, dto.getChangedAt());
    }

    @Test
    @DisplayName("Should map AlertHistory domain with null state change type")
    void shouldMapAlertHistoryDomainWithNullStateChangeType() {
        // Given
        AlertHistory domain = AlertHistory.builder()
                .id("history-123")
                .stateChangeType(null)
                .build();

        // When
        AlertHistoryResponseDto dto = mapper.toAlertHistoryResponseDto(domain);

        // Then
        assertEquals("history-123", dto.getId());
        assertNull(dto.getStateChangeType());
    }

    @Test
    @DisplayName("Should map all alert severities correctly")
    void shouldMapAllAlertSeveritiesCorrectly() {
        // Given
        AlertRule[] domains = {
                AlertRule.builder().severity(AlertRule.AlertSeverity.CRITICAL).build(),
                AlertRule.builder().severity(AlertRule.AlertSeverity.HIGH).build(),
                AlertRule.builder().severity(AlertRule.AlertSeverity.MEDIUM).build(),
                AlertRule.builder().severity(AlertRule.AlertSeverity.LOW).build(),
                AlertRule.builder().severity(AlertRule.AlertSeverity.INFO).build()
        };

        String[] expected = {"CRITICAL", "HIGH", "MEDIUM", "LOW", "INFO"};

        // When & Then
        for (int i = 0; i < domains.length; i++) {
            AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domains[i]);
            assertEquals(expected[i], dto.getSeverity());
        }
    }

    @Test
    @DisplayName("Should map all alert statuses correctly")
    void shouldMapAllAlertStatusesCorrectly() {
        // Given
        Alert[] domains = {
                Alert.builder().status(Alert.AlertStatus.OPEN).build(),
                Alert.builder().status(Alert.AlertStatus.ACKNOWLEDGED).build(),
                Alert.builder().status(Alert.AlertStatus.RESOLVED).build(),
                Alert.builder().status(Alert.AlertStatus.SUPPRESSED).build(),
                Alert.builder().status(Alert.AlertStatus.CLOSED).build()
        };

        String[] expected = {"OPEN", "ACKNOWLEDGED", "RESOLVED", "SUPPRESSED", "CLOSED"};

        // When & Then
        for (int i = 0; i < domains.length; i++) {
            AlertResponseDto dto = mapper.toAlertResponseDto(domains[i]);
            assertEquals(expected[i], dto.getStatus());
        }
    }

    @Test
    @DisplayName("Should map all condition types correctly")
    void shouldMapAllConditionTypesCorrectly() {
        // Given
        AlertRule[] domains = {
                AlertRule.builder().conditionType(AlertRule.ConditionType.THRESHOLD).build(),
                AlertRule.builder().conditionType(AlertRule.ConditionType.ANOMALY_DETECTION).build(),
                AlertRule.builder().conditionType(AlertRule.ConditionType.RATE_OF_CHANGE).build(),
                AlertRule.builder().conditionType(AlertRule.ConditionType.MISSING_DATA).build(),
                AlertRule.builder().conditionType(AlertRule.ConditionType.PREDICTIVE).build()
        };

        String[] expected = {"THRESHOLD", "ANOMALY_DETECTION", "RATE_OF_CHANGE", "MISSING_DATA", "PREDICTIVE"};

        // When & Then
        for (int i = 0; i < domains.length; i++) {
            AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domains[i]);
            assertEquals(expected[i], dto.getConditionType());
        }
    }

    @Test
    @DisplayName("Should map all comparison operators correctly")
    void shouldMapAllComparisonOperatorsCorrectly() {
        // Given
        AlertRule[] domains = {
                AlertRule.builder().operator(AlertRule.ComparisonOperator.GREATER_THAN).build(),
                AlertRule.builder().operator(AlertRule.ComparisonOperator.LESS_THAN).build(),
                AlertRule.builder().operator(AlertRule.ComparisonOperator.EQUAL_TO).build(),
                AlertRule.builder().operator(AlertRule.ComparisonOperator.NOT_EQUAL_TO).build(),
                AlertRule.builder().operator(AlertRule.ComparisonOperator.GREATER_THAN_OR_EQUAL).build(),
                AlertRule.builder().operator(AlertRule.ComparisonOperator.LESS_THAN_OR_EQUAL).build()
        };

        String[] expected = {"GREATER_THAN", "LESS_THAN", "EQUAL_TO", "NOT_EQUAL_TO",
                             "GREATER_THAN_OR_EQUAL", "LESS_THAN_OR_EQUAL"};

        // When & Then
        for (int i = 0; i < domains.length; i++) {
            AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domains[i]);
            assertEquals(expected[i], dto.getOperator());
        }
    }

    @Test
    @DisplayName("Should map all notification channels correctly")
    void shouldMapAllNotificationChannelsCorrectly() {
        // Given
        AlertRule domain = AlertRule.builder()
                .notificationChannels(List.of(
                        AlertRule.NotificationChannel.EMAIL,
                        AlertRule.NotificationChannel.SMS,
                        AlertRule.NotificationChannel.WEBHOOK,
                        AlertRule.NotificationChannel.SLACK,
                        AlertRule.NotificationChannel.PAGERDUTY,
                        AlertRule.NotificationChannel.INCIDENT_MANAGEMENT
                ))
                .build();

        // When
        AlertRuleResponseDto dto = mapper.toAlertRuleResponseDto(domain);

        // Then
        assertEquals(6, dto.getNotificationChannels().size());
        assertTrue(dto.getNotificationChannels().contains("EMAIL"));
        assertTrue(dto.getNotificationChannels().contains("SMS"));
        assertTrue(dto.getNotificationChannels().contains("WEBHOOK"));
        assertTrue(dto.getNotificationChannels().contains("SLACK"));
        assertTrue(dto.getNotificationChannels().contains("PAGERDUTY"));
        assertTrue(dto.getNotificationChannels().contains("INCIDENT_MANAGEMENT"));
    }
}
