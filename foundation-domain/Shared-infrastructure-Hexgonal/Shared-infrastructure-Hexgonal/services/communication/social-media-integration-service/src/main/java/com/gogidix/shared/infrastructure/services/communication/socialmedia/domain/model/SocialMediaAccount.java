package com.gogidix.shared.infrastructure.services.communication.socialmedia.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Social Media Account MongoDB Document
 * Multi-tenant support with tenant isolation.
 */
@Document(collection = "social_media_accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaAccount {

    @Field("tenant_id")
    @Indexed
    private TenantId tenantId;

    @Id
    private String id;

    // Domain context for cross-domain identification
    @Indexed
    private String domainContext;

    @Indexed
    private Long userId;

    @NotNull
    @Indexed
    private SocialPlatform platform;

    @NotBlank
    @Size(max = 255)
    @Indexed
    private String platformUserId;

    @NotBlank
    @Size(max = 255)
    @Indexed
    private String username;

    @Size(max = 255)
    private String displayName;

    @Size(max = 500)
    private String profileImageUrl;

    @Size(max = 1000)
    private String bio;

    private Long followerCount;

    private Long followingCount;

    @Builder.Default
    private Boolean verified = false;

    @Indexed
    private SyncStatus syncStatus = SyncStatus.ACTIVE;

    private String accessToken;

    private String refreshToken;

    private LocalDateTime tokenExpiresAt;

    private LocalDateTime lastSyncAt;

    @Builder.Default
    @Indexed
    private Boolean isActive = true;

    // Account-specific configurations (JSON storage)
    private String platformConfig;

    // Transient storage for platform configuration Map
    private transient Map<String, Object> platformConfigurationMap;

    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Social platform enum
    public enum SocialPlatform {
        FACEBOOK,
        TWITTER,
        INSTAGRAM,
        LINKEDIN,
        YOUTUBE,
        TIKTOK
    }

    // Sync status enum
    public enum SyncStatus {
        ACTIVE,
        INACTIVE,
        ERROR,
        PENDING_SYNC,
        SYNCING
    }

    // Business methods
    public boolean isTokenExpired() {
        return tokenExpiresAt != null && LocalDateTime.now().isAfter(tokenExpiresAt);
    }

    public boolean needsSync() {
        return lastSyncAt == null ||
               lastSyncAt.isBefore(LocalDateTime.now().minusHours(1));
    }

    public Map<String, Object> getPlatformConfiguration() {
        if (platformConfigurationMap == null) {
            platformConfigurationMap = new java.util.HashMap<>();
        }
        return platformConfigurationMap;
    }

    public void setPlatformConfiguration(Map<String, Object> config) {
        this.platformConfigurationMap = config != null ? new java.util.HashMap<>(config) : new java.util.HashMap<>();
    }
}
