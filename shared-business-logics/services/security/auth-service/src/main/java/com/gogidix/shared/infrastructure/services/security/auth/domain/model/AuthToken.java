package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Authentication Token domain entity.
 * <p>
 * Represents an authentication token (JWT) issued to a user.
 * Supports token revocation and refresh token management.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "auth_tokens")
public class AuthToken {

    @Id
    private String id;

    @Indexed
    private String tokenId;

    @Indexed
    private String userId;

    @Indexed
    private String tenantId;

    @Indexed
    private String token;

    @Indexed
    private String accessToken;

    @Indexed
    private String refreshToken;

    private String tokenType;

    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;

    private LocalDateTime refreshExpiresAt;

    private boolean revoked;

    private boolean revokedByRefresh;

    private String ipAddress;

    private String userAgent;

    /**
     * Checks if the token is expired.
     *
     * @return true if the token is expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Checks if the refresh token is expired.
     *
     * @return true if the refresh token is expired
     */
    public boolean isRefreshExpired() {
        return refreshExpiresAt != null && LocalDateTime.now().isAfter(refreshExpiresAt);
    }

    /**
     * Checks if the token is valid (not expired and not revoked).
     *
     * @return true if the token is valid
     */
    public boolean isValid() {
        return !revoked && !isExpired();
    }

    /**
     * Revokes the token.
     */
    public void revoke() {
        this.revoked = true;
    }

    public boolean isRefreshable() {
        return !revoked && !isRefreshExpired();
    }
}
