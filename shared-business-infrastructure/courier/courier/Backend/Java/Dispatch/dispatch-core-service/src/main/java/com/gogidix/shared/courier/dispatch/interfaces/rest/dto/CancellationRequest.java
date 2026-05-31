package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for cancelling a dispatch
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRequest {

    @NotBlank(message = "Cancelled by is required")
    private String cancelledBy;

    private String cancellationReason;
}
