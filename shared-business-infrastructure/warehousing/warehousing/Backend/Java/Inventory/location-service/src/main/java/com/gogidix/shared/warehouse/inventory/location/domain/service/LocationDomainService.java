package com.gogidix.shared.warehouse.inventory.location.domain.service;

import com.gogidix.shared.warehouse.inventory.location.domain.entity.StorageLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Domain Service for Location Operations
 */
@Slf4j
@Service
public class LocationDomainService {

    /**
     * Validate location code format
     * Expected format: ZONE-AISLE-SHELF-BIN (e.g., A-01-01-01)
     */
    public boolean isValidLocationCode(String locationCode) {
        if (locationCode == null || locationCode.isBlank()) {
            return false;
        }

        String pattern = "^[A-Z]-\\d{2}-\\d{2}-\\d{2}$";
        return locationCode.matches(pattern);
    }

    /**
     * Generate location path from location code
     */
    public String generateLocationPath(String zone, String aisle, String shelf, String bin) {
        return String.format("%s-%s-%s-%s", zone, aisle, shelf, bin);
    }

    /**
     * Check if location is at capacity
     */
    public boolean isAtCapacity(StorageLocation location) {
        return location.getOccupied() >= location.getCapacity();
    }

    /**
     * Check if location can accommodate additional items
     */
    public boolean hasCapacity(StorageLocation location, int requiredSpace) {
        return (location.getOccupied() + requiredSpace) <= location.getCapacity();
    }

    /**
     * Calculate location utilization percentage
     */
    public double calculateUtilization(StorageLocation location) {
        if (location.getCapacity() == 0) {
            return 0.0;
        }
        return (double) location.getOccupied() / location.getCapacity() * 100;
    }

    /**
     * Determine if location needs restocking
     */
    public boolean needsRestocking(StorageLocation location) {
        return calculateUtilization(location) < 20.0;
    }
}
