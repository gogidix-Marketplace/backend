package com.gogidix.sales.territory.domain.port.out;

import com.gogidix.sales.territory.domain.model.Territory;

import java.util.List;

/**
 * Overlap Detection Service Interface (Port)
 * Defines the contract for detecting territory overlaps
 */
public interface OverlapDetectionService {

    /**
     * Checks for overlaps with existing territories
     */
    List<TerritoryOverlap> detectOverlaps(Territory territory);

    /**
     * Checks for overlaps between two territories
     */
    boolean hasOverlap(Territory territory1, Territory territory2);

    /**
     * Territory overlap result
     */
    record TerritoryOverlap(
            String overlappingTerritoryId,
            String overlappingTerritoryName,
            String overlapDescription,
            Double overlapPercentage
    ) {}
}
