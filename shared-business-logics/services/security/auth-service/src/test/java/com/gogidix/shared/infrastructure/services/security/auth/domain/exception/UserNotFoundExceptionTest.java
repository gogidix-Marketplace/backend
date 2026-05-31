package com.gogidix.shared.infrastructure.services.security.auth.domain.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserNotFoundException.
 */
@DisplayName("UserNotFoundException Tests")
class UserNotFoundExceptionTest {

    @Test
    @DisplayName("Should create UserNotFoundException with userId")
    void shouldCreateUserNotFoundExceptionWithUserId() {
        // Given
        String userId = "user-123";

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(userId));
        assertEquals(userId, exception.getUserId());
    }

    @Test
    @DisplayName("Should create UserNotFoundException with field and value")
    void shouldCreateUserNotFoundExceptionWithFieldAndValue() {
        // Given
        String field = "email";
        String value = "test@example.com";

        // When
        UserNotFoundException exception = new UserNotFoundException(field, value);

        // Then
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(field));
        assertTrue(exception.getMessage().contains(value));
        assertEquals(value, exception.getUserId());
    }

    @Test
    @DisplayName("Should be instance of AuthenticationException")
    void shouldBeInstanceOfAuthenticationException() {
        // When
        UserNotFoundException exception = new UserNotFoundException("user-123");

        // Then
        assertTrue(exception instanceof AuthenticationException);
    }

    @Test
    @DisplayName("Should return correct userId from getUserId")
    void shouldReturnCorrectUserIdFromGetUserId() {
        // Given
        String userId = "user-456";

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        assertEquals(userId, exception.getUserId());
    }

    @Test
    @DisplayName("Should return value as userId when using field constructor")
    void shouldReturnValueAsUserIdWhenUsingFieldConstructor() {
        // Given
        String value = "john.doe";

        // When
        UserNotFoundException exception = new UserNotFoundException("username", value);

        // Then
        assertEquals(value, exception.getUserId());
    }

    @Test
    @DisplayName("Should handle null userId")
    void shouldHandleNullUserId() {
        // When
        UserNotFoundException exception = new UserNotFoundException((String) null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getUserId());
    }

    @Test
    @DisplayName("Should handle empty userId")
    void shouldHandleEmptyUserId() {
        // Given
        String emptyUserId = "";

        // When
        UserNotFoundException exception = new UserNotFoundException(emptyUserId);

        // Then
        assertNotNull(exception);
        assertEquals(emptyUserId, exception.getUserId());
        assertTrue(exception.getMessage().contains(""));
    }

    @Test
    @DisplayName("Should handle null field and value")
    void shouldHandleNullFieldAndValue() {
        // When
        UserNotFoundException exception = new UserNotFoundException(null, null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getUserId());
    }

    @Test
    @DisplayName("Should be throwable and catchable")
    void shouldBeThrowableAndCatchable() {
        // Given
        String userId = "user-789";

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            throw new UserNotFoundException(userId);
        });
    }

    @Test
    @DisplayName("Should be catchable as AuthenticationException")
    void shouldBeCatchableAsAuthenticationException() {
        // Given
        String userId = "user-999";

        // When & Then
        assertThrows(AuthenticationException.class, () -> {
            throw new UserNotFoundException(userId);
        });
    }

    @Test
    @DisplayName("Should preserve stack trace")
    void shouldPreserveStackTrace() {
        // Given
        UserNotFoundException exception = new UserNotFoundException("user-123");

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    @DisplayName("Should create exception with different fields")
    void shouldCreateExceptionWithDifferentFields() {
        // Given
        UserNotFoundException byUsername = new UserNotFoundException("username", "john.doe");
        UserNotFoundException byEmail = new UserNotFoundException("email", "john@example.com");
        UserNotFoundException byId = new UserNotFoundException("user-123");

        // Then
        assertTrue(byUsername.getMessage().contains("username"));
        assertTrue(byEmail.getMessage().contains("email"));
        assertTrue(byId.getMessage().contains("user-123"));
    }

    @Test
    @DisplayName("Should handle special characters in userId")
    void shouldHandleSpecialCharactersInUserId() {
        // Given
        String specialUserId = "user@123#$%";

        // When
        UserNotFoundException exception = new UserNotFoundException(specialUserId);

        // Then
        assertEquals(specialUserId, exception.getUserId());
        assertTrue(exception.getMessage().contains(specialUserId));
    }

    @Test
    @DisplayName("Should handle userId with spaces")
    void shouldHandleUserIdWithSpaces() {
        // Given
        String userIdWithSpaces = "user 123";

        // When
        UserNotFoundException exception = new UserNotFoundException(userIdWithSpaces);

        // Then
        assertEquals(userIdWithSpaces, exception.getUserId());
    }

    @Test
    @DisplayName("Should create multiple independent instances")
    void shouldCreateMultipleIndependentInstances() {
        // When
        UserNotFoundException exception1 = new UserNotFoundException("user-1");
        UserNotFoundException exception2 = new UserNotFoundException("user-2");

        // Then
        assertNotEquals(exception1.getUserId(), exception2.getUserId());
        assertNotSame(exception1, exception2);
    }

    @Test
    @DisplayName("Should format message correctly with field constructor")
    void shouldFormatMessageCorrectlyWithFieldConstructor() {
        // Given
        String field = "email";
        String value = "test@example.com";

        // When
        UserNotFoundException exception = new UserNotFoundException(field, value);

        // Then
        String expectedMessage = "User not found with " + field + ": " + value;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should format message correctly with userId constructor")
    void shouldFormatMessageCorrectlyWithUserIdConstructor() {
        // Given
        String userId = "user-abc123";

        // When
        UserNotFoundException exception = new UserNotFoundException(userId);

        // Then
        String expectedMessage = "User not found: " + userId;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
