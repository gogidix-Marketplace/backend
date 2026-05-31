package com.gogidix.aiservices.aifrauddetectionservice.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for fraud analysis requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAnalysisRequestDTO {

    @JsonProperty("transactionId")
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @JsonProperty("currency")
    @NotBlank(message = "Currency is required")
    private String currency;

    @JsonProperty("merchantId")
    @NotBlank(message = "Merchant ID is required")
    private String merchantId;

    @JsonProperty("userId")
    @NotBlank(message = "User ID is required")
    private String userId;

    @JsonProperty("transactionDate")
    private LocalDateTime transactionDate;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;
}
