package com.gogidix.shared.infrastructure.services.security.usermanagement.application.service;

import com.gogidix.shared.servicediscovery.config.context.TenantContextHolder;
import com.gogidix.shared.servicediscovery.config.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.mapper.UserProfileMapper;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.exception.UserNotFoundException;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.in.UserManagementPort;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.out.UserProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Management Service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementService implements UserManagementPort {

    private final UserProfileRepositoryPort userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final TenantContextHolder tenantContextHolder;

    @Override
    @Transactional
    public UserProfileResponseDto createUser(CreateUserRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Creating user profile for userId: {} in tenant: {}", request.getUserId(), tenantId);

        UserProfile profile = UserProfile.builder()
                .userId(request.getUserId())
                .tenantId(TenantId.of(tenantId))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .displayName(request.getDisplayName() != null
                    ? request.getDisplayName()
                    : request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .avatarUrl(request.getAvatarUrl())
                .bio(request.getBio())
                .address(request.getAddress())
                .preferences(request.getPreferences() != null
                    ? request.getPreferences()
                    : UserProfile.UserPreferences.builder().build())
                .build();

        UserProfile saved = userProfileRepository.save(profile);
        log.info("Created user profile: {}", saved.getId());

        return userProfileMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public UserProfileResponseDto updateUser(String userId, UpdateUserRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Updating user profile for userId: {} in tenant: {}", userId, tenantId);

        UserProfile profile = userProfileRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new UserNotFoundException("userId", userId));

        // Update fields if provided
        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getDisplayName() != null) {
            profile.setDisplayName(request.getDisplayName());
        }
        if (request.getEmail() != null) {
            profile.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAvatarUrl() != null) {
            profile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }
        if (request.getPreferences() != null) {
            profile.setPreferences(request.getPreferences());
        }

        UserProfile saved = userProfileRepository.save(profile);
        log.info("Updated user profile: {}", saved.getId());

        return userProfileMapper.toResponseDto(saved);
    }

    @Override
    public Optional<UserProfileResponseDto> findById(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return userProfileRepository.findByIdAndTenantId(userId, tenantId)
                .map(userProfileMapper::toResponseDto);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Deleting user profile for userId: {} in tenant: {}", userId, tenantId);

        UserProfile profile = userProfileRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new UserNotFoundException("userId", userId));

        userProfileRepository.delete(profile);
        log.info("Deleted user profile: {}", profile.getId());
    }

    @Override
    public List<UserProfileResponseDto> listUsers() {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        return userProfileRepository.findByTenantId(tenantId).stream()
                .map(userProfileMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public UserProfileResponseDto updatePreferences(String userId, Object preferences) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        UserProfile profile = userProfileRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new UserNotFoundException("userId", userId));

        // TODO: Map preferences object to UserPreferences
        profile.setPreferences((UserProfile.UserPreferences) preferences);

        UserProfile saved = userProfileRepository.save(profile);
        return userProfileMapper.toResponseDto(saved);
    }
}
