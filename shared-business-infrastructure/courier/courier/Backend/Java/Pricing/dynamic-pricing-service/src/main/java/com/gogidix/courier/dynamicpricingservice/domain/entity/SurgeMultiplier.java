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

@Document(collection = "surge_multipliers")
@CompoundIndex(name = "idx_zone_time", def = "{'zoneId': 1, 'effectiveTime': 1}")
public class SurgeMultiplier {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("zone_id")
    private String zoneId;

    @Field("multiplier")
    private BigDecimal multiplier;

    @Field("effective_time")
    private LocalDateTime effectiveTime;

    @Field("expiry_time")
    private LocalDateTime expiryTime;

    @Field("demand_level")
    private DemandLevel demandLevel;

    @Field("reason")
    private String reason;

    @Field("created_at")
    private Instant createdAt;

    protected SurgeMultiplier() {
    }

    public SurgeMultiplier(String tenantId, String zoneId, BigDecimal multiplier, DemandLevel demandLevel) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.zoneId = Objects.requireNonNull(zoneId);
        this.multiplier = Objects.requireNonNull(multiplier);
        this.demandLevel = demandLevel;
        this.effectiveTime = LocalDateTime.now();
        this.createdAt = Instant.now();
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(effectiveTime) &&
                (expiryTime == null || now.isBefore(expiryTime));
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getZoneId() { return zoneId; }
    public BigDecimal getMultiplier() { return multiplier; }
    public LocalDateTime getEffectiveTime() { return effectiveTime; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public DemandLevel getDemandLevel() { return demandLevel; }
    public String getReason() { return reason; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    public void setReason(String reason) { this.reason = reason; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setZoneId(String zoneId) { this.zoneId = zoneId; }
    protected void setMultiplier(BigDecimal multiplier) { this.multiplier = multiplier; }
    protected void setEffectiveTime(LocalDateTime effectiveTime) { this.effectiveTime = effectiveTime; }
    protected void setDemandLevel(DemandLevel demandLevel) { this.demandLevel = demandLevel; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public enum DemandLevel {
        LOW,
        NORMAL,
        HIGH,
        CRITICAL
    }
}
