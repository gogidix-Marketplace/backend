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
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vendor_stock_registrations")
@CompoundIndex(def = "{'vendorId': 1, 'warehouseId': 1}", name = "idx_vendor_warehouse")
public class VendorStockRegistration {

    @Id
    private String id;

    @Indexed
    private String vendorId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String sellingRadius;
    private List<StockItem> items;
    private String status;
    private LocalDateTime estimatedReceivingDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StockItem {
        private String sku;
        private String productName;
        private Integer quantity;
        private String unitOfMeasure;
        private String category;
        private Double weight;
        private String weightUnit;
        private Dimensions dimensions;
        private Map<String, String> attributes;
        private String allocatedLocation;
        private String status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Dimensions {
        private Double length;
        private Double width;
        private Double height;
    }
}
