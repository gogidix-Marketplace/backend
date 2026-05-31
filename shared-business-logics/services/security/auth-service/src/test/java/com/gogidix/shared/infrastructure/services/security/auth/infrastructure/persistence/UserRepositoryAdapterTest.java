package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.persistence;

import com.gogidix.shared.servicediscovery.config.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserRepositoryAdapter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id("user-123")
                .tenantId(new TenantId("tenant-123"))
                .username("john.doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .enabled(true)
                .roles(Set.of("USER"))
                .build();
    }

    @Test
    @DisplayName("Should implement UserRepositoryPort")
    void shouldImplementUserRepositoryPort() {
        // Then
        assertTrue(userRepositoryAdapter instanceof UserRepositoryPort);
    }

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userRepositoryAdapter.save(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should find user by id and tenant id")
    void shouldFindUserByIdAndTenantId() {
        // Given
        String userId = "user-123";
        String tenantId = "tenant-123";
        when(userRepository.findByIdAndTenantId_Value(userId, tenantId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userRepositoryAdapter.findByIdAndTenantId(userId, tenantId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByIdAndTenantId_Value(userId, tenantId);
    }

    @Test
    @DisplayName("Should return empty when user not found by id and tenant id")
    void shouldReturnEmptyWhenUserNotFoundByIdAndTenantId() {
        // Given
        String userId = "non-existent";
        String tenantId = "tenant-123";
        when(userRepository.findByIdAndTenantId_Value(userId, tenantId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByIdAndTenantId(userId, tenantId);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByIdAndTenantId_Value(userId, tenantId);
    }

    @Test
    @DisplayName("Should find user by username and tenant id")
    void shouldFindUserByUsernameAndTenantId() {
        // Given
        String username = "john.doe";
        String tenantId = "tenant-123";
        when(userRepository.findByUsernameAndTenantId_Value(username, tenantId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userRepositoryAdapter.findByUsernameAndTenantId(username, tenantId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByUsernameAndTenantId_Value(username, tenantId);
    }

    @Test
    @DisplayName("Should return empty when user not found by username and tenant id")
    void shouldReturnEmptyWhenUserNotFoundByUsernameAndTenantId() {
        // Given
        String username = "nonexistent";
        String tenantId = "tenant-123";
        when(userRepository.findByUsernameAndTenantId_Value(username, tenantId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByUsernameAndTenantId(username, tenantId);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByUsernameAndTenantId_Value(username, tenantId);
    }

    @Test
    @DisplayName("Should find user by email and tenant id")
    void shouldFindUserByEmailAndTenantId() {
        // Given
        String email = "john.doe@example.com";
        String tenantId = "tenant-123";
        when(userRepository.findByEmailAndTenantId_Value(email, tenantId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userRepositoryAdapter.findByEmailAndTenantId(email, tenantId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmailAndTenantId_Value(email, tenantId);
    }

    @Test
    @DisplayName("Should return empty when user not found by email and tenant id")
    void shouldReturnEmptyWhenUserNotFoundByEmailAndTenantId() {
        // Given
        String email = "nonexistent@example.com";
        String tenantId = "tenant-123";
        when(userRepository.findByEmailAndTenantId_Value(email, tenantId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByEmailAndTenantId(email, tenantId);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByEmailAndTenantId_Value(email, tenantId);
    }

    @Test
    @DisplayName("Should check if username exists for tenant")
    void shouldCheckIfUsernameExistsForTenant() {
        // Given
        String username = "john.doe";
        String tenantId = "tenant-123";
        when(userRepository.existsByUsernameAndTenantId_Value(username, tenantId)).thenReturn(true);

        // When
        boolean result = userRepositoryAdapter.existsByUsernameAndTenantId(username, tenantId);

        // Then
        assertTrue(result);
        verify(userRepository).existsByUsernameAndTenantId_Value(username, tenantId);
    }

    @Test
    @DisplayName("Should return false when username does not exist for tenant")
    void shouldReturnFalseWhenUsernameDoesNotExistForTenant() {
        // Given
        String username = "nonexistent";
        String tenantId = "tenant-123";
        when(userRepository.existsByUsernameAndTenantId_Value(username, tenantId)).thenReturn(false);

        // When
        boolean result = userRepositoryAdapter.existsByUsernameAndTenantId(username, tenantId);

        // Then
        assertFalse(result);
        verify(userRepository).existsByUsernameAndTenantId_Value(username, tenantId);
    }

    @Test
    @DisplayName("Should check if email exists for tenant")
    void shouldCheckIfEmailExistsForTenant() {
        // Given
        String email = "john.doe@example.com";
        String tenantId = "tenant-123";
        when(userRepository.existsByEmailAndTenantId_Value(email, tenantId)).thenReturn(true);

        // When
        boolean result = userRepositoryAdapter.existsByEmailAndTenantId(email, tenantId);

        // Then
        assertTrue(result);
        verify(userRepository).existsByEmailAndTenantId_Value(email, tenantId);
    }

    @Test
    @DisplayName("Should return false when email does not exist for tenant")
    void shouldReturnFalseWhenEmailDoesNotExistForTenant() {
        // Given
        String email = "nonexistent@example.com";
        String tenantId = "tenant-123";
        when(userRepository.existsByEmailAndTenantId_Value(email, tenantId)).thenReturn(false);

        // When
        boolean result = userRepositoryAdapter.existsByEmailAndTenantId(email, tenantId);

        // Then
        assertFalse(result);
        verify(userRepository).existsByEmailAndTenantId_Value(email, tenantId);
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        // When
        userRepositoryAdapter.delete(testUser);

        // Then
        verify(userRepository).delete(testUser);
    }

    @Test
    @DisplayName("Should handle deleting null user")
    void shouldHandleDeletingNullUser() {
        // When
        userRepositoryAdapter.delete(null);

        // Then
        verify(userRepository).delete(null);
    }

    @Test
    @DisplayName("Should forward save call to repository")
    void shouldForwardSaveCallToRepository() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userRepositoryAdapter.save(testUser);

        // Then
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should handle saving user with null tenant id")
    void shouldHandleSavingUserWithNullTenantId() {
        // Given
        testUser.setTenantId(null);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userRepositoryAdapter.save(testUser);

        // Then
        assertNotNull(result);
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should handle null username in findByUsernameAndTenantId")
    void shouldHandleNullUsernameInFindByUsernameAndTenantId() {
        // Given
        when(userRepository.findByUsernameAndTenantId_Value(isNull(), anyString())).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByUsernameAndTenantId(null, "tenant-123");

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should handle null tenant id in findByIdAndTenantId")
    void shouldHandleNullTenantIdInFindByIdAndTenantId() {
        // Given
        when(userRepository.findByIdAndTenantId_Value(anyString(), isNull())).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByIdAndTenantId("user-123", null);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should handle empty strings in queries")
    void shouldHandleEmptyStringsInQueries() {
        // Given
        when(userRepository.findByUsernameAndTenantId_Value("", "")).thenReturn(Optional.empty());

        // When
        Optional<User> result = userRepositoryAdapter.findByUsernameAndTenantId("", "");

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByUsernameAndTenantId_Value("", "");
    }

    @Test
    @DisplayName("Should verify adapter is component")
    void shouldVerifyAdapterIsComponent() {
        // Then
        assertTrue(UserRepositoryAdapter.class.isAnnotationPresent(org.springframework.stereotype.Component.class));
    }

    @Test
    @DisplayName("Should verify adapter has constructor accepting UserRepository")
    void shouldVerifyAdapterHasRequiredArgsConstructorAnnotation() throws NoSuchMethodException {
        assertNotNull(UserRepositoryAdapter.class.getDeclaredConstructor(UserRepository.class));
    }
}
