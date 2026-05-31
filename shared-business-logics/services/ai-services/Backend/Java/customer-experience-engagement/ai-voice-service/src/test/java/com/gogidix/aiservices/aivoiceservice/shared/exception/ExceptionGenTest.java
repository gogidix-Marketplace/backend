package com.gogidix.aiservices.aivoiceservice.shared.exception;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ExceptionGenTest {
    @Test void msg() { assertThat(new ValidationException("e").getMessage()).isEqualTo("e"); }
    @Test void cause() { assertThat(new ValidationException("m", new RuntimeException()).getCause()).isNotNull(); }
}
