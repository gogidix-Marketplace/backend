package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.domain.enums.UserRole;
import com.gogidix.corporatecms.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 */
@Tag(name = "Users", description = "User management APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create user", description = "Create a new user")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO dto) {
        String createdBy = getCurrentUserId();
        UserDTO user = userService.createUser(dto, createdBy);
        return ResponseEntity.status(201).body(ApiResponse.created(user));
    }

    @Operation(summary = "Update user", description = "Update an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @Parameter(description = "User ID") @PathVariable String id,
            @Valid @RequestBody UserDTO dto) {
        UserDTO user = userService.updateUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
            @Parameter(description = "User ID") @PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "Get user by username", description = "Retrieve a user by username")
    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "Get users by role", description = "Retrieve users filtered by role")
    @GetMapping("/role/{role}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsersByRole(
            @Parameter(description = "User role") @PathVariable UserRole role,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<UserDTO> users = userService.getUsersByRole(role, page, size);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(summary = "Search users", description = "Search users by keyword")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> searchUsers(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<UserDTO> users = userService.searchUsers(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(summary = "Delete user", description = "Soft delete a user")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID") @PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    @Operation(summary = "Change password", description = "Change user password")
    @PostMapping("/{id}/password")
    public ResponseEntity<ApiResponse<UserDTO>> changePassword(
            @Parameter(description = "User ID") @PathVariable String id,
            @Parameter(description = "Current password") @RequestParam String oldPassword,
            @Parameter(description = "New password") @RequestParam String newPassword) {
        UserDTO user = userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", user));
    }

    @Operation(summary = "Get active users", description = "Retrieve all active users")
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getActiveUsers() {
        List<UserDTO> users = userService.getActiveUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @Operation(summary = "Get users by department", description = "Retrieve users by department")
    @GetMapping("/department/{department}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByDepartment(
            @Parameter(description = "Department name") @PathVariable String department) {
        List<UserDTO> users = userService.getUsersByDepartment(department);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    private String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }
}
