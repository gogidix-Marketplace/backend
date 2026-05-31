package com.gogidix.aiservices.aiauthenticationservice.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class TokenValidationResponse {
    private boolean valid;
    private UUID userId;
    private Set<String> roles;
    private boolean expired;
    private String error;
}
