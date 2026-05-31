package com.gogidix.monitoring.alertmanagementservice.application.service;

import com.gogidix.monitoring.alertmanagementservice.application.dto.*;
import com.gogidix.monitoring.alertmanagementservice.application.mapper.AlertMapper;
import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertHistory;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import com.gogidix.monitoring.alertmanagementservice.domain.port.out.*;
import com.gogidix.monitoring.alertmanagementservice.shared.exception.NotFoundException;
import com.gogidix.monitoring.alertmanagementservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertManagementApplicationService Tests")
class AlertManagementApplicationServiceTest {

    @Mock
    private AlertRuleRepositoryPort alertRuleRepository;

    @Mock
    private AlertRepositoryPort alertRepository;

    @Mock
    private AlertHistoryRepositoryPort alertHistoryRepository;

    @Mock
    private AlertNotificationServicePort notificationService;

    @Mock
    private AlertMapper mapper;

    private AlertManagementApplicationService service;

    private AlertRuleResponseDto alertRuleResponseDto;
    private AlertResponseDto alertResponseDto;
    private AlertHistoryResponseDto alertHistoryResponseDto;

    @BeforeEach
    void setUp() {
        service = new AlertManagementApplicationService(
                alertRuleRepository,
                alertRepository,
                alertHistoryRepository,
                notificationService,
                mapper
        );

        // Setup common DTOs
        alertRuleResponseDto = AlertRuleResponseDto.builder()
                .id("rule-1")
                .name("High CPU")
                .build();

        alertResponseDto = AlertResponseDto.builder()
                .id("alert-1")
                .status("OPEN")
                .build();

        alertHistoryResponseDto = AlertHistoryResponseDto.builder()
                .id("history-1")
                .stateChangeType("CREATED")
                .build();
    }

    @Test
    @DisplayName("Should create alert rule successfully")
    void shouldCreateAlertRuleSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        CreateAlertRuleRequestDto request = CreateAlertRuleRequestDto.builder()
                .name("High CPU Usage")
                .description("Alert when CPU exceeds 80%")
                .metricName("cpu.usage")
                .conditionType("THRESHOLD")
                .threshold(80.0)
                .operator("GREATER_THAN")
                .severity("HIGH")
                .durationSeconds(300)
                .cooldownSeconds(600)
                .notificationChannels(List.of("EMAIL"))
                .recipients(List.of("admin@example.com"))
                .build();

        AlertRule savedRule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU Usage")
                .build();

        when(alertRuleRepository.save(any(AlertRule.class))).thenReturn(savedRule);
        when(mapper.toAlertRuleResponseDto(savedRule)).thenReturn(alertRuleResponseDto);

        // When
        AlertRuleResponseDto result = service.createAlertRule(tenantId, request, "user-1");

        // Then
        assertNotNull(result);
        assertEquals("rule-1", result.getId());
        verify(alertRuleRepository).save(any(AlertRule.class));
        verify(mapper).toAlertRuleResponseDto(savedRule);
    }

    @Test
    @DisplayName("Should get all alert rules for tenant")
    void shouldGetAllAlertRulesForTenant() {
        // Given
        String tenantId = "tenant-1";
        List<AlertRule> rules = List.of(
                AlertRule.builder().id("rule-1").name("Rule 1").build(),
                AlertRule.builder().id("rule-2").name("Rule 2").build()
        );

        when(alertRuleRepository.findByTenantId(tenantId)).thenReturn(rules);
        when(mapper.toAlertRuleResponseDto(any())).thenReturn(
                AlertRuleResponseDto.builder().id("rule-1").name("Rule 1").build(),
                AlertRuleResponseDto.builder().id("rule-2").name("Rule 2").build()
        );

        // When
        List<AlertRuleResponseDto> result = service.getAlertRules(tenantId);

        // Then
        assertEquals(2, result.size());
        verify(alertRuleRepository).findByTenantId(tenantId);
        verify(mapper, times(2)).toAlertRuleResponseDto(any());
    }

    @Test
    @DisplayName("Should get enabled alert rules")
    void shouldGetEnabledAlertRules() {
        // Given
        String tenantId = "tenant-1";
        List<AlertRule> rules = List.of(
                AlertRule.builder().id("rule-1").enabled(true).build()
        );

        when(alertRuleRepository.findEnabledByTenantId(tenantId)).thenReturn(rules);

        // When
        List<AlertRule> result = service.getEnabledAlertRules(tenantId);

        // Then
        assertEquals(1, result.size());
        verify(alertRuleRepository).findEnabledByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should get alert rule by ID when exists")
    void shouldGetAlertRuleByIdWhenExists() {
        // Given
        String tenantId = "tenant-1";
        String ruleId = "rule-1";
        AlertRule rule = AlertRule.builder()
                .id(ruleId)
                .tenantId(tenantId)
                .name("Rule 1")
                .build();

        when(alertRuleRepository.findById(ruleId)).thenReturn(Optional.of(rule));
        when(mapper.toAlertRuleResponseDto(rule)).thenReturn(alertRuleResponseDto);

        // When
        AlertRuleResponseDto result = service.getAlertRule(tenantId, ruleId);

        // Then
        assertNotNull(result);
        verify(alertRuleRepository).findById(ruleId);
        verify(mapper).toAlertRuleResponseDto(rule);
    }

    @Test
    @DisplayName("Should throw NotFoundException when alert rule does not exist")
    void shouldThrowNotFoundExceptionWhenAlertRuleDoesNotExist() {
        // Given
        String tenantId = "tenant-1";
        String ruleId = "non-existent";

        when(alertRuleRepository.findById(ruleId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getAlertRule(tenantId, ruleId));
        verify(alertRuleRepository).findById(ruleId);
        verify(mapper, never()).toAlertRuleResponseDto(any());
    }

    @Test
    @DisplayName("Should throw NotFoundException when alert rule belongs to different tenant")
    void shouldThrowNotFoundExceptionWhenAlertRuleBelongsToDifferentTenant() {
        // Given
        String tenantId = "tenant-1";
        String ruleId = "rule-1";
        AlertRule rule = AlertRule.builder()
                .id(ruleId)
                .tenantId("tenant-2")
                .build();

        when(alertRuleRepository.findById(ruleId)).thenReturn(Optional.of(rule));

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getAlertRule(tenantId, ruleId));
    }

    @Test
    @DisplayName("Should delete alert rule successfully")
    void shouldDeleteAlertRuleSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String ruleId = "rule-1";
        AlertRule rule = AlertRule.builder()
                .id(ruleId)
                .tenantId(tenantId)
                .build();

        when(alertRuleRepository.findById(ruleId)).thenReturn(Optional.of(rule));
        doNothing().when(alertRuleRepository).deleteById(ruleId);

        // When
        service.deleteAlertRule(tenantId, ruleId);

        // Then
        verify(alertRuleRepository).findById(ruleId);
        verify(alertRuleRepository).deleteById(ruleId);
    }

    @Test
    @DisplayName("Should get alerts with pagination")
    void shouldGetAlertsWithPagination() {
        // Given
        String tenantId = "tenant-1";
        List<Alert> alerts = List.of(
                Alert.builder().id("alert-1").build(),
                Alert.builder().id("alert-2").build(),
                Alert.builder().id("alert-3").build()
        );

        when(alertRepository.findByTenantId(tenantId)).thenReturn(alerts);
        when(mapper.toAlertResponseDto(any())).thenReturn(alertResponseDto);

        // When
        var result = service.getAlerts(tenantId, null, null, 0, 2);

        // Then
        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        verify(alertRepository).findByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should get alerts filtered by status")
    void shouldGetAlertsFilteredByStatus() {
        // Given
        String tenantId = "tenant-1";
        List<Alert> alerts = List.of(
                Alert.builder().id("alert-1").status(Alert.AlertStatus.OPEN).build()
        );

        when(alertRepository.findByTenantIdAndStatus(tenantId, Alert.AlertStatus.OPEN))
                .thenReturn(alerts);
        when(mapper.toAlertResponseDto(any())).thenReturn(alertResponseDto);

        // When
        var result = service.getAlerts(tenantId, Alert.AlertStatus.OPEN, null, 0, 10);

        // Then
        assertEquals(1, result.getContent().size());
        verify(alertRepository).findByTenantIdAndStatus(tenantId, Alert.AlertStatus.OPEN);
    }

    @Test
    @DisplayName("Should get alert by ID when exists")
    void shouldGetAlertByIdWhenExists() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .build();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(mapper.toAlertResponseDto(alert)).thenReturn(alertResponseDto);

        // When
        AlertResponseDto result = service.getAlert(tenantId, alertId);

        // Then
        assertNotNull(result);
        verify(alertRepository).findById(alertId);
        verify(mapper).toAlertResponseDto(alert);
    }

    @Test
    @DisplayName("Should throw NotFoundException when alert does not exist")
    void shouldThrowNotFoundExceptionWhenAlertDoesNotExist() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "non-existent";

        when(alertRepository.findById(alertId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> service.getAlert(tenantId, alertId));
    }

    @Test
    @DisplayName("Should acknowledge alert successfully")
    void shouldAcknowledgeAlertSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .status(Alert.AlertStatus.OPEN)
                .build();

        AcknowledgeAlertRequestDto request = AcknowledgeAlertRequestDto.builder()
                .userId("user-1")
                .comment("Looking into it")
                .build();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any())).thenReturn(alert);
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());
        when(mapper.toAlertResponseDto(alert)).thenReturn(alertResponseDto);

        // When
        AlertResponseDto result = service.acknowledgeAlert(tenantId, alertId, request);

        // Then
        assertNotNull(result);
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED, alert.getStatus());
        assertEquals("user-1", alert.getAcknowledgedBy());
        assertEquals("Looking into it", alert.getAcknowledgmentComment());
        verify(alertRepository).save(alert);
        verify(alertHistoryRepository).save(any(AlertHistory.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when acknowledging already acknowledged alert")
    void shouldThrowValidationExceptionWhenAcknowledgingAlreadyAcknowledgedAlert() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .status(Alert.AlertStatus.ACKNOWLEDGED)
                .build();

        AcknowledgeAlertRequestDto request = AcknowledgeAlertRequestDto.builder()
                .userId("user-1")
                .build();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));

        // When & Then
        assertThrows(ValidationException.class, () -> service.acknowledgeAlert(tenantId, alertId, request));
        verify(alertRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should resolve alert successfully")
    void shouldResolveAlertSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .status(Alert.AlertStatus.ACKNOWLEDGED)
                .build();

        ResolveAlertRequestDto request = ResolveAlertRequestDto.builder()
                .userId("user-1")
                .comment("Fixed the issue")
                .build();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(alertRepository.save(any())).thenReturn(alert);
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());
        when(mapper.toAlertResponseDto(alert)).thenReturn(alertResponseDto);

        // When
        AlertResponseDto result = service.resolveAlert(tenantId, alertId, request);

        // Then
        assertNotNull(result);
        assertEquals(Alert.AlertStatus.RESOLVED, alert.getStatus());
        assertEquals("user-1", alert.getResolvedBy());
        assertEquals("Fixed the issue", alert.getResolutionComment());
        verify(alertRepository).save(alert);
        verify(alertHistoryRepository).save(any(AlertHistory.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when resolving already resolved alert")
    void shouldThrowValidationExceptionWhenResolvingAlreadyResolvedAlert() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .status(Alert.AlertStatus.RESOLVED)
                .build();

        ResolveAlertRequestDto request = ResolveAlertRequestDto.builder()
                .userId("user-1")
                .build();

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));

        // When & Then
        assertThrows(ValidationException.class, () -> service.resolveAlert(tenantId, alertId, request));
        verify(alertRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get alert history")
    void shouldGetAlertHistory() {
        // Given
        String tenantId = "tenant-1";
        String alertId = "alert-1";
        Alert alert = Alert.builder()
                .id(alertId)
                .tenantId(tenantId)
                .build();

        List<AlertHistory> historyList = List.of(
                AlertHistory.builder().id("history-1").build(),
                AlertHistory.builder().id("history-2").build()
        );

        when(alertRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(alertHistoryRepository.findByAlertId(alertId)).thenReturn(historyList);
        when(mapper.toAlertHistoryResponseDto(any())).thenReturn(alertHistoryResponseDto);

        // When
        List<AlertHistoryResponseDto> result = service.getAlertHistory(tenantId, alertId);

        // Then
        assertEquals(2, result.size());
        verify(alertRepository).findById(alertId);
        verify(alertHistoryRepository).findByAlertId(alertId);
    }

    @Test
    @DisplayName("Should create alert from rule successfully")
    void shouldCreateAlertFromRuleSuccessfully() {
        // Given
        String tenantId = "tenant-1";
        AlertRule rule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU")
                .metricName("cpu.usage")
                .severity(AlertRule.AlertSeverity.HIGH)
                .threshold(80.0)
                .build();

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());

        // When
        Alert result = service.createAlertFromRule(
                tenantId,
                rule,
                "order-service",
                85.5,
                Map.of("host", "server-1")
        );

        // Then
        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("rule-1", result.getRuleId());
        assertEquals("High CPU", result.getRuleName());
        assertEquals("order-service", result.getServiceName());
        assertEquals("cpu.usage", result.getMetricName());
        assertEquals(AlertRule.AlertSeverity.HIGH, result.getSeverity());
        assertEquals(Alert.AlertStatus.OPEN, result.getStatus());
        assertEquals(85.5, result.getTriggerValue());
        assertEquals(80.0, result.getThreshold());
        verify(alertRepository).save(any(Alert.class));
        verify(alertHistoryRepository).save(any(AlertHistory.class));
    }

    @Test
    @DisplayName("Should send notifications when creating alert from rule with notification channels")
    void shouldSendNotificationsWhenCreatingAlertFromRuleWithNotificationChannels() {
        // Given
        String tenantId = "tenant-1";
        AlertRule rule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU")
                .metricName("cpu.usage")
                .severity(AlertRule.AlertSeverity.HIGH)
                .threshold(80.0)
                .notificationChannels(List.of(
                        AlertRule.NotificationChannel.EMAIL,
                        AlertRule.NotificationChannel.SLACK
                ))
                .recipients(List.of("admin@example.com", "#alerts"))
                .build();

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());
        doNothing().when(notificationService).sendNotification(any(), any(), any());

        // When
        service.createAlertFromRule(tenantId, rule, "order-service", 85.5, Map.of());

        // Then
        verify(notificationService, times(2)).sendNotification(any(), any(), any());
    }

    @Test
    @DisplayName("Should not send notifications when creating alert from rule without notification channels")
    void shouldNotSendNotificationsWhenCreatingAlertFromRuleWithoutNotificationChannels() {
        // Given
        String tenantId = "tenant-1";
        AlertRule rule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU")
                .metricName("cpu.usage")
                .threshold(80.0)
                .build();

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());

        // When
        service.createAlertFromRule(tenantId, rule, "order-service", 85.5, Map.of());

        // Then
        verify(notificationService, never()).sendNotification(any(), any(), any());
    }

    @Test
    @DisplayName("Should use custom message template when creating alert")
    void shouldUseCustomMessageTemplateWhenCreatingAlert() {
        // Given
        String tenantId = "tenant-1";
        AlertRule rule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU")
                .metricName("cpu.usage")
                .threshold(80.0)
                .messageTemplate("Service {service} has metric {metric} at value {value} exceeding threshold {threshold}")
                .build();

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());

        // When
        Alert result = service.createAlertFromRule(tenantId, rule, "order-service", 85.5, Map.of());

        // Then
        assertTrue(result.getMessage().contains("order-service"));
        assertTrue(result.getMessage().contains("cpu.usage"));
        assertTrue(result.getMessage().contains("85.5"));
        assertTrue(result.getMessage().contains("80.0"));
    }

    @Test
    @DisplayName("Should use default message when no template provided")
    void shouldUseDefaultMessageWhenNoTemplateProvided() {
        // Given
        String tenantId = "tenant-1";
        AlertRule rule = AlertRule.builder()
                .id("rule-1")
                .name("High CPU")
                .metricName("cpu.usage")
                .threshold(80.0)
                .build();

        when(alertRepository.save(any(Alert.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(alertHistoryRepository.save(any())).thenReturn(new AlertHistory());

        // When
        Alert result = service.createAlertFromRule(tenantId, rule, "order-service", 85.5, Map.of());

        // Then
        assertNotNull(result.getMessage());
        assertTrue(result.getMessage().contains("High CPU"));
        assertTrue(result.getMessage().contains("order-service"));
    }
}
