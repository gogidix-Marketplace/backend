package com.gogidix.aiservices.aigatewayservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.application.service.GatewayRouteApplicationService;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for GatewayRouteController.
 */
@WebMvcTest(controllers = GatewayRouteController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@DisplayName("GatewayRouteController Tests")
class GatewayRouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GatewayRouteApplicationService service;

    @BeforeEach
    void setUp() {
        RequestContextHolder.setContext(new com.gogidix.aiservices.aigatewayservice.shared.requestcontext.RequestContext(
                "tenant-123", "user-123", "corr-123", Instant.now()
        ));
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should create route")
    void shouldCreateRoute() throws Exception {
        String requestJson = """
                {
                    "path": "/api/v1/test",
                    "targetService": "test-service",
                    "targetUrls": ["http://localhost:8080"],
                    "rateLimit": 500,
                    "requestTimeout": 60
                }
                """;

        GatewayRouteResponseDto response = new GatewayRouteResponseDto(
                "id-123", "route-123", "tenant-123", "/api/v1/test",
                "test-service", List.of("http://localhost:8080"), List.of(),
                500, LoadBalancingStrategy.ROUND_ROBIN, 60, RouteStatus.ACTIVE,
                false, 0L, Instant.now(), Instant.now()
        );

        when(service.createRoute(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/gateway/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Correlation-Id"))
                .andExpect(jsonPath("$.routeId").value("route-123"));
    }

    @Test
    @DisplayName("Should get route by ID")
    void shouldGetRouteById() throws Exception {
        GatewayRouteResponseDto response = new GatewayRouteResponseDto(
                "id-123", "route-123", "tenant-123", "/api/v1/test",
                "test-service", List.of("http://localhost:8080"), List.of(),
                1000, LoadBalancingStrategy.ROUND_ROBIN, 30, RouteStatus.ACTIVE,
                false, 0L, Instant.now(), Instant.now()
        );

        lenient().when(service.getRouteById(any(), any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/gateway/routes/route-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeId").value("route-123"))
                .andExpect(jsonPath("$.path").value("/api/v1/test"));
    }

    @Test
    @DisplayName("Should list routes")
    void shouldListRoutes() throws Exception {
        GatewayRouteResponseDto route1 = new GatewayRouteResponseDto(
                "id-1", "route-1", "tenant-123", "/api/v1/test1",
                "service1", List.of("http://localhost:8080"), List.of(),
                1000, LoadBalancingStrategy.ROUND_ROBIN, 30, RouteStatus.ACTIVE,
                false, 0L, Instant.now(), Instant.now()
        );

        GatewayRouteResponseDto route2 = new GatewayRouteResponseDto(
                "id-2", "route-2", "tenant-123", "/api/v1/test2",
                "service2", List.of("http://localhost:8081"), List.of(),
                1000, LoadBalancingStrategy.ROUND_ROBIN, 30, RouteStatus.ACTIVE,
                false, 0L, Instant.now(), Instant.now()
        );

        lenient().when(service.getRoutesByTenant(any()))
                .thenReturn(List.of(route1, route2));

        mockMvc.perform(get("/api/v1/gateway/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("Should delete route")
    void shouldDeleteRoute() throws Exception {
        mockMvc.perform(delete("/api/v1/gateway/routes/route-123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return health status")
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/api/v1/gateway/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
