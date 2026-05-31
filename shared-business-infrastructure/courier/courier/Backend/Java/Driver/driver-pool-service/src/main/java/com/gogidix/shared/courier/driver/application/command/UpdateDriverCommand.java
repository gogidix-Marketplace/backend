package com.gogidix.shared.courier.driver.application.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to update an existing driver profile
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverCommand {

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    private String vehicleType;

    private String licensePlate;
}
