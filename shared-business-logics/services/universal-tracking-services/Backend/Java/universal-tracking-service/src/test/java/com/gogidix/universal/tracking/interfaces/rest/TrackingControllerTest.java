package com.gogidix.universal.tracking.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.security.context.RequestContext;
import com.gogidix.universal.tracking.application.dto.request.CreateEventRequestDto;
import com.gogidix.universal.tracking.application.dto.request.CreateSessionRequestDto;
import com.gogidix.universal.tracking.application.dto.request.UpdateSessionRequestDto;
import com.gogidix.universal.tracking.application.dto.response.PagedResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingEventResponseDto;
import com.gogidix.universal.tracking.application.dto.response.TrackingSessionResponseDto;
import com.gogidix.universal.tracking.application.mapper.TrackingMapper;
import com.gogidix.universal.tracking.application.service.TrackingCommandService;
import com.gogidix.universal.tracking.application.service.TrackingQueryService;
import com.gogidix.universal.tracking.domain.port.in.CreateEventCommand;
import com.gogidix.universal.tracking.domain.port.in.CreateSessionCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrackingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrackingCommandService commandService;

    @Mock
    private TrackingQueryService queryService;

    @Mock
    private TrackingMapper trackingMapper;

    @InjectMocks
    private TrackingController trackingController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String tenantId = "test-tenant";

    @BeforeEach
    void setUp() {
        RequestContext.setTenantId(tenantId);
        mockMvc = MockMvcBuilders.standaloneSetup(trackingController).build();
        objectMapper.findAndRegisterModules();
    }

    @AfterEach
    void tearDown() {
        RequestContext.clear();
    }

    @Test
    void testCreateSession_Success() throws Exception {
        CreateSessionRequestDto request = CreateSessionRequestDto.builder()
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .build();

        TrackingSessionResponseDto response = TrackingSessionResponseDto.builder()
            .id(UUID.randomUUID().toString())
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .build();

        CreateSessionCommand command = CreateSessionCommand.builder()
            .sessionId("session-123")
            .userId("user-456")
            .source("WEB")
            .build();

        when(trackingMapper.toCommand(request)).thenReturn(command);
        when(commandService.createSession(command)).thenReturn(response);

        mockMvc.perform(post("/api/v1/tracking/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sessionId").value("session-123"))
                .andExpect(jsonPath("$.userId").value("user-456"));
    }

    @Test
    void testGetSession_Success() throws Exception {
        String sessionId = "session-123";
        TrackingSessionResponseDto response = TrackingSessionResponseDto.builder()
            .id(UUID.randomUUID().toString())
            .sessionId(sessionId)
            .userId("user-456")
            .source("WEB")
            .build();

        when(queryService.getSession(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/tracking/sessions/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId));
    }

    @Test
    void testUpdateSession_Success() throws Exception {
        String sessionId = "session-123";
        UpdateSessionRequestDto request = UpdateSessionRequestDto.builder()
            .referrer("https://google.com")
            .endSession(false)
            .build();

        TrackingSessionResponseDto response = TrackingSessionResponseDto.builder()
            .id(UUID.randomUUID().toString())
            .sessionId(sessionId)
            .referrer("https://google.com")
            .build();

        when(commandService.updateSession(any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/tracking/sessions/{sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referrer").value("https://google.com"));
    }

    @Test
    void testGetSessionEvents_Success() throws Exception {
        String sessionId = "session-123";
        List<TrackingEventResponseDto> events = List.of(
            TrackingEventResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .eventType("PAGE_VIEW")
                .sessionId(sessionId)
                .build(),
            TrackingEventResponseDto.builder()
                .id(UUID.randomUUID().toString())
                .eventType("CLICK")
                .sessionId(sessionId)
                .build()
        );

        when(queryService.getEventsBySessionId(sessionId)).thenReturn(events);

        mockMvc.perform(get("/api/v1/tracking/sessions/{sessionId}/events", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCreateEvent_Success() throws Exception {
        CreateEventRequestDto request = CreateEventRequestDto.builder()
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .timestamp(LocalDateTime.now())
            .pageUrl("/home")
            .pageTitle("Home Page")
            .build();

        TrackingEventResponseDto response = TrackingEventResponseDto.builder()
            .id(UUID.randomUUID().toString())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .pageUrl("/home")
            .pageTitle("Home Page")
            .build();

        CreateEventCommand command = CreateEventCommand.builder()
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .userId("user-456")
            .timestamp(request.getTimestamp())
            .build();

        when(trackingMapper.toCommand(request)).thenReturn(command);
        when(commandService.createEvent(command)).thenReturn(response);

        mockMvc.perform(post("/api/v1/tracking/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventType").value("PAGE_VIEW"))
                .andExpect(jsonPath("$.sessionId").value("session-123"));
    }

    @Test
    void testGetEvent_Success() throws Exception {
        UUID eventId = UUID.randomUUID();
        TrackingEventResponseDto response = TrackingEventResponseDto.builder()
            .id(eventId.toString())
            .eventType("PAGE_VIEW")
            .sessionId("session-123")
            .build();

        when(queryService.getEvent(eventId.toString())).thenReturn(response);

        mockMvc.perform(get("/api/v1/tracking/events/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId.toString()))
                .andExpect(jsonPath("$.eventType").value("PAGE_VIEW"));
    }

    @Test
    void testSearchEvents_Success() throws Exception {
        PagedResponseDto<TrackingEventResponseDto> response = PagedResponseDto.<TrackingEventResponseDto>builder()
            .items(List.of(
                TrackingEventResponseDto.builder()
                    .id(UUID.randomUUID().toString())
                    .eventType("PAGE_VIEW")
                    .build()
            ))
            .page(0)
            .size(20)
            .totalElements(1L)
            .totalPages(1)
            .build();

        when(queryService.searchEvents(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/tracking/events")
                .param("eventType", "PAGE_VIEW")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    void testGetStatistics_Success() throws Exception {
        when(queryService.countEventsByType("")).thenReturn(1000L);
        when(queryService.countActiveSessions()).thenReturn(50L);
        when(queryService.countEventsByDateRange(any(), any())).thenReturn(250L);

        mockMvc.perform(get("/api/v1/tracking/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEvents").value(1000))
                .andExpect(jsonPath("$.activeSessions").value(50))
                .andExpect(jsonPath("$.todayEvents").value(250))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testCreateEvent_ValidationError() throws Exception {
        CreateEventRequestDto request = CreateEventRequestDto.builder()
            .eventType("")
            .timestamp(null)
            .build();

        mockMvc.perform(post("/api/v1/tracking/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSession_ValidationError() throws Exception {
        CreateSessionRequestDto request = CreateSessionRequestDto.builder()
            .sessionId("")
            .build();

        mockMvc.perform(post("/api/v1/tracking/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
