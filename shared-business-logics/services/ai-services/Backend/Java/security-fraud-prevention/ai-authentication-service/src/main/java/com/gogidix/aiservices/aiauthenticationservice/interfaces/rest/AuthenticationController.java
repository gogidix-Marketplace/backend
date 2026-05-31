package com.gogidix.aiservices.aiauthenticationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.AuthenticationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.MfaVerificationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.TokenRefreshRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.AuthenticationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.TokenValidationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.service.AuthenticationService;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AccountLockedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationFailedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request,
                                         HttpServletRequest httpRequest) {
        // Enrich request with metadata from HTTP request
        AuthenticationRequest enrichedRequest = request.toBuilder()
                .ipAddress(getClientIp(httpRequest))
                .userAgent(httpRequest.getHeader("User-Agent"))
                .build();

        AuthenticationResponse response = authenticationService.authenticate(enrichedRequest);

        if (response.isAuthenticated()) {
            return ResponseEntity.ok(response);
        } else if (response.isMfaRequired()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/mfa/verify")
    public ResponseEntity<?> verifyMfa(@Valid @RequestBody MfaVerificationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.verifyMfa(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(e.getMessage()));
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String token = extractBearerToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TokenValidationResponse response = authenticationService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        try {
            AuthenticationResponse response = authenticationService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorResponse(e.getMessage()));
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request,
                                       @RequestParam String sessionId) {
        String token = extractBearerToken(request);
        if (token != null) {
            authenticationService.logout(token, sessionId);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String token = extractBearerToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UUID userId = authenticationService.extractUserIdFromToken(token);
            var profile = authenticationService.getUserProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request,
                                           HttpServletRequest httpRequest) {
        String token = extractBearerToken(httpRequest);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            UUID userId = authenticationService.extractUserIdFromToken(token);
            var profile = authenticationService.getUserProfile(userId);
            authenticationService.updatePassword(
                    profile.username(),
                    request.get("oldPassword"),
                    request.get("newPassword")
            );
            return ResponseEntity.noContent().build();
        } catch (AuthenticationFailedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(e.getMessage()));
        }
    }

    @ExceptionHandler({AuthenticationFailedException.class, InvalidTokenException.class})
    public ResponseEntity<Map<String, Object>> handleAuthException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleLockedException(AccountLockedException e) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(buildErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse("An unexpected error occurred"));
    }

    private String extractBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", true);
        error.put("message", message);
        error.put("timestamp", Instant.now().toString());
        return error;
    }
}
