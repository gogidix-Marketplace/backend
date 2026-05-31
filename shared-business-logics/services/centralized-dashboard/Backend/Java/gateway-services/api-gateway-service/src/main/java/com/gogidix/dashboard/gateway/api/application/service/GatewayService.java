package com.gogidix.dashboard.gateway.api.application.service;

import com.gogidix.dashboard.gateway.api.application.dto.response.AggregatedDashboardDataDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.AggregateResponseDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.ServiceHealthDto;
import com.gogidix.dashboard.gateway.api.domain.model.ServiceRegistry;
import com.gogidix.dashboard.gateway.api.domain.repository.ServiceRegistryRepository;
import com.gogidix.dashboard.gateway.api.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for handling gateway aggregation logic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

    private final WebClient.Builder webClientBuilder;
    private final ServiceRegistryRepository serviceRegistryRepository;

    @Value("${gateway.downstream.services.saga-coordinator.base-url:http://localhost:8080}")
    private String sagaCoordinatorUrl;

    @Value("${gateway.downstream.services.chart-service.base-url:http://localhost:8909}")
    private String chartServiceUrl;

    @Value("${gateway.downstream.services.websocket-gateway.base-url:http://localhost:8908}")
    private String websocketGatewayUrl;

    private static final int DEFAULT_TIMEOUT_MS = 5000;

    /**
     * Aggregate dashboard data from all downstream services
     */
    @Cacheable(value = "dashboard-data", key = "#tenantId", unless = "#result == null")
    public AggregatedDashboardDataDto getAggregatedDashboardData(String tenantId) {
        log.info("Aggregating dashboard data for tenant: {}", tenantId);

        Map<String, Object> sagaStatistics = fetchSagaStatistics();
        Map<String, Object> chartData = fetchChartData();
        Map<String, ServiceHealthDto> serviceHealth = checkAllServicesHealth();

        return AggregatedDashboardDataDto.builder()
                .sagaStatistics(sagaStatistics)
                .chartData(chartData)
                .serviceHealth(serviceHealth)
                .timestamp(LocalDateTime.now().toString())
                .tenantId(tenantId)
                .build();
    }

    /**
     * Fetch saga statistics from saga coordinator
     */
    public Map<String, Object> fetchSagaStatistics() {
        try {
            WebClient client = webClientBuilder.build();
            Map<String, Object> response = client.get()
                    .uri(sagaCoordinatorUrl + "/api/v1/sagas/statistics")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(5));

            log.debug("Fetched saga statistics: {}", response);
            return response != null ? response : Collections.emptyMap();
        } catch (Exception e) {
            log.error("Error fetching saga statistics", e);
            return Collections.singletonMap("error", "Failed to fetch saga statistics");
        }
    }

    /**
     * Fetch chart data from chart service
     */
    public Map<String, Object> fetchChartData() {
        try {
            WebClient client = webClientBuilder.build();
            Map<String, Object> response = client.get()
                    .uri(chartServiceUrl + "/api/v1/charts/summary")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(5));

            log.debug("Fetched chart data: {}", response);
            return response != null ? response : Collections.emptyMap();
        } catch (Exception e) {
            log.error("Error fetching chart data", e);
            return Collections.singletonMap("error", "Failed to fetch chart data");
        }
    }

    /**
     * Proxy request to downstream service
     */
    public AggregateResponseDto proxyRequest(String serviceName, String path, String method, Object body) {
        long startTime = System.currentTimeMillis();
        String tenantId = TenantContext.getTenantId();

        ServiceRegistry service = serviceRegistryRepository
                .findByServiceNameAndTenantId(serviceName, tenantId)
                .orElse(null);

        if (service == null || !service.getEnabled()) {
            return AggregateResponseDto.builder()
                    .serviceName(serviceName)
                    .endpoint(path)
                    .success(false)
                    .error("Service not found or disabled")
                    .statusCode(503)
                    .tenantId(tenantId)
                    .timestamp(LocalDateTime.now())
                    .durationMs(System.currentTimeMillis() - startTime)
                    .build();
        }

        try {
            WebClient client = webClientBuilder
                    .baseUrl(service.getBaseUrl())
                    .build();

            WebClient.ResponseSpec responseSpec = switch (method.toUpperCase()) {
                case "GET" -> client.get().uri(path).retrieve();
                case "POST" -> client.post().uri(path).bodyValue(body).retrieve();
                case "PUT" -> client.put().uri(path).bodyValue(body).retrieve();
                case "DELETE" -> client.delete().uri(path).retrieve();
                default -> client.get().uri(path).retrieve();
            };

            Object data = responseSpec.bodyToMono(Object.class).block(Duration.ofMillis(service.getTimeoutMs()));

            return AggregateResponseDto.builder()
                    .serviceName(serviceName)
                    .endpoint(path)
                    .data(data)
                    .success(true)
                    .statusCode(200)
                    .tenantId(tenantId)
                    .timestamp(LocalDateTime.now())
                    .durationMs(System.currentTimeMillis() - startTime)
                    .build();

        } catch (WebClientResponseException e) {
            log.error("Error proxying request to {} {}: {}", serviceName, path, e.getMessage());
            return AggregateResponseDto.builder()
                    .serviceName(serviceName)
                    .endpoint(path)
                    .success(false)
                    .error(e.getMessage())
                    .statusCode(e.getStatusCode().value())
                    .tenantId(tenantId)
                    .timestamp(LocalDateTime.now())
                    .durationMs(System.currentTimeMillis() - startTime)
                    .build();
        } catch (Exception e) {
            log.error("Unexpected error proxying request to {} {}", serviceName, path, e);
            return AggregateResponseDto.builder()
                    .serviceName(serviceName)
                    .endpoint(path)
                    .success(false)
                    .error("Internal gateway error")
                    .statusCode(500)
                    .tenantId(tenantId)
                    .timestamp(LocalDateTime.now())
                    .durationMs(System.currentTimeMillis() - startTime)
                    .build();
        }
    }

    /**
     * Check health of all registered services
     */
    public Map<String, ServiceHealthDto> checkAllServicesHealth() {
        String tenantId = TenantContext.getTenantId();
        List<ServiceRegistry> services = serviceRegistryRepository
                .findByTenantIdAndEnabledTrue(tenantId);

        Map<String, ServiceHealthDto> healthMap = new HashMap<>();

        // Add default services
        healthMap.put("saga-coordinator", checkServiceHealth("saga-coordinator", sagaCoordinatorUrl + "/actuator/health"));
        healthMap.put("chart-service", checkServiceHealth("chart-service", chartServiceUrl + "/actuator/health"));
        healthMap.put("websocket-gateway", checkServiceHealth("websocket-gateway", websocketGatewayUrl + "/actuator/health"));

        // Add registered services
        for (ServiceRegistry service : services) {
            String healthUrl = service.getBaseUrl() + (service.getHealthCheckUrl() != null ? service.getHealthCheckUrl() : "/actuator/health");
            healthMap.put(service.getServiceName(), checkServiceHealth(service.getServiceName(), healthUrl));
        }

        return healthMap;
    }

    /**
     * Check health of a single service
     */
    private ServiceHealthDto checkServiceHealth(String serviceName, String healthUrl) {
        long startTime = System.currentTimeMillis();

        try {
            WebClient client = webClientBuilder.build();
            Map<String, Object> response = client.get()
                    .uri(healthUrl)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(3));

            return ServiceHealthDto.builder()
                    .serviceName(serviceName)
                    .status(ServiceHealthDto.Status.UP.name())
                    .lastChecked(LocalDateTime.now())
                    .responseTimeMs(System.currentTimeMillis() - startTime)
                    .details(response)
                    .build();

        } catch (Exception e) {
            return ServiceHealthDto.builder()
                    .serviceName(serviceName)
                    .status(ServiceHealthDto.Status.DOWN.name())
                    .lastChecked(LocalDateTime.now())
                    .responseTimeMs(System.currentTimeMillis() - startTime)
                    .details(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    /**
     * Register a new service
     */
    public ServiceRegistry registerService(ServiceRegistry serviceRegistry) {
        String tenantId = TenantContext.getTenantId();
        serviceRegistry.setTenantId(tenantId);
        return serviceRegistryRepository.save(serviceRegistry);
    }

    /**
     * Get all services for current tenant
     */
    public List<ServiceRegistry> getAllServices() {
        String tenantId = TenantContext.getTenantId();
        return serviceRegistryRepository.findByTenantIdAndEnabledTrue(tenantId);
    }
}
