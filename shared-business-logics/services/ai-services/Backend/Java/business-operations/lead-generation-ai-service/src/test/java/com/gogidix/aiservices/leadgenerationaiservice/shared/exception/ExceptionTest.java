package com.gogidix.aiservices.leadgenerationaiservice.shared.exception;
import org.junit.jupiter.api.*; import static org.assertj.core.api.Assertions.*;
class ExceptionTest {
    @Test void leadGeneration() { var e = new LeadGenerationException("msg"); assertThat(e.getMessage()).isEqualTo("msg"); }
    @Test void leadGenerationCause() { assertThat(new LeadGenerationException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
    @Test void leadNotFound() { var e = new LeadNotFoundException("l1"); assertThat(e.getMessage()).contains("l1"); }
    @Test void leadNotFoundCause() { assertThat(new LeadNotFoundException("m", new RuntimeException()).getCause()).isInstanceOf(RuntimeException.class); }
}
