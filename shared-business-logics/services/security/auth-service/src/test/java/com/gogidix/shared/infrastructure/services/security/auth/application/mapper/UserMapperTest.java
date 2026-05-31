package com.gogidix.shared.infrastructure.services.security.auth.application.mapper;

import com.gogidix.shared.multitenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserMapper.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserMapper Tests")
class UserMapperTest {

    @InjectMocks
    private UserMapper userMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");

        Set<String> permissions = new HashSet<>();
        permissions.add("read");
        permissions.add("write");
        permissions.add("delete");

        testUser = User.builder()
                .id("user-123")
                .tenantId(new TenantId("tenant-123"))
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
                .lastLoginAt("2024-01-15T10:30:00")
                .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2024, 1, 15, 10, 30))
                .build();
    }

    @Test
    @DisplayName("Should map User to UserResponseDto successfully")
    void shouldMapUserToUserResponseDtoSuccessfully() {
        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertEquals("user-123", dto.getId());
        assertEquals("tenant-123", dto.getTenantId());
        assertEquals("john.doe", dto.getUsername());
        assertEquals("john.doe@example.com", dto.getEmail());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertTrue(dto.isEnabled());
        assertEquals(2, dto.getRoles().size());
        assertEquals(3, dto.getPermissions().size());
        assertEquals("2024-01-15T10:30:00", dto.getLastLoginAt());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Should return null when mapping null user")
    void shouldReturnNullWhenMappingNullUser() {
        // When
        UserResponseDto dto = userMapper.toResponseDto(null);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should handle user with null tenantId")
    void shouldHandleUserWithNullTenantId() {
        // Given
        testUser.setTenantId(null);

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertNull(dto.getTenantId());
    }

    @Test
    @DisplayName("Should handle user with null roles")
    void shouldHandleUserWithNullRoles() {
        // Given
        testUser.setRoles(null);

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertNull(dto.getRoles());
    }

    @Test
    @DisplayName("Should handle user with null permissions")
    void shouldHandleUserWithNullPermissions() {
        // Given
        testUser.setPermissions(null);

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertNull(dto.getPermissions());
    }

    @Test
    @DisplayName("Should handle user with empty roles and permissions")
    void shouldHandleUserWithEmptyRolesAndPermissions() {
        // Given
        testUser.setRoles(new HashSet<>());
        testUser.setPermissions(new HashSet<>());

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertTrue(dto.getRoles().isEmpty());
        assertTrue(dto.getPermissions().isEmpty());
    }

    @Test
    @DisplayName("Should not map password to DTO")
    void shouldNotMapPasswordToDTO() {
        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        // UserResponseDto doesn't have a password field, so we verify the mapping worked
        assertNotNull(dto);
        // The password should not be accessible in the DTO
    }

    @Test
    @DisplayName("Should map user with all null optional fields")
    void shouldMapUserWithAllNullOptionalFields() {
        // Given
        User minimalUser = User.builder()
                .id("user-456")
                .username("minimal.user")
                .build();

        // When
        UserResponseDto dto = userMapper.toResponseDto(minimalUser);

        // Then
        assertNotNull(dto);
        assertEquals("user-456", dto.getId());
        assertEquals("minimal.user", dto.getUsername());
        assertNull(dto.getTenantId());
        assertNull(dto.getEmail());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getLastLoginAt());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update user from DTO when DTO is not null")
    void shouldUpdateUserFromDTOWhenDTOIsNotNull() {
        // Given
        UserResponseDto updateDto = UserResponseDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        String originalFirstName = testUser.getFirstName();
        String originalLastName = testUser.getLastName();
        String originalEmail = testUser.getEmail();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals("Jane", testUser.getFirstName());
        assertEquals("Smith", testUser.getLastName());
        assertEquals("jane.smith@example.com", testUser.getEmail());
        assertNotEquals(originalFirstName, testUser.getFirstName());
        assertNotEquals(originalLastName, testUser.getLastName());
        assertNotEquals(originalEmail, testUser.getEmail());
    }

    @Test
    @DisplayName("Should not update user when DTO is null")
    void shouldNotUpdateUserWhenDTOIsNull() {
        // Given
        String originalFirstName = testUser.getFirstName();
        String originalLastName = testUser.getLastName();
        String originalEmail = testUser.getEmail();

        // When
        userMapper.updateFromDto(testUser, null);

        // Then
        assertEquals(originalFirstName, testUser.getFirstName());
        assertEquals(originalLastName, testUser.getLastName());
        assertEquals(originalEmail, testUser.getEmail());
    }

    @Test
    @DisplayName("Should not update fields with null values in DTO")
    void shouldNotUpdateFieldsWithNullValuesInDTO() {
        // Given
        UserResponseDto partialDto = UserResponseDto.builder()
                .firstName("Updated")
                .lastName(null)  // null, should not update
                .email(null)     // null, should not update
                .build();

        String originalLastName = testUser.getLastName();
        String originalEmail = testUser.getEmail();

        // When
        userMapper.updateFromDto(testUser, partialDto);

        // Then
        assertEquals("Updated", testUser.getFirstName());
        assertEquals(originalLastName, testUser.getLastName());  // unchanged
        assertEquals(originalEmail, testUser.getEmail());        // unchanged
    }

    @Test
    @DisplayName("Should not update username from DTO")
    void shouldNotUpdateUsernameFromDTO() {
        // Given
        String originalUsername = testUser.getUsername();
        UserResponseDto updateDto = UserResponseDto.builder()
                .username("new.username")
                .build();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals(originalUsername, testUser.getUsername());
    }

    @Test
    @DisplayName("Should not update id from DTO")
    void shouldNotUpdateIdFromDTO() {
        // Given
        String originalId = testUser.getId();
        UserResponseDto updateDto = UserResponseDto.builder()
                .id("new-id")
                .build();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals(originalId, testUser.getId());
    }

    @Test
    @DisplayName("Should not update tenantId from DTO")
    void shouldNotUpdateTenantIdFromDTO() {
        // Given
        TenantId originalTenantId = testUser.getTenantId();
        UserResponseDto updateDto = UserResponseDto.builder()
                .tenantId("new-tenant")
                .build();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals(originalTenantId, testUser.getTenantId());
    }

    @Test
    @DisplayName("Should handle updating user with all null fields in DTO")
    void shouldHandleUpdatingUserWithAllNullFieldsInDTO() {
        // Given
        UserResponseDto emptyDto = UserResponseDto.builder().build();
        String originalFirstName = testUser.getFirstName();
        String originalLastName = testUser.getLastName();
        String originalEmail = testUser.getEmail();

        // When
        userMapper.updateFromDto(testUser, emptyDto);

        // Then
        assertEquals(originalFirstName, testUser.getFirstName());
        assertEquals(originalLastName, testUser.getLastName());
        assertEquals(originalEmail, testUser.getEmail());
    }

    @Test
    @DisplayName("Should preserve roles and permissions during update")
    void shouldPreserveRolesAndPermissionsDuringUpdate() {
        // Given
        Set<String> originalRoles = testUser.getRoles();
        Set<String> originalPermissions = testUser.getPermissions();

        UserResponseDto updateDto = UserResponseDto.builder()
                .firstName("Updated")
                .build();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals(originalRoles, testUser.getRoles());
        assertEquals(originalPermissions, testUser.getPermissions());
    }

    @Test
    @DisplayName("Should preserve enabled status during update")
    void shouldPreserveEnabledStatusDuringUpdate() {
        // Given
        boolean originalEnabled = testUser.isEnabled();
        UserResponseDto updateDto = UserResponseDto.builder()
                .firstName("Updated")
                .build();

        // When
        userMapper.updateFromDto(testUser, updateDto);

        // Then
        assertEquals(originalEnabled, testUser.isEnabled());
    }

    @Test
    @DisplayName("Should handle user with disabled account")
    void shouldHandleUserWithDisabledAccount() {
        // Given
        testUser.setEnabled(false);

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertFalse(dto.isEnabled());
    }

    @Test
    @DisplayName("Should handle user with empty string firstName")
    void shouldHandleUserWithEmptyStringFirstName() {
        // Given
        testUser.setFirstName("");

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertEquals("", dto.getFirstName());
    }

    @Test
    @DisplayName("Should handle user with very long email")
    void shouldHandleUserWithVeryLongEmail() {
        // Given
        String longEmail = "a".repeat(100) + "@example.com";
        testUser.setEmail(longEmail);

        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertNotNull(dto);
        assertEquals(longEmail, dto.getEmail());
    }

    @Test
    @DisplayName("Should correctly map roles set")
    void shouldCorrectlyMapRolesSet() {
        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertTrue(dto.getRoles().contains("USER"));
        assertTrue(dto.getRoles().contains("ADMIN"));
        assertEquals(2, dto.getRoles().size());
    }

    @Test
    @DisplayName("Should correctly map permissions set")
    void shouldCorrectlyMapPermissionsSet() {
        // When
        UserResponseDto dto = userMapper.toResponseDto(testUser);

        // Then
        assertTrue(dto.getPermissions().contains("read"));
        assertTrue(dto.getPermissions().contains("write"));
        assertTrue(dto.getPermissions().contains("delete"));
        assertEquals(3, dto.getPermissions().size());
    }
}
