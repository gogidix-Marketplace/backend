package com.gogidix.transaction.onboarding.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Tests")
class ExceptionTest {

    @Test
    @DisplayName("DuplicateOnboardingException")
    void testDuplicate() {
        DuplicateOnboardingException ex = new DuplicateOnboardingException("dup");
        assertEquals("dup", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("InvalidTransitionException")
    void testInvalidTransition() {
        InvalidTransitionException ex = new InvalidTransitionException("invalid");
        assertEquals("invalid", ex.getMessage());
    }

    @Test
    @DisplayName("OnboardingNotFoundException")
    void testNotFound() {
        OnboardingNotFoundException ex = new OnboardingNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
    }
}
