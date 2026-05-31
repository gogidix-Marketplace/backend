package com.gogidix.shared.warehousing.stock.domain.entity;

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

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_levels")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1, 'locationId': 1}", name = "idx_stock_sku_location", unique = true)
public class StockLevel {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String sku;

    @Indexed
    private String locationId;

    private Integer availableQuantity;

    @Builder.Default
    private Integer reservedQuantity = 0;

    @Builder.Default
    private Integer allocatedQuantity = 0;

    private Integer reorderPoint;

    private Integer reorderQuantity;

    private LocalDateTime lastMovementAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Integer getTotalQuantity() {
        return availableQuantity + reservedQuantity + allocatedQuantity;
    }

    public boolean isBelowReorderPoint() {
        return reorderPoint != null && availableQuantity <= reorderPoint;
    }

    public boolean reserve(Integer quantity) {
        if (availableQuantity >= quantity) {
            availableQuantity -= quantity;
            reservedQuantity += quantity;
            lastMovementAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean release(Integer quantity) {
        if (reservedQuantity >= quantity) {
            reservedQuantity -= quantity;
            availableQuantity += quantity;
            lastMovementAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean allocate(Integer quantity) {
        if (reservedQuantity >= quantity) {
            reservedQuantity -= quantity;
            allocatedQuantity += quantity;
            lastMovementAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean confirmAllocation(Integer quantity) {
        if (allocatedQuantity >= quantity) {
            allocatedQuantity -= quantity;
            lastMovementAt = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public void addStock(Integer quantity) {
        availableQuantity += quantity;
        lastMovementAt = LocalDateTime.now();
    }

    public boolean removeStock(Integer quantity) {
        if (availableQuantity >= quantity) {
            availableQuantity -= quantity;
            lastMovementAt = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
