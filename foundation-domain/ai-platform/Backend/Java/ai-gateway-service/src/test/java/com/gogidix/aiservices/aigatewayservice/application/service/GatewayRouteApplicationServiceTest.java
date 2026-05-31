package com.gogidix.aiservices.aigatewayservice.application.service;

import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.domain.event.RouteCreatedEvent;
import com.gogidix.aiservices.aigatewayservice.domain.event.RouteDeletedEvent;
import com.gogidix.aiservices.aigatewayservice.domain.event.RouteUpdatedEvent;
import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.domain.port.out.RouteEventPublisherPort;
import com.gogidix.aiservices.aigatewayservice.domain.repository.GatewayRouteRepository;
import com.gogidix.aiservices.aigatewayservice.shared.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GatewayRouteApplicationService.
 */
@DisplayName("GatewayRouteApplicationService Tests")
class GatewayRouteApplicationServiceTest {

    private GatewayRouteRepository repository;
    private RouteEventPublisherPort eventPublisher;
    private GatewayRouteApplicationService service;

    @BeforeEach
    void setUp() {
        repository = mock(GatewayRouteRepository.class);
        eventPublisher = mock(RouteEventPublisherPort.class);
        service = new GatewayRouteApplicationService(repository, eventPublisher);
    }

    @Nested
    @DisplayName("Create Route Tests")
    class CreateRouteTests {

        @Test
        @DisplayName("Should create route successfully")
        void shouldCreateRouteSuccessfully() {
            String tenantId = "tenant-123";
            String path = "/api/v1/test";
            String targetService = "test-service";
            List<String> targetUrls = List.of("http://localhost:8080");

            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.createRoute(tenantId, path, targetService, targetUrls, null, null, null, null);

            verify(repository).save(any(GatewayRoute.class));
            verify(eventPublisher).publish(any(RouteCreatedEvent.class));
        }

        @Test
        @DisplayName("Should create route with filters")
        void shouldCreateRouteWithFilters() {
            String tenantId = "tenant-123";
            String path = "/api/v1/test";
            String targetService = "test-service";
            List<String> targetUrls = List.of("http://localhost:8080");
            List<RouteFilter> filters = List.of(
                    RouteFilter.builder().name("RateLimitFilter").type("RATE_LIMIT").build()
            );

            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            GatewayRouteResponseDto route = service.createRoute(
                    tenantId, path, targetService, targetUrls, filters, null, null, null
            );

            verify(eventPublisher).publish(any(RouteCreatedEvent.class));
        }
    }

    @Nested
    @DisplayName("Update Route Tests")
    class UpdateRouteTests {

        @Test
        @DisplayName("Should update existing route")
        void shouldUpdateExistingRoute() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute existingRoute = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/old-path")
                    .targetService("old-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(existingRoute));
            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.updateRoute(
                    routeId, tenantId, "/new-path", "new-service",
                    List.of("http://localhost:8081"), null,
                    500, LoadBalancingStrategy.LEAST_CONNECTIONS, 60
            );

            verify(repository).save(any(GatewayRoute.class));
            verify(eventPublisher).publish(any(RouteUpdatedEvent.class));
        }

        @Test
        @DisplayName("Should throw when updating non-existent route")
        void shouldThrowWhenUpdatingNonExistentRoute() {
            String routeId = "non-existent";
            String tenantId = "tenant-123";

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    service.updateRoute(
                            routeId, tenantId, "/new-path", "new-service",
                            List.of("http://localhost:8081"), null,
                            500, null, 60
                    )
            );
        }
    }

    @Nested
    @DisplayName("Delete Route Tests")
    class DeleteRouteTests {

        @Test
        @DisplayName("Should delete existing route")
        void shouldDeleteExistingRoute() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute existingRoute = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/api/v1/test")
                    .targetService("test-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(existingRoute));

            service.deleteRoute(routeId, tenantId, "user-123");

            verify(repository).deleteByRouteIdAndTenantId(routeId, tenantId);
            verify(eventPublisher).publish(any(RouteDeletedEvent.class));
        }

        @Test
        @DisplayName("Should throw when deleting non-existent route")
        void shouldThrowWhenDeletingNonExistentRoute() {
            String routeId = "non-existent";
            String tenantId = "tenant-123";

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    service.deleteRoute(routeId, tenantId, "user-123")
            );
        }
    }

    @Nested
    @DisplayName("Query Tests")
    class QueryTests {

        @Test
        @DisplayName("Should get route by ID")
        void shouldGetRouteById() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute route = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/api/v1/test")
                    .targetService("test-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(route));

            var response = service.getRouteById(routeId, tenantId);

            assertNotNull(response);
            assertEquals(routeId, response.routeId());
        }

        @Test
        @DisplayName("Should throw when getting non-existent route")
        void shouldThrowWhenGettingNonExistentRoute() {
            String routeId = "non-existent";
            String tenantId = "tenant-123";

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    service.getRouteById(routeId, tenantId)
            );
        }

        @Test
        @DisplayName("Should get routes by tenant")
        void shouldGetRoutesByTenant() {
            String tenantId = "tenant-123";
List<GatewayRoute> routes = List.of(
GatewayRoute.builder()
                            .routeId("route-1")
                            .tenantId(tenantId)
                            .path("/api/v1/test1")
                            .targetService("service1")
                            .targetUrls(List.of("http://localhost:8080"))
.build(),
GatewayRoute.builder()
                            .routeId("route-2")
                            .tenantId(tenantId)
                            .path("/api/v1/test2")
                            .targetService("service2")
                            .targetUrls(List.of("http://localhost:8081"))
                            .build()
            );

            when(repository.findByTenantId(tenantId)).thenReturn(routes);

            var response = service.getRoutesByTenant(tenantId);

            assertEquals(2, response.size());
        }
    }

    @Nested
    @DisplayName("Activation/Deactivation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate route")
        void shouldActivateRoute() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute route = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/api/v1/test")
                    .targetService("test-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .status(RouteStatus.INACTIVE)
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(route));
            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.activateRoute(routeId, tenantId);

            verify(repository).save(any(GatewayRoute.class));
        }

        @Test
        @DisplayName("Should deactivate route")
        void shouldDeactivateRoute() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute route = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/api/v1/test")
                    .targetService("test-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .status(RouteStatus.ACTIVE)
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(route));
            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.deactivateRoute(routeId, tenantId);

            verify(repository).save(any(GatewayRoute.class));
        }
    }

    @Nested
    @DisplayName("Circuit Breaker Tests")
    class CircuitBreakerTests {

        @Test
        @DisplayName("Should reset circuit breaker")
        void shouldResetCircuitBreaker() {
            String routeId = "route-123";
            String tenantId = "tenant-123";
            GatewayRoute route = GatewayRoute.builder()
                    .routeId(routeId)
                    .tenantId(tenantId)
                    .path("/api/v1/test")
                    .targetService("test-service")
                    .targetUrls(List.of("http://localhost:8080"))
                    .build();

            when(repository.findByRouteIdAndTenantId(routeId, tenantId))
                    .thenReturn(Optional.of(route));
            when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            service.resetCircuitBreaker(routeId, tenantId);

            verify(repository).save(any(GatewayRoute.class));
        }
    }
}
