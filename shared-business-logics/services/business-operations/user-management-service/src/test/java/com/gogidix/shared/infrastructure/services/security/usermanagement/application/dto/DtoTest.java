package com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto;

import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Tests")
class DtoTest {

    @Nested
    @DisplayName("CreateUserRequestDto")
    class CreateUserRequestDtoTests {

        @Test
        void shouldBuildAndAccess() {
            CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .userId("u1")
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@test.com")
                .phoneNumber("+123")
                .avatarUrl("http://avatar")
                .bio("bio")
                .address(UserProfile.Address.builder().city("NYC").build())
                .preferences(UserProfile.UserPreferences.builder().language("en").build())
                .build();

            assertEquals("u1", dto.getUserId());
            assertEquals("John", dto.getFirstName());
            assertEquals("Doe", dto.getLastName());
            assertEquals("john@test.com", dto.getEmail());
        }

        @Test
        void shouldSetFields() {
            CreateUserRequestDto dto = new CreateUserRequestDto();
            dto.setUserId("u1");
            dto.setFirstName("Jane");
            assertEquals("u1", dto.getUserId());
            assertEquals("Jane", dto.getFirstName());
        }
    }

    @Nested
    @DisplayName("UpdateUserRequestDto")
    class UpdateUserRequestDtoTests {

        @Test
        void shouldBuildAndAccess() {
            UpdateUserRequestDto dto = UpdateUserRequestDto.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@test.com")
                .build();

            assertEquals("Jane", dto.getFirstName());
            assertEquals("Smith", dto.getLastName());
        }

        @Test
        void shouldSetFields() {
            UpdateUserRequestDto dto = new UpdateUserRequestDto();
            dto.setBio("new bio");
            dto.setDisplayName("New Name");
            assertEquals("new bio", dto.getBio());
            assertEquals("New Name", dto.getDisplayName());
        }
    }

    @Nested
    @DisplayName("UserProfileResponseDto")
    class ResponseDtoTests {

        @Test
        void shouldBuildAndAccess() {
            UserProfileResponseDto dto = UserProfileResponseDto.builder()
                .id("p1")
                .userId("u1")
                .tenantId("t1")
                .firstName("John")
                .lastName("Doe")
                .displayName("John Doe")
                .email("john@test.com")
                .phoneNumber("+123")
                .avatarUrl("http://avatar")
                .bio("bio")
                .address(UserProfile.Address.builder().city("NYC").build())
                .preferences(UserProfile.UserPreferences.builder().language("en").build())
                .metadata(Map.of("k", "v"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            assertEquals("p1", dto.getId());
            assertEquals("u1", dto.getUserId());
            assertEquals("t1", dto.getTenantId());
        }

        @Test
        void shouldSetFields() {
            UserProfileResponseDto dto = new UserProfileResponseDto();
            dto.setId("x");
            dto.setUserId("y");
            assertEquals("x", dto.getId());
            assertEquals("y", dto.getUserId());
        }
    }
}
