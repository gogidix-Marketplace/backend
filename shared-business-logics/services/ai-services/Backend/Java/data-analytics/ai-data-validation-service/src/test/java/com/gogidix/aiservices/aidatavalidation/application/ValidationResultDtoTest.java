package com.gogidix.aiservices.aidatavalidation.application;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aidatavalidation.domain.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ValidationResultDtoTest {
    @Test
    void fromDomain() {
        var stats = new ValidationStatistics(100, 80, 15, 5);
        var result = ValidationResult.builder()
            .validationId("id")
            .valid(true)
            .errors(List.of(new ValidationError("f", "CODE", "msg")))
            .warnings(List.of(new ValidationWarning("f", "CODE", "msg")))
            .statistics(stats)
            .validatedAt(LocalDateTime.now())
            .build();
        var dto = ValidationResultDto.fromDomain(result);
        assertThat(dto.getValidationId()).isEqualTo("id");
        assertThat(dto.isValid()).isTrue();
        assertThat(dto.getErrors()).hasSize(1);
        assertThat(dto.getWarnings()).hasSize(1);
    }

    @Test
    void toResponseJson() {
        var stats = new ValidationStatistics(10, 8, 1, 1);
        var result = ValidationResult.builder()
            .validationId("id")
            .valid(false)
            .errors(Collections.emptyList())
            .warnings(Collections.emptyList())
            .statistics(stats)
            .validatedAt(LocalDateTime.now())
            .build();
        var dto = ValidationResultDto.fromDomain(result);
        var json = dto.toResponseJson();
        assertThat(json).containsKey("validationId");
        assertThat(json).containsKey("isValid");
    }

    @Test
    void errorDto() {
        var e = new ValidationResultDto.ValidationErrorDto("f", "CODE", "msg");
        assertThat(e.getField()).isEqualTo("f");
        assertThat(e.toMap()).containsKey("field");
    }

    @Test
    void warningDto() {
        var w = new ValidationResultDto.ValidationWarningDto("f", "CODE", "msg");
        assertThat(w.toMap()).containsKey("field");
    }

    @Test
    void statisticsDto() {
        var stats = new ValidationStatistics(10, 8, 1, 1);
        var dto = ValidationResultDto.ValidationStatisticsDto.fromDomain(stats);
        assertThat(dto.getTotalRecords()).isEqualTo(10);
        assertThat(dto.toMap()).containsKey("totalRecords");
    }
}
