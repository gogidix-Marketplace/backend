package com.gogidix.shared.warehousing.fulfillment.picking.domain.entity;

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
 * Pick Order entity
 * Represents a picking order for warehouse fulfillment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pick_orders")
public class PickOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String orderNumber;

    private String pickNumber;

    private String referenceNumber;

    private PickStatus status;

    private Integer priority;

    @Indexed
    private LocalDateTime requestedDate;

    @Indexed
    private LocalDateTime dueDate;

    private LocalDateTime scheduledDate;

    private LocalDateTime startedDate;

    private LocalDateTime completedDate;

    private String pickerId;

    private String pickerName;

    @Builder.Default
    private List<PickItem> items = new ArrayList<>();

    private Integer totalItems;

    private Integer itemsPicked;

    private Double totalQuantity;

    private Double quantityPicked;

    private String zone;

    private String aisle;

    private String destination;

    private String notes;

    private String orderType;

    private String shippingMethod;

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
    public static class PickItem {
        private String itemId;
        private String sku;
        private String productName;
        private String description;
        private Double quantity;
        private String unit;
        private String location;
        private String binCode;
        private Double pickedQuantity;
        private Boolean picked;
        private PickItemStatus status;
        private String barcode;
        private Double quantityVerified;
        private LocalDateTime pickedAt;
        private String pickedBy;
        private String notes;
    }

    public enum PickStatus {
        PENDING,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        PARTIALLY_COMPLETED,
        CANCELLED,
        ON_HOLD
    }

    public enum PickItemStatus {
        PENDING,
        PICKED,
        VERIFIED,
        SHORT,
        DAMAGED,
        CANCELLED
    }

    public void startPicking(String pickerId, String pickerName) {
        this.status = PickStatus.IN_PROGRESS;
        this.pickerId = pickerId;
        this.pickerName = pickerName;
        this.startedDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void completePicking() {
        this.status = PickStatus.COMPLETED;
        this.completedDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(PickItem item) {
        this.items.add(item);
        this.totalItems = this.items.size();
        this.totalQuantity = this.items.stream().mapToDouble(PickItem::getQuantity).sum();
        this.updatedAt = LocalDateTime.now();
    }

    public void markItemPicked(String itemId, Double quantity, String pickedBy) {
        PickItem item = findItemById(itemId);
        if (item != null) {
            item.setPickedQuantity(quantity);
            item.setPicked(true);
            item.setStatus(PickItemStatus.PICKED);
            item.setPickedAt(LocalDateTime.now());
            item.setPickedBy(pickedBy);

            this.quantityPicked = this.items.stream()
                    .mapToDouble(i -> i.getPickedQuantity() != null ? i.getPickedQuantity() : 0.0)
                    .sum();
            this.itemsPicked = (int) this.items.stream().filter(PickItem::getPicked).count();

            if (this.itemsPicked.equals(this.totalItems)) {
                this.status = PickStatus.COMPLETED;
            } else if (this.itemsPicked > 0) {
                this.status = PickStatus.PARTIALLY_COMPLETED;
            }

            this.updatedAt = LocalDateTime.now();
        }
    }

    public Double getProgressPercentage() {
        if (totalQuantity == null || totalQuantity == 0) {
            return 0.0;
        }
        return (quantityPicked != null ? quantityPicked : 0.0) / totalQuantity * 100.0;
    }

    private PickItem findItemById(String itemId) {
        return items.stream().filter(i -> i.getItemId().equals(itemId)).findFirst().orElse(null);
    }
}
