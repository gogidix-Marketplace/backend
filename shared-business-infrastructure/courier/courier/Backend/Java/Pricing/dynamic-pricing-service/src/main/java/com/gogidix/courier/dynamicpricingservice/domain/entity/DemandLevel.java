package com.gogidix.courier.dynamicpricingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "demand_levels")
@CompoundIndex(name = "idx_zone_time", def = "{'zoneId': 1, 'timestamp': -1}")
public class DemandLevel {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("zone_id")
    private String zoneId;

    @Field("level")
    private Level level;

    @Field("active_orders")
    private Integer activeOrders;

    @Field("available_drivers")
    private Integer availableDrivers;

    @Field("ratio")
    private Double ratio;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("created_at")
    private Instant createdAt;

    protected DemandLevel() {
    }

    public DemandLevel(String tenantId, String zoneId, Level level, int activeOrders, int availableDrivers) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.zoneId = Objects.requireNonNull(zoneId);
        this.level = level;
        this.activeOrders = activeOrders;
        this.availableDrivers = availableDrivers;
        this.ratio = availableDrivers > 0 ? (double) activeOrders / availableDrivers : Double.MAX_VALUE;
        this.timestamp = LocalDateTime.now();
        this.createdAt = Instant.now();
    }

    public enum Level {
        LOW,
        NORMAL,
        ELEVATED,
        HIGH,
        SURGE
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getZoneId() { return zoneId; }
    public Level getLevel() { return level; }
    public Integer getActiveOrders() { return activeOrders; }
    public Integer getAvailableDrivers() { return availableDrivers; }
    public Double getRatio() { return ratio; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Instant getCreatedAt() { return createdAt; }

    // Setters
    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setZoneId(String zoneId) { this.zoneId = zoneId; }
    protected void setLevel(Level level) { this.level = level; }
    protected void setActiveOrders(Integer activeOrders) { this.activeOrders = activeOrders; }
    protected void setAvailableDrivers(Integer availableDrivers) { this.availableDrivers = availableDrivers; }
    protected void setRatio(Double ratio) { this.ratio = ratio; }
    protected void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
