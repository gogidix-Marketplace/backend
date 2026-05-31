package com.gogidix.shared.warehousing.inventory.analytics.domain.entity;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory_turnover")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1, 'warehouseId': 1}", name = "idx_tenant_sku_warehouse")
public class InventoryTurnover {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String sku;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime periodStart;

    private LocalDateTime periodEnd;

    // Turnover metrics
    private Double turnoverRate;
    private Double daysInInventory;

    // Financial metrics
    private Double costOfGoodsSold;
    private Double averageInventoryValue;

    // Volume metrics
    private Integer beginningInventory;
    private Integer endingInventory;
    private Integer unitsSold;

    // Classification
    private TurnoverCategory category;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum TurnoverCategory {
        FAST_MOVING,
        MODERATE_MOVING,
        SLOW_MOVING,
        OBSOLETE
    }
}
