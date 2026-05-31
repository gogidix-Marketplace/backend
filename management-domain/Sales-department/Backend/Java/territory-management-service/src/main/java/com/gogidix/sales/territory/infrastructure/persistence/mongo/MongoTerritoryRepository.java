package com.gogidix.sales.territory.infrastructure.persistence.mongo;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Territory
 * Implements territory persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTerritoryRepository implements TerritoryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Territory save(Territory territory) {
        log.debug("Saving territory: {} for tenant: {}",
            territory.getTerritoryId(), territory.getTenantId());
        return mongoTemplate.save(territory);
    }

    @Override
    public List<Territory> saveAll(List<Territory> territories) {
        return territories.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Territory> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Territory.class));
    }

    @Override
    public Optional<Territory> findByTerritoryIdAndTenantId(String territoryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("territoryId").is(territoryId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Territory.class));
    }

    @Override
    public List<Territory> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndStatus(String tenantId, Territory.TerritoryStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndType(String tenantId, Territory.TerritoryType type) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("type").is(type)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndRegionId(String tenantId, String regionId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("regionId").is(regionId)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndManagerId(String tenantId, String managerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("managerId").is(managerId)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndParentTerritoryId(String tenantId, String parentTerritoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("parentTerritoryId").is(parentTerritoryId)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Territory.TerritoryStatus.ACTIVE)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findPendingRealignmentByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("pendingRealignment").is(true)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public Optional<Territory> findByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
            Criteria.where("code").is(code)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Territory.class));
    }

    @Override
    public boolean existsByCodeAndTenantId(String code, String tenantId) {
        Query query = Query.query(
            Criteria.where("code").is(code)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Territory.class);
    }

    @Override
    public boolean existsByTerritoryIdAndTenantId(String territoryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("territoryId").is(territoryId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Territory.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Territory.class);
    }

    @Override
    public void deleteByTerritoryIdAndTenantId(String territoryId, String tenantId) {
        Query query = Query.query(
            Criteria.where("territoryId").is(territoryId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Territory.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Territory.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Territory.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Territory.TerritoryStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Territory.class);
    }

    @Override
    public List<Territory> findGeographicTerritoriesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("type").is(Territory.TerritoryType.GEOGRAPHIC)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndProductCategoriesContaining(String tenantId, String productCategory) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("productCategories").is(productCategory)
        );
        return mongoTemplate.find(query, Territory.class);
    }

    @Override
    public List<Territory> findByTenantIdAndCustomerSegmentsContaining(String tenantId, String customerSegment) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerSegments").is(customerSegment)
        );
        return mongoTemplate.find(query, Territory.class);
    }
}
