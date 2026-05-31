package com.gogidix.finance.cashflow.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cashflow Statement Domain Entity
 * Multi-tenant cashflow statement reports for financial reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document(collection = "cashflow_statements")
public class CashflowStatement {

    @Id
    private String id;

    @Indexed
    private String statementId;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    private StatementType statementType;

    private LocalDate startDate;

    private LocalDate endDate;

    private StatementPeriod period;

    private StatementStatus status;

    private String generatedBy;

    private Instant generatedAt;

    private Instant lastUpdated;

    private BigDecimal beginningCash;

    private BigDecimal endingCash;

    private BigDecimal netCashIncreaseDecrease;

    private CashflowSection operatingActivities;

    private CashflowSection investingActivities;

    private CashflowSection financingActivities;

    private List<StatementItem> lineItems;

    private String currency;

    private Integer fiscalYear;

    private Integer fiscalPeriod;

    private Boolean isConsolidated;

    private List<String> subsidiaryIds;

    private String parentStatementId;

    private BigDecimal priorPeriodCash;

    private BigDecimal cashChangePercentage;

    private List<String> tags;

    private String notes;

    private String approvalStatus;

    private String approvedBy;

    private Instant approvedAt;

    @Builder.Default
    private List<StatementMetadata> metadata = new ArrayList<>();

    public enum StatementType {
        DIRECT,
        INDIRECT,
        CONSOLIDATED,
        PROJECTED,
        ACTUAL
    }

    public enum StatementPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        SEMI_ANNUALLY,
        ANNUALLY,
        YTD,
        CUSTOM
    }

    public enum StatementStatus {
        DRAFT,
        IN_REVIEW,
        FINALIZED,
        APPROVED,
        REJECTED,
        ARCHIVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashflowSection {
        private BigDecimal totalInflow;
        private BigDecimal totalOutflow;
        private BigDecimal netCashflow;
        private List<StatementItem> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementItem {
        private String itemId;
        private String code;
        private String description;
        private BigDecimal amount;
        private ItemType type;
        private String category;
        private Integer sequence;
        private Boolean isSubtotal;
        private String parentItemId;
    }

    public enum ItemType {
        INFLOW,
        OUTFLOW,
        NET,
        SUBTOTAL,
        HEADER
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatementMetadata {
        private String key;
        private String value;
        private String type;
    }

    /**
     * Creates a new cashflow statement
     */
    public static CashflowStatement create(String tenantId, String name,
                                           StatementType statementType, LocalDate startDate,
                                           LocalDate endDate, StatementPeriod period,
                                           String generatedBy, String currency) {
        CashflowStatement statement = CashflowStatement.builder()
                .statementId(java.util.UUID.randomUUID().toString())
                .tenantId(tenantId)
                .name(name)
                .statementType(statementType)
                .startDate(startDate)
                .endDate(endDate)
                .period(period)
                .generatedBy(generatedBy)
                .currency(currency)
                .status(StatementStatus.DRAFT)
                .operatingActivities(new CashflowSection(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>()))
                .investingActivities(new CashflowSection(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>()))
                .financingActivities(new CashflowSection(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>()))
                .lineItems(new ArrayList<>())
                .subsidiaryIds(new ArrayList<>())
                .tags(new ArrayList<>())
                .metadata(new ArrayList<>())
                .build();

        return statement;
    }

    /**
     * Generates the statement from cashflow items
     */
    public void generate(List<CashflowItem> cashflowItems, BigDecimal beginningCash) {
        this.beginningCash = beginningCash;

        BigDecimal operatingInflow = BigDecimal.ZERO;
        BigDecimal operatingOutflow = BigDecimal.ZERO;
        BigDecimal investingInflow = BigDecimal.ZERO;
        BigDecimal investingOutflow = BigDecimal.ZERO;
        BigDecimal financingInflow = BigDecimal.ZERO;
        BigDecimal financingOutflow = BigDecimal.ZERO;

        for (CashflowItem item : cashflowItems) {
            BigDecimal amount = item.getNetAmount() != null ? item.getNetAmount() : item.getAmount();

            if (isOperatingActivity(item.getCategory())) {
                if (item.isInflow()) {
                    operatingInflow = operatingInflow.add(amount);
                } else {
                    operatingOutflow = operatingOutflow.add(amount);
                }
            } else if (isInvestingActivity(item.getCategory())) {
                if (item.isInflow()) {
                    investingInflow = investingInflow.add(amount);
                } else {
                    investingOutflow = investingOutflow.add(amount);
                }
            } else if (isFinancingActivity(item.getCategory())) {
                if (item.isInflow()) {
                    financingInflow = financingInflow.add(amount);
                } else {
                    financingOutflow = financingOutflow.add(amount);
                }
            }
        }

        this.operatingActivities = CashflowSection.builder()
                .totalInflow(operatingInflow)
                .totalOutflow(operatingOutflow)
                .netCashflow(operatingInflow.subtract(operatingOutflow))
                .items(new ArrayList<>())
                .build();

        this.investingActivities = CashflowSection.builder()
                .totalInflow(investingInflow)
                .totalOutflow(investingOutflow)
                .netCashflow(investingInflow.subtract(investingOutflow))
                .items(new ArrayList<>())
                .build();

        this.financingActivities = CashflowSection.builder()
                .totalInflow(financingInflow)
                .totalOutflow(financingOutflow)
                .netCashflow(financingInflow.subtract(financingOutflow))
                .items(new ArrayList<>())
                .build();

        BigDecimal totalNetCashflow = this.operatingActivities.getNetCashflow()
                .add(this.investingActivities.getNetCashflow())
                .add(this.financingActivities.getNetCashflow());

        this.netCashIncreaseDecrease = totalNetCashflow;
        this.endingCash = this.beginningCash.add(totalNetCashflow);

        if (this.beginningCash.compareTo(BigDecimal.ZERO) != 0) {
            this.cashChangePercentage = totalNetCashflow
                    .divide(this.beginningCash, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }

        this.status = StatementStatus.IN_REVIEW;
        this.generatedAt = Instant.now();
        this.lastUpdated = Instant.now();
    }

    /**
     * Finalizes the statement
     */
    public void finalizeStatement() {
        if (this.status != StatementStatus.IN_REVIEW) {
            throw new IllegalStateException("Can only finalize statements in review");
        }

        this.status = StatementStatus.FINALIZED;
        this.lastUpdated = Instant.now();
    }

    /**
     * Approves the statement
     */
    public void approve(String approvedBy) {
        if (this.status != StatementStatus.FINALIZED && this.status != StatementStatus.IN_REVIEW) {
            throw new IllegalStateException("Can only approve finalized or in-review statements");
        }

        this.status = StatementStatus.APPROVED;
        this.approvalStatus = "APPROVED";
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.lastUpdated = Instant.now();
    }

    /**
     * Rejects the statement
     */
    public void reject(String rejectedBy, String reason) {
        if (this.status == StatementStatus.APPROVED) {
            throw new IllegalStateException("Cannot reject approved statements");
        }

        this.status = StatementStatus.REJECTED;
        this.approvalStatus = "REJECTED";
        this.notes = reason;
        this.lastUpdated = Instant.now();
    }

    /**
     * Archives the statement
     */
    public void archive() {
        if (this.status == StatementStatus.ARCHIVED) {
            throw new IllegalStateException("Statement is already archived");
        }

        this.status = StatementStatus.ARCHIVED;
        this.lastUpdated = Instant.now();
    }

    /**
     * Adds a line item to the statement
     */
    public void addLineItem(StatementItem item) {
        if (this.lineItems == null) {
            this.lineItems = new ArrayList<>();
        }
        this.lineItems.add(item);
    }

    /**
     * Sets fiscal period information
     */
    public void setFiscalPeriod(Integer fiscalYear, Integer fiscalPeriod) {
        this.fiscalYear = fiscalYear;
        this.fiscalPeriod = fiscalPeriod;
    }

    /**
     * Marks as consolidated statement
     */
    public void markAsConsolidated(List<String> subsidiaryIds) {
        this.isConsolidated = true;
        this.subsidiaryIds = subsidiaryIds;
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, String value, String type) {
        if (this.metadata == null) {
            this.metadata = new ArrayList<>();
        }
        this.metadata.add(StatementMetadata.builder()
                .key(key)
                .value(value)
                .type(type)
                .build());
    }

    /**
     * Calculates cash position health
     */
    public CashPositionHealth getCashPositionHealth() {
        if (this.netCashIncreaseDecrease == null) {
            return CashPositionHealth.UNKNOWN;
        }

        if (this.netCashIncreaseDecrease.compareTo(BigDecimal.ZERO) > 0) {
            if (this.netCashIncreaseDecrease.compareTo(this.beginningCash.multiply(new BigDecimal("0.1"))) > 0) {
                return CashPositionHealth.EXCELLENT;
            }
            return CashPositionHealth.GOOD;
        } else {
            if (this.netCashIncreaseDecrease.abs().compareTo(this.beginningCash.multiply(new BigDecimal("0.05"))) > 0) {
                return CashPositionHealth.CRITICAL;
            }
            return CashPositionHealth.WARNING;
        }
    }

    public enum CashPositionHealth {
        EXCELLENT,
        GOOD,
        WARNING,
        CRITICAL,
        UNKNOWN
    }

    private boolean isOperatingActivity(CashflowItem.CashflowCategory category) {
        return category == CashflowItem.CashflowCategory.OPERATING_REVENUE ||
                category == CashflowItem.CashflowCategory.OPERATING_EXPENSE ||
                category == CashflowItem.CashflowCategory.PAYROLL ||
                category == CashflowItem.CashflowCategory.TAX_PAYMENT ||
                category == CashflowItem.CashflowCategory.SUPPLIER_PAYMENT ||
                category == CashflowItem.CashflowCategory.REFUND_RECEIVED ||
                category == CashflowItem.CashflowCategory.REFUND_ISSUED;
    }

    private boolean isInvestingActivity(CashflowItem.CashflowCategory category) {
        return category == CashflowItem.CashflowCategory.CAPITAL_EXPENDITURE ||
                category == CashflowItem.CashflowCategory.INVESTMENT_RETURN;
    }

    private boolean isFinancingActivity(CashflowItem.CashflowCategory category) {
        return category == CashflowItem.CashflowCategory.LOAN_PROCEEDS ||
                category == CashflowItem.CashflowCategory.LOAN_REPAYMENT ||
                category == CashflowItem.CashflowCategory.CAPITAL_CONTRIBUTION ||
                category == CashflowItem.CashflowCategory.DIVIDEND_INCOME ||
                category == CashflowItem.CashflowCategory.DIVIDEND_PAYMENT ||
                category == CashflowItem.CashflowCategory.INTEREST_INCOME ||
                category == CashflowItem.CashflowCategory.INTEREST_PAYMENT;
    }

    /**
     * Gets total net cashflow from all activities
     */
    public BigDecimal getTotalNetCashflow() {
        BigDecimal total = BigDecimal.ZERO;
        if (this.operatingActivities != null) {
            total = total.add(this.operatingActivities.getNetCashflow());
        }
        if (this.investingActivities != null) {
            total = total.add(this.investingActivities.getNetCashflow());
        }
        if (this.financingActivities != null) {
            total = total.add(this.financingActivities.getNetCashflow());
        }
        return total;
    }
}
