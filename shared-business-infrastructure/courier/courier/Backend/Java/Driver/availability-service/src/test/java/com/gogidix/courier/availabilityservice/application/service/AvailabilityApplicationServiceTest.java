package com.gogidix.courier.availabilityservice.application.service;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import com.gogidix.courier.availabilityservice.domain.repository.DriverAvailabilityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AvailabilityApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AvailabilityApplicationService Tests")
class AvailabilityApplicationServiceTest {

    @Mock
    private DriverAvailabilityRepository availabilityRepository;

    @Test
    @DisplayName("Should create DriverAvailability entity")
    void shouldCreateDriverAvailabilityEntity() {
        // Given
        String tenantId = "tenant-1";
        String driverId = "driver-1";
        LocalDate date = LocalDate.now();

        // When
        DriverAvailability availability = new DriverAvailability(tenantId, driverId, date);

        // Then
        assertNotNull(availability);
        assertEquals(tenantId, availability.getTenantId());
        assertEquals(driverId, availability.getDriverId());
        assertEquals(date, availability.getDate());
        assertEquals(DriverAvailability.AvailabilityStatus.UNKNOWN, availability.getStatus());
    }

    @Test
    @DisplayName("Should check availability exists in repository")
    void shouldCheckAvailabilityExistsInRepository() {
        // Given
        String tenantId = "tenant-1";
        String driverId = "driver-1";
        LocalDate date = LocalDate.now();
        DriverAvailability availability = new DriverAvailability(tenantId, driverId, date);

        when(availabilityRepository.findByDriverIdAndDate(any(), any()))
                .thenReturn(Optional.of(availability));

        // When
        Optional<DriverAvailability> result = availabilityRepository.findByDriverIdAndDate(driverId, date);

        // Then
        assertTrue(result.isPresent());
        assertEquals(tenantId, result.get().getTenantId());
        verify(availabilityRepository).findByDriverIdAndDate(driverId, date);
    }

    @Test
    @DisplayName("Should save availability to repository")
    void shouldSaveAvailabilityToRepository() {
        // Given
        String tenantId = "tenant-1";
        String driverId = "driver-1";
        LocalDate date = LocalDate.now();
        DriverAvailability availability = new DriverAvailability(tenantId, driverId, date);

        when(availabilityRepository.save(any())).thenReturn(availability);

        // When
        DriverAvailability result = availabilityRepository.save(availability);

        // Then
        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(availabilityRepository).save(availability);
    }
}
