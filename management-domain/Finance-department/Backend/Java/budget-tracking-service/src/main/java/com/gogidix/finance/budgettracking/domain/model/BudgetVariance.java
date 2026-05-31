package com.gogidix.finance.budgettracking.domain.model;

import com.gogidix.finance.budgettracking.domain.event.BudgetVarianceEvent;
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
 * Budget Variance Domain Entity
 * Tracks variance between budgeted and actual amounts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "budget_variances")
public class BudgetVariance extends BaseEntity {

    @Indexed(unique = true)
    private String varianceId;

    @Indexed
    private String tenantId;

    private String budgetId;

    private String budgetCode;

    private String budgetName;

    private String period;

    private YearMonth yearMonth;

    private BigDecimal budgetedAmount;

    private BigDecimal actualAmount;

    private BigDecimal committedAmount;

    private BigDecimal varianceAmount;

    private BigDecimal variancePercentage;

    private VarianceStatus varianceStatus;

    private String category;

    private String department;

    private String costCenter;

    private String fiscalYear;

    private String createdBy;

    private String lastUpdatedBy;

    private Instant calculatedAt;

    private String description;

    private String analysis;

    private List<VarianceBreakdown> breakdowns;

    private List<String> contributingFactors;

    private boolean requiresInvestigation;

    private String assignedTo;

    private Instant investigationDueBy;

    private boolean investigated;

    private String investigatedBy;

    private Instant investigatedAt;

    private String investigationNotes;

    private BigDecimal forecastedAmount;

    private BigDecimal forecastVariance;

    private List<String> tags;

    @Builder.Default
    private List<BudgetVarianceEvent> domainEvents = new ArrayList<>();

    public enum VarianceStatus {
        FAVORABLE,
        UNFAVORABLE,
        NEUTRAL,
        SIGNIFICANT_FAVORABLE,
        SIGNIFICANT_UNFAVORABLE,
        CRITICAL
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VarianceBreakdown {
        private String category;
        private String description;
        private BigDecimal budgetedAmount;
        private BigDecimal actualAmount;
        private BigDecimal varianceAmount;
        private BigDecimal variancePercentage;
        private String contribution;
    }

    /**
     * Creates a new budget variance
     */
    public static BudgetVariance create(String tenantId, String budgetId, String budgetCode,
                                       String budgetName, String period, YearMonth yearMonth,
                                       BigDecimal budgetedAmount, String createdBy) {
        BudgetVariance variance = BudgetVariance.builder()
            .tenantId(tenantId)
            .budgetId(budgetId)
            .budgetCode(budgetCode)
            .budgetName(budgetName)
            .period(period)
            .yearMonth(yearMonth)
            .budgetedAmount(budgetedAmount)
            .actualAmount(BigDecimal.ZERO)
            .committedAmount(BigDecimal.ZERO)
            .varianceAmount(budgetedAmount)
            .variancePercentage(BigDecimal.ZERO)
            .varianceStatus(VarianceStatus.NEUTRAL)
            .createdBy(createdBy)
            .calculatedAt(Instant.now())
            .breakdowns(new ArrayList<>())
            .contributingFactors(new ArrayList<>())
            .requiresInvestigation(false)
            .investigated(false)
            .tags(new ArrayList<>())
            .build();

        return variance;
    }

    /**
     * Calculates variance
     */
    public void calculateVariance(BigDecimal actualAmount, BigDecimal committedAmount) {
        this.actualAmount = actualAmount;
        this.committedAmount = committedAmount;
        this.varianceAmount = this.budgetedAmount.subtract(actualAmount);

        if (this.budgetedAmount.compareTo(BigDecimal.ZERO) != 0) {
            this.variancePercentage = this.varianceAmount
                .divide(this.budgetedAmount, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        } else {
            this.variancePercentage = BigDecimal.ZERO;
        }

        this.calculatedAt = Instant.now();
        updateVarianceStatus();
    }

    /**
     * Updates forecast
     */
    public void updateForecast(BigDecimal forecastedAmount) {
        this.forecastedAmount = forecastedAmount;
        this.forecastVariance = this.budgetedAmount.subtract(forecastedAmount);

        if (this.budgetedAmount.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal forecastVariancePct = this.forecastVariance
                .divide(this.budgetedAmount, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

            if (forecastVariancePct.compareTo(new BigDecimal("-10")) < 0) {
                this.varianceStatus = VarianceStatus.SIGNIFICANT_UNFAVORABLE;
                this.requiresInvestigation = true;
            }
        }
    }

    /**
     * Adds variance breakdown
     */
    public void addBreakdown(String category, String description, BigDecimal budgeted,
                            BigDecimal actual) {
        VarianceBreakdown breakdown = VarianceBreakdown.builder()
            .category(category)
            .description(description)
            .budgetedAmount(budgeted)
            .actualAmount(actual)
            .varianceAmount(budgeted.subtract(actual))
            .build();

        if (budgeted.compareTo(BigDecimal.ZERO) != 0) {
            breakdown.setVariancePercentage(
                breakdown.getVarianceAmount()
                    .divide(budgeted, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
            );
        }

        if (this.breakdowns == null) {
            this.breakdowns = new ArrayList<>();
        }
        this.breakdowns.add(breakdown);
    }

    /**
     * Adds contributing factor
     */
    public void addContributingFactor(String factor) {
        if (this.contributingFactors == null) {
            this.contributingFactors = new ArrayList<>();
        }
        if (!this.contributingFactors.contains(factor)) {
            this.contributingFactors.add(factor);
        }
    }

    /**
     * Assigns investigation
     */
    public void assignInvestigation(String assignedTo, Instant dueBy) {
        this.assignedTo = assignedTo;
        this.investigationDueBy = dueBy;
        this.requiresInvestigation = true;

        addDomainEvent(BudgetVarianceEvent.builder()
            .varianceId(this.varianceId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .varianceAmount(this.varianceAmount)
            .variancePercentage(this.variancePercentage)
            .varianceStatus(this.varianceStatus.name())
            .timestamp(Instant.now())
            .eventType("BUDGET_VARIANCE_INVESTIGATION_ASSIGNED")
            .build());
    }

    /**
     * Completes investigation
     */
    public void completeInvestigation(String investigatedBy, String notes) {
        this.investigated = true;
        this.investigatedBy = investigatedBy;
        this.investigatedAt = Instant.now();
        this.investigationNotes = notes;

        addDomainEvent(BudgetVarianceEvent.builder()
            .varianceId(this.varianceId)
            .budgetId(this.budgetId)
            .budgetCode(this.budgetCode)
            .tenantId(this.tenantId)
            .varianceAmount(this.varianceAmount)
            .variancePercentage(this.variancePercentage)
            .varianceStatus(this.varianceStatus.name())
            .timestamp(Instant.now())
            .eventType("BUDGET_VARIANCE_INVESTIGATION_COMPLETED")
            .build());
    }

    /**
     * Checks if variance is significant
     */
    public boolean isSignificant() {
        return this.varianceStatus == VarianceStatus.SIGNIFICANT_FAVORABLE
            || this.varianceStatus == VarianceStatus.SIGNIFICANT_UNFAVORABLE
            || this.varianceStatus == VarianceStatus.CRITICAL;
    }

    /**
     * Checks if variance is favorable
     */
    public boolean isFavorable() {
        return this.varianceAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if variance is unfavorable
     */
    public boolean isUnfavorable() {
        return this.varianceAmount.compareTo(BigDecimal.ZERO) < 0;
    }

    private void updateVarianceStatus() {
        BigDecimal absVariancePct = this.variancePercentage.abs();

        if (isFavorable()) {
            if (absVariancePct.compareTo(new BigDecimal("10")) >= 0) {
                this.varianceStatus = VarianceStatus.SIGNIFICANT_FAVORABLE;
            } else {
                this.varianceStatus = VarianceStatus.FAVORABLE;
            }
        } else if (isUnfavorable()) {
            if (absVariancePct.compareTo(new BigDecimal("20")) >= 0) {
                this.varianceStatus = VarianceStatus.CRITICAL;
                this.requiresInvestigation = true;
            } else if (absVariancePct.compareTo(new BigDecimal("10")) >= 0) {
                this.varianceStatus = VarianceStatus.SIGNIFICANT_UNFAVORABLE;
                this.requiresInvestigation = true;
            } else {
                this.varianceStatus = VarianceStatus.UNFAVORABLE;
            }
        } else {
            this.varianceStatus = VarianceStatus.NEUTRAL;
        }
    }

    public void addDomainEvent(BudgetVarianceEvent event) {
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
