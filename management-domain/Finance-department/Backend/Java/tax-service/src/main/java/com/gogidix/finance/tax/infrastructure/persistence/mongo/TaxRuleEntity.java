package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.model.TaxRule;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * MongoDB Entity for TaxRule
 * Infrastructure layer - separate from domain model
 */
@Document(collection = "tax_rules")
@CompoundIndex(def = "{'tenantId': 1, 'jurisdiction': 1, 'ruleType': 1}", name = "tenant_jurisdiction_rule_idx")
public class TaxRuleEntity {

    @Id
    private String id;

    @Field("tenant_id")
    @Indexed
    private String tenantId;

    @Indexed(unique = true)
    @Field("rule_id")
    private String ruleId;

    @Field("rule_name")
    @Indexed
    private String ruleName;

    @Field("rule_type")
    @Indexed
    private TaxRule.RuleType ruleType;

    @Field("jurisdiction")
    @Indexed
    private TaxRate.Jurisdiction jurisdiction;

    @Field("tax_type")
    @Indexed
    private TaxRate.TaxType taxType;

    @Field("description")
    private String description;

    @Field("conditions")
    private List<TaxRule.RuleCondition> conditions;

    @Field("actions")
    private List<TaxRule.RuleAction> actions;

    @Field("priority")
    private Integer priority;

    @Field("effective_date")
    private LocalDate effectiveDate;

    @Field("expiry_date")
    private LocalDate expiryDate;

    @Field("is_active")
    private Boolean isActive;

    @Field("applies_to")
    private List<String> appliesTo;

    @Field("exemptions")
    private List<String> exemptions;

    @Field("thresholds")
    private Map<String, BigDecimal> thresholds;

    @Field("status")
    private TaxRule.TaxRuleStatus status;

    @Field("created_by")
    private String createdBy;

    @Field("version")
    private Integer version;

    @Field("metadata")
    private Map<String, Object> metadata;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public TaxRuleEntity() {}

    // Constructor from domain model
    public TaxRuleEntity(TaxRule domain) {
        this.id = domain.getId();
        this.tenantId = domain.getTenantId();
        this.ruleId = domain.getRuleId();
        this.ruleName = domain.getRuleName();
        this.ruleType = domain.getRuleType();
        this.jurisdiction = domain.getJurisdiction();
        this.taxType = domain.getTaxType();
        this.description = domain.getDescription();
        this.conditions = domain.getConditions();
        this.actions = domain.getActions();
        this.priority = domain.getPriority();
        this.effectiveDate = domain.getEffectiveDate();
        this.expiryDate = domain.getExpiryDate();
        this.isActive = domain.getIsActive();
        this.appliesTo = domain.getAppliesTo();
        this.exemptions = domain.getExemptions();
        this.thresholds = domain.getThresholds();
        this.status = domain.getStatus();
        this.createdBy = domain.getCreatedBy();
        this.version = domain.getVersion();
        this.metadata = domain.getMetadata();
        this.createdAt = domain.getCreatedAt();
        this.updatedAt = domain.getUpdatedAt();
    }

    // Convert to domain model
    public TaxRule toDomain() {
        return TaxRule.builder()
            .id(this.id)
            .tenantId(this.tenantId)
            .ruleId(this.ruleId)
            .ruleName(this.ruleName)
            .ruleType(this.ruleType)
            .jurisdiction(this.jurisdiction)
            .taxType(this.taxType)
            .description(this.description)
            .conditions(this.conditions)
            .actions(this.actions)
            .priority(this.priority)
            .effectiveDate(this.effectiveDate)
            .expiryDate(this.expiryDate)
            .isActive(this.isActive)
            .appliesTo(this.appliesTo)
            .exemptions(this.exemptions)
            .thresholds(this.thresholds)
            .status(this.status)
            .createdBy(this.createdBy)
            .version(this.version)
            .metadata(this.metadata)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .build();
    }

    // Static factory method
    public static TaxRuleEntity fromDomain(TaxRule domain) {
        return new TaxRuleEntity(domain);
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public TaxRule.RuleType getRuleType() { return ruleType; }
    public void setRuleType(TaxRule.RuleType ruleType) { this.ruleType = ruleType; }

    public TaxRate.Jurisdiction getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(TaxRate.Jurisdiction jurisdiction) { this.jurisdiction = jurisdiction; }

    public TaxRate.TaxType getTaxType() { return taxType; }
    public void setTaxType(TaxRate.TaxType taxType) { this.taxType = taxType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<TaxRule.RuleCondition> getConditions() { return conditions; }
    public void setConditions(List<TaxRule.RuleCondition> conditions) { this.conditions = conditions; }

    public List<TaxRule.RuleAction> getActions() { return actions; }
    public void setActions(List<TaxRule.RuleAction> actions) { this.actions = actions; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public List<String> getAppliesTo() { return appliesTo; }
    public void setAppliesTo(List<String> appliesTo) { this.appliesTo = appliesTo; }

    public List<String> getExemptions() { return exemptions; }
    public void setExemptions(List<String> exemptions) { this.exemptions = exemptions; }

    public Map<String, BigDecimal> getThresholds() { return thresholds; }
    public void setThresholds(Map<String, BigDecimal> thresholds) { this.thresholds = thresholds; }

    public TaxRule.TaxRuleStatus getStatus() { return status; }
    public void setStatus(TaxRule.TaxRuleStatus status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
