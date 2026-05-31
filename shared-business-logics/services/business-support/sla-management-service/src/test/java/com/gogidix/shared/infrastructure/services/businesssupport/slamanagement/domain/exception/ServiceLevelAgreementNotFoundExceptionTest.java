package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ServiceLevelAgreementNotFoundException Tests")
class ServiceLevelAgreementNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with ID")
    void shouldCreateExceptionWithId() {
        ServiceLevelAgreementNotFoundException ex = new ServiceLevelAgreementNotFoundException("sla-123");
        assertTrue(ex.getMessage().contains("sla-123"));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("Should be a RuntimeException")
    void shouldBeRuntimeException() {
        ServiceLevelAgreementNotFoundException ex = new ServiceLevelAgreementNotFoundException("id");
        assertInstanceOf(RuntimeException.class, ex);
    }
}
