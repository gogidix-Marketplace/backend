package com.gogidix.analytics.metrics.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.in.CreateMetricAlertCommand;
import com.gogidix.analytics.metrics.domain.port.in.IngestMetricCommand;
import com.gogidix.analytics.metrics.domain.port.out.MetricEventPublisher;
import com.gogidix.analytics.metrics.domain.repository.MetricAlertRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricDataPointRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CQRS Command Handler for Metrics operations.
 * Handles all write operations for metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MetricsCommandService {

    private final MetricDataPointRepository metricRepository;
    private final MetricAlertRepository alertRepository;
    private final MetricEventPublisher eventPublisher;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    /**
     * Ingest a metric data point
     */
    @Transactional
    public MetricDataPoint ingestMetric(IngestMetricCommand command) {
        log.debug("Ingesting metric: name={}, value={}", command.getMetricName(), command.getMetricValue());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        MetricDataPoint metric = MetricDataPoint.builder()
            .metricName(command.getMetricName())
            .metricType(command.getMetricType())
            .metricValue(command.getMetricValue())
            .unit(command.getUnit())
            .timestamp(command.getTimestamp() != null ? command.getTimestamp() : LocalDateTime.now())
            .sourceService(command.getSourceService())
            .tags(convertTagsToJson(command.getTags()))
            .dimensions(convertDimensionsToJson(command.getDimensions()))
            .aggregationLevel(command.getAggregationLevel())
            .tenantId(tenantId)
            .build();

        MetricDataPoint savedMetric = metricRepository.save(metric);

        // Publish metric ingested event
        eventPublisher.publishMetricIngested(savedMetric);

        // Async check alerts
        checkAlertsForMetric(savedMetric);

        log.debug("Metric ingested: id={}", savedMetric.getId());
        return savedMetric;
    }

    /**
     * Ingest multiple metric data points in batch
     */
    @Transactional
    public List<MetricDataPoint> ingestMetricsBatch(List<IngestMetricCommand> commands) {
        log.info("Ingesting metrics batch: size={}", commands.size());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        List<MetricDataPoint> metrics = commands.stream()
            .map(cmd -> MetricDataPoint.builder()
                .metricName(cmd.getMetricName())
                .metricType(cmd.getMetricType())
                .metricValue(cmd.getMetricValue())
                .unit(cmd.getUnit())
                .timestamp(cmd.getTimestamp() != null ? cmd.getTimestamp() : LocalDateTime.now())
                .sourceService(cmd.getSourceService())
                .tags(convertTagsToJson(cmd.getTags()))
                .dimensions(convertDimensionsToJson(cmd.getDimensions()))
                .aggregationLevel(cmd.getAggregationLevel())
                .tenantId(tenantId)
                .build())
            .toList();

        List<MetricDataPoint> savedMetrics = metricRepository.saveAll(metrics);

        // Publish events for each metric
        savedMetrics.forEach(eventPublisher::publishMetricIngested);

        log.info("Metrics batch ingested: count={}", savedMetrics.size());
        return savedMetrics;
    }

    /**
     * Create a metric alert
     */
    @Transactional
    public MetricAlert createAlert(CreateMetricAlertCommand command) {
        log.info("Creating metric alert: name={}, metric={}", command.getAlertName(), command.getMetricName());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        MetricAlert alert = MetricAlert.builder()
            .alertName(command.getAlertName())
            .metricName(command.getMetricName())
            .conditionType(command.getConditionType())
            .thresholdValue(command.getThresholdValue())
            .evaluationWindowSeconds(command.getEvaluationWindowSeconds())
            .severity(command.getSeverity())
            .status(MetricAlert.AlertStatus.ACTIVE)
            .enabled(true)
            .notificationChannels(command.getNotificationChannels())
            .description(command.getDescription())
            .tenantId(tenantId)
            .cooldownSeconds(command.getCooldownSeconds() != null ? command.getCooldownSeconds() : 300)
            .createdBy(command.getCreatedBy())
            .build();

        MetricAlert savedAlert = alertRepository.save(alert);

        auditService.logEvent(
            "METRIC_ALERT_CREATED",
            "MetricAlert",
            savedAlert.getId(),
            "Created metric alert: " + savedAlert.getAlertName()
        );

        log.info("Metric alert created: id={}", savedAlert.getId());
        return savedAlert;
    }

    /**
     * Update a metric alert
     */
    @Transactional
    public MetricAlert updateAlert(String alertId, CreateMetricAlertCommand command) {
        log.info("Updating metric alert: id={}", alertId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        MetricAlert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (!alert.getTenantId().equals(tenantId)) {
            throw new com.gogidix.shared.exceptions.ValidationException("Access denied: Alert belongs to different tenant");
        }

        alert.setAlertName(command.getAlertName());
        alert.setMetricName(command.getMetricName());
        alert.setConditionType(command.getConditionType());
        alert.setThresholdValue(command.getThresholdValue());
        alert.setEvaluationWindowSeconds(command.getEvaluationWindowSeconds());
        alert.setSeverity(command.getSeverity());
        alert.setNotificationChannels(command.getNotificationChannels());
        alert.setDescription(command.getDescription());
        if (command.getCooldownSeconds() != null) {
            alert.setCooldownSeconds(command.getCooldownSeconds());
        }

        MetricAlert savedAlert = alertRepository.save(alert);

        auditService.logEvent(
            "METRIC_ALERT_UPDATED",
            "MetricAlert",
            savedAlert.getId(),
            "Updated metric alert: " + savedAlert.getAlertName()
        );

        log.info("Metric alert updated: id={}", savedAlert.getId());
        return savedAlert;
    }

    /**
     * Delete a metric alert
     */
    @Transactional
    public void deleteAlert(String alertId) {
        log.info("Deleting metric alert: id={}", alertId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        MetricAlert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (!alert.getTenantId().equals(tenantId)) {
            throw new com.gogidix.shared.exceptions.ValidationException("Access denied: Alert belongs to different tenant");
        }

        alertRepository.delete(alert);

        auditService.logEvent(
            "METRIC_ALERT_DELETED",
            "MetricAlert",
            alertId,
            "Deleted metric alert: " + alert.getAlertName()
        );

        log.info("Metric alert deleted: id={}", alertId);
    }

    /**
     * Enable/disable a metric alert
     */
    @Transactional
    public MetricAlert toggleAlert(String alertId, boolean enabled) {
        log.info("{} metric alert: id={}", enabled ? "Enabling" : "Disabling", alertId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found in request context");
        }

        MetricAlert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new NotFoundException("Alert not found: " + alertId));

        if (!alert.getTenantId().equals(tenantId)) {
            throw new com.gogidix.shared.exceptions.ValidationException("Access denied: Alert belongs to different tenant");
        }

        alert.setEnabled(enabled);
        alert.setStatus(enabled ? MetricAlert.AlertStatus.ACTIVE : MetricAlert.AlertStatus.PAUSED);

        MetricAlert savedAlert = alertRepository.save(alert);

        auditService.logEvent(
            "METRIC_ALERT_TOGGLED",
            "MetricAlert",
            alertId,
            (enabled ? "Enabled" : "Disabled") + " metric alert: " + alert.getAlertName()
        );

        log.info("Metric alert {}: id={}", enabled ? "enabled" : "disabled", alertId);
        return savedAlert;
    }

    /**
     * Check alerts for a metric asynchronously
     */
    @Async
    protected void checkAlertsForMetric(MetricDataPoint metric) {
        List<MetricAlert> alerts = alertRepository.findByTenantIdAndMetricName(
            metric.getTenantId(), metric.getMetricName());

        for (MetricAlert alert : alerts) {
            if (!alert.getEnabled() || alert.isCooldownPeriod()) {
                continue;
            }

            boolean triggered = evaluateAlert(alert, metric.getMetricValue());
            if (triggered) {
                alert.markAsTriggered();
                alert.setStatus(MetricAlert.AlertStatus.TRIGGERED);
                alertRepository.save(alert);

                eventPublisher.publishAlertTriggered(alert,
                    "Alert triggered for metric: " + metric.getMetricName() +
                    " with value: " + metric.getMetricValue());

                log.warn("Alert triggered: alertId={}, metricName={}, value={}",
                    alert.getId(), metric.getMetricName(), metric.getMetricValue());
            }
        }
    }

    /**
     * Evaluate if an alert should be triggered
     */
    private boolean evaluateAlert(MetricAlert alert, BigDecimal value) {
        if (alert.getThresholdValue() == null) {
            return false;
        }

        int comparison = value.compareTo(alert.getThresholdValue());

        return switch (alert.getConditionType()) {
            case GREATER_THAN -> comparison > 0;
            case LESS_THAN -> comparison < 0;
            case EQUALS -> comparison == 0;
            case NOT_EQUALS -> comparison != 0;
            case GREATER_THAN_OR_EQUAL -> comparison >= 0;
            case LESS_THAN_OR_EQUAL -> comparison <= 0;
            default -> false;
        };
    }

    private String convertTagsToJson(java.util.Map<String, String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(tags);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert tags to JSON", e);
            return null;
        }
    }

    private String convertDimensionsToJson(java.util.Map<String, Object> dimensions) {
        if (dimensions == null || dimensions.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(dimensions);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert dimensions to JSON", e);
            return null;
        }
    }
}
