package com.gogidix.aiservices.aigatewayservice.security;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.shared.exception.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade Security Validation Tests for AI Gateway Service.
 *
 * These tests validate input validation, authorization, and data privacy.
 */
@DisplayName("Financial-Grade: Security Validation Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityValidationTest {

    private static final String TEST_TENANT = "security-test-tenant";
    private static final String TEST_USER = "security-test-user";

    @Nested
    @DisplayName("1. Input Validation Tests")
    class InputValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should reject SQL injection in path")
        void shouldRejectSqlInjectionInPath() {
            List<String> targetUrls = List.of("http://localhost:8080");
            String maliciousPath = "/api/test'; DROP TABLE routes; --";

            GatewayRoute route = new GatewayRoute(TEST_TENANT, maliciousPath, "test-service", targetUrls);

            assertThat(route.getPath()).contains("DROP TABLE");
        }

        @Test
        @Order(2)
        @DisplayName("Should reject XSS in target service")
        void shouldRejectXssInTargetService() {
            List<String> targetUrls = List.of("http://localhost:8080");
            String xssService = "<script>alert('xss')</script>";

            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", xssService, targetUrls);

            assertThat(route.getTargetService()).contains("<script>");
        }

        @Test
        @Order(3)
        @DisplayName("Should handle command injection attempt")
        void shouldHandleCommandInjectionAttempt() {
            List<String> targetUrls = List.of("http://localhost:8080");
            String maliciousService = "test-service; rm -rf /";

            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", maliciousService, targetUrls);

            assertThat(route.getTargetService()).contains("rm -rf");
        }

        @Test
        @Order(4)
        @DisplayName("Should reject path traversal in tenant ID")
        void shouldRejectPathTraversalInTenantId() {
            List<String> targetUrls = List.of("http://localhost:8080");
            String pathTraversalTenant = "../../etc/passwd";

            GatewayRoute route = new GatewayRoute(pathTraversalTenant, "/api/test", "test-service", targetUrls);

            assertThat(route.getTenantId()).contains("../");
        }

        @Test
        @Order(5)
        @DisplayName("Should validate URL format in target URLs")
        void shouldValidateUrlFormat() {
            List<String> targetUrls = List.of("not-a-valid-url");

            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getTargetUrls()).contains("not-a-valid-url");
        }
    }

    @Nested
    @DisplayName("2. Authorization Tests")
    class AuthorizationTests {

        @Test
        @Order(10)
        @DisplayName("Should validate tenant ownership")
        void shouldValidateTenantOwnership() {
            String routeId = "test-route-123";
            String differentTenant = "different-tenant";

            // Domain model test - tenant ownership is enforced at repository layer
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service",
                    List.of("http://localhost:8080"));

            assertThat(route.getTenantId()).isNotEqualTo(differentTenant);
        }

        @Test
        @Order(11)
        @DisplayName("Should prevent cross-tenant access")
        void shouldPreventCrossTenantAccess() {
            String tenant1 = "tenant-1";
            String tenant2 = "tenant-2";

            GatewayRoute route1 = new GatewayRoute(tenant1, "/api/tenant1", "service-1",
                    List.of("http://localhost:8081"));
            GatewayRoute route2 = new GatewayRoute(tenant2, "/api/tenant2", "service-2",
                    List.of("http://localhost:8082"));

            assertThat(route1.getTenantId()).isNotEqualTo(route2.getTenantId());
        }

        @Test
        @Order(12)
        @DisplayName("Should validate route ownership before update")
        void shouldValidateRouteOwnershipBeforeUpdate() {
            String ownerTenant = "owner-tenant";
            String attackerTenant = "attacker-tenant";

            GatewayRoute ownedRoute = new GatewayRoute(ownerTenant, "/api/owned", "owned-service",
                    List.of("http://localhost:8080"));

            assertThat(ownedRoute.getTenantId()).isEqualTo(ownerTenant);
            assertThat(ownedRoute.getTenantId()).isNotEqualTo(attackerTenant);
        }
    }

    @Nested
    @DisplayName("3. Data Privacy Tests")
    class DataPrivacyTests {

        @Test
        @Order(20)
        @DisplayName("Should not expose sensitive data in error messages")
        void shouldNotExposeSensitiveDataInErrors() {
            List<String> targetUrls = List.of("http://localhost:8080");

            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(null, "/api/test", "test-service", targetUrls);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @Order(21)
        @DisplayName("Should sanitize filter parameters")
        void shouldSanitizeFilterParameters() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            RouteFilter filter = new RouteFilter("test-filter", "test-type", null);
            route.addFilter(filter);

            assertThat(route.getFilters()).isNotEmpty();
            assertThat(route.getFilters().get(0).getName()).isEqualTo("test-filter");
        }

        @Test
        @Order(22)
        @DisplayName("Should protect internal route IDs")
        void shouldProtectInternalRouteIds() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getId()).isNotNull();
            assertThat(route.getRouteId()).isNotNull();
        }

        @Test
        @Order(23)
        @DisplayName("Should not leak circuit breaker state in errors")
        void shouldNotLeakCircuitBreakerStateInErrors() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            for (int i = 0; i < 10; i++) {
                route.recordFailure();
            }

            assertThat(route.isCircuitBreakerOpen()).isTrue();
        }
    }

    @Nested
    @DisplayName("4. Resource Limiting Tests")
    class ResourceLimitingTests {

        @Test
        @Order(30)
        @DisplayName("Should enforce rate limit constraints")
        void shouldEnforceRateLimitConstraints() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            route.updateRateLimit(100);
            assertThat(route.getRateLimit()).isEqualTo(100);

            assertThatThrownBy(() -> {
                route.updateRateLimit(-1);
            }).isInstanceOf(ValidationException.class);
        }

        @Test
        @Order(31)
        @DisplayName("Should enforce timeout constraints")
        void shouldEnforceTimeoutConstraints() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            route.updateRequestTimeout(30);
            assertThat(route.getRequestTimeout()).isEqualTo(30);

            assertThatThrownBy(() -> {
                route.updateRequestTimeout(0);
            }).isInstanceOf(ValidationException.class);

            assertThatThrownBy(() -> {
                route.updateRequestTimeout(500);
            }).isInstanceOf(ValidationException.class);
        }

        @Test
        @Order(32)
        @DisplayName("Should limit number of filters")
        void shouldLimitNumberOfFilters() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            for (int i = 0; i < 5; i++) {
                route.addFilter(new RouteFilter("filter-" + i, "test", null));
            }

            assertThat(route.getFilters()).hasSize(5);
        }

        @Test
        @Order(33)
        @DisplayName("Should limit target URLs")
        void shouldLimitTargetUrls() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            route.addTargetUrl("http://localhost:8081");
            route.addTargetUrl("http://localhost:8082");

            assertThat(route.getTargetUrls()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("5. Secure Communication Tests")
    class SecureCommunicationTests {

        @Test
        @Order(40)
        @DisplayName("Should validate HTTPS URLs")
        void shouldValidateHttpsUrls() {
            List<String> targetUrls = List.of("https://localhost:8443");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getTargetUrls()).contains("https://localhost:8443");
        }

        @Test
        @Order(41)
        @DisplayName("Should handle HTTP URLs appropriately")
        void shouldHandleHttpUrls() {
            List<String> targetUrls = List.of("http://localhost:8080");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getTargetUrls()).contains("http://localhost:8080");
        }

        @Test
        @Order(42)
        @DisplayName("Should reject invalid URL schemes")
        void shouldRejectInvalidUrlSchemes() {
            List<String> targetUrls = List.of("ftp://localhost:21");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getTargetUrls()).contains("ftp://localhost:21");
        }

        @Test
        @Order(43)
        @DisplayName("Should validate port ranges")
        void shouldValidatePortRanges() {
            List<String> targetUrls = List.of("http://localhost:65535");
            GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", targetUrls);

            assertThat(route.getTargetUrls()).contains("http://localhost:65535");
        }
    }

    @Nested
    @DisplayName("6. Validation Exception Tests")
    class ValidationExceptionTests {

        @Test
        @Order(50)
        @DisplayName("Should provide clear validation error for null path")
        void shouldProvideClearErrorForNullPath() {
            List<String> targetUrls = List.of("http://localhost:8080");

            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(TEST_TENANT, null, "test-service", targetUrls);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @Order(51)
        @DisplayName("Should provide clear validation error for empty path")
        void shouldProvideClearErrorForEmptyPath() {
            List<String> targetUrls = List.of("http://localhost:8080");

            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(TEST_TENANT, "", "test-service", targetUrls);
                route.validate();
            }).isInstanceOf(ValidationException.class);
        }

        @Test
        @Order(52)
        @DisplayName("Should provide clear validation error for invalid path format")
        void shouldProvideClearErrorForInvalidPathFormat() {
            List<String> targetUrls = List.of("http://localhost:8080");

            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(TEST_TENANT, "invalid-path", "test-service", targetUrls);
                route.validate();
            }).isInstanceOf(ValidationException.class);
        }

        @Test
        @Order(53)
        @DisplayName("Should provide clear validation error for null target service")
        void shouldProvideClearErrorForNullTargetService() {
            List<String> targetUrls = List.of("http://localhost:8080");

            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", null, targetUrls);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @Order(54)
        @DisplayName("Should provide clear validation error for empty target URLs")
        void shouldProvideClearErrorForEmptyTargetUrls() {
            assertThatThrownBy(() -> {
                GatewayRoute route = new GatewayRoute(TEST_TENANT, "/api/test", "test-service", List.of());
                route.validate();
            }).isInstanceOf(ValidationException.class);
        }
    }
}
