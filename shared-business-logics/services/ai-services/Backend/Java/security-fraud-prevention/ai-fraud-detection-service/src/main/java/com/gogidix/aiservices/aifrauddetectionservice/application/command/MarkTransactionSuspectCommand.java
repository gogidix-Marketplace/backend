package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.TransactionDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * CQRS Command for marking a transaction as suspicious.
 * Used to flag transactions for manual review or investigation.
 */
@Getter
@Builder
@AllArgsConstructor
public class MarkTransactionSuspectCommand implements Command<TransactionDTO> {

    @NotBlank(message = "Transaction ID is required")
    private final String transactionId;

    @NotBlank(message = "Reason for flagging is required")
    private final String reason;

    @NotBlank(message = "Flagged by is required")
    private final String flaggedBy;
}
