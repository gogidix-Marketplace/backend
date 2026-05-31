package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for User entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO for managing CMS users")
public class UserDTO {

    @Schema(description = "User ID")
    private String id;

    @Schema(description = "Username")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(description = "Email address")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "First name")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Schema(description = "Last name")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Schema(description = "Display name")
    private String displayName;

    @Schema(description = "Avatar URL")
    private String avatar;

    @Schema(description = "Phone number")
    private String phoneNumber;

    @Schema(description = "User role")
    private UserRole role;

    @Schema(description = "Department")
    private String department;

    @Schema(description = "Job title")
    private String jobTitle;

    @Schema(description = "User bio")
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Schema(description = "Is enabled")
    private Boolean enabled;

    @Schema(description = "Email verified")
    private Boolean emailVerified;

    @Schema(description = "User preferences")
    private Map<String, String> preferences;

    @Schema(description = "Additional permissions")
    private List<String> permissions;

    @Schema(description = "Locale")
    private String locale;

    @Schema(description = "Timezone")
    private String timezone;

    @Schema(description = "Last login at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLoginAt;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
