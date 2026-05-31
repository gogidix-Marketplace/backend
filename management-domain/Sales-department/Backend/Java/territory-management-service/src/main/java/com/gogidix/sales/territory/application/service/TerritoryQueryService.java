package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.port.out.OverlapDetectionService;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Territory Query Service
 * Handles all read operations for territories
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryQueryService {

    private final TerritoryRepository territoryRepository;
    private final OverlapDetectionService overlapDetectionService;
    private final MongoTemplate mongoTemplate;

    public Territory getById(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTerritoryIdAndTenantId(territoryId, tenantId)
            .orElseThrow(() -> new NotFoundException("Territory", territoryId));
    }

    public Territory getByCode(String code) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByCodeAndTenantId(code, tenantId)
            .orElseThrow(() -> new NotFoundException("Territory", code));
    }

    public List<Territory> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantId(tenantId);
    }

    public List<Territory> getActiveForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findActiveByTenantId(tenantId);
    }

    public List<Territory> getByStatus(Territory.TerritoryStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Territory> getByType(Territory.TerritoryType type) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantIdAndType(tenantId, type);
    }

    public List<Territory> getByRegionId(String regionId) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantIdAndRegionId(tenantId, regionId);
    }

    public List<Territory> getByManagerId(String managerId) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantIdAndManagerId(tenantId, managerId);
    }

    public List<Territory> getChildTerritories(String parentTerritoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findByTenantIdAndParentTerritoryId(tenantId, parentTerritoryId);
    }

    public List<Territory> getPendingRealignment() {
        String tenantId = RequestContextHolder.getTenantId();
        return territoryRepository.findPendingRealignmentByTenantId(tenantId);
    }

    public List<OverlapDetectionService.TerritoryOverlap> checkOverlaps(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(territoryId, tenantId)
            .orElseThrow(() -> new NotFoundException("Territory", territoryId));

        return overlapDetectionService.detectOverlaps(territory);
    }

    public TerritorySummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long totalTerritories = territoryRepository.countByTenantId(tenantId);
        long activeTerritories = territoryRepository.countByTenantIdAndStatus(tenantId, Territory.TerritoryStatus.ACTIVE);
        long inactiveTerritories = territoryRepository.countByTenantIdAndStatus(tenantId, Territory.TerritoryStatus.INACTIVE);
        long pendingRealignment = territoryRepository.findPendingRealignmentByTenantId(tenantId).size();

        List<Territory> allTerritories = territoryRepository.findByTenantId(tenantId);
        Map<Territory.TerritoryType, Long> countByType = allTerritories.stream()
            .collect(Collectors.groupingBy(Territory::getType, Collectors.counting()));

        return TerritorySummary.builder()
            .totalTerritories(totalTerritories)
            .activeTerritories(activeTerritories)
            .inactiveTerritories(inactiveTerritories)
            .pendingRealignment(pendingRealignment)
            .countByType(countByType)
            .build();
    }

    public Page<Territory> searchTerritories(String searchTerm, Territory.TerritoryType type,
                                              Territory.TerritoryStatus status,
                                              Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();

        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            Criteria searchCriteria = new Criteria().orOperator(
                Criteria.where("name").regex(searchTerm, "i"),
                Criteria.where("code").regex(searchTerm, "i"),
                Criteria.where("description").regex(searchTerm, "i")
            );
            criteria = criteria.andOperator(searchCriteria);
        }

        if (type != null) {
            criteria = criteria.and("type").is(type);
        }

        if (status != null) {
            criteria = criteria.and("status").is(status);
        }

        Query query = Query.query(criteria).with(pageable);
        List<Territory> results = mongoTemplate.find(query, Territory.class);

        long count = mongoTemplate.count(Query.query(criteria), Territory.class);

        return new PageImpl<>(results, pageable, count);
    }

    /**
     * Territory Summary DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TerritorySummary {
        private Long totalTerritories;
        private Long activeTerritories;
        private Long inactiveTerritories;
        private Long pendingRealignment;
        private Map<Territory.TerritoryType, Long> countByType;
    }
}
