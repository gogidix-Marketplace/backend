package com.gogidix.sales.territory.infrastructure.service;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.port.out.OverlapDetectionService;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Overlap Detection Service Implementation
 * Detects overlaps between territories
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OverlapDetectionServiceImpl implements OverlapDetectionService {

    private final TerritoryRepository territoryRepository;

    @Override
    public List<TerritoryOverlap> detectOverlaps(Territory territory) {
        List<TerritoryOverlap> overlaps = new ArrayList<>();

        if (territory.getType() != Territory.TerritoryType.GEOGRAPHIC) {
            log.debug("Non-geographic territory, skipping overlap detection");
            return overlaps;
        }

        List<Territory> existingTerritories = territoryRepository.findGeographicTerritoriesByTenantId(
            territory.getTenantId()
        );

        for (Territory existing : existingTerritories) {
            if (existing.getTerritoryId().equals(territory.getTerritoryId())) {
                continue;
            }

            if (hasOverlap(territory, existing)) {
                overlaps.add(new TerritoryOverlap(
                    existing.getTerritoryId(),
                    existing.getName(),
                    describeOverlap(territory, existing),
                    calculateOverlapPercentage(territory, existing)
                ));
            }
        }

        return overlaps;
    }

    @Override
    public boolean hasOverlap(Territory territory1, Territory territory2) {
        if (territory1 == null || territory2 == null) {
            return false;
        }

        if (territory1.getType() != Territory.TerritoryType.GEOGRAPHIC ||
            territory2.getType() != Territory.TerritoryType.GEOGRAPHIC) {
            return false;
        }

        if (territory1.getGeographicBoundary() == null || territory2.getGeographicBoundary() == null) {
            return false;
        }

        return territory1.getGeographicBoundary().overlapsWith(territory2.getGeographicBoundary());
    }

    private String describeOverlap(Territory territory1, Territory territory2) {
        var boundary1 = territory1.getGeographicBoundary();
        var boundary2 = territory2.getGeographicBoundary();

        if (boundary1.getBoundingBox() != null && boundary2.getBoundingBox() != null) {
            return "Geographic overlap detected";
        }

        if (boundary1.getStates() != null && boundary2.getStates() != null) {
            List<String> commonStates = new ArrayList<>(boundary1.getStates());
            commonStates.retainAll(boundary2.getStates());
            if (!commonStates.isEmpty()) {
                return "Overlapping states: " + commonStates;
            }
        }

        if (boundary1.getCities() != null && boundary2.getCities() != null) {
            List<String> commonCities = new ArrayList<>(boundary1.getCities());
            commonCities.retainAll(boundary2.getCities());
            if (!commonCities.isEmpty()) {
                return "Overlapping cities: " + commonCities;
            }
        }

        if (boundary1.getPostalCodes() != null && boundary2.getPostalCodes() != null) {
            List<String> commonPostalCodes = new ArrayList<>(boundary1.getPostalCodes());
            commonPostalCodes.retainAll(boundary2.getPostalCodes());
            if (!commonPostalCodes.isEmpty()) {
                return "Overlapping postal codes: " + commonPostalCodes;
            }
        }

        return "Geographic boundary overlap";
    }

    private Double calculateOverlapPercentage(Territory territory1, Territory territory2) {
        // Simplified overlap calculation
        // In a real implementation, you would use polygon intersection libraries
        var boundary1 = territory1.getGeographicBoundary();
        var boundary2 = territory2.getGeographicBoundary();

        if (boundary1.getBoundingBox() == null || boundary2.getBoundingBox() == null) {
            return null;
        }

        // Very rough estimation based on shared attributes
        double sharedAttributes = 0;
        double totalAttributes = 0;

        if (boundary1.getStates() != null && boundary2.getStates() != null) {
            totalAttributes += boundary1.getStates().size();
            List<String> commonStates = new ArrayList<>(boundary1.getStates());
            commonStates.retainAll(boundary2.getStates());
            sharedAttributes += commonStates.size();
        }

        if (boundary1.getCities() != null && boundary2.getCities() != null) {
            totalAttributes += boundary1.getCities().size();
            List<String> commonCities = new ArrayList<>(boundary1.getCities());
            commonCities.retainAll(boundary2.getCities());
            sharedAttributes += commonCities.size();
        }

        if (totalAttributes > 0) {
            return (sharedAttributes / totalAttributes) * 100.0;
        }

        return 50.0; // Default for boundary overlaps
    }
}
