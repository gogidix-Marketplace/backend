package com.gogidix.shared.warehousing.ecommerce.domain.entity;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "warehouse_stock_levels")
@CompoundIndex(def = "{'sku': 1, 'warehouseId': 1}", unique = true, name = "idx_sku_warehouse")
public class WarehouseStockLevel {

    @Id
    private String id;

    @Indexed
    private String sku;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String vendorId;
    private String productName;
    private String category;
    private Integer quantity;
    private Integer reserved;
    private Integer incoming;
    private String sellingRadius;
    private String estimatedPickTime;
    private Integer reorderThreshold;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Integer getAvailable() {
        return quantity - reserved;
    }

    public boolean isLowStock() {
        return getAvailable() <= reorderThreshold;
    }

    public boolean isDepleted() {
        return getAvailable() <= 0;
    }
}
