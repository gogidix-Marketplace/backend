package com.gogidix.shared.infrastructure.services.security.auth.domain.port.out;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;

import java.util.Optional;

/**
 * User repository output port.
 * <p>
 * Defines the contract for user persistence operations.
 */
public interface UserRepositoryPort {

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Finds a user by ID and tenant ID.
     *
     * @param userId the user ID
     * @param tenantId the tenant ID
     * @return the user if found
     */
    Optional<User> findByIdAndTenantId(String userId, String tenantId);

    /**
     * Finds a user by username and tenant ID.
     *
     * @param username the username
     * @param tenantId the tenant ID
     * @return the user if found
     */
    Optional<User> findByUsernameAndTenantId(String username, String tenantId);

    /**
     * Finds a user by email and tenant ID.
     *
     * @param email the email
     * @param tenantId the tenant ID
     * @return the user if found
     */
    Optional<User> findByEmailAndTenantId(String email, String tenantId);

    /**
     * Checks if a username exists for a tenant.
     *
     * @param username the username
     * @param tenantId the tenant ID
     * @return true if the username exists
     */
    boolean existsByUsernameAndTenantId(String username, String tenantId);

    /**
     * Checks if an email exists for a tenant.
     *
     * @param email the email
     * @param tenantId the tenant ID
     * @return true if the email exists
     */
    boolean existsByEmailAndTenantId(String email, String tenantId);

    /**
     * Deletes a user.
     *
     * @param user the user to delete
     */
    void delete(User user);
}
