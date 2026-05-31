package com.gogidix.shared.warehousing.fulfillment.packing.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pack Order entity
 * Represents a packing order for warehouse fulfillment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pack_orders")
public class PackOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String pickOrderId;

    private String pickNumber;

    @Indexed
    private String packNumber;

    private PackStatus status;

    private Integer priority;

    @Indexed
    private LocalDateTime requestedDate;

    @Indexed
    private LocalDateTime dueDate;

    private LocalDateTime startedDate;

    private LocalDateTime completedDate;

    private String packerId;

    private String packerName;

    @Builder.Default
    private List<PackItem> items = new ArrayList<>();

    private Integer totalItems;

    private Integer itemsPacked;

    private String shippingMethod;

    private String carrier;

    private String trackingNumber;

    private String destinationAddress;

    private String recipientName;

    private BoxInfo boxInfo;

    private String packingNotes;

    private Double totalWeight;

    private String weightUnit;

    private String instructions;

    private Boolean fragile;

    private Boolean hazardous;

    @Indexed
    private LocalDateTime createdAt;

    @Indexed
    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PackItem {
        private String itemId;
        private String sku;
        private String productName;
        private Double quantity;
        private String unit;
        private Double packedQuantity;
        private Boolean packed;
        private PackItemStatus status;
        private String boxNumber;
        private LocalDateTime packedAt;
        private String packedBy;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoxInfo {
        private String boxType;
        private Double length;
        private Double width;
        private Double height;
        private String dimensionUnit;
        private Double boxWeight;
        private Double maxWeight;
        private Integer boxCount;
        private List<BoxDetail> boxDetails;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoxDetail {
        private String boxNumber;
        private Double length;
        private Double width;
        private Double height;
        private Double weight;
        private List<String> items;
        private String sealNumber;
    }

    public enum PackStatus {
        PENDING,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        PARTIALLY_COMPLETED,
        CANCELLED,
        SHIPPED
    }

    public enum PackItemStatus {
        PENDING,
        PACKED,
        VERIFIED,
        DAMAGED,
        MISSING,
        CANCELLED
    }

    public void startPacking(String packerId, String packerName) {
        this.status = PackStatus.IN_PROGRESS;
        this.packerId = packerId;
        this.packerName = packerName;
        this.startedDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void completePacking() {
        this.status = PackStatus.COMPLETED;
        this.completedDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsShipped(String trackingNumber) {
        this.status = PackStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(PackItem item) {
        this.items.add(item);
        this.totalItems = this.items.size();
        this.updatedAt = LocalDateTime.now();
    }

    public void markItemPacked(String itemId, Double quantity, String boxNumber, String packedBy) {
        PackItem item = findItemById(itemId);
        if (item != null) {
            item.setPackedQuantity(quantity);
            item.setPacked(true);
            item.setStatus(PackItemStatus.PACKED);
            item.setBoxNumber(boxNumber);
            item.setPackedAt(LocalDateTime.now());
            item.setPackedBy(packedBy);

            this.itemsPacked = (int) this.items.stream().filter(PackItem::getPacked).count();

            if (this.itemsPacked.equals(this.totalItems)) {
                this.status = PackStatus.COMPLETED;
            } else if (this.itemsPacked > 0) {
                this.status = PackStatus.PARTIALLY_COMPLETED;
            }

            this.updatedAt = LocalDateTime.now();
        }
    }

    public Double getProgressPercentage() {
        if (totalItems == null || totalItems == 0) {
            return 0.0;
        }
        return (itemsPacked != null ? itemsPacked : 0) * 100.0 / totalItems;
    }

    private PackItem findItemById(String itemId) {
        return items.stream().filter(i -> i.getItemId().equals(itemId)).findFirst().orElse(null);
    }
}
