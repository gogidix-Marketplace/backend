package com.gogidix.shared.infrastructure.services.gateway.discovery.domain;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.aggregate.ServiceRegistry;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceDeregisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRegisteredEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event.ServiceRenewedEvent;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceRegistration;
import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.registry.ServiceRegistryStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ServiceRegistry aggregate root.
 */
class ServiceRegistryIntegrationTest {

    private ServiceRegistry serviceRegistry;

    @BeforeEach
    void setUp() {
        serviceRegistry = new ServiceRegistry();
    }

    @Test
    void testRegisterService_Success() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .zone("zone1")
                .build();

        // Act
        ServiceInstance instance = serviceRegistry.register(registration, "instance-1");

        // Assert
        assertNotNull(instance);
        assertEquals("instance-1", instance.getInstanceId());
        assertEquals("test-service", instance.getAppName());
        assertEquals("localhost", instance.getHostName());
        assertEquals(8080, instance.getPort());
        assertEquals("zone1", instance.getZone());
        assertEquals("UP", instance.getStatus());
        assertTrue(instance.isUp());
    }

    @Test
    void testRegisterMultipleServices_Success() {
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
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");

        // Assert
        assertEquals(2, serviceRegistry.size());
        assertEquals(2, serviceRegistry.getAllApplications().size());
        assertEquals(2, serviceRegistry.getAllInstances().size());
    }

    @Test
    void testRegisterMultipleInstancesOfSameService_Success() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .build();

        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .build();

        // Act
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");

        // Assert
        assertEquals(2, serviceRegistry.size());
        assertEquals(1, serviceRegistry.getAllApplications().size());
        assertEquals(2, serviceRegistry.getInstancesByApp("test-service").size());
    }

    @Test
    void testDeregisterService_Success() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Act
        var deregistered = serviceRegistry.deregister("instance-1");

        // Assert
        assertTrue(deregistered.isPresent());
        assertEquals("instance-1", deregistered.get().getInstanceId());
        assertTrue(serviceRegistry.isEmpty());
    }

    @Test
    void testDeregisterNonExistentService_ReturnsEmpty() {
        // Act
        var deregistered = serviceRegistry.deregister("non-existent");

        // Assert
        assertTrue(deregistered.isEmpty());
    }

    @Test
    void testRenewService_Success() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .leaseDuration(30)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Act
        var renewed = serviceRegistry.renew("instance-1");

        // Assert
        assertTrue(renewed.isPresent());
        assertNotNull(renewed.get().getLastRenewalTime());
    }

    @Test
    void testUpdateStatus_Success() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Act
        var updated = serviceRegistry.updateStatus("instance-1", "OUT_OF_SERVICE");

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("OUT_OF_SERVICE", updated.get().getStatus());
        assertTrue(updated.get().isOutOfService());
    }

    @Test
    void testGetInstancesByApp_Success() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");

        // Act
        List<ServiceInstance> instances = serviceRegistry.getInstancesByApp("test-service");

        // Assert
        assertEquals(2, instances.size());
    }

    @Test
    void testGetUpInstancesByApp_OnlyReturnsUpInstances() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");
        serviceRegistry.updateStatus("instance-2", "DOWN");

        // Act
        List<ServiceInstance> upInstances = serviceRegistry.getUpInstancesByApp("test-service");

        // Assert
        assertEquals(1, upInstances.size());
        assertEquals("instance-1", upInstances.get(0).getInstanceId());
    }

    @Test
    void testGetStatistics_ReturnsCorrectStats() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("service-a")
                .hostName("host1")
                .port(8080)
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("service-a")
                .hostName("host2")
                .port(8080)
                .build();
        ServiceRegistration registration3 = ServiceRegistration.builder()
                .appName("service-b")
                .hostName("host3")
                .port(9090)
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");
        serviceRegistry.register(registration3, "instance-3");
        serviceRegistry.updateStatus("instance-3", "DOWN");

        // Act
        ServiceRegistryStatistics stats = serviceRegistry.getStatistics();

        // Assert
        assertEquals(2, stats.getTotalApplications());
        assertEquals(3, stats.getTotalInstances());
        assertEquals(2, stats.getUpInstances());
        assertEquals(1, stats.getDownInstances());
    }

    @Test
    void testServiceRegisteredEvent_IsPublished() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<ServiceRegisteredEvent> eventRef = new AtomicReference<>();

        serviceRegistry.onServiceRegistered(event -> {
            eventRef.set(event);
            latch.countDown();
        });

        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();

        // Act
        serviceRegistry.register(registration, "instance-1");

        // Assert
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotNull(eventRef.get());
        assertEquals("test-service", eventRef.get().getAppName());
        assertEquals("instance-1", eventRef.get().getInstanceId());
    }

    @Test
    void testServiceDeregisteredEvent_IsPublished() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<ServiceDeregisteredEvent> eventRef = new AtomicReference<>();

        serviceRegistry.onServiceDeregistered(event -> {
            eventRef.set(event);
            latch.countDown();
        });

        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Act
        serviceRegistry.deregister("instance-1");

        // Assert
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotNull(eventRef.get());
        assertEquals("test-service", eventRef.get().getAppName());
        assertEquals("instance-1", eventRef.get().getInstanceId());
    }

    @Test
    void testServiceRenewedEvent_IsPublished() throws InterruptedException {
        // Arrange
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<ServiceRenewedEvent> eventRef = new AtomicReference<>();

        serviceRegistry.onServiceRenewed(event -> {
            eventRef.set(event);
            latch.countDown();
        });

        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Act
        serviceRegistry.renew("instance-1");

        // Assert
        assertTrue(latch.await(1, TimeUnit.SECONDS));
        assertNotNull(eventRef.get());
        assertEquals("test-service", eventRef.get().getAppName());
        assertEquals("instance-1", eventRef.get().getInstanceId());
    }

    @Test
    void testGetInstancesByZone_Success() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .zone("zone1")
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .zone("zone2")
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");

        // Act
        List<ServiceInstance> zone1Instances = serviceRegistry.getInstancesByZone("zone1");
        List<ServiceInstance> zone2Instances = serviceRegistry.getInstancesByZone("zone2");

        // Assert
        assertEquals(1, zone1Instances.size());
        assertEquals(1, zone2Instances.size());
        assertEquals("zone1", zone1Instances.get(0).getZone());
        assertEquals("zone2", zone2Instances.get(0).getZone());
    }

    @Test
    void testGetInstancesByDataCenter_Success() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .dataCenter("dc1")
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .dataCenter("dc2")
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");

        // Act
        List<ServiceInstance> dc1Instances = serviceRegistry.getInstancesByDataCenter("dc1");

        // Assert
        assertEquals(1, dc1Instances.size());
        assertEquals("dc1", dc1Instances.get(0).getDataCenter());
    }

    @Test
    void testEvictExpiredInstances_RemovesExpired() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .leaseDuration(1)
                .build();
        serviceRegistry.register(registration, "instance-1");

        // Wait for lease to expire
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }

        // Act
        List<ServiceInstance> expired = serviceRegistry.evictExpired();

        // Assert
        assertEquals(1, expired.size());
        assertTrue(serviceRegistry.isEmpty());
    }

    @Test
    void testServiceInstance_ToBuilder_CreatesCopy() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .zone("zone1")
                .build();
        ServiceInstance original = serviceRegistry.register(registration, "instance-1");

        // Act
        ServiceInstance copy = original.toBuilder().zone("zone2").build();

        // Assert
        assertEquals("zone1", original.getZone());
        assertEquals("zone2", copy.getZone());
        assertEquals(original.getInstanceId(), copy.getInstanceId());
    }

    @Test
    void testServiceInstance_GetServiceUrl_ReturnsCorrectUrl() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("example.com")
                .port(8761)
                .secure(false)
                .build();
        ServiceInstance instance = serviceRegistry.register(registration, "instance-1");

        // Act
        String url = instance.getServiceUrl();

        // Assert
        assertEquals("http://example.com:8761", url);
    }

    @Test
    void testServiceInstance_GetServiceUrl_ReturnsHttpsUrl() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("example.com")
                .port(8761)
                .secure(true)
                .build();
        ServiceInstance instance = serviceRegistry.register(registration, "instance-1");

        // Act
        String url = instance.getServiceUrl();

        // Assert
        assertEquals("https://example.com:8761", url);
    }

    @Test
    void testServiceInstance_EqualsAndHashCode() {
        // Arrange
        ServiceRegistration registration = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("localhost")
                .port(8080)
                .build();
        ServiceInstance instance1 = serviceRegistry.register(registration, "instance-1");
        ServiceInstance instance2 = serviceRegistry.register(registration, "instance-1");

        // Assert
        assertEquals(instance1, instance2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    void testGetStatistics_PerZone() {
        // Arrange
        ServiceRegistration registration1 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host1")
                .port(8080)
                .zone("zone1")
                .build();
        ServiceRegistration registration2 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host2")
                .port(8080)
                .zone("zone2")
                .build();
        ServiceRegistration registration3 = ServiceRegistration.builder()
                .appName("test-service")
                .hostName("host3")
                .port(8080)
                .zone("zone1")
                .build();
        serviceRegistry.register(registration1, "instance-1");
        serviceRegistry.register(registration2, "instance-2");
        serviceRegistry.register(registration3, "instance-3");

        // Act
        ServiceRegistryStatistics stats = serviceRegistry.getStatistics();

        // Assert
        assertEquals(2, stats.getInstancesPerZone().get("zone1"));
        assertEquals(1, stats.getInstancesPerZone().get("zone2"));
    }
}
