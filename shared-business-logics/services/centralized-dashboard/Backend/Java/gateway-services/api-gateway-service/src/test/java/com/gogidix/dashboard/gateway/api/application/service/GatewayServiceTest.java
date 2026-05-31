package com.gogidix.dashboard.gateway.api.application.service;

import com.gogidix.dashboard.gateway.api.application.dto.response.AggregatedDashboardDataDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.AggregateResponseDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.ServiceHealthDto;
import com.gogidix.dashboard.gateway.api.domain.model.ServiceRegistry;
import com.gogidix.dashboard.gateway.api.domain.repository.ServiceRegistryRepository;
import com.gogidix.dashboard.gateway.api.infrastructure.security.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private ServiceRegistryRepository serviceRegistryRepository;

    @InjectMocks
    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        TenantContext.clear();
        ReflectionTestUtils.setField(gatewayService, "sagaCoordinatorUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(gatewayService, "chartServiceUrl", "http://localhost:8909");
        ReflectionTestUtils.setField(gatewayService, "websocketGatewayUrl", "http://localhost:8908");
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Nested
    @DisplayName("proxyRequest tests")
    class ProxyRequestTests {

        private ServiceRegistry createEnabledService() {
            return ServiceRegistry.builder()
                    .serviceName("svc")
                    .baseUrl("http://localhost")
                    .enabled(true)
                    .timeoutMs(5000)
                    .build();
        }

        @Test
        void proxyRequest_serviceNotFound_returns503() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("missing", "t1"))
                    .thenReturn(Optional.empty());

            AggregateResponseDto response = gatewayService.proxyRequest("missing", "/api/test", "GET", null);

            assertFalse(response.getSuccess());
            assertEquals(503, response.getStatusCode());
            assertTrue(response.getError().contains("not found"));
        }

        @Test
        void proxyRequest_serviceDisabled_returns503() {
            TenantContext.setTenantId("t1");
            ServiceRegistry disabled = ServiceRegistry.builder()
                    .serviceName("svc")
                    .baseUrl("http://localhost")
                    .enabled(false)
                    .build();
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(disabled));

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "GET", null);

            assertFalse(response.getSuccess());
            assertEquals(503, response.getStatusCode());
        }

        @Test
        void proxyRequest_GET_success() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));
            setupWebClientMockForGet();

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "GET", null);

            assertTrue(response.getSuccess());
            assertEquals(200, response.getStatusCode());
            assertNotNull(response.getDurationMs());
        }

        @Test
        void proxyRequest_POST_success() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));
            setupWebClientMockForPost();

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "POST", Map.of("key", "val"));

            assertTrue(response.getSuccess());
            assertEquals(200, response.getStatusCode());
        }

        @Test
        void proxyRequest_PUT_success() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));
            setupWebClientMockForPut();

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "PUT", Map.of("key", "val"));

            assertTrue(response.getSuccess());
            assertEquals(200, response.getStatusCode());
        }

        @Test
        void proxyRequest_DELETE_success() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));
            setupWebClientMockForDelete();

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "DELETE", null);

            assertTrue(response.getSuccess());
            assertEquals(200, response.getStatusCode());
        }

        @Test
        void proxyRequest_defaultMethod_usesGet() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));
            setupWebClientMockForGet();

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "PATCH", null);

            assertTrue(response.getSuccess());
            assertEquals(200, response.getStatusCode());
        }

        @Test
        void proxyRequest_webClientResponseException_returnsUpstreamError() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));

            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(
                    WebClientResponseException.create(502, "Bad Gateway",
                            HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8)));

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "GET", null);

            assertFalse(response.getSuccess());
            assertEquals(502, response.getStatusCode());
            assertNotNull(response.getError());
        }

        @Test
        void proxyRequest_genericException_returns500() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByServiceNameAndTenantId("svc", "t1"))
                    .thenReturn(Optional.of(createEnabledService()));

            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(new RuntimeException("Connection timeout")));

            AggregateResponseDto response = gatewayService.proxyRequest("svc", "/api/test", "GET", null);

            assertFalse(response.getSuccess());
            assertEquals(500, response.getStatusCode());
            assertEquals("Internal gateway error", response.getError());
        }

        private void setupWebClientMockForGet() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(Map.of("result", "ok")));
        }

        private void setupWebClientMockForPost() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestBodyUriSpec bodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
            WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.post()).thenReturn(bodyUriSpec);
            when(bodyUriSpec.uri(anyString())).thenReturn(bodySpec);
            when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(Map.of("result", "created")));
        }

        private void setupWebClientMockForPut() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestBodyUriSpec bodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
            WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.put()).thenReturn(bodyUriSpec);
            when(bodyUriSpec.uri(anyString())).thenReturn(bodySpec);
            when(bodySpec.bodyValue(any())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(Map.of("result", "updated")));
        }

        private void setupWebClientMockForDelete() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.delete()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(Map.of("result", "deleted")));
        }
    }

    @Nested
    @DisplayName("fetchSagaStatistics tests")
    class FetchSagaStatisticsTests {
        @Test
        void fetchSagaStatistics_success_returnsMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("total", 50)));

            Map<String, Object> result = gatewayService.fetchSagaStatistics();
            assertNotNull(result);
            assertEquals(50, result.get("total"));
        }

        @Test
        void fetchSagaStatistics_nullResponse_returnsEmptyMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

            Map<String, Object> result = gatewayService.fetchSagaStatistics();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void fetchSagaStatistics_error_returnsErrorMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("Connection refused")));

            Map<String, Object> result = gatewayService.fetchSagaStatistics();
            assertNotNull(result);
            assertTrue(result.containsKey("error"));
        }
    }

    @Nested
    @DisplayName("fetchChartData tests")
    class FetchChartDataTests {
        @Test
        void fetchChartData_success_returnsMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("charts", 10)));

            Map<String, Object> result = gatewayService.fetchChartData();
            assertNotNull(result);
            assertEquals(10, result.get("charts"));
        }

        @Test
        void fetchChartData_nullResponse_returnsEmptyMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.empty());

            Map<String, Object> result = gatewayService.fetchChartData();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void fetchChartData_error_returnsErrorMap() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("Connection refused")));

            Map<String, Object> result = gatewayService.fetchChartData();
            assertNotNull(result);
            assertTrue(result.containsKey("error"));
        }
    }

    @Nested
    @DisplayName("checkAllServicesHealth tests")
    class CheckAllHealthTests {
        @Test
        void checkAllServicesHealth_noRegisteredServices_returns3Default() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of());

            setupErrorWebClient();

            Map<String, ServiceHealthDto> result = gatewayService.checkAllServicesHealth();
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.containsKey("saga-coordinator"));
            assertTrue(result.containsKey("chart-service"));
            assertTrue(result.containsKey("websocket-gateway"));
        }

        @Test
        void checkAllServicesHealth_withRegisteredServiceWithHealthCheckUrl() {
            TenantContext.setTenantId("t1");
            ServiceRegistry registered = ServiceRegistry.builder()
                    .serviceName("custom-svc")
                    .baseUrl("http://custom:8080")
                    .healthCheckUrl("/custom/health")
                    .enabled(true)
                    .build();
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of(registered));

            setupErrorWebClient();

            Map<String, ServiceHealthDto> result = gatewayService.checkAllServicesHealth();
            assertEquals(4, result.size());
            assertTrue(result.containsKey("custom-svc"));
            assertEquals("DOWN", result.get("custom-svc").getStatus());
        }

        @Test
        void checkAllServicesHealth_withRegisteredServiceNullHealthCheckUrl() {
            TenantContext.setTenantId("t1");
            ServiceRegistry registered = ServiceRegistry.builder()
                    .serviceName("custom-svc2")
                    .baseUrl("http://custom:8081")
                    .healthCheckUrl(null)
                    .enabled(true)
                    .build();
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of(registered));

            setupErrorWebClient();

            Map<String, ServiceHealthDto> result = gatewayService.checkAllServicesHealth();
            assertEquals(4, result.size());
            assertTrue(result.containsKey("custom-svc2"));
        }

        @Test
        void checkAllServicesHealth_serviceUp_returnsUpStatus() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of());

            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("status", "UP")));

            Map<String, ServiceHealthDto> result = gatewayService.checkAllServicesHealth();
            for (ServiceHealthDto health : result.values()) {
                assertEquals("UP", health.getStatus());
            }
        }

        private void setupErrorWebClient() {
            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.error(new RuntimeException("down")));
        }
    }

    @Nested
    @DisplayName("registerService tests")
    class RegisterServiceTests {
        @Test
        void registerService_setsTenantIdAndSaves() {
            TenantContext.setTenantId("t1");
            ServiceRegistry registry = ServiceRegistry.builder()
                    .serviceName("new-svc")
                    .baseUrl("http://localhost:8080")
                    .build();
            when(serviceRegistryRepository.save(any())).thenReturn(registry);

            ServiceRegistry result = gatewayService.registerService(registry);

            assertEquals("t1", registry.getTenantId());
            verify(serviceRegistryRepository).save(registry);
        }
    }

    @Nested
    @DisplayName("getAllServices tests")
    class GetAllServicesTests {
        @Test
        void getAllServices_returnsServicesForTenant() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of());

            List<ServiceRegistry> services = gatewayService.getAllServices();
            assertNotNull(services);
            verify(serviceRegistryRepository).findByTenantIdAndEnabledTrue("t1");
        }
    }

    @Nested
    @DisplayName("getAggregatedDashboardData tests")
    class GetAggregatedDashboardDataTests {
        @Test
        void getAggregatedDashboardData_returnsData() {
            TenantContext.setTenantId("t1");
            when(serviceRegistryRepository.findByTenantIdAndEnabledTrue("t1"))
                    .thenReturn(List.of());

            WebClient mockClient = mock(WebClient.class);
            WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
            WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
            WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

            when(webClientBuilder.build()).thenReturn(mockClient);
            when(mockClient.get()).thenReturn(uriSpec);
            when(uriSpec.uri(anyString())).thenReturn(headersSpec);
            when(headersSpec.retrieve()).thenReturn(responseSpec);
            when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("data", "value")));

            AggregatedDashboardDataDto result = gatewayService.getAggregatedDashboardData("t1");

            assertNotNull(result);
            assertEquals("t1", result.getTenantId());
            assertNotNull(result.getTimestamp());
            assertNotNull(result.getSagaStatistics());
            assertNotNull(result.getChartData());
            assertNotNull(result.getServiceHealth());
        }
    }
}
