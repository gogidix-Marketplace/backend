package com.gogidix.shared.warehousing.inventory.application.command;

import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory.LocationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command to update an existing inventory item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryCommand {

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;

    @NotBlank(message = "Location ID is required")
    private String locationId;

    @NotNull(message = "Location type is required")
    private LocationType locationType;

    private Map<String, Object> attributes;
}
