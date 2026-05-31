package com.gogidix.courier.commissionservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "commission_rules")
@CompoundIndex(name = "idx_partner_priority", def = "{'partnerId': 1, 'priority': -1}")
public class CommissionRule {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_id")
    private String partnerId;

    @Field("rule_name")
    private String ruleName;

    @Field("commission_type")
    private CommissionType commissionType;

    @Field("commission_rate")
    private BigDecimal commissionRate;

    @Field("fixed_amount")
    private BigDecimal fixedAmount;

    @Field("min_order_amount")
    private BigDecimal minOrderAmount;

    @Field("max_order_amount")
    private BigDecimal maxOrderAmount;

    @Field("priority")
    private Integer priority;

    @Field("status")
    private RuleStatus status;

    @Field("created_at")
    private Instant createdAt;

    protected CommissionRule() {
    }

    public CommissionRule(String tenantId, String partnerId, String ruleName,
                          CommissionType type, BigDecimal rate) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerId = Objects.requireNonNull(partnerId);
        this.ruleName = ruleName;
        this.commissionType = type;
        this.commissionRate = rate;
        this.status = RuleStatus.ACTIVE;
        this.priority = 0;
        this.createdAt = Instant.now();
    }

    public boolean isApplicable(BigDecimal orderAmount) {
        if (status != RuleStatus.ACTIVE) return false;
        if (minOrderAmount != null && orderAmount.compareTo(minOrderAmount) < 0) return false;
        if (maxOrderAmount != null && orderAmount.compareTo(maxOrderAmount) > 0) return false;
        return true;
    }

    public BigDecimal calculateCommission(BigDecimal orderAmount) {
        switch (commissionType) {
            case PERCENTAGE:
                return orderAmount.multiply(commissionRate);
            case FIXED:
                return fixedAmount != null ? fixedAmount : BigDecimal.ZERO;
            case TIERED:
                return orderAmount.multiply(commissionRate); // Simplified
            default:
                return BigDecimal.ZERO;
        }
    }

    public enum CommissionType {
        PERCENTAGE,
        FIXED,
        TIERED
    }

    public enum RuleStatus {
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerId() { return partnerId; }
    public String getRuleName() { return ruleName; }
    public CommissionType getCommissionType() { return commissionType; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public BigDecimal getFixedAmount() { return fixedAmount; }
    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public BigDecimal getMaxOrderAmount() { return maxOrderAmount; }
    public Integer getPriority() { return priority; }
    public RuleStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setFixedAmount(BigDecimal fixedAmount) { this.fixedAmount = fixedAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }
    public void setMaxOrderAmount(BigDecimal maxOrderAmount) { this.maxOrderAmount = maxOrderAmount; }
    public void setPriority(Integer priority) { this.priority = priority; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    protected void setRuleName(String ruleName) { this.ruleName = ruleName; }
    protected void setCommissionType(CommissionType commissionType) { this.commissionType = commissionType; }
    protected void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    protected void setStatus(RuleStatus status) { this.status = status; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
