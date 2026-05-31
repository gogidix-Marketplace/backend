package com.gogidix.transaction.monitoring.dto;

import com.gogidix.transaction.monitoring.domain.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {

    private UUID id;
    private UUID transactionId;
    private Alert.AlertType alertType;
    private Alert.AlertSeverity severity;
    private String title;
    private String description;
    private String metricName;
    private String thresholdValue;
    private String actualValue;
    private Alert.AlertStatus status;
    private String acknowledgedBy;
    private LocalDateTime acknowledgedAt;
    private String resolvedBy;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
