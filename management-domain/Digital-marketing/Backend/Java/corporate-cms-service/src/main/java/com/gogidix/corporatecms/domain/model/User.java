package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain model representing CMS users.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String displayName;

    private String password;

    private String avatar;

    private String phoneNumber;

    @Indexed
    private UserRole role;

    private String department;

    private String jobTitle;

    private String bio;

    @Indexed
    private Boolean enabled;

    @Builder.Default
    private Boolean emailVerified = false;

    private String emailVerificationToken;

    private LocalDateTime emailVerifiedAt;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    @Builder.Default
    private Map<String, String> preferences = new HashMap<>();

    @Builder.Default
    private List<String> permissions = new ArrayList<>();

    @Indexed
    private String tenantId;

    private Locale locale;

    private String timezone;

    @Indexed
    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return displayName != null ? displayName : username;
    }

    public boolean hasPermission(String permission) {
        return role != null && role.getPermissions().stream()
                .anyMatch(p -> p.name().equals(permission));
    }

    public void addPermission(String permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }

    public void removePermission(String permission) {
        permissions.remove(permission);
    }
}
