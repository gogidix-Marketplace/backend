package com.gogidix.aiservices.aidocumentprocessingservice.shared.exception;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ExceptionTest {
    @Test void processing() { assertThat(new DocumentProcessingException("m").getMessage()).isEqualTo("m"); }
    @Test void processingCause() { assertThat(new DocumentProcessingException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
    @Test void notFound() { assertThat(new DocumentNotFoundException("x").getMessage()).contains("x"); }
}
