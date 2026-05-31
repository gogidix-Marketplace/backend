package com.gogidix.shared.warehousing.receiving.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Document(collection = "receiving_orders")
@CompoundIndex(def = "{'tenantId': 1, 'orderNumber': 1}", name = "idx_tenant_order")
@Schema(description = "Receiving order entity")
public class ReceivingOrder {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String orderNumber;

    private ReceivingStatus status;

    private String warehouseId;

    private String dockDoor;

    private LocalDateTime scheduledDate;

    private LocalDateTime arrivedDate;

    private LocalDateTime completedDate;

    private List<ReceivingLine> lines;

    private String carrier;

    private String vehicleNumber;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum ReceivingStatus {
        SCHEDULED, ARRIVED, IN_RECEIVING, COMPLETED, CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceivingLine {
        private Integer lineNumber;
        private String sku;
        private String productName;
        private Integer expectedQuantity;
        private Integer receivedQuantity;
        private LineStatus status;
        private String locationId;

        public enum LineStatus {
            PENDING, PARTIALLY_RECEIVED, RECEIVED, DAMAGED
        }
    }
}
