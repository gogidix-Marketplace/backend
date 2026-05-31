package com.gogidix.aiservices.aisecurityservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DecryptionRequest {
    @NotBlank
    private String encryptedData;

    @NotBlank
    private String keyId;
}
