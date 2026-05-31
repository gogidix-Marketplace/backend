package com.gogidix.aiservices.aiinferenceservice.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionGenTest {

    @Test
    void message() {
        var ex = new ValidationException("err");
        assertThat(ex.getMessage()).isEqualTo("err");
    }

    @Test
    void withCause() {
        var cause = new RuntimeException("c");
        var ex = new ValidationException("m", cause);
        assertThat(ex.getMessage()).isEqualTo("m");
        assertThat(ex.getCause()).isSameAs(cause);
    }
}
