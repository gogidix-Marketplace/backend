package com.gogidix.shared.warehousing.fulfillment.analytics.domain.entity;

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
@Document(collection = "order_cycle_times")
@CompoundIndex(def = "{'tenantId': 1, 'orderId': 1}", name = "idx_tenant_order")
public class OrderCycleTime {
    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String orderId;
    @Indexed
    private String warehouseId;

    // Timestamps
    private LocalDateTime orderReceived;
    private LocalDateTime orderPicked;
    private LocalDateTime orderPacked;
    private LocalDateTime orderShipped;
    private LocalDateTime orderDelivered;

    // Duration calculations (in minutes)
    private Long pickDuration;
    private Long packDuration;
    private Long shipDuration;
    private Long totalCycleTime;

    // Status
    private String status;
    private Boolean onTime;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
