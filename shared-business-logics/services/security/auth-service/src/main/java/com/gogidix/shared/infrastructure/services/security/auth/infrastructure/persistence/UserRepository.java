package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for User entity.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by ID and tenant ID.
     *
     * @param id the user ID
     * @param tenantId the tenant ID value
     * @return the user if found
     */
    Optional<User> findByIdAndTenantId_Value(String id, String tenantId);

    /**
     * Finds a user by username and tenant ID.
     *
     * @param username the username
     * @param tenantId the tenant ID value
     * @return the user if found
     */
    Optional<User> findByUsernameAndTenantId_Value(String username, String tenantId);

    /**
     * Finds a user by email and tenant ID.
     *
     * @param email the email
     * @param tenantId the tenant ID value
     * @return the user if found
     */
    Optional<User> findByEmailAndTenantId_Value(String email, String tenantId);

    /**
     * Checks if a username exists for a tenant.
     *
     * @param username the username
     * @param tenantId the tenant ID value
     * @return true if the username exists
     */
    boolean existsByUsernameAndTenantId_Value(String username, String tenantId);

    /**
     * Checks if an email exists for a tenant.
     *
     * @param email the email
     * @param tenantId the tenant ID value
     * @return true if the email exists
     */
    boolean existsByEmailAndTenantId_Value(String email, String tenantId);
}
