package com.gogidix.shared.warehousing.zone.application.service;

import com.gogidix.shared.warehousing.zone.domain.entity.Zone;
import com.gogidix.shared.warehousing.zone.domain.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    private Zone lagosZone;
    private Zone abujaZone;

    @BeforeEach
    void setUp() {
        lagosZone = Zone.builder()
                .id("zone-001")
                .zoneId("zone-lagos")
                .zoneName("Lagos Zone")
                .name("Lagos")
                .state("Lagos")
                .country("Nigeria")
                .warehouseIds(List.of("wh-lagos-01", "wh-lagos-02"))
                .courierPartnerIds(List.of("courier-01"))
                .hubId("hub-lagos")
                .coordinates(Zone.Coordinates.builder()
                        .centerLatitude(6.5244)
                        .centerLongitude(3.3792)
                        .radiusKm(50.0)
                        .build())
                .status("ACTIVE")
                .build();

        abujaZone = Zone.builder()
                .id("zone-002")
                .zoneId("zone-abuja")
                .zoneName("Abuja Zone")
                .name("Abuja")
                .state("FCT")
                .country("Nigeria")
                .warehouseIds(List.of("wh-abuja-01"))
                .coordinates(Zone.Coordinates.builder()
                        .centerLatitude(9.0579)
                        .centerLongitude(7.4951)
                        .radiusKm(40.0)
                        .build())
                .status("ACTIVE")
                .build();
    }

    @Test
    void getWarehousesForZone_returnsMatchingZones() {
        when(zoneRepository.findByWarehouseIdsContaining("wh-lagos-01"))
                .thenReturn(List.of(lagosZone));

        List<Zone> result = zoneService.getWarehousesForZone("wh-lagos-01");

        assertEquals(1, result.size());
        assertEquals("zone-lagos", result.get(0).getZoneId());
        verify(zoneRepository).findByWarehouseIdsContaining("wh-lagos-01");
    }

    @Test
    void resolveZone_withinRadius_returnsZone() {
        when(zoneRepository.findAll()).thenReturn(List.of(lagosZone, abujaZone));

        Zone result = zoneService.resolveZone(6.5, 3.4);

        assertNotNull(result);
        assertEquals("zone-lagos", result.getZoneId());
    }

    @Test
    void resolveZone_noMatch_returnsFirstZone() {
        Zone zoneWithoutCoords = Zone.builder()
                .id("z-1").zoneId("zone-test").zoneName("Test").build();
        when(zoneRepository.findAll()).thenReturn(List.of(zoneWithoutCoords));

        Zone result = zoneService.resolveZone(0, 0);

        assertNotNull(result);
        assertEquals("zone-test", result.getZoneId());
    }

    @Test
    void resolveZone_noZones_throws() {
        when(zoneRepository.findAll()).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> zoneService.resolveZone(0, 0));
    }

    @Test
    void findNearbyZones_returnsMatchingZones() {
        when(zoneRepository.findAll()).thenReturn(List.of(lagosZone, abujaZone));

        List<Zone> result = zoneService.findNearbyZones(6.5, 3.4, 50);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(z -> "zone-lagos".equals(z.getZoneId())));
    }

    @Test
    void findNearbyZones_noMatch_returnsEmpty() {
        when(zoneRepository.findAll()).thenReturn(List.of(lagosZone));

        List<Zone> result = zoneService.findNearbyZones(50.0, 0.0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void createZone_savesAndReturns() {
        when(zoneRepository.save(any(Zone.class))).thenReturn(lagosZone);

        Zone result = zoneService.createZone(lagosZone);

        assertNotNull(result);
        assertEquals("zone-lagos", result.getZoneId());
        verify(zoneRepository).save(lagosZone);
    }

    @Test
    void getZoneById_found() {
        when(zoneRepository.findById("zone-001")).thenReturn(Optional.of(lagosZone));

        Zone result = zoneService.getZoneById("zone-001");

        assertEquals("Lagos Zone", result.getZoneName());
    }

    @Test
    void getZoneById_notFound_throws() {
        when(zoneRepository.findById("xxx")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> zoneService.getZoneById("xxx"));
    }

    @Test
    void getZoneByZoneId_found() {
        when(zoneRepository.findByZoneId("zone-lagos")).thenReturn(Optional.of(lagosZone));

        Zone result = zoneService.getZoneByZoneId("zone-lagos");

        assertEquals("Lagos Zone", result.getZoneName());
    }

    @Test
    void getZoneByZoneId_notFound_throws() {
        when(zoneRepository.findByZoneId("xxx")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> zoneService.getZoneByZoneId("xxx"));
    }

    @Test
    void getAllZones_returnsAll() {
        when(zoneRepository.findAll()).thenReturn(List.of(lagosZone, abujaZone));

        List<Zone> result = zoneService.getAllZones();

        assertEquals(2, result.size());
    }
}
