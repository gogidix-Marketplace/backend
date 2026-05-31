package com.gogidix.aiservices.aigatewayservice.domain.model;

import com.gogidix.aiservices.aigatewayservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GatewayRoute domain model.
 */
@DisplayName("GatewayRoute Domain Model Tests")
class GatewayRouteTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String PATH = "/api/v1/test";
    private static final String TARGET_SERVICE = "test-service";
    private static final List<String> TARGET_URLS = List.of("http://localhost:8080", "http://localhost:8081");

    private RouteFilter filter;

    @BeforeEach
    void setUp() {
        filter = RouteFilter.builder()
                .name("RateLimitFilter")
                .type("RATE_LIMIT")
                .parameters(Map.of("limit", 100))
                .build();
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create route with valid parameters")
        void shouldCreateRouteWithValidParameters() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            assertNotNull(route.getId());
            assertNotNull(route.getRouteId());
            assertEquals(TENANT_ID, route.getTenantId());
            assertEquals(PATH, route.getPath());
            assertEquals(TARGET_SERVICE, route.getTargetService());
            assertEquals(TARGET_URLS, route.getTargetUrls());
            assertEquals(RouteStatus.INACTIVE, route.getStatus());
            assertEquals(LoadBalancingStrategy.ROUND_ROBIN, route.getLoadBalancingStrategy());
            assertEquals(1000, route.getRateLimit());
            assertEquals(30, route.getRequestTimeout());
            assertFalse(route.isCircuitBreakerOpen());
            assertEquals(0L, route.getCircuitBreakerFailureCount());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new GatewayRoute(null, PATH, TARGET_SERVICE, TARGET_URLS));
        }

        @Test
        @DisplayName("Should throw exception when path is null")
        void shouldThrowWhenPathIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new GatewayRoute(TENANT_ID, null, TARGET_SERVICE, TARGET_URLS));
        }

        @Test
        @DisplayName("Should throw exception when targetService is null")
        void shouldThrowWhenTargetServiceIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new GatewayRoute(TENANT_ID, PATH, null, TARGET_URLS));
        }

        @Test
        @DisplayName("Should throw exception when targetUrls is null")
        void shouldThrowWhenTargetUrlsIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, null));
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate route when in INACTIVE status")
        void shouldActivateRouteWhenInactive() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.activate();

            assertEquals(RouteStatus.ACTIVE, route.getStatus());
        }

        @Test
        @DisplayName("Should throw when activating already active route")
        void shouldThrowWhenActivatingActiveRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.activate();

            assertThrows(IllegalStateException.class, route::activate);
        }

        @Test
        @DisplayName("Should throw when activating route without target URLs")
        void shouldThrowWhenActivatingWithoutTargetUrls() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.setTargetUrls(List.of());

            assertThrows(IllegalStateException.class, route::activate);
        }
    }

    @Nested
    @DisplayName("Deactivation Tests")
    class DeactivationTests {

        @Test
        @DisplayName("Should deactivate active route")
        void shouldDeactivateActiveRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.activate();

            route.deactivate();

            assertEquals(RouteStatus.INACTIVE, route.getStatus());
        }

        @Test
        @DisplayName("Should throw when deactivating already inactive route")
        void shouldThrowWhenDeactivatingInactiveRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            assertThrows(IllegalStateException.class, route::deactivate);
        }
    }

    @Nested
    @DisplayName("Filter Management Tests")
    class FilterManagementTests {

        @Test
        @DisplayName("Should add filter to route")
        void shouldAddFilterToRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.addFilter(filter);

            assertTrue(route.hasFilter("RateLimitFilter"));
            assertEquals(1, route.getFilters().size());
        }

        @Test
        @DisplayName("Should not add duplicate filter")
        void shouldNotAddDuplicateFilter() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.addFilter(filter);

            route.addFilter(filter);

            assertEquals(1, route.getFilters().size());
        }

        @Test
        @DisplayName("Should remove filter from route")
        void shouldRemoveFilterFromRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.addFilter(filter);

            route.removeFilter("RateLimitFilter");

            assertFalse(route.hasFilter("RateLimitFilter"));
            assertEquals(0, route.getFilters().size());
        }
    }

    @Nested
    @DisplayName("Rate Limit Tests")
    class RateLimitTests {

        @Test
        @DisplayName("Should update rate limit with valid value")
        void shouldUpdateRateLimitWithValidValue() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.updateRateLimit(500);

            assertEquals(500, route.getRateLimit());
        }

        @Test
        @DisplayName("Should throw when rate limit is non-positive")
        void shouldThrowWhenRateLimitIsNonPositive() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            assertThrows(ValidationException.class, () -> route.updateRateLimit(0));
            assertThrows(ValidationException.class, () -> route.updateRateLimit(-100));
        }
    }

    @Nested
    @DisplayName("Request Timeout Tests")
    class RequestTimeoutTests {

        @Test
        @DisplayName("Should update request timeout with valid value")
        void shouldUpdateRequestTimeoutWithValidValue() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.updateRequestTimeout(60);

            assertEquals(60, route.getRequestTimeout());
        }

        @Test
        @DisplayName("Should throw when timeout is out of range")
        void shouldThrowWhenTimeoutIsOutOfRange() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            assertThrows(ValidationException.class, () -> route.updateRequestTimeout(0));
            assertThrows(ValidationException.class, () -> route.updateRequestTimeout(400));
        }
    }

    @Nested
    @DisplayName("Circuit Breaker Tests")
    class CircuitBreakerTests {

        @Test
        @DisplayName("Should record failure and trip circuit breaker after threshold")
        void shouldRecordFailureAndTripCircuitBreaker() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            for (int i = 0; i < 5; i++) {
                route.recordFailure();
            }

            assertTrue(route.isCircuitBreakerOpen());
            assertEquals(5L, route.getCircuitBreakerFailureCount());
        }

        @Test
        @DisplayName("Should reset circuit breaker")
        void shouldResetCircuitBreaker() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            for (int i = 0; i < 5; i++) {
                route.recordFailure();
            }

            route.resetCircuitBreaker();

            assertFalse(route.isCircuitBreakerOpen());
            assertEquals(0L, route.getCircuitBreakerFailureCount());
        }

        @Test
        @DisplayName("Should record success and reset failure count")
        void shouldRecordSuccessAndResetFailureCount() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);
            route.recordFailure();
            route.recordFailure();

            route.recordSuccess();

            assertFalse(route.isCircuitBreakerOpen());
            assertEquals(0L, route.getCircuitBreakerFailureCount());
        }
    }

    @Nested
    @DisplayName("Target URL Management Tests")
    class TargetUrlManagementTests {

        @Test
        @DisplayName("Should add target URL")
        void shouldAddTargetUrl() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.addTargetUrl("http://localhost:8082");

            assertTrue(route.getTargetUrls().contains("http://localhost:8082"));
        }

        @Test
        @DisplayName("Should not add duplicate target URL")
        void shouldNotAddDuplicateTargetUrl() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            route.addTargetUrl("http://localhost:8080");

            assertEquals(2, route.getTargetUrls().size());
        }

        @Test
        @DisplayName("Should remove target URL")
        void shouldRemoveTargetUrl() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, List.of("url1", "url2"));

            route.removeTargetUrl("url1");

            assertEquals(1, route.getTargetUrls().size());
            assertFalse(route.getTargetUrls().contains("url1"));
        }

        @Test
        @DisplayName("Should throw when removing last URL from active route")
        void shouldThrowWhenRemovingLastUrlFromActiveRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, List.of("url1"));
            route.activate();

            assertThrows(IllegalStateException.class, () -> route.removeTargetUrl("url1"));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid route")
        void shouldValidateValidRoute() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, PATH, TARGET_SERVICE, TARGET_URLS);

            assertDoesNotThrow(route::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            GatewayRoute route = new GatewayRoute("   ", PATH, TARGET_SERVICE, TARGET_URLS);

            assertThrows(ValidationException.class, route::validate);
        }

        @Test
        @DisplayName("Should throw when path is blank")
        void shouldThrowWhenPathIsBlank() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, "   ", TARGET_SERVICE, TARGET_URLS);

            assertThrows(ValidationException.class, route::validate);
        }

        @Test
        @DisplayName("Should throw when path does not start with /")
        void shouldThrowWhenPathDoesNotStartWithSlash() {
            GatewayRoute route = new GatewayRoute(TENANT_ID, "api/v1/test", TARGET_SERVICE, TARGET_URLS);

            assertThrows(ValidationException.class, route::validate);
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build route using builder")
        void shouldBuildRouteUsingBuilder() {
            GatewayRoute route = GatewayRoute.builder()
                    .tenantId(TENANT_ID)
                    .path(PATH)
                    .targetService(TARGET_SERVICE)
                    .targetUrls(TARGET_URLS)
                    .status(RouteStatus.ACTIVE)
                    .rateLimit(500)
                    .loadBalancingStrategy(LoadBalancingStrategy.LEAST_CONNECTIONS)
                    .build();

            assertEquals(TENANT_ID, route.getTenantId());
            assertEquals(PATH, route.getPath());
            assertEquals(TARGET_SERVICE, route.getTargetService());
            assertEquals(RouteStatus.ACTIVE, route.getStatus());
            assertEquals(500, route.getRateLimit());
            assertEquals(LoadBalancingStrategy.LEAST_CONNECTIONS, route.getLoadBalancingStrategy());
        }
    }
}
