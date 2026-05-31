package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.BiometricType;

import java.security.SecureRandom;
import java.util.Set;

public class BiometricPolicy {
    private static final double DEFAULT_CONFIDENCE_THRESHOLD = 0.85;
    private static final double LIVENESS_THRESHOLD = 0.8;

    private static final Set<BiometricType> SUPPORTED_TYPES = Set.of(
            BiometricType.FACE_RECOGNITION,
            BiometricType.FINGERPRINT,
            BiometricType.VOICE_RECOGNITION,
            BiometricType.IRIS_SCAN
    );

    private final SecureRandom random = new SecureRandom();

    public boolean isValidConfidence(double confidenceScore) {
        return confidenceScore >= DEFAULT_CONFIDENCE_THRESHOLD;
    }

    public double getRequiredConfidence(BiometricType type) {
        return switch (type) {
            case FACE_RECOGNITION -> 0.85;
            case FINGERPRINT -> 0.90;
            case VOICE_RECOGNITION -> 0.80;
            case IRIS_SCAN -> 0.95;
        };
    }

    public Set<BiometricType> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public double assessLiveness(byte[] biometricData) {
        // Simulated liveness assessment
        // In real implementation, this would use actual ML models
        double baseScore = 0.5 + (random.nextDouble() * 0.5);

        // Check for data characteristics that indicate spoofing
        if (biometricData != null && biometricData.length > 0) {
            // Simple heuristic: all zeros likely indicates fake/spoofed data
            boolean allZeros = true;
            for (byte b : biometricData) {
                if (b != 0) {
                    allZeros = false;
                    break;
                }
            }
            if (allZeros) {
                return 0.0;
            }
        }

        return Math.min(baseScore, 1.0);
    }

    public boolean isGenuine(byte[] biometricData) {
        return assessLiveness(biometricData) >= LIVENESS_THRESHOLD;
    }
}
