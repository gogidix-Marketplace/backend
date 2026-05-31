package com.gogidix.transaction.audit.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Command interface for creating audit log entries.
 */
public interface CreateAuditLogCommand {

    String getTenantId();

    String getEntityType();

    String getEntityId();

    String getAction();

    String getActorId();

    String getActorType();

    String getIpAddress();

    String getUserAgent();

    String getCorrelationId();

    String getOldState();

    String getNewState();

    String getChangedFields();

    String getMetadata();

    String getBusinessContext();

    String getSeverity();

    String getCategory();

    String getDescription();

    String getStatus();

    String getErrorMessage();

    String getSessionId();

    String getRequestId();

    @Builder
    @Data
    class CreateAuditLogCommandDto implements CreateAuditLogCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Entity type is required")
        private String entityType;

        @NotBlank(message = "Entity ID is required")
        private String entityId;

        @NotBlank(message = "Action is required")
        private String action;

        private String actorId;

        private String actorType;

        private String ipAddress;

        private String userAgent;

        private String correlationId;

        private String oldState;

        private String newState;

        private String changedFields;

        private String metadata;

        private String businessContext;

        private String severity;

        private String category;

        private String description;

        private String status;

        private String errorMessage;

        private String sessionId;

        private String requestId;
    }
}
