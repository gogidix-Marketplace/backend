package com.gogidix.courier.assignmentservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request DTO for cancelling a driver assignment.
 */
@Schema(description = "Request DTO for cancelling an assignment")
public record CancelAssignmentRequest(

        @JsonProperty("cancellation_reason")
        @Schema(description = "Reason for cancellation", example = "Customer cancelled order")
        String cancellationReason
) {}
