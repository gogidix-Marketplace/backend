package com.gogidix.shared.infrastructure.services.security.dlp.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DlpPolicyPort interface contract.
 */
@DisplayName("DlpPolicyPort Interface Tests")
class DlpPolicyPortTest {

    @Test
    @DisplayName("Should have DlpPolicyPort interface defined")
    void shouldHaveDlpPolicyPortInterface() {
        assertNotNull(DlpPolicyPort.class);
        assertTrue(DlpPolicyPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have create method defined")
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("create", CreateDlpPolicyRequestDto.class);
        assertNotNull(method);
        assertEquals(DlpPolicyResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findById method defined")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("findById", String.class);
        assertNotNull(method);
        assertEquals(DlpPolicyResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findAll method defined")
    void shouldHaveFindAllMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("findAll");
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findByStatus method defined")
    void shouldHaveFindByStatusMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("findByStatus", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have update method defined")
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("update", String.class, UpdateDlpPolicyRequestDto.class);
        assertNotNull(method);
        assertEquals(DlpPolicyResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have delete method defined")
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("delete", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have activate method defined")
    void shouldHaveActivateMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("activate", String.class);
        assertNotNull(method);
        assertEquals(DlpPolicyResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have deactivate method defined")
    void shouldHaveDeactivateMethod() throws NoSuchMethodException {
        var method = DlpPolicyPort.class.getMethod("deactivate", String.class);
        assertNotNull(method);
        assertEquals(DlpPolicyResponseDto.class, method.getReturnType());
    }
}
