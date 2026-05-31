package com.gogidix.shared.infrastructure.services.security.orchestration.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.CreateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.request.UpdateSecurityWorkflowRequestDto;
import com.gogidix.shared.infrastructure.services.security.orchestration.application.dto.response.SecurityWorkflowResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityWorkflowPort interface contract.
 */
@DisplayName("SecurityWorkflowPort Interface Tests")
class SecurityWorkflowPortTest {

    @Test
    @DisplayName("Should have SecurityWorkflowPort interface defined")
    void shouldHaveSecurityWorkflowPortInterface() {
        assertNotNull(SecurityWorkflowPort.class);
        assertTrue(SecurityWorkflowPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have create method defined")
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("create", CreateSecurityWorkflowRequestDto.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findById method defined")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("findById", String.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findAll method defined")
    void shouldHaveFindAllMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("findAll");
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findByStatus method defined")
    void shouldHaveFindByStatusMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("findByStatus", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have update method defined")
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("update", String.class, UpdateSecurityWorkflowRequestDto.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have delete method defined")
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("delete", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have activate method defined")
    void shouldHaveActivateMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("activate", String.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have deactivate method defined")
    void shouldHaveDeactivateMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("deactivate", String.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have execute method defined")
    void shouldHaveExecuteMethod() throws NoSuchMethodException {
        var method = SecurityWorkflowPort.class.getMethod("execute", String.class);
        assertNotNull(method);
        assertEquals(SecurityWorkflowResponseDto.class, method.getReturnType());
    }
}
