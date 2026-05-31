package com.gogidix.finance.budgettracking.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Threshold Alert Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThresholdAlertResponseDto {

    private String id;

    private String alertId;

    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String alertName;

    private String description;

    private AlertTypeDto alertType;

    private ThresholdTypeDto thresholdType;

    private BigDecimal thresholdValue;

    private ThresholdLevelDto thresholdLevel;

    private boolean enabled;

    private AlertStatusDto status;

    private String category;

    private String department;

    private String costCenter;

    private String createdBy;

    private String modifiedBy;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveTo;

    private AlertFrequencyDto frequency;

    private boolean recurring;

    private String recurrencePattern;

    private List<String> recipients;

    private List<String> recipientGroups;

    private String notificationChannel;

    private boolean requireAcknowledgement;

    private Integer escalationLevel;

    private String escalateTo;

    private BigDecimal escalationThreshold;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastTriggeredAt;

    private Integer triggerCount;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant lastAcknowledgedAt;

    private String lastAcknowledgedBy;

    private String templateId;

    private String customMessage;

    private List<AlertHistoryDto> history;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum AlertTypeDto {
        UTILIZATION,
        VARIANCE,
        EXPENDITURE_RATE,
        COMMITMENT,
        BALANCE,
        FORECAST
    }

    public enum ThresholdTypeDto {
        PERCENTAGE,
        AMOUNT,
        COUNT,
        RATE
    }

    public enum ThresholdLevelDto {
        INFO,
        WARNING,
        CRITICAL
    }

    public enum AlertStatusDto {
        ACTIVE,
        INACTIVE,
        PAUSED,
        EXPIRED,
        TRIGGERED,
        ACKNOWLEDGED
    }

    public enum AlertFrequencyDto {
        IMMEDIATE,
        HOURLY,
        DAILY,
        WEEKLY,
        MONTHLY
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertHistoryDto {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant triggeredAt;
        private BigDecimal currentValue;
        private String triggeredBy;
        private String message;
        private boolean acknowledged;
        private String acknowledgedBy;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        private Instant acknowledgedAt;
    }
}
