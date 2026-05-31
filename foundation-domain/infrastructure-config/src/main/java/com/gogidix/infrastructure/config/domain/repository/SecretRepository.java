package com.gogidix.infrastructure.config.domain.repository;

import com.gogidix.infrastructure.config.domain.model.Secret;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for Secret entities.
 */
@Repository
public interface SecretRepository extends MongoRepository<Secret, String> {

    /**
     * Find all secrets by tenant ID.
     */
    List<Secret> findByTenantId(String tenantId);

    /**
     * Find secrets by tenant ID with pagination.
     */
    Page<Secret> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find secret by tenant ID and secret key.
     */
    Optional<Secret> findByTenantIdAndSecretKey(String tenantId, String secretKey);

    /**
     * Find active secrets by tenant ID.
     */
    List<Secret> findByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Find valid secrets (active and not expired) by tenant ID.
     */
    @Query("{ 'tenantId': ?0, 'isActive': true, $or: [ { 'expiresAt': null }, { 'expiresAt': { $gt: ?1 } } ] }")
    List<Secret> findValidSecrets(String tenantId, LocalDateTime currentTime);

    /**
     * Find secrets by tenant ID and secret type.
     */
    List<Secret> findByTenantIdAndSecretType(String tenantId, Secret.SecretType secretType);

    /**
     * Find secrets by tenant ID and category.
     */
    List<Secret> findByTenantIdAndCategory(String tenantId, String category);

    /**
     * Find secrets by tenant ID and tags.
     */
    List<Secret> findByTenantIdAndTagsIn(String tenantId, List<String> tags);

    /**
     * Find secrets by owner.
     */
    List<Secret> findByTenantIdAndOwner(String tenantId, String owner);

    /**
     * Find secrets that need rotation.
     */
    @Query("{ 'tenantId': ?0, 'rotationIntervalDays': { $ne: null }, $or: [ { 'nextRotationAt': null }, { 'nextRotationAt': { $lte: ?1 } } ] }")
    List<Secret> findSecretsNeedingRotation(String tenantId, LocalDateTime currentTime);

    /**
     * Find expired secrets.
     */
    @Query("{ 'tenantId': ?0, 'expiresAt': { $ne: null, $lte: ?1 } }")
    List<Secret> findExpiredSecrets(String tenantId, LocalDateTime currentTime);

    /**
     * Search secrets by key or name pattern.
     */
    @Query("{ 'tenantId': ?0, $or: [ { 'secretKey': { $regex: ?1, $options: 'i' } }, { 'name': { $regex: ?1, $options: 'i' } } ] }")
    List<Secret> searchByKeyOrName(String tenantId, String pattern);

    /**
     * Count secrets by tenant ID.
     */
    long countByTenantId(String tenantId);

    /**
     * Count active secrets by tenant ID.
     */
    long countByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Delete all secrets for a tenant.
     */
    void deleteByTenantId(String tenantId);

    /**
     * Find secrets created after a given timestamp.
     */
    List<Secret> findByTenantIdAndCreatedAtAfter(String tenantId, LocalDateTime timestamp);

    /**
     * Find secrets recently accessed.
     */
    @Query("{ 'tenantId': ?0, 'lastAccessedAt': { $ne: null, $gte: ?1 } }")
    List<Secret> findRecentlyAccessedSecrets(String tenantId, LocalDateTime since);

    /**
     * Find secrets expiring soon.
     */
    @Query("{ 'tenantId': ?0, 'expiresAt': { $ne: null, $lte: ?1 } }")
    List<Secret> findSecretsExpiringBefore(String tenantId, LocalDateTime dateTime);
}
