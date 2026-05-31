package com.gogidix.aiservices.aisecurityservice.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DecryptionResponse {
    private String decryptedData;
}
