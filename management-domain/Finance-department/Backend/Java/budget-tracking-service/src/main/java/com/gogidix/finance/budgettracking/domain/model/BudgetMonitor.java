package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.domain.event.BudgetThresholdExceededEvent;
import com.gogidix.finance.budgettracking.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * Budget Monitor Domain Entity
 * Real-time budget monitoring with threshold tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "budget_monitors")
public class BudgetMonitor extends BaseEntity {

    @Indexed(unique = true)
    private String monitorId;

    @Indexed
    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String budgetName;

    private String budgetPeriod;

    private YearMonth period;

    private BigDecimal allocatedAmount;

    private BigDecimal committedAmount;

    private BigDecimal actualExpenditure;

    private BigDecimal availableBalance;

    private BigDecimal variance;

    private BigDecimal utilizationPercentage;

    private MonitorStatus status;

    private String category;

    private String department;

    private String costCenter;

    private String fiscalYear;

    private String createdBy;

    private String lastUpdatedBy;

    private Instant lastCalculatedAt;

    private List<ThresholdStatus> thresholdStatuses;

    private List<String> alertRecipients;

    private String currency;

    private boolean thresholdBreached;

    private Integer warningCount;

    private Integer criticalCount;

    @Builder.Default
    private List<BudgetThresholdExceededEvent> domainEvents = new ArrayList<>();

    public enum MonitorStatus {
        ON_TRACK,
        ATTENTION,
        WARNING,
        CRITICAL,
        EXHAUSTED,
        OVER_BUDGET
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThresholdStatus {
        private String thresholdType;
        private BigDecimal thresholdValue;
        private BigDecimal currentValue;
        private ThresholdLevel level;
        private boolean breached;
        private Instant breachedAt;
        private boolean acknowledged;
        private String acknowledgedBy;
        private Instant acknowledgedAt;
    }

    public enum ThresholdLevel {
        INFO,
        WARNING,
        CRITICAL
    }

    /**
     * Creates a new budget monitor
     */
    public static BudgetMonitor create(String tenantId, String budgetId, String budgetCode,
                                       String budgetName, String budgetPeriod, YearMonth period,
                                       BigDecimal allocatedAmount, String currency, String createdBy) {
        BudgetMonitor monitor = BudgetMonitor.builder()
            .tenantId(tenantId)
            .budgetId(budgetId)
            .budgetCode(budgetCode)
            .budgetName(budgetName)
            .budgetPeriod(budgetPeriod)
            .period(period)
            .allocatedAmount(allocatedAmount)
            .currency(currency)
            .committedAmount(BigDecimal.ZERO)
            .actualExpenditure(BigDecimal.ZERO)
            .availableBalance(allocatedAmount)
            .variance(allocatedAmount)
            .utilizationPercentage(BigDecimal.ZERO)
            .status(MonitorStatus.ON_TRACK)
            .createdBy(createdBy)
            .thresholdStatuses(new ArrayList<>())
            .alertRecipients(new ArrayList<>())
            .thresholdBreached(false)
            .warningCount(0)
            .criticalCount(0)
            .build();

        monitor.setLastCalculatedAt(Instant.now());

        return monitor;
    }

    /**
     * Records an expenditure
     */
    public void recordExpenditure(BigDecimal amount) {
        this.actualExpenditure = this.actualExpenditure.add(amount);
        this.availableBalance = this.allocatedAmount.subtract(this.committedAmount).subtract(this.actualExpenditure);
        recalculate();
    }

    /**
     * Records a commitment
     */
    public void recordCommitment(BigDecimal amount) {
        this.committedAmount = this.committedAmount.add(amount);
        this.availableBalance = this.allocatedAmount.subtract(this.committedAmount).subtract(this.actualExpenditure);
        recalculate();
    }

    /**
     * Releases a commitment
     */
    public void releaseCommitment(BigDecimal amount) {
        this.committedAmount = this.committedAmount.subtract(amount);
        if (this.committedAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.committedAmount = BigDecimal.ZERO;
        }
        this.availableBalance = this.allocatedAmount.subtract(this.committedAmount).subtract(this.actualExpenditure);
        recalculate();
    }

    /**
     * Adjusts allocated amount
     */
    public void adjustAllocation(BigDecimal newAllocation, String updatedBy) {
        this.allocatedAmount = newAllocation;
        this.availableBalance = newAllocation.subtract(this.committedAmount).subtract(this.actualExpenditure);
        this.lastUpdatedBy = updatedBy;
        recalculate();
    }

    /**
     * Reverses an expenditure
     */
    public void reverseExpenditure(BigDecimal amount) {
        this.actualExpenditure = this.actualExpenditure.subtract(amount);
        if (this.actualExpenditure.compareTo(BigDecimal.ZERO) < 0) {
            this.actualExpenditure = BigDecimal.ZERO;
        }
        this.availableBalance = this.allocatedAmount.subtract(this.committedAmount).subtract(this.actualExpenditure);
        recalculate();
    }

    /**
     * Checks and updates threshold status
     */
    public ThresholdStatus checkThreshold(String thresholdType, BigDecimal thresholdValue, ThresholdLevel level) {
        ThresholdStatus existingStatus = findThresholdStatus(thresholdType);

        boolean breached = this.utilizationPercentage.compareTo(thresholdValue) >= 0;

        ThresholdStatus newStatus = ThresholdStatus.builder()
            .thresholdType(thresholdType)
            .thresholdValue(thresholdValue)
            .currentValue(this.utilizationPercentage)
            .level(level)
            .breached(breached)
            .breachedAt(breached ? Instant.now() : null)
            .acknowledged(existingStatus != null && existingStatus.acknowledged)
            .acknowledgedBy(existingStatus != null ? existingStatus.acknowledgedBy : null)
            .acknowledgedAt(existingStatus != null ? existingStatus.acknowledgedAt : null)
            .build();

        updateThresholdStatus(newStatus);

        if (breached && (existingStatus == null || !existingStatus.breached)) {
            this.thresholdBreached = true;
            if (level == ThresholdLevel.WARNING) {
                this.warningCount++;
            } else if (level == ThresholdLevel.CRITICAL) {
                this.criticalCount++;
            }

            addDomainEvent(BudgetThresholdExceededEvent.builder()
                .monitorId(this.monitorId)
                .budgetId(this.budgetId)
                .budgetCode(this.budgetCode)
                .tenantId(this.tenantId)
                .thresholdType(thresholdType)
                .thresholdValue(thresholdValue)
                .currentValue(this.utilizationPercentage)
                .level(level.name())
                .timestamp(Instant.now())
                .build());
        }

        return newStatus;
    }

    /**
     * Acknowledges a threshold breach
     */
    public void acknowledgeThreshold(String thresholdType, String acknowledgedBy) {
        ThresholdStatus status = findThresholdStatus(thresholdType);
        if (status != null) {
            status.setAcknowledged(true);
            status.setAcknowledgedBy(acknowledgedBy);
            status.setAcknowledgedAt(Instant.now());
            updateThresholdStatus(status);
        }
    }

    /**
     * Gets utilization percentage
     */
    public BigDecimal getUtilizationPercentage() {
        if (this.allocatedAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalUsed = this.committedAmount.add(this.actualExpenditure);
        return totalUsed.divide(this.allocatedAmount, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Gets variance amount
     */
    public BigDecimal getVariance() {
        return this.allocatedAmount.subtract(this.committedAmount).subtract(this.actualExpenditure);
    }

    /**
     * Checks if budget is available
     */
    public boolean isAvailable(BigDecimal amount) {
        return this.availableBalance.compareTo(amount) >= 0;
    }

    /**
     * Checks if budget is exhausted
     */
    public boolean isExhausted() {
        return this.availableBalance.compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * Checks if budget is over budget
     */
    public boolean isOverBudget() {
        return this.availableBalance.compareTo(BigDecimal.ZERO) < 0;
    }

    private void recalculate() {
        this.variance = getVariance();
        this.utilizationPercentage = getUtilizationPercentage();
        this.lastCalculatedAt = Instant.now();
        updateStatus();
    }

    private void updateStatus() {
        if (isOverBudget()) {
            this.status = MonitorStatus.OVER_BUDGET;
        } else if (isExhausted()) {
            this.status = MonitorStatus.EXHAUSTED;
        } else if (this.criticalCount > 0 || this.utilizationPercentage.compareTo(new BigDecimal("90")) >= 0) {
            this.status = MonitorStatus.CRITICAL;
        } else if (this.warningCount > 0 || this.utilizationPercentage.compareTo(new BigDecimal("75")) >= 0) {
            this.status = MonitorStatus.WARNING;
        } else if (this.utilizationPercentage.compareTo(new BigDecimal("50")) >= 0) {
            this.status = MonitorStatus.ATTENTION;
        } else {
            this.status = MonitorStatus.ON_TRACK;
        }
    }

    private ThresholdStatus findThresholdStatus(String thresholdType) {
        if (this.thresholdStatuses == null) {
            return null;
        }
        return this.thresholdStatuses.stream()
            .filter(ts -> ts.getThresholdType().equals(thresholdType))
            .findFirst()
            .orElse(null);
    }

    private void updateThresholdStatus(ThresholdStatus newStatus) {
        if (this.thresholdStatuses == null) {
            this.thresholdStatuses = new ArrayList<>();
        }
        this.thresholdStatuses.removeIf(ts -> ts.getThresholdType().equals(newStatus.getThresholdType()));
        this.thresholdStatuses.add(newStatus);
    }

    /**
     * Adds alert recipient
     */
    public void addAlertRecipient(String recipient) {
        if (this.alertRecipients == null) {
            this.alertRecipients = new ArrayList<>();
        }
        if (!this.alertRecipients.contains(recipient)) {
            this.alertRecipients.add(recipient);
        }
    }

    /**
     * Removes alert recipient
     */
    public void removeAlertRecipient(String recipient) {
        if (this.alertRecipients != null) {
            this.alertRecipients.remove(recipient);
        }
    }

    public void addDomainEvent(BudgetThresholdExceededEvent event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
