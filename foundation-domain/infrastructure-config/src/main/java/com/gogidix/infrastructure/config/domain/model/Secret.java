package com.gogidix.infrastructure.config.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * Domain model for secret storage.
 *
 * <p>Provides secure storage for sensitive configuration values:</p>
 * <ul>
 *   <li>AES-256 encryption for all secret values</li>
 *   <li>Automatic rotation support</li>
 *   <li>Audit logging for access</li>
 *   <li>TTL-based expiration</li>
 *   <li>Access control lists</li>
 * </ul>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "secrets")
public class Secret {

    /**
     * Unique identifier for the secret.
     */
    @Id
    private String id;

    /**
     * Tenant identifier for multi-tenancy support.
     */
    @Indexed
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    /**
     * Unique key for the secret within tenant scope.
     */
    @Indexed
    @NotBlank(message = "Secret key is required")
    private String secretKey;

    /**
     * Human-readable name for the secret.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Detailed description of the secret.
     */
    private String description;

    /**
     * Encrypted secret value.
     */
    @NotBlank(message = "Secret value is required")
    private String encryptedValue;

    /**
     * IV (Initialization Vector) used for encryption.
     */
    private String iv;

    /**
     * Type/Category of secret (API_KEY, PASSWORD, CERTIFICATE, etc).
     */
    @Indexed
    @NotNull(message = "Secret type is required")
    private SecretType secretType;

    /**
     * User who created this secret.
     */
    private String createdBy;

    /**
     * User who last updated this secret.
     */
    private String lastUpdatedBy;

    /**
     * Current version number for rotation tracking.
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Tags for categorization and search.
     */
    private Set<String> tags;

    /**
     * Category for grouping related secrets.
     */
    private String category;

    /**
     * Owner/team responsible for this secret.
     */
    private String owner;

    /**
     * Access control list - list of user IDs allowed to access this secret.
     */
    private Set<String> accessControlList;

    /**
     * Rotation interval in days.
     */
    private Integer rotationIntervalDays;

    /**
     * Timestamp when the secret was last rotated.
     */
    private LocalDateTime lastRotatedAt;

    /**
     * Timestamp when the secret is due for rotation.
     */
    private LocalDateTime nextRotationAt;

    /**
     * Timestamp when this secret expires.
     */
    private LocalDateTime expiresAt;

    /**
     * Metadata associated with this secret.
     */
    private Map<String, Object> metadata;

    /**
     * Timestamp when this secret was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Timestamp when this secret was last modified.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Timestamp of the last access to this secret.
     */
    private LocalDateTime lastAccessedAt;

    /**
     * User who last accessed this secret.
     */
    private String lastAccessedBy;

    /**
     * Number of times this secret has been accessed.
     */
    @Builder.Default
    private Integer accessCount = 0;

    /**
     * Whether this secret is currently active.
     */
    @Builder.Default
    private boolean isActive = true;

    /**
     * Secret type enumeration.
     */
    public enum SecretType {
        API_KEY,
        DATABASE_PASSWORD,
        OAUTH_TOKEN,
        CERTIFICATE,
        SSH_KEY,
        ENCRYPTION_KEY,
        WEBHOOK_SECRET,
        JWT_SECRET,
        THIRD_PARTY_KEY,
        GENERIC_PASSWORD,
        SERVICE_ACCOUNT_KEY,
        ACCESS_TOKEN,
        REFRESH_TOKEN
    }

    /**
     * Checks if this secret needs rotation.
     *
     * @return true if the secret is due for rotation
     */
    public boolean needsRotation() {
        if (rotationIntervalDays == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return nextRotationAt == null || now.isAfter(nextRotationAt);
    }

    /**
     * Checks if this secret has expired.
     *
     * @return true if the secret has expired
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Checks if this secret is currently valid (active and not expired).
     *
     * @return true if the secret is valid
     */
    public boolean isValid() {
        return isActive && !isExpired();
    }

    /**
     * Checks if a user is allowed to access this secret.
     *
     * @param userId The user ID to check
     * @return true if the user has access
     */
    public boolean hasAccess(String userId) {
        if (accessControlList == null || accessControlList.isEmpty()) {
            return true; // No ACL means open access
        }
        return accessControlList.contains(userId);
    }

    /**
     * Records an access to this secret.
     *
     * @param userId The user who accessed the secret
     */
    public void recordAccess(String userId) {
        this.lastAccessedAt = LocalDateTime.now();
        this.lastAccessedBy = userId;
        this.accessCount = (this.accessCount != null ? this.accessCount : 0) + 1;
    }

    /**
     * Updates the rotation schedule based on the rotation interval.
     */
    public void updateRotationSchedule() {
        if (rotationIntervalDays != null && rotationIntervalDays > 0) {
            this.lastRotatedAt = LocalDateTime.now();
            this.nextRotationAt = this.lastRotatedAt.plusDays(rotationIntervalDays);
        }
    }

    /**
     * Increments the version number.
     */
    public void incrementVersion() {
        this.version = (this.version != null ? this.version : 0) + 1;
    }
}
