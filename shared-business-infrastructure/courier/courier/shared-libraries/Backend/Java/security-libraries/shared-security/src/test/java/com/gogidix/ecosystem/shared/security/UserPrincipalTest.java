package com.gogidix.ecosystem.shared.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for UserPrincipal.
 * 
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@DisplayName("User Principal Tests")
class UserPrincipalTest {

    @Test
    @DisplayName("Should create user principal with valid data")
    void shouldCreateUserPrincipalWithValidData() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList("USER", "ADMIN"),
            true
        );

        // Then
        assertThat(userPrincipal).isNotNull();
        assertThat(userPrincipal.getId()).isEqualTo(1L);
        assertThat(userPrincipal.getUsername()).isEqualTo("testuser");
        assertThat(userPrincipal.getEmail()).isEqualTo("test@example.com");
        assertThat(userPrincipal.getPassword()).isEqualTo("password");
        assertThat(userPrincipal.isEnabled()).isTrue();
        assertThat(userPrincipal.isAccountNonExpired()).isTrue();
        assertThat(userPrincipal.isAccountNonLocked()).isTrue();
        assertThat(userPrincipal.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Should have correct authorities")
    void shouldHaveCorrectAuthorities() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList("USER", "ADMIN"),
            true
        );

        // Then
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        assertThat(authorities).hasSize(2);
        assertThat(authorities)
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("Should handle empty roles list")
    void shouldHandleEmptyRolesList() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList(),
            true
        );

        // Then
        assertThat(userPrincipal.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("Should handle disabled user")
    void shouldHandleDisabledUser() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList("USER"),
            false
        );

        // Then
        assertThat(userPrincipal.isEnabled()).isFalse();
        assertThat(userPrincipal.isAccountNonExpired()).isTrue();
        assertThat(userPrincipal.isAccountNonLocked()).isTrue();
        assertThat(userPrincipal.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        // Given
        UserPrincipal user1 = UserPrincipal.create(
            1L, "user1", "user1@example.com", "password", Arrays.asList("USER"), true
        );
        UserPrincipal user2 = UserPrincipal.create(
            1L, "user2", "user2@example.com", "password", Arrays.asList("ADMIN"), true
        );
        UserPrincipal user3 = UserPrincipal.create(
            2L, "user3", "user3@example.com", "password", Arrays.asList("USER"), true
        );

        // Then
        assertThat(user1).isEqualTo(user2); // Same ID
        assertThat(user1).isNotEqualTo(user3); // Different ID
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isEqualTo(user1);
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        // Given
        UserPrincipal user1 = UserPrincipal.create(
            1L, "user1", "user1@example.com", "password", Arrays.asList("USER"), true
        );
        UserPrincipal user2 = UserPrincipal.create(
            1L, "user2", "user2@example.com", "password", Arrays.asList("ADMIN"), true
        );
        UserPrincipal user3 = UserPrincipal.create(
            2L, "user3", "user3@example.com", "password", Arrays.asList("USER"), true
        );

        // Then
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode()); // Same ID
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode()); // Different ID
    }

    @Test
    @DisplayName("Should handle single role")
    void shouldHandleSingleRole() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList("USER"),
            true
        );

        // Then
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Should handle multiple roles")
    void shouldHandleMultipleRoles() {
        // When
        UserPrincipal userPrincipal = UserPrincipal.create(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Arrays.asList("USER", "ADMIN", "MODERATOR"),
            true
        );

        // Then
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        assertThat(authorities).hasSize(3);
        assertThat(authorities)
            .extracting(GrantedAuthority::getAuthority)
            .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR");
    }

    @Test
    @DisplayName("Should create user principal with constructor")
    void shouldCreateUserPrincipalWithConstructor() {
        // Given
        UserPrincipal userPrincipal = new UserPrincipal();

        // When
        userPrincipal.setId(1L);
        userPrincipal.setUsername("testuser");
        userPrincipal.setEmail("test@example.com");
        userPrincipal.setEnabled(true);

        // Then
        assertThat(userPrincipal.getId()).isEqualTo(1L);
        assertThat(userPrincipal.getUsername()).isEqualTo("testuser");
        assertThat(userPrincipal.getEmail()).isEqualTo("test@example.com");
        assertThat(userPrincipal.isEnabled()).isTrue();
    }
}