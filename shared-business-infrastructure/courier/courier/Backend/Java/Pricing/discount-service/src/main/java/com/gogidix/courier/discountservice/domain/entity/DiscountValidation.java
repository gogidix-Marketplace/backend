package com.gogidix.courier.discountservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a Discount Validation result.
 * Stores cached validation results for performance.
 */
@Document(collection = "discount_validation")
public class DiscountValidation {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("discount_code")
    private String discountCode;

    @Field("user_id")
    private String userId;

    @Field("order_amount")
    private BigDecimal orderAmount;

    @Field("zone_id")
    private String zoneId;

    @Field("service_type")
    private String serviceType;

    @Field("is_valid")
    private Boolean isValid;

    @Field("discount_amount")
    private BigDecimal discountAmount;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("validated_at")
    private Instant validatedAt;

    @Field("expires_at")
    private Instant expiresAt;

    protected DiscountValidation() {
    }

    public DiscountValidation(String tenantId, String discountCode, String userId,
                              BigDecimal orderAmount, String zoneId, String serviceType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.discountCode = Objects.requireNonNull(discountCode, "discountCode is required");
        this.userId = userId;
        this.orderAmount = orderAmount;
        this.zoneId = zoneId;
        this.serviceType = serviceType;
        this.validatedAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(300); // 5 minutes
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getDiscountCode() { return discountCode; }
    public String getUserId() { return userId; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public String getZoneId() { return zoneId; }
    public String getServiceType() { return serviceType; }
    public Boolean getIsValid() { return isValid; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public String getRejectionReason() { return rejectionReason; }
    public Instant getValidatedAt() { return validatedAt; }
    public Instant getExpiresAt() { return expiresAt; }

    // Setters
    public void setIsValid(Boolean isValid) { this.isValid = isValid; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setDiscountCode(String discountCode) { this.discountCode = discountCode; }
    protected void setUserId(String userId) { this.userId = userId; }
    protected void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    protected void setZoneId(String zoneId) { this.zoneId = zoneId; }
    protected void setServiceType(String serviceType) { this.serviceType = serviceType; }
    protected void setValidatedAt(Instant validatedAt) { this.validatedAt = validatedAt; }
    protected void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
}
