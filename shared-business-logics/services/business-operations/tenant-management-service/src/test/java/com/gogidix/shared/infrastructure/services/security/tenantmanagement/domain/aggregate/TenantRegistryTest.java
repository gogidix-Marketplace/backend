package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.aggregate;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantCreatedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantDeletedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantUpdatedEvent;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.EventPublisherPort;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.port.out.TenantRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("TenantRegistry Tests")
class TenantRegistryTest {

    private TenantRepositoryPort repo;
    private EventPublisherPort publisher;
    private TenantRegistry registry;

    @BeforeEach
    void setUp() {
        repo = mock(TenantRepositoryPort.class);
        publisher = mock(EventPublisherPort.class);
        registry = new TenantRegistry(repo, publisher);
    }

    @Nested
    @DisplayName("Create Tenant")
    class CreateTests {

        @Test
        void shouldCreateTenant() {
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
            Tenant t = registry.createTenant("Test", "test.com", "e@t.com", "Admin",
                Tenant.TenantPlan.FREE, 5, 1);

            assertNotNull(t);
            assertEquals("Test", t.getName());
            assertEquals(Tenant.TenantStatus.TRIAL, t.getStatus());
            assertEquals(Tenant.TenantPlan.FREE, t.getPlan());
            assertNotNull(t.getTrialEndsAt());
            verify(publisher).publish(any(TenantCreatedEvent.class));
        }

        @Test
        void shouldCreateActiveTenantForNonFreePlan() {
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
            Tenant t = registry.createTenant("Pro", "pro.com", "e@t.com", "Admin",
                Tenant.TenantPlan.PROFESSIONAL, 100, 100);

            assertEquals(Tenant.TenantStatus.ACTIVE, t.getStatus());
            assertNull(t.getTrialEndsAt());
        }

        @Test
        void shouldFireCreatedHandler() {
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));
            String[] captured = {null};
            registry.onTenantCreated(e -> captured[0] = e.getName());
            registry.createTenant("Test", null, null, null, Tenant.TenantPlan.FREE, 5, 1);
            assertEquals("Test", captured[0]);
        }
    }

    @Nested
    @DisplayName("Update Tenant")
    class UpdateTests {

        @Test
        void shouldUpdateTenant() {
            Tenant existing = Tenant.builder().tenantId("t1").name("Old").build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(existing));
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            Optional<Tenant> result = registry.updateTenant("t1", "New", "new.com", "new@t.com", "Admin");
            assertTrue(result.isPresent());
            assertEquals("New", result.get().getName());
            verify(publisher).publish(any(TenantUpdatedEvent.class));
        }

        @Test
        void shouldReturnEmptyWhenNotFound() {
            when(repo.findByTenantId("missing")).thenReturn(Optional.empty());
            assertTrue(registry.updateTenant("missing", "New", null, null, null).isEmpty());
        }
    }

    @Nested
    @DisplayName("Update Status")
    class StatusTests {

        @Test
        void shouldUpdateStatus() {
            Tenant existing = Tenant.builder().tenantId("t1").status(Tenant.TenantStatus.ACTIVE).build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(existing));

            assertTrue(registry.updateTenantStatus("t1", Tenant.TenantStatus.SUSPENDED));
            verify(publisher).publish(any(TenantUpdatedEvent.class));
        }

        @Test
        void shouldReturnFalseWhenNotFound() {
            when(repo.findByTenantId("missing")).thenReturn(Optional.empty());
            assertFalse(registry.updateTenantStatus("missing", Tenant.TenantStatus.ACTIVE));
        }
    }

    @Nested
    @DisplayName("Delete Tenant")
    class DeleteTests {

        @Test
        void shouldDeleteTenant() {
            Tenant existing = Tenant.builder().tenantId("t1").name("Test").build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(existing));

            assertTrue(registry.deleteTenant("t1"));
            verify(repo).deleteByTenantId("t1");
            verify(publisher).publish(any(TenantDeletedEvent.class));
        }

        @Test
        void shouldReturnFalseWhenNotFound() {
            when(repo.findByTenantId("missing")).thenReturn(Optional.empty());
            assertFalse(registry.deleteTenant("missing"));
        }
    }

    @Nested
    @DisplayName("Queries")
    class QueryTests {

        @Test
        void shouldGetTenant() {
            Tenant t = Tenant.builder().tenantId("t1").build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(t));
            assertTrue(registry.getTenant("t1").isPresent());
        }

        @Test
        void shouldGetTenantByDomain() {
            when(repo.findByDomain("test.com")).thenReturn(Optional.of(Tenant.builder().build()));
            assertTrue(registry.getTenantByDomain("test.com").isPresent());
        }

        @Test
        void shouldGetAllTenants() {
            when(repo.findAll()).thenReturn(List.of(Tenant.builder().build()));
            assertEquals(1, registry.getAllTenants().size());
        }

        @Test
        void shouldGetTenantsByStatus() {
            when(repo.findByStatus(any())).thenReturn(List.of(Tenant.builder().build()));
            assertEquals(1, registry.getTenantsByStatus(Tenant.TenantStatus.ACTIVE).size());
        }

        @Test
        void shouldGetTenantsByPlan() {
            when(repo.findByPlan(any())).thenReturn(List.of(Tenant.builder().build()));
            assertEquals(1, registry.getTenantsByPlan(Tenant.TenantPlan.FREE).size());
        }

        @Test
        void shouldCheckIfTenantActive() {
            Tenant t = Tenant.builder().tenantId("t1").status(Tenant.TenantStatus.ACTIVE).build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(t));
            assertTrue(registry.isTenantActive("t1"));
        }

        @Test
        void shouldReturnNotActiveWhenMissing() {
            when(repo.findByTenantId("missing")).thenReturn(Optional.empty());
            assertFalse(registry.isTenantActive("missing"));
        }
    }

    @Nested
    @DisplayName("Event Handlers")
    class EventHandlerTests {

        @Test
        void shouldFireUpdatedHandler() {
            Tenant existing = Tenant.builder().tenantId("t1").name("Old").build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(existing));
            when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

            String[] captured = {null};
            registry.onTenantUpdated(e -> captured[0] = e.getName());
            registry.updateTenant("t1", "New", null, null, null);
            assertEquals("New", captured[0]);
        }

        @Test
        void shouldFireDeletedHandler() {
            Tenant existing = Tenant.builder().tenantId("t1").name("Test").build();
            when(repo.findByTenantId("t1")).thenReturn(Optional.of(existing));

            String[] captured = {null};
            registry.onTenantDeleted(e -> captured[0] = e.getName());
            registry.deleteTenant("t1");
            assertEquals("Test", captured[0]);
        }
    }
}
