package com.gogidix.aiservices.aisecurityservice.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class EncryptionKey {
    private String keyId;
    private EncryptionAlgorithm algorithm;
    private KeyStatus status;
    private Instant createdAt;
    private Instant expiresAt;
    private Instant lastRotated;
    private Set<String> allowedUsers;
    private String version;
}
