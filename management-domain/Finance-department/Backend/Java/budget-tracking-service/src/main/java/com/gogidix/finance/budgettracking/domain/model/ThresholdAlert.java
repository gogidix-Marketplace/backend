package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Threshold Alert Domain Entity
 * Configurable alert thresholds for budget monitoring
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "threshold_alerts")
public class ThresholdAlert extends BaseEntity {

    @Indexed(unique = true)
    private String alertId;

    @Indexed
    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String alertName;

    private String description;

    private AlertType alertType;

    private ThresholdType thresholdType;

    private BigDecimal thresholdValue;

    private ThresholdLevel thresholdLevel;

    private boolean enabled;

    private AlertStatus status;

    private String category;

    private String department;

    private String costCenter;

    private String createdBy;

    private String modifiedBy;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private AlertFrequency frequency;

    private boolean recurring;

    private String recurrencePattern;

    private List<String> recipients;

    private List<String> recipientGroups;

    private String notificationChannel;

    private boolean requireAcknowledgement;

    private Integer escalationLevel;

    private String escalateTo;

    private BigDecimal escalationThreshold;

    private Instant lastTriggeredAt;

    private Integer triggerCount;

    private Instant lastAcknowledgedAt;

    private String lastAcknowledgedBy;

    private String templateId;

    private String customMessage;

    private List<AlertHistory> history;

    public enum AlertType {
        UTILIZATION,
        VARIANCE,
        EXPENDITURE_RATE,
        COMMITMENT,
        BALANCE,
        FORECAST
    }

    public enum ThresholdType {
        PERCENTAGE,
        AMOUNT,
        COUNT,
        RATE
    }

    public enum ThresholdLevel {
        INFO,
        WARNING,
        CRITICAL
    }

    public enum AlertStatus {
        ACTIVE,
        INACTIVE,
        PAUSED,
        EXPIRED,
        TRIGGERED,
        ACKNOWLEDGED
    }

    public enum AlertFrequency {
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
    public static class AlertHistory {
        private Instant triggeredAt;
        private BigDecimal currentValue;
        private String triggeredBy;
        private String message;
        private boolean acknowledged;
        private String acknowledgedBy;
        private Instant acknowledgedAt;
    }

    /**
     * Creates a new threshold alert
     */
    public static ThresholdAlert create(String tenantId, String budgetId, String budgetCode,
                                       String alertName, AlertType alertType, ThresholdType thresholdType,
                                       BigDecimal thresholdValue, ThresholdLevel thresholdLevel,
                                       String createdBy) {
        ThresholdAlert alert = ThresholdAlert.builder()
            .tenantId(tenantId)
            .budgetId(budgetId)
            .budgetCode(budgetCode)
            .alertName(alertName)
            .alertType(alertType)
            .thresholdType(thresholdType)
            .thresholdValue(thresholdValue)
            .thresholdLevel(thresholdLevel)
            .enabled(true)
            .status(AlertStatus.ACTIVE)
            .createdBy(createdBy)
            .effectiveFrom(LocalDate.now())
            .frequency(AlertFrequency.IMMEDIATE)
            .recurring(false)
            .recipients(new ArrayList<>())
            .recipientGroups(new ArrayList<>())
            .requireAcknowledgement(true)
            .escalationLevel(0)
            .triggerCount(0)
            .history(new ArrayList<>())
            .build();

        return alert;
    }

    /**
     * Triggers the alert
     */
    public void trigger(BigDecimal currentValue, String triggeredBy) {
        this.status = AlertStatus.TRIGGERED;
        this.lastTriggeredAt = Instant.now();
        this.triggerCount++;

        AlertHistory historyEntry = AlertHistory.builder()
            .triggeredAt(Instant.now())
            .currentValue(currentValue)
            .triggeredBy(triggeredBy)
            .message(this.customMessage != null ? this.customMessage : buildAlertMessage(currentValue))
            .acknowledged(false)
            .build();

        if (this.history == null) {
            this.history = new ArrayList<>();
        }
        this.history.add(historyEntry);
    }

    /**
     * Acknowledges the alert
     */
    public void acknowledge(String acknowledgedBy) {
        if (this.status != AlertStatus.TRIGGERED) {
            throw new IllegalStateException("Can only acknowledge triggered alerts");
        }

        this.status = AlertStatus.ACKNOWLEDGED;
        this.lastAcknowledgedAt = Instant.now();
        this.lastAcknowledgedBy = acknowledgedBy;

        if (!this.history.isEmpty()) {
            AlertHistory lastEntry = this.history.get(this.history.size() - 1);
            lastEntry.setAcknowledged(true);
            lastEntry.setAcknowledgedBy(acknowledgedBy);
            lastEntry.setAcknowledgedAt(Instant.now());
        }

        if (!this.recurring) {
            this.enabled = false;
        }
    }

    /**
     * Resets the alert for re-triggering
     */
    public void reset() {
        this.status = AlertStatus.ACTIVE;
    }

    /**
     * Enables the alert
     */
    public void enable() {
        this.enabled = true;
        if (this.status == AlertStatus.INACTIVE || this.status == AlertStatus.PAUSED) {
            this.status = AlertStatus.ACTIVE;
        }
    }

    /**
     * Disables the alert
     */
    public void disable() {
        this.enabled = false;
        this.status = AlertStatus.INACTIVE;
    }

    /**
     * Pauses the alert
     */
    public void pause() {
        this.enabled = false;
        this.status = AlertStatus.PAUSED;
    }

    /**
     * Adds a recipient
     */
    public void addRecipient(String recipient) {
        if (this.recipients == null) {
            this.recipients = new ArrayList<>();
        }
        if (!this.recipients.contains(recipient)) {
            this.recipients.add(recipient);
        }
    }

    /**
     * Removes a recipient
     */
    public void removeRecipient(String recipient) {
        if (this.recipients != null) {
            this.recipients.remove(recipient);
        }
    }

    /**
     * Adds a recipient group
     */
    public void addRecipientGroup(String group) {
        if (this.recipientGroups == null) {
            this.recipientGroups = new ArrayList<>();
        }
        if (!this.recipientGroups.contains(group)) {
            this.recipientGroups.add(group);
        }
    }

    /**
     * Updates threshold configuration
     */
    public void updateThreshold(BigDecimal thresholdValue, ThresholdLevel thresholdLevel, String modifiedBy) {
        this.thresholdValue = thresholdValue;
        this.thresholdLevel = thresholdLevel;
        this.modifiedBy = modifiedBy;
    }

    /**
     * Checks if alert is effective
     */
    public boolean isEffective() {
        LocalDate now = LocalDate.now();
        boolean afterStart = this.effectiveFrom == null || !now.isBefore(this.effectiveFrom);
        boolean beforeEnd = this.effectiveTo == null || !now.isAfter(this.effectiveTo);
        return afterStart && beforeEnd;
    }

    /**
     * Checks if threshold is breached
     */
    public boolean isThresholdBreached(BigDecimal currentValue) {
        if (!this.enabled || !isEffective()) {
            return false;
        }

        return switch (this.thresholdType) {
            case PERCENTAGE, AMOUNT, RATE -> currentValue.compareTo(this.thresholdValue) >= 0;
            case COUNT -> currentValue.intValue() >= this.thresholdValue.intValue();
        };
    }

    /**
     * Escalates the alert
     */
    public void escalate() {
        this.escalationLevel++;
        this.status = AlertStatus.TRIGGERED;
    }

    /**
     * Gets the escalation recipient
     */
    public String getEscalationRecipient() {
        if (this.escalateTo != null && !this.escalateTo.isBlank()) {
            return this.escalateTo;
        }
        return this.recipients != null && !this.recipients.isEmpty()
            ? this.recipients.get(0)
            : null;
    }

    private String buildAlertMessage(BigDecimal currentValue) {
        return String.format("Alert '%s' for budget %s has been triggered. Current value: %s, Threshold: %s",
            this.alertName, this.budgetCode, currentValue, this.thresholdValue);
    }

    /**
     * Gets recent history entries
     */
    public List<AlertHistory> getRecentHistory(int limit) {
        if (this.history == null || this.history.isEmpty()) {
            return List.of();
        }
        int start = Math.max(0, this.history.size() - limit);
        return this.history.subList(start, this.history.size());
    }
}
