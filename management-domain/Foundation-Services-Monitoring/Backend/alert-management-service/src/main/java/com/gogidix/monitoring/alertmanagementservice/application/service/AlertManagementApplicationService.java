package com.gogidix.monitoring.alertmanagementservice.application.service;

import com.gogidix.monitoring.alertmanagementservice.application.dto.*;
import com.gogidix.monitoring.alertmanagementservice.application.mapper.AlertMapper;
import com.gogidix.monitoring.alertmanagementservice.domain.model.Alert;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertHistory;
import com.gogidix.monitoring.alertmanagementservice.domain.model.AlertRule;
import com.gogidix.monitoring.alertmanagementservice.domain.port.out.*;
import com.gogidix.monitoring.alertmanagementservice.shared.exception.NotFoundException;
import com.gogidix.monitoring.alertmanagementservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Application service for alert management operations.
 */
@Service
@Transactional(readOnly = true)
public class AlertManagementApplicationService {

    private static final Logger log = LoggerFactory.getLogger(AlertManagementApplicationService.class);

    private final AlertRuleRepositoryPort alertRuleRepository;
    private final AlertRepositoryPort alertRepository;
    private final AlertHistoryRepositoryPort alertHistoryRepository;
    private final AlertNotificationServicePort notificationService;
    private final AlertMapper mapper;

    public AlertManagementApplicationService(
            AlertRuleRepositoryPort alertRuleRepository,
            AlertRepositoryPort alertRepository,
            AlertHistoryRepositoryPort alertHistoryRepository,
            AlertNotificationServicePort notificationService,
            AlertMapper mapper) {
        this.alertRuleRepository = alertRuleRepository;
        this.alertRepository = alertRepository;
        this.alertHistoryRepository = alertHistoryRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    /**
     * Create a new alert rule.
     */
    @Transactional
    @CacheEvict(value = "alertRules", allEntries = true)
    public AlertRuleResponseDto createAlertRule(String tenantId, CreateAlertRuleRequestDto request, String userId) {
        log.info("Creating alert rule: {} for tenant: {}", request.getName(), tenantId);

        AlertRule rule = AlertRule.builder()
                .id(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .enabled(request.getEnabled())
                .serviceName(request.getServiceName())
                .metricName(request.getMetricName())
                .conditionType(parseConditionType(request.getConditionType()))
                .threshold(request.getThreshold())
                .operator(parseOperator(request.getOperator()))
                .durationSeconds(request.getDurationSeconds())
                .severity(parseSeverity(request.getSeverity()))
                .notificationChannels(request.getNotificationChannels() != null
                        ? request.getNotificationChannels().stream()
                        .map(AlertRuleService::parseNotificationChannel)
                        .toList()
                        : null)
                .recipients(request.getRecipients())
                .cooldownSeconds(request.getCooldownSeconds() != null ? request.getCooldownSeconds() : 300)
                .messageTemplate(request.getMessageTemplate())
                .metadata(request.getMetadata())
                .tags(request.getTags())
                .createdAt(Instant.now())
                .createdBy(userId)
                .build();

        AlertRule saved = alertRuleRepository.save(rule);
        log.info("Alert rule created with ID: {}", saved.getId());

        return mapper.toAlertRuleResponseDto(saved);
    }

    /**
     * Get all alert rules for a tenant.
     */
    public List<AlertRuleResponseDto> getAlertRules(String tenantId) {
        return alertRuleRepository.findByTenantId(tenantId).stream()
                .map(mapper::toAlertRuleResponseDto)
                .toList();
    }

    /**
     * Get enabled alert rules for evaluation.
     */
    public List<AlertRule> getEnabledAlertRules(String tenantId) {
        return alertRuleRepository.findEnabledByTenantId(tenantId);
    }

    /**
     * Get an alert rule by ID.
     */
    public AlertRuleResponseDto getAlertRule(String tenantId, String ruleId) {
        return alertRuleRepository.findById(ruleId)
                .filter(rule -> rule.getTenantId().equals(tenantId))
                .map(mapper::toAlertRuleResponseDto)
                .orElseThrow(() -> new NotFoundException("Alert rule not found: " + ruleId));
    }

    /**
     * Delete an alert rule.
     */
    @Transactional
    @CacheEvict(value = "alertRules", allEntries = true)
    public void deleteAlertRule(String tenantId, String ruleId) {
        AlertRule rule = alertRuleRepository.findById(ruleId)
                .filter(r -> r.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("Alert rule not found: " + ruleId));

        alertRuleRepository.deleteById(ruleId);
        log.info("Alert rule deleted: {}", ruleId);
    }

    /**
     * Get alerts for a tenant.
     */
    public Page<AlertResponseDto> getAlerts(
            String tenantId,
            Alert.AlertStatus status,
            AlertRule.AlertSeverity severity,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "triggeredAt"));

        List<Alert> alerts;
        long total;

        if (status != null) {
            alerts = alertRepository.findByTenantIdAndStatus(tenantId, status);
            total = alerts.size();
        } else if (severity != null) {
            alerts = alertRepository.findByTenantIdAndSeverity(tenantId, severity);
            total = alerts.size();
        } else {
            alerts = alertRepository.findByTenantId(tenantId);
            total = alerts.size();
        }

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), alerts.size());
        List<Alert> pagedAlerts = alerts.subList(start, end);

        return new PageImpl<>(
                pagedAlerts.stream().map(mapper::toAlertResponseDto).toList(),
                pageable,
                total
        );
    }

    /**
     * Get an alert by ID.
     */
    public AlertResponseDto getAlert(String tenantId, String alertId) {
        return alertRepository.findById(alertId)
                .filter(alert -> alert.getTenantId().equals(tenantId))
                .map(mapper::toAlertResponseDto)
                .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));
    }

    /**
     * Acknowledge an alert.
     */
    @Transactional
    public AlertResponseDto acknowledgeAlert(String tenantId, String alertId, AcknowledgeAlertRequestDto request) {
        Alert alert = alertRepository.findById(alertId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (alert.isAcknowledged()) {
            throw new ValidationException("Alert is already acknowledged");
        }

        alert.acknowledge(request.getUserId(), request.getComment());
        Alert saved = alertRepository.save(alert);

        // Record history
        recordHistory(saved, AlertHistory.StateChangeType.ACKNOWLEDGED, request.getUserId(), request.getComment());

        log.info("Alert {} acknowledged by user {}", alertId, request.getUserId());

        return mapper.toAlertResponseDto(saved);
    }

    /**
     * Resolve an alert.
     */
    @Transactional
    public AlertResponseDto resolveAlert(String tenantId, String alertId, ResolveAlertRequestDto request) {
        Alert alert = alertRepository.findById(alertId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (alert.isResolved()) {
            throw new ValidationException("Alert is already resolved");
        }

        alert.resolve(request.getUserId(), request.getComment());
        Alert saved = alertRepository.save(alert);

        // Record history
        recordHistory(saved, AlertHistory.StateChangeType.RESOLVED, request.getUserId(), request.getComment());

        log.info("Alert {} resolved by user {}", alertId, request.getUserId());

        return mapper.toAlertResponseDto(saved);
    }

    /**
     * Get alert history.
     */
    public List<com.gogidix.monitoring.alertmanagementservice.application.dto.AlertHistoryResponseDto> getAlertHistory(
            String tenantId,
            String alertId
    ) {
        // Verify alert exists
        alertRepository.findById(alertId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        return alertHistoryRepository.findByAlertId(alertId).stream()
                .map(mapper::toAlertHistoryResponseDto)
                .toList();
    }

    /**
     * Create a new alert from rule evaluation.
     */
    @Transactional
    public Alert createAlertFromRule(
            String tenantId,
            AlertRule rule,
            String serviceName,
            Double triggerValue,
            Map<String, Object> context
    ) {
        String message = generateAlertMessage(rule, serviceName, triggerValue);

        Alert alert = Alert.builder()
                .id(UUID.randomUUID().toString())
                .tenantId(tenantId)
                .ruleId(rule.getId())
                .ruleName(rule.getName())
                .serviceName(serviceName)
                .metricName(rule.getMetricName())
                .severity(rule.getSeverity())
                .status(Alert.AlertStatus.OPEN)
                .message(message)
                .triggerValue(triggerValue)
                .threshold(rule.getThreshold())
                .triggeredAt(Instant.now())
                .notificationStatus(Alert.NotificationStatus.PENDING)
                .notificationAttempts(0)
                .context(context)
                .tags(rule.getTags())
                .createdAt(Instant.now())
                .build();

        Alert saved = alertRepository.save(alert);

        // Record history
        recordHistory(saved, AlertHistory.StateChangeType.CREATED, "system", null);

        // Send notifications
        sendNotifications(saved, rule);

        log.warn("Alert created: {} for service: {} with value: {}", rule.getName(), serviceName, triggerValue);

        return saved;
    }

    private void recordHistory(Alert alert, AlertHistory.StateChangeType changeType, String userId, String comment) {
        AlertHistory history = AlertHistory.builder()
                .id(UUID.randomUUID().toString())
                .alertId(alert.getId())
                .tenantId(alert.getTenantId())
                .stateChangeType(changeType)
                .previousState(alert.getStatus().name())
                .newState(alert.getStatus().name())
                .changedBy(userId)
                .comment(comment)
                .changedAt(Instant.now())
                .build();

        alertHistoryRepository.save(history);
    }

    private void sendNotifications(Alert alert, AlertRule rule) {
        if (rule.getNotificationChannels() == null || rule.getNotificationChannels().isEmpty()) {
            return;
        }

        for (AlertRule.NotificationChannel channel : rule.getNotificationChannels()) {
            notificationService.sendNotification(alert, channel, rule.getRecipients());
        }
    }

    private String generateAlertMessage(AlertRule rule, String serviceName, Double triggerValue) {
        if (rule.getMessageTemplate() != null && !rule.getMessageTemplate().isBlank()) {
            return rule.getMessageTemplate()
                    .replace("{service}", serviceName)
                    .replace("{metric}", rule.getMetricName())
                    .replace("{value}", String.valueOf(triggerValue))
                    .replace("{threshold}", String.valueOf(rule.getThreshold()));
        }

        return String.format("Alert: %s - Service: %s, Metric: %s, Value: %.2f, Threshold: %.2f",
                rule.getName(), serviceName, rule.getMetricName(), triggerValue, rule.getThreshold());
    }

    private AlertRule.ConditionType parseConditionType(String value) {
        if (value == null) return AlertRule.ConditionType.THRESHOLD;
        try {
            return AlertRule.ConditionType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AlertRule.ConditionType.THRESHOLD;
        }
    }

    private AlertRule.ComparisonOperator parseOperator(String value) {
        if (value == null) return AlertRule.ComparisonOperator.GREATER_THAN;
        try {
            return AlertRule.ComparisonOperator.valueOf(value.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return AlertRule.ComparisonOperator.GREATER_THAN;
        }
    }

    private AlertRule.AlertSeverity parseSeverity(String value) {
        if (value == null) return AlertRule.AlertSeverity.MEDIUM;
        try {
            return AlertRule.AlertSeverity.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return AlertRule.AlertSeverity.MEDIUM;
        }
    }
}
