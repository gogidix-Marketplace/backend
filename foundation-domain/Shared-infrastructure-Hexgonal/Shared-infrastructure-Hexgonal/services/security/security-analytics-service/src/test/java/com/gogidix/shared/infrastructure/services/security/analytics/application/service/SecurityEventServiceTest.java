package com.gogidix.shared.infrastructure.services.security.analytics.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.mapper.SecurityEventMapper;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.exception.SecurityEventNotFoundException;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.exception.SecurityEventValidationException;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.port.out.SecurityEventRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SecurityEventService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Security Event Service Tests")
class SecurityEventServiceTest {

    @Mock
    private SecurityEventMapper mapper;

    @Mock
    private SecurityEventRepositoryPort repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private SecurityEventService securityEventService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_EVENT_ID = "event-123";

    private SecurityEvent testEvent;
    private SecurityEventResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        testEvent = new SecurityEvent(TenantId.of(TEST_TENANT_ID), "LOGIN_ATTEMPT", "HIGH");
        testEvent.setId(TEST_EVENT_ID);
        testEvent.setSource("web-app");
        testEvent.setDescription("Failed login attempt");
        testEvent.setCreatedAt(LocalDateTime.now());

        testResponseDto = new SecurityEventResponseDto(
            TEST_EVENT_ID,
            TEST_TENANT_ID,
            testEvent.getEventId(),
            "LOGIN_ATTEMPT",
            "HIGH",
            "web-app",
            "Failed login attempt",
            testEvent.getTimestamp(),
            testEvent.getCreatedAt()
        );
    }

    @Test
    @DisplayName("Should create event successfully")
    void shouldCreateEventSuccessfully() {
        CreateSecurityEventRequestDto request = new CreateSecurityEventRequestDto(
            "DATA_BREACH",
            "CRITICAL",
            "external-api",
            "Potential data breach",
            LocalDateTime.now()
        );

        when(mapper.toEntity(request, TEST_TENANT_ID)).thenReturn(testEvent);
        when(repository.save(testEvent)).thenReturn(testEvent);
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        SecurityEventResponseDto response = securityEventService.create(request);

        assertNotNull(response);
        assertEquals(TEST_EVENT_ID, response.id());
        assertEquals("LOGIN_ATTEMPT", response.eventType());
        verify(repository).save(testEvent);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find event by id successfully")
    void shouldFindEventByIdSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID)).thenReturn(Optional.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        SecurityEventResponseDto response = securityEventService.findById(TEST_EVENT_ID);

        assertNotNull(response);
        assertEquals(TEST_EVENT_ID, response.id());
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when event not found by id")
    void shouldThrowExceptionWhenEventNotFoundById() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent-id")).thenReturn(Optional.empty());

        assertThrows(SecurityEventNotFoundException.class, () -> securityEventService.findById("non-existent-id"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find all events")
    void shouldFindAllEvents() {
        SecurityEvent event2 = new SecurityEvent(TenantId.of(TEST_TENANT_ID), "LOGOUT", "LOW");
        event2.setId("event-456");

        SecurityEventResponseDto responseDto2 = new SecurityEventResponseDto(
            "event-456",
            TEST_TENANT_ID,
            event2.getEventId(),
            "LOGOUT",
            "LOW",
            null,
            null,
            event2.getTimestamp(),
            null
        );

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testEvent, event2));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);
        when(mapper.toResponseDto(event2)).thenReturn(responseDto2);

        List<SecurityEventResponseDto> responses = securityEventService.findAll();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(TEST_EVENT_ID, responses.get(0).id());
        assertEquals("event-456", responses.get(1).id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return empty list when no events found")
    void shouldReturnEmptyListWhenNoEventsFound() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of());

        List<SecurityEventResponseDto> responses = securityEventService.findAll();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should find events by severity")
    void shouldFindEventsBySeverity() {
        when(repository.findByTenantIdAndSeverity(TEST_TENANT_ID, "HIGH")).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findBySeverity("HIGH");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("HIGH", responses.get(0).severity());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find events by event type")
    void shouldFindEventsByEventType() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findByEventType("login_attempt");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("LOGIN_ATTEMPT", responses.get(0).eventType());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should find recent events by hours")
    void shouldFindRecentEventsByHours() {
        when(repository.findByTenantIdAndTimestampAfter(eq(TEST_TENANT_ID), any(LocalDateTime.class)))
            .thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findRecent("24");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        verify(repository).findByTenantIdAndTimestampAfter(eq(TEST_TENANT_ID), any(LocalDateTime.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception for invalid hours parameter")
    void shouldThrowExceptionForInvalidHoursParameter() {
        assertThrows(SecurityEventValidationException.class, () -> securityEventService.findRecent("invalid"));
        verify(repository, never()).findByTenantIdAndTimestampAfter(any(), any());
    }

    @Test
    @DisplayName("Should delete event successfully")
    void shouldDeleteEventSuccessfully() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID)).thenReturn(Optional.of(testEvent));
        doNothing().when(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID);

        assertDoesNotThrow(() -> securityEventService.delete(TEST_EVENT_ID));
        verify(repository).findByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID);
        verify(repository).deleteByTenantIdAndId(TEST_TENANT_ID, TEST_EVENT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent event")
    void shouldThrowExceptionWhenDeletingNonExistentEvent() {
        when(repository.findByTenantIdAndId(TEST_TENANT_ID, "non-existent")).thenReturn(Optional.empty());

        assertThrows(SecurityEventNotFoundException.class, () -> securityEventService.delete("non-existent"));
        verify(repository, never()).deleteByTenantIdAndId(any(), any());
    }

    @Test
    @DisplayName("Should find events with CRITICAL severity")
    void shouldFindEventsWithCriticalSeverity() {
        testEvent.setSeverity("CRITICAL");
        SecurityEventResponseDto criticalResponseDto = new SecurityEventResponseDto(
            TEST_EVENT_ID,
            TEST_TENANT_ID,
            testEvent.getEventId(),
            "LOGIN_ATTEMPT",
            "CRITICAL",
            "web-app",
            "Failed login attempt",
            testEvent.getTimestamp(),
            testEvent.getCreatedAt()
        );

        when(repository.findByTenantIdAndSeverity(TEST_TENANT_ID, "CRITICAL")).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(criticalResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findBySeverity("CRITICAL");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("CRITICAL", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should find events with LOW severity")
    void shouldFindEventsWithLowSeverity() {
        testEvent.setSeverity("LOW");
        SecurityEventResponseDto lowResponseDto = new SecurityEventResponseDto(
            TEST_EVENT_ID,
            TEST_TENANT_ID,
            testEvent.getEventId(),
            "LOGIN_ATTEMPT",
            "LOW",
            "web-app",
            "Failed login attempt",
            testEvent.getTimestamp(),
            testEvent.getCreatedAt()
        );

        when(repository.findByTenantIdAndSeverity(TEST_TENANT_ID, "LOW")).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(lowResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findBySeverity("LOW");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("LOW", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should find events with MEDIUM severity")
    void shouldFindEventsWithMediumSeverity() {
        testEvent.setSeverity("MEDIUM");
        SecurityEventResponseDto mediumResponseDto = new SecurityEventResponseDto(
            TEST_EVENT_ID,
            TEST_TENANT_ID,
            testEvent.getEventId(),
            "LOGIN_ATTEMPT",
            "MEDIUM",
            "web-app",
            "Failed login attempt",
            testEvent.getTimestamp(),
            testEvent.getCreatedAt()
        );

        when(repository.findByTenantIdAndSeverity(TEST_TENANT_ID, "MEDIUM")).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(mediumResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findBySeverity("MEDIUM");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("MEDIUM", responses.get(0).severity());
    }

    @Test
    @DisplayName("Should handle finding recent events with zero hours")
    void shouldHandleFindingRecentEventsWithZeroHours() {
        when(repository.findByTenantIdAndTimestampAfter(eq(TEST_TENANT_ID), any(LocalDateTime.class)))
            .thenReturn(List.of());

        List<SecurityEventResponseDto> responses = securityEventService.findRecent("0");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should handle finding recent events with large hours value")
    void shouldHandleFindingRecentEventsWithLargeHoursValue() {
        when(repository.findByTenantIdAndTimestampAfter(eq(TEST_TENANT_ID), any(LocalDateTime.class)))
            .thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findRecent("168"); // 1 week

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should filter events by event type case insensitively")
    void shouldFilterEventsByEventTypeCaseInsensitively() {
        testEvent.setEventType("DATA_BREACH");

        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testEvent));
        when(mapper.toResponseDto(testEvent)).thenReturn(testResponseDto);

        List<SecurityEventResponseDto> responses = securityEventService.findByEventType("data_breach");

        assertNotNull(responses);
        assertEquals(1, responses.size());
    }

    @Test
    @DisplayName("Should return empty list when no events match event type")
    void shouldReturnEmptyListWhenNoEventsMatchEventType() {
        when(repository.findAllByTenantId(TEST_TENANT_ID)).thenReturn(List.of(testEvent));

        List<SecurityEventResponseDto> responses = securityEventService.findByEventType("NON_EXISTENT_TYPE");

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}
