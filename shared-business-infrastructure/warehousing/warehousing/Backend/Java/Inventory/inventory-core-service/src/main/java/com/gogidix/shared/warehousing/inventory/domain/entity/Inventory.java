package com.gogidix.shared.warehousing.inventory.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Inventory Entity - Multi-tenant with MongoDB
 *
 * Each inventory item is isolated by tenant_id via MongoDB queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory_items")
@CompoundIndex(def = "{'tenantId': 1, 'id': 1}", name = "idx_tenant_id")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1}", name = "idx_tenant_sku")
@CompoundIndex(def = "{'tenantId': 1, 'locationId': 1}", name = "idx_tenant_location")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1, 'locationId': 1}", name = "idx_tenant_sku_location")
public class Inventory {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    private TenantType tenantType;

    @Indexed
    private String sku;

    private Integer quantity;

    @Indexed
    private String locationId;

    private LocationType locationType;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum TenantType {
        ECOMMERCE_VENDOR,
        LOGISTICS_PARTNER,
        PROCUREMENT_CUSTOMER,
        PUBLIC_USER_WAREHOUSING
    }

    public enum LocationType {
        WAREHOUSE,
        SELF_STORAGE,
        VENDOR_LOCATION,
        FULFILLMENT_CENTER
    }
}
