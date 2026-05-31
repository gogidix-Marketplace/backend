package com.gogidix.aiservices.aidatavalidation.domain;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ValidationResultDomainTest {
    @Test
    void builder() {
        var r = ValidationResult.builder()
            .validationId("id")
            .valid(true)
            .errors(List.of())
            .warnings(List.of())
            .statistics(new ValidationStatistics(10, 8, 1, 1))
            .validatedAt(LocalDateTime.now())
            .build();
        assertThat(r.getValidationId()).isEqualTo("id");
        assertThat(r.isValid()).isTrue();
        assertThat(r.getErrors()).isEmpty();
    }

    @Test
    void withErrorsAndWarnings() {
        var r = ValidationResult.builder()
            .validationId("id")
            .valid(false)
            .errors(List.of(new ValidationError("f", "CODE", "msg")))
            .warnings(List.of(new ValidationWarning("f", "CODE", "msg")))
            .statistics(new ValidationStatistics(10, 8, 1, 1))
            .validatedAt(LocalDateTime.now())
            .build();
        assertThat(r.isValid()).isFalse();
        assertThat(r.getErrors()).hasSize(1);
        assertThat(r.getWarnings()).hasSize(1);
    }
}
