package com.gogidix.transaction.monitoring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.monitoring.domain.entity.Alert;
import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics;
import com.gogidix.transaction.monitoring.domain.repository.AlertRepository;
import com.gogidix.transaction.monitoring.domain.repository.TransactionMetricsRepository;
import com.gogidix.transaction.monitoring.dto.*;
import com.gogidix.transaction.monitoring.mapper.MonitoringMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionMonitoringService {

    private final TransactionMetricsRepository metricsRepository;
    private final AlertRepository alertRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public TransactionMetricsResponse recordMetric(MetricCreateRequest request) {
        log.debug("Recording metric: {} for transaction: {}", request.getMetricName(), request.getTransactionId());

        TransactionMetrics metric = MonitoringMapper.toMetricEntity(request);

        // Evaluate thresholds
        evaluateThresholds(metric);

        TransactionMetrics saved = metricsRepository.save(metric);
        return MonitoringMapper.toMetricResponse(saved);
    }

    @Transactional
    public AlertResponse createAlert(AlertCreateRequest request) {
        log.info("Creating alert: {} for transaction: {}", request.getTitle(), request.getTransactionId());

        Alert alert = MonitoringMapper.toAlertEntity(request);
        Alert saved = alertRepository.save(alert);

        // Publish alert event
        publishAlertEvent(saved);

        return MonitoringMapper.toAlertResponse(saved);
    }

    @Transactional
    public AlertResponse acknowledgeAlert(UUID alertId, String acknowledgedBy) {
        log.info("Acknowledging alert: {} by: {}", alertId, acknowledgedBy);

        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new RuntimeException("Alert not found: " + alertId));

        alert.setStatus(Alert.AlertStatus.ACKNOWLEDGED);
        alert.setAcknowledgedBy(acknowledgedBy);
        alert.setAcknowledgedAt(LocalDateTime.now());

        Alert saved = alertRepository.save(alert);
        return MonitoringMapper.toAlertResponse(saved);
    }

    @Transactional
    public AlertResponse resolveAlert(UUID alertId, String resolvedBy, String resolutionNotes) {
        log.info("Resolving alert: {} by: {}", alertId, resolvedBy);

        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new RuntimeException("Alert not found: " + alertId));

        alert.setStatus(Alert.AlertStatus.RESOLVED);
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolutionNotes(resolutionNotes);

        Alert saved = alertRepository.save(alert);
        return MonitoringMapper.toAlertResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TransactionMetricsResponse> getMetricsByTransactionId(UUID transactionId) {
        List<TransactionMetrics> metrics = metricsRepository.findByTransactionIdOrderByTimestampDesc(transactionId);
        return metrics.stream()
            .map(MonitoringMapper::toMetricResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getAlertsByTransactionId(UUID transactionId) {
        List<Alert> alerts = alertRepository.findByTransactionIdOrderByCreatedAtDesc(transactionId);
        return alerts.stream()
            .map(MonitoringMapper::toAlertResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getOpenAlerts() {
        List<Alert> alerts = alertRepository.findByStatusOrderByCreatedAtDesc(Alert.AlertStatus.OPEN);
        return alerts.stream()
            .map(MonitoringMapper::toAlertResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> getCriticalAlerts() {
        List<Alert> alerts = alertRepository.findBySeverityAndStatusOrderByCreatedAtDesc(
            Alert.AlertSeverity.CRITICAL, Alert.AlertStatus.OPEN);
        return alerts.stream()
            .map(MonitoringMapper::toAlertResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MonitoringDashboard getDashboardData() {
        long openAlerts = alertRepository.countByStatus(Alert.AlertStatus.OPEN);
        long criticalAlerts = alertRepository.countBySeverityAndStatus(
            Alert.AlertSeverity.CRITICAL, Alert.AlertStatus.OPEN);
        long warningAlerts = alertRepository.countBySeverityAndStatus(
            Alert.AlertSeverity.WARNING, Alert.AlertStatus.OPEN);

        LocalDateTime since = LocalDateTime.now().minusHours(24);
        long recentAlerts = alertRepository.countRecentAlerts(since);

        return MonitoringDashboard.builder()
            .openAlerts(openAlerts)
            .criticalAlerts(criticalAlerts)
            .warningAlerts(warningAlerts)
            .recentAlerts(recentAlerts)
            .timestamp(LocalDateTime.now())
            .build();
    }

    private void evaluateThresholds(TransactionMetrics metric) {
        if (metric.getThresholdCritical() != null &&
            metric.getMetricValue().compareTo(metric.getThresholdCritical()) >= 0) {

            metric.setSeverity(TransactionMetrics.MetricSeverity.CRITICAL);

            createAlert(AlertCreateRequest.builder()
                .transactionId(metric.getTransactionId())
                .alertType(Alert.AlertType.PERFORMANCE)
                .severity(Alert.AlertSeverity.CRITICAL)
                .title("Critical threshold exceeded for " + metric.getMetricName())
                .description(String.format("Metric %s exceeded critical threshold: %s %s",
                    metric.getMetricName(), metric.getMetricValue(), metric.getMetricUnit()))
                .metricName(metric.getMetricName())
                .thresholdValue(metric.getThresholdCritical().toString())
                .actualValue(metric.getMetricValue().toString())
                .build());

        } else if (metric.getThresholdWarning() != null &&
                   metric.getMetricValue().compareTo(metric.getThresholdWarning()) >= 0) {

            metric.setSeverity(TransactionMetrics.MetricSeverity.WARNING);

            createAlert(AlertCreateRequest.builder()
                .transactionId(metric.getTransactionId())
                .alertType(Alert.AlertType.PERFORMANCE)
                .severity(Alert.AlertSeverity.WARNING)
                .title("Warning threshold exceeded for " + metric.getMetricName())
                .description(String.format("Metric %s exceeded warning threshold: %s %s",
                    metric.getMetricName(), metric.getMetricValue(), metric.getMetricUnit()))
                .metricName(metric.getMetricName())
                .thresholdValue(metric.getThresholdWarning().toString())
                .actualValue(metric.getMetricValue().toString())
                .build());

        } else {
            metric.setSeverity(TransactionMetrics.MetricSeverity.NORMAL);
        }
    }

    private void publishAlertEvent(Alert alert) {
        // Kafka removed for local development - just logging the alert
        log.debug("Alert created: {}, type: {}, severity: {}, transaction: {}",
            alert.getId(), alert.getAlertType(), alert.getSeverity(), alert.getTransactionId());
    }

    @Scheduled(fixedDelay = 60000)
    public void cleanupOldMetrics() {
        log.debug("Cleaning up old metrics");

        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        List<TransactionMetrics> oldMetrics = metricsRepository.findTop50ByMetricTypeOrderByTimestampDesc(
            TransactionMetrics.MetricType.CUSTOM_METRIC);

        // This is a simplified cleanup - in production, you'd want more sophisticated logic
        log.info("Metrics cleanup completed");
    }
}
