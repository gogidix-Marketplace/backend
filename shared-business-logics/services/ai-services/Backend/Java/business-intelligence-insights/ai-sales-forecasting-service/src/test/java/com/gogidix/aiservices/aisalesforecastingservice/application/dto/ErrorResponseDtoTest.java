package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ErrorResponseDtoTest {

    @Test
    void shouldCreateBasicError() {
        var dto = ErrorResponseDto.of(400, "Bad Request", "invalid", "/path", "corr1");
        assertThat(dto.status()).isEqualTo(400);
        assertThat(dto.error()).isEqualTo("Bad Request");
        assertThat(dto.message()).isEqualTo("invalid");
        assertThat(dto.path()).isEqualTo("/path");
        assertThat(dto.correlationId()).isEqualTo("corr1");
        assertThat(dto.fieldErrors()).isNull();
        assertThat(dto.timestamp()).isNotNull();
    }

    @Test
    void shouldCreateValidationError() {
        var errors = List.of(ErrorResponseDto.ValidationError.of("name", "required", ""));
        var dto = ErrorResponseDto.validation("validation failed", "/path", "corr1", errors);
        assertThat(dto.status()).isEqualTo(400);
        assertThat(dto.fieldErrors()).hasSize(1);
    }

    @Test
    void shouldCreateValidationErrorRecord() {
        var ve = new ErrorResponseDto.ValidationError("field1", "must not be blank", null);
        assertThat(ve.field()).isEqualTo("field1");
        assertThat(ve.message()).isEqualTo("must not be blank");
        assertThat(ve.rejectedValue()).isNull();
    }
}
