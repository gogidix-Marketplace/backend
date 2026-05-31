package com.gogidix.shared.warehousing.pricing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Pricing Rule Entity
 *
 * Defines pricing rules for storage services based on various dimensions
 * Multi-tenant with MongoDB support
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pricing_rules")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'serviceType': 1, 'storageType': 1}", name = "idx_pricing_tenant_service")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1}", name = "idx_tenant_warehouse")
@CompoundIndex(def = "{'tenantId': 1, 'active': 1}", name = "idx_tenant_active")
public class PricingRule {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    @Indexed
    private ServiceType serviceType;

    @Indexed
    private StorageType storageType;

    private BigDecimal basePrice;

    private PriceUnit priceUnit;

    private BigDecimal minimumCharge;

    private BillingCycle billingCycle;

    private ZoneType zoneType;

    private String warehouseId;

    private Integer volumeThreshold;

    private Integer weightThreshold;

    private BigDecimal volumePriceOverride;

    private BigDecimal weightPriceOverride;

    private Boolean active;

    private Integer priority;

    private String currency;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ServiceType {
        STORAGE,
        HANDLING,
        PICKING,
        PACKING,
        SHIPPING,
        VALUE_ADDED_SERVICE
    }

    public enum StorageType {
        PALLET,
        SHELF,
        BIN,
        BULK,
        CLIMATE_CONTROLLED,
        HAZARDOUS_MATERIALS,
        VALET_STORAGE
    }

    public enum PriceUnit {
        PER_ITEM,
        PER_PALLET,
        PER_SQUARE_FOOT,
        PER_CUBIC_METER,
        PER_KILOGRAM,
        PER_HOUR,
        PER_DAY,
        PER_WEEK,
        PER_MONTH
    }

    public enum BillingCycle {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        ANNUALLY
    }

    public enum ZoneType {
        STANDARD,
        CLIMATE_CONTROLLED,
        FREEZER,
        SECURE,
        HAZMAT,
        HIGH_VALUE
    }
}
