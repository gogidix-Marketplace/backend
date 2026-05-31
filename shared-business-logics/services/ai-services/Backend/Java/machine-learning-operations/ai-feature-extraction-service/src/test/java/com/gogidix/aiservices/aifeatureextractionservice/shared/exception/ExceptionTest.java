package com.gogidix.aiservices.aifeatureextractionservice.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionTest {

    @Test
    void validationExceptionMessage() {
        var ex = new ValidationException("test error");
        assertThat(ex.getMessage()).isEqualTo("test error");
    }

    @Test
    void validationExceptionWithCause() {
        var cause = new RuntimeException("root cause");
        var ex = new ValidationException("test", cause);
        assertThat(ex.getMessage()).isEqualTo("test");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
