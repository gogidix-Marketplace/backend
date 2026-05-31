package com.gogidix.shared.warehousing.storage.application.service;

import com.gogidix.shared.warehousing.storage.application.dto.*;
import com.gogidix.shared.warehousing.storage.domain.entity.StorageSpace;
import com.gogidix.shared.warehousing.storage.domain.repository.StorageSpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageSpaceServiceTest {

    @Mock
    private StorageSpaceRepository repository;

    @InjectMocks
    private StorageSpaceService service;

    private CreateSpaceRequest defaultRequest;

    @BeforeEach
    void setUp() {
        defaultRequest = new CreateSpaceRequest();
        defaultRequest.setTenantId("tenant-123");
        defaultRequest.setSpaceCode("SPACE-001");
        defaultRequest.setSpaceType("GENERAL");
        defaultRequest.setLengthMeters(5.0);
        defaultRequest.setWidthMeters(3.0);
        defaultRequest.setHeightMeters(2.5);
        defaultRequest.setBasePricePerDay(new BigDecimal("50.00"));
        defaultRequest.setCurrency("USD");
        defaultRequest.setFacilityZone("ZONE-A");
        defaultRequest.setShelfLevel("LEVEL-1");
        defaultRequest.setBinNumber("BIN-001");
    }

    @Test
    void shouldCreateStorageSpace() {
        when(repository.existsByTenantIdAndSpaceCode("tenant-123", "SPACE-001")).thenReturn(false);
        when(repository.save(any(StorageSpace.class))).thenAnswer(invocation -> {
            StorageSpace space = invocation.getArgument(0);
            space.setId("space-id-001");
            return space;
        });

        StorageSpaceResponse response = service.createSpace(defaultRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("space-id-001");
        assertThat(response.getSpaceCode()).isEqualTo("SPACE-001");
        assertThat(response.getTotalCapacityCubicMeters()).isEqualTo(37.5);
        assertThat(response.getAvailableCapacityCubicMeters()).isEqualTo(37.5);
        assertThat(response.getStatus()).isEqualTo("AVAILABLE");
        verify(repository).save(any(StorageSpace.class));
    }

    @Test
    void shouldNotCreateDuplicateSpaceCode() {
        when(repository.existsByTenantIdAndSpaceCode("tenant-123", "SPACE-001")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createSpace(defaultRequest));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldGetSpacesByTenant() {
        StorageSpace space1 = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        StorageSpace space2 = createSpace("id-2", "SPACE-002", "CLIMATE_CONTROLLED", 37.5, 37.5, "AVAILABLE");
        when(repository.findByTenantId("tenant-123")).thenReturn(List.of(space1, space2));

        List<StorageSpaceResponse> spaces = service.getSpaces("tenant-123");

        assertThat(spaces).hasSize(2);
        assertThat(spaces).extracting(StorageSpaceResponse::getSpaceCode)
            .containsExactlyInAnyOrder("SPACE-001", "SPACE-002");
    }

    @Test
    void shouldGetAvailableSpacesByType() {
        StorageSpace space1 = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        StorageSpace space2 = createSpace("id-2", "SPACE-002", "GENERAL", 37.5, 37.5, "AVAILABLE");
        when(repository.findByTenantIdAndStatusAndSpaceType("tenant-123", "AVAILABLE", "GENERAL"))
            .thenReturn(List.of(space1, space2));

        List<StorageSpaceResponse> available = service.getAvailableSpaces("tenant-123", "GENERAL");

        assertThat(available).hasSize(2);
    }

    @Test
    void shouldUpdateSpace() {
        StorageSpace existing = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        when(repository.findById("id-1")).thenReturn(Optional.of(existing));
        when(repository.save(any(StorageSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        defaultRequest.setLengthMeters(6.0);
        defaultRequest.setWidthMeters(4.0);
        defaultRequest.setHeightMeters(3.0);
        defaultRequest.setBasePricePerDay(new BigDecimal("60.00"));

        StorageSpaceResponse updated = service.updateSpace("id-1", defaultRequest);

        assertThat(updated.getTotalCapacityCubicMeters()).isEqualTo(72.0);
        assertThat(updated.getBasePricePerDay()).isEqualByComparingTo("60.00");
    }

    @Test
    void shouldAllocateSpace() {
        StorageSpace space = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        when(repository.findById("id-1")).thenReturn(Optional.of(space));
        when(repository.save(any(StorageSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AllocateSpaceRequest allocRequest = new AllocateSpaceRequest();
        allocRequest.setCustomerId("customer-001");
        allocRequest.setRequiredCapacityCubicMeters(10.0);

        StorageSpaceResponse allocated = service.allocateSpace("id-1", allocRequest);

        assertThat(allocated.getAvailableCapacityCubicMeters()).isEqualTo(27.5);
        assertThat(allocated.getStatus()).isEqualTo("AVAILABLE");
    }

    @Test
    void shouldFullyOccupySpaceWhenCapacityExhausted() {
        StorageSpace space = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        when(repository.findById("id-1")).thenReturn(Optional.of(space));
        when(repository.save(any(StorageSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AllocateSpaceRequest allocRequest = new AllocateSpaceRequest();
        allocRequest.setCustomerId("customer-001");
        allocRequest.setRequiredCapacityCubicMeters(37.5);

        StorageSpaceResponse allocated = service.allocateSpace("id-1", allocRequest);

        assertThat(allocated.getAvailableCapacityCubicMeters()).isEqualTo(0.0);
        assertThat(allocated.getStatus()).isEqualTo("OCCUPIED");
    }

    @Test
    void shouldNotAllocateInsufficientCapacity() {
        StorageSpace space = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 37.5, "AVAILABLE");
        when(repository.findById("id-1")).thenReturn(Optional.of(space));

        AllocateSpaceRequest allocRequest = new AllocateSpaceRequest();
        allocRequest.setCustomerId("customer-001");
        allocRequest.setRequiredCapacityCubicMeters(100.0);

        assertThrows(IllegalStateException.class, () -> service.allocateSpace("id-1", allocRequest));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldOptimizeSpace() {
        StorageSpace space = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 27.5, "AVAILABLE");
        when(repository.findById("id-1")).thenReturn(Optional.of(space));
        when(repository.save(any(StorageSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.optimizeSpace("id-1");

        verify(repository).save(argThat(s -> s.getAvailableSlots() == 1));
    }

    @Test
    void shouldGenerateUtilizationReport() {
        StorageSpace space1 = createSpace("id-1", "SPACE-001", "GENERAL", 37.5, 18.75, "AVAILABLE");
        StorageSpace space2 = createSpace("id-2", "SPACE-002", "CLIMATE_CONTROLLED", 37.5, 37.5, "AVAILABLE");
        when(repository.findByTenantId("tenant-123")).thenReturn(List.of(space1, space2));

        UtilizationReport report = service.getUtilizationReport("tenant-123");

        assertThat(report.getTotalSpaces()).isEqualTo(2);
        assertThat(report.getAvailableSpaces()).isEqualTo(2);
        assertThat(report.getTotalCapacityCubicMeters()).isEqualTo(75.0);
        assertThat(report.getUsedCapacityCubicMeters()).isEqualTo(18.75);
        assertThat(report.getAverageUtilizationPercentage()).isEqualTo(25.0);
        assertThat(report.getStatisticsByType()).hasSize(2);
    }

    @Test
    void shouldThrowWhenSpaceNotFound() {
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getSpaceById("nonexistent"));
    }

    private StorageSpace createSpace(String id, String spaceCode, String spaceType,
                                      double totalCapacity, double availableCapacity, String status) {
        return StorageSpace.builder()
            .id(id)
            .tenantId("tenant-123")
            .spaceCode(spaceCode)
            .spaceType(spaceType)
            .lengthMeters(5.0)
            .widthMeters(3.0)
            .heightMeters(2.5)
            .totalCapacityCubicMeters(totalCapacity)
            .availableCapacityCubicMeters(availableCapacity)
            .availableSlots(1)
            .basePricePerDay(new BigDecimal("50.00"))
            .currency("USD")
            .facilityZone("ZONE-A")
            .status(status)
            .build();
    }
}
