package com.gogidix.finance.budgettracking.infrastructure.persistence.mongodb;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import com.gogidix.finance.budgettracking.domain.event.BudgetThresholdExceededEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing BudgetMonitor domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "budget_monitors")
public class BudgetMonitorEntity {

    @Id
    private String id;

    @Indexed
    @Field("monitor_id")
    private String monitorId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("budget_id")
    private String budgetId;

    @Field("budget_code")
    private String budgetCode;

    @Field("budget_name")
    private String budgetName;

    @Field("budget_period")
    private String budgetPeriod;

    @Field("period")
    private String period;

    @Field("allocated_amount")
    private BigDecimal allocatedAmount;

    @Field("committed_amount")
    private BigDecimal committedAmount;

    @Field("actual_expenditure")
    private BigDecimal actualExpenditure;

    @Field("available_balance")
    private BigDecimal availableBalance;

    @Field("variance")
    private BigDecimal variance;

    @Field("utilization_percentage")
    private BigDecimal utilizationPercentage;

    @Field("status")
    private String status;

    @Field("category")
    private String category;

    @Field("department")
    private String department;

    @Field("cost_center")
    private String costCenter;

    @Field("fiscal_year")
    private String fiscalYear;

    @Field("created_by")
    private String createdBy;

    @Field("last_updated_by")
    private String lastUpdatedBy;

    @Field("last_calculated_at")
    private Instant lastCalculatedAt;

    @Field("threshold_statuses")
    private List<ThresholdStatusEmbed> thresholdStatuses;

    @Field("alert_recipients")
    private List<String> alertRecipients;

    @Field("currency")
    private String currency;

    @Field("threshold_breached")
    private Boolean thresholdBreached;

    @Field("warning_count")
    private Integer warningCount;

    @Field("critical_count")
    private Integer criticalCount;

    @Field("domain_events")
    private List<BudgetThresholdExceededEvent> domainEvents;

    // Default constructor for MongoDB
    public BudgetMonitorEntity() {
    }

    // Constructor from domain model
    public BudgetMonitorEntity(BudgetMonitor monitor) {
        this.monitorId = monitor.getMonitorId();
        this.tenantId = monitor.getTenantId();
        this.budgetId = monitor.getBudgetId();
        this.budgetCode = monitor.getBudgetCode();
        this.budgetName = monitor.getBudgetName();
        this.budgetPeriod = monitor.getBudgetPeriod();
        this.period = monitor.getPeriod() != null ? monitor.getPeriod().toString() : null;
        this.allocatedAmount = monitor.getAllocatedAmount();
        this.committedAmount = monitor.getCommittedAmount();
        this.actualExpenditure = monitor.getActualExpenditure();
        this.availableBalance = monitor.getAvailableBalance();
        this.variance = monitor.getVariance();
        this.utilizationPercentage = monitor.getUtilizationPercentage();
        this.status = monitor.getStatus() != null ? monitor.getStatus().name() : null;
        this.category = monitor.getCategory();
        this.department = monitor.getDepartment();
        this.costCenter = monitor.getCostCenter();
        this.fiscalYear = monitor.getFiscalYear();
        this.createdBy = monitor.getCreatedBy();
        this.lastUpdatedBy = monitor.getLastUpdatedBy();
        this.lastCalculatedAt = monitor.getLastCalculatedAt();
        this.currency = monitor.getCurrency();
        this.thresholdBreached = monitor.isThresholdBreached();
        this.warningCount = monitor.getWarningCount();
        this.criticalCount = monitor.getCriticalCount();

        // Convert threshold statuses
        if (monitor.getThresholdStatuses() != null) {
            this.thresholdStatuses = new ArrayList<>();
            for (BudgetMonitor.ThresholdStatus ts : monitor.getThresholdStatuses()) {
                this.thresholdStatuses.add(new ThresholdStatusEmbed(ts));
            }
        }

        this.alertRecipients = monitor.getAlertRecipients() != null
            ? new ArrayList<>(monitor.getAlertRecipients())
            : new ArrayList<>();
        this.domainEvents = monitor.getDomainEvents() != null
            ? new ArrayList<>(monitor.getDomainEvents())
            : new ArrayList<>();
    }

    // Convert to domain model
    public BudgetMonitor toDomainModel() {
        List<BudgetMonitor.ThresholdStatus> thresholdStatusList = new ArrayList<>();
        if (this.thresholdStatuses != null) {
            for (ThresholdStatusEmbed embed : this.thresholdStatuses) {
                thresholdStatusList.add(embed.toDomainModel());
            }
        }

        YearMonth yearMonth = this.period != null ? YearMonth.parse(this.period) : null;

        return BudgetMonitor.builder()
            .monitorId(this.monitorId)
            .tenantId(this.tenantId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .budgetName(this.budgetName)
            .budgetPeriod(this.budgetPeriod)
            .period(yearMonth)
            .allocatedAmount(this.allocatedAmount)
            .committedAmount(this.committedAmount)
            .actualExpenditure(this.actualExpenditure)
            .availableBalance(this.availableBalance)
            .variance(this.variance)
            .utilizationPercentage(this.utilizationPercentage)
            .status(this.status != null ? BudgetMonitor.MonitorStatus.valueOf(this.status) : null)
            .category(this.category)
            .department(this.department)
            .costCenter(this.costCenter)
            .fiscalYear(this.fiscalYear)
            .createdBy(this.createdBy)
            .lastUpdatedBy(this.lastUpdatedBy)
            .lastCalculatedAt(this.lastCalculatedAt)
            .thresholdStatuses(thresholdStatusList)
            .alertRecipients(this.alertRecipients != null ? new ArrayList<>(this.alertRecipients) : new ArrayList<>())
            .currency(this.currency)
            .thresholdBreached(this.thresholdBreached != null ? this.thresholdBreached : false)
            .warningCount(this.warningCount != null ? this.warningCount : 0)
            .criticalCount(this.criticalCount != null ? this.criticalCount : 0)
            .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
            .build();
    }

    // Embedded class for threshold statuses
    public static class ThresholdStatusEmbed {
        private String thresholdType;
        private BigDecimal thresholdValue;
        private BigDecimal currentValue;
        private String level;
        private Boolean breached;
        private Instant breachedAt;
        private Boolean acknowledged;
        private String acknowledgedBy;
        private Instant acknowledgedAt;

        public ThresholdStatusEmbed() {
        }

        public ThresholdStatusEmbed(BudgetMonitor.ThresholdStatus ts) {
            this.thresholdType = ts.getThresholdType();
            this.thresholdValue = ts.getThresholdValue();
            this.currentValue = ts.getCurrentValue();
            this.level = ts.getLevel() != null ? ts.getLevel().name() : null;
            this.breached = ts.isBreached();
            this.breachedAt = ts.getBreachedAt();
            this.acknowledged = ts.isAcknowledged();
            this.acknowledgedBy = ts.getAcknowledgedBy();
            this.acknowledgedAt = ts.getAcknowledgedAt();
        }

        public BudgetMonitor.ThresholdStatus toDomainModel() {
            return new BudgetMonitor.ThresholdStatus(
                this.thresholdType,
                this.thresholdValue,
                this.currentValue,
                this.level != null ? BudgetMonitor.ThresholdLevel.valueOf(this.level) : null,
                this.breached != null ? this.breached : false,
                this.breachedAt,
                this.acknowledged != null ? this.acknowledged : false,
                this.acknowledgedBy,
                this.acknowledgedAt
            );
        }

        // Getters and setters
        public String getThresholdType() {
            return thresholdType;
        }

        public void setThresholdType(String thresholdType) {
            this.thresholdType = thresholdType;
        }

        public BigDecimal getThresholdValue() {
            return thresholdValue;
        }

        public void setThresholdValue(BigDecimal thresholdValue) {
            this.thresholdValue = thresholdValue;
        }

        public BigDecimal getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(BigDecimal currentValue) {
            this.currentValue = currentValue;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Boolean isBreached() {
            return breached;
        }

        public void setBreached(Boolean breached) {
            this.breached = breached;
        }

        public Instant getBreachedAt() {
            return breachedAt;
        }

        public void setBreachedAt(Instant breachedAt) {
            this.breachedAt = breachedAt;
        }

        public Boolean isAcknowledged() {
            return acknowledged;
        }

        public void setAcknowledged(Boolean acknowledged) {
            this.acknowledged = acknowledged;
        }

        public String getAcknowledgedBy() {
            return acknowledgedBy;
        }

        public void setAcknowledgedBy(String acknowledgedBy) {
            this.acknowledgedBy = acknowledgedBy;
        }

        public Instant getAcknowledgedAt() {
            return acknowledgedAt;
        }

        public void setAcknowledgedAt(Instant acknowledgedAt) {
            this.acknowledgedAt = acknowledgedAt;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getBudgetPeriod() {
        return budgetPeriod;
    }

    public void setBudgetPeriod(String budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getCommittedAmount() {
        return committedAmount;
    }

    public void setCommittedAmount(BigDecimal committedAmount) {
        this.committedAmount = committedAmount;
    }

    public BigDecimal getActualExpenditure() {
        return actualExpenditure;
    }

    public void setActualExpenditure(BigDecimal actualExpenditure) {
        this.actualExpenditure = actualExpenditure;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getVariance() {
        return variance;
    }

    public void setVariance(BigDecimal variance) {
        this.variance = variance;
    }

    public BigDecimal getUtilizationPercentage() {
        return utilizationPercentage;
    }

    public void setUtilizationPercentage(BigDecimal utilizationPercentage) {
        this.utilizationPercentage = utilizationPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastCalculatedAt() {
        return lastCalculatedAt;
    }

    public void setLastCalculatedAt(Instant lastCalculatedAt) {
        this.lastCalculatedAt = lastCalculatedAt;
    }

    public List<ThresholdStatusEmbed> getThresholdStatuses() {
        return thresholdStatuses;
    }

    public void setThresholdStatuses(List<ThresholdStatusEmbed> thresholdStatuses) {
        this.thresholdStatuses = thresholdStatuses;
    }

    public List<String> getAlertRecipients() {
        return alertRecipients;
    }

    public void setAlertRecipients(List<String> alertRecipients) {
        this.alertRecipients = alertRecipients;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean isThresholdBreached() {
        return thresholdBreached;
    }

    public void setThresholdBreached(Boolean thresholdBreached) {
        this.thresholdBreached = thresholdBreached;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public Integer getCriticalCount() {
        return criticalCount;
    }

    public void setCriticalCount(Integer criticalCount) {
        this.criticalCount = criticalCount;
    }

    public List<BudgetThresholdExceededEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<BudgetThresholdExceededEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}
