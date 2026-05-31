package com.gogidix.aiservices.aisecurityservice.application.dto.request;

import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EncryptionRequest {
    @NotBlank
    private String data;

    private EncryptionAlgorithm algorithm = EncryptionAlgorithm.AES256;
}
