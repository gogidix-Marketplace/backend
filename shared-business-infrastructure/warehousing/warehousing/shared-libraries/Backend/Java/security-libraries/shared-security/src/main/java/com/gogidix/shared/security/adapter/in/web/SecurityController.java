package com.gogidix.shared.security.adapter.in.web;

import com.gogidix.shared.security.application.port.in.SecurityTokenUseCase;
import com.gogidix.shared.security.domain.model.SecurityToken;
import com.gogidix.shared.security.domain.model.TokenType;
import com.gogidix.shared.security.domain.model.SecurityLevel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * REST controller for security token management operations.
 * Provides HTTP endpoints for token operations and authorization.
 */
@RestController
@RequestMapping("/api/v1/security")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Security Management", description = "Security token and authorization operations")
public class SecurityController {
    
    private final SecurityTokenUseCase securityTokenUseCase;
    
    public SecurityController(SecurityTokenUseCase securityTokenUseCase) {
        this.securityTokenUseCase = securityTokenUseCase;
    }
    
    @PostMapping("/tokens")
    @Operation(summary = "Create security token", description = "Creates a new security token for authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Token created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PreAuthorize("hasAuthority('TOKEN_CREATE')")
    public ResponseEntity<TokenResponse> createToken(@Valid @RequestBody CreateTokenDto request) {
        
        SecurityTokenUseCase.CreateTokenRequest createRequest = new SecurityTokenUseCase.CreateTokenRequest(
            request.getUserId(),
            request.getUsername(),
            request.getType(),
            request.getRoles(),
            request.getPermissions(),
            request.getSecurityLevel(),
            request.getDeviceId(),
            request.getIpAddress(),
            request.getUserAgent(),
            request.getCustomExpirationSeconds(),
            request.isMultiFactorAuthenticated()
        );
        
        SecurityTokenUseCase.TokenResult result = securityTokenUseCase.createToken(createRequest);
        
        if (result.isSuccess()) {
            return ResponseEntity.status(201).body(TokenResponse.success(result.getToken(), result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(TokenResponse.error(result.getMessage()));
        }
    }
    
    @PostMapping("/tokens/{tokenId}/validate")
    @Operation(summary = "Validate security token", description = "Validates a security token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed"),
        @ApiResponse(responseCode = "404", description = "Token not found")
    })
    public ResponseEntity<ValidationResponse> validateToken(
            @PathVariable String tokenId,
            @Valid @RequestBody ValidateTokenDto request) {
        
        SecurityTokenUseCase.ValidationResult result = securityTokenUseCase.validateToken(tokenId, request.getRawToken());
        
        return ResponseEntity.ok(ValidationResponse.from(result));
    }
    
    @PostMapping("/tokens/{tokenId}/refresh")
    @Operation(summary = "Refresh access token", description = "Refreshes an access token using a refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
        @ApiResponse(responseCode = "404", description = "Token not found")
    })
    public ResponseEntity<TokenResponse> refreshToken(
            @PathVariable String tokenId,
            @Valid @RequestBody RefreshTokenDto request) {
        
        SecurityTokenUseCase.TokenResult result = securityTokenUseCase.refreshToken(tokenId, request.getRefreshToken());
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(TokenResponse.success(result.getToken(), result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(TokenResponse.error(result.getMessage()));
        }
    }
    
    @PostMapping("/tokens/{tokenId}/revoke")
    @Operation(summary = "Revoke security token", description = "Revokes a security token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token revoked successfully"),
        @ApiResponse(responseCode = "404", description = "Token not found")
    })
    @PreAuthorize("hasAuthority('TOKEN_REVOKE') or @securityService.isTokenOwner(#tokenId, authentication.name)")
    public ResponseEntity<TokenResponse> revokeToken(
            @PathVariable String tokenId,
            @Valid @RequestBody RevokeTokenDto request) {
        
        SecurityTokenUseCase.TokenResult result = securityTokenUseCase.revokeToken(tokenId, request.getReason());
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(TokenResponse.success(result.getToken(), result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(TokenResponse.error(result.getMessage()));
        }
    }
    
    @PostMapping("/tokens/{tokenId}/suspend")
    @Operation(summary = "Suspend security token", description = "Temporarily suspends a security token")
    @PreAuthorize("hasAuthority('TOKEN_SUSPEND')")
    public ResponseEntity<TokenResponse> suspendToken(
            @PathVariable String tokenId,
            @Valid @RequestBody SuspendTokenDto request) {
        
        SecurityTokenUseCase.TokenResult result = securityTokenUseCase.suspendToken(
            tokenId, request.getReason(), request.getSuspendDurationSeconds());
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(TokenResponse.success(result.getToken(), result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(TokenResponse.error(result.getMessage()));
        }
    }
    
    @PostMapping("/tokens/{tokenId}/reactivate")
    @Operation(summary = "Reactivate suspended token", description = "Reactivates a suspended security token")
    @PreAuthorize("hasAuthority('TOKEN_REACTIVATE')")
    public ResponseEntity<TokenResponse> reactivateToken(@PathVariable String tokenId) {
        
        SecurityTokenUseCase.TokenResult result = securityTokenUseCase.reactivateToken(tokenId);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(TokenResponse.success(result.getToken(), result.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(TokenResponse.error(result.getMessage()));
        }
    }
    
    @GetMapping("/tokens/{tokenId}")
    @Operation(summary = "Get token information", description = "Retrieves information about a security token")
    @PreAuthorize("hasAuthority('TOKEN_READ') or @securityService.isTokenOwner(#tokenId, authentication.name)")
    public ResponseEntity<TokenInfoResponse> getToken(@PathVariable String tokenId) {
        
        return securityTokenUseCase.getToken(tokenId)
            .map(token -> ResponseEntity.ok(TokenInfoResponse.from(token)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/users/{userId}/tokens")
    @Operation(summary = "Get user tokens", description = "Retrieves all tokens for a specific user")
    @PreAuthorize("hasAuthority('TOKEN_READ_ALL') or #userId == authentication.name")
    public ResponseEntity<List<TokenInfoResponse>> getUserTokens(
            @PathVariable String userId,
            @RequestParam(required = false) TokenType type) {
        
        List<SecurityToken> tokens = type != null ? 
            securityTokenUseCase.getUserTokens(userId, type) :
            securityTokenUseCase.getUserTokens(userId, null);
        
        List<TokenInfoResponse> response = tokens.stream()
            .map(TokenInfoResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{userId}/sessions")
    @Operation(summary = "Get active sessions", description = "Retrieves active sessions for a user")
    @PreAuthorize("hasAuthority('SESSION_READ') or #userId == authentication.name")
    public ResponseEntity<List<TokenInfoResponse>> getActiveSessions(@PathVariable String userId) {
        
        List<SecurityToken> sessions = securityTokenUseCase.getActiveSessions(userId);
        
        List<TokenInfoResponse> response = sessions.stream()
            .map(TokenInfoResponse::from)
            .toList();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/tokens/{tokenId}/authorize")
    @Operation(summary = "Check authorization", description = "Checks if token has required permissions")
    public ResponseEntity<AuthorizationResponse> authorize(
            @PathVariable String tokenId,
            @Valid @RequestBody AuthorizeDto request) {
        
        SecurityTokenUseCase.AuthorizationResult result = securityTokenUseCase.authorize(tokenId, request.getRequiredPermissions());
        
        return ResponseEntity.ok(AuthorizationResponse.from(result));
    }
    
    @PostMapping("/tokens/{tokenId}/authorize-roles")
    @Operation(summary = "Check role authorization", description = "Checks if token has required roles")
    public ResponseEntity<AuthorizationResponse> authorizeRoles(
            @PathVariable String tokenId,
            @Valid @RequestBody AuthorizeRolesDto request) {
        
        SecurityTokenUseCase.AuthorizationResult result = securityTokenUseCase.authorizeRoles(tokenId, request.getRequiredRoles());
        
        return ResponseEntity.ok(AuthorizationResponse.from(result));
    }
    
    @GetMapping("/tokens/{tokenId}/permissions")
    @Operation(summary = "Get user permissions", description = "Gets permissions associated with a token")
    public ResponseEntity<Set<String>> getUserPermissions(@PathVariable String tokenId) {
        
        Set<String> permissions = securityTokenUseCase.getUserPermissions(tokenId);
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/tokens/{tokenId}/roles")
    @Operation(summary = "Get user roles", description = "Gets roles associated with a token")
    public ResponseEntity<Set<String>> getUserRoles(@PathVariable String tokenId) {
        
        Set<String> roles = securityTokenUseCase.getUserRoles(tokenId);
        return ResponseEntity.ok(roles);
    }
    
    @PostMapping("/users/{userId}/tokens/revoke-all")
    @Operation(summary = "Revoke all user tokens", description = "Revokes all tokens for a specific user")
    @PreAuthorize("hasAuthority('TOKEN_REVOKE_ALL') or #userId == authentication.name")
    public ResponseEntity<BulkTokenResponse> revokeAllUserTokens(
            @PathVariable String userId,
            @Valid @RequestBody RevokeAllTokensDto request) {
        
        SecurityTokenUseCase.BulkTokenResult result = securityTokenUseCase.revokeAllUserTokens(userId, request.getReason());
        
        return ResponseEntity.ok(BulkTokenResponse.from(result));
    }
    
    @PostMapping("/users/{userId}/tokens/revoke-type")
    @Operation(summary = "Revoke user tokens by type", description = "Revokes all tokens of a specific type for a user")
    @PreAuthorize("hasAuthority('TOKEN_REVOKE_TYPE') or #userId == authentication.name")
    public ResponseEntity<BulkTokenResponse> revokeUserTokensByType(
            @PathVariable String userId,
            @Valid @RequestBody RevokeTokensByTypeDto request) {
        
        SecurityTokenUseCase.BulkTokenResult result = securityTokenUseCase.revokeUserTokensByType(
            userId, request.getType(), request.getReason());
        
        return ResponseEntity.ok(BulkTokenResponse.from(result));
    }
    
    @PostMapping("/admin/cleanup-expired")
    @Operation(summary = "Cleanup expired tokens", description = "Removes expired tokens from the system")
    @PreAuthorize("hasAuthority('TOKEN_CLEANUP')")
    public ResponseEntity<CleanupResponse> cleanupExpiredTokens() {
        
        SecurityTokenUseCase.CleanupResult result = securityTokenUseCase.cleanupExpiredTokens();
        
        return ResponseEntity.ok(CleanupResponse.from(result));
    }
    
    @GetMapping("/users/{userId}/statistics")
    @Operation(summary = "Get token statistics", description = "Gets token usage statistics for a user")
    @PreAuthorize("hasAuthority('TOKEN_STATS') or #userId == authentication.name")
    public ResponseEntity<TokenStatisticsResponse> getTokenStatistics(@PathVariable String userId) {
        
        SecurityTokenUseCase.TokenStatistics statistics = securityTokenUseCase.getTokenStatistics(userId);
        
        return ResponseEntity.ok(TokenStatisticsResponse.from(statistics));
    }
    
    @PostMapping("/tokens/{tokenId}/validate-policy")
    @Operation(summary = "Validate security policy", description = "Validates token against security policies")
    public ResponseEntity<PolicyValidationResponse> validateSecurityPolicy(
            @PathVariable String tokenId,
            @Valid @RequestBody ValidatePolicyDto request) {
        
        SecurityTokenUseCase.PolicyValidationResult result = securityTokenUseCase.validateSecurityPolicy(tokenId, request.getOperation());
        
        return ResponseEntity.ok(PolicyValidationResponse.from(result));
    }
    
    // DTO Classes for Request/Response
    
    public static class CreateTokenDto {
        @NotBlank private String userId;
        @NotBlank private String username;
        @NotNull private TokenType type;
        private Set<String> roles;
        private Set<String> permissions;
        private SecurityLevel securityLevel = SecurityLevel.STANDARD;
        private String deviceId;
        private String ipAddress;
        private String userAgent;
        private Long customExpirationSeconds;
        private boolean multiFactorAuthenticated = false;
        
        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public TokenType getType() { return type; }
        public void setType(TokenType type) { this.type = type; }
        public Set<String> getRoles() { return roles; }
        public void setRoles(Set<String> roles) { this.roles = roles; }
        public Set<String> getPermissions() { return permissions; }
        public void setPermissions(Set<String> permissions) { this.permissions = permissions; }
        public SecurityLevel getSecurityLevel() { return securityLevel; }
        public void setSecurityLevel(SecurityLevel securityLevel) { this.securityLevel = securityLevel; }
        public String getDeviceId() { return deviceId; }
        public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
        public String getUserAgent() { return userAgent; }
        public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
        public Long getCustomExpirationSeconds() { return customExpirationSeconds; }
        public void setCustomExpirationSeconds(Long customExpirationSeconds) { this.customExpirationSeconds = customExpirationSeconds; }
        public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
        public void setMultiFactorAuthenticated(boolean multiFactorAuthenticated) { this.multiFactorAuthenticated = multiFactorAuthenticated; }
    }
    
    public static class ValidateTokenDto {
        @NotBlank private String rawToken;
        
        public String getRawToken() { return rawToken; }
        public void setRawToken(String rawToken) { this.rawToken = rawToken; }
    }
    
    public static class RefreshTokenDto {
        @NotBlank private String refreshToken;
        
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
    
    public static class RevokeTokenDto {
        @NotBlank private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class SuspendTokenDto {
        @NotBlank private String reason;
        private long suspendDurationSeconds = 3600; // 1 hour default
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public long getSuspendDurationSeconds() { return suspendDurationSeconds; }
        public void setSuspendDurationSeconds(long suspendDurationSeconds) { this.suspendDurationSeconds = suspendDurationSeconds; }
    }
    
    public static class AuthorizeDto {
        @NotNull private Set<String> requiredPermissions;
        
        public Set<String> getRequiredPermissions() { return requiredPermissions; }
        public void setRequiredPermissions(Set<String> requiredPermissions) { this.requiredPermissions = requiredPermissions; }
    }
    
    public static class AuthorizeRolesDto {
        @NotNull private Set<String> requiredRoles;
        
        public Set<String> getRequiredRoles() { return requiredRoles; }
        public void setRequiredRoles(Set<String> requiredRoles) { this.requiredRoles = requiredRoles; }
    }
    
    public static class RevokeAllTokensDto {
        @NotBlank private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class RevokeTokensByTypeDto {
        @NotNull private TokenType type;
        @NotBlank private String reason;
        
        public TokenType getType() { return type; }
        public void setType(TokenType type) { this.type = type; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class ValidatePolicyDto {
        @NotBlank private String operation;
        
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
    }
    
    // Response Classes
    
    public static class TokenResponse {
        private boolean success;
        private SecurityToken token;
        private String message;
        
        public static TokenResponse success(SecurityToken token, String message) {
            TokenResponse response = new TokenResponse();
            response.success = true;
            response.token = token;
            response.message = message;
            return response;
        }
        
        public static TokenResponse error(String message) {
            TokenResponse response = new TokenResponse();
            response.success = false;
            response.message = message;
            return response;
        }
        
        public boolean isSuccess() { return success; }
        public SecurityToken getToken() { return token; }
        public String getMessage() { return message; }
    }
    
    public static class ValidationResponse {
        private boolean valid;
        private String reason;
        private long remainingTimeSeconds;
        private boolean willExpireSoon;
        
        public static ValidationResponse from(SecurityTokenUseCase.ValidationResult result) {
            ValidationResponse response = new ValidationResponse();
            response.valid = result.isValid();
            response.reason = result.getReason();
            response.remainingTimeSeconds = result.getRemainingTimeSeconds();
            response.willExpireSoon = result.willExpireSoon();
            return response;
        }
        
        public boolean isValid() { return valid; }
        public String getReason() { return reason; }
        public long getRemainingTimeSeconds() { return remainingTimeSeconds; }
        public boolean isWillExpireSoon() { return willExpireSoon; }
    }
    
    public static class TokenInfoResponse {
        private String tokenId;
        private String userId;
        private String username;
        private TokenType type;
        private String status;
        private java.time.LocalDateTime issuedAt;
        private java.time.LocalDateTime expiresAt;
        private Set<String> roles;
        private Set<String> permissions;
        private boolean multiFactorAuthenticated;
        
        public static TokenInfoResponse from(SecurityToken token) {
            TokenInfoResponse response = new TokenInfoResponse();
            response.tokenId = token.getTokenId();
            response.userId = token.getUserId();
            response.username = token.getUsername();
            response.type = token.getType();
            response.status = token.getStatus().getDisplayName();
            response.issuedAt = token.getIssuedAt();
            response.expiresAt = token.getExpiresAt();
            response.roles = token.getRoles();
            response.permissions = token.getPermissions();
            response.multiFactorAuthenticated = token.isMultiFactorAuthenticated();
            return response;
        }
        
        // Getters
        public String getTokenId() { return tokenId; }
        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public TokenType getType() { return type; }
        public String getStatus() { return status; }
        public java.time.LocalDateTime getIssuedAt() { return issuedAt; }
        public java.time.LocalDateTime getExpiresAt() { return expiresAt; }
        public Set<String> getRoles() { return roles; }
        public Set<String> getPermissions() { return permissions; }
        public boolean isMultiFactorAuthenticated() { return multiFactorAuthenticated; }
    }
    
    public static class AuthorizationResponse {
        private boolean authorized;
        private Set<String> grantedPermissions;
        private Set<String> missingPermissions;
        private String reason;
        
        public static AuthorizationResponse from(SecurityTokenUseCase.AuthorizationResult result) {
            AuthorizationResponse response = new AuthorizationResponse();
            response.authorized = result.isAuthorized();
            response.grantedPermissions = result.getGrantedPermissions();
            response.missingPermissions = result.getMissingPermissions();
            response.reason = result.getReason();
            return response;
        }
        
        public boolean isAuthorized() { return authorized; }
        public Set<String> getGrantedPermissions() { return grantedPermissions; }
        public Set<String> getMissingPermissions() { return missingPermissions; }
        public String getReason() { return reason; }
    }
    
    public static class BulkTokenResponse {
        private int totalTokens;
        private int successfulOperations;
        private int failedOperations;
        private List<String> failedTokenIds;
        private String message;
        private double successRate;
        
        public static BulkTokenResponse from(SecurityTokenUseCase.BulkTokenResult result) {
            BulkTokenResponse response = new BulkTokenResponse();
            response.totalTokens = result.getTotalTokens();
            response.successfulOperations = result.getSuccessfulOperations();
            response.failedOperations = result.getFailedOperations();
            response.failedTokenIds = result.getFailedTokenIds();
            response.message = result.getMessage();
            response.successRate = result.getSuccessRate();
            return response;
        }
        
        public int getTotalTokens() { return totalTokens; }
        public int getSuccessfulOperations() { return successfulOperations; }
        public int getFailedOperations() { return failedOperations; }
        public List<String> getFailedTokenIds() { return failedTokenIds; }
        public String getMessage() { return message; }
        public double getSuccessRate() { return successRate; }
    }
    
    public static class CleanupResponse {
        private int expiredTokensFound;
        private int tokensRemoved;
        private long cleanupTimeMs;
        private String summary;
        
        public static CleanupResponse from(SecurityTokenUseCase.CleanupResult result) {
            CleanupResponse response = new CleanupResponse();
            response.expiredTokensFound = result.getExpiredTokensFound();
            response.tokensRemoved = result.getTokensRemoved();
            response.cleanupTimeMs = result.getCleanupTimeMs();
            response.summary = result.getSummary();
            return response;
        }
        
        public int getExpiredTokensFound() { return expiredTokensFound; }
        public int getTokensRemoved() { return tokensRemoved; }
        public long getCleanupTimeMs() { return cleanupTimeMs; }
        public String getSummary() { return summary; }
    }
    
    public static class TokenStatisticsResponse {
        private String userId;
        private int totalTokens;
        private int activeTokens;
        private int expiredTokens;
        private int revokedTokens;
        private Map<TokenType, Integer> tokensByType;
        private java.time.LocalDateTime lastActivity;
        
        public static TokenStatisticsResponse from(SecurityTokenUseCase.TokenStatistics statistics) {
            TokenStatisticsResponse response = new TokenStatisticsResponse();
            response.userId = statistics.getUserId();
            response.totalTokens = statistics.getTotalTokens();
            response.activeTokens = statistics.getActiveTokens();
            response.expiredTokens = statistics.getExpiredTokens();
            response.revokedTokens = statistics.getRevokedTokens();
            response.tokensByType = statistics.getTokensByType();
            response.lastActivity = statistics.getLastActivity();
            return response;
        }
        
        public String getUserId() { return userId; }
        public int getTotalTokens() { return totalTokens; }
        public int getActiveTokens() { return activeTokens; }
        public int getExpiredTokens() { return expiredTokens; }
        public int getRevokedTokens() { return revokedTokens; }
        public Map<TokenType, Integer> getTokensByType() { return tokensByType; }
        public java.time.LocalDateTime getLastActivity() { return lastActivity; }
    }
    
    public static class PolicyValidationResponse {
        private boolean allowed;
        private String policyName;
        private String reason;
        private Set<String> requiredActions;
        
        public static PolicyValidationResponse from(SecurityTokenUseCase.PolicyValidationResult result) {
            PolicyValidationResponse response = new PolicyValidationResponse();
            response.allowed = result.isAllowed();
            response.policyName = result.getPolicyName();
            response.reason = result.getReason();
            response.requiredActions = result.getRequiredActions();
            return response;
        }
        
        public boolean isAllowed() { return allowed; }
        public String getPolicyName() { return policyName; }
        public String getReason() { return reason; }
        public Set<String> getRequiredActions() { return requiredActions; }
    }
}