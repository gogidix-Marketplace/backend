package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.ActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddActivityRequest {
    @NotNull(message = "Lead ID is required")
    private String leadId;

    @NotNull(message = "Activity type is required")
    private ActivityType type;

    @NotBlank(message = "Description is required")
    private String description;

    private Instant timestamp;
    private String createdBy;
    private String notes;
}
