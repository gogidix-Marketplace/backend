package com.gogidix.courier.availabilityservice.domain;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DriverAvailability entity.
 */
@DisplayName("DriverAvailability Domain Tests")
class DriverAvailabilityTest {

    @Test
    @DisplayName("Should create driver availability successfully")
    void shouldCreateDriverAvailability() {
        // Given
        String tenantId = "tenant-1";
        String driverId = "driver-1";
        LocalDate date = LocalDate.now();

        // When
        DriverAvailability availability = new DriverAvailability(tenantId, driverId, date);

        // Then
        assertNotNull(availability.getId());
        assertEquals(tenantId, availability.getTenantId());
        assertEquals(driverId, availability.getDriverId());
        assertEquals(date, availability.getDate());
        assertEquals(DriverAvailability.AvailabilityStatus.UNKNOWN, availability.getStatus());
        assertEquals(10, availability.getMaxCapacity());
        assertEquals(0, availability.getCurrentLoad());
    }

    @Test
    @DisplayName("Should mark driver as available")
    void shouldMarkAsAvailable() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());

        // When
        availability.markAvailable();

        // Then
        assertEquals(DriverAvailability.AvailabilityStatus.AVAILABLE, availability.getStatus());
    }

    @Test
    @DisplayName("Should mark driver as unavailable")
    void shouldMarkAsUnavailable() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());

        // When
        availability.markUnavailable("Sick leave");

        // Then
        assertEquals(DriverAvailability.AvailabilityStatus.UNAVAILABLE, availability.getStatus());
        assertFalse(availability.getUnavailablePeriods().isEmpty());
    }

    @Test
    @DisplayName("Should add availability slot")
    void shouldAddAvailabilitySlot() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());

        // When
        availability.addAvailabilitySlot(java.time.LocalTime.of(9, 0), java.time.LocalTime.of(12, 0));

        // Then
        assertEquals(1, availability.getSlots().size());
        assertEquals(DriverAvailability.AvailabilityStatus.AVAILABLE, availability.getStatus());
    }

    @Test
    @DisplayName("Should increment and decrement load")
    void shouldHandleLoadChanges() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());

        // When
        availability.incrementLoad();

        // Then
        assertEquals(1, availability.getCurrentLoad());
        assertFalse(availability.isAtCapacity());

        // When
        availability.decrementLoad();

        // Then
        assertEquals(0, availability.getCurrentLoad());
    }

    @Test
    @DisplayName("Should detect when at capacity")
    void shouldDetectAtCapacity() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());
        availability.setMaxCapacity(2);

        // When
        availability.incrementLoad();
        availability.incrementLoad();

        // Then
        assertTrue(availability.isAtCapacity());
    }

    @Test
    @DisplayName("Should check availability at specific time")
    void shouldCheckAvailabilityAtTime() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());
        availability.addAvailabilitySlot(java.time.LocalTime.of(9, 0), java.time.LocalTime.of(12, 0));

        // When
        boolean isAvailable = availability.isAvailableAt(LocalDate.now().atTime(10, 0));

        // Then
        assertTrue(isAvailable);
    }

    @Test
    @DisplayName("Should validate slot times")
    void shouldValidateSlotTimes() {
        // Given
        DriverAvailability availability = new DriverAvailability("tenant-1", "driver-1", LocalDate.now());

        // When & Then
        assertThrows(IllegalArgumentException.class, () ->
                availability.addAvailabilitySlot(java.time.LocalTime.of(12, 0), java.time.LocalTime.of(9, 0))
        );
    }
}
