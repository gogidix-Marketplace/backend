package com.gogidix.universal.tracking.interfaces.rest;

import com.gogidix.universal.tracking.application.dto.request.CreateEventRequestDto;
import com.gogidix.universal.tracking.application.dto.request.CreateSessionRequestDto;
import com.gogidix.universal.tracking.application.dto.request.UpdateSessionRequestDto;
import com.gogidix.universal.tracking.application.dto.response.PagedResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.application.mapper.TrackingMapper;
import com.gogidix.universal.tracking.application.service.TrackingCommandService;
import com.gogidix.universal.tracking.application.service.TrackingQueryService;
import com.gogidix.universal.tracking.domain.port.in.GetEventsQuery;
import com.gogidix.universal.tracking.domain.port.in.GetSessionQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Tracking operations.
 * Provides API endpoints for managing tracking events and sessions.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
@Tag(name = "Universal Tracking", description = "APIs for universal event tracking and analytics")
public class TrackingController {

    private final TrackingCommandService commandService;
    private final TrackingQueryService queryService;
    private final TrackingMapper trackingMapper;

    /**
     * Create a new tracking session
     */
    @PostMapping(value = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new tracking session",
        description = "Creates a new tracking session for grouping related events"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Session created successfully",
            headers = @Header(name = "X-Correlation-ID", description = "Correlation ID for tracking"),
            content = @Content(schema = @Schema(implementation = TrackingSessionResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "409", description = "Session with ID already exists")
    })
    public ResponseEntity<TrackingSessionResponseDto> createSession(
        @Parameter(description = "Session creation request", required = true)
        @Valid @RequestBody CreateSessionRequestDto request
    ) {
        log.info("POST /api/v1/tracking/sessions - Creating session: sessionId={}", request.getSessionId());

        var command = trackingMapper.toCommand(request);
        TrackingSessionResponseDto response = commandService.createSession(command);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/v1/tracking/sessions/" + response.getSessionId()))
            .body(response);
    }

    /**
     * Get session by ID
     */
    @GetMapping(value = "/sessions/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get session by ID",
        description = "Retrieves detailed information about a specific tracking session"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session found", content = @Content(schema = @Schema(implementation = TrackingSessionResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<TrackingSessionResponseDto> getSession(
        @Parameter(description = "Session identifier", required = true, example = "session-abc-123")
        @PathVariable String sessionId
    ) {
        log.info("GET /api/v1/tracking/sessions/{} - Getting session", sessionId);

        var query = GetSessionQuery.builder()
            .sessionId(sessionId)
            .build();

        TrackingSessionResponseDto response = queryService.getSession(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Update session
     */
    @PutMapping(value = "/sessions/{sessionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Update tracking session",
        description = "Updates a tracking session with new information or ends the session"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<TrackingSessionResponseDto> updateSession(
        @Parameter(description = "Session identifier", required = true)
        @PathVariable String sessionId,
        @Parameter(description = "Session update request", required = true)
        @Valid @RequestBody UpdateSessionRequestDto request
    ) {
        log.info("PUT /api/v1/tracking/sessions/{} - Updating session", sessionId);

        var command = com.gogidix.universal.tracking.domain.port.in.UpdateSessionCommand.builder()
            .sessionId(sessionId)
            .referrer(request.getReferrer())
            .landingPage(request.getLandingPage())
            .campaign(request.getCampaign())
            .endSession(request.getEndSession())
            .build();

        TrackingSessionResponseDto response = commandService.updateSession(command);
        return ResponseEntity.ok(response);
    }

    /**
     * Get events for a session
     */
    @GetMapping(value = "/sessions/{sessionId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get events for a session",
        description = "Retrieves all events associated with a specific session"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<List<TrackingEventResponseDto>> getSessionEvents(
        @Parameter(description = "Session identifier", required = true)
        @PathVariable String sessionId
    ) {
        log.info("GET /api/v1/tracking/sessions/{}/events - Getting session events", sessionId);

        List<TrackingEventResponseDto> response = queryService.getEventsBySessionId(sessionId);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new tracking event
     */
    @PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create a new tracking event",
        description = "Creates a new tracking event with the provided data"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Event created successfully",
            headers = @Header(name = "X-Correlation-ID", description = "Correlation ID for tracking"),
            content = @Content(schema = @Schema(implementation = TrackingEventResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<TrackingEventResponseDto> createEvent(
        @Parameter(description = "Event creation request", required = true)
        @Valid @RequestBody CreateEventRequestDto request
    ) {
        log.info("POST /api/v1/tracking/events - Creating event: type={}", request.getEventType());

        var command = trackingMapper.toCommand(request);
        TrackingEventResponseDto response = commandService.createEvent(command);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .location(URI.create("/api/v1/tracking/events/" + response.getId()))
            .body(response);
    }

    /**
     * Get event by ID
     */
    @GetMapping(value = "/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get event by ID",
        description = "Retrieves detailed information about a specific tracking event"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event found", content = @Content(schema = @Schema(implementation = TrackingEventResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<TrackingEventResponseDto> getEvent(
        @Parameter(description = "Event ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
        @PathVariable String eventId
    ) {
        log.info("GET /api/v1/tracking/events/{} - Getting event", eventId);

        TrackingEventResponseDto response = queryService.getEvent(eventId);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for events
     */
    @GetMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Search for events",
        description = "Searches for tracking events with optional filters and pagination"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    public ResponseEntity<PagedResponseDto<TrackingEventResponseDto>> searchEvents(
        @Parameter(description = "Filter by event type") @RequestParam(required = false) String eventType,
        @Parameter(description = "Filter by session ID") @RequestParam(required = false) String sessionId,
        @Parameter(description = "Filter by user ID") @RequestParam(required = false) String userId,
        @Parameter(description = "Filter by source") @RequestParam(required = false) String source,
        @Parameter(description = "Filter by start date (ISO format)") @RequestParam(required = false) String startDate,
        @Parameter(description = "Filter by end date (ISO format)") @RequestParam(required = false) String endDate,
        @Parameter(description = "Filter by processed status") @RequestParam(required = false) Boolean processed,
        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") Integer page,
        @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") Integer size,
        @Parameter(description = "Sort field", example = "timestamp") @RequestParam(defaultValue = "timestamp") String sortBy,
        @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC") @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        log.info("GET /api/v1/tracking/events - Searching events: type={}, page={}", eventType, page);

        var query = GetEventsQuery.builder()
            .eventType(eventType)
            .sessionId(sessionId)
            .userId(userId)
            .source(source)
            .startDate(startDate != null ? LocalDateTime.parse(startDate) : null)
            .endDate(endDate != null ? LocalDateTime.parse(endDate) : null)
            .processed(processed)
            .page(page)
            .size(size)
            .sortBy(sortBy)
            .sortDirection(sortDirection)
            .build();

        PagedResponseDto<TrackingEventResponseDto> response = queryService.searchEvents(query);
        return ResponseEntity.ok(response);
    }

    /**
     * Get tracking statistics
     */
    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get tracking statistics",
        description = "Returns aggregate statistics about tracking data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<java.util.Map<String, Object>> getStatistics() {
        log.info("GET /api/v1/tracking/statistics - Getting statistics");

        Long totalEvents = queryService.countEventsByType("");
        Long activeSessions = queryService.countActiveSessions();
        Long todayEvents = queryService.countEventsByDateRange(
            LocalDateTime.now().toLocalDate().atStartOfDay(),
            LocalDateTime.now()
        );

        java.util.Map<String, Object> stats = java.util.Map.of(
            "totalEvents", totalEvents,
            "activeSessions", activeSessions,
            "todayEvents", todayEvents,
            "timestamp", LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(stats);
    }
}
