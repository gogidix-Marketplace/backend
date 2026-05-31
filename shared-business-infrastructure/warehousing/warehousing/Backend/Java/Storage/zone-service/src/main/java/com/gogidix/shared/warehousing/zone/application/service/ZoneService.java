package com.gogidix.shared.warehousing.zone.application.service;

import com.gogidix.shared.warehousing.zone.domain.entity.Zone;
import com.gogidix.shared.warehousing.zone.domain.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public List<Zone> getWarehousesForZone(String zoneId) {
        log.info("Getting warehouses for zone {}", zoneId);
        return zoneRepository.findByWarehouseIdsContaining(zoneId);
    }

    public Zone resolveZone(double latitude, double longitude) {
        log.info("Resolving zone for lat={}, lng={}", latitude, longitude);
        List<Zone> allZones = zoneRepository.findAll();
        for (Zone zone : allZones) {
            if (zone.getCoordinates() != null && zone.getCoordinates().getCenterLatitude() != null) {
                double distance = calculateDistance(latitude, longitude,
                        zone.getCoordinates().getCenterLatitude(),
                        zone.getCoordinates().getCenterLongitude());
                Double radius = zone.getCoordinates().getRadiusKm();
                if (radius != null && distance <= radius) {
                    return zone;
                }
            }
        }
        if (!allZones.isEmpty()) {
            return allZones.get(0);
        }
        throw new IllegalArgumentException("No zones configured");
    }

    public List<Zone> findNearbyZones(double latitude, double longitude, double radiusKm) {
        log.info("Finding nearby zones for lat={}, lng={}, radius={}km", latitude, longitude, radiusKm);
        List<Zone> allZones = zoneRepository.findAll();
        return allZones.stream()
                .filter(zone -> {
                    if (zone.getCoordinates() != null && zone.getCoordinates().getCenterLatitude() != null) {
                        double distance = calculateDistance(latitude, longitude,
                                zone.getCoordinates().getCenterLatitude(),
                                zone.getCoordinates().getCenterLongitude());
                        return distance <= radiusKm;
                    }
                    return false;
                })
                .toList();
    }

    public Zone createZone(Zone zone) {
        log.info("Creating zone: {}", zone.getZoneName());
        return zoneRepository.save(zone);
    }

    public Zone getZoneById(String id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + id));
    }

    public Zone getZoneByZoneId(String zoneId) {
        return zoneRepository.findByZoneId(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + zoneId));
    }

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
