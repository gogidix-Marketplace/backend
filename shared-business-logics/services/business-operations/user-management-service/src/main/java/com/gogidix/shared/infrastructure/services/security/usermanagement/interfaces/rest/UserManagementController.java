package com.gogidix.shared.infrastructure.services.security.usermanagement.interfaces.rest;

import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "User profile management APIs")
public class UserManagementController {

    private final UserManagementService userManagementService;

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user profile")
    public ResponseEntity<UserProfileResponseDto> createUser(
            @Valid @RequestBody CreateUserRequestDto request) {
        UserProfileResponseDto response = userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user", description = "Get user profile by ID")
    public ResponseEntity<UserProfileResponseDto> getUser(@PathVariable String userId) {
        return userManagementService.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user", description = "Update user profile")
    public ResponseEntity<UserProfileResponseDto> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserRequestDto request) {
        UserProfileResponseDto response = userManagementService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Delete user profile")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userManagementService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List users", description = "List all users for current tenant")
    public ResponseEntity<List<UserProfileResponseDto>> listUsers() {
        List<UserProfileResponseDto> users = userManagementService.listUsers();
        return ResponseEntity.ok(users);
    }
}
