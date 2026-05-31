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

@DisplayName("DTO Comprehensive Tests")
class DtoComprehensiveTest {

    @Nested
    @DisplayName("CreateUserRequestDto Equality and ToString")
    class CreateUserDtoTests {

        @Test
        void shouldBeEqualWithSameFields() {
            CreateUserRequestDto d1 = CreateUserRequestDto.builder()
                .userId("u1").firstName("John").lastName("Doe").build();
            CreateUserRequestDto d2 = CreateUserRequestDto.builder()
                .userId("u1").firstName("John").lastName("Doe").build();
            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            CreateUserRequestDto d1 = CreateUserRequestDto.builder().userId("u1").build();
            CreateUserRequestDto d2 = CreateUserRequestDto.builder().userId("u2").build();
            assertNotEquals(d1, d2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            CreateUserRequestDto d = CreateUserRequestDto.builder().build();
            assertNotEquals(null, d);
        }

        @Test
        void shouldBeEqualToSelf() {
            CreateUserRequestDto d = CreateUserRequestDto.builder().build();
            assertEquals(d, d);
        }

        @Test
        void shouldHaveToString() {
            CreateUserRequestDto d = CreateUserRequestDto.builder().userId("u1").firstName("John").build();
            assertNotNull(d.toString());
        }

        @Test
        void shouldBuildWithAllFields() {
            CreateUserRequestDto dto = CreateUserRequestDto.builder()
                .userId("u1").firstName("J").lastName("D").displayName("JD")
                .email("e@t.com").phoneNumber("+1").avatarUrl("a.png").bio("b")
                .address(UserProfile.Address.builder().build())
                .preferences(UserProfile.UserPreferences.builder().build())
                .build();
            assertNotNull(dto);
            assertEquals("u1", dto.getUserId());
        }

        @Test
        void shouldUseNoArgsConstructor() {
            CreateUserRequestDto dto = new CreateUserRequestDto();
            assertNotNull(dto);
            assertNull(dto.getUserId());
        }

        @Test
        void shouldUseAllArgsConstructor() {
            CreateUserRequestDto dto = new CreateUserRequestDto(
                "u1", "J", "D", "JD", "e@t.com", "+1", "a.png", "b",
                null, null
            );
            assertEquals("u1", dto.getUserId());
        }
    }

    @Nested
    @DisplayName("UpdateUserRequestDto Equality and ToString")
    class UpdateUserDtoTests {

        @Test
        void shouldBeEqualWithSameFields() {
            UpdateUserRequestDto d1 = UpdateUserRequestDto.builder()
                .firstName("John").lastName("Doe").build();
            UpdateUserRequestDto d2 = UpdateUserRequestDto.builder()
                .firstName("John").lastName("Doe").build();
            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            UpdateUserRequestDto d1 = UpdateUserRequestDto.builder().firstName("John").build();
            UpdateUserRequestDto d2 = UpdateUserRequestDto.builder().firstName("Jane").build();
            assertNotEquals(d1, d2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UpdateUserRequestDto d = UpdateUserRequestDto.builder().build();
            assertNotEquals(null, d);
        }

        @Test
        void shouldBeEqualToSelf() {
            UpdateUserRequestDto d = UpdateUserRequestDto.builder().build();
            assertEquals(d, d);
        }

        @Test
        void shouldHaveToString() {
            UpdateUserRequestDto d = UpdateUserRequestDto.builder().firstName("John").build();
            assertNotNull(d.toString());
        }

        @Test
        void shouldBuildWithAllFields() {
            UpdateUserRequestDto dto = UpdateUserRequestDto.builder()
                .firstName("J").lastName("D").displayName("JD")
                .email("e@t.com").phoneNumber("+1").avatarUrl("a.png").bio("b")
                .address(UserProfile.Address.builder().city("NYC").build())
                .preferences(UserProfile.UserPreferences.builder().language("en").build())
                .build();
            assertNotNull(dto);
        }

        @Test
        void shouldUseAllArgsConstructor() {
            UpdateUserRequestDto dto = new UpdateUserRequestDto(
                "J", "D", "JD", "e@t.com", "+1", "a.png", "b", null, null
            );
            assertEquals("J", dto.getFirstName());
        }
    }

    @Nested
    @DisplayName("UserProfileResponseDto Equality and ToString")
    class ResponseDtoTests {

        @Test
        void shouldBeEqualWithSameFields() {
            UserProfileResponseDto d1 = UserProfileResponseDto.builder()
                .id("1").userId("u1").build();
            UserProfileResponseDto d2 = UserProfileResponseDto.builder()
                .id("1").userId("u1").build();
            assertEquals(d1, d2);
            assertEquals(d1.hashCode(), d2.hashCode());
        }

        @Test
        void shouldNotBeEqualWithDifferentFields() {
            UserProfileResponseDto d1 = UserProfileResponseDto.builder().id("1").build();
            UserProfileResponseDto d2 = UserProfileResponseDto.builder().id("2").build();
            assertNotEquals(d1, d2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            UserProfileResponseDto d = UserProfileResponseDto.builder().build();
            assertNotEquals(null, d);
        }

        @Test
        void shouldBeEqualToSelf() {
            UserProfileResponseDto d = UserProfileResponseDto.builder().build();
            assertEquals(d, d);
        }

        @Test
        void shouldHaveToString() {
            UserProfileResponseDto d = UserProfileResponseDto.builder().userId("u1").build();
            assertNotNull(d.toString());
        }

        @Test
        void shouldBuildWithAllFields() {
            UserProfileResponseDto dto = UserProfileResponseDto.builder()
                .id("1").userId("u1").tenantId("t1")
                .firstName("J").lastName("D").displayName("JD")
                .email("e@t.com").phoneNumber("+1").avatarUrl("a.png").bio("b")
                .address(UserProfile.Address.builder().build())
                .preferences(UserProfile.UserPreferences.builder().build())
                .metadata(Map.of("k", "v"))
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
            assertNotNull(dto);
            assertEquals("1", dto.getId());
            assertEquals("t1", dto.getTenantId());
        }

        @Test
        void shouldUseAllArgsConstructor() {
            UserProfileResponseDto dto = new UserProfileResponseDto(
                "1", "u1", "t1", "J", "D", "JD", "e@t.com", "+1", "a.png", "b",
                null, null, null, null, null
            );
            assertEquals("1", dto.getId());
        }
    }
}
