package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportGenerationExceptionTest {

    @Test
    void constructor_withMessage() {
        ReportGenerationException ex = new ReportGenerationException("error");
        assertEquals("error", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void constructor_withMessageAndCause() {
        RuntimeException cause = new RuntimeException("root");
        ReportGenerationException ex = new ReportGenerationException("error", cause);
        assertEquals("error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void isRuntimeException() {
        assertTrue(new ReportGenerationException("x") instanceof RuntimeException);
    }
}
