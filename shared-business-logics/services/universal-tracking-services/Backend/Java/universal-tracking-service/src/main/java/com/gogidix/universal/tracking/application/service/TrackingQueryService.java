package com.gogidix.universal.tracking.application.service;

import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import com.gogidix.universal.tracking.application.dto.response.PagedResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingMetricResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.application.mapper.TrackingMapper;
import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import com.gogidix.universal.tracking.domain.model.TrackingMetric;
import com.gogidix.universal.tracking.domain.model.TrackingSession;
import com.gogidix.universal.tracking.domain.port.in.GetEventsQuery;
import com.gogidix.universal.tracking.domain.port.in.GetMetricsQuery;
import com.gogidix.universal.tracking.domain.port.in.GetSessionQuery;
import com.gogidix.universal.tracking.domain.port.out.TrackingEventRepositoryPort;
import com.gogidix.universal.tracking.domain.port.out.TrackingMetricRepositoryPort;
import com.gogidix.universal.tracking.domain.port.out.TrackingSessionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * CQRS Query Handler for Tracking operations.
 * Handles all read operations for events, sessions, and metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrackingQueryService {

    private final TrackingSessionRepositoryPort sessionRepository;
    private final TrackingEventRepositoryPort eventRepository;
    private final TrackingMetricRepositoryPort metricRepository;
    private final TrackingMapper trackingMapper;

    /**
     * Get session by ID
     */
    @Transactional(readOnly = true)
    public TrackingSessionResponseDto getSession(GetSessionQuery query) {
        log.info("Getting session: sessionId={}", query.getSessionId());

        TrackingSession session = sessionRepository.findBySessionId(query.getSessionId())
            .orElseThrow(() -> new NotFoundException("Session not found: " + query.getSessionId()));

        // Validate tenant access
        validateTenantAccess(session.getTenantId());

        return trackingMapper.toResponseDto(session);
    }

    /**
     * Get session by UUID
     */
    @Transactional(readOnly = true)
    public TrackingSessionResponseDto getSessionById(String sessionId) {
        log.info("Getting session by ID: id={}", sessionId);

        UUID id = UUID.fromString(sessionId);
        TrackingSession session = sessionRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Session not found: " + sessionId));

        // Validate tenant access
        validateTenantAccess(session.getTenantId());

        return trackingMapper.toResponseDto(session);
    }

    /**
     * Get event by ID
     */
    @Transactional(readOnly = true)
    public TrackingEventResponseDto getEvent(String eventId) {
        log.info("Getting event: id={}", eventId);

        UUID id = UUID.fromString(eventId);
        TrackingEvent event = eventRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        // Validate tenant access
        validateTenantAccess(event.getTenantId());

        return trackingMapper.toResponseDto(event);
    }

    /**
     * Search events with filters
     */
    @Transactional(readOnly = true)
    public PagedResponseDto<TrackingEventResponseDto> searchEvents(GetEventsQuery query) {
        log.info("Searching events: tenantId={}, type={}, page={}",
            query.getTenantId(), query.getEventType(), query.getPage());

        String tenantId = query.getTenantId() != null ? query.getTenantId()
            : RequestContext.getTenantId().orElse(null);

        // Create pageable
        Pageable pageable = PageRequest.of(
            query.getPage() != null ? query.getPage() : 0,
            query.getSize() != null ? query.getSize() : 20,
            Sort.by(
                query.getSortDirection() != null && query.getSortDirection().equalsIgnoreCase("ASC")
                    ? Sort.Direction.ASC : Sort.Direction.DESC,
                query.getSortBy() != null ? query.getSortBy() : "timestamp"
            )
        );

        // Search events
        Page<TrackingEvent> events = eventRepository.findByFilters(
            tenantId,
            query.getEventType(),
            query.getSessionId(),
            query.getUserId(),
            query.getSource(),
            query.getStartDate(),
            query.getEndDate(),
            query.getProcessed(),
            pageable
        );

        return trackingMapper.toPagedResponse(
            events.map(trackingMapper::toResponseDto)
        );
    }

    /**
     * Get metrics with filters
     */
    @Transactional(readOnly = true)
    public PagedResponseDto<TrackingMetricResponseDto> searchMetrics(GetMetricsQuery query) {
        log.info("Searching metrics: tenantId={}, name={}, page={}",
            query.getTenantId(), query.getMetricName(), query.getPage());

        String tenantId = query.getTenantId() != null ? query.getTenantId()
            : RequestContext.getTenantId().orElse(null);

        // Create pageable
        Pageable pageable = PageRequest.of(
            query.getPage() != null ? query.getPage() : 0,
            query.getSize() != null ? query.getSize() : 20,
            Sort.by(Sort.Direction.DESC, "metricDate", "lastUpdatedAt")
        );

        // Search metrics
        Page<TrackingMetric> metrics = metricRepository.findByFilters(
            tenantId,
            query.getMetricName(),
            query.getMetricType(),
            query.getEventType(),
            query.getSource(),
            query.getMetricDate(),
            null,
            pageable
        );

        return trackingMapper.toPagedResponse(
            metrics.map(trackingMapper::toResponseDto)
        );
    }

    /**
     * Get events by session ID
     */
    @Transactional(readOnly = true)
    public List<TrackingEventResponseDto> getEventsBySessionId(String sessionId) {
        log.info("Getting events for session: sessionId={}", sessionId);

        List<TrackingEvent> events = eventRepository.findBySessionId(sessionId);

        // Validate tenant access for first event if exists
        if (!events.isEmpty()) {
            validateTenantAccess(events.get(0).getTenantId());
        }

        return trackingMapper.toEventResponseDtoList(events);
    }

    /**
     * Count events by type and tenant
     */
    @Transactional(readOnly = true)
    public Long countEventsByType(String eventType) {
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        return eventRepository.countByEventTypeAndTenantId(eventType, tenantId);
    }

    /**
     * Count events by date range and tenant
     */
    @Transactional(readOnly = true)
    public Long countEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        return eventRepository.countByTimestampBetweenAndTenantId(startDate, endDate, tenantId);
    }

    /**
     * Get active sessions count for tenant
     */
    @Transactional(readOnly = true)
    public Long countActiveSessions() {
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        return sessionRepository.countByTenantIdAndIsActive(tenantId, true);
    }

    /**
     * Validate tenant access
     */
    private void validateTenantAccess(String resourceTenantId) {
        String tenantId = RequestContext.getTenantId()
            .orElseThrow(() -> new ValidationException("Tenant ID not found in request context"));

        if (!resourceTenantId.equals(tenantId)) {
            throw new ValidationException("Access denied: Resource belongs to different tenant");
        }
    }
}
