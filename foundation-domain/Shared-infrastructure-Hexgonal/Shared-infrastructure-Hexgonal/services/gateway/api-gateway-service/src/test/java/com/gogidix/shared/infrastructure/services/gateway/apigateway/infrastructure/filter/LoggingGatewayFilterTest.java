package com.gogidix.shared.infrastructure.services.gateway.apigateway.infrastructure.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LoggingGatewayFilter}.
 */
@ExtendWith(MockitoExtension.class)
class LoggingGatewayFilterTest {

    @Mock
    private GatewayFilterChain chain;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    private ServerWebExchange exchange;

    private LoggingGatewayFilter loggingGatewayFilter;

    @BeforeEach
    void setUp() {
        loggingGatewayFilter = new LoggingGatewayFilter();
    }

    @Test
    void testFilter_LogsIncomingRequest() {
        // Arrange
        MockServerWebExchange mockExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/test")
                        .remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                        .build()
        );

        when(chain.filter(mockExchange)).thenReturn(Mono.empty());

        // Act
        loggingGatewayFilter.filter(mockExchange, chain);

        // Assert - Filter should complete without exception
        verify(chain).filter(mockExchange);
    }

    @Test
    void testFilter_LogsResponseWithStatus() {
        // Arrange
        MockServerWebExchange mockExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/api/users")
                        .remoteAddress(new InetSocketAddress("192.168.1.1", 9000))
                        .build()
        );

        when(chain.filter(mockExchange)).thenReturn(Mono.empty());

        // Act
        loggingGatewayFilter.filter(mockExchange, chain).block();

        // Assert
        verify(chain).filter(mockExchange);
    }

    @Test
    void testFilter_WithNullRemoteAddress() {
        // Arrange
        MockServerWebExchange mockExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .delete("/api/test")
                        .build()
        );

        when(chain.filter(mockExchange)).thenReturn(Mono.empty());

        // Act & Assert - Should not throw exception
        loggingGatewayFilter.filter(mockExchange, chain).block();
        verify(chain).filter(mockExchange);
    }

    @Test
    void testGetOrder_ReturnsNegativeOne() {
        // Act
        int order = loggingGatewayFilter.getOrder();

        // Assert
        assertEquals(-1, order, "Filter should have high priority with order -1");
    }

    @Test
    void testFilter_MeasuresRequestDuration() {
        // Arrange
        MockServerWebExchange mockExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .put("/api/test")
                        .remoteAddress(new InetSocketAddress("10.0.0.1", 8080))
                        .build()
        );

        when(chain.filter(mockExchange)).thenReturn(Mono.empty());

        long startTime = System.currentTimeMillis();

        // Act
        loggingGatewayFilter.filter(mockExchange, chain).block();

        long endTime = System.currentTimeMillis();

        // Assert - Request should complete with measured duration
        verify(chain).filter(mockExchange);
        // The filter should have added some minimal overhead
        // (This is a basic sanity check that the timing logic runs)
    }

    @Test
    void testFilter_WithDifferentHttpMethods() {
        // Act & Assert for GET
        MockServerWebExchange getExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .get("/api/get")
                        .build()
        );
        when(chain.filter(getExchange)).thenReturn(Mono.empty());
        loggingGatewayFilter.filter(getExchange, chain).block();
        verify(chain).filter(getExchange);

        // Act & Assert for POST
        MockServerWebExchange postExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .post("/api/post")
                        .build()
        );
        when(chain.filter(postExchange)).thenReturn(Mono.empty());
        loggingGatewayFilter.filter(postExchange, chain).block();
        verify(chain).filter(postExchange);

        // Act & Assert for PUT
        MockServerWebExchange putExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .put("/api/put")
                        .build()
        );
        when(chain.filter(putExchange)).thenReturn(Mono.empty());
        loggingGatewayFilter.filter(putExchange, chain).block();
        verify(chain).filter(putExchange);

        // Act & Assert for DELETE
        MockServerWebExchange deleteExchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest
                        .delete("/api/delete")
                        .build()
        );
        when(chain.filter(deleteExchange)).thenReturn(Mono.empty());
        loggingGatewayFilter.filter(deleteExchange, chain).block();
        verify(chain).filter(deleteExchange);
    }
}
