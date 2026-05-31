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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

/**
 * Unit tests for TrackingQueryService.
 */
@ExtendWith(MockitoExtension.class)
class TrackingQueryServiceTest {

    @Mock
    private TrackingSessionRepositoryPort sessionRepository;

    @Mock
    private TrackingEventRepositoryPort eventRepository;

    @Mock
    private TrackingMetricRepositoryPort metricRepository;

    @Mock
    private TrackingMapper trackingMapper;

    @InjectMocks
    private TrackingQueryService queryService;

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
    void testGetSession_Success() {
        // Given
        String sessionId = "session-123";
        GetSessionQuery query = GetSessionQuery.builder()
            .sessionId(sessionId)
            .build();

        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId(sessionId)
            .tenantId(tenantId)
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId(sessionId)
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));
        when(trackingMapper.toResponseDto(session)).thenReturn(responseDto);

        // When
        TrackingSessionResponseDto result = queryService.getSession(query);

        // Then
        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
        verify(sessionRepository).findBySessionId(sessionId);
        verify(trackingMapper).toResponseDto(session);
    }

    @Test
    void testGetSession_NotFound() {
        // Given
        String sessionId = "non-existent";
        GetSessionQuery query = GetSessionQuery.builder()
            .sessionId(sessionId)
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(NotFoundException.class, () -> queryService.getSession(query));
    }

    @Test
    void testGetSession_DifferentTenant() {
        // Given
        String sessionId = "session-123";
        GetSessionQuery query = GetSessionQuery.builder()
            .sessionId(sessionId)
            .build();

        TrackingSession session = TrackingSession.builder()
            .id(UUID.randomUUID())
            .sessionId(sessionId)
            .tenantId("different-tenant")
            .build();

        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.of(session));

        // When/Then
        assertThrows(ValidationException.class, () -> queryService.getSession(query));
    }

    @Test
    void testGetSessionById_Success() {
        // Given
        UUID sessionId = UUID.randomUUID();
        TrackingSession session = TrackingSession.builder()
            .id(sessionId)
            .sessionId("session-123")
            .tenantId(tenantId)
            .build();

        TrackingSessionResponseDto responseDto = TrackingSessionResponseDto.builder()
            .sessionId("session-123")
            .build();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(trackingMapper.toResponseDto(session)).thenReturn(responseDto);

        // When
        TrackingSessionResponseDto result = queryService.getSessionById(sessionId.toString());

        // Then
        assertNotNull(result);
        verify(sessionRepository).findById(sessionId);
    }

    @Test
    void testGetEvent_Success() {
        // Given
        UUID eventId = UUID.randomUUID();
        TrackingEvent event = TrackingEvent.builder()
            .id(eventId)
            .eventType("PAGE_VIEW")
            .tenantId(tenantId)
            .build();

        TrackingEventResponseDto responseDto = TrackingEventResponseDto.builder()
            .id(eventId.toString())
            .eventType("PAGE_VIEW")
            .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(trackingMapper.toResponseDto(event)).thenReturn(responseDto);

        // When
        TrackingEventResponseDto result = queryService.getEvent(eventId.toString());

        // Then
        assertNotNull(result);
        assertEquals(eventId.toString(), result.getId());
    }

    @Test
    void testSearchEvents_Success() {
        // Given
        GetEventsQuery query = GetEventsQuery.builder()
            .eventType("PAGE_VIEW")
            .page(0)
            .size(20)
            .build();

        List<TrackingEvent> events = List.of(
            TrackingEvent.builder()
                .id(UUID.randomUUID())
                .eventType("PAGE_VIEW")
                .tenantId(tenantId)
                .build()
        );

        Page<TrackingEvent> eventPage = new PageImpl<>(events);
        PagedResponseDto<TrackingEventResponseDto> pagedResponse = PagedResponseDto.<TrackingEventResponseDto>builder()
            .items(List.of(TrackingEventResponseDto.builder().eventType("PAGE_VIEW").build()))
            .page(0)
            .size(20)
            .totalElements(1L)
            .totalPages(1)
            .build();

        when(eventRepository.findByFilters(eq(tenantId), eq("PAGE_VIEW"), isNull(), isNull(),
            isNull(), isNull(), isNull(), isNull(), any(PageRequest.class))).thenReturn(eventPage);
        doReturn(pagedResponse).when(trackingMapper).toPagedResponse(any());

        // When
        PagedResponseDto<TrackingEventResponseDto> result = queryService.searchEvents(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSearchMetrics_Success() {
        // Given
        GetMetricsQuery query = GetMetricsQuery.builder()
            .metricName("daily_page_views")
            .page(0)
            .size(20)
            .build();

        List<TrackingMetric> metrics = List.of(
            TrackingMetric.builder()
                .id(UUID.randomUUID())
                .metricName("daily_page_views")
                .tenantId(tenantId)
                .build()
        );

        Page<TrackingMetric> metricPage = new PageImpl<>(metrics);
        PagedResponseDto<TrackingMetricResponseDto> pagedResponse = PagedResponseDto.<TrackingMetricResponseDto>builder()
            .items(List.of(TrackingMetricResponseDto.builder().metricName("daily_page_views").build()))
            .page(0)
            .size(20)
            .totalElements(1L)
            .totalPages(1)
            .build();

        when(metricRepository.findByFilters(eq(tenantId), eq("daily_page_views"), isNull(),
            isNull(), isNull(), isNull(), isNull(), any(PageRequest.class))).thenReturn(metricPage);
        doReturn(pagedResponse).when(trackingMapper).toPagedResponse(any());

        // When
        PagedResponseDto<TrackingMetricResponseDto> result = queryService.searchMetrics(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetEventsBySessionId_Success() {
        // Given
        String sessionId = "session-123";
        List<TrackingEvent> events = List.of(
            TrackingEvent.builder()
                .id(UUID.randomUUID())
                .eventType("PAGE_VIEW")
                .sessionId(sessionId)
                .tenantId(tenantId)
                .build()
        );

        List<TrackingEventResponseDto> responseDtos = List.of(
            TrackingEventResponseDto.builder()
                .eventType("PAGE_VIEW")
                .sessionId(sessionId)
                .build()
        );

        when(eventRepository.findBySessionId(sessionId)).thenReturn(events);
        when(trackingMapper.toEventResponseDtoList(events)).thenReturn(responseDtos);

        // When
        List<TrackingEventResponseDto> result = queryService.getEventsBySessionId(sessionId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCountEventsByType_Success() {
        // Given
        String eventType = "PAGE_VIEW";
        when(eventRepository.countByEventTypeAndTenantId(eventType, tenantId)).thenReturn(42L);

        // When
        Long result = queryService.countEventsByType(eventType);

        // Then
        assertEquals(42L, result);
        verify(eventRepository).countByEventTypeAndTenantId(eventType, tenantId);
    }

    @Test
    void testCountEventsByDateRange_Success() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(eventRepository.countByTimestampBetweenAndTenantId(start, end, tenantId)).thenReturn(100L);

        // When
        Long result = queryService.countEventsByDateRange(start, end);

        // Then
        assertEquals(100L, result);
    }

    @Test
    void testCountActiveSessions_Success() {
        // Given
        when(sessionRepository.countByTenantIdAndIsActive(tenantId, true)).thenReturn(15L);

        // When
        Long result = queryService.countActiveSessions();

        // Then
        assertEquals(15L, result);
    }

    @Test
    void testCountEventsByType_NoTenantInContext() {
        // Given
        RequestContext.clear();
        String eventType = "PAGE_VIEW";

        // When/Then
        assertThrows(ValidationException.class, () -> queryService.countEventsByType(eventType));
    }
}
