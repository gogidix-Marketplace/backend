package com.gogidix.shared.warehousing.stock.application.command;

import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement.MovementType;
import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement.ReferenceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to adjust stock quantity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdjustStockCommand {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Movement type is required")
    private MovementType movementType;

    private ReferenceType referenceType;

    private String referenceId;

    private String reason;

    private String performedBy;
}
