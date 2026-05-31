package com.gogidix.finance.consolidation.infrastructure.persistence.mongodb;

import com.gogidix.finance.consolidation.domain.model.ConsolidatedBalance;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MongoDB document entity for storing ConsolidatedBalance domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "consolidated_balances")
@CompoundIndex(def = "{'tenantId': 1, 'accountCode': 1, 'asOfDate': 1}")
public class ConsolidatedBalanceEntity {

    @Id
    private String id;

    @Indexed
    @Field("balance_id")
    private String balanceId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("job_id")
    private String jobId;

    @Field("consolidation_rule_id")
    private String consolidationRuleId;

    @Field("account_code")
    private String accountCode;

    @Field("account_name")
    private String accountName;

    @Field("account_type")
    private String accountType;

    @Field("balance_type")
    private String balanceType;

    @Field("as_of_date")
    private LocalDate asOfDate;

    @Field("currency_code")
    private String currencyCode;

    @Field("debit_amount")
    private BigDecimal debitAmount;

    @Field("credit_amount")
    private BigDecimal creditAmount;

    @Field("net_amount")
    private BigDecimal netAmount;

    @Field("converted_amount")
    private BigDecimal convertedAmount;

    @Field("base_currency")
    private String baseCurrency;

    @Field("exchange_rate")
    private BigDecimal exchangeRate;

    @Field("subsidiary_id")
    private String subsidiaryId;

    @Field("subsidiary_name")
    private String subsidiaryName;

    @Field("department_id")
    private String departmentId;

    @Field("department_name")
    private String departmentName;

    @Field("cost_center_id")
    private String costCenterId;

    @Field("cost_center_name")
    private String costCenterName;

    @Field("region")
    private String region;

    @Field("business_unit")
    private String businessUnit;

    @Field("product_line")
    private String productLine;

    @Field("components")
    private List<BalanceComponentEntity> components;

    @Field("adjustments")
    private List<AdjustmentEntryEntity> adjustments;

    @Field("eliminations")
    private List<EliminationEntryEntity> eliminations;

    @Field("attributes")
    private Map<String, Object> attributes;

    @Field("is_intercompany")
    private Boolean isIntercompany;

    @Field("is_adjusted")
    private Boolean isAdjusted;

    @Field("is_eliminated")
    private Boolean isEliminated;

    @Field("counterpart_entity")
    private String counterpartEntity;

    @Field("consolidation_method")
    private String consolidationMethod;

    @Field("parent_account_id")
    private String parentAccountId;

    @Field("hierarchy_level")
    private Integer hierarchyLevel;

    @Field("child_account_ids")
    private List<String> childAccountIds;

    @Field("prior_period_amount")
    private BigDecimal priorPeriodAmount;

    @Field("variance_amount")
    private BigDecimal varianceAmount;

    @Field("variance_percentage")
    private BigDecimal variancePercentage;

    @Field("notes")
    private String notes;

    @Field("audit_trail")
    private String auditTrail;

    @Field("tags")
    private List<BalanceTagEntity> tags;

    // Default constructor for MongoDB
    public ConsolidatedBalanceEntity() {}

    // Constructor from domain model
    public ConsolidatedBalanceEntity(ConsolidatedBalance balance) {
        this.balanceId = balance.getBalanceId();
        this.tenantId = balance.getTenantId();
        this.jobId = balance.getJobId();
        this.consolidationRuleId = balance.getConsolidationRuleId();
        this.accountCode = balance.getAccountCode();
        this.accountName = balance.getAccountName();
        this.accountType = balance.getAccountType() != null ? balance.getAccountType().name() : null;
        this.balanceType = balance.getBalanceType() != null ? balance.getBalanceType().name() : null;
        this.asOfDate = balance.getAsOfDate();
        this.currencyCode = balance.getCurrencyCode();
        this.debitAmount = balance.getDebitAmount();
        this.creditAmount = balance.getCreditAmount();
        this.netAmount = balance.getNetAmount();
        this.convertedAmount = balance.getConvertedAmount();
        this.baseCurrency = balance.getBaseCurrency();
        this.exchangeRate = balance.getExchangeRate();
        this.subsidiaryId = balance.getSubsidiaryId();
        this.subsidiaryName = balance.getSubsidiaryName();
        this.departmentId = balance.getDepartmentId();
        this.departmentName = balance.getDepartmentName();
        this.costCenterId = balance.getCostCenterId();
        this.costCenterName = balance.getCostCenterName();
        this.region = balance.getRegion();
        this.businessUnit = balance.getBusinessUnit();
        this.productLine = balance.getProductLine();
        this.components = balance.getComponents().stream()
                .map(BalanceComponentEntity::new)
                .collect(Collectors.toList());
        this.adjustments = balance.getAdjustments().stream()
                .map(AdjustmentEntryEntity::new)
                .collect(Collectors.toList());
        this.eliminations = balance.getEliminations().stream()
                .map(EliminationEntryEntity::new)
                .collect(Collectors.toList());
        this.attributes = balance.getAttributes();
        this.isIntercompany = balance.getIsIntercompany();
        this.isAdjusted = balance.getIsAdjusted();
        this.isEliminated = balance.getIsEliminated();
        this.counterpartEntity = balance.getCounterpartEntity();
        this.consolidationMethod = balance.getConsolidationMethod();
        this.parentAccountId = balance.getParentAccountId();
        this.hierarchyLevel = balance.getHierarchyLevel();
        this.childAccountIds = balance.getChildAccountIds();
        this.priorPeriodAmount = balance.getPriorPeriodAmount();
        this.varianceAmount = balance.getVarianceAmount();
        this.variancePercentage = balance.getVariancePercentage();
        this.notes = balance.getNotes();
        this.auditTrail = balance.getAuditTrail();
        this.tags = balance.getTags().stream()
                .map(BalanceTagEntity::new)
                .collect(Collectors.toList());
    }

    // Convert to domain model
    public ConsolidatedBalance toDomainModel() {
        return ConsolidatedBalance.builder()
                .balanceId(this.balanceId)
                .tenantId(this.tenantId)
                .jobId(this.jobId)
                .consolidationRuleId(this.consolidationRuleId)
                .accountCode(this.accountCode)
                .accountName(this.accountName)
                .accountType(this.accountType != null ? ConsolidatedBalance.AccountType.valueOf(this.accountType) : null)
                .balanceType(this.balanceType != null ? ConsolidatedBalance.BalanceType.valueOf(this.balanceType) : null)
                .asOfDate(this.asOfDate)
                .currencyCode(this.currencyCode)
                .debitAmount(this.debitAmount)
                .creditAmount(this.creditAmount)
                .netAmount(this.netAmount)
                .convertedAmount(this.convertedAmount)
                .baseCurrency(this.baseCurrency)
                .exchangeRate(this.exchangeRate)
                .subsidiaryId(this.subsidiaryId)
                .subsidiaryName(this.subsidiaryName)
                .departmentId(this.departmentId)
                .departmentName(this.departmentName)
                .costCenterId(this.costCenterId)
                .costCenterName(this.costCenterName)
                .region(this.region)
                .businessUnit(this.businessUnit)
                .productLine(this.productLine)
                .components(this.components != null ? this.components.stream()
                        .map(BalanceComponentEntity::toDomainModel)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .adjustments(this.adjustments != null ? this.adjustments.stream()
                        .map(AdjustmentEntryEntity::toDomainModel)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .eliminations(this.eliminations != null ? this.eliminations.stream()
                        .map(EliminationEntryEntity::toDomainModel)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .attributes(this.attributes != null ? new HashMap<>(this.attributes) : new HashMap<>())
                .isIntercompany(this.isIntercompany)
                .isAdjusted(this.isAdjusted)
                .isEliminated(this.isEliminated)
                .counterpartEntity(this.counterpartEntity)
                .consolidationMethod(this.consolidationMethod)
                .parentAccountId(this.parentAccountId)
                .hierarchyLevel(this.hierarchyLevel)
                .childAccountIds(this.childAccountIds != null ? new ArrayList<>(this.childAccountIds) : new ArrayList<>())
                .priorPeriodAmount(this.priorPeriodAmount)
                .varianceAmount(this.varianceAmount)
                .variancePercentage(this.variancePercentage)
                .notes(this.notes)
                .auditTrail(this.auditTrail)
                .tags(this.tags != null ? this.tags.stream()
                        .map(BalanceTagEntity::toDomainModel)
                        .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    // Getters and setters for MongoDB
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBalanceId() { return balanceId; }
    public void setBalanceId(String balanceId) { this.balanceId = balanceId; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    // Nested entity classes
    public static class BalanceComponentEntity {
        private String componentId;
        private String sourceEntityId;
        private String sourceEntityName;
        private String accountCode;
        private BigDecimal amount;
        private String currency;
        private BigDecimal exchangeRate;
        private BigDecimal convertedAmount;
        private String type;

        public BalanceComponentEntity() {}

        public BalanceComponentEntity(ConsolidatedBalance.BalanceComponent component) {
            this.componentId = component.getComponentId();
            this.sourceEntityId = component.getSourceEntityId();
            this.sourceEntityName = component.getSourceEntityName();
            this.accountCode = component.getAccountCode();
            this.amount = component.getAmount();
            this.currency = component.getCurrency();
            this.exchangeRate = component.getExchangeRate();
            this.convertedAmount = component.getConvertedAmount();
            this.type = component.getType();
        }

        public ConsolidatedBalance.BalanceComponent toDomainModel() {
            return ConsolidatedBalance.BalanceComponent.builder()
                    .componentId(this.componentId)
                    .sourceEntityId(this.sourceEntityId)
                    .sourceEntityName(this.sourceEntityName)
                    .accountCode(this.accountCode)
                    .amount(this.amount)
                    .currency(this.currency)
                    .exchangeRate(this.exchangeRate)
                    .convertedAmount(this.convertedAmount)
                    .type(this.type)
                    .build();
        }

        public String getComponentId() { return componentId; }
        public void setComponentId(String componentId) { this.componentId = componentId; }
        public String getSourceEntityId() { return sourceEntityId; }
        public void setSourceEntityId(String sourceEntityId) { this.sourceEntityId = sourceEntityId; }
        public String getSourceEntityName() { return sourceEntityName; }
        public void setSourceEntityName(String sourceEntityName) { this.sourceEntityName = sourceEntityName; }
        public String getAccountCode() { return accountCode; }
        public void setAccountCode(String accountCode) { this.accountCode = accountCode; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public BigDecimal getExchangeRate() { return exchangeRate; }
        public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
        public BigDecimal getConvertedAmount() { return convertedAmount; }
        public void setConvertedAmount(BigDecimal convertedAmount) { this.convertedAmount = convertedAmount; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    public static class AdjustmentEntryEntity {
        private String adjustmentId;
        private String adjustmentType;
        private String description;
        private BigDecimal amount;
        private String reason;
        private String adjustedBy;
        private LocalDate adjustedOn;
        private String referenceDocument;

        public AdjustmentEntryEntity() {}

        public AdjustmentEntryEntity(ConsolidatedBalance.AdjustmentEntry adjustment) {
            this.adjustmentId = adjustment.getAdjustmentId();
            this.adjustmentType = adjustment.getAdjustmentType();
            this.description = adjustment.getDescription();
            this.amount = adjustment.getAmount();
            this.reason = adjustment.getReason();
            this.adjustedBy = adjustment.getAdjustedBy();
            this.adjustedOn = adjustment.getAdjustedOn();
            this.referenceDocument = adjustment.getReferenceDocument();
        }

        public ConsolidatedBalance.AdjustmentEntry toDomainModel() {
            return ConsolidatedBalance.AdjustmentEntry.builder()
                    .adjustmentId(this.adjustmentId)
                    .adjustmentType(this.adjustmentType)
                    .description(this.description)
                    .amount(this.amount)
                    .reason(this.reason)
                    .adjustedBy(this.adjustedBy)
                    .adjustedOn(this.adjustedOn)
                    .referenceDocument(this.referenceDocument)
                    .build();
        }

        public String getAdjustmentId() { return adjustmentId; }
        public void setAdjustmentId(String adjustmentId) { this.adjustmentId = adjustmentId; }
        public String getAdjustmentType() { return adjustmentType; }
        public void setAdjustmentType(String adjustmentType) { this.adjustmentType = adjustmentType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getAdjustedBy() { return adjustedBy; }
        public void setAdjustedBy(String adjustedBy) { this.adjustedBy = adjustedBy; }
        public LocalDate getAdjustedOn() { return adjustedOn; }
        public void setAdjustedOn(LocalDate adjustedOn) { this.adjustedOn = adjustedOn; }
        public String getReferenceDocument() { return referenceDocument; }
        public void setReferenceDocument(String referenceDocument) { this.referenceDocument = referenceDocument; }
    }

    public static class EliminationEntryEntity {
        private String eliminationId;
        private String eliminationRuleId;
        private String counterpartEntityId;
        private String counterpartAccountCode;
        private BigDecimal amount;
        private String description;
        private LocalDate eliminationDate;

        public EliminationEntryEntity() {}

        public EliminationEntryEntity(ConsolidatedBalance.EliminationEntry elimination) {
            this.eliminationId = elimination.getEliminationId();
            this.eliminationRuleId = elimination.getEliminationRuleId();
            this.counterpartEntityId = elimination.getCounterpartEntityId();
            this.counterpartAccountCode = elimination.getCounterpartAccountCode();
            this.amount = elimination.getAmount();
            this.description = elimination.getDescription();
            this.eliminationDate = elimination.getEliminationDate();
        }

        public ConsolidatedBalance.EliminationEntry toDomainModel() {
            return ConsolidatedBalance.EliminationEntry.builder()
                    .eliminationId(this.eliminationId)
                    .eliminationRuleId(this.eliminationRuleId)
                    .counterpartEntityId(this.counterpartEntityId)
                    .counterpartAccountCode(this.counterpartAccountCode)
                    .amount(this.amount)
                    .description(this.description)
                    .eliminationDate(this.eliminationDate)
                    .build();
        }

        public String getEliminationId() { return eliminationId; }
        public void setEliminationId(String eliminationId) { this.eliminationId = eliminationId; }
        public String getEliminationRuleId() { return eliminationRuleId; }
        public void setEliminationRuleId(String eliminationRuleId) { this.eliminationRuleId = eliminationRuleId; }
        public String getCounterpartEntityId() { return counterpartEntityId; }
        public void setCounterpartEntityId(String counterpartEntityId) { this.counterpartEntityId = counterpartEntityId; }
        public String getCounterpartAccountCode() { return counterpartAccountCode; }
        public void setCounterpartAccountCode(String counterpartAccountCode) { this.counterpartAccountCode = counterpartAccountCode; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDate getEliminationDate() { return eliminationDate; }
        public void setEliminationDate(LocalDate eliminationDate) { this.eliminationDate = eliminationDate; }
    }

    public static class BalanceTagEntity {
        private String key;
        private String value;

        public BalanceTagEntity() {}

        public BalanceTagEntity(ConsolidatedBalance.BalanceTag tag) {
            this.key = tag.getKey();
            this.value = tag.getValue();
        }

        public ConsolidatedBalance.BalanceTag toDomainModel() {
            return ConsolidatedBalance.BalanceTag.builder()
                    .key(this.key)
                    .value(this.value)
                    .build();
        }

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
