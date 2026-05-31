package com.gogidix.courier.loadbalancingservice.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DriverCapacity entity.
 */
@DisplayName("DriverCapacity Entity Tests")
class DriverCapacityTest {

    @Test
    @DisplayName("Should create driver capacity with valid parameters")
    void shouldCreateDriverCapacityWithValidParameters() {
        // Given
        String tenantId = "tenant-001";
        String driverId = "driver-001";
        String zoneId = "zone-001";
        Integer maxCapacity = 10;

        // When
        DriverCapacity capacity = new DriverCapacity(tenantId, driverId, zoneId, maxCapacity);

        // Then
        assertNotNull(capacity.getId());
        assertEquals(tenantId, capacity.getTenantId());
        assertEquals(driverId, capacity.getDriverId());
        assertEquals(zoneId, capacity.getZoneId());
        assertEquals(maxCapacity, capacity.getMaxCapacity());
        assertEquals(0, capacity.getCurrentLoad());
        assertEquals(0.0, capacity.getUtilizationPercent());
        assertEquals(DriverCapacity.CapacityStatus.AVAILABLE, capacity.getStatus());
        assertEquals(0, capacity.getTotalAssignmentsToday());
        assertNotNull(capacity.getCreatedAt());
        assertNotNull(capacity.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw when tenantId is null")
    void shouldThrowWhenTenantIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new DriverCapacity(null, "driver-001", "zone-001", 10)
        );
    }

    @Test
    @DisplayName("Should throw when driverId is null")
    void shouldThrowWhenDriverIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new DriverCapacity("tenant-001", null, "zone-001", 10)
        );
    }

    @Test
    @DisplayName("Should throw when zoneId is null")
    void shouldThrowWhenZoneIdIsNull() {
        assertThrows(NullPointerException.class, () ->
            new DriverCapacity("tenant-001", "driver-001", null, 10)
        );
    }

    @Test
    @DisplayName("Should throw when maxCapacity is null")
    void shouldThrowWhenMaxCapacityIsNull() {
        assertThrows(NullPointerException.class, () ->
            new DriverCapacity("tenant-001", "driver-001", "zone-001", null)
        );
    }

    @Test
    @DisplayName("Should assign work successfully")
    void shouldAssignWorkSuccessfully() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);

        // When
        boolean result = capacity.assign();

        // Then
        assertTrue(result);
        assertEquals(1, capacity.getCurrentLoad());
        assertEquals(10.0, capacity.getUtilizationPercent());
        assertEquals(DriverCapacity.CapacityStatus.AVAILABLE, capacity.getStatus());
        assertEquals(1, capacity.getTotalAssignmentsToday());
        assertNotNull(capacity.getLastAssignment());
    }

    @Test
    @DisplayName("Should fail assignment when at capacity")
    void shouldFailAssignmentWhenAtCapacity() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 5);
        for (int i = 0; i < 5; i++) {
            capacity.assign();
        }

        // When
        boolean result = capacity.assign();

        // Then
        assertFalse(result);
        assertEquals(5, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.FULL, capacity.getStatus());
    }

    @Test
    @DisplayName("Should release work successfully")
    void shouldReleaseWorkSuccessfully() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        capacity.assign();
        capacity.assign();
        assertEquals(2, capacity.getCurrentLoad());

        // When
        capacity.release();

        // Then
        assertEquals(1, capacity.getCurrentLoad());
        assertEquals(10.0, capacity.getUtilizationPercent());
    }

    @Test
    @DisplayName("Should not release when load is zero")
    void shouldNotReleaseWhenLoadIsZero() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);

        // When
        capacity.release();

        // Then
        assertEquals(0, capacity.getCurrentLoad());
        assertEquals(0.0, capacity.getUtilizationPercent());
    }

    @Test
    @DisplayName("Should update max capacity successfully")
    void shouldUpdateMaxCapacitySuccessfully() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        capacity.assign();
        capacity.assign();
        assertEquals(2, capacity.getCurrentLoad());

        // When
        capacity.updateCapacity(15);

        // Then
        assertEquals(15, capacity.getMaxCapacity());
        assertEquals(2, capacity.getCurrentLoad());
        assertEquals(13.33, capacity.getUtilizationPercent(), 0.01);
    }

    @Test
    @DisplayName("Should adjust current load when capacity reduced below current load")
    void shouldAdjustCurrentLoadWhenCapacityReduced() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        for (int i = 0; i < 8; i++) {
            capacity.assign();
        }

        // When
        capacity.updateCapacity(5);

        // Then
        assertEquals(5, capacity.getMaxCapacity());
        assertEquals(5, capacity.getCurrentLoad());
        assertEquals(DriverCapacity.CapacityStatus.FULL, capacity.getStatus());
    }

    @Test
    @DisplayName("Should check availability correctly")
    void shouldCheckAvailabilityCorrectly() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);

        // Then
        assertTrue(capacity.isAvailable());

        // When
        for (int i = 0; i < 10; i++) {
            capacity.assign();
        }

        // Then
        assertFalse(capacity.isAvailable());
    }

    @Test
    @DisplayName("Should check if at capacity correctly")
    void shouldCheckIfAtCapacityCorrectly() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 5);

        // Then
        assertFalse(capacity.isAtCapacity());

        // When
        for (int i = 0; i < 5; i++) {
            capacity.assign();
        }

        // Then
        assertTrue(capacity.isAtCapacity());
    }

    @Test
    @DisplayName("Should calculate available slots correctly")
    void shouldCalculateAvailableSlotsCorrectly() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        capacity.assign();
        capacity.assign();
        capacity.assign();

        // When
        int availableSlots = capacity.getAvailableSlots();

        // Then
        assertEquals(7, availableSlots);
    }

    @Test
    @DisplayName("Should return zero available slots when at capacity")
    void shouldReturnZeroAvailableSlotsWhenAtCapacity() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 5);
        for (int i = 0; i < 5; i++) {
            capacity.assign();
        }

        // When
        int availableSlots = capacity.getAvailableSlots();

        // Then
        assertEquals(0, availableSlots);
    }

    @Test
    @DisplayName("Should check overload correctly")
    void shouldCheckOverloadCorrectly() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        for (int i = 0; i < 8; i++) {
            capacity.assign();
        }

        // When
        boolean overloaded = capacity.isOverloaded(0.7);

        // Then
        assertTrue(overloaded);
    }

    @Test
    @DisplayName("Should not be overloaded when below threshold")
    void shouldNotBeOverloadedWhenBelowThreshold() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        capacity.assign();
        capacity.assign();

        // When
        boolean overloaded = capacity.isOverloaded(0.8);

        // Then
        assertFalse(overloaded);
    }

    @Test
    @DisplayName("Should update status to MEDIUM_LOAD at 50%")
    void shouldUpdateStatusToMediumLoadAt50Percent() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        for (int i = 0; i < 5; i++) {
            capacity.assign();
        }

        // Then
        assertEquals(DriverCapacity.CapacityStatus.MEDIUM_LOAD, capacity.getStatus());
    }

    @Test
    @DisplayName("Should update status to HIGH_LOAD at 80%")
    void shouldUpdateStatusToHighLoadAt80Percent() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        for (int i = 0; i < 8; i++) {
            capacity.assign();
        }

        // Then
        assertEquals(DriverCapacity.CapacityStatus.HIGH_LOAD, capacity.getStatus());
    }

    @Test
    @DisplayName("Should update status to FULL at 100%")
    void shouldUpdateStatusToFullAt100Percent() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 10);
        for (int i = 0; i < 10; i++) {
            capacity.assign();
        }

        // Then
        assertEquals(DriverCapacity.CapacityStatus.FULL, capacity.getStatus());
    }

    @Test
    @DisplayName("Should handle zero capacity gracefully")
    void shouldHandleZeroCapacityGracefully() {
        // Given
        DriverCapacity capacity = new DriverCapacity("tenant-001", "driver-001", "zone-001", 0);

        // Then
        assertEquals(0.0, capacity.getUtilizationPercent());
        assertFalse(capacity.assign());
    }
}
