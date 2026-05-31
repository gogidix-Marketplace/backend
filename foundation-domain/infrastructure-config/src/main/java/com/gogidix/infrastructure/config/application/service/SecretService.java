package com.gogidix.infrastructure.config.application.service;

import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.Secret;
import com.gogidix.infrastructure.config.domain.repository.ConfigVersionRepository;
import com.gogidix.infrastructure.config.domain.repository.SecretRepository;
import com.gogidix.infrastructure.config.infrastructure.crypto.EncryptionService;
import com.gogidix.infrastructure.config.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing secrets.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecretService {

    private final SecretRepository repository;
    private final ConfigVersionRepository versionRepository;
    private final EncryptionService encryptionService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String SECRET_CACHE = "secret";
    private static final String CONFIG_CHANGED_TOPIC = "infrastructure.config.changed";

    /**
     * Creates a new secret.
     */
    @Transactional
    public Secret createSecret(CreateSecretRequest request, String userId) {
        log.info("Creating secret: tenantId={}, key={}", request.tenantId(), request.secretKey());

        // Check for existing secret
        repository.findByTenantIdAndSecretKey(request.tenantId(), request.secretKey())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Secret with key '" + request.secretKey() + "' already exists");
                });

        Secret secret = Secret.builder()
                .tenantId(request.tenantId())
                .secretKey(request.secretKey())
                .name(request.name())
                .description(request.description())
                .encryptedValue(encryptionService.encrypt(request.secretValue()))
                .iv(encryptionService.generateIv())
                .secretType(request.secretType())
                .tags(request.tags())
                .category(request.category())
                .owner(request.owner())
                .accessControlList(request.accessControlList())
                .rotationIntervalDays(request.rotationIntervalDays())
                .expiresAt(request.expiresAt())
                .metadata(request.metadata())
                .createdBy(userId)
                .lastUpdatedBy(userId)
                .build();

        secret.updateRotationSchedule();

        Secret saved = repository.save(secret);

        // Create version record
        createVersionRecord(saved, null, "***", ConfigVersion.ChangeType.CREATED, userId, request.changeReason());

        // Publish event
        publishSecretChangedEvent(saved, "CREATED");

        log.info("Secret created: id={}", saved.getId());
        return saved;
    }

    /**
     * Updates an existing secret.
     */
    @Transactional
    @CacheEvict(value = SECRET_CACHE, key = "#tenantId + ':' + #secretKey")
    public Secret updateSecret(String id, UpdateSecretRequest request,
            String tenantId, String secretKey, String userId) {
        log.info("Updating secret: id={}", id);

        Secret secret = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + id));

        // Verify tenant and key
        if (!secret.getTenantId().equals(tenantId) || !secret.getSecretKey().equals(secretKey)) {
            throw new IllegalArgumentException("Secret mismatch");
        }

        // Update fields
        if (request.name() != null) {
            secret.setName(request.name());
        }
        if (request.description() != null) {
            secret.setDescription(request.description());
        }
        if (request.secretValue() != null) {
            String encryptedValue = encryptionService.encrypt(request.secretValue());
            secret.setEncryptedValue(encryptedValue);
            secret.setIv(encryptionService.generateIv());
        }
        if (request.secretType() != null) {
            secret.setSecretType(request.secretType());
        }
        if (request.tags() != null) {
            secret.setTags(request.tags());
        }
        if (request.category() != null) {
            secret.setCategory(request.category());
        }
        if (request.owner() != null) {
            secret.setOwner(request.owner());
        }
        if (request.accessControlList() != null) {
            secret.setAccessControlList(request.accessControlList());
        }
        if (request.rotationIntervalDays() != null) {
            secret.setRotationIntervalDays(request.rotationIntervalDays());
        }
        if (request.expiresAt() != null) {
            secret.setExpiresAt(request.expiresAt());
        }
        if (request.metadata() != null) {
            secret.setMetadata(request.metadata());
        }

        secret.setLastUpdatedBy(userId);

        Secret saved = repository.save(secret);

        // Create version record
        createVersionRecord(saved, "***", "***",
                ConfigVersion.ChangeType.UPDATED, userId, request.changeReason());

        // Publish event
        publishSecretChangedEvent(saved, "UPDATED");

        log.info("Secret updated: id={}", saved.getId());
        return saved;
    }

    /**
     * Gets a secret by tenant and key (without decrypting).
     */
    @Transactional(readOnly = true)
    public Secret getSecret(String tenantId, String secretKey) {
        log.debug("Getting secret: tenantId={}, key={}", tenantId, secretKey);
        return repository.findByTenantIdAndSecretKey(tenantId, secretKey)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + secretKey));
    }

    /**
     * Gets a secret by ID (without decrypting).
     */
    @Transactional(readOnly = true)
    public Secret getSecretById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + id));
    }

    /**
     * Gets all secrets for a tenant.
     */
    @Transactional(readOnly = true)
    public List<Secret> getSecrets(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    /**
     * Gets secrets with pagination.
     */
    @Transactional(readOnly = true)
    public Page<Secret> getSecrets(String tenantId, Pageable pageable) {
        return repository.findByTenantId(tenantId, pageable);
    }

    /**
     * Gets the decrypted value of a secret.
     */
    @Cacheable(value = SECRET_CACHE, key = "#tenantId + ':' + #secretKey")
    @Transactional
    public String getSecretValue(String tenantId, String secretKey, String userId) {
        log.info("Accessing secret value: tenantId={}, key={}, user={}", tenantId, secretKey, userId);

        Secret secret = repository.findByTenantIdAndSecretKey(tenantId, secretKey)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + secretKey));

        // Check access
        if (!secret.hasAccess(userId)) {
            throw new IllegalArgumentException("User not authorized to access this secret");
        }

        // Check validity
        if (!secret.isValid()) {
            throw new IllegalArgumentException("Secret is not valid (inactive or expired)");
        }

        // Decrypt value
        String decryptedValue = encryptionService.decrypt(secret.getEncryptedValue());

        // Record access
        secret.recordAccess(userId);
        repository.save(secret);

        return decryptedValue;
    }

    /**
     * Rotates a secret value.
     */
    @Transactional
    @CacheEvict(value = SECRET_CACHE, key = "#tenantId + ':' + #secretKey")
    public Secret rotateSecret(String id, RotateSecretRequest request,
            String tenantId, String secretKey, String userId) {
        log.info("Rotating secret: id={}", id);

        Secret secret = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + id));

        if (!secret.getTenantId().equals(tenantId) || !secret.getSecretKey().equals(secretKey)) {
            throw new IllegalArgumentException("Secret mismatch");
        }

        // Rotate the value
        String encryptedValue = encryptionService.encrypt(request.newSecretValue());
        secret.setEncryptedValue(encryptedValue);
        secret.setIv(encryptionService.generateIv());
        secret.incrementVersion();
        secret.setLastUpdatedBy(userId);
        secret.updateRotationSchedule();

        Secret saved = repository.save(secret);

        // Create version record
        createVersionRecord(saved, "***", "***",
                ConfigVersion.ChangeType.ROTATED, userId, request.changeReason());

        // Publish event
        publishSecretChangedEvent(saved, "ROTATED");

        log.info("Secret rotated: id={}, newVersion={}", saved.getId(), saved.getVersion());
        return saved;
    }

    /**
     * Deletes a secret.
     */
    @Transactional
    @CacheEvict(value = SECRET_CACHE, key = "#tenantId + ':' + #secretKey")
    public void deleteSecret(String id, String tenantId, String secretKey, String userId) {
        log.info("Deleting secret: id={}", id);

        Secret secret = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Secret not found: " + id));

        if (!secret.getTenantId().equals(tenantId) || !secret.getSecretKey().equals(secretKey)) {
            throw new IllegalArgumentException("Secret mismatch");
        }

        // Create version record
        createVersionRecord(secret, "***", null,
                ConfigVersion.ChangeType.DELETED, userId, null);

        repository.delete(secret);

        // Publish event
        publishSecretChangedEvent(secret, "DELETED");

        log.info("Secret deleted: id={}", id);
    }

    /**
     * Finds secrets that need rotation.
     */
    @Transactional(readOnly = true)
    public List<Secret> findSecretsNeedingRotation(String tenantId) {
        return repository.findSecretsNeedingRotation(tenantId, LocalDateTime.now());
    }

    /**
     * Finds expired secrets.
     */
    @Transactional(readOnly = true)
    public List<Secret> findExpiredSecrets(String tenantId) {
        return repository.findExpiredSecrets(tenantId, LocalDateTime.now());
    }

    /**
     * Searches secrets by key or name pattern.
     */
    @Transactional(readOnly = true)
    public List<Secret> searchSecrets(String tenantId, String pattern) {
        return repository.searchByKeyOrName(tenantId, pattern);
    }

    /**
     * Gets version history for a secret.
     */
    @Transactional(readOnly = true)
    public List<ConfigVersion> getVersionHistory(String configId) {
        return versionRepository.findByConfigIdOrderByVersionDesc(configId);
    }

    /**
     * Creates a version record for a secret change.
     */
    private void createVersionRecord(Secret secret, String previousValue,
            String newValue, ConfigVersion.ChangeType changeType, String userId, String reason) {

        ConfigVersion version = ConfigVersion.builder()
                .tenantId(secret.getTenantId())
                .configId(secret.getId())
                .configType(ConfigVersion.ConfigType.SECRET)
                .configKey(secret.getSecretKey())
                .version(secret.getVersion())
                .previousValue(previousValue)
                .newValue(newValue)
                .changeType(changeType)
                .changedBy(userId)
                .changeReason(reason)
                .changedAt(LocalDateTime.now())
                .snapshot(createSnapshot(secret))
                .build();

        versionRepository.save(version);
    }

    /**
     * Creates a snapshot map of the secret.
     */
    private Map<String, Object> createSnapshot(Secret secret) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("name", secret.getName());
        snapshot.put("description", secret.getDescription());
        snapshot.put("secretType", secret.getSecretType());
        snapshot.put("tags", secret.getTags());
        snapshot.put("category", secret.getCategory());
        snapshot.put("owner", secret.getOwner());
        snapshot.put("rotationIntervalDays", secret.getRotationIntervalDays());
        snapshot.put("expiresAt", secret.getExpiresAt());
        return snapshot;
    }

    /**
     * Publishes a secret changed event to Kafka.
     */
    private void publishSecretChangedEvent(Secret secret, String action) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("action", action);
            event.put("tenantId", secret.getTenantId());
            event.put("key", secret.getSecretKey());
            event.put("type", "SECRET");
            event.put("timestamp", LocalDateTime.now().toString());

            String eventJson = toJson(event);
            kafkaTemplate.send(CONFIG_CHANGED_TOPIC, secret.getTenantId(), eventJson);
        } catch (Exception e) {
            log.warn("Failed to publish secret changed event", e);
        }
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":\"").append(v).append("\","));
        if (!map.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
