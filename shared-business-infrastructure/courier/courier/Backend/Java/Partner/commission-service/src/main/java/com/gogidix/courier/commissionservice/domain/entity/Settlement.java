package com.gogidix.courier.commissionservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "settlements")
public class Settlement {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_id")
    private String partnerId;

    @Field("settlement_number")
    private String settlementNumber;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("commission_count")
    private Integer commissionCount;

    @Field("status")
    private SettlementStatus status;

    @Field("currency")
    private String currency;

    @Field("commission_ids")
    private List<String> commissionIds;

    @Field("period_start")
    private Instant periodStart;

    @Field("period_end")
    private Instant periodEnd;

    @Field("created_at")
    private Instant createdAt;

    @Field("settled_at")
    private Instant settledAt;

    @Field("payment_reference")
    private String paymentReference;

    protected Settlement() {
    }

    public Settlement(String tenantId, String partnerId, String settlementNumber) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerId = Objects.requireNonNull(partnerId);
        this.settlementNumber = settlementNumber;
        this.totalAmount = BigDecimal.ZERO;
        this.commissionCount = 0;
        this.status = SettlementStatus.PENDING;
        this.currency = "USD";
        this.commissionIds = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    public void addCommission(String commissionId, BigDecimal amount) {
        this.commissionIds.add(commissionId);
        this.totalAmount = this.totalAmount.add(amount);
        this.commissionCount++;
    }

    public void markAsPaid(String paymentReference) {
        this.status = SettlementStatus.PAID;
        this.paymentReference = paymentReference;
        this.settledAt = Instant.now();
    }

    public void markAsProcessing() {
        this.status = SettlementStatus.PROCESSING;
    }

    public enum SettlementStatus {
        PENDING,
        PROCESSING,
        PAID,
        FAILED,
        CANCELLED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerId() { return partnerId; }
    public String getSettlementNumber() { return settlementNumber; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Integer getCommissionCount() { return commissionCount; }
    public SettlementStatus getStatus() { return status; }
    public String getCurrency() { return currency; }
    public List<String> getCommissionIds() { return commissionIds; }
    public Instant getPeriodStart() { return periodStart; }
    public Instant getPeriodEnd() { return periodEnd; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getSettledAt() { return settledAt; }
    public String getPaymentReference() { return paymentReference; }

    // Setters
    public void setPeriodStart(Instant periodStart) { this.periodStart = periodStart; }
    public void setPeriodEnd(Instant periodEnd) { this.periodEnd = periodEnd; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerId(String partnerId) { this.partnerId = partnerId; }
    protected void setSettlementNumber(String settlementNumber) { this.settlementNumber = settlementNumber; }
    protected void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    protected void setCommissionCount(Integer commissionCount) { this.commissionCount = commissionCount; }
    protected void setStatus(SettlementStatus status) { this.status = status; }
    protected void setCurrency(String currency) { this.currency = currency; }
    protected void setCommissionIds(List<String> commissionIds) { this.commissionIds = commissionIds; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setSettledAt(Instant settledAt) { this.settledAt = settledAt; }
    protected void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
}
