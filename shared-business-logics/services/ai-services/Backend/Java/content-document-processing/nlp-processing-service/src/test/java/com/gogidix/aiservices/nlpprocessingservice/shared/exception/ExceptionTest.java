package com.gogidix.aiservices.nlpprocessingservice.shared.exception;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ExceptionTest {
    @Test void msg() { assertThat(new NlpProcessingException("m").getMessage()).isEqualTo("m"); }
    @Test void cause() { assertThat(new NlpProcessingException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
}
