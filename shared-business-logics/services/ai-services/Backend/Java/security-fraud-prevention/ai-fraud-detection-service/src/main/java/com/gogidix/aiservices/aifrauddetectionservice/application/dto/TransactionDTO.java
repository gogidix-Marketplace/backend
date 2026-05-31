package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * DTO for Transaction information.
 * Contains transaction details for fraud analysis.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TransactionDTO extends BaseDTO {

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String tenantId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Merchant is required")
    private String merchant;

    private Instant timestamp;

    private RiskLevel riskLevel;

    private String status;

    private Boolean isSuspicious;

    private String flaggedReason;

    private String flaggedBy;

    private Instant flaggedAt;

    private String fraudAnalysisId;

    private Map<String, Object> metadata;
}
