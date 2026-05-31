package com.gogidix.universal.tracking.application;

import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.messaging.event.EventPublisher;
import com.gogidix.shared.security.context.RequestContext;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.application.mapper.TrackingMapper;
import com.gogidix.universal.tracking.application.service.TrackingCommandService;
import com.gogidix.universal.tracking.domain.model.TrackingEvent;
import com.gogidix.universal.tracking.domain.model.TrackingSession;
import com.gogidix.universal.tracking.domain.port.in.CreateEventCommand;
import com.gogidix.universal.tracking.domain.port.in.CreateSessionCommand;
import com.gogidix.universal.tracking.domain.port.in.UpdateSessionCommand;
import com.gogidix.universal.tracking.domain.port.out.TrackingEventRepositoryPort;
import com.gogidix.universal.tracking.domain.port.out.TrackingSessionRepositoryPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TrackingCommandService.
 */
@ExtendWith(MockitoExtension.class)
class TrackingCommandServiceTest {

    @Mock
    private TrackingSessionRepositoryPort sessionRepository;

    @Mock
    private TrackingEventRepositoryPort eventRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private AuditService auditService;

    @Mock
    private TrackingMapper trackingMapper;

    @InjectMocks
    private TrackingCommandService commandService;

    private final String tenantId = "test-tenant";

    @BeforeEach
    void setUp() {
        RequestContext.setTenantId(tenantId);
    }

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    @Test
    void testCreateSession_Success() {
        // Given
        CreateSessionCommand command = CreateSessionCommand.builder()
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .ipAddress("192.168.1.1")
            .userAgent("Mozilla/5.0")
            .deviceType("DESKTOP")
            .browser("Chrome")
            .os("Windows")
            .country("US")
            .city("New York")
            .referrer("https://google.com")
            .landingPage("/home")
            .campaign("spring_sale")
            .build();

        TrackingSession savedSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .userId("user-456")
            .tenantId(tenantId)
            .source("WEB")
            .ipAddress("192.168.1.1")
            .userAgent("Mozilla/5.0")
            .deviceType("DESKTOP")
            .browser("Chrome")
            .os("Windows")
            .country("US")
            .city("New York")
            .referrer("https://google.com")
            .landingPage("/home")
            .campaign("spring_sale")
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .build();

        when(sessionRepository.existsBySessionId("session-123")).thenReturn(false);
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(savedSession);
        when(trackingMapper.toResponseDto(savedSession)).thenReturn(responseDto);

        // When
        TrackingSessionResponseDto result = commandService.createSession(command);

        // Then
        assertNotNull(result);
        assertEquals("session-123", result.getSessionId());
        verify(sessionRepository).save(any(TrackingSession.class));
        verify(eventPublisher).publish(eq("tracking-sessions"), any());
        verify(auditService).audit(eq("SESSION_CREATED"), eq("TrackingSession"), anyString(), anyString());
    }

    @Test
    void testCreateSession_WithDefaults() {
        // Given
        CreateSessionCommand command = CreateSessionCommand.builder()
            .sessionId("session-456")
            .build();

        TrackingSession savedSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-456")
            .tenantId(tenantId)
            .source("WEB")  // Default source
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId("session-456")
            .build();

        when(sessionRepository.existsBySessionId("session-456")).thenReturn(false);
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(savedSession);
        when(trackingMapper.toResponseDto(savedSession)).thenReturn(responseDto);

        // When
        TrackingSessionResponseDto result = commandService.createSession(command);

        // Then
        assertNotNull(result);
        verify(sessionRepository).save(any(TrackingSession.class));
    }

    @Test
    void testCreateSession_AlreadyExists() {
        // Given
        CreateSessionCommand command = CreateSessionCommand.builder()
            .sessionId("existing-session")
            .build();

        when(sessionRepository.existsBySessionId("existing-session")).thenReturn(true);

        // When/Then
        assertThrows(ConflictException.class, () -> commandService.createSession(command));
        verify(sessionRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any(), any());
    }

    @Test
    void testCreateSession_NoTenantInContext() {
        // Given
        RequestContext.clear();
        CreateSessionCommand command = CreateSessionCommand.builder()
            .sessionId("session-123")
            .build();

        when(sessionRepository.existsBySessionId("session-123")).thenReturn(false);

        // When/Then
        assertThrows(ValidationException.class, () -> commandService.createSession(command));
    }

    @Test
    void testCreateEvent_Success() {
        // Given
        CreateEventCommand command = CreateEventCommand.builder()
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .timestamp(LocalDateTime.now())
            .eventName("Homepage Visit")
            .description("User visited the homepage")
            .ipAddress("192.168.1.1")
            .userAgent("Mozilla/5.0")
            .referrer("https://google.com")
            .pageUrl("/home")
            .pageTitle("Home Page")
            .correlationId("corr-123")
            .priority(5)
            .build();

        TrackingEvent savedEvent = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .tenantId(tenantId)
            .source("WEB")
            .timestamp(command.getTimestamp())
            .eventName("Homepage Visit")
            .correlationId("corr-123")
            .priority(5)
            .build();

        TrackingEventResponseDto responseDto = TrackingEventResponseDto.builder()
            .id(savedEvent.getId().toString())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .build();

        when(eventRepository.save(any(TrackingEvent.class))).thenReturn(savedEvent);
        when(sessionRepository.findBySessionId("session-123")).thenReturn(Optional.empty());
        when(trackingMapper.toResponseDto(savedEvent)).thenReturn(responseDto);

        // When
        TrackingEventResponseDto result = commandService.createEvent(command);

        // Then
        assertNotNull(result);
        assertEquals("PAGE_VIEW", result.getEventType());
        verify(eventRepository).save(any(TrackingEvent.class));
        verify(eventPublisher).publish(eq("tracking-events"), any());
        verify(auditService).audit(eq("EVENT_CREATED"), eq("TrackingEvent"), anyString(), anyString());
    }

    @Test
    void testCreateEvent_WithSessionUpdate() {
        // Given
        CreateEventCommand command = CreateEventCommand.builder()
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .timestamp(LocalDateTime.now())
            .build();

        TrackingEvent savedEvent = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .tenantId(tenantId)
            .build();

        TrackingSession existingSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId(tenantId)
            .eventCount(5)
            .pageViewCount(3)
            .build();

        TrackingEventResponseDto responseDto = TrackingEventResponseDto.builder()
            .eventType("PAGE_VIEW")
            .build();

        when(eventRepository.save(any(TrackingEvent.class))).thenReturn(savedEvent);
        when(sessionRepository.findBySessionId("session-123")).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(existingSession);
        when(trackingMapper.toResponseDto(savedEvent)).thenReturn(responseDto);

        // When
        commandService.createEvent(command);

        // Then
        verify(sessionRepository).save(any(TrackingSession.class));
        assertEquals(6, existingSession.getEventCount()); // Incremented
        assertEquals(4, existingSession.getPageViewCount()); // Incremented for PAGE_VIEW
    }

    @Test
    void testCreateEvent_ClickEventDoesNotIncrementPageViews() {
        // Given
        CreateEventCommand command = CreateEventCommand.builder()
            .eventType("CLICK")
            .sessionId("session-123")
            .timestamp(LocalDateTime.now())
            .build();

        TrackingEvent savedEvent = TrackingEvent.builder()
            .id(UUID.randomUUID())
            .eventType("CLICK")
            .sessionId("session-123")
            .tenantId(tenantId)
            .build();

        TrackingSession existingSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId("session-123")
            .tenantId(tenantId)
            .eventCount(5)
            .pageViewCount(3)
            .build();

        TrackingEventResponseDto responseDto = TrackingEventResponseDto.builder()
            .eventType("CLICK")
            .build();

        when(eventRepository.save(any(TrackingEvent.class))).thenReturn(savedEvent);
        when(sessionRepository.findBySessionId("session-123")).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(existingSession);
        when(trackingMapper.toResponseDto(savedEvent)).thenReturn(responseDto);

        // When
        commandService.createEvent(command);

        // Then
        assertEquals(6, existingSession.getEventCount()); // Incremented
        assertEquals(3, existingSession.getPageViewCount()); // Not incremented for CLICK
    }

    @Test
    void testUpdateSession_Success() {
        // Given
        String sessionId = "session-123";
        UpdateSessionCommand command = UpdateSessionCommand.builder()
            .sessionId(sessionId)
            .referrer("https://new-referrer.com")
            .landingPage("/new-landing")
            .campaign("summer_campaign")
            .endSession(false)
            .build();

        TrackingSession existingSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId(sessionId)
            .tenantId(tenantId)
            .referrer("https://old-referrer.com")
            .landingPage("/old-landing")
            .campaign("spring_campaign")
            .isActive(true)
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId(sessionId)
            .referrer("https://new-referrer.com")
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(existingSession);
        when(trackingMapper.toResponseDto(existingSession)).thenReturn(responseDto);

        // When
        TrackingSessionResponseDto result = commandService.updateSession(command);

        // Then
        assertNotNull(result);
        assertEquals("https://new-referrer.com", existingSession.getReferrer());
        assertEquals("/new-landing", existingSession.getLandingPage());
        assertEquals("summer_campaign", existingSession.getCampaign());
        verify(sessionRepository).save(any(TrackingSession.class));
    }

    @Test
    void testUpdateSession_EndSession() {
        // Given
        String sessionId = "session-123";
        UpdateSessionCommand command = UpdateSessionCommand.builder()
            .sessionId(sessionId)
            .endSession(true)
            .build();

        TrackingSession existingSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId(sessionId)
            .tenantId(tenantId)
            .isActive(true)
            .startedAt(LocalDateTime.now().minusMinutes(30))
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId(sessionId)
            .isActive(false)
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(TrackingSession.class))).thenReturn(existingSession);
        when(trackingMapper.toResponseDto(existingSession)).thenReturn(responseDto);

        // When
        commandService.updateSession(command);

        // Then
        assertFalse(existingSession.isActiveSession());
        assertNotNull(existingSession.getEndedAt());
        assertNotNull(existingSession.getDurationSeconds());
    }

    @Test
    void testUpdateSession_NotFound() {
        // Given
        String sessionId = "non-existent";
        UpdateSessionCommand command = UpdateSessionCommand.builder()
            .sessionId(sessionId)
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> commandService.updateSession(command));
    }

    @Test
    void testUpdateSession_DifferentTenant() {
        // Given
        String sessionId = "session-123";
        UpdateSessionCommand command = UpdateSessionCommand.builder()
            .sessionId(sessionId)
            .build();

        TrackingSession existingSession = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId(sessionId)
            .tenantId("different-tenant")
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(existingSession));

        // When/Then
        assertThrows(ValidationException.class, () -> commandService.updateSession(command));
    }

    @Test
    void testCreateEvent_NoTenantInContext() {
        // Given
        RequestContext.clear();
        CreateEventCommand command = CreateEventCommand.builder()
            .eventType("PAGE_VIEW")
            .timestamp(LocalDateTime.now())
            .build();

        // When/Then
        assertThrows(ValidationException.class, () -> commandService.createEvent(command));
    }
}
