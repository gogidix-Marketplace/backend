package com.gogidix.aiservices.aisecurityservice.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class EncryptionResult {
    private String encryptedData;
    private String keyId;
    private EncryptionAlgorithm algorithm;
    private Instant encryptedAt;
    private String checksum;
}
