package com.gogidix.shared.infrastructure.services.security.usermanagement.application.mapper;

import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import org.springframework.stereotype.Component;

/**
 * Mapper for UserProfile entity and DTOs.
 */
@Component
public class UserProfileMapper {

    public UserProfileResponseDto toResponseDto(UserProfile profile) {
        if (profile == null) {
            return null;
        }

        return UserProfileResponseDto.builder()
                .id(profile.getId())
                .userId(profile.getUserId())
                .tenantId(profile.getTenantId() != null ? profile.getTenantId().getValue() : null)
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .displayName(profile.getDisplayName())
                .email(profile.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .address(profile.getAddress())
                .preferences(profile.getPreferences())
                .metadata(profile.getMetadata())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
