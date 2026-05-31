package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model;

import com.gogidix.shared.multitenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserProfile domain model.
 */
@DisplayName("UserProfile Domain Model Tests")
class UserProfileTest {

    @Test
    @DisplayName("Should create user profile with builder")
    void shouldCreateUserProfileWithBuilder() {
        UserProfile.Address address = UserProfile.Address.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .build();

        UserProfile.UserPreferences preferences = UserProfile.UserPreferences.builder()
                .language("en")
                .timezone("UTC")
                .theme("dark")
                .currency("USD")
                .dateFormat("MM/dd/yyyy")
                .emailNotifications(true)
                .smsNotifications(false)
                .pushNotifications(true)
                .build();

        UserProfile profile = UserProfile.builder()
                .id("profile-123")
                .userId("user-123")
                .tenantId(TenantId.of("tenant-001"))
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@example.com")
                .avatarUrl("avatar.png")
                .bio("Software Developer")
                .phoneNumber("+1234567890")
                .address(address)
                .preferences(preferences)
                .build();

        assertEquals("profile-123", profile.getId());
        assertEquals("user-123", profile.getUserId());
        assertEquals("tenant-001", profile.getTenantId().getValue());
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("John Doe", profile.getDisplayName());
        assertEquals("john@example.com", profile.getEmail());
        assertEquals("avatar.png", profile.getAvatarUrl());
        assertEquals("Software Developer", profile.getBio());
        assertEquals("+1234567890", profile.getPhoneNumber());
        assertNotNull(profile.getAddress());
        assertNotNull(profile.getPreferences());
    }

    @Test
    @DisplayName("Should create user profile with default values")
    void shouldCreateUserProfileWithDefaults() {
        UserProfile profile = new UserProfile();

        assertNull(profile.getId());
        assertNull(profile.getUserId());
        assertNull(profile.getTenantId());
        assertNull(profile.getFirstName());
        assertNull(profile.getLastName());
        assertNull(profile.getDisplayName());
        assertNull(profile.getEmail());
        assertNull(profile.getAvatarUrl());
        assertNull(profile.getBio());
        assertNull(profile.getPhoneNumber());
        assertNull(profile.getAddress());
        assertNull(profile.getPreferences());
        assertNull(profile.getMetadata());
        assertNull(profile.getCreatedAt());
        assertNull(profile.getUpdatedAt());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        UserProfile profile = new UserProfile();
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");

        profile.setId("id-123");
        profile.setUserId("user-456");
        profile.setTenantId(TenantId.of("tenant-789"));
        profile.setFirstName("Jane");
        profile.setLastName("Smith");
        profile.setDisplayName("Jane Smith");
        profile.setEmail("jane@example.com");
        profile.setAvatarUrl("jane.png");
        profile.setBio("Designer");
        profile.setPhoneNumber("+9876543210");
        profile.setMetadata(metadata);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());

        assertEquals("id-123", profile.getId());
        assertEquals("user-456", profile.getUserId());
        assertEquals("tenant-789", profile.getTenantId().getValue());
        assertEquals("Jane", profile.getFirstName());
        assertEquals("Smith", profile.getLastName());
        assertEquals("Jane Smith", profile.getDisplayName());
        assertEquals("jane@example.com", profile.getEmail());
        assertEquals("jane.png", profile.getAvatarUrl());
        assertEquals("Designer", profile.getBio());
        assertEquals("+9876543210", profile.getPhoneNumber());
        assertEquals(metadata, profile.getMetadata());
        assertNotNull(profile.getCreatedAt());
        assertNotNull(profile.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create address with builder")
    void shouldCreateAddressWithBuilder() {
        UserProfile.Address address = UserProfile.Address.builder()
                .street("456 Oak Ave")
                .city("Los Angeles")
                .state("CA")
                .postalCode("90001")
                .country("USA")
                .build();

        assertEquals("456 Oak Ave", address.getStreet());
        assertEquals("Los Angeles", address.getCity());
        assertEquals("CA", address.getState());
        assertEquals("90001", address.getPostalCode());
        assertEquals("USA", address.getCountry());
    }

    @Test
    @DisplayName("Should create address with no args constructor")
    void shouldCreateAddressWithNoArgsConstructor() {
        UserProfile.Address address = new UserProfile.Address();

        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getPostalCode());
        assertNull(address.getCountry());
    }

    @Test
    @DisplayName("Should set and get address properties")
    void shouldSetAndGetAddressProperties() {
        UserProfile.Address address = new UserProfile.Address();

        address.setStreet("789 Pine Rd");
        address.setCity("Chicago");
        address.setState("IL");
        address.setPostalCode("60601");
        address.setCountry("USA");

        assertEquals("789 Pine Rd", address.getStreet());
        assertEquals("Chicago", address.getCity());
        assertEquals("IL", address.getState());
        assertEquals("60601", address.getPostalCode());
        assertEquals("USA", address.getCountry());
    }

    @Test
    @DisplayName("Should create preferences with builder")
    void shouldCreatePreferencesWithBuilder() {
        UserProfile.UserPreferences preferences = UserProfile.UserPreferences.builder()
                .language("es")
                .timezone("America/Mexico_City")
                .theme("light")
                .currency("MXN")
                .dateFormat("dd/MM/yyyy")
                .emailNotifications(false)
                .smsNotifications(true)
                .pushNotifications(false)
                .build();

        assertEquals("es", preferences.getLanguage());
        assertEquals("America/Mexico_City", preferences.getTimezone());
        assertEquals("light", preferences.getTheme());
        assertEquals("MXN", preferences.getCurrency());
        assertEquals("dd/MM/yyyy", preferences.getDateFormat());
        assertFalse(preferences.isEmailNotifications());
        assertTrue(preferences.isSmsNotifications());
        assertFalse(preferences.isPushNotifications());
    }

    @Test
    @DisplayName("Should create preferences with no args constructor")
    void shouldCreatePreferencesWithNoArgsConstructor() {
        UserProfile.UserPreferences preferences = new UserProfile.UserPreferences();

        assertNull(preferences.getLanguage());
        assertNull(preferences.getTimezone());
        assertNull(preferences.getTheme());
        assertNull(preferences.getCurrency());
        assertNull(preferences.getDateFormat());
        assertFalse(preferences.isEmailNotifications());
        assertFalse(preferences.isSmsNotifications());
        assertFalse(preferences.isPushNotifications());
    }

    @Test
    @DisplayName("Should set and get preferences properties")
    void shouldSetAndGetPreferencesProperties() {
        UserProfile.UserPreferences preferences = new UserProfile.UserPreferences();

        preferences.setLanguage("fr");
        preferences.setTimezone("Europe/Paris");
        preferences.setTheme("dark");
        preferences.setCurrency("EUR");
        preferences.setDateFormat("dd/MM/yyyy");
        preferences.setEmailNotifications(true);
        preferences.setSmsNotifications(true);
        preferences.setPushNotifications(true);

        assertEquals("fr", preferences.getLanguage());
        assertEquals("Europe/Paris", preferences.getTimezone());
        assertEquals("dark", preferences.getTheme());
        assertEquals("EUR", preferences.getCurrency());
        assertEquals("dd/MM/yyyy", preferences.getDateFormat());
        assertTrue(preferences.isEmailNotifications());
        assertTrue(preferences.isSmsNotifications());
        assertTrue(preferences.isPushNotifications());
    }

    @Test
    @DisplayName("Should create user profile with all args constructor")
    void shouldCreateUserProfileWithAllArgsConstructor() {
        UserProfile profile = new UserProfile(
                "id-123",
                "user-123",
                TenantId.of("tenant-001"),
                "John",
                "Doe",
                "John Doe",
                "john@example.com",
                "avatar.png",
                "Bio",
                "+1234567890",
                null,
                null,
                null,
                null,
                null
        );

        assertEquals("id-123", profile.getId());
        assertEquals("user-123", profile.getUserId());
        assertEquals("tenant-001", profile.getTenantId().getValue());
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
    }

    @Test
    @DisplayName("Should handle null address and preferences")
    void shouldHandleNullAddressAndPreferences() {
        UserProfile profile = UserProfile.builder()
                .userId("user-123")
                .tenantId(TenantId.of("tenant-001"))
                .build();

        assertNull(profile.getAddress());
        assertNull(profile.getPreferences());
    }
}
