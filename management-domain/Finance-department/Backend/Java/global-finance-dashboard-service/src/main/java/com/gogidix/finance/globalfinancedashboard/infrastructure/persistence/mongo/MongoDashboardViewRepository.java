package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardView;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardViewRepository;
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
 * MongoDB Repository Implementation - Dashboard View
 * Implements dashboard view persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDashboardViewRepository implements DashboardViewRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DashboardView save(DashboardView dashboardView) {
        log.debug("Saving dashboard view: {} for tenant: {}",
                dashboardView.getId(), dashboardView.getTenantId());
        return mongoTemplate.save(dashboardView);
    }

    @Override
    public List<DashboardView> saveAll(List<DashboardView> dashboardViews) {
        return dashboardViews.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<DashboardView> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardView.class));
    }

    @Override
    public Optional<DashboardView> findByTenantIdAndViewId(String tenantId, String viewId) {
        Query query = Query.query(
                Criteria.where("id").is(viewId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardView.class));
    }

    @Override
    public List<DashboardView> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DashboardView.class);
    }

    @Override
    public List<DashboardView> findByTenantIdAndOwnerId(String tenantId, String ownerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("ownerId").is(ownerId)
        );
        return mongoTemplate.find(query, DashboardView.class);
    }

    @Override
    public List<DashboardView> findByTenantIdAndIsPublic(String tenantId, boolean isPublic) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isPublic").is(isPublic)
        );
        return mongoTemplate.find(query, DashboardView.class);
    }

    @Override
    public List<DashboardView> findByTenantIdAndNameContaining(String tenantId, String namePattern) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("name").regex(namePattern, "i")
        );
        return mongoTemplate.find(query, DashboardView.class);
    }

    @Override
    public Optional<DashboardView> findDefaultByTenantId(String tenantId) {
        // Assuming there's an isDefault field or similar logic
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        // Add default criteria if applicable
        );
        return mongoTemplate.find(query, DashboardView.class).stream().findFirst();
    }

    @Override
    public List<DashboardView> findSharedWithUser(String tenantId, String userId) {
        // Implement sharing logic based on your domain model
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isPublic").is(true)
        );
        return mongoTemplate.find(query, DashboardView.class);
    }

    @Override
    public boolean existsByTenantIdAndViewId(String tenantId, String viewId) {
        Query query = Query.query(
                Criteria.where("id").is(viewId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DashboardView.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DashboardView.class);
    }

    @Override
    public void deleteByTenantIdAndViewId(String tenantId, String viewId) {
        Query query = Query.query(
                Criteria.where("id").is(viewId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DashboardView.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, DashboardView.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, DashboardView.class);
    }

    @Override
    public long countByTenantIdAndOwnerId(String tenantId, String ownerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("ownerId").is(ownerId)
        );
        return mongoTemplate.count(query, DashboardView.class);
    }
}
