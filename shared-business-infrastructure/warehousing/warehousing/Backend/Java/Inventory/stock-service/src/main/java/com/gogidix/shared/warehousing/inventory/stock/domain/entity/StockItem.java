package com.gogidix.shared.warehousing.inventory.stock.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Stock Item entity
 * Represents an item in inventory stock
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_items")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'sku': 1}", unique = true)
public class StockItem {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String sku;

    private String productName;

    private String description;

    private String category;

    private String brand;

    private String manufacturer;

    private String upc;

    private String ean;

    private Double unitCost;

    private String currency;

    private Double sellingPrice;

    private StockStatus status;

    private Integer quantityOnHand;

    private Integer quantityAllocated;

    private Integer quantityAvailable;

    private Integer reorderLevel;

    private Integer maxStockLevel;

    private Integer reorderQuantity;

    @Indexed
    private Boolean reorderNeeded;

    private String location;

    private String zone;

    private String aisle;

    private String bay;

    private String bin;

    private String palletPosition;

    private Dimensions dimensions;

    private WeightInfo weightInfo;

    private StorageInfo storageInfo;

    private String supplierId;

    private String supplierName;

    private String supplierSku;

    private Integer leadTimeDays;

    private LocalDateTime lastReceivedDate;

    private LocalDateTime lastIssuedDate;

    private Double averageMonthlyUsage;

    private Integer daysOfStockOnHand;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    private LocalDateTime lastStocktakeDate;

    private Integer countedQuantity;

    private Integer varianceQuantity;

    private String notes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dimensions {
        private Double length;
        private Double width;
        private Double height;
        private String unit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeightInfo {
        private Double netWeight;
        private Double grossWeight;
        private String weightUnit;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorageInfo {
        private String temperatureZone;
        private Boolean climateControlled;
        private Boolean hazardous;
        private String hazardClass;
        private Boolean fragile;
        private Boolean stackable;
        private Integer maxStackHeight;
    }

    public enum StockStatus {
        IN_STOCK,
        LOW_STOCK,
        OUT_OF_STOCK,
        DISCONTINUED,
        ON_ORDER,
        DAMAGED
    }

    public Integer getAvailableQuantity() {
        return quantityOnHand != null && quantityAllocated != null
                ? quantityOnHand - quantityAllocated
                : quantityOnHand != null ? quantityOnHand : 0;
    }

    public boolean needsReorder() {
        if (reorderLevel == null || quantityOnHand == null) {
            return false;
        }
        return quantityOnHand <= reorderLevel;
    }

    public void updateQuantity(Integer quantityChange, String reason) {
        if (quantityOnHand == null) {
            quantityOnHand = 0;
        }
        quantityOnHand += quantityChange;

        if (quantityAllocated != null && quantityAllocated > 0) {
            quantityAvailable = quantityOnHand - quantityAllocated;
        } else {
            quantityAvailable = quantityOnHand;
        }

        reorderNeeded = needsReorder();
        updatedAt = LocalDateTime.now();

        if (quantityOnHand <= 0) {
            status = StockStatus.OUT_OF_STOCK;
        } else if (needsReorder()) {
            status = StockStatus.LOW_STOCK;
        } else {
            status = StockStatus.IN_STOCK;
        }
    }

    public void allocate(Integer quantity) {
        if (quantityOnHand == null) {
            quantityOnHand = 0;
        }
        if (quantityAllocated == null) {
            quantityAllocated = 0;
        }

        if (quantityOnHand < quantity) {
            throw new IllegalStateException("Insufficient stock. Available: " + quantityOnHand + ", Requested: " + quantity);
        }

        quantityAllocated += quantity;
        quantityAvailable = quantityOnHand - quantityAllocated;
        updatedAt = LocalDateTime.now();
    }

    public void deallocate(Integer quantity) {
        if (quantityAllocated == null) {
            quantityAllocated = 0;
        }

        quantityAllocated = Math.max(0, quantityAllocated - quantity);
        quantityAvailable = quantityOnHand - quantityAllocated;
        updatedAt = LocalDateTime.now();
    }
}
