package com.gogidix.shared.infrastructure.services.observability.audit.application.dto.request;

import com.gogidix.shared.infrastructure.services.observability.audit.domain.model.AuditLog;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Create audit log request DTO.
 */
public record CreateAuditLogRequestDto(
        String userId,
        String username,
        @NotNull AuditLog.AuditAction action,
        AuditLog.AuditEntityType entityType,
        String entityId,
        String entityName,
        String description,
        String ipAddress,
        String userAgent,
        boolean success,
        String errorMessage,
        Map<String, Object> changes,
        Map<String, Object> metadata
) {
    public CreateAuditLogRequestDto {
        if (success == false && errorMessage == null) {
            errorMessage = "Operation failed";
        }
    }

    public static CreateAuditLogRequestDtoBuilder builder() {
        return new CreateAuditLogRequestDtoBuilder();
    }

    public static class CreateAuditLogRequestDtoBuilder {
        private String userId;
        private String username;
        private AuditLog.AuditAction action;
        private AuditLog.AuditEntityType entityType;
        private String entityId;
        private String entityName;
        private String description;
        private String ipAddress;
        private String userAgent;
        private boolean success = true;
        private String errorMessage;
        private Map<String, Object> changes;
        private Map<String, Object> metadata;

        public CreateAuditLogRequestDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder action(AuditLog.AuditAction action) {
            this.action = action;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder entityType(AuditLog.AuditEntityType entityType) {
            this.entityType = entityType;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder entityId(String entityId) {
            this.entityId = entityId;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder entityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder changes(Map<String, Object> changes) {
            this.changes = changes;
            return this;
        }

        public CreateAuditLogRequestDtoBuilder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public CreateAuditLogRequestDto build() {
            return new CreateAuditLogRequestDto(
                userId, username, action, entityType, entityId, entityName,
                description, ipAddress, userAgent, success, errorMessage,
                changes, metadata
            );
        }
    }
}
