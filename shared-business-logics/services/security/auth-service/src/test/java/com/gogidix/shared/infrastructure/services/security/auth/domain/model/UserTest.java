package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import com.gogidix.shared.multitenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Domain Model Tests")
class UserTest {

    @Test
    @DisplayName("Should create user with builder")
    void shouldCreateUserWithBuilder() {
        TenantId tenantId = TenantId.of("tenant-123");
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");
        Set<String> permissions = new HashSet<>();
        permissions.add("read");
        permissions.add("write");

        User user = User.builder()
                .id("user-123")
                .tenantId(tenantId)
                .username("john.doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(roles)
                .permissions(permissions)
                .lastLoginAt(LocalDateTime.now().toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(user);
        assertEquals("user-123", user.getId());
        assertEquals("tenant-123", user.getTenantId().getValue());
        assertEquals("john.doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertEquals(2, user.getRoles().size());
        assertEquals(2, user.getPermissions().size());
    }

    @Test
    @DisplayName("Should create user with default empty sets")
    void shouldCreateUserWithDefaultEmptySets() {
        User user = User.builder()
                .id("user-123")
                .username("john.doe")
                .build();

        assertNotNull(user);
        assertNotNull(user.getRoles());
        assertNotNull(user.getPermissions());
        assertTrue(user.getRoles().isEmpty());
        assertTrue(user.getPermissions().isEmpty());
    }

    @Test
    @DisplayName("Should return true when user has role")
    void shouldReturnTrueWhenUserHasRole() {
        User user = User.builder()
                .id("user-123")
                .roles(new HashSet<>(Set.of("USER", "ADMIN")))
                .build();

        assertTrue(user.hasRole("ADMIN"));
    }

    @Test
    @DisplayName("Should return false when user does not have role")
    void shouldReturnFalseWhenUserDoesNotHaveRole() {
        User user = User.builder()
                .id("user-123")
                .roles(new HashSet<>(Set.of("USER")))
                .build();

        assertFalse(user.hasRole("ADMIN"));
    }

    @Test
    @DisplayName("Should return false when roles is null")
    void shouldReturnFalseWhenRolesIsNull() {
        User user = new User();
        user.setRoles(null);

        assertFalse(user.hasRole("ADMIN"));
    }

    @Test
    @DisplayName("Should return false when checking role with null value")
    void shouldReturnFalseWhenCheckingRoleWithNull() {
        User user = User.builder()
                .id("user-123")
                .roles(new HashSet<>(Set.of("USER", "ADMIN")))
                .build();

        assertFalse(user.hasRole(null));
    }

    @Test
    @DisplayName("Should return true when user has permission")
    void shouldReturnTrueWhenUserHasPermission() {
        User user = User.builder()
                .id("user-123")
                .permissions(new HashSet<>(Set.of("read", "write", "delete")))
                .build();

        assertTrue(user.hasPermission("write"));
    }

    @Test
    @DisplayName("Should return false when user does not have permission")
    void shouldReturnFalseWhenUserDoesNotHavePermission() {
        User user = User.builder()
                .id("user-123")
                .permissions(new HashSet<>(Set.of("read")))
                .build();

        assertFalse(user.hasPermission("delete"));
    }

    @Test
    @DisplayName("Should return false when permissions is null")
    void shouldReturnFalseWhenPermissionsIsNull() {
        User user = new User();
        user.setPermissions(null);

        assertFalse(user.hasPermission("read"));
    }

    @Test
    @DisplayName("Should return false when checking permission with null value")
    void shouldReturnFalseWhenCheckingPermissionWithNull() {
        User user = User.builder()
                .id("user-123")
                .permissions(new HashSet<>(Set.of("read", "write")))
                .build();

        assertFalse(user.hasPermission(null));
    }

    @Test
    @DisplayName("Should add role to user")
    void shouldAddRoleToUser() {
        User user = User.builder()
                .id("user-123")
                .roles(new HashSet<>())
                .build();

        user.addRole("ADMIN");

        assertTrue(user.hasRole("ADMIN"));
        assertEquals(1, user.getRoles().size());
    }

    @Test
    @DisplayName("Should add role when roles is null")
    void shouldAddRoleWhenRolesIsNull() {
        User user = new User();
        user.setRoles(null);

        user.addRole("USER");

        assertNotNull(user.getRoles());
        assertTrue(user.hasRole("USER"));
        assertEquals(1, user.getRoles().size());
    }

    @Test
    @DisplayName("Should not duplicate role when adding existing role")
    void shouldNotDuplicateRoleWhenAddingExistingRole() {
        User user = User.builder()
                .id("user-123")
                .roles(new HashSet<>(Set.of("USER")))
                .build();

        user.addRole("USER");

        assertEquals(1, user.getRoles().size());
    }

    @Test
    @DisplayName("Should add permission to user")
    void shouldAddPermissionToUser() {
        User user = User.builder()
                .id("user-123")
                .permissions(new HashSet<>())
                .build();

        user.addPermission("write");

        assertTrue(user.hasPermission("write"));
        assertEquals(1, user.getPermissions().size());
    }

    @Test
    @DisplayName("Should add permission when permissions is null")
    void shouldAddPermissionWhenPermissionsIsNull() {
        User user = new User();
        user.setPermissions(null);

        user.addPermission("read");

        assertNotNull(user.getPermissions());
        assertTrue(user.hasPermission("read"));
        assertEquals(1, user.getPermissions().size());
    }

    @Test
    @DisplayName("Should not duplicate permission when adding existing permission")
    void shouldNotDuplicatePermissionWhenAddingExistingPermission() {
        User user = User.builder()
                .id("user-123")
                .permissions(new HashSet<>(Set.of("read")))
                .build();

        user.addPermission("read");

        assertEquals(1, user.getPermissions().size());
    }

    @Test
    @DisplayName("Should handle no-args constructor")
    void shouldHandleNoArgsConstructor() {
        User user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertTrue(user.getRoles() == null || user.getRoles().isEmpty());
        assertTrue(user.getPermissions() == null || user.getPermissions().isEmpty());
    }

    @Test
    @DisplayName("Should handle all-args constructor")
    void shouldHandleAllArgsConstructor() {
        TenantId tenantId = TenantId.of("tenant-123");
        Set<String> roles = new HashSet<>(Set.of("USER"));
        Set<String> permissions = new HashSet<>(Set.of("read"));

        User user = new User(
                "user-123",
                tenantId,
                "john.doe",
                "john.doe@example.com",
                "encodedPassword",
                "John",
                "Doe",
                true,
                true,
                true,
                true,
                roles,
                permissions,
                null,
                null,
                null
        );

        assertNotNull(user);
        assertEquals("user-123", user.getId());
        assertEquals("tenant-123", user.getTenantId().getValue());
        assertEquals("john.doe", user.getUsername());
        assertEquals("john.doe@example.com", user.getEmail());
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Should verify Lombok getters and setters")
    void shouldVerifyLombokGettersAndSetters() {
        User user = new User();

        user.setId("user-456");
        user.setUsername("jane.doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("password123");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEnabled(false);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);

        assertEquals("user-456", user.getId());
        assertEquals("jane.doe", user.getUsername());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertFalse(user.isEnabled());
        assertFalse(user.isAccountNonExpired());
        assertFalse(user.isAccountNonLocked());
        assertFalse(user.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Should verify equals and hashCode with same values")
    void shouldVerifyEqualsAndHashCodeWithSameValues() {
        TenantId tenantId = TenantId.of("tenant-123");
        User user1 = User.builder()
                .id("user-123")
                .tenantId(tenantId)
                .username("john.doe")
                .build();

        User user2 = User.builder()
                .id("user-123")
                .tenantId(tenantId)
                .username("john.doe")
                .build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    @DisplayName("Should verify toString method")
    void shouldVerifyToStringMethod() {
        User user = User.builder()
                .id("user-123")
                .username("john.doe")
                .email("john.doe@example.com")
                .build();

        String toString = user.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("user-123") || toString.contains("User"));
    }
}
