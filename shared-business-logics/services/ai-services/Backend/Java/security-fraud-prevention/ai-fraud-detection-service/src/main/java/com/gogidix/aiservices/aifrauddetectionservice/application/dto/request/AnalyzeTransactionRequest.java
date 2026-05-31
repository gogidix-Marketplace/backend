package com.gogidix.aiservices.aifrauddetectionservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class AnalyzeTransactionRequest {
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Merchant is required")
    private String merchant;

    private Instant timestamp;

    private String currency;

    private Map<String, Object> metadata;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AnalyzeTransactionRequest request;

        public Builder() {
            this.request = new AnalyzeTransactionRequest();
        }

        public Builder transactionId(String transactionId) {
            request.transactionId = transactionId;
            return this;
        }

        public Builder userId(String userId) {
            request.userId = userId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            request.amount = amount;
            return this;
        }

        public Builder merchant(String merchant) {
            request.merchant = merchant;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            request.timestamp = timestamp;
            return this;
        }

        public Builder currency(String currency) {
            request.currency = currency;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            request.metadata = metadata;
            return this;
        }

        public AnalyzeTransactionRequest build() {
            return request;
        }
    }
}
