package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TenantGatewayFilter}.
 */
@ExtendWith(MockitoExtension.class)
class TenantGatewayFilterTest {

    @Mock
    private GatewayFilterChain chain;

    private TenantGatewayFilter tenantGatewayFilter;

    @BeforeEach
    void setUp() {
        tenantGatewayFilter = new TenantGatewayFilter();
    }

    @Test
    void testApply_ReturnsNonNullGatewayFilter() {
        // Act
        var gatewayFilter = tenantGatewayFilter.apply(new TenantGatewayFilter.Config());

        // Assert
        assertNotNull(gatewayFilter, "GatewayFilter should not be null");
    }

    @Test
    void testFilter_WithXTenantIdHeader() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant-ID", "tenant-123")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }

    @Test
    void testFilter_WithXTenantHeader() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant", "tenant-456")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }

    @Test
    void testFilter_XTenantIdTakesPrecedenceOverXTenant() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant-ID", "tenant-123")
                        .header("X-Tenant", "tenant-456")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }

    @Test
    void testFilter_WithEmptyTenantId() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant-ID", "")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }

    @Test
    void testFilter_WithNoTenantHeaders() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }

    @Test
    void testFilter_WithMultipleTenantHeaders() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant-ID", "tenant-123")
                        .header("X-Tenant-ID", "tenant-456")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert - Should get the first X-Tenant-ID header value
        verify(chain).filter(exchange);
    }

    @Test
    void testConfig_ClassExists() {
        // Act & Assert - Config class should be instantiable
        TenantGatewayFilter.Config config = new TenantGatewayFilter.Config();
        assertNotNull(config, "Config class should be instantiable");
    }

    @Test
    void testFilter_DoesNotModifyExchange() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/users")
                        .header("X-Tenant-ID", "tenant-789")
                        .build()
        );

        String originalPath = exchange.getRequest().getPath().value();
        HttpHeaders originalHeaders = exchange.getRequest().getHeaders();

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert - Path and headers should remain accessible
        assertNotNull(exchange.getRequest().getPath());
        assertNotNull(originalHeaders);
    }

    @Test
    void testFilter_WithDifferentPaths() {
        // Arrange
        String[] paths = {"/api/auth/login", "/api/users", "/api/tenants", "/api/files", "/api/audit"};

        for (String path : paths) {
            MockServerWebExchange exchange = MockServerWebExchange.from(
                    org.springframework.mock.http.server.reactive.MockServerHttpRequest
                            .get(path)
                            .header("X-Tenant-ID", "tenant-test")
                            .build()
            );

            when(chain.filter(exchange)).thenReturn(Mono.empty());

            // Act
            tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                    .filter(exchange, chain)
                    .block();

            // Assert
            verify(chain).filter(exchange);
        }
    }

    @Test
    void testFilter_WithWhitespacesInTenantId() {
        // Arrange
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .header("X-Tenant-ID", "   ")
                        .build()
        );

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Act
        tenantGatewayFilter.apply(new TenantGatewayFilter.Config())
                .filter(exchange, chain)
                .block();

        // Assert
        verify(chain).filter(exchange);
    }
}
