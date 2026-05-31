package com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response;

import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile.Address;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile.UserPreferences;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * User profile response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {

    private String id;
    private String userId;
    private String tenantId;
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String bio;
    private Address address;
    private UserPreferences preferences;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
