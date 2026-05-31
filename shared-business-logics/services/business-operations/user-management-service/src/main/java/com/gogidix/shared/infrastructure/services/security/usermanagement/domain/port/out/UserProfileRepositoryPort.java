package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;

import java.util.List;
import java.util.Optional;

/**
 * User Profile repository output port.
 */
public interface UserProfileRepositoryPort {

    /**
     * Saves a user profile.
     */
    UserProfile save(UserProfile userProfile);

    /**
     * Finds a user profile by ID and tenant ID.
     */
    Optional<UserProfile> findByIdAndTenantId(String userId, String tenantId);

    /**
     * Finds all user profiles for a tenant.
     */
    List<UserProfile> findByTenantId(String tenantId);

    /**
     * Deletes a user profile.
     */
    void delete(UserProfile userProfile);

    /**
     * Checks if a user exists.
     */
    boolean existsByIdAndTenantId(String userId, String tenantId);
}
