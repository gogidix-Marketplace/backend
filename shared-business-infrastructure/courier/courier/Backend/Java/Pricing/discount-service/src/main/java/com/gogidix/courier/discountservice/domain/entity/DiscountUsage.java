package com.gogidix.courier.discountservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Discount Usage record.
 * Tracks each usage of a discount code.
 */
@Document(collection = "discount_usage")
@CompoundIndex(name = "idx_tenant_code_user", def = "{'tenantId': 1, 'discountCode': 1, 'userId': 1}")
public class DiscountUsage {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("discount_code")
    private String discountCode;

    @Indexed
    @Field("user_id")
    private String userId;

    @Field("order_id")
    private String orderId;

    @Field("original_amount")
    private BigDecimal originalAmount;

    @Field("discount_amount")
    private BigDecimal discountAmount;

    @Field("final_amount")
    private BigDecimal finalAmount;

    @Field("used_at")
    private Instant usedAt;

    @Field("zone_id")
    private String zoneId;

    @Field("service_type")
    private String serviceType;

    protected DiscountUsage() {
    }

    public DiscountUsage(String tenantId, String discountCode, String userId, String orderId,
                         BigDecimal originalAmount, BigDecimal discountAmount, BigDecimal finalAmount) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.discountCode = Objects.requireNonNull(discountCode, "discountCode is required");
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.orderId = orderId;
        this.originalAmount = originalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.usedAt = Instant.now();
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getDiscountCode() { return discountCode; }
    public String getUserId() { return userId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getOriginalAmount() { return originalAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getFinalAmount() { return finalAmount; }
    public Instant getUsedAt() { return usedAt; }
    public String getZoneId() { return zoneId; }
    public String getServiceType() { return serviceType; }

    // Setters
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setDiscountCode(String discountCode) { this.discountCode = discountCode; }
    protected void setUserId(String userId) { this.userId = userId; }
    protected void setOrderId(String orderId) { this.orderId = orderId; }
    protected void setOriginalAmount(BigDecimal originalAmount) { this.originalAmount = originalAmount; }
    protected void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    protected void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }
    protected void setUsedAt(Instant usedAt) { this.usedAt = usedAt; }
}
