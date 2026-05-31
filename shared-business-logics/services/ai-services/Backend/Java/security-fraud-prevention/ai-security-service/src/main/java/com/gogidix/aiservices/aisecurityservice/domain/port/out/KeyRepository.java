package com.gogidix.aiservices.aisecurityservice.domain.port.out;

import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionKey;

import java.util.Optional;
import java.util.Set;

public interface KeyRepository {
    EncryptionKey getActiveKey(EncryptionAlgorithm algorithm);
    EncryptionKey findById(String keyId);
    Set<EncryptionKey> findAll();
}
