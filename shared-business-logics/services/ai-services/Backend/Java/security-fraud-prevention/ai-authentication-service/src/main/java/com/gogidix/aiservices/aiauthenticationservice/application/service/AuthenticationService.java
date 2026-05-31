package com.gogidix.aiservices.aiauthenticationservice.application.service;

import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.AuthenticationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.MfaVerificationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.TokenRefreshRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.AuthenticationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.TokenValidationResponse;
import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.*;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.*;
import com.gogidix.aiservices.aiauthenticationservice.domain.policy.*;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AccountLockedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationFailedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final TokenPolicy tokenPolicy;
    private final MfaPolicy mfaPolicy;
    private final AccountLockoutPolicy lockoutPolicy;
    private final RiskAssessmentPolicy riskPolicy;
    private final EventPublisherPort eventPublisher;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Find user
        AuthenticationRepository.UserRecord user = authenticationRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    eventPublisher.publish("authentication.failed", new AuthenticationFailedEvent(request.getUsername(), "User not found"));
                    return new AuthenticationFailedException("Invalid credentials");
                });

        // Check if locked
        if (user.isLocked() && user.lockUntil() != null && user.lockUntil().isAfter(Instant.now())) {
            eventPublisher.publish("account.locked", new AccountLockedEvent(request.getUsername()));
            throw new AccountLockedException("Account is locked until " + user.lockUntil());
        }

        // Create attempt
        AuthenticationAttempt attempt = createAttempt(request, user);

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.passwordHash())) {
            attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            authenticationRepository.saveAttempt(attempt);

            // Check if should be locked
            if (lockoutPolicy.isLockedOut(attempt)) {
                authenticationRepository.lockAccount(request.getUsername(), lockoutPolicy.getLockoutUntil(attempt));
                eventPublisher.publish("account.locked", new AccountLockedEvent(request.getUsername()));
                throw new AccountLockedException("Account locked due to too many failed attempts");
            }

            eventPublisher.publish("authentication.failed", new AuthenticationFailedEvent(request.getUsername(), "Invalid password"));
            return buildFailedResponse(attempt);
        }

        // Assess risk
        double riskScore = riskPolicy.assessRisk(attempt);
        RiskLevel riskLevel = riskPolicy.classifyRisk(riskScore);

        // Check if MFA required
        AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                user.userId().toString(),
                UUID.randomUUID().toString(),
                riskScore
        );

        if (mfaPolicy.isMfaRequired(result)) {
            String mfaCode = mfaPolicy.generateMfaCode();
            String sessionId = UUID.randomUUID().toString();
            attempt.requireMfa("SMS");
            attempt.setMfaCode(mfaCode);

            authenticationRepository.saveMfaSession(new AuthenticationRepository.PendingMfaSession(
                    sessionId,
                    request.getUsername(),
                    mfaCode,
                    Instant.now().plusSeconds(300)
            ));

            eventPublisher.publish("mfa.required", new MfaRequiredEvent(request.getUsername(), "SMS"));
            return buildMfaRequiredResponse(sessionId, "SMS", riskScore, riskLevel);
        }

        // Success
        attempt.markSuccessful();
        authenticationRepository.saveAttempt(attempt);

        if (user.failedAttempts() > 0) {
            authenticationRepository.resetFailedAttempts(request.getUsername());
        }

        String sessionId = UUID.randomUUID().toString();
        authenticationRepository.saveSession(new AuthenticationRepository.ActiveSession(
                sessionId,
                user.userId(),
                Instant.now().plusSeconds(3600)
        ));

        eventPublisher.publish("authentication.success", new AuthenticationSuccessEvent(request.getUsername()));
        return buildSuccessResponse(user, sessionId, riskScore, riskLevel);
    }

    public AuthenticationResponse verifyMfa(MfaVerificationRequest request) {
        AuthenticationRepository.PendingMfaSession pendingMfa = authenticationRepository.findPendingMfaSession(request.getSessionId())
                .orElseThrow(() -> new AuthenticationFailedException("MFA session expired"));

        if (mfaPolicy.isMfaLocked(request.getMfaCode())) {
            throw new AccountLockedException("MFA locked due to too many failed attempts");
        }

        if (!mfaPolicy.validateMfaCode(pendingMfa.mfaCode(), request.getMfaCode())) {
            eventPublisher.publish("mfa.failed", new MfaFailedEvent(request.getUsername()));
            throw new AuthenticationFailedException("Invalid MFA code");
        }

        authenticationRepository.consumeMfaSession(request.getSessionId());

        AuthenticationRepository.UserRecord user = authenticationRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        String sessionId = UUID.randomUUID().toString();
        authenticationRepository.saveSession(new AuthenticationRepository.ActiveSession(
                sessionId,
                user.userId(),
                Instant.now().plusSeconds(3600)
        ));

        eventPublisher.publish("authentication.success", new AuthenticationSuccessEvent(request.getUsername()));
        return buildSuccessResponse(user, sessionId, 0.1, RiskLevel.LOW);
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        if (!tokenPolicy.validateRefreshToken(request.getRefreshToken())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String userId = tokenPolicy.extractUserId(request.getRefreshToken());
        AuthenticationRepository.UserRecord user = authenticationRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        if (user.isLocked()) {
            throw new AccountLockedException("Account is locked");
        }

        String sessionId = UUID.randomUUID().toString();
        authenticationRepository.saveSession(new AuthenticationRepository.ActiveSession(
                sessionId,
                user.userId(),
                Instant.now().plusSeconds(3600)
        ));

        return buildSuccessResponse(user, sessionId, 0.1, RiskLevel.LOW);
    }

    public TokenValidationResponse validateToken(String token) {
        if (!tokenPolicy.validateAccessToken(token)) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }

        String userId = tokenPolicy.extractUserId(token);
        Set<String> roles = tokenPolicy.extractRoles(token);
        boolean expired = tokenPolicy.isTokenExpired(token);

        return TokenValidationResponse.builder()
                .valid(!expired)
                .userId(UUID.fromString(userId))
                .roles(roles)
                .expired(expired)
                .build();
    }

    public void logout(String token, String sessionId) {
        authenticationRepository.invalidateSession(sessionId);
        authenticationRepository.blacklistToken(token);
        eventPublisher.publish("authentication.logout", new LogoutEvent(tokenPolicy.extractUserId(token)));
    }

    public record UserProfile(UUID userId, String username, Set<String> roles, boolean isLocked, Instant lockedUntil) {}

    public UserProfile getUserProfile(UUID userId) {
        AuthenticationRepository.UserRecord user = authenticationRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        return new UserProfile(user.userId(), user.username(), user.roles(), user.isLocked(), user.lockUntil());
    }

    public void updatePassword(String username, String oldPassword, String newPassword) {
        AuthenticationRepository.UserRecord user = authenticationRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.passwordHash())) {
            throw new AuthenticationFailedException("Current password is incorrect");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        authenticationRepository.updatePassword(username, encodedPassword);
        eventPublisher.publish("password.changed", new PasswordChangedEvent(username));
    }

    public UUID extractUserIdFromToken(String token) {
        return UUID.fromString(tokenPolicy.extractUserId(token));
    }

    private AuthenticationAttempt createAttempt(AuthenticationRequest request, AuthenticationRepository.UserRecord user) {
        AuthenticationAttempt attempt = AuthenticationAttempt.create(request.getUsername());
        attempt.setUserId(user.userId());
        attempt.setIpAddress(request.getIpAddress());
        attempt.setUserAgent(request.getUserAgent());
        attempt.setDeviceFingerprint(request.getDeviceFingerprint());
        attempt.setLocation(request.getLocation());
        return attempt;
    }

    private AuthenticationResponse buildSuccessResponse(AuthenticationRepository.UserRecord user, String sessionId,
                                                        double riskScore, RiskLevel riskLevel) {
        return AuthenticationResponse.builder()
                .authenticated(true)
                .accessToken(tokenPolicy.generateAccessToken(user.userId().toString(), user.roles()))
                .refreshToken(tokenPolicy.generateRefreshToken(user.userId().toString()))
                .expiresIn(tokenPolicy.getAccessTokenExpiration())
                .userId(user.userId())
                .roles(user.roles())
                .sessionId(sessionId)
                .riskScore(riskScore)
                .riskLevel(riskLevel)
                .remainingAttempts(5)
                .build();
    }

    private AuthenticationResponse buildFailedResponse(AuthenticationAttempt attempt) {
        return AuthenticationResponse.builder()
                .authenticated(false)
                .failureReason(attempt.getFailureReason().toString())
                .remainingAttempts(attempt.getRemainingAttempts())
                .build();
    }

    private AuthenticationResponse buildMfaRequiredResponse(String sessionId, String mfaMethod,
                                                             double riskScore, RiskLevel riskLevel) {
        return AuthenticationResponse.builder()
                .authenticated(false)
                .mfaRequired(true)
                .mfaMethod(mfaMethod)
                .sessionId(sessionId)
                .riskScore(riskScore)
                .riskLevel(riskLevel)
                .build();
    }

    // Event records
    private record AuthenticationSuccessEvent(String username) {}
    private record AuthenticationFailedEvent(String username, String reason) {}
    private record AccountLockedEvent(String username) {}
    private record MfaRequiredEvent(String username, String method) {}
    private record MfaFailedEvent(String username) {}
    private record LogoutEvent(String userId) {}
    private record PasswordChangedEvent(String username) {}
}
