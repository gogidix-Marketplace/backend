package com.gogidix.aiservices.aimonitoringservice.application.service;

import com.gogidix.aiservices.aimonitoringservice.application.dto.ServiceHealthResponseDto;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;
import com.gogidix.aiservices.aimonitoringservice.domain.repository.ServiceHealthRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceHealthApplicationService Tests")
class ServiceHealthApplicationServiceTest {

    @Mock
    private ServiceHealthRepository repository;

    @InjectMocks
    private ServiceHealthApplicationService service;

    private static final String TENANT_ID = "tenant-123";
    private static final String SERVICE_NAME = "ai-model-service";

    @Nested
    @DisplayName("getServiceHealth() Tests")
    class GetServiceHealthTests {

        @Test
        @DisplayName("Should return existing service health")
        void shouldReturnExistingServiceHealth() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            health.updateStatus(ServiceHealth.HealthStatus.UP, "Service is healthy");

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));

            ServiceHealthResponseDto result = service.getServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.serviceName()).isEqualTo(SERVICE_NAME);
            assertThat(result.status()).isEqualTo(ServiceHealth.HealthStatus.UP);
            assertThat(result.message()).isEqualTo("Service is healthy");

            verify(repository).findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID);
            verify(repository, never()).save(any(ServiceHealth.class));
        }

        @Test
        @DisplayName("Should create new service health when not exists")
        void shouldCreateNewServiceHealthWhenNotExists() {
            ServiceHealth newHealth = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.empty());
            when(repository.save(any(ServiceHealth.class))).thenReturn(newHealth);

            ServiceHealthResponseDto result = service.getServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.serviceName()).isEqualTo(SERVICE_NAME);
            assertThat(result.status()).isEqualTo(ServiceHealth.HealthStatus.UNKNOWN);

            verify(repository).findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID);
            verify(repository).save(any(ServiceHealth.class));
        }

        @Test
        @DisplayName("Should return health with metrics")
        void shouldReturnHealthWithMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);
            health.updateStatus(ServiceHealth.HealthStatus.UP, "OK");
            health.updateMetrics(Map.of("cpu", 50.0, "memory", 75.0));

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));

            ServiceHealthResponseDto result = service.getServiceHealth(SERVICE_NAME, TENANT_ID);

            assertThat(result).isNotNull();
            assertThat(result.metrics()).isNotNull();
            assertThat(result.metrics()).hasSize(2);
            assertThat(result.metrics().get("cpu")).isEqualTo(50.0);
        }
    }

    @Nested
    @DisplayName("updateServiceHealth() Tests")
    class UpdateServiceHealthTests {

        @Test
        @DisplayName("Should update existing service health")
        void shouldUpdateExistingServiceHealth() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            Map<String, Object> metrics = Map.of("cpu", 80.0, "memory", 60.0);
            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP,
                    "Service is running",
                    metrics
            );

            assertThat(result).isNotNull();
            assertThat(result.status()).isEqualTo(ServiceHealth.HealthStatus.UP);
            assertThat(result.message()).isEqualTo("Service is running");
            assertThat(result.metrics()).isEqualTo(metrics);

            verify(repository).save(any(ServiceHealth.class));
        }

        @Test
        @DisplayName("Should create and update when service health not exists")
        void shouldCreateAndUpdateWhenServiceHealthNotExists() {
            ServiceHealth newHealth = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.empty());
            when(repository.save(any(ServiceHealth.class))).thenReturn(newHealth);

            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP,
                    "Created",
                    null
            );

            assertThat(result).isNotNull();
            verify(repository).save(any(ServiceHealth.class));
        }

        @Test
        @DisplayName("Should update status to DOWN")
        void shouldUpdateStatusToDown() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.DOWN,
                    "Service is down",
                    null
            );

            assertThat(result.status()).isEqualTo(ServiceHealth.HealthStatus.DOWN);
            assertThat(result.message()).isEqualTo("Service is down");
            assertThat(health.getTotalChecks()).isEqualTo(1L);
            assertThat(health.getFailedChecks()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should update status to DEGRADED")
        void shouldUpdateStatusToDegraded() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.DEGRADED,
                    "High latency",
                    null
            );

            assertThat(result.status()).isEqualTo(ServiceHealth.HealthStatus.DEGRADED);
            assertThat(result.message()).isEqualTo("High latency");
            assertThat(health.getFailedChecks()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should update with null metrics")
        void shouldUpdateWithNullMetrics() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP,
                    "OK",
                    null
            );

            assertThat(result).isNotNull();
            assertThat(result.metrics()).isNull();
        }

        @Test
        @DisplayName("Should update metrics when provided")
        void shouldUpdateMetricsWhenProvided() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            Map<String, Object> metrics = Map.of(
                    "cpu", 75.0,
                    "memory", 80.0,
                    "requests", 1000L
            );

            ServiceHealthResponseDto result = service.updateServiceHealth(
                    SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP,
                    "OK",
                    metrics
            );

            assertThat(result.metrics()).isEqualTo(metrics);
            assertThat(result.metrics()).hasSize(3);
        }

        @Test
        @DisplayName("Should track total checks correctly")
        void shouldTrackTotalChecksCorrectly() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP, "OK", null);
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP, "OK", null);
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.DOWN, "Failed", null);

            assertThat(health.getTotalChecks()).isEqualTo(3L);
            assertThat(health.getFailedChecks()).isEqualTo(1L);
            assertThat(health.getSuccessRate()).isEqualTo(2.0 / 3.0);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should handle service lifecycle")
        void shouldHandleServiceLifecycle() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            // Service starts
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP, "Started", Map.of("cpu", 30.0));

            // Service gets busy
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP, "Running", Map.of("cpu", 80.0));

            // Service degrades
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.DEGRADED, "High latency", null);

            // Service recovers
            service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                    ServiceHealth.HealthStatus.UP, "Recovered", Map.of("cpu", 50.0));

            assertThat(health.getTotalChecks()).isEqualTo(4L);
            assertThat(health.getFailedChecks()).isEqualTo(1L);
            assertThat(health.getSuccessRate()).isEqualTo(0.75);
        }

        @Test
        @DisplayName("Should calculate success rate correctly")
        void shouldCalculateSuccessRateCorrectly() {
            ServiceHealth health = new ServiceHealth(SERVICE_NAME, TENANT_ID);

            when(repository.findByServiceNameAndTenantId(SERVICE_NAME, TENANT_ID))
                    .thenReturn(Optional.of(health));
            when(repository.save(any(ServiceHealth.class))).thenAnswer(i -> i.getArgument(0));

            // 5 UP, 3 DOWN, 2 DEGRADED
            for (int i = 0; i < 5; i++) {
                service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                        ServiceHealth.HealthStatus.UP, "OK", null);
            }
            for (int i = 0; i < 3; i++) {
                service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                        ServiceHealth.HealthStatus.DOWN, "Failed", null);
            }
            for (int i = 0; i < 2; i++) {
                service.updateServiceHealth(SERVICE_NAME, TENANT_ID,
                        ServiceHealth.HealthStatus.DEGRADED, "Slow", null);
            }

            assertThat(health.getTotalChecks()).isEqualTo(10L);
            assertThat(health.getFailedChecks()).isEqualTo(5L); // 3 DOWN + 2 DEGRADED
            assertThat(health.getSuccessRate()).isEqualTo(0.5);
        }
    }
}
