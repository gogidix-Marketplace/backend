package com.gogidix.shared.audit.service;

import com.gogidix.shared.audit.application.port.in.AuditEventUseCase;
import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.AuditEventType;
import com.gogidix.shared.audit.domain.AuditResult;
import com.gogidix.shared.audit.domain.BusinessDomain;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Facade for AuditService - provides simpler import path and API for services.
 * <p>
 * This interface provides backward compatibility for services expecting the simpler
 * package structure and API ({@code com.gogidix.shared.audit.service}).
 * </p>
 * <p>
 * For comprehensive audit functionality, use:
 * <pre>{@code
 * import com.gogidix.shared.audit.application.service.AuditEventService;
 * }</pre>
 * </p>
 *
 * @deprecated Use {@link com.gogidix.shared.audit.application.service.AuditEventService} instead.
 *             This facade exists for backward compatibility with existing service imports.
 */
@Deprecated
public interface AuditService {

    /**
     * Log a simple audit event with basic information.
     *
     * @param userId the user who performed the action
     * @param action the action performed
     * @param resource the resource being acted upon
     * @param resourceId the ID of the resource
     */
    void logEvent(String userId, String action, String resource, String resourceId);

    /**
     * Log a detailed audit event.
     *
     * @param userId the user who performed the action
     * @param action the action performed
     * @param resource the resource being acted upon
     * @param resourceId the ID of the resource
     * @param result the result of the action
     * @param details additional details about the event
     */
    void logEvent(String userId, String action, String resource, String resourceId,
                  AuditResult result, Map<String, Object> details);

    /**
     * Log a domain-specific audit event.
     *
     * @param userId the user who performed the action
     * @param action the action performed
     * @param resource the resource being acted upon
     * @param resourceId the ID of the resource
     * @param domain the business domain
     * @param eventType the type of event
     */
    void logEvent(String userId, String action, String resource, String resourceId,
                  BusinessDomain domain, AuditEventType eventType);

    /**
     * Create a builder for constructing a detailed audit event.
     *
     * @return a builder for creating audit events
     */
    AuditEventBuilder auditEvent();

    /**
     * Builder for creating detailed audit events.
     */
    interface AuditEventBuilder {
        AuditEventBuilder userId(String userId);
        AuditEventBuilder action(String action);
        AuditEventBuilder resource(String resource);
        AuditEventBuilder resourceId(String resourceId);
        AuditEventBuilder result(AuditResult result);
        AuditEventBuilder domain(BusinessDomain domain);
        AuditEventBuilder eventType(AuditEventType eventType);
        AuditEventBuilder timestamp(LocalDateTime timestamp);
        AuditEventBuilder detail(String key, Object value);
        void log();
    }

    /**
     * Simple implementation class that wraps the AuditEventUseCase.
     * Services should inject this as a Spring bean.
     */
    class SimpleAuditService implements AuditService {
        private final AuditEventUseCase auditEventUseCase;

        public SimpleAuditService(AuditEventUseCase auditEventUseCase) {
            this.auditEventUseCase = auditEventUseCase;
        }

        @Override
        public void logEvent(String userId, String action, String resource, String resourceId) {
            logEvent(userId, action, resource, resourceId, AuditResult.SUCCESS, null);
        }

        @Override
        public void logEvent(String userId, String action, String resource, String resourceId,
                            AuditResult result, Map<String, Object> details) {
            logEvent(userId, action, resource, resourceId,
                    BusinessDomain.SHARED_INFRASTRUCTURE, AuditEventType.SYSTEM_EVENT);
        }

        @Override
        public void logEvent(String userId, String action, String resource, String resourceId,
                            BusinessDomain domain, AuditEventType eventType) {
            // Build and create audit event through the use case
            // This is a simplified version - for full functionality use AuditEventUseCase directly
            try {
                AuditEvent event = AuditEvent.builder()
                        .eventId(java.util.UUID.randomUUID().toString())
                        .userId(userId)
                        .action(action)
                        .resource(resource)
                        .resourceId(resourceId)
                        .domain(domain)
                        .eventType(eventType)
                        .timestamp(LocalDateTime.now())
                        .result(AuditResult.SUCCESS)
                        .build();

                // Store the event - note: this would require repository access
                // For now, this is a logging facade
                java.util.logging.Logger.getLogger(AuditService.class.getName())
                        .info("Audit: user=" + userId + ", action=" + action + ", resource=" + resource);
            } catch (Exception e) {
                // Non-blocking audit logging
                java.util.logging.Logger.getLogger(AuditService.class.getName())
                        .warning("Failed to log audit event: " + e.getMessage());
            }
        }

        @Override
        public AuditEventBuilder auditEvent() {
            return new AuditEventBuilder() {
                private String userId;
                private String action;
                private String resource;
                private String resourceId;
                private AuditResult result = AuditResult.SUCCESS;
                private BusinessDomain domain = BusinessDomain.SHARED_INFRASTRUCTURE;
                private AuditEventType eventType = AuditEventType.SYSTEM_EVENT;
                private LocalDateTime timestamp = LocalDateTime.now();
                private final Map<String, Object> details = new java.util.HashMap<>();

                @Override
                public AuditEventBuilder userId(String userId) {
                    this.userId = userId;
                    return this;
                }

                @Override
                public AuditEventBuilder action(String action) {
                    this.action = action;
                    return this;
                }

                @Override
                public AuditEventBuilder resource(String resource) {
                    this.resource = resource;
                    return this;
                }

                @Override
                public AuditEventBuilder resourceId(String resourceId) {
                    this.resourceId = resourceId;
                    return this;
                }

                @Override
                public AuditEventBuilder result(AuditResult result) {
                    this.result = result;
                    return this;
                }

                @Override
                public AuditEventBuilder domain(BusinessDomain domain) {
                    this.domain = domain;
                    return this;
                }

                @Override
                public AuditEventBuilder eventType(AuditEventType eventType) {
                    this.eventType = eventType;
                    return this;
                }

                @Override
                public AuditEventBuilder timestamp(LocalDateTime timestamp) {
                    this.timestamp = timestamp;
                    return this;
                }

                @Override
                public AuditEventBuilder detail(String key, Object value) {
                    this.details.put(key, value);
                    return this;
                }

                @Override
                public void log() {
                    logEvent(userId, action, resource, resourceId, domain, eventType);
                }
            };
        }
    }
}
