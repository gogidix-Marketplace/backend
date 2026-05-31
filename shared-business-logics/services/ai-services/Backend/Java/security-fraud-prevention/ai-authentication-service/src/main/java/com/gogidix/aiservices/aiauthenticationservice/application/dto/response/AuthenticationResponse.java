package com.gogidix.aiservices.aiauthenticationservice.application.dto.response;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.RiskLevel;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class AuthenticationResponse {
    private boolean authenticated;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private UUID userId;
    private Set<String> roles;
    private String sessionId;
    private String mfaMethod;
    private boolean mfaRequired;
    private Double riskScore;
    private RiskLevel riskLevel;
    private String failureReason;
    private Instant lockedUntil;
    private Integer remainingAttempts;
}
