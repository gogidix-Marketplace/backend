package com.gogidix.shared.infrastructure.services.security.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Refresh token request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDto {

    /**
     * The refresh token.
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
