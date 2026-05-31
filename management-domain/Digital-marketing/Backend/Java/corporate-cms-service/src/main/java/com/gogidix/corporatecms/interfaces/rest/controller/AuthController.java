package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.domain.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication operations.
 */
@Tag(name = "Authentication", description = "Authentication APIs")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "User login", description = "Authenticate a user and receive JWT tokens")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Parameter(description = "Username or email") @RequestParam @NotBlank String usernameOrEmail,
            @Parameter(description = "Password") @RequestParam @NotBlank String password,
            @Parameter(description = "Client IP address") @RequestHeader(value = "X-Forwarded-For", required = false) String ip) {
        Map<String, Object> response = authenticationService.authenticate(usernameOrEmail, password, ip);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(summary = "Refresh token", description = "Refresh an expired access token using a refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            @Parameter(description = "Refresh token") @RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        Map<String, Object> response = authenticationService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", response));
    }

    @Operation(summary = "Get current user", description = "Get information about the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(
            @Parameter(description = "User ID from token") @RequestHeader(value = "X-User-Id", required = false) String userId) {
        UserDTO user = authenticationService.getCurrentUser(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "User logout", description = "Logout the current user")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout successful", null));
    }
}
