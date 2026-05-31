package com.gogidix.aiservices.aidatavalidation.application;

import com.gogidix.aiservices.aidatavalidation.domain.*;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DTO for validation results.
 */
@Value
public class ValidationResultDto {

    String validationId;
    boolean isValid;
    List<ValidationErrorDto> errors;
    List<ValidationWarningDto> warnings;
    ValidationStatisticsDto statistics;
    LocalDateTime validatedAt;

    public static ValidationResultDto fromDomain(ValidationResult result) {
        return new ValidationResultDto(
            result.getValidationId(),
            result.isValid(),
            result.getErrors().stream()
                .map(e -> new ValidationErrorDto(e.getField(), e.getCode(), e.getMessage()))
                .collect(Collectors.toList()),
            result.getWarnings().stream()
                .map(w -> new ValidationWarningDto(w.getField(), w.getCode(), w.getMessage()))
                .collect(Collectors.toList()),
            ValidationStatisticsDto.fromDomain(result.getStatistics()),
            result.getValidatedAt()
        );
    }

    public Map<String, Object> toResponseJson() {
        return Map.of(
            "validationId", validationId,
            "isValid", isValid,
            "errors", errors.stream().map(ValidationErrorDto::toMap).collect(Collectors.toList()),
            "warnings", warnings.stream().map(ValidationWarningDto::toMap).collect(Collectors.toList()),
            "statistics", statistics.toMap(),
            "validatedAt", validatedAt.toString()
        );
    }

    @Value
    public static class ValidationErrorDto {
        String field;
        String code;
        String message;

        Map<String, Object> toMap() {
            return Map.of("field", field, "code", code, "message", message);
        }
    }

    @Value
    public static class ValidationWarningDto {
        String field;
        String code;
        String message;

        Map<String, Object> toMap() {
            return Map.of("field", field, "code", code, "message", message);
        }
    }

    @Value
    public static class ValidationStatisticsDto {
        int totalRecords;
        int validRecords;
        int invalidRecords;
        int skippedRecords;
        double validityRate;

        public static ValidationStatisticsDto fromDomain(ValidationStatistics statistics) {
            return new ValidationStatisticsDto(
                statistics.getTotalRecords(),
                statistics.getValidRecords(),
                statistics.getInvalidRecords(),
                statistics.getSkippedRecords(),
                statistics.getValidityRate()
            );
        }

        Map<String, Object> toMap() {
            return Map.of(
                "totalRecords", totalRecords,
                "validRecords", validRecords,
                "invalidRecords", invalidRecords,
                "skippedRecords", skippedRecords,
                "validityRate", validityRate
            );
        }
    }
}
