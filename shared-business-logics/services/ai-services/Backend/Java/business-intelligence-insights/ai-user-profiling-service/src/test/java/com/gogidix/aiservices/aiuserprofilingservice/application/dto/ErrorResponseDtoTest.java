package com.gogidix.aiservices.aiuserprofilingservice.application.dto;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class ErrorResponseDtoTest {
    @Test
    void shouldCreateBasicError() {
        var dto = ErrorResponseDto.of(400, "Bad Request", "invalid", "/path", "corr1");
        assertThat(dto.status()).isEqualTo(400);
        assertThat(dto.fieldErrors()).isNull();
    }
    @Test
    void shouldCreateValidationError() {
        var errors = List.of(ErrorResponseDto.ValidationError.of("name", "required", ""));
        var dto = ErrorResponseDto.validation("failed", "/path", "corr1", errors);
        assertThat(dto.fieldErrors()).hasSize(1);
    }
}
