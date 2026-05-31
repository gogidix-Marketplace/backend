package com.gogidix.aiservices.aisecurityservice.application.service;

import com.gogidix.aiservices.aisecurityservice.domain.model.*;
import com.gogidix.aiservices.aisecurityservice.domain.port.out.KeyRepository;
import com.gogidix.aiservices.aisecurityservice.domain.port.out.KeyRotationService;
import com.gogidix.aiservices.aisecurityservice.domain.policy.SecurityPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final KeyRepository keyRepository;
    private final KeyRotationService keyRotationService;
    private final SecurityPolicy securityPolicy;

    private static final int MAX_DATA_SIZE = 10 * 1024 * 1024; // 10MB

    public EncryptionResult encrypt(String data, EncryptionAlgorithm algorithm) {
        securityPolicy.validateDataSize(data.length());
        securityPolicy.validateAlgorithm(algorithm);

        EncryptionKey key = keyRepository.getActiveKey(algorithm);
        if (key == null) {
            key = keyRotationService.generateKey(algorithm);
        }

        // Simulated encryption (in production, use proper crypto libraries)
        String encryptedData = performEncryption(data, key.getKeyId());

        String checksum = securityPolicy.calculateChecksum(data);

        return EncryptionResult.builder()
                .encryptedData(encryptedData)
                .keyId(key.getKeyId())
                .algorithm(algorithm)
                .encryptedAt(Instant.now())
                .checksum(checksum)
                .build();
    }

    public String decrypt(String encryptedData, String keyId) {
        EncryptionKey key = keyRepository.findById(keyId);
        if (key == null) {
            throw new IllegalArgumentException("Key not found: " + keyId);
        }

        securityPolicy.validateKeyStatus(key);

        // Simulated decryption
        return performDecryption(encryptedData, key.getKeyId());
    }

    public void rotateKeys() {
        keyRotationService.rotateAllKeys();
    }

    public EncryptionKey getKey(String keyId) {
        return keyRepository.findById(keyId);
    }

    public Set<EncryptionKey> getAllKeys() {
        return keyRepository.findAll();
    }

    private String performEncryption(String data, String keyId) {
        // Simulated AES encryption
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = keyId.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = new byte[dataBytes.length];

        for (int i = 0; i < dataBytes.length; i++) {
            encrypted[i] = (byte) (dataBytes[i] ^ keyBytes[i % keyBytes.length]);
        }

        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String performDecryption(String encryptedData, String keyId) {
        byte[] encrypted = Base64.getDecoder().decode(encryptedData);
        byte[] keyBytes = keyId.getBytes(StandardCharsets.UTF_8);
        byte[] decrypted = new byte[encrypted.length];

        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = (byte) (encrypted[i] ^ keyBytes[i % keyBytes.length]);
        }

        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
