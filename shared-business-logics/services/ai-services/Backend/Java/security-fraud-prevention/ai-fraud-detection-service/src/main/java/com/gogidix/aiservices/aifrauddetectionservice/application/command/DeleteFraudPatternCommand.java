package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.DeletionResultDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * CQRS Command for deleting a fraud pattern.
 * Used to remove obsolete or invalid fraud patterns.
 */
@Getter
@Builder
@AllArgsConstructor
public class DeleteFraudPatternCommand implements Command<DeletionResultDTO> {

    @NotBlank(message = "Pattern ID is required")
    private final String patternId;

    @NotBlank(message = "Reason for deletion is required")
    private final String reason;

    @NotBlank(message = "Deleted by is required")
    private final String deletedBy;
}
