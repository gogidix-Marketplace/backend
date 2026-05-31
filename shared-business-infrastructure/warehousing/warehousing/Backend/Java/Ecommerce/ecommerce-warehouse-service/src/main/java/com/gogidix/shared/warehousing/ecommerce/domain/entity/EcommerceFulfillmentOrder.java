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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ecommerce_fulfillment_orders")
@CompoundIndex(def = "{'orderId': 1, 'warehouseId': 1}", name = "idx_order_warehouse")
@CompoundIndex(def = "{'vendorId': 1, 'status': 1}", name = "idx_vendor_status")
public class EcommerceFulfillmentOrder {

    @Id
    private String id;

    @Indexed
    private String fulfillmentId;

    @Indexed
    private String orderId;

    @Indexed
    private String subOrderId;

    @Indexed
    private String vendorId;

    @Indexed
    private String warehouseId;

    @Indexed
    private String zoneId;

    private String status;
    private String priority;
    private LocalDateTime deliveryDeadline;
    private String deliveryType;
    private List<FulfillmentItem> items;
    private CustomerAddress customerAddress;
    private String specialInstructions;
    private LocalDateTime estimatedCompletion;
    private List<FulfillmentStage> stages;

    private AssignedStaff assignedStaff;
    private FulfillmentProgress progress;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FulfillmentItem {
        private String sku;
        private String productName;
        private Integer quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerAddress {
        private String address;
        private Double latitude;
        private Double longitude;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FulfillmentStage {
        private String stage;
        private String status;
        private LocalDateTime completedAt;
        private LocalDateTime startedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AssignedStaff {
        private String name;
        private String role;
        private LocalDateTime startedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FulfillmentProgress {
        private Integer itemsPicked;
        private Integer totalItems;
        private Integer percentageComplete;
    }

    public void initializeStages() {
        this.stages = new java.util.ArrayList<>(List.of(
            FulfillmentStage.builder().stage("RECEIVED").status("COMPLETED").completedAt(LocalDateTime.now()).build(),
            FulfillmentStage.builder().stage("PICKING").status("PENDING").build(),
            FulfillmentStage.builder().stage("PACKING").status("PENDING").build(),
            FulfillmentStage.builder().stage("READY_FOR_PICKUP").status("PENDING").build()
        ));
    }

    public void transitionTo(String newStatus) {
        this.status = newStatus;
        if (this.stages != null) {
            for (int i = 0; i < stages.size(); i++) {
                FulfillmentStage stage = stages.get(i);
                if (stage.getStage().equalsIgnoreCase(newStatus) && "PENDING".equals(stage.getStatus())) {
                    stages.set(i, FulfillmentStage.builder()
                        .stage(stage.getStage())
                        .status("IN_PROGRESS")
                        .startedAt(LocalDateTime.now())
                        .build());
                    break;
                }
            }
        }
        this.updatedAt = LocalDateTime.now();
    }
}
