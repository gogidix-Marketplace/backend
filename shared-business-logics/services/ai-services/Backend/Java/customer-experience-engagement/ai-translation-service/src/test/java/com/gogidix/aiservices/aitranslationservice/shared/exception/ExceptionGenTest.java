package com.gogidix.aiservices.aitranslationservice.shared.exception;

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
        var ex = new ValidationException("m", new RuntimeException("c"));
        assertThat(ex.getCause()).isNotNull();
    }
}
