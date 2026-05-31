package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model;

import com.gogidix.shared.servicediscovery.config.model.TenantId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * User Profile domain entity.
 * <p>
 * Extended user profile information beyond authentication.
 * Stores additional user details, preferences, and settings.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_profiles")
public class UserProfile {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private TenantId tenantId;

    // Personal Information
    private String firstName;
    private String lastName;
    private String displayName;
    private String email;
    private String avatarUrl;
    private String bio;
    private String phoneNumber;

    // Address Information
    private Address address;

    // Preferences
    private UserPreferences preferences;

    // Metadata
    private Map<String, Object> metadata;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * Address value object.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
    }

    /**
     * User preferences value object.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPreferences {
        private String language;
        private String timezone;
        private String theme;
        private String currency;
        private String dateFormat;
        private boolean emailNotifications;
        private boolean smsNotifications;
        private boolean pushNotifications;
    }
}
