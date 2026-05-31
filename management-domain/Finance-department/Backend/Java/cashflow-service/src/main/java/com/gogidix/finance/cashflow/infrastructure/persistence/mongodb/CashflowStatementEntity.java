package com.gogidix.finance.cashflow.infrastructure.persistence.mongodb;

import com.gogidix.finance.cashflow.domain.model.CashflowStatement;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing CashflowStatement domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "cashflow_statements")
public class CashflowStatementEntity {

    @Id
    private String id;

    @Indexed
    @Field("statement_id")
    private String statementId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("statement_type")
    private String statementType;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("period")
    private String period;

    @Field("status")
    private String status;

    @Field("generated_by")
    private String generatedBy;

    @Field("generated_at")
    private Instant generatedAt;

    @Field("last_updated")
    private Instant lastUpdated;

    @Field("beginning_cash")
    private BigDecimal beginningCash;

    @Field("ending_cash")
    private BigDecimal endingCash;

    @Field("net_cash_increase_decrease")
    private BigDecimal netCashIncreaseDecrease;

    @Field("operating_activities")
    private CashflowSectionEmbed operatingActivities;

    @Field("investing_activities")
    private CashflowSectionEmbed investingActivities;

    @Field("financing_activities")
    private CashflowSectionEmbed financingActivities;

    @Field("line_items")
    private List<StatementItemEmbed> lineItems;

    @Field("currency")
    private String currency;

    @Field("fiscal_year")
    private Integer fiscalYear;

    @Field("fiscal_period")
    private Integer fiscalPeriod;

    @Field("is_consolidated")
    private Boolean isConsolidated;

    @Field("subsidiary_ids")
    private List<String> subsidiaryIds;

    @Field("parent_statement_id")
    private String parentStatementId;

    @Field("prior_period_cash")
    private BigDecimal priorPeriodCash;

    @Field("cash_change_percentage")
    private BigDecimal cashChangePercentage;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("approval_status")
    private String approvalStatus;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("metadata")
    private List<StatementMetadataEmbed> metadata;

    // Default constructor for MongoDB
    public CashflowStatementEntity() {
    }

    // Constructor from domain model
    public CashflowStatementEntity(CashflowStatement statement) {
        this.statementId = statement.getStatementId();
        this.tenantId = statement.getTenantId();
        this.name = statement.getName();
        this.description = statement.getDescription();
        this.statementType = statement.getStatementType() != null ? statement.getStatementType().name() : null;
        this.startDate = statement.getStartDate();
        this.endDate = statement.getEndDate();
        this.period = statement.getPeriod() != null ? statement.getPeriod().name() : null;
        this.status = statement.getStatus() != null ? statement.getStatus().name() : null;
        this.generatedBy = statement.getGeneratedBy();
        this.generatedAt = statement.getGeneratedAt();
        this.lastUpdated = statement.getLastUpdated();
        this.beginningCash = statement.getBeginningCash();
        this.endingCash = statement.getEndingCash();
        this.netCashIncreaseDecrease = statement.getNetCashIncreaseDecrease();
        this.operatingActivities = statement.getOperatingActivities() != null ? new CashflowSectionEmbed(statement.getOperatingActivities()) : null;
        this.investingActivities = statement.getInvestingActivities() != null ? new CashflowSectionEmbed(statement.getInvestingActivities()) : null;
        this.financingActivities = statement.getFinancingActivities() != null ? new CashflowSectionEmbed(statement.getFinancingActivities()) : null;
        this.currency = statement.getCurrency();
        this.fiscalYear = statement.getFiscalYear();
        this.fiscalPeriod = statement.getFiscalPeriod();
        this.isConsolidated = statement.getIsConsolidated();
        this.subsidiaryIds = statement.getSubsidiaryIds() != null ? new ArrayList<>(statement.getSubsidiaryIds()) : new ArrayList<>();
        this.parentStatementId = statement.getParentStatementId();
        this.priorPeriodCash = statement.getPriorPeriodCash();
        this.cashChangePercentage = statement.getCashChangePercentage();
        this.tags = statement.getTags() != null ? new ArrayList<>(statement.getTags()) : new ArrayList<>();
        this.notes = statement.getNotes();
        this.approvalStatus = statement.getApprovalStatus();
        this.approvedBy = statement.getApprovedBy();
        this.approvedAt = statement.getApprovedAt();

        // Convert line items
        if (statement.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (CashflowStatement.StatementItem item : statement.getLineItems()) {
                this.lineItems.add(new StatementItemEmbed(item));
            }
        }

        // Convert metadata
        if (statement.getMetadata() != null) {
            this.metadata = new ArrayList<>();
            for (CashflowStatement.StatementMetadata meta : statement.getMetadata()) {
                this.metadata.add(new StatementMetadataEmbed(meta));
            }
        }
    }

    // Convert to domain model
    public CashflowStatement toDomainModel() {
        List<CashflowStatement.StatementItem> lineItemList = new ArrayList<>();
        if (this.lineItems != null) {
            for (StatementItemEmbed embed : this.lineItems) {
                lineItemList.add(embed.toDomainModel());
            }
        }

        List<CashflowStatement.StatementMetadata> metadataList = new ArrayList<>();
        if (this.metadata != null) {
            for (StatementMetadataEmbed embed : this.metadata) {
                metadataList.add(embed.toDomainModel());
            }
        }

        return CashflowStatement.builder()
                .statementId(this.statementId)
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .statementType(this.statementType != null ? CashflowStatement.StatementType.valueOf(this.statementType) : null)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .period(this.period != null ? CashflowStatement.StatementPeriod.valueOf(this.period) : null)
                .status(this.status != null ? CashflowStatement.StatementStatus.valueOf(this.status) : null)
                .generatedBy(this.generatedBy)
                .generatedAt(this.generatedAt)
                .lastUpdated(this.lastUpdated)
                .beginningCash(this.beginningCash)
                .endingCash(this.endingCash)
                .netCashIncreaseDecrease(this.netCashIncreaseDecrease)
                .operatingActivities(this.operatingActivities != null ? this.operatingActivities.toDomainModel() : null)
                .investingActivities(this.investingActivities != null ? this.investingActivities.toDomainModel() : null)
                .financingActivities(this.financingActivities != null ? this.financingActivities.toDomainModel() : null)
                .lineItems(lineItemList)
                .currency(this.currency)
                .fiscalYear(this.fiscalYear)
                .fiscalPeriod(this.fiscalPeriod)
                .isConsolidated(this.isConsolidated)
                .subsidiaryIds(this.subsidiaryIds != null ? new ArrayList<>(this.subsidiaryIds) : new ArrayList<>())
                .parentStatementId(this.parentStatementId)
                .priorPeriodCash(this.priorPeriodCash)
                .cashChangePercentage(this.cashChangePercentage)
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .notes(this.notes)
                .approvalStatus(this.approvalStatus)
                .approvedBy(this.approvedBy)
                .approvedAt(this.approvedAt)
                .metadata(metadataList)
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(CashflowStatement statement) {
        this.name = statement.getName();
        this.description = statement.getDescription();
        this.statementType = statement.getStatementType() != null ? statement.getStatementType().name() : null;
        this.startDate = statement.getStartDate();
        this.endDate = statement.getEndDate();
        this.period = statement.getPeriod() != null ? statement.getPeriod().name() : null;
        this.status = statement.getStatus() != null ? statement.getStatus().name() : null;
        this.generatedBy = statement.getGeneratedBy();
        this.generatedAt = statement.getGeneratedAt();
        this.lastUpdated = statement.getLastUpdated();
        this.beginningCash = statement.getBeginningCash();
        this.endingCash = statement.getEndingCash();
        this.netCashIncreaseDecrease = statement.getNetCashIncreaseDecrease();
        this.operatingActivities = statement.getOperatingActivities() != null ? new CashflowSectionEmbed(statement.getOperatingActivities()) : null;
        this.investingActivities = statement.getInvestingActivities() != null ? new CashflowSectionEmbed(statement.getInvestingActivities()) : null;
        this.financingActivities = statement.getFinancingActivities() != null ? new CashflowSectionEmbed(statement.getFinancingActivities()) : null;
        this.currency = statement.getCurrency();
        this.fiscalYear = statement.getFiscalYear();
        this.fiscalPeriod = statement.getFiscalPeriod();
        this.isConsolidated = statement.getIsConsolidated();
        this.subsidiaryIds = statement.getSubsidiaryIds() != null ? new ArrayList<>(statement.getSubsidiaryIds()) : new ArrayList<>();
        this.parentStatementId = statement.getParentStatementId();
        this.priorPeriodCash = statement.getPriorPeriodCash();
        this.cashChangePercentage = statement.getCashChangePercentage();
        this.tags = statement.getTags() != null ? new ArrayList<>(statement.getTags()) : new ArrayList<>();
        this.notes = statement.getNotes();
        this.approvalStatus = statement.getApprovalStatus();
        this.approvedBy = statement.getApprovedBy();
        this.approvedAt = statement.getApprovedAt();

        // Update line items
        if (statement.getLineItems() != null) {
            this.lineItems = new ArrayList<>();
            for (CashflowStatement.StatementItem item : statement.getLineItems()) {
                this.lineItems.add(new StatementItemEmbed(item));
            }
        }

        // Update metadata
        if (statement.getMetadata() != null) {
            this.metadata = new ArrayList<>();
            for (CashflowStatement.StatementMetadata meta : statement.getMetadata()) {
                this.metadata.add(new StatementMetadataEmbed(meta));
            }
        }
    }

    // Embedded class for CashflowSection
    public static class CashflowSectionEmbed {
        private BigDecimal totalInflow;
        private BigDecimal totalOutflow;
        private BigDecimal netCashflow;
        private List<StatementItemEmbed> items;

        public CashflowSectionEmbed() {
        }

        public CashflowSectionEmbed(CashflowStatement.CashflowSection section) {
            this.totalInflow = section.getTotalInflow();
            this.totalOutflow = section.getTotalOutflow();
            this.netCashflow = section.getNetCashflow();
            if (section.getItems() != null) {
                this.items = new ArrayList<>();
                for (CashflowStatement.StatementItem item : section.getItems()) {
                    this.items.add(new StatementItemEmbed(item));
                }
            }
        }

        public CashflowStatement.CashflowSection toDomainModel() {
            List<CashflowStatement.StatementItem> itemsList = new ArrayList<>();
            if (this.items != null) {
                for (StatementItemEmbed embed : this.items) {
                    itemsList.add(embed.toDomainModel());
                }
            }
            return CashflowStatement.CashflowSection.builder()
                    .totalInflow(this.totalInflow)
                    .totalOutflow(this.totalOutflow)
                    .netCashflow(this.netCashflow)
                    .items(itemsList)
                    .build();
        }

        // Getters and setters
        public BigDecimal getTotalInflow() {
            return totalInflow;
        }

        public void setTotalInflow(BigDecimal totalInflow) {
            this.totalInflow = totalInflow;
        }

        public BigDecimal getTotalOutflow() {
            return totalOutflow;
        }

        public void setTotalOutflow(BigDecimal totalOutflow) {
            this.totalOutflow = totalOutflow;
        }

        public BigDecimal getNetCashflow() {
            return netCashflow;
        }

        public void setNetCashflow(BigDecimal netCashflow) {
            this.netCashflow = netCashflow;
        }

        public List<StatementItemEmbed> getItems() {
            return items;
        }

        public void setItems(List<StatementItemEmbed> items) {
            this.items = items;
        }
    }

    // Embedded class for StatementItem
    public static class StatementItemEmbed {
        private String itemId;
        private String code;
        private String description;
        private BigDecimal amount;
        private String type;
        private String category;
        private Integer sequence;
        private Boolean isSubtotal;
        private String parentItemId;

        public StatementItemEmbed() {
        }

        public StatementItemEmbed(CashflowStatement.StatementItem item) {
            this.itemId = item.getItemId();
            this.code = item.getCode();
            this.description = item.getDescription();
            this.amount = item.getAmount();
            this.type = item.getType() != null ? item.getType().name() : null;
            this.category = item.getCategory();
            this.sequence = item.getSequence();
            this.isSubtotal = item.getIsSubtotal();
            this.parentItemId = item.getParentItemId();
        }

        public CashflowStatement.StatementItem toDomainModel() {
            return CashflowStatement.StatementItem.builder()
                    .itemId(this.itemId)
                    .code(this.code)
                    .description(this.description)
                    .amount(this.amount)
                    .type(this.type != null ? CashflowStatement.ItemType.valueOf(this.type) : null)
                    .category(this.category)
                    .sequence(this.sequence)
                    .isSubtotal(this.isSubtotal)
                    .parentItemId(this.parentItemId)
                    .build();
        }

        // Getters and setters
        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Integer getSequence() {
            return sequence;
        }

        public void setSequence(Integer sequence) {
            this.sequence = sequence;
        }

        public Boolean getIsSubtotal() {
            return isSubtotal;
        }

        public void setIsSubtotal(Boolean isSubtotal) {
            this.isSubtotal = isSubtotal;
        }

        public String getParentItemId() {
            return parentItemId;
        }

        public void setParentItemId(String parentItemId) {
            this.parentItemId = parentItemId;
        }
    }

    // Embedded class for StatementMetadata
    public static class StatementMetadataEmbed {
        private String key;
        private String value;
        private String type;

        public StatementMetadataEmbed() {
        }

        public StatementMetadataEmbed(CashflowStatement.StatementMetadata metadata) {
            this.key = metadata.getKey();
            this.value = metadata.getValue();
            this.type = metadata.getType();
        }

        public CashflowStatement.StatementMetadata toDomainModel() {
            return CashflowStatement.StatementMetadata.builder()
                    .key(this.key)
                    .value(this.value)
                    .type(this.type)
                    .build();
        }

        // Getters and setters
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getBeginningCash() {
        return beginningCash;
    }

    public void setBeginningCash(BigDecimal beginningCash) {
        this.beginningCash = beginningCash;
    }

    public BigDecimal getEndingCash() {
        return endingCash;
    }

    public void setEndingCash(BigDecimal endingCash) {
        this.endingCash = endingCash;
    }

    public BigDecimal getNetCashIncreaseDecrease() {
        return netCashIncreaseDecrease;
    }

    public void setNetCashIncreaseDecrease(BigDecimal netCashIncreaseDecrease) {
        this.netCashIncreaseDecrease = netCashIncreaseDecrease;
    }

    public CashflowSectionEmbed getOperatingActivities() {
        return operatingActivities;
    }

    public void setOperatingActivities(CashflowSectionEmbed operatingActivities) {
        this.operatingActivities = operatingActivities;
    }

    public CashflowSectionEmbed getInvestingActivities() {
        return investingActivities;
    }

    public void setInvestingActivities(CashflowSectionEmbed investingActivities) {
        this.investingActivities = investingActivities;
    }

    public CashflowSectionEmbed getFinancingActivities() {
        return financingActivities;
    }

    public void setFinancingActivities(CashflowSectionEmbed financingActivities) {
        this.financingActivities = financingActivities;
    }

    public List<StatementItemEmbed> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<StatementItemEmbed> lineItems) {
        this.lineItems = lineItems;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Integer fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Integer getFiscalPeriod() {
        return fiscalPeriod;
    }

    public void setFiscalPeriod(Integer fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

    public Boolean getIsConsolidated() {
        return isConsolidated;
    }

    public void setIsConsolidated(Boolean isConsolidated) {
        this.isConsolidated = isConsolidated;
    }

    public List<String> getSubsidiaryIds() {
        return subsidiaryIds;
    }

    public void setSubsidiaryIds(List<String> subsidiaryIds) {
        this.subsidiaryIds = subsidiaryIds;
    }

    public String getParentStatementId() {
        return parentStatementId;
    }

    public void setParentStatementId(String parentStatementId) {
        this.parentStatementId = parentStatementId;
    }

    public BigDecimal getPriorPeriodCash() {
        return priorPeriodCash;
    }

    public void setPriorPeriodCash(BigDecimal priorPeriodCash) {
        this.priorPeriodCash = priorPeriodCash;
    }

    public BigDecimal getCashChangePercentage() {
        return cashChangePercentage;
    }

    public void setCashChangePercentage(BigDecimal cashChangePercentage) {
        this.cashChangePercentage = cashChangePercentage;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public List<StatementMetadataEmbed> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<StatementMetadataEmbed> metadata) {
        this.metadata = metadata;
    }
}
