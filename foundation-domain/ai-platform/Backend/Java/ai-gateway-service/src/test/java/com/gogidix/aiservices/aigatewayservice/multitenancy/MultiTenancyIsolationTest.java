package com.gogidix.aiservices.aigatewayservice.multitenancy;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.infrastructure.metrics.GatewayMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Multi-tenancy isolation tests for financial-grade certification.
 * Tests that tenant resources are properly isolated and metrics are segregated.
 */
@DisplayName("Multi-Tenancy Isolation Tests")
class MultiTenancyIsolationTest {

    private GatewayMetrics metrics;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new GatewayMetrics(meterRegistry);
    }

    @Nested
    @DisplayName("Domain Model Multi-Tenancy Tests")
    class DomainModelTests {

        @Test
        @DisplayName("Should create routes with distinct tenant IDs")
        void shouldCreateRoutesWithDistinctTenantIds() {
            GatewayRoute route1 = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service1")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .build();

            GatewayRoute route2 = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service2")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .build();

            assertThat(route1.getTenantId()).isEqualTo("tenant-1");
            assertThat(route2.getTenantId()).isEqualTo("tenant-2");
            assertThat(route1.getTenantId()).isNotEqualTo(route2.getTenantId());
        }

        @Test
        @DisplayName("Should maintain route isolation between tenants")
        void shouldMaintainRouteIsolationBetweenTenants() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .build();

            // Same path, different tenants - should be treated as separate routes
            assertThat(tenant1Route.getTenantId()).isNotEqualTo(tenant2Route.getTenantId());
            assertThat(tenant1Route.getPath()).isEqualTo(tenant2Route.getPath());
            assertThat(tenant1Route.getTargetService()).isNotEqualTo(tenant2Route.getTargetService());
        }

        @Test
        @DisplayName("Should support independent route status per tenant")
        void shouldSupportIndependentRouteStatusPerTenant() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .status(RouteStatus.ACTIVE)
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .status(RouteStatus.INACTIVE)
                    .build();

            assertThat(tenant1Route.getStatus()).isEqualTo(RouteStatus.ACTIVE);
            assertThat(tenant2Route.getStatus()).isEqualTo(RouteStatus.INACTIVE);
            assertThat(tenant1Route.getStatus()).isNotEqualTo(tenant2Route.getStatus());
        }

        @Test
        @DisplayName("Should support independent rate limits per tenant")
        void shouldSupportIndependentRateLimitsPerTenant() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .rateLimit(1000)
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .rateLimit(5000)
                    .build();

            assertThat(tenant1Route.getRateLimit()).isEqualTo(1000);
            assertThat(tenant2Route.getRateLimit()).isEqualTo(5000);
        }

        @Test
        @DisplayName("Should support independent circuit breaker state per tenant")
        void shouldSupportIndependentCircuitBreakerStatePerTenant() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .circuitBreakerThreshold(5)
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .circuitBreakerThreshold(10)
                    .build();

            // Record failures in tenant 1 route
            for (int i = 0; i < 5; i++) {
                tenant1Route.recordFailure();
            }

            assertThat(tenant1Route.isCircuitBreakerOpen()).isTrue();
            assertThat(tenant2Route.isCircuitBreakerOpen()).isFalse();
        }
    }

    @Nested
    @DisplayName("Metrics Multi-Tenancy Tests")
    class MetricsTests {

        @Test
        @DisplayName("Should record metrics with tenant tags")
        void shouldRecordMetricsWithTenantTags() {
            metrics.recordRequest("tenant-1", "route-1", true, 150);
            metrics.recordRequest("tenant-2", "route-2", true, 200);

            Search search = meterRegistry.find("ai.gateway.requests.tagged");
            assertThat(search.counter()).isNotNull();

            // Verify tenant-specific metrics exist
            var tenant1Counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-1", "route", "route-1")
                    .counter();

            var tenant2Counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-2", "route", "route-2")
                    .counter();

            assertThat(tenant1Counter).isNotNull();
            assertThat(tenant2Counter).isNotNull();
        }

        @Test
        @DisplayName("Should maintain separate counters per tenant")
        void shouldMaintainSeparateCountersPerTenant() {
            metrics.recordRequest("tenant-1", "route-1", true, 100);
            metrics.recordRequest("tenant-1", "route-1", true, 100);
            metrics.recordRequest("tenant-2", "route-1", true, 100);

            var tenant1Counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-1", "route", "route-1", "status", "success")
                    .counter();

            var tenant2Counter = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-2", "route", "route-1", "status", "success")
                    .counter();

            assertThat(tenant1Counter).isNotNull();
            assertThat(tenant1Counter.count()).isEqualTo(2);
            assertThat(tenant2Counter).isNotNull();
            assertThat(tenant2Counter.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should record tenant-specific latency metrics")
        void shouldRecordTenantSpecificLatencyMetrics() {
            metrics.recordRequest("tenant-1", "route-1", true, 100);
            metrics.recordRequest("tenant-1", "route-1", true, 200);
            metrics.recordRequest("tenant-2", "route-1", true, 300);

            var tenant1Timer = meterRegistry.find("ai.gateway.latency.tagged")
                    .tags("tenant", "tenant-1", "route", "route-1")
                    .timer();

            var tenant2Timer = meterRegistry.find("ai.gateway.latency.tagged")
                    .tags("tenant", "tenant-2", "route", "route-1")
                    .timer();

            assertThat(tenant1Timer).isNotNull();
            assertThat(tenant2Timer).isNotNull();
            assertThat(tenant1Timer.count()).isEqualTo(2);
            assertThat(tenant2Timer.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should support tenant-based failure tracking")
        void shouldSupportTenantBasedFailureTracking() {
            metrics.recordRequest("tenant-1", "route-1", false, 100);
            metrics.recordRequest("tenant-1", "route-1", true, 100);
            metrics.recordRequest("tenant-2", "route-1", true, 100);

            var tenant1Failures = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-1", "status", "failure")
                    .counter();

            var tenant2Failures = meterRegistry.find("ai.gateway.requests.tagged")
                    .tags("tenant", "tenant-2", "status", "failure")
                    .counter();

            assertThat(tenant1Failures).isNotNull();
            assertThat(tenant1Failures.count()).isEqualTo(1);
            // tenant2 had no failures, so counter may be null or have count 0
            if (tenant2Failures != null) {
                assertThat(tenant2Failures.count()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("Data Isolation Tests")
    class DataIsolationTests {

        @Test
        @DisplayName("Should prevent cross-tenant route interference")
        void shouldPreventCrossTenantRouteInterference() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/resource")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .rateLimit(100)
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/resource")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .rateLimit(200)
                    .build();

            // Activate tenant 1 route
            tenant1Route.activate();

            // Tenant 2 route should remain inactive
            assertThat(tenant1Route.getStatus()).isEqualTo(RouteStatus.ACTIVE);
            assertThat(tenant2Route.getStatus()).isEqualTo(RouteStatus.INACTIVE);

            // Tenant 1 rate limit should not affect tenant 2
            assertThat(tenant1Route.getRateLimit()).isEqualTo(100);
            assertThat(tenant2Route.getRateLimit()).isEqualTo(200);
        }

        @Test
        @DisplayName("Should maintain separate filter configurations per tenant")
        void shouldMaintainSeparateFilterConfigurationsPerTenant() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080"))
                    .build();

            // Add filters to each route
            tenant1Route.addFilter(com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter.builder()
                    .name("auth-filter")
                    .type("authentication")
                    .parameters(java.util.Map.of("provider", "oauth2"))
                    .build());

            tenant2Route.addFilter(com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter.builder()
                    .name("auth-filter")
                    .type("authentication")
                    .parameters(java.util.Map.of("provider", "ldap"))
                    .build());

            // Filters should be independent
            assertThat(tenant1Route.hasFilter("auth-filter")).isTrue();
            assertThat(tenant2Route.hasFilter("auth-filter")).isTrue();

            var tenant1Filter = tenant1Route.getFilters().stream()
                    .filter(f -> f.getName().equals("auth-filter"))
                    .findFirst();

            var tenant2Filter = tenant2Route.getFilters().stream()
                    .filter(f -> f.getName().equals("auth-filter"))
                    .findFirst();

            assertThat(tenant1Filter).isPresent();
            assertThat(tenant2Filter).isPresent();
            assertThat(tenant1Filter.get().getParameters().get("provider")).isEqualTo("oauth2");
            assertThat(tenant2Filter.get().getParameters().get("provider")).isEqualTo("ldap");
        }

        @Test
        @DisplayName("Should support tenant-specific load balancing strategies")
        void shouldSupportTenantSpecificLoadBalancingStrategies() {
            GatewayRoute tenant1Route = GatewayRoute.builder()
                    .tenantId("tenant-1")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080", "http://service-1-backup:8080"))
                    .loadBalancingStrategy(LoadBalancingStrategy.ROUND_ROBIN)
                    .build();

            GatewayRoute tenant2Route = GatewayRoute.builder()
                    .tenantId("tenant-2")
                    .path("/api/service")
                    .targetService("service-2")
                    .targetUrls(List.of("http://service-2:8080", "http://service-2-backup:8080"))
                    .loadBalancingStrategy(LoadBalancingStrategy.LEAST_CONNECTIONS)
                    .build();

            assertThat(tenant1Route.getLoadBalancingStrategy()).isEqualTo(LoadBalancingStrategy.ROUND_ROBIN);
            assertThat(tenant2Route.getLoadBalancingStrategy()).isEqualTo(LoadBalancingStrategy.LEAST_CONNECTIONS);
        }
    }

    @Nested
    @DisplayName("Security Isolation Tests")
    class SecurityIsolationTests {

        @Test
        @DisplayName("Should validate tenant ID on route creation")
        void shouldValidateTenantIdOnRouteCreation() {
            GatewayRoute route = GatewayRoute.builder()
                    .tenantId("tenant-123")
                    .path("/api/service")
                    .targetService("service-1")
                    .targetUrls(List.of("http://service-1:8080"))
                    .build();

            route.validate();

            assertThat(route.getTenantId()).isEqualTo("tenant-123");
        }

        @Test
        @DisplayName("Should reject route with null tenant ID")
        void shouldRejectRouteWithNullTenantId() {
            org.junit.jupiter.api.Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> GatewayRoute.builder()
                            .path("/api/service")
                            .targetService("service-1")
                            .targetUrls(List.of("http://service-1:8080"))
                            .build(),
                    "Expected exception when building route without tenantId"
            );
        }

        @Test
        @DisplayName("Should reject route with empty tenant ID")
        void shouldRejectRouteWithEmptyTenantId() {
            org.junit.jupiter.api.Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> GatewayRoute.builder()
                            .tenantId("")
                            .path("/api/service")
                            .targetService("service-1")
                            .targetUrls(List.of("http://service-1:8080"))
                            .build(),
                    "Expected exception when building route with empty tenantId"
            );
        }
    }
}
