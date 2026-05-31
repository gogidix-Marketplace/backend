package com.gogidix.shared.infrastructure.services.security.usermanagement.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserNotFoundException Tests")
class UserNotFoundExceptionTest {

    @Test
    void shouldCreateWithUserId() {
        UserNotFoundException ex = new UserNotFoundException("user-1");
        assertTrue(ex.getMessage().contains("user-1"));
    }

    @Test
    void shouldCreateWithFieldAndValue() {
        UserNotFoundException ex = new UserNotFoundException("email", "test@test.com");
        assertTrue(ex.getMessage().contains("email"));
        assertTrue(ex.getMessage().contains("test@test.com"));
    }

    @Test
    void shouldBeRuntimeException() {
        UserNotFoundException ex = new UserNotFoundException("x");
        assertInstanceOf(RuntimeException.class, ex);
    }
}
