package com.gogidix.transaction.progress.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StepNotFoundException Tests")
class StepNotFoundExceptionTest {

    @Test
    @DisplayName("Should create with message")
    void shouldCreateWithMessage() {
        StepNotFoundException ex = new StepNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
        assertInstanceOf(RuntimeException.class, ex);
    }
}
