package com.gogidix.shared.infrastructure.services.security.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login request DTO.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /**
     * Username or email for authentication.
     */
    @NotBlank(message = "Username or email is required")
    private String username;

    /**
     * User password.
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Optional IP address for logging.
     */
    private String ipAddress;

    /**
     * Optional user agent for logging.
     */
    private String userAgent;
}
