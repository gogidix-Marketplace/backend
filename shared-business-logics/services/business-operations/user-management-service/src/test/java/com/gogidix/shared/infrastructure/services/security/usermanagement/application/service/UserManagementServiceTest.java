package com.gogidix.shared.infrastructure.services.security.usermanagement.application.service;

import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import com.gogidix.shared.multitenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.mapper.UserProfileMapper;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.exception.UserNotFoundException;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.port.out.UserProfileRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserManagementService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Management Service Tests")
class UserManagementServiceTest {

    @Mock
    private UserProfileRepositoryPort userProfileRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private UserManagementService userManagementService;

    private static final String TEST_TENANT_ID = "tenant-001";
    private UserProfile testUserProfile;
    private UserProfileResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        testUserProfile = UserProfile.builder()
                .id("profile-123")
                .userId("user-123")
                .tenantId(TenantId.of(TEST_TENANT_ID))
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@example.com")
                .build();

        testResponseDto = UserProfileResponseDto.builder()
                .id("profile-123")
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@example.com")
                .build();
    }

    @Test
    @DisplayName("Should create user profile successfully")
    void shouldCreateUserProfileSuccessfully() {
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@example.com")
                .build();

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        UserProfileResponseDto result = userManagementService.createUser(request);

        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should create user profile with default display name")
    void shouldCreateUserProfileWithDefaultDisplayName() {
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .displayName(null)
                .build();

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        userManagementService.createUser(request);

        verify(userProfileRepository).save(argThat(profile ->
                "John Doe".equals(profile.getDisplayName())
        ));
    }

    @Test
    @DisplayName("Should create user profile with default preferences")
    void shouldCreateUserProfileWithDefaultPreferences() {
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .preferences(null)
                .build();

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        userManagementService.createUser(request);

        verify(userProfileRepository).save(argThat(profile ->
                profile.getPreferences() != null
        ));
    }

    @Test
    @DisplayName("Should update user profile successfully")
    void shouldUpdateUserProfileSuccessfully() {
        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testUserProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        UserProfileResponseDto result = userManagementService.updateUser("user-123", request);

        assertNotNull(result);
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .firstName("Jane")
                .build();

        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userManagementService.updateUser("non-existent", request)
        );

        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Should update only provided fields")
    void shouldUpdateOnlyProvidedFields() {
        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .firstName("Jane")
                .build();

        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testUserProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        userManagementService.updateUser("user-123", request);

        verify(userProfileRepository).save(argThat(profile ->
                "Jane".equals(profile.getFirstName()) &&
                "Doe".equals(profile.getLastName())
        ));
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindUserById() {
        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testUserProfile));
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        Optional<UserProfileResponseDto> result = userManagementService.findById("user-123");

        assertTrue(result.isPresent());
        assertEquals("user-123", result.get().getUserId());
        verify(userProfileRepository).findByIdAndTenantId("user-123", TEST_TENANT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        Optional<UserProfileResponseDto> result = userManagementService.findById("non-existent");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testUserProfile));

        userManagementService.deleteUser("user-123");

        verify(userProfileRepository).delete(testUserProfile);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userManagementService.deleteUser("non-existent")
        );

        verify(userProfileRepository, never()).delete(any(UserProfile.class));
    }

    @Test
    @DisplayName("Should list all users for tenant")
    void shouldListAllUsersForTenant() {
        List<UserProfile> profiles = Arrays.asList(testUserProfile);
        when(userProfileRepository.findByTenantId(anyString())).thenReturn(profiles);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        List<UserProfileResponseDto> result = userManagementService.listUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userProfileRepository).findByTenantId(TEST_TENANT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should update user preferences")
    void shouldUpdateUserPreferences() {
        UserProfile.UserPreferences preferences = UserProfile.UserPreferences.builder()
                .language("en")
                .theme("dark")
                .build();

        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testUserProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        UserProfileResponseDto result = userManagementService.updatePreferences("user-123", preferences);

        assertNotNull(result);
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when updating preferences for non-existent user")
    void shouldThrowExceptionWhenUpdatingPreferencesForNonExistentUser() {
        UserProfile.UserPreferences preferences = UserProfile.UserPreferences.builder().build();

        when(userProfileRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userManagementService.updatePreferences("non-existent", preferences)
        );

        verify(userProfileRepository, never()).save(any(UserProfile.class));
    }

    @Test
    @DisplayName("Should create user with address")
    void shouldCreateUserWithAddress() {
        UserProfile.Address address = UserProfile.Address.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("USA")
                .build();

        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .address(address)
                .build();

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        userManagementService.createUser(request);

        verify(userProfileRepository).save(argThat(profile ->
                profile.getAddress() != null &&
                "New York".equals(profile.getAddress().getCity())
        ));
    }

    @Test
    @DisplayName("Should create user with preferences")
    void shouldCreateUserWithPreferences() {
        UserProfile.UserPreferences preferences = UserProfile.UserPreferences.builder()
                .language("es")
                .timezone("UTC")
                .theme("light")
                .build();

        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .preferences(preferences)
                .build();

        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(testUserProfile);
        when(userProfileMapper.toResponseDto(any(UserProfile.class))).thenReturn(testResponseDto);

        userManagementService.createUser(request);

        verify(userProfileRepository).save(argThat(profile ->
                profile.getPreferences() != null &&
                "es".equals(profile.getPreferences().getLanguage())
        ));
    }
}
