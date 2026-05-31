package com.gogidix.shared.infrastructure.services.security.usermanagement.interfaces.rest;

import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.CreateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.request.UpdateUserRequestDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.service.UserManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserManagementController Tests")
class UserManagementControllerTest {

    @Mock
    private UserManagementService userManagementService;

    @InjectMocks
    private UserManagementController controller;

    @Nested
    @DisplayName("Create User")
    class CreateUserTests {

        @Test
        void shouldCreateUser() {
            CreateUserRequestDto request = CreateUserRequestDto.builder()
                .userId("u1").firstName("John").lastName("Doe").build();
            UserProfileResponseDto response = UserProfileResponseDto.builder()
                .userId("u1").firstName("John").build();

            when(userManagementService.createUser(any())).thenReturn(response);

            ResponseEntity<UserProfileResponseDto> result = controller.createUser(request);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertNotNull(result.getBody());
            assertEquals("u1", result.getBody().getUserId());
        }
    }

    @Nested
    @DisplayName("Get User")
    class GetUserTests {

        @Test
        void shouldGetUser() {
            UserProfileResponseDto response = UserProfileResponseDto.builder().userId("u1").build();
            when(userManagementService.findById("u1")).thenReturn(Optional.of(response));

            ResponseEntity<UserProfileResponseDto> result = controller.getUser("u1");

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertNotNull(result.getBody());
        }

        @Test
        void shouldReturnNotFoundWhenUserMissing() {
            when(userManagementService.findById("missing")).thenReturn(Optional.empty());

            ResponseEntity<UserProfileResponseDto> result = controller.getUser("missing");

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
            assertNull(result.getBody());
        }
    }

    @Nested
    @DisplayName("Update User")
    class UpdateUserTests {

        @Test
        void shouldUpdateUser() {
            UpdateUserRequestDto request = UpdateUserRequestDto.builder().firstName("Jane").build();
            UserProfileResponseDto response = UserProfileResponseDto.builder().userId("u1").build();

            when(userManagementService.updateUser(anyString(), any())).thenReturn(response);

            ResponseEntity<UserProfileResponseDto> result = controller.updateUser("u1", request);

            assertEquals(HttpStatus.OK, result.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Delete User")
    class DeleteUserTests {

        @Test
        void shouldDeleteUser() {
            ResponseEntity<Void> result = controller.deleteUser("u1");

            assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
            verify(userManagementService).deleteUser("u1");
        }
    }

    @Nested
    @DisplayName("List Users")
    class ListUsersTests {

        @Test
        void shouldListUsers() {
            List<UserProfileResponseDto> users = List.of(
                UserProfileResponseDto.builder().userId("u1").build(),
                UserProfileResponseDto.builder().userId("u2").build()
            );
            when(userManagementService.listUsers()).thenReturn(users);

            ResponseEntity<List<UserProfileResponseDto>> result = controller.listUsers();

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(2, result.getBody().size());
        }
    }
}
