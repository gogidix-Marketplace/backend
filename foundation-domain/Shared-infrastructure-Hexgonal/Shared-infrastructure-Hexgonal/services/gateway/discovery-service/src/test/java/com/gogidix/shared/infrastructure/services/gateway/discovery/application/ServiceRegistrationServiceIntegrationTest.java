package com.gogidix.shared.infrastructure.services.gateway.discovery.application;

import com.gogidix.shared.infrastructure.services.gateway.discovery.application.port.in.ServiceRegistrationPort;
import com.gogidix.shared.infrastructure.services.gateway.discovery.config.DomainConfig;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceRegistration;
import com.gogidix.shared.infrastructure.services.gateway.discovery.application.service.ServiceRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ServiceRegistrationService.
 */
class ServiceRegistrationServiceIntegrationTest {

    private ServiceRegistrationService service;
    private ServiceRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ServiceRegistry();
        service = new ServiceRegistrationService(registry);
    }

    @Test
    void testRegister_ValidRegistration_ReturnsInstance() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .zone("zone1")
                .build();

        // Act
        ServiceInstance instance = service.register(registration, "instance-1");

        // Assert
        assertNotNull(instance);
        assertEquals("instance-1", instance.getInstanceId());
        assertEquals("test-service", instance.getAppName());
        assertEquals("localhost", instance.getHostName());
        assertEquals(8080, instance.getPort());
        assertEquals("zone1", instance.getZone());
        assertEquals("UP", instance.getStatus());
    }

    @Test
    void testRegister_MultipleRegistrations_CreatesMultipleInstances() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("service-a")
                .hostName("host1")
                .port(8080)
                .build();

        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("service-b")
                .hostName("host2")
                .port(9090)
                .build();

        // Act
        service.register(registration1, "instance-1");
        service.register(registration2, "instance-2");

        // Assert
        assertEquals(2, registry.size());
    }

    @Test
    void testDeregister_ExistingInstance_RemovesInstance() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        service.register(registration, "instance-1");

        // Act
        var result = service.deregister("instance-1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("instance-1", result.get().getInstanceId());
        assertTrue(registry.isEmpty());
    }

    @Test
    void testDeregister_NonExistentInstance_ReturnsEmpty() {
        // Act
        var result = service.deregister("non-existent");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testRenew_ExistingInstance_UpdatesLeaseTime() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .leaseDuration(30)
                .build();
        service.register(registration, "instance-1");

        // Act
        var renewed = service.renew("instance-1");

        // Assert
        assertTrue(renewed.isPresent());
        assertNotNull(renewed.get().getLastRenewalTime());
    }

    @Test
    void testRenew_NonExistentInstance_ReturnsEmpty() {
        // Act
        var renewed = service.renew("non-existent");

        // Assert
        assertTrue(renewed.isEmpty());
    }

    @Test
    void testUpdateStatus_ExistingInstance_ChangesStatus() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        service.register(registration, "instance-1");

        // Act
        var updated = service.updateStatus("instance-1", "OUT_OF_SERVICE");

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("OUT_OF_SERVICE", updated.get().getStatus());
        assertTrue(updated.get().isOutOfService());
    }

    @Test
    void testUpdateStatus_NonExistentInstance_ReturnsEmpty() {
        // Act
        var updated = service.updateStatus("non-existent", "DOWN");

        // Assert
        assertTrue(updated.isEmpty());
    }

    @Test
    void testUpdateStatus_ToDown_ChangesStatus() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        service.register(registration, "instance-1");

        // Act
        var updated = service.updateStatus("instance-1", "DOWN");

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("DOWN", updated.get().getStatus());
        assertFalse(updated.get().isUp());
    }

    @Test
    void testRegister_WithMetadata_PreservesMetadata() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .metadata(Map.of("version", "1.0.0", "environment", "prod"))
                .build();

        // Act
        ServiceInstance instance = service.register(registration, "instance-1");

        // Assert
        assertNotNull(instance.getMetadata());
        assertEquals("1.0.0", instance.getMetadata().get("version"));
        assertEquals("prod", instance.getMetadata().get("environment"));
    }

    @Test
    void testRegister_SecureService_CreatesHttpsUrl() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("example.com")
                .port(8443)
                .secure(true)
                .build();

        // Act
        ServiceInstance instance = service.register(registration, "instance-1");

        // Assert
        assertTrue(instance.isSecure());
        assertTrue(instance.getServiceUrl().startsWith("https://"));
    }

    @Test
    void testUpdateStatus_MultipleStatusChanges_KeepsLastStatus() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        service.register(registration, "instance-1");

        // Act
        service.updateStatus("instance-1", "OUT_OF_SERVICE");
        service.updateStatus("instance-1", "UP");
        var result = service.updateStatus("instance-1", "DOWN");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("DOWN", result.get().getStatus());
    }

    @Test
    void testRegister_WithCustomLeaseDuration_SetsDuration() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .leaseDuration(60)
                .build();

        // Act
        ServiceInstance instance = service.register(registration, "instance-1");

        // Assert
        assertEquals(60, instance.getLeaseDuration());
    }

    @Test
    void testRegister_WithZoneInformation_SetsZone() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .zone("us-west-1")
                .dataCenter("dc-west")
                .build();

        // Act
        ServiceInstance instance = service.register(registration, "instance-1");

        // Assert
        assertEquals("us-west-1", instance.getZone());
        assertEquals("dc-west", instance.getDataCenter());
    }
}
