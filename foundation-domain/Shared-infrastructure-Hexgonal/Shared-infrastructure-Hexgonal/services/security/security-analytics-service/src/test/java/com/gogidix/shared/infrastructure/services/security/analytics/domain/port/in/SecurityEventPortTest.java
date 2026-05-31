package com.gogidix.shared.infrastructure.services.security.analytics.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityEventPort interface contract.
 */
@DisplayName("SecurityEventPort Interface Tests")
class SecurityEventPortTest {

    @Test
    @DisplayName("Should have SecurityEventPort interface defined")
    void shouldHaveSecurityEventPortInterface() {
        assertNotNull(SecurityEventPort.class);
        assertTrue(SecurityEventPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have create method defined")
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("create", CreateSecurityEventRequestDto.class);
        assertNotNull(method);
        assertEquals(SecurityEventResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findById method defined")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("findById", String.class);
        assertNotNull(method);
        assertEquals(SecurityEventResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findAll method defined")
    void shouldHaveFindAllMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("findAll");
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findBySeverity method defined")
    void shouldHaveFindBySeverityMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("findBySeverity", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findByEventType method defined")
    void shouldHaveFindByEventTypeMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("findByEventType", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findRecent method defined")
    void shouldHaveFindRecentMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("findRecent", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have delete method defined")
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        var method = SecurityEventPort.class.getMethod("delete", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }
}
