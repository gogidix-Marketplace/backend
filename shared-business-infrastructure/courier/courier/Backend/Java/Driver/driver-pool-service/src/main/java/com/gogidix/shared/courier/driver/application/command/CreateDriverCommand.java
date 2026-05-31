package com.gogidix.shared.courier.driver.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to create a new driver profile
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverCommand {

    @NotBlank(message = "Driver ID is required")
    private String driverId;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be valid")
    private String phone;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private String licensePlate;
}
