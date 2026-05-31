package com.gogidix.courier.discountservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Domain Entity representing a Discount Code.
 * Manages promotional discount codes with validation rules.
 */
@Document(collection = "discount_codes")
@CompoundIndex(name = "idx_tenant_code", def = "{'tenantId': 1, 'code': 1}")
public class DiscountCode {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("code")
    private String code;

    @Field("description")
    private String description;

    @Field("discount_type")
    private DiscountType discountType;

    @Field("discount_value")
    private BigDecimal discountValue;

    @Field("max_discount_amount")
    private BigDecimal maxDiscountAmount;

    @Field("min_order_amount")
    private BigDecimal minOrderAmount;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("max_uses")
    private Integer maxUses;

    @Field("current_uses")
    private Integer currentUses;

    @Field("max_uses_per_user")
    private Integer maxUsesPerUser;

    @Field("status")
    private DiscountStatus status;

    @Field("applicable_zones")
    private String applicableZones;

    @Field("applicable_services")
    private String applicableServices;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("version")
    private Long version;

    protected DiscountCode() {
    }

    public DiscountCode(String tenantId, String code, DiscountType discountType, BigDecimal discountValue) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.code = Objects.requireNonNull(code, "code is required").toUpperCase();
        this.discountType = Objects.requireNonNull(discountType, "discountType is required");
        this.discountValue = Objects.requireNonNull(discountValue, "discountValue is required");
        this.status = DiscountStatus.ACTIVE;
        this.currentUses = 0;
        this.maxUses = 1000;
        this.maxUsesPerUser = 5;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;
    }

    // Domain Logic

    public boolean isValid() {
        Instant now = Instant.now();
        return status == DiscountStatus.ACTIVE
                && !isExpired()
                && !isMaxUsageReached();
    }

    public boolean isExpired() {
        if (endDate == null) {
            return false;
        }
        return LocalDate.now().isAfter(endDate);
    }

    public boolean isNotYetStarted() {
        if (startDate == null) {
            return false;
        }
        return LocalDate.now().isBefore(startDate);
    }

    public boolean isMaxUsageReached() {
        return maxUses != null && currentUses >= maxUses;
    }

    public boolean canBeUsedByUser(String userId, int userUsageCount) {
        return maxUsesPerUser == null || userUsageCount < maxUsesPerUser;
    }

    public boolean isApplicableToZone(String zoneId) {
        return applicableZones == null || applicableZones.isEmpty()
                || applicableZones.contains(zoneId);
    }

    public boolean isApplicableToService(String serviceType) {
        return applicableServices == null || applicableServices.isEmpty()
                || applicableServices.contains(serviceType);
    }

    public boolean meetsMinimumOrder(BigDecimal orderAmount) {
        return minOrderAmount == null || orderAmount.compareTo(minOrderAmount) >= 0;
    }

    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        BigDecimal discount;
        switch (discountType) {
            case PERCENTAGE:
                discount = orderAmount.multiply(discountValue)
                        .divide(BigDecimal.valueOf(100));
                break;
            case FIXED:
                discount = discountValue;
                break;
            default:
                discount = BigDecimal.ZERO;
        }

        if (maxDiscountAmount != null && discount.compareTo(maxDiscountAmount) > 0) {
            return maxDiscountAmount;
        }

        return discount;
    }

    public void recordUsage() {
        this.currentUses++;
        this.updatedAt = Instant.now();
        this.version++;
    }

    public void activate() {
        this.status = DiscountStatus.ACTIVE;
        this.updatedAt = Instant.now();
        this.version++;
    }

    public void deactivate() {
        this.status = DiscountStatus.INACTIVE;
        this.updatedAt = Instant.now();
        this.version++;
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public DiscountType getDiscountType() { return discountType; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public BigDecimal getMaxDiscountAmount() { return maxDiscountAmount; }
    public BigDecimal getMinOrderAmount() { return minOrderAmount; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Integer getMaxUses() { return maxUses; }
    public Integer getCurrentUses() { return currentUses; }
    public Integer getMaxUsesPerUser() { return maxUsesPerUser; }
    public DiscountStatus getStatus() { return status; }
    public String getApplicableZones() { return applicableZones; }
    public String getApplicableServices() { return applicableServices; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }

    // Setters
    public void setDescription(String description) { this.description = description; }
    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) { this.maxDiscountAmount = maxDiscountAmount; }
    public void setMinOrderAmount(BigDecimal minOrderAmount) { this.minOrderAmount = minOrderAmount; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setMaxUses(Integer maxUses) { this.maxUses = maxUses; }
    public void setMaxUsesPerUser(Integer maxUsesPerUser) { this.maxUsesPerUser = maxUsesPerUser; }
    public void setApplicableZones(String applicableZones) { this.applicableZones = applicableZones; }
    public void setApplicableServices(String applicableServices) { this.applicableServices = applicableServices; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setCode(String code) { this.code = code; }
    protected void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    protected void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    protected void setCurrentUses(Integer currentUses) { this.currentUses = currentUses; }
    protected void setStatus(DiscountStatus status) { this.status = status; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    protected void setVersion(Long version) { this.version = version; }

    public enum DiscountType {
        PERCENTAGE,
        FIXED
    }

    public enum DiscountStatus {
        ACTIVE,
        INACTIVE,
        EXPIRED,
        DRAFT
    }
}
