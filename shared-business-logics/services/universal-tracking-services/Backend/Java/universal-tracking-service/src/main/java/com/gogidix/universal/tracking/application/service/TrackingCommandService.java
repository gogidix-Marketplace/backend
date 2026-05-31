package com.gogidix.universal.tracking.application.service;

import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.messaging.event.EventPublisher;
import com.gogidix.shared.security.context.RequestContext;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.application.mapper.TrackingMapper;
import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import com.gogidix.universal.tracking.domain.model.TrackingSession;
import com.gogidix.universal.tracking.domain.port.in.CreateEventCommand;
import com.gogidix.universal.tracking.domain.port.in.CreateSessionCommand;
import com.gogidix.universal.tracking.domain.port.in.UpdateSessionCommand;
import com.gogidix.universal.tracking.domain.port.out.TrackingEventRepositoryPort;
import com.gogidix.universal.tracking.domain.port.out.TrackingSessionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * CQRS Command Handler for Tracking operations.
 * Handles all write operations for events and sessions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingCommandService {

    private final TrackingSessionRepositoryPort sessionRepository;
    private final TrackingEventRepositoryPort eventRepository;
    private final EventPublisher eventPublisher;
    private final AuditService auditService;
    private final TrackingMapper trackingMapper;

    /**
     * Create a new tracking session
     */
    @Transactional
    public TrackingSessionResponseDto createSession(CreateSessionCommand command) {
        log.info("Creating session: sessionId={}", command.getSessionId());

        // Check if session already exists
        if (sessionRepository.existsBySessionId(command.getSessionId())) {
            throw new ConflictException("Session with ID already exists: " + command.getSessionId());
        }

        // Get tenant ID from request context
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        // Create session
        TrackingSession session = TrackingSession.builder()
            .sessionId(command.getSessionId())
            .userId(command.getUserId())
            .tenantId(tenantId)
            .source(command.getSource() != null ? command.getSource() : "WEB")
            .ipAddress(command.getIpAddress())
            .userAgent(command.getUserAgent())
            .deviceType(command.getDeviceType())
            .browser(command.getBrowser())
            .os(command.getOs())
            .country(command.getCountry())
            .city(command.getCity())
            .referrer(command.getReferrer())
            .landingPage(command.getLandingPage())
            .campaign(command.getCampaign())
            .startedAt(LocalDateTime.now())
            .lastActivityAt(LocalDateTime.now())
            .eventCount(0)
            .pageViewCount(0)
            .isActive(true)
            .metadata(trackingMapper instanceof TrackingMapper ? null : null) // Will be set by mapper if needed
            .build();

        // Save session
        TrackingSession savedSession = sessionRepository.save(session);

        // Publish session created event
        publishSessionEvent(savedSession, "SESSION_CREATED");

        // Audit log
        auditService.audit(
            "SESSION_CREATED",
            "TrackingSession",
            savedSession.getId().toString(),
            "Created tracking session: " + savedSession.getSessionId()
        );

        log.info("Session created successfully: id={}, sessionId={}", savedSession.getId(), savedSession.getSessionId());

        return trackingMapper.toResponseDto(savedSession);
    }

    /**
     * Create a new tracking event
     */
    @Transactional
    public TrackingEventResponseDto createEvent(CreateEventCommand command) {
        log.info("Creating event: type={}, sessionId={}", command.getEventType(), command.getSessionId());

        // Get tenant ID from request context
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        // Create event
        TrackingEvent event = TrackingEvent.builder()
            .eventType(command.getEventType())
            .sessionId(command.getSessionId())
            .userId(command.getUserId())
            .source(command.getSource() != null ? command.getSource() : "WEB")
            .timestamp(command.getTimestamp())
            .eventName(command.getEventName())
            .description(command.getDescription())
            .properties(null) // Will be set by mapper
            .metadata(null) // Will be set by mapper
            .ipAddress(command.getIpAddress())
            .userAgent(command.getUserAgent())
            .referrer(command.getReferrer())
            .pageUrl(command.getPageUrl())
            .pageTitle(command.getPageTitle())
            .tenantId(tenantId)
            .correlationId(command.getCorrelationId())
            .priority(command.getPriority() != null ? command.getPriority() : 5)
            .processed(false)
            .build();

        // Save event
        TrackingEvent savedEvent = eventRepository.save(event);

        // Update session if sessionId is provided
        if (savedEvent.getSessionId() != null) {
            sessionRepository.findBySessionId(savedEvent.getSessionId()).ifPresent(session -> {
                session.updateActivity();
                session.incrementEventCount();

                // Increment page view count if this is a page view event
                if ("PAGE_VIEW".equalsIgnoreCase(savedEvent.getEventType())) {
                    session.incrementPageViewCount();
                }

                sessionRepository.save(session);
            });
        }

        // Publish event created event
        publishTrackingEvent(savedEvent, "EVENT_CREATED");

        // Audit log
        auditService.audit(
            "EVENT_CREATED",
            "TrackingEvent",
            savedEvent.getId().toString(),
            "Created tracking event: " + savedEvent.getEventType()
        );

        log.info("Event created successfully: id={}, type={}", savedEvent.getId(), savedEvent.getEventType());

        return trackingMapper.toResponseDto(savedEvent);
    }

    /**
     * Update a tracking session
     */
    @Transactional
    public TrackingSessionResponseDto updateSession(UpdateSessionCommand command) {
        log.info("Updating session: sessionId={}", command.getSessionId());

        // Find session
        TrackingSession session = sessionRepository.findBySessionId(command.getSessionId())
            .orElseThrow(() -> new NotFoundException("Session not found: " + command.getSessionId()));

        // Validate tenant access
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));
        if (!session.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Session belongs to different tenant");
        }

        // Update session fields
        if (command.getReferrer() != null) {
            session.setReferrer(command.getReferrer());
        }
        if (command.getLandingPage() != null) {
            session.setLandingPage(command.getLandingPage());
        }
        if (command.getCampaign() != null) {
            session.setCampaign(command.getCampaign());
        }

        // End session if requested
        if (command.getEndSession() != null && command.getEndSession()) {
            session.endSession();
        } else {
            session.updateActivity();
        }

        // Save session
        TrackingSession savedSession = sessionRepository.save(session);

        // Publish session updated event
        publishSessionEvent(savedSession, "SESSION_UPDATED");

        // Audit log
        auditService.audit(
            "SESSION_UPDATED",
            "TrackingSession",
            savedSession.getId().toString(),
            "Updated tracking session: " + savedSession.getSessionId()
        );

        log.info("Session updated successfully: id={}, sessionId={}", savedSession.getId(), savedSession.getSessionId());

        return trackingMapper.toResponseDto(savedSession);
    }

    /**
     * Publish tracking event to Kafka
     */
    private void publishTrackingEvent(TrackingEvent event, String eventType) {
        try {
            Map<String, Object> payload = Map.of(
                "eventId", event.getId().toString(),
                "eventType", event.getEventType(),
                "sessionId", event.getSessionId() != null ? event.getSessionId() : "",
                "userId", event.getUserId() != null ? event.getUserId() : "",
                "source", event.getSource(),
                "tenantId", event.getTenantId(),
                "eventAction", eventType,
                "timestamp", event.getTimestamp().toString(),
                "correlationId", event.getCorrelationId() != null ? event.getCorrelationId() : ""
            );

            eventPublisher.publish("tracking-events", payload);
            log.debug("Published tracking event: type={}, eventId={}", eventType, event.getId());
        } catch (Exception e) {
            log.error("Failed to publish tracking event: eventId={}, eventType={}", event.getId(), eventType, e);
        }
    }

    /**
     * Publish session event to Kafka
     */
    private void publishSessionEvent(TrackingSession session, String eventType) {
        try {
            Map<String, Object> payload = Map.of(
                "sessionId", session.getId().toString(),
                "sessionIdentifier", session.getSessionId(),
                "userId", session.getUserId() != null ? session.getUserId() : "",
                "source", session.getSource(),
                "tenantId", session.getTenantId(),
                "eventAction", eventType,
                "isActive", session.isActiveSession(),
                "timestamp", LocalDateTime.now().toString()
            );

            eventPublisher.publish("tracking-sessions", payload);
            log.debug("Published session event: type={}, sessionId={}", eventType, session.getId());
        } catch (Exception e) {
            log.error("Failed to publish session event: sessionId={}, eventType={}", session.getId(), eventType, e);
        }
    }
}
