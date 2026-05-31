package com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request;

import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile.Address;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile.UserPreferences;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create user request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100)
    private String lastName;

    private String displayName;

    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 20)
    private String phoneNumber;

    private String avatarUrl;

    private String bio;

    private Address address;

    private UserPreferences preferences;
}
