package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consolidation Report Domain Entity
 * Represents generated consolidation reports
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "consolidation_reports")
@CompoundIndex(def = "{'tenantId': 1, 'reportType': 1, 'periodEnd': -1}")
public class ConsolidationReport extends BaseEntity {

    @Indexed
    private String reportId;

    @Indexed
    private String tenantId;

    private String jobId;

    private String reportName;

    private ReportType reportType;

    private ReportStatus status;

    private LocalDate periodStart;

    private LocalDate periodEnd;

    private LocalDate asOfDate;

    private String baseCurrency;

    private String generatedBy;

    private Instant generatedAt;

    private String approvedBy;

    private Instant approvedAt;

    private String version;

    private ReportSummary summary;

    private BalanceSheetData balanceSheet;

    private IncomeStatementData incomeStatement;

    private CashFlowData cashFlow;

    private EquityData equityChanges;

    private List<SubsidiaryData> subsidiaryData;

    private List<AdjustmentReportEntry> adjustments;

    private List<EliminationReportEntry> eliminations;

    private List<IntercompanyTransaction> intercompanyTransactions;

    private Map<String, BigDecimal> currencyAdjustments;

    private List<ValidationError> validationErrors;

    private List<ReportWarning> warnings;

    private List<String> includedSubsidiaries;

    private List<String> excludedSubsidiaries;

    private Map<String, Object> metadata;

    private String reportUrl;

    private String pdfUrl;

    private String excelUrl;

    private Boolean isFinal;

    private String notes;

    private String auditTrail;

    @Builder.Default
    private List<ReportTag> tags = new ArrayList<>();

    @Builder.Default
    private List<ReportParameter> parameters = new ArrayList<>();

    public enum ReportType {
        CONSOLIDATED_BALANCE_SHEET,
        CONSOLIDATED_INCOME_STATEMENT,
        CONSOLIDATED_CASH_FLOW,
        CONSOLIDATED_EQUITY_CHANGES,
        CONSOLIDATED_FINANCIAL_STATEMENTS,
        TRIAL_BALANCE,
        INTERCOMPANY_ELIMINATION,
        CURRENCY_TRANSLATION,
        REGULATORY_REPORT,
        MANAGEMENT_REPORT,
        CUSTOM
    }

    public enum ReportStatus {
        DRAFT,
        IN_PROGRESS,
        GENERATED,
        VALIDATED,
        APPROVED,
        REJECTED,
        PUBLISHED,
        ARCHIVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSummary {
        private BigDecimal totalAssets;
        private BigDecimal totalLiabilities;
        private BigDecimal totalEquity;
        private BigDecimal totalRevenue;
        private BigDecimal totalExpenses;
        private BigDecimal netIncome;
        private BigDecimal grossProfit;
        private BigDecimal operatingIncome;
        private BigDecimal ebitda;
        private Integer subsidiaryCount;
        private Integer adjustmentCount;
        private Integer eliminationCount;
        private BigDecimal intercompanyEliminations;
        private String currencyCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceSheetData {
        private List<BalanceSheetLineItem> assets;
        private List<BalanceSheetLineItem> liabilities;
        private List<BalanceSheetLineItem> equity;
        private BigDecimal totalAssets;
        private BigDecimal totalLiabilities;
        private BigDecimal totalEquity;
        private BigDecimal workingCapital;
        private BigDecimal debtToEquityRatio;
        private BigDecimal currentRatio;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceSheetLineItem {
        private String lineCode;
        private String lineName;
        private BigDecimal amount;
        private BigDecimal priorPeriodAmount;
        private BigDecimal variance;
        private String accountType;
        private Integer hierarchyLevel;
        private Boolean isHeader;
        private Boolean isTotal;
        private List<BalanceSheetLineItem> subItems;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncomeStatementData {
        private List<IncomeStatementLineItem> revenue;
        private List<IncomeStatementLineItem> expenses;
        private BigDecimal grossProfit;
        private BigDecimal operatingIncome;
        private BigDecimal netIncome;
        private BigDecimal ebitda;
        private BigDecimal grossMargin;
        private BigDecimal operatingMargin;
        private BigDecimal netMargin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IncomeStatementLineItem {
        private String lineCode;
        private String lineName;
        private BigDecimal amount;
        private BigDecimal priorPeriodAmount;
        private BigDecimal variance;
        private BigDecimal percentageOfRevenue;
        private Integer hierarchyLevel;
        private Boolean isHeader;
        private Boolean isTotal;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashFlowData {
        private List<CashFlowLineItem> operatingActivities;
        private List<CashFlowLineItem> investingActivities;
        private List<CashFlowLineItem> financingActivities;
        private BigDecimal netCashFromOperations;
        private BigDecimal netCashFromInvesting;
        private BigDecimal netCashFromFinancing;
        private BigDecimal netChangeInCash;
        private BigDecimal beginningCash;
        private BigDecimal endingCash;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashFlowLineItem {
        private String lineCode;
        private String lineName;
        private BigDecimal amount;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquityData {
        private List<EquityLineItem> equityComponents;
        private BigDecimal beginningBalance;
        private BigDecimal netIncomeAttributable;
        private BigDecimal dividendsPaid;
        private BigDecimal otherComprehensiveIncome;
        private BigDecimal endingBalance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquityLineItem {
        private String componentType;
        private String componentName;
        private BigDecimal amount;
        private List<EquityMovement> movements;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquityMovement {
        private String movementType;
        private String description;
        private BigDecimal amount;
        private LocalDate movementDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubsidiaryData {
        private String subsidiaryId;
        private String subsidiaryName;
        private String currencyCode;
        private BigDecimal contribution;
        private BigDecimal exchangeRate;
        private BigDecimal netIncome;
        private BigDecimal totalAssets;
        private String consolidationMethod;
        private BigDecimal ownershipPercentage;
        private BigDecimal controllingInterest;
        private BigDecimal nonControllingInterest;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdjustmentReportEntry {
        private String adjustmentId;
        private String accountCode;
        private String accountName;
        private String adjustmentType;
        private BigDecimal amount;
        private String currency;
        private String description;
        private String reason;
        private String adjustedBy;
        private LocalDate adjustedOn;
        private String referenceDocument;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EliminationReportEntry {
        private String eliminationId;
        private String ruleId;
        private String entity1Id;
        private String entity1Name;
        private String entity2Id;
        private String entity2Name;
        private String accountCode;
        private BigDecimal amount;
        private String description;
        private String eliminationMethod;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntercompanyTransaction {
        private String transactionId;
        private String fromEntityId;
        private String fromEntityName;
        private String toEntityId;
        private String toEntityName;
        private String accountCode;
        private String accountName;
        private BigDecimal amount;
        private String currency;
        private LocalDate transactionDate;
        private String description;
        private Boolean isEliminated;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String errorId;
        private String severity;
        private String errorCode;
        private String errorMessage;
        private String affectedAccount;
        private BigDecimal affectedAmount;
        private String recommendation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportWarning {
        private String warningId;
        private String warningCode;
        private String warningMessage;
        private String affectedArea;
        private String recommendation;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportTag {
        private String key;
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportParameter {
        private String name;
        private Object value;
        private String description;
    }

    /**
     * Creates a new consolidation report
     */
    public static ConsolidationReport create(String tenantId, String reportName,
                                               ReportType reportType, LocalDate periodEnd,
                                               String baseCurrency, String generatedBy) {
        ConsolidationReport report = ConsolidationReport.builder()
            .reportId(generateReportId())
            .tenantId(tenantId)
            .reportName(reportName)
            .reportType(reportType)
            .status(ReportStatus.DRAFT)
            .periodEnd(periodEnd)
            .baseCurrency(baseCurrency)
            .generatedBy(generatedBy)
            .generatedAt(Instant.now())
            .version("1.0")
            .includedSubsidiaries(new ArrayList<>())
            .excludedSubsidiaries(new ArrayList<>())
            .subsidiaryData(new ArrayList<>())
            .adjustments(new ArrayList<>())
            .eliminations(new ArrayList<>())
            .intercompanyTransactions(new ArrayList<>())
            .validationErrors(new ArrayList<>())
            .warnings(new ArrayList<>())
            .tags(new ArrayList<>())
            .parameters(new ArrayList<>())
            .isFinal(false)
            .build();

        return report;
    }

    /**
     * Marks the report as generated
     */
    public void markAsGenerated() {
        this.status = ReportStatus.GENERATED;
        this.generatedAt = Instant.now();
    }

    /**
     * Validates the report
     */
    public void validate() {
        if (this.validationErrors == null || this.validationErrors.isEmpty()) {
            this.status = ReportStatus.VALIDATED;
        }
    }

    /**
     * Approves the report
     */
    public void approve(String approver) {
        this.status = ReportStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();
        this.isFinal = true;
    }

    /**
     * Rejects the report
     */
    public void reject(String reason) {
        this.status = ReportStatus.REJECTED;
        this.notes = reason;
        this.isFinal = false;
    }

    /**
     * Publishes the report
     */
    public void publish() {
        if (this.status == ReportStatus.APPROVED) {
            this.status = ReportStatus.PUBLISHED;
        }
    }

    /**
     * Adds a validation error
     */
    public void addValidationError(String errorCode, String errorMessage,
                                    String affectedAccount, String severity) {
        if (this.validationErrors == null) {
            this.validationErrors = new ArrayList<>();
        }
        ValidationError error = ValidationError.builder()
            .errorId(java.util.UUID.randomUUID().toString())
            .severity(severity)
            .errorCode(errorCode)
            .errorMessage(errorMessage)
            .affectedAccount(affectedAccount)
            .build();
        this.validationErrors.add(error);
    }

    /**
     * Adds a warning
     */
    public void addWarning(String warningCode, String warningMessage, String affectedArea) {
        if (this.warnings == null) {
            this.warnings = new ArrayList<>();
        }
        ReportWarning warning = ReportWarning.builder()
            .warningId(java.util.UUID.randomUUID().toString())
            .warningCode(warningCode)
            .warningMessage(warningMessage)
            .affectedArea(affectedArea)
            .build();
        this.warnings.add(warning);
    }

    /**
     * Adds a subsidiary
     */
    public void addSubsidiary(String subsidiaryId) {
        if (this.includedSubsidiaries == null) {
            this.includedSubsidiaries = new ArrayList<>();
        }
        if (!this.includedSubsidiaries.contains(subsidiaryId)) {
            this.includedSubsidiaries.add(subsidiaryId);
        }
    }

    /**
     * Adds a tag
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(ReportTag.builder().key(key).value(value).build());
    }

    /**
     * Adds a parameter
     */
    public void addParameter(String name, Object value, String description) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(ReportParameter.builder()
            .name(name)
            .value(value)
            .description(description)
            .build());
    }

    /**
     * Sets a metadata value
     */
    public void setMetadataValue(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Checks if report has validation errors
     */
    public boolean hasValidationErrors() {
        return this.validationErrors != null && !this.validationErrors.isEmpty();
    }

    /**
     * Checks if report has warnings
     */
    public boolean hasWarnings() {
        return this.warnings != null && !this.warnings.isEmpty();
    }

    /**
     * Checks if report can be approved
     */
    public boolean canBeApproved() {
        return this.status == ReportStatus.VALIDATED ||
               (this.status == ReportStatus.GENERATED && !hasValidationErrors());
    }

    private static String generateReportId() {
        return "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
