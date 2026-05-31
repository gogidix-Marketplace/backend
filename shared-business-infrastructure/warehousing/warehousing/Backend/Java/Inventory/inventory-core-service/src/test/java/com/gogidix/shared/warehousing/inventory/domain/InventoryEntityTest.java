package com.gogidix.shared.warehousing.inventory.domain;

import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive domain entity tests for Inventory
 * Tests business logic within the entity
 */
class InventoryEntityTest {

    @Test
    void inventoryBuilder_CreatesValidEntity() {
        // Given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("brand", "TestBrand");
        attributes.put("category", "Electronics");

        // When
        Inventory inventory = Inventory.builder()
            .id("inv-001")
            .tenantId("tenant-123")
            .sku("SKU-001")
            .quantity(100)
            .locationId("LOC-001")
            .tenantType(Inventory.TenantType.ECOMMERCE_VENDOR)
            .locationType(Inventory.LocationType.WAREHOUSE)
            .attributes(attributes)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // Then
        assertNotNull(inventory);
        assertEquals("inv-001", inventory.getId());
        assertEquals("tenant-123", inventory.getTenantId());
        assertEquals("SKU-001", inventory.getSku());
        assertEquals(100, inventory.getQuantity());
        assertEquals("LOC-001", inventory.getLocationId());
        assertEquals(Inventory.TenantType.ECOMMERCE_VENDOR, inventory.getTenantType());
        assertEquals(Inventory.LocationType.WAREHOUSE, inventory.getLocationType());
        assertNotNull(inventory.getAttributes());
        assertEquals("TestBrand", inventory.getAttributes().get("brand"));
    }

    @Test
    void inventoryWithNoArgsConstructor_CreatesEmptyEntity() {
        // When
        Inventory inventory = new Inventory();

        // Then
        assertNotNull(inventory);
        assertNull(inventory.getId());
        assertNull(inventory.getTenantId());
        assertNull(inventory.getSku());
        assertNull(inventory.getQuantity());
    }

    @Test
    void inventoryTenantType_EnumValues() {
        // Verify all expected tenant types exist
        Inventory.TenantType[] types = Inventory.TenantType.values();
        assertEquals(4, types.length);

        assertTrue(java.util.Arrays.asList(types).contains(Inventory.TenantType.ECOMMERCE_VENDOR));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.TenantType.LOGISTICS_PARTNER));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.TenantType.PROCUREMENT_CUSTOMER));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.TenantType.PUBLIC_USER_WAREHOUSING));
    }

    @Test
    void inventoryLocationType_EnumValues() {
        // Verify all expected location types exist
        Inventory.LocationType[] types = Inventory.LocationType.values();
        assertEquals(4, types.length);

        assertTrue(java.util.Arrays.asList(types).contains(Inventory.LocationType.WAREHOUSE));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.LocationType.SELF_STORAGE));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.LocationType.VENDOR_LOCATION));
        assertTrue(java.util.Arrays.asList(types).contains(Inventory.LocationType.FULFILLMENT_CENTER));
    }

    @Test
    void inventorySetters_UpdateFieldsCorrectly() {
        // Given
        Inventory inventory = new Inventory();

        // When
        inventory.setId("inv-002");
        inventory.setTenantId("tenant-456");
        inventory.setSku("SKU-002");
        inventory.setQuantity(200);
        inventory.setLocationId("LOC-002");
        inventory.setTenantType(Inventory.TenantType.LOGISTICS_PARTNER);
        inventory.setLocationType(Inventory.LocationType.SELF_STORAGE);

        // Then
        assertEquals("inv-002", inventory.getId());
        assertEquals("tenant-456", inventory.getTenantId());
        assertEquals("SKU-002", inventory.getSku());
        assertEquals(200, inventory.getQuantity());
        assertEquals("LOC-002", inventory.getLocationId());
        assertEquals(Inventory.TenantType.LOGISTICS_PARTNER, inventory.getTenantType());
        assertEquals(Inventory.LocationType.SELF_STORAGE, inventory.getLocationType());
    }

    @Test
    void inventoryWithAllArgsConstructor_AllFieldsSet() {
        // Given
        Map<String, Object> attributes = new HashMap<>();

        // When
        Inventory inventory = new Inventory(
            "inv-003",
            "tenant-789",
            Inventory.TenantType.PROCUREMENT_CUSTOMER,
            "SKU-003",
            50,
            "LOC-003",
            Inventory.LocationType.VENDOR_LOCATION,
            attributes,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Then
        assertEquals("inv-003", inventory.getId());
        assertEquals("tenant-789", inventory.getTenantId());
        assertEquals(Inventory.TenantType.PROCUREMENT_CUSTOMER, inventory.getTenantType());
        assertEquals("SKU-003", inventory.getSku());
        assertEquals(50, inventory.getQuantity());
        assertEquals("LOC-003", inventory.getLocationId());
        assertEquals(Inventory.LocationType.VENDOR_LOCATION, inventory.getLocationType());
    }
}
