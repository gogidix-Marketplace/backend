package com.gogidix.aiservices.aidatavalidation.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionTest {
    @Test
    void validationExceptionMessage() { assertThat(new ValidationException("msg").getMessage()).isEqualTo("msg"); }
    @Test
    void validationExceptionCause() { var c = new RuntimeException("c"); assertThat(new ValidationException("msg", c).getCause()).isEqualTo(c); }
    @Test
    void validationNotFoundExceptionMessage() { assertThat(new ValidationNotFoundException("id").getMessage()).contains("id"); }
}
