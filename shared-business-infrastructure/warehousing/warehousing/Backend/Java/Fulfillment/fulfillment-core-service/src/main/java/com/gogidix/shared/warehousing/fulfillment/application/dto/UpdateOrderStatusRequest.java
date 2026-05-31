package com.gogidix.shared.warehousing.fulfillment.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for updating order status
 */
@Data
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;

    private String trackingNumber;
    private String carrier;
}
