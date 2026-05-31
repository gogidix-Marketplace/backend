package com.gogidix.shared.infrastructure.services.security.auth.application.mapper;

import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper for User entity and DTOs.
 */
@Component
public class UserMapper {

    /**
     * Converts User entity to UserResponseDto.
     *
     * @param user the user entity
     * @return the user response DTO
     */
    public UserResponseDto toResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .tenantId(user.getTenantId() != null ? user.getTenantId().getValue() : null)
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnabled())
                .roles(user.getRoles())
                .permissions(user.getPermissions())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Updates User entity from UserResponseDto.
     *
     * @param user the user entity to update
     * @param dto the user response DTO
     */
    public void updateFromDto(User user, UserResponseDto dto) {
        if (dto == null) {
            return;
        }

        if (dto.getFirstName() != null) {
            user.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            user.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
    }
}
