package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * User Management use case port.
 * <p>
 * Defines user lifecycle management operations.
 */
public interface UserManagementPort {

    /**
     * Creates a new user profile.
     *
     * @param request the create user request
     * @return the created user profile
     */
    UserProfileResponseDto createUser(CreateUserRequestDto request);

    /**
     * Updates an existing user profile.
     *
     * @param userId the user ID
     * @param request the update user request
     * @return the updated user profile
     */
    UserProfileResponseDto updateUser(String userId, UpdateUserRequestDto request);

    /**
     * Finds a user profile by ID.
     *
     * @param userId the user ID
     * @return the user profile if found
     */
    Optional<UserProfileResponseDto> findById(String userId);

    /**
     * Deletes a user profile.
     *
     * @param userId the user ID
     */
    void deleteUser(String userId);

    /**
     * Lists all users for the current tenant.
     *
     * @return list of user profiles
     */
    List<UserProfileResponseDto> listUsers();

    /**
     * Updates user preferences.
     *
     * @param userId the user ID
     * @param preferences the preferences to update
     * @return the updated user profile
     */
    UserProfileResponseDto updatePreferences(String userId, Object preferences);
}
