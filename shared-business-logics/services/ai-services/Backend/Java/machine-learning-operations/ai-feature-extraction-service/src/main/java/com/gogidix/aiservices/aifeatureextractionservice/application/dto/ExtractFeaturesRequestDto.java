package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request DTO for extracting features.
 */
public record ExtractFeaturesRequestDto(
        @NotBlank(message = "dataSource is required")
        String dataSource,

        @NotEmpty(message = "At least one feature name is required")
        @Size(max = 1000, message = "Max 1000 features allowed")
        List<String> features,

        @NotEmpty(message = "At least one extraction method is required")
        List<ExtractionMethod> methods,

        boolean normalize
) {
}
