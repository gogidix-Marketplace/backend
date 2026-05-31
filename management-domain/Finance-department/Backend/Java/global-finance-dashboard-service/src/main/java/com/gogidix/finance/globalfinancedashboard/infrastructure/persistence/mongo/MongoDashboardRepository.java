package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.DashboardRepository;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Dashboard
 * Implements dashboard persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDashboardRepository implements DashboardRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Dashboard save(Dashboard dashboard) {
        log.debug("Saving dashboard: {} for tenant: {}",
                dashboard.getDashboardId(), dashboard.getTenantId());
        return mongoTemplate.save(dashboard);
    }

    @Override
    public Optional<Dashboard> findById(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Dashboard.class));
    }

    @Override
    public Optional<Dashboard> findByShareToken(String shareToken) {
        Query query = Query.query(
                Criteria.where("shareToken").is(shareToken)
                        .and("shareTokenExpiry").gt(java.time.Instant.now())
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Dashboard.class));
    }

    @Override
    public List<Dashboard> findAll(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public List<Dashboard> findByOwner(String tenantId, String owner) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("owner").is(owner)
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public List<Dashboard> findSharedWith(String tenantId, String userId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("sharedWith").in(userId)
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public List<Dashboard> findSharedWithGroup(String tenantId, String groupId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("sharedWithGroups").in(groupId)
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public Optional<Dashboard> findDefault(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isDefault").is(true)
                        .and("status").is(Dashboard.DashboardStatus.ACTIVE)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Dashboard.class));
    }

    @Override
    public List<Dashboard> findByType(String tenantId, Dashboard.DashboardType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public List<Dashboard> searchByName(String tenantId, String namePattern) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("name").regex(namePattern, "i")
                        .and("status").ne(Dashboard.DashboardStatus.DELETED)
        );
        return mongoTemplate.find(query, Dashboard.class);
    }

    @Override
    public void delete(Dashboard dashboard) {
        log.debug("Deleting dashboard: {} for tenant: {}",
                dashboard.getDashboardId(), dashboard.getTenantId());
        mongoTemplate.remove(dashboard);
    }

    @Override
    public boolean existsById(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Dashboard.class);
    }
}
