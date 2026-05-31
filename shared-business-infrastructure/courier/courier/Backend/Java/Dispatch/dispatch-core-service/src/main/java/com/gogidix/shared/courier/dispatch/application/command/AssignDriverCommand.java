package com.gogidix.shared.courier.dispatch.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to assign a driver to a dispatch order
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignDriverCommand {

    @NotBlank(message = "Driver ID is required")
    private String driverId;

    private String vehicleId;

    private String notes;
}
