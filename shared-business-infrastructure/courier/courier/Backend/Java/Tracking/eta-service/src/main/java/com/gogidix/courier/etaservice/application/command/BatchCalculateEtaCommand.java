package com.gogidix.courier.etaservice.application.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Command to batch calculate ETA for multiple dispatches.
 */
public record BatchCalculateEtaCommand(

        @NotNull(message = "Requests list is required")
        @NotEmpty(message = "At least one request is required")
        @Valid
        List<CalculateEtaCommand> requests

) {
    public BatchCalculateEtaCommand {
        if (requests.size() > 100) {
            throw new IllegalArgumentException("Maximum 100 requests allowed per batch");
        }
    }
}
