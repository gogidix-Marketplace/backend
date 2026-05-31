package com.gogidix.courier.commissionservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "commissions")
@CompoundIndex(name = "idx_partner_order", def = "{'partnerId': 1, 'orderId': 1}")
public class Commission {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_id")
    private String partnerId;

    @Field("order_id")
    private String orderId;

    @Field("order_amount")
    private BigDecimal orderAmount;

    @Field("commission_rate")
    private BigDecimal commissionRate;

    @Field("commission_amount")
    private BigDecimal commissionAmount;

    @Field("status")
    private CommissionStatus status;

    @Field("currency")
    private String currency;

    @Field("earned_at")
    private Instant earnedAt;

    @Field("settled_at")
    private Instant settledAt;

    @Field("settlement_id")
    private String settlementId;

    @Field("created_at")
    private Instant createdAt;

    protected Commission() {
    }

    public Commission(String tenantId, String partnerId, String orderId,
                      BigDecimal orderAmount, BigDecimal commissionRate) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerId = Objects.requireNonNull(partnerId);
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.commissionRate = commissionRate;
        this.commissionAmount = orderAmount.multiply(commissionRate);
        this.status = CommissionStatus.PENDING;
        this.currency = "USD";
        this.earnedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    public void markAsSettled(String settlementId) {
        this.status = CommissionStatus.SETTLED;
        this.settlementId = settlementId;
        this.settledAt = Instant.now();
    }

    public enum CommissionStatus {
        PENDING,
        SETTLED,
        CANCELLED,
        ON_HOLD
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerId() { return partnerId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public CommissionStatus getStatus() { return status; }
    public String getCurrency() { return currency; }
    public Instant getEarnedAt() { return earnedAt; }
    public Instant getSettledAt() { return settledAt; }
    public String getSettlementId() { return settlementId; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    protected void setOrderId(String orderId) { this.orderId = orderId; }
    protected void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    protected void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    protected void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    protected void setStatus(CommissionStatus status) { this.status = status; }
    protected void setCurrency(String currency) { this.currency = currency; }
    protected void setEarnedAt(Instant earnedAt) { this.earnedAt = earnedAt; }
    protected void setSettledAt(Instant settledAt) { this.settledAt = settledAt; }
    protected void setSettlementId(String settlementId) { this.settlementId = settlementId; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
