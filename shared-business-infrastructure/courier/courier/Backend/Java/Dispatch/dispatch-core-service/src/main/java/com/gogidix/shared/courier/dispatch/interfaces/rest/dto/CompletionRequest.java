package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for completing a dispatch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionRequest {

    @NotNull(message = "Actual distance is required")
    @PositiveOrZero(message = "Actual distance must be positive or zero")
    private Double actualDistanceMeters;

    @NotNull(message = "Actual duration is required")
    @PositiveOrZero(message = "Actual duration must be positive or zero")
    private Long actualDurationMinutes;

    private String deliveryNotes;

    private String signature;
}
