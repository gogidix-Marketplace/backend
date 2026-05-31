package com.gogidix.aiservices.aisecurityservice.domain.port.out;

import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionKey;

public interface KeyRotationService {
    EncryptionKey generateKey(EncryptionAlgorithm algorithm);
    void rotateAllKeys();
}
