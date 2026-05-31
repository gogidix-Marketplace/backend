package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for assigning a driver to a dispatch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {

    @NotBlank(message = "Driver ID is required")
    private String driverId;

    private String vehicleId;

    private String notes;
}
