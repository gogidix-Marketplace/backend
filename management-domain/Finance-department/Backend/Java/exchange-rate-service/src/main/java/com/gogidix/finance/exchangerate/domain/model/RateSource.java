package com.gogidix.finance.exchangerate.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity - Rate Source
 * Represents external sources for exchange rates
 * Multi-tenant SaaS entity with mandatory tenantId field
 */
@Document(collection = "rate_sources")
public class RateSource extends BaseEntity {

    @Indexed
    private String sourceCode;

    private String sourceName;

    private String description;

    private String baseUrl;

    private String apiKey;

    private SourceType sourceType;

    private Integer priority;

    private boolean active;

    private Integer timeoutSeconds;

    private Integer retryAttempts;

    private List<String> supportedCurrencies;

    private Instant lastSuccessfulFetch;

    private Instant lastFailedFetch;

    private String lastErrorMessage;

    public enum SourceType {
        REST_API,
        FIX_PROTOCOL,
        FILE_FEED,
        WEB_SOCKET
    }

    protected RateSource() {
    }

    private RateSource(Builder builder) {
        super(builder.tenantId);
        this.sourceCode = Objects.requireNonNull(builder.sourceCode, "sourceCode is required");
        this.sourceName = Objects.requireNonNull(builder.sourceName, "sourceName is required");
        this.baseUrl = builder.baseUrl;
        this.apiKey = builder.apiKey;
        this.sourceType = builder.sourceType != null ? builder.sourceType : SourceType.REST_API;
        this.priority = builder.priority != null ? builder.priority : 100;
        this.active = builder.active;
        this.timeoutSeconds = builder.timeoutSeconds != null ? builder.timeoutSeconds : 30;
        this.retryAttempts = builder.retryAttempts != null ? builder.retryAttempts : 3;
        this.supportedCurrencies = builder.supportedCurrencies;
    }

    public void updateLastSuccessfulFetch() {
        this.lastSuccessfulFetch = Instant.now();
        this.lastErrorMessage = null;
        this.updateTimestamp();
    }

    public void updateLastFailedFetch(String errorMessage) {
        this.lastFailedFetch = Instant.now();
        this.lastErrorMessage = errorMessage;
        this.updateTimestamp();
    }

    public boolean isAvailable() {
        return active && (lastFailedFetch == null ||
               Instant.now().minusSeconds(300).isAfter(lastFailedFetch));
    }

    public void activate() {
        this.active = true;
        this.updateTimestamp();
    }

    public void deactivate() {
        this.active = false;
        this.updateTimestamp();
    }

    public void setPriority(Integer priority) {
        this.priority = Objects.requireNonNull(priority, "priority is required");
        this.updateTimestamp();
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDescription() {
        return description;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public Integer getPriority() {
        return priority;
    }

    public boolean isActive() {
        return active;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    public List<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    public Instant getLastSuccessfulFetch() {
        return lastSuccessfulFetch;
    }

    public Instant getLastFailedFetch() {
        return lastFailedFetch;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String tenantId;
        private String sourceCode;
        private String sourceName;
        private String description;
        private String baseUrl;
        private String apiKey;
        private SourceType sourceType;
        private Integer priority;
        private boolean active = true;
        private Integer timeoutSeconds;
        private Integer retryAttempts;
        private List<String> supportedCurrencies;

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder sourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
            return this;
        }

        public Builder sourceName(String sourceName) {
            this.sourceName = sourceName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder sourceType(SourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder timeoutSeconds(Integer timeoutSeconds) {
            this.timeoutSeconds = timeoutSeconds;
            return this;
        }

        public Builder retryAttempts(Integer retryAttempts) {
            this.retryAttempts = retryAttempts;
            return this;
        }

        public Builder supportedCurrencies(List<String> supportedCurrencies) {
            this.supportedCurrencies = supportedCurrencies;
            return this;
        }

        public RateSource build() {
            return new RateSource(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RateSource that = (RateSource) o;
        return Objects.equals(sourceCode, that.sourceCode) &&
               Objects.equals(tenantId, that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceCode, tenantId);
    }
}
