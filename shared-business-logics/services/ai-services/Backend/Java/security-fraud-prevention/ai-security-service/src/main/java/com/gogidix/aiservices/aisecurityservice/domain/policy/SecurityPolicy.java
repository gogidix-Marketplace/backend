package com.gogidix.aiservices.aisecurityservice.domain.policy;

import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionKey;
import com.gogidix.aiservices.aisecurityservice.domain.model.KeyStatus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityPolicy {

    private static final int MAX_DATA_SIZE = 10 * 1024 * 1024; // 10MB

    public void validateDataSize(int dataSize) {
        if (dataSize > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("Data size exceeds maximum limit");
        }
    }

    public void validateAlgorithm(EncryptionAlgorithm algorithm) {
        if (algorithm == null) {
            throw new IllegalArgumentException("Algorithm cannot be null");
        }
    }

    public void validateKeyStatus(EncryptionKey key) {
        if (key.getStatus() != KeyStatus.ACTIVE) {
            throw new IllegalArgumentException("Key is not active: " + key.getKeyId());
        }
    }

    public String calculateChecksum(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to calculate checksum", e);
        }
    }
}
