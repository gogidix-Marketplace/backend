package com.gogidix.transaction.monitoring.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.monitoring.domain.entity.Alert;
import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics;
import com.gogidix.transaction.monitoring.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MonitoringMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static TransactionMetrics toMetricEntity(MetricCreateRequest request) {
        return TransactionMetrics.builder()
            .transactionId(request.getTransactionId())
            .metricType(request.getMetricType())
            .metricName(request.getMetricName())
            .metricValue(request.getMetricValue())
            .metricUnit(request.getMetricUnit())
            .thresholdWarning(request.getThresholdWarning())
            .thresholdCritical(request.getThresholdCritical())
            .build();
    }

    public static TransactionMetricsResponse toMetricResponse(TransactionMetrics entity) {
        return TransactionMetricsResponse.builder()
            .id(entity.getId())
            .transactionId(entity.getTransactionId())
            .metricType(entity.getMetricType())
            .metricName(entity.getMetricName())
            .metricValue(entity.getMetricValue())
            .metricUnit(entity.getMetricUnit())
            .thresholdWarning(entity.getThresholdWarning())
            .thresholdCritical(entity.getThresholdCritical())
            .severity(entity.getSeverity())
            .metadata(parseJson(entity.getMetadata()))
            .timestamp(entity.getTimestamp())
            .version(entity.getVersion())
            .build();
    }

    public static Alert toAlertEntity(AlertCreateRequest request) {
        return Alert.builder()
            .transactionId(request.getTransactionId())
            .alertType(request.getAlertType())
            .severity(request.getSeverity())
            .title(request.getTitle())
            .description(request.getDescription())
            .metricName(request.getMetricName())
            .thresholdValue(request.getThresholdValue())
            .actualValue(request.getActualValue())
            .status(Alert.AlertStatus.OPEN)
            .build();
    }

    public static AlertResponse toAlertResponse(Alert entity) {
        return AlertResponse.builder()
            .id(entity.getId())
            .transactionId(entity.getTransactionId())
            .alertType(entity.getAlertType())
            .severity(entity.getSeverity())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .metricName(entity.getMetricName())
            .thresholdValue(entity.getThresholdValue())
            .actualValue(entity.getActualValue())
            .status(entity.getStatus())
            .acknowledgedBy(entity.getAcknowledgedBy())
            .acknowledgedAt(entity.getAcknowledgedAt())
            .resolvedBy(entity.getResolvedBy())
            .resolvedAt(entity.getResolvedAt())
            .resolutionNotes(entity.getResolutionNotes())
            .metadata(parseJson(entity.getMetadata()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .version(entity.getVersion())
            .build();
    }

    private static Map<String, Object> parseJson(String json) {
        if (json == null || json.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }
}
