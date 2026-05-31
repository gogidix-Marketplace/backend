package com.gogidix.centralizeddashboard.metrics.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceHealthTest {

    @Test
    void defaultConstructor_initializesIdAndTimestamps() {
        ServiceHealth sh = new ServiceHealth();
        assertNotNull(sh.getId());
        assertNotNull(sh.getLastUpdated());
        assertNotNull(sh.getLastCheckTime());
    }

    @Test
    void constructorWithFields_setsFields() {
        ServiceHealth sh = new ServiceHealth("payment-service", "instance-1", ServiceHealth.HealthStatus.HEALTHY);
        assertEquals("payment-service", sh.getServiceName());
        assertEquals("instance-1", sh.getInstanceId());
        assertEquals(ServiceHealth.HealthStatus.HEALTHY, sh.getStatus());
        assertNotNull(sh.getId());
    }

    @Test
    void settersWork() {
        ServiceHealth sh = new ServiceHealth();
        sh.setId("custom-id");
        sh.setServiceName("order-service");
        sh.setInstanceId("inst-2");
        sh.setStatus(ServiceHealth.HealthStatus.UNHEALTHY);
        sh.setDetails("Service crashed");
        sh.setLastUpdated(null);
        sh.setLastCheckTime(null);

        assertEquals("custom-id", sh.getId());
        assertEquals("order-service", sh.getServiceName());
        assertEquals("inst-2", sh.getInstanceId());
        assertEquals(ServiceHealth.HealthStatus.UNHEALTHY, sh.getStatus());
        assertEquals("Service crashed", sh.getDetails());
        assertNull(sh.getLastUpdated());
    }

    @Test
    void updateStatus_updatesFields() {
        ServiceHealth sh = new ServiceHealth("svc", "inst", ServiceHealth.HealthStatus.HEALTHY);
        sh.updateStatus(ServiceHealth.HealthStatus.UNHEALTHY, "Connection timeout");

        assertEquals(ServiceHealth.HealthStatus.UNHEALTHY, sh.getStatus());
        assertEquals("Connection timeout", sh.getDetails());
        assertNotNull(sh.getLastUpdated());
        assertNotNull(sh.getLastCheckTime());
    }

    @Test
    void updateCheckTime_updatesField() {
        ServiceHealth sh = new ServiceHealth();
        sh.updateCheckTime();
        assertNotNull(sh.getLastCheckTime());
    }

    @Test
    void healthStatus_enumValues() {
        assertEquals(6, ServiceHealth.HealthStatus.values().length);
        assertNotNull(ServiceHealth.HealthStatus.valueOf("HEALTHY"));
        assertNotNull(ServiceHealth.HealthStatus.valueOf("DEGRADED"));
        assertNotNull(ServiceHealth.HealthStatus.valueOf("UNHEALTHY"));
        assertNotNull(ServiceHealth.HealthStatus.valueOf("UNKNOWN"));
        assertNotNull(ServiceHealth.HealthStatus.valueOf("MAINTENANCE"));
        assertNotNull(ServiceHealth.HealthStatus.valueOf("DRAINING"));
    }
}
