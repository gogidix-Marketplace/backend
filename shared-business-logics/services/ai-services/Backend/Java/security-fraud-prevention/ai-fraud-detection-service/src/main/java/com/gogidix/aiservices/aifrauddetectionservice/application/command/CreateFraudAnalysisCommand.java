package com.gogidix.aiservices.aifrauddetectionservice.application.command;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.FraudAnalysisDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * CQRS Command for creating a new fraud analysis.
 * Used to initiate fraud detection for a transaction.
 */
@Getter
@Builder
@AllArgsConstructor
public class CreateFraudAnalysisCommand implements Command<FraudAnalysisDTO> {

    @NotBlank(message = "Transaction ID is required")
    private final String transactionId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private final BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private final String currency;

    @NotBlank(message = "Merchant ID is required")
    private final String merchantId;

    private final Map<String, Object> metadata;
}
