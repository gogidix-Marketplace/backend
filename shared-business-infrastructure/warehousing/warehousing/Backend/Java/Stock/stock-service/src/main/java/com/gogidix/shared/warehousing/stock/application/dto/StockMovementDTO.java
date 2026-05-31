package com.gogidix.shared.warehousing.stock.application.dto;

import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement.MovementType;
import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Stock Movement Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementDTO {

    private String id;
    private String tenantId;
    private String sku;
    private String locationId;
    private MovementType movementType;
    private Integer quantity;
    private String referenceId;
    private ReferenceType referenceType;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String reason;
    private String performedBy;
    private LocalDateTime timestamp;
}
