package com.gogidix.centralizeddashboard.reporting.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportExceptionTest {

    @Test
    void constructor_withMessage() {
        ExportException ex = new ExportException("test error");
        assertEquals("test error", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void constructor_withMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        ExportException ex = new ExportException("test error", cause);
        assertEquals("test error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void isRuntimeException() {
        ExportException ex = new ExportException("test");
        assertTrue(ex instanceof RuntimeException);
    }
}
