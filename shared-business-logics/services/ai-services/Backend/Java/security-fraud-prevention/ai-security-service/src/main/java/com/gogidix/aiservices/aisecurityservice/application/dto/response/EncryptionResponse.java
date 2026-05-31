package com.gogidix.aiservices.aisecurityservice.application.dto.response;

import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionResult;
import lombok.Data;

import java.time.Instant;

@Data
public class EncryptionResponse {
    private String encryptedData;
    private String keyId;
    private EncryptionAlgorithm algorithm;
    private Instant encryptedAt;
    private String checksum;

    public static EncryptionResponse fromDomain(EncryptionResult result) {
        EncryptionResponse response = new EncryptionResponse();
        response.setEncryptedData(result.getEncryptedData());
        response.setKeyId(result.getKeyId());
        response.setAlgorithm(result.getAlgorithm());
        response.setEncryptedAt(result.getEncryptedAt());
        response.setChecksum(result.getChecksum());
        return response;
    }
}
