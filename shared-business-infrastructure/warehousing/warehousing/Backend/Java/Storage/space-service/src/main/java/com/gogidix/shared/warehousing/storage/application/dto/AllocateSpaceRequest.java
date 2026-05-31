package com.gogidix.shared.warehousing.storage.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO for allocating storage space to a customer
 */
@Data
public class AllocateSpaceRequest {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Required capacity is required")
    @Positive(message = "Required capacity must be positive")
    private Double requiredCapacityCubicMeters;

    private String notes;
}
