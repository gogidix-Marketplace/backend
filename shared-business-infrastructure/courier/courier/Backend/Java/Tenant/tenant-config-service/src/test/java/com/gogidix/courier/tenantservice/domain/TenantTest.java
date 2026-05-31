package com.gogidix.courier.tenantservice.domain;

import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.domain.entity.TenantConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Tenant domain entity.
 */
@DisplayName("Tenant Domain Entity Tests")
class TenantTest {

    @Test
    @DisplayName("Should create a new tenant successfully")
    void shouldCreateNewTenant() {
        // Given
        String tenantId = "tenant-001";
        String name = "Test Tenant";
        String description = "Test Description";

        // When
        Tenant tenant = new Tenant(tenantId, name, description);

        // Then
        assertNotNull(tenant.getId());
        assertEquals(tenantId, tenant.getTenantId());
        assertEquals(name, tenant.getName());
        assertEquals(description, tenant.getDescription());
        assertEquals(Tenant.TenantStatus.PENDING, tenant.getStatus());
        assertNotNull(tenant.getConfig());
        assertNotNull(tenant.getCreatedAt());
        assertNotNull(tenant.getUpdatedAt());
    }

    @Test
    @DisplayName("Should activate a pending tenant")
    void shouldActivatePendingTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When
        tenant.activate();

        // Then
        assertEquals(Tenant.TenantStatus.ACTIVE, tenant.getStatus());
        assertNotNull(tenant.getActivatedAt());
    }

    @Test
    @DisplayName("Should not activate an already active tenant")
    void shouldNotActivateActiveTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.activate();

        // When & Then
        assertThrows(IllegalStateException.class, tenant::activate);
    }

    @Test
    @DisplayName("Should not activate a terminated tenant")
    void shouldNotActivateTerminatedTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.activate();
        tenant.terminate();

        // When & Then
        assertThrows(IllegalStateException.class, tenant::activate);
    }

    @Test
    @DisplayName("Should deactivate an active tenant")
    void shouldDeactivateActiveTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.activate();

        // When
        tenant.deactivate();

        // Then
        assertEquals(Tenant.TenantStatus.INACTIVE, tenant.getStatus());
        assertNotNull(tenant.getDeactivatedAt());
    }

    @Test
    @DisplayName("Should not deactivate a pending tenant")
    void shouldNotDeactivatePendingTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When & Then
        assertThrows(IllegalStateException.class, tenant::deactivate);
    }

    @Test
    @DisplayName("Should terminate a tenant")
    void shouldTerminateTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.activate();

        // When
        tenant.terminate();

        // Then
        assertEquals(Tenant.TenantStatus.TERMINATED, tenant.getStatus());
        assertNotNull(tenant.getDeactivatedAt());
    }

    @Test
    @DisplayName("Should update tenant details")
    void shouldUpdateTenantDetails() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When
        tenant.updateDetails("Updated Name", "Updated Description");

        // Then
        assertEquals("Updated Name", tenant.getName());
        assertEquals("Updated Description", tenant.getDescription());
    }

    @Test
    @DisplayName("Should not update terminated tenant")
    void shouldNotUpdateTerminatedTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.terminate();

        // When & Then
        assertThrows(IllegalStateException.class,
                () -> tenant.updateDetails("New Name", "New Description"));
    }

    @Test
    @DisplayName("Should update tenant config")
    void shouldUpdateTenantConfig() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        TenantConfig newConfig = new TenantConfig();
        newConfig.setMaxDrivers(200);

        // When
        tenant.updateConfig(newConfig);

        // Then
        assertEquals(200, tenant.getConfig().getMaxDrivers());
    }

    @Test
    @DisplayName("Should update single config value")
    void shouldUpdateSingleConfigValue() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When
        tenant.updateConfigValue("customKey", "customValue");

        // Then
        assertEquals("customValue", tenant.getConfig().getSetting("customKey"));
    }

    @Test
    @DisplayName("Should return true when tenant is active")
    void shouldReturnTrueWhenActive() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.activate();

        // When
        boolean isActive = tenant.isActive();

        // Then
        assertTrue(isActive);
    }

    @Test
    @DisplayName("Should return false when tenant is not active")
    void shouldReturnFalseWhenNotActive() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When
        boolean isActive = tenant.isActive();

        // Then
        assertFalse(isActive);
    }

    @Test
    @DisplayName("Should return true when tenant is modifiable")
    void shouldReturnTrueWhenModifiable() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");

        // When
        boolean isModifiable = tenant.isModifiable();

        // Then
        assertTrue(isModifiable);
    }

    @Test
    @DisplayName("Should return false when tenant is terminated")
    void shouldReturnFalseWhenTerminated() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Test", "Description");
        tenant.terminate();

        // When
        boolean isModifiable = tenant.isModifiable();

        // Then
        assertFalse(isModifiable);
    }

    @Test
    @DisplayName("Should validate valid tenant")
    void shouldValidateValidTenant() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "Valid Name", "Description");

        // When & Then - should not throw
        assertDoesNotThrow(tenant::validate);
    }

    @Test
    @DisplayName("Should throw on invalid tenant - blank tenantId")
    void shouldThrowOnBlankTenantId() {
        // Given
        Tenant tenant = new Tenant(" ", "Name", "Description");

        // When & Then
        assertThrows(IllegalArgumentException.class, tenant::validate);
    }

    @Test
    @DisplayName("Should throw on invalid tenant - blank name")
    void shouldThrowOnBlankName() {
        // Given
        Tenant tenant = new Tenant("tenant-001", "   ", "Description");

        // When & Then
        assertThrows(IllegalArgumentException.class, tenant::validate);
    }

    @Test
    @DisplayName("Should throw on invalid tenant - name too long")
    void shouldThrowOnNameTooLong() {
        // Given
        String longName = "a".repeat(101);
        Tenant tenant = new Tenant("tenant-001", longName, "Description");

        // When & Then
        assertThrows(IllegalArgumentException.class, tenant::validate);
    }
}
