package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain Event Tests")
class DomainEventTest {

    @Test
    void shouldCreateTenantCreatedEvent() {
        Tenant tenant = Tenant.builder()
            .id("1").tenantId("t1").name("Test").domain("test.com")
            .status(Tenant.TenantStatus.ACTIVE).plan(Tenant.TenantPlan.FREE)
            .maxUsers(10).maxStorageGB(5).build();

        TenantCreatedEvent event = new TenantCreatedEvent(tenant);
        assertEquals("TenantCreated", event.getEventType());
        assertEquals("Tenant", event.getAggregateType());
        assertEquals("1", event.getAggregateId());
        assertEquals("t1", event.getTenantIdValue());
        assertEquals("Test", event.getName());
        assertEquals("test.com", event.getDomain());
        assertEquals(Tenant.TenantStatus.ACTIVE, event.getStatus());
        assertEquals(Tenant.TenantPlan.FREE, event.getPlan());
        assertEquals(10, event.getMaxUsers());
        assertEquals(5, event.getMaxStorageGB());
        assertNotNull(event.getOccurredAt());
    }

    @Test
    void shouldCreateTenantUpdatedEvent() {
        Tenant tenant = Tenant.builder()
            .id("2").tenantId("t2").name("Updated").build();
        TenantUpdatedEvent event = new TenantUpdatedEvent(tenant);
        assertEquals("TenantUpdated", event.getEventType());
        assertEquals("t2", event.getTenantIdValue());
    }

    @Test
    void shouldCreateTenantDeletedEvent() {
        TenantDeletedEvent event = new TenantDeletedEvent("t3", "TestTenant");
        assertEquals("TenantDeleted", event.getEventType());
        assertEquals("t3", event.getTenantIdValue());
        assertEquals("TestTenant", event.getName());
    }
}
