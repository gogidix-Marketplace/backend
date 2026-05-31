package com.gogidix.shared.infrastructure.services.security.auth.interfaces.rest;

import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and authorization APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with username/email and password")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request,
            HttpServletRequest httpRequest) {

        LoginRequestDto enrichedRequest = request.toBuilder()
                .ipAddress(getClientIP(httpRequest))
                .userAgent(httpRequest.getHeader("User-Agent"))
                .build();

        AuthResponseDto response = authService.login(enrichedRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<AuthResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto request) {

        AuthResponseDto response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logout and invalidate tokens")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token", description = "Validate current token and get user info")
    public ResponseEntity<UserResponseDto> validateToken(
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", "");
        Optional<UserResponseDto> user = authService.validateToken(token);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user info")
    public ResponseEntity<UserResponseDto> getCurrentUser(
            @RequestHeader("X-User-Id") String userId) {

        Optional<UserResponseDto> user = authService.findById(userId);

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Gets the client IP address from the request.
     *
     * @param request the HTTP request
     * @return the client IP address
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
