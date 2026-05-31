package com.gogidix.transaction.monitoring.dto;

import com.gogidix.transaction.monitoring.domain.entity.Alert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertCreateRequest {

    private UUID transactionId;

    @NotNull(message = "Alert type is required")
    private Alert.AlertType alertType;

    @NotNull(message = "Severity is required")
    private Alert.AlertSeverity severity;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private String metricName;

    private String thresholdValue;

    private String actualValue;

    private Map<String, Object> metadata;
}
