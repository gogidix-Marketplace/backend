package com.gogidix.aiservices.aiuserprofilingservice.shared.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.aiservices.aiuserprofilingservice.shared.context.RequestContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Standard error response structure for all API errors.
 * Used by GlobalExceptionHandler to return consistent error responses.
 */
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private final Instant timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;

    private final String correlationId;

    private final String tenantId;

    private final List<String> details;

    private ErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.path = builder.path;
        this.correlationId = builder.correlationId != null ? builder.correlationId :
                RequestContextHolder.getCorrelationId();
        this.tenantId = builder.tenantId != null ? builder.tenantId :
                RequestContextHolder.getTenantId();
        this.details = builder.details;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder fromException(Exception ex, String path, int status) {
        String error = "INTERNAL_SERVER_ERROR";
        String message = ex.getMessage();

        if (ex instanceof BaseDomainException) {
            BaseDomainException domainEx = (BaseDomainException) ex;
            error = domainEx.getErrorCode();
        }

        return builder()
                .status(status)
                .error(error)
                .message(message != null ? message : "An unexpected error occurred")
                .path(path);
    }

    // Getters
    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public List<String> getDetails() {
        return details;
    }

    public static class Builder {
        private Instant timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        private String correlationId;
        private String tenantId;
        private List<String> details;

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder details(List<String> details) {
            this.details = details;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
