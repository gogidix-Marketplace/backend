package com.gogidix.infrastructure.config.interfaces.rest;

import com.gogidix.infrastructure.config.application.service.SecretService;
import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.Secret;
import com.gogidix.infrastructure.config.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for secret management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/secrets")
@RequiredArgsConstructor
@Tag(name = "Secrets", description = "APIs for secret management")
public class SecretController {

    private final SecretService secretService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";
    private static final String DEFAULT_USER_HEADER = "X-User-ID";

    /**
     * Creates a new secret.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Create secret", description = "Creates a new secret with encrypted value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Secret created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Secret already exists")
    })
    public ResponseEntity<Secret> createSecret(
            @Valid @RequestBody CreateSecretRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("POST /api/v1/secrets - Creating secret: key={}", request.secretKey());

        Secret response = secretService.createSecret(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Updates an existing secret.
     */
    @PutMapping(value = "/{tenantId}/{secretKey}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Update secret", description = "Updates an existing secret")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secret updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Secret not found")
    })
    public ResponseEntity<Secret> updateSecret(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String secretKey,
            @Valid @RequestBody UpdateSecretRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("PUT /api/v1/secrets/{}/{} - Updating secret", tenantId, secretKey);

        Secret response = secretService.updateSecret(id, request, tenantId, secretKey, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets a secret by tenant and key (without decrypting).
     */
    @GetMapping(value = "/{tenantId}/{secretKey}", produces = "application/json")
    @Operation(summary = "Get secret metadata", description = "Retrieves secret metadata without the decrypted value")
    public ResponseEntity<Secret> getSecret(
            @PathVariable String tenantId,
            @PathVariable String secretKey) {

        log.debug("GET /api/v1/secrets/{}/{}", tenantId, secretKey);

        Secret response = secretService.getSecret(tenantId, secretKey);

        return ResponseEntity.ok(response);
    }

    /**
     * Gets all secrets for a tenant.
     */
    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all secrets", description = "Retrieves all secrets for a tenant (metadata only)")
    public ResponseEntity<List<Secret>> getSecrets(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "50") int size) {

        log.debug("GET /api/v1/secrets - tenantId={}", tenantId);

        if (size > 0) {
            Page<Secret> response = secretService.getSecrets(
                    tenantId, PageRequest.of(page, size, Sort.by("secretKey").ascending()));
            return ResponseEntity.ok(response.getContent());
        }

        List<Secret> response = secretService.getSecrets(tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets the decrypted value of a secret.
     */
    @GetMapping(value = "/{tenantId}/{secretKey}/value", produces = "application/json")
    @Operation(summary = "Get secret value", description = "Retrieves the decrypted value of a secret")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secret value retrieved"),
            @ApiResponse(responseCode = "403", description = "User not authorized"),
            @ApiResponse(responseCode = "404", description = "Secret not found")
    })
    public ResponseEntity<Map<String, String>> getSecretValue(
            @PathVariable String tenantId,
            @PathVariable String secretKey,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("GET /api/v1/secrets/{}/{}/value - user={}", tenantId, secretKey, userId);

        String value = secretService.getSecretValue(tenantId, secretKey, userId);

        return ResponseEntity.ok(Map.of("secretKey", secretKey, "value", value));
    }

    /**
     * Rotates a secret value.
     */
    @PostMapping(value = "/{tenantId}/{secretKey}/rotate", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Rotate secret", description = "Rotates a secret to a new value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secret rotated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Secret not found")
    })
    public ResponseEntity<Secret> rotateSecret(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String secretKey,
            @Valid @RequestBody RotateSecretRequest request,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("POST /api/v1/secrets/{}/{} - Rotating secret", tenantId, secretKey);

        Secret response = secretService.rotateSecret(id, request, tenantId, secretKey, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Searches secrets by key or name pattern.
     */
    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Search secrets", description = "Searches secrets by key or name pattern")
    public ResponseEntity<List<Secret>> searchSecrets(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId,

            @Parameter(description = "Search pattern", required = true) @RequestParam String pattern) {

        log.debug("GET /api/v1/secrets/search - pattern={}", pattern);

        List<Secret> response = secretService.searchSecrets(tenantId, pattern);
        return ResponseEntity.ok(response);
    }

    /**
     * Finds secrets that need rotation.
     */
    @GetMapping(value = "/rotation-needed", produces = "application/json")
    @Operation(summary = "Get secrets needing rotation", description = "Retrieves secrets that need rotation")
    public ResponseEntity<List<Secret>> findSecretsNeedingRotation(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        log.debug("GET /api/v1/secrets/rotation-needed - tenantId={}", tenantId);

        List<Secret> response = secretService.findSecretsNeedingRotation(tenantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets version history for a secret.
     */
    @GetMapping(value = "/{id}/versions", produces = "application/json")
    @Operation(summary = "Get secret versions", description = "Retrieves version history for a secret")
    public ResponseEntity<List<ConfigVersion>> getVersionHistory(@PathVariable String id) {

        log.debug("GET /api/v1/secrets/{}/versions", id);

        List<ConfigVersion> response = secretService.getVersionHistory(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a secret.
     */
    @DeleteMapping(value = "/{tenantId}/{secretKey}")
    @Operation(summary = "Delete secret", description = "Deletes a secret")
    @ApiResponse(responseCode = "204", description = "Secret deleted")
    public ResponseEntity<Void> deleteSecret(
            @PathVariable String id,
            @PathVariable String tenantId,
            @PathVariable String secretKey,

            @Parameter(description = "User ID")
            @RequestHeader(value = DEFAULT_USER_HEADER, defaultValue = "system") String userId) {

        log.info("DELETE /api/v1/secrets/{} - Deleting secret", secretKey);

        secretService.deleteSecret(id, tenantId, secretKey, userId);

        return ResponseEntity.noContent().build();
    }
}
