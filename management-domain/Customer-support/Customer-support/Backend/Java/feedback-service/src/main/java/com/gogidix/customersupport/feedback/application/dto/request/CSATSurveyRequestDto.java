package com.gogidix.customersupport.feedback.application.dto.request;

import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Request DTO for creating/updating CSAT surveys
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CSATSurveyRequestDto {

    @NotBlank(message = "Survey name is required")
    private String name;

    private String description;

    @NotNull(message = "Rating scale is required")
    @Min(value = 1, message = "Rating scale must be at least 1")
    private Integer ratingScale;

    @NotEmpty(message = "At least one question is required")
    @Valid
    private List<CSATSurvey.SurveyQuestion> questions;

    private List<String> triggerEvents;

    @Min(value = 0, message = "Trigger delay cannot be negative")
    private Integer triggerDelayHours;

    private String locale;

    private Instant activeFrom;

    private Instant activeUntil;

    @Min(value = 1, message = "Max responses must be at least 1")
    private Integer maxResponses;
}
