package com.gogidix.shared.warehousing.stock.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stock_movements")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1}", name = "idx_tenant_sku")
public class StockMovement {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String sku;

    @Indexed
    private String locationId;

    private MovementType movementType;

    private Integer quantity;

    private String referenceId;

    private ReferenceType referenceType;

    private Integer previousQuantity;

    private Integer newQuantity;

    private String reason;

    private String performedBy;

    @CreatedDate
    private LocalDateTime timestamp;

    public enum MovementType {
        INBOUND_RECEIPT,
        INBOUND_RETURN,
        INBOUND_ADJUSTMENT,
        OUTBOUND_SHIPMENT,
        OUTBOUND_TRANSFER,
        OUTBOUND_ADJUSTMENT,
        RESERVE,
        RELEASE,
        ALLOCATE,
        CONFIRM_ALLOCATION,
        CYCLE_COUNT
    }

    public enum ReferenceType {
        PURCHASE_ORDER,
        SALES_ORDER,
        TRANSFER_ORDER,
        RETURN_ORDER,
        ADJUSTMENT,
        CYCLE_COUNT,
        SYSTEM
    }
}
