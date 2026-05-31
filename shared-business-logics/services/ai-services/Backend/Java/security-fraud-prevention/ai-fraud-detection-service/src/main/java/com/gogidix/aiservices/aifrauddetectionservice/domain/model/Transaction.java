package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class Transaction {
    private final String transactionId;
    private final String userId;
    private final String tenantId;
    private final BigDecimal amount;
    private final String merchant;
    private final Instant timestamp;
    private final String currency;
    private final Map<String, Object> metadata;

    private Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.userId = builder.userId;
        this.tenantId = builder.tenantId;
        this.amount = builder.amount;
        this.merchant = builder.merchant;
        this.timestamp = builder.timestamp;
        this.currency = builder.currency;
        this.metadata = builder.metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static class Builder {
        private String transactionId;
        private String userId;
        private String tenantId;
        private BigDecimal amount;
        private String merchant;
        private Instant timestamp;
        private String currency;
        private Map<String, Object> metadata;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder merchant(String merchant) {
            this.merchant = merchant;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
