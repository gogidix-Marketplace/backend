package com.gogidix.courier.dynamicpricingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "pricing_adjustments")
@CompoundIndex(name = "idx_tenant_code", def = "{'tenantId': 1, 'adjustmentCode': 1}")
public class PricingAdjustment {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("adjustment_code")
    private String adjustmentCode;

    @Field("adjustment_type")
    private AdjustmentType adjustmentType;

    @Field("adjustment_value")
    private BigDecimal adjustmentValue;

    @Field("zone_id")
    private String zoneId;

    @Field("service_type")
    private String serviceType;

    @Field("start_time")
    private LocalDateTime startTime;

    @Field("end_time")
    private LocalDateTime endTime;

    @Field("status")
    private Status status;

    @Field("description")
    private String description;

    @Field("created_at")
    private Instant createdAt;

    protected PricingAdjustment() {
    }

    public PricingAdjustment(String tenantId, String adjustmentCode, AdjustmentType type, BigDecimal value) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.adjustmentCode = adjustmentCode;
        this.adjustmentType = type;
        this.adjustmentValue = value;
        this.status = Status.ACTIVE;
        this.createdAt = Instant.now();
    }

    public boolean isActive() {
        if (status != Status.ACTIVE) return false;
        LocalDateTime now = LocalDateTime.now();
        if (startTime != null && now.isBefore(startTime)) return false;
        if (endTime != null && now.isAfter(endTime)) return false;
        return true;
    }

    public enum AdjustmentType {
        PERCENTAGE,
        FIXED,
        MULTIPLIER
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        SCHEDULED,
        EXPIRED
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getAdjustmentCode() { return adjustmentCode; }
    public AdjustmentType getAdjustmentType() { return adjustmentType; }
    public BigDecimal getAdjustmentValue() { return adjustmentValue; }
    public String getZoneId() { return zoneId; }
    public String getServiceType() { return serviceType; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Status getStatus() { return status; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setStatus(Status status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setAdjustmentCode(String adjustmentCode) { this.adjustmentCode = adjustmentCode; }
    protected void setAdjustmentType(AdjustmentType adjustmentType) { this.adjustmentType = adjustmentType; }
    protected void setAdjustmentValue(BigDecimal adjustmentValue) { this.adjustmentValue = adjustmentValue; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
