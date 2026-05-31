package com.gogidix.shared.infrastructure.services.security.auth.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * User response DTO.
 * <p>
 * Represents user information returned to clients.
 * Password and sensitive information are excluded.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    /**
     * User ID.
     */
    private String id;

    /**
     * Tenant ID.
     */
    private String tenantId;

    /**
     * Username.
     */
    private String username;

    /**
     * Email address.
     */
    private String email;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Account enabled status.
     */
    private boolean enabled;

    /**
     * User roles.
     */
    private Set<String> roles;

    /**
     * User permissions.
     */
    private Set<String> permissions;

    /**
     * Last login timestamp.
     */
    private String lastLoginAt;

    /**
     * Account creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    private LocalDateTime updatedAt;
}
