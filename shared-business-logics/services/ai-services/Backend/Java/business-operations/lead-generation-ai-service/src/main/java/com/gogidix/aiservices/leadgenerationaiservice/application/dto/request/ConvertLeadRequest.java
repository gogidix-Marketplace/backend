package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertLeadRequest {
    @NotNull(message = "Lead ID is required")
    private String leadId;

    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    private Double value;

    private String currency;
}
