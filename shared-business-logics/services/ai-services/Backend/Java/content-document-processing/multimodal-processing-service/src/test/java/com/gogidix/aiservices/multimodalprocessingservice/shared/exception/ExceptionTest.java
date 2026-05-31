package com.gogidix.aiservices.multimodalprocessingservice.shared.exception;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ExceptionTest {
    @Test void msg() { assertThat(new MultimodalProcessingException("m").getMessage()).isEqualTo("m"); }
    @Test void cause() { assertThat(new MultimodalProcessingException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
}
