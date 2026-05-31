package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.port.in.TerritoryCommand;
import com.gogidix.sales.territory.domain.port.out.EventPublisher;
import com.gogidix.sales.territory.domain.port.out.OverlapDetectionService;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import com.gogidix.sales.territory.shared.exception.ConflictException;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.exception.ValidationException;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Territory Command Service
 * Handles all write operations for territories
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryCommandService {

    private final TerritoryRepository territoryRepository;
    private final EventPublisher eventPublisher;
    private final OverlapDetectionService overlapDetectionService;

    @Transactional
    public Territory create(TerritoryCommand.CreateTerritoryCommand command) {
        log.info("Creating territory: {} for tenant: {}", command.getCode(), command.getTenantId());

        // Check if territory code already exists
        if (territoryRepository.existsByCodeAndTenantId(command.getCode(), command.getTenantId())) {
            throw new ConflictException("Territory", "Code already exists: " + command.getCode());
        }

        Territory territory = Territory.create(
            command.getTenantId(),
            command.getName(),
            command.getCode(),
            command.getType(),
            RequestContextHolder.getUserId()
        );

        // Set optional fields
        territory.setDescription(command.getDescription());
        territory.setRegionId(command.getRegionId());
        territory.setManagerId(command.getManagerId());
        territory.setPriority(command.getPriority());
        territory.setParentTerritoryId(command.getParentTerritoryId());

        // Set geographic boundary if provided
        if (command.getGeographicBoundary() != null) {
            territory.setGeographicBoundaryData(command.getGeographicBoundary());
        }

        // Set product categories if provided
        if (command.getProductCategories() != null) {
            territory.getProductCategories().addAll(command.getProductCategories());
        }

        // Set product IDs if provided
        if (command.getProductIds() != null) {
            territory.getProductIds().addAll(command.getProductIds());
        }

        // Set customer segments if provided
        if (command.getCustomerSegments() != null) {
            territory.getCustomerSegments().addAll(command.getCustomerSegments());
        }

        // Set customer tier IDs if provided
        if (command.getCustomerTierIds() != null) {
            territory.getCustomerTierIds().addAll(command.getCustomerTierIds());
        }

        // Check for overlaps for geographic territories
        if (territory.getType() == Territory.TerritoryType.GEOGRAPHIC) {
            List<OverlapDetectionService.TerritoryOverlap> overlaps =
                overlapDetectionService.detectOverlaps(territory);
            if (!overlaps.isEmpty()) {
                log.warn("Territory overlaps detected: {}", overlaps);
                // Still allow creation but log the overlaps
            }
        }

        // Add to parent if specified
        if (command.getParentTerritoryId() != null) {
            Territory parent = territoryRepository.findByTerritoryIdAndTenantId(
                command.getParentTerritoryId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Parent Territory", command.getParentTerritoryId()));
            parent.addChildTerritory(territory.getTerritoryId());
            territoryRepository.save(parent);
        }

        Territory savedTerritory = territoryRepository.save(territory);
        publishEvents(savedTerritory);

        log.info("Created territory: {} for tenant: {}", savedTerritory.getTerritoryId(), command.getTenantId());
        return savedTerritory;
    }

    @Transactional
    public Territory update(TerritoryCommand.UpdateTerritoryCommand command) {
        log.info("Updating territory: {} for tenant: {}", command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        if (command.getName() != null) {
            territory.setName(command.getName());
        }
        if (command.getDescription() != null) {
            territory.setDescription(command.getDescription());
        }
        if (command.getRegionId() != null) {
            territory.setRegionId(command.getRegionId());
        }
        if (command.getManagerId() != null) {
            territory.setManagerId(command.getManagerId());
        }
        if (command.getPriority() != null) {
            territory.setPriority(command.getPriority());
        }
        if (command.getGeographicBoundary() != null) {
            territory.setGeographicBoundaryData(command.getGeographicBoundary());
        }
        if (command.getProductCategories() != null) {
            territory.setProductCategories(command.getProductCategories());
        }
        if (command.getProductIds() != null) {
            territory.setProductIds(command.getProductIds());
        }
        if (command.getCustomerSegments() != null) {
            territory.setCustomerSegments(command.getCustomerSegments());
        }
        if (command.getCustomerTierIds() != null) {
            territory.setCustomerTierIds(command.getCustomerTierIds());
        }

        Territory savedTerritory = territoryRepository.save(territory);
        publishEvents(savedTerritory);

        return savedTerritory;
    }

    @Transactional
    public void activate(TerritoryCommand.ActivateTerritoryCommand command) {
        log.info("Activating territory: {} for tenant: {}", command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.activate();
        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Activated territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void deactivate(TerritoryCommand.DeactivateTerritoryCommand command) {
        log.info("Deactivating territory: {} for tenant: {}", command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.deactivate();
        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Deactivated territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void archive(TerritoryCommand.ArchiveTerritoryCommand command) {
        log.info("Archiving territory: {} for tenant: {}", command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.archive();
        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Archived territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void requestRealignment(TerritoryCommand.RequestRealignmentCommand command) {
        log.info("Requesting realignment for territory: {} for tenant: {}",
            command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.requestRealignment(command.getRequestedBy());
        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Requested realignment for territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void completeRealignment(TerritoryCommand.CompleteRealignmentCommand command) {
        log.info("Completing realignment for territory: {} for tenant: {}",
            command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.completeRealignment();
        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Completed realignment for territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void delete(TerritoryCommand.DeleteTerritoryCommand command) {
        log.info("Deleting territory: {} for tenant: {}", command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        // Check if territory has child territories
        if (territory.getChildTerritoryIds() != null && !territory.getChildTerritoryIds().isEmpty()) {
            throw new ValidationException("Cannot delete territory with child territories");
        }

        // Remove from parent if exists
        if (territory.getParentTerritoryId() != null) {
            Territory parent = territoryRepository.findByTerritoryIdAndTenantId(
                territory.getParentTerritoryId(), command.getTenantId()).orElse(null);
            if (parent != null) {
                parent.removeChildTerritory(territory.getTerritoryId());
                territoryRepository.save(parent);
            }
        }

        territoryRepository.deleteByTerritoryIdAndTenantId(command.getTerritoryId(), command.getTenantId());

        log.info("Deleted territory: {}", command.getTerritoryId());
    }

    @Transactional
    public void addChildTerritory(TerritoryCommand.AddChildTerritoryCommand command) {
        log.info("Adding child territory: {} to parent: {} for tenant: {}",
            command.getChildTerritoryId(), command.getParentTerritoryId(), command.getTenantId());

        Territory parent = territoryRepository.findByTerritoryIdAndTenantId(
            command.getParentTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Parent Territory", command.getParentTerritoryId()));

        Territory child = territoryRepository.findByTerritoryIdAndTenantId(
            command.getChildTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Child Territory", command.getChildTerritoryId()));

        parent.addChildTerritory(child.getTerritoryId());
        child.setParentTerritoryId(parent.getTerritoryId());

        territoryRepository.save(parent);
        territoryRepository.save(child);

        log.info("Added child territory: {} to parent: {}", command.getChildTerritoryId(), command.getParentTerritoryId());
    }

    @Transactional
    public void removeChildTerritory(TerritoryCommand.RemoveChildTerritoryCommand command) {
        log.info("Removing child territory: {} from parent: {} for tenant: {}",
            command.getChildTerritoryId(), command.getParentTerritoryId(), command.getTenantId());

        Territory parent = territoryRepository.findByTerritoryIdAndTenantId(
            command.getParentTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Parent Territory", command.getParentTerritoryId()));

        Territory child = territoryRepository.findByTerritoryIdAndTenantId(
            command.getChildTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Child Territory", command.getChildTerritoryId()));

        parent.removeChildTerritory(child.getTerritoryId());
        child.setParentTerritoryId(null);

        territoryRepository.save(parent);
        territoryRepository.save(child);

        log.info("Removed child territory: {} from parent: {}", command.getChildTerritoryId(), command.getParentTerritoryId());
    }

    @Transactional
    public void updatePerformance(TerritoryCommand.UpdatePerformanceCommand command) {
        log.info("Updating performance for territory: {} for tenant: {}",
            command.getTerritoryId(), command.getTenantId());

        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        territory.updatePerformance(
            command.getCurrentSales(),
            command.getQuota(),
            command.getAccountsCount(),
            command.getDealsCount()
        );

        territoryRepository.save(territory);
        publishEvents(territory);

        log.info("Updated performance for territory: {}", command.getTerritoryId());
    }

    private void publishEvents(Territory territory) {
        if (!territory.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(territory.getDomainEvents());
            territory.clearDomainEvents();
        }
    }
}
