package com.gogidix.aiservices.aigatewayservice.application.mapper;

import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GatewayRouteMapper.
 */
@DisplayName("GatewayRouteMapper Tests")
class GatewayRouteMapperTest {

    @Test
    @DisplayName("Should convert entity to response DTO")
    void shouldConvertEntityToResponseDto() {
        GatewayRoute route = GatewayRoute.builder()
                .id("id-123")
                .routeId("route-123")
                .tenantId("tenant-123")
                .path("/api/v1/test")
                .targetService("test-service")
                .targetUrls(List.of("http://localhost:8080"))
                .status(RouteStatus.ACTIVE)
                .rateLimit(500)
                .loadBalancingStrategy(LoadBalancingStrategy.LEAST_CONNECTIONS)
                .requestTimeout(60)
                .build();

        GatewayRouteResponseDto dto = GatewayRouteMapper.toResponseDto(route);

        assertEquals("route-123", dto.routeId());
        assertEquals("tenant-123", dto.tenantId());
        assertEquals("/api/v1/test", dto.path());
        assertEquals("test-service", dto.targetService());
        assertEquals(RouteStatus.ACTIVE, dto.status());
        assertEquals(500, dto.rateLimit());
        assertEquals(LoadBalancingStrategy.LEAST_CONNECTIONS, dto.loadBalancingStrategy());
        assertEquals(60, dto.requestTimeout());
    }

    @Test
    @DisplayName("Should convert list of entities to response DTOs")
    void shouldConvertListToResponseDtos() {
List<GatewayRoute> routes = List.of(
GatewayRoute.builder()
                        .routeId("route-1")
                        .tenantId("tenant-123")
                        .path("/api/v1/test1")
                        .targetService("service1")
                        .targetUrls(List.of("http://localhost:8080"))
.build(),
GatewayRoute.builder()
                        .routeId("route-2")
                        .tenantId("tenant-123")
                        .path("/api/v1/test2")
                        .targetService("service2")
                        .targetUrls(List.of("http://localhost:8081"))
                        .build()
        );

        List<GatewayRouteResponseDto> dtos = GatewayRouteMapper.toResponseDtoList(routes);

        assertEquals(2, dtos.size());
        assertEquals("route-1", dtos.get(0).routeId());
        assertEquals("route-2", dtos.get(1).routeId());
    }
}
