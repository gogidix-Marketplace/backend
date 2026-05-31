package com.gogidix.shared.infrastructure.services.security.auth.application.service;

import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import com.gogidix.shared.multitenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.mapper.UserMapper;
import com.gogidix.shared.infrastructure.services.security.auth.domain.exception.InvalidCredentialsException;
import com.gogidix.shared.infrastructure.services.security.auth.domain.exception.UserNotFoundException;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.in.AuthPort;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.TokenRepositoryPort;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.UserRepositoryPort;
import com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

/**
 * Authentication service implementation.
 * <p>
 * Handles login, logout, token refresh, and user validation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthPort {

    private final UserRepositoryPort userRepository;
    private final TokenRepositoryPort tokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TenantContextHolder tenantContextHolder;

    private static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15;
    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    @Override
    @Transactional
    public AuthResponseDto login(LoginRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Login attempt for user: {} in tenant: {}", request.getUsername(), tenantId);

        // Find user by username or email
        User user = findByUsernameOrEmail(request.getUsername(), tenantId)
                .orElseThrow(() -> new InvalidCredentialsException());

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for user: {} in tenant: {}", request.getUsername(), tenantId);
            throw new InvalidCredentialsException();
        }

        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Account is disabled");
        }

        // Generate tokens
        String accessToken = jwtProvider.generateAccessToken(user.getId(), tenantId, user.getRoles());
        String refreshToken = UUID.randomUUID().toString();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        LocalDateTime refreshExpiresAt = now.plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS);

        // Save token
        AuthToken authToken = AuthToken.builder()
                .userId(user.getId())
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .refreshExpiresAt(refreshExpiresAt)
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .revoked(false)
                .build();

        tokenRepository.save(authToken);

        // Update last login
        user.setLastLoginAt(now.toString());
        userRepository.save(user);

        log.info("Successful login for user: {} in tenant: {}", user.getUsername(), tenantId);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresAt(expiresAt.atZone(java.time.ZoneId.systemDefault()).toInstant())
                .refreshExpiresAt(refreshExpiresAt.atZone(java.time.ZoneId.systemDefault()).toInstant())
                .user(userMapper.toResponseDto(user))
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Token refresh request in tenant: {}", tenantId);

        // Find refresh token
        AuthToken authToken = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        // Validate token
        if (authToken.isRefreshExpired() || authToken.isRevoked()) {
            throw new InvalidCredentialsException("Refresh token expired or revoked");
        }

        // Find user
        User user = userRepository.findByIdAndTenantId(authToken.getUserId(), tenantId)
                .orElseThrow(() -> new UserNotFoundException(authToken.getUserId()));

        // Revoke old token
        authToken.revoke();
        tokenRepository.save(authToken);

        // Generate new tokens
        String accessToken = jwtProvider.generateAccessToken(user.getId(), tenantId, user.getRoles());
        String newRefreshToken = UUID.randomUUID().toString();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        LocalDateTime refreshExpiresAt = now.plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS);

        // Save new token
        AuthToken newAuthToken = AuthToken.builder()
                .userId(user.getId())
                .token(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .refreshExpiresAt(refreshExpiresAt)
                .revoked(false)
                .build();

        tokenRepository.save(newAuthToken);

        log.info("Token refreshed for user: {} in tenant: {}", user.getUsername(), tenantId);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresAt(expiresAt.atZone(java.time.ZoneId.systemDefault()).toInstant())
                .refreshExpiresAt(refreshExpiresAt.atZone(java.time.ZoneId.systemDefault()).toInstant())
                .user(userMapper.toResponseDto(user))
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        log.info("Logout request");

        // Find and revoke token
        tokenRepository.findByToken(token).ifPresent(authToken -> {
            authToken.revoke();
            tokenRepository.save(authToken);
            log.info("User logged out: {}", authToken.getUserId());
        });
    }

    @Override
    public Optional<UserResponseDto> validateToken(String token) {
        try {
            String tenantId = tenantContextHolder.getRequiredTenantId();

            // Validate JWT
            if (!jwtProvider.validateToken(token)) {
                return Optional.empty();
            }

            // Check token in repository
            return tokenRepository.findValidByToken(token)
                    .flatMap(authToken -> userRepository.findByIdAndTenantId(authToken.getUserId(), tenantId))
                    .map(userMapper::toResponseDto);

        } catch (Exception e) {
            log.warn("Token validation failed", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserResponseDto> findById(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return userRepository.findByIdAndTenantId(userId, tenantId)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Optional<UserResponseDto> findByUsername(String username) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return userRepository.findByUsernameAndTenantId(username, tenantId)
                .map(userMapper::toResponseDto);
    }

    @Override
    public Optional<UserResponseDto> findByEmail(String email) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return userRepository.findByEmailAndTenantId(email, tenantId)
                .map(userMapper::toResponseDto);
    }

    /**
     * Finds a user by username or email.
     *
     * @param usernameOrEmail the username or email
     * @param tenantId the tenant ID
     * @return the user if found
     */
    private Optional<User> findByUsernameOrEmail(String usernameOrEmail, String tenantId) {
        // Try username first
        Optional<User> user = userRepository.findByUsernameAndTenantId(usernameOrEmail, tenantId);

        // If not found, try email
        if (user.isEmpty()) {
            user = userRepository.findByEmailAndTenantId(usernameOrEmail, tenantId);
        }

        return user;
    }
}
