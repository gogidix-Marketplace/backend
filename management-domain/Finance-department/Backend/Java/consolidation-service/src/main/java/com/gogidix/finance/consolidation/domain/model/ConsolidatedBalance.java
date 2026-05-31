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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Consolidated Balance Domain Entity
 * Represents consolidated financial balances across entities
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "consolidated_balances")
@CompoundIndex(def = "{'tenantId': 1, 'accountCode': 1, 'asOfDate': 1}")
public class ConsolidatedBalance extends BaseEntity {

    @Indexed
    private String balanceId;

    @Indexed
    private String tenantId;

    private String jobId;

    private String consolidationRuleId;

    private String accountCode;

    private String accountName;

    private AccountType accountType;

    private BalanceType balanceType;

    private LocalDate asOfDate;

    private String currencyCode;

    private BigDecimal debitAmount;

    private BigDecimal creditAmount;

    private BigDecimal netAmount;

    private BigDecimal convertedAmount;

    private String baseCurrency;

    private BigDecimal exchangeRate;

    private String subsidiaryId;

    private String subsidiaryName;

    private String departmentId;

    private String departmentName;

    private String costCenterId;

    private String costCenterName;

    private String region;

    private String businessUnit;

    private String productLine;

    private List<BalanceComponent> components;

    private List<AdjustmentEntry> adjustments;

    private List<EliminationEntry> eliminations;

    private Map<String, Object> attributes;

    private Boolean isIntercompany;

    private Boolean isAdjusted;

    private Boolean isEliminated;

    private String counterpartEntity;

    private String consolidationMethod;

    private String parentAccountId;

    private Integer hierarchyLevel;

    private List<String> childAccountIds;

    private BigDecimal priorPeriodAmount;

    private BigDecimal varianceAmount;

    private BigDecimal variancePercentage;

    private String notes;

    private String auditTrail;

    @Builder.Default
    private List<BalanceTag> tags = new ArrayList<>();

    public enum AccountType {
        ASSET,
        LIABILITY,
        EQUITY,
        REVENUE,
        EXPENSE,
        GAIN,
        LOSS,
        CONTRA_ASSET,
        CONTRA_LIABILITY,
        OTHER
    }

    public enum BalanceType {
        DEBIT,
        CREDIT,
        ZERO,
        MIXED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceComponent {
        private String componentId;
        private String sourceEntityId;
        private String sourceEntityName;
        private String accountCode;
        private BigDecimal amount;
        private String currency;
        private BigDecimal exchangeRate;
        private BigDecimal convertedAmount;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdjustmentEntry {
        private String adjustmentId;
        private String adjustmentType;
        private String description;
        private BigDecimal amount;
        private String reason;
        private String adjustedBy;
        private LocalDate adjustedOn;
        private String referenceDocument;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EliminationEntry {
        private String eliminationId;
        private String eliminationRuleId;
        private String counterpartEntityId;
        private String counterpartAccountCode;
        private BigDecimal amount;
        private String description;
        private LocalDate eliminationDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceTag {
        private String key;
        private String value;
    }

    /**
     * Creates a new consolidated balance
     */
    public static ConsolidatedBalance create(String tenantId, String accountCode,
                                               String accountName, AccountType accountType,
                                               LocalDate asOfDate, String currencyCode) {
        ConsolidatedBalance balance = ConsolidatedBalance.builder()
            .balanceId(generateBalanceId())
            .tenantId(tenantId)
            .accountCode(accountCode)
            .accountName(accountName)
            .accountType(accountType)
            .asOfDate(asOfDate)
            .currencyCode(currencyCode)
            .debitAmount(BigDecimal.ZERO)
            .creditAmount(BigDecimal.ZERO)
            .netAmount(BigDecimal.ZERO)
            .convertedAmount(BigDecimal.ZERO)
            .isIntercompany(false)
            .isAdjusted(false)
            .isEliminated(false)
            .hierarchyLevel(0)
            .components(new ArrayList<>())
            .adjustments(new ArrayList<>())
            .eliminations(new ArrayList<>())
            .attributes(new HashMap<>())
            .tags(new ArrayList<>())
            .childAccountIds(new ArrayList<>())
            .build();

        return balance;
    }

    /**
     * Adds a debit amount
     */
    public void addDebit(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.debitAmount = this.debitAmount.add(amount);
            recalculateNetAmount();
        }
    }

    /**
     * Adds a credit amount
     */
    public void addCredit(BigDecimal amount) {
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.creditAmount = this.creditAmount.add(amount);
            recalculateNetAmount();
        }
    }

    /**
     * Adds a component balance
     */
    public void addComponent(BalanceComponent component) {
        if (this.components == null) {
            this.components = new ArrayList<>();
        }
        this.components.add(component);

        // Add the component amount to the balance
        if (component.getAmount() != null) {
            BigDecimal amount = component.getConvertedAmount() != null ?
                component.getConvertedAmount() : component.getAmount();
            addDebit(amount);
        }
    }

    /**
     * Adds an adjustment
     */
    public void addAdjustment(AdjustmentEntry adjustment) {
        if (this.adjustments == null) {
            this.adjustments = new ArrayList<>();
        }
        this.adjustments.add(adjustment);
        this.isAdjusted = true;

        if (adjustment.getAmount() != null) {
            this.netAmount = this.netAmount.add(adjustment.getAmount());
        }
    }

    /**
     * Adds an elimination entry
     */
    public void addElimination(EliminationEntry elimination) {
        if (this.eliminations == null) {
            this.eliminations = new ArrayList<>();
        }
        this.eliminations.add(elimination);
        this.isEliminated = true;

        if (elimination.getAmount() != null) {
            this.netAmount = this.netAmount.subtract(elimination.getAmount());
        }
    }

    /**
     * Converts amount to base currency
     */
    public void convertToBaseCurrency(String baseCurrency, BigDecimal exchangeRate) {
        this.baseCurrency = baseCurrency;
        this.exchangeRate = exchangeRate;

        if (this.netAmount != null && exchangeRate != null &&
            exchangeRate.compareTo(BigDecimal.ZERO) > 0) {
            this.convertedAmount = this.netAmount.multiply(exchangeRate);
        }
    }

    /**
     * Calculates variance from prior period
     */
    public void calculateVariance() {
        if (this.priorPeriodAmount != null && this.netAmount != null) {
            this.varianceAmount = this.netAmount.subtract(this.priorPeriodAmount);

            if (this.priorPeriodAmount.compareTo(BigDecimal.ZERO) != 0) {
                this.variancePercentage = this.varianceAmount
                    .divide(this.priorPeriodAmount, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            }
        }
    }

    /**
     * Sets prior period amount and calculates variance
     */
    public void setPriorPeriodAmount(BigDecimal priorAmount) {
        this.priorPeriodAmount = priorAmount;
        calculateVariance();
    }

    /**
     * Adds a tag
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(BalanceTag.builder().key(key).value(value).build());
    }

    /**
     * Sets an attribute
     */
    public void setAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
    }

    /**
     * Marks as intercompany balance
     */
    public void markAsIntercompany(String counterpartEntity) {
        this.isIntercompany = true;
        this.counterpartEntity = counterpartEntity;
    }

    /**
     * Adds a child account
     */
    public void addChildAccount(String childAccountId) {
        if (this.childAccountIds == null) {
            this.childAccountIds = new ArrayList<>();
        }
        if (!this.childAccountIds.contains(childAccountId)) {
            this.childAccountIds.add(childAccountId);
        }
    }

    /**
     * Sets hierarchy level
     */
    public void setHierarchyLevel(Integer level) {
        this.hierarchyLevel = level != null ? level : 0;
    }

    /**
     * Checks if balance is balanced (debits equal credits)
     */
    public boolean isBalanced() {
        if (this.debitAmount == null || this.creditAmount == null) {
            return true;
        }
        return this.debitAmount.compareTo(this.creditAmount) == 0;
    }

    /**
     * Gets absolute amount
     */
    public BigDecimal getAbsoluteAmount() {
        if (this.netAmount == null) {
            return BigDecimal.ZERO;
        }
        return this.netAmount.abs();
    }

    /**
     * Determines if this is a debit balance account
     */
    public boolean isDebitAccount() {
        return this.accountType == AccountType.ASSET ||
               this.accountType == AccountType.EXPENSE ||
               this.accountType == AccountType.GAIN ||
               this.accountType == AccountType.CONTRA_LIABILITY;
    }

    /**
     * Determines if this is a credit balance account
     */
    public boolean isCreditAccount() {
        return this.accountType == AccountType.LIABILITY ||
               this.accountType == AccountType.EQUITY ||
               this.accountType == AccountType.REVENUE ||
               this.accountType == AccountType.LOSS ||
               this.accountType == AccountType.CONTRA_ASSET;
    }

    private void recalculateNetAmount() {
        this.netAmount = this.debitAmount.subtract(this.creditAmount);
    }

    private static String generateBalanceId() {
        return "BAL-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
