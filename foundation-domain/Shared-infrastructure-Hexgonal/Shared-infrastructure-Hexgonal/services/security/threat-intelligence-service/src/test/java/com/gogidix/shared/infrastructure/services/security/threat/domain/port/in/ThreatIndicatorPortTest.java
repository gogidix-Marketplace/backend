package com.gogidix.shared.infrastructure.services.security.threat.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ThreatIndicatorPort interface contract.
 */
@DisplayName("ThreatIndicatorPort Interface Tests")
class ThreatIndicatorPortTest {

    @Test
    @DisplayName("Should have ThreatIndicatorPort interface defined")
    void shouldHaveThreatIndicatorPortInterface() {
        assertNotNull(ThreatIndicatorPort.class);
        assertTrue(ThreatIndicatorPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have create method defined")
    void shouldHaveCreateMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("create", CreateThreatIndicatorRequestDto.class);
        assertNotNull(method);
        assertEquals(ThreatIndicatorResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findById method defined")
    void shouldHaveFindByIdMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("findById", String.class);
        assertNotNull(method);
        assertEquals(ThreatIndicatorResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findAll method defined")
    void shouldHaveFindAllMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("findAll");
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findActive method defined")
    void shouldHaveFindActiveMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("findActive");
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findByType method defined")
    void shouldHaveFindByTypeMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("findByType", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have findBySeverity method defined")
    void shouldHaveFindBySeverityMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("findBySeverity", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have update method defined")
    void shouldHaveUpdateMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("update", String.class, UpdateThreatIndicatorRequestDto.class);
        assertNotNull(method);
        assertEquals(ThreatIndicatorResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have delete method defined")
    void shouldHaveDeleteMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("delete", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have deactivate method defined")
    void shouldHaveDeactivateMethod() throws NoSuchMethodException {
        var method = ThreatIndicatorPort.class.getMethod("deactivate", String.class);
        assertNotNull(method);
        assertEquals(ThreatIndicatorResponseDto.class, method.getReturnType());
    }
}
