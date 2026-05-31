package com.gogidix.sales.leadmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import com.gogidix.sales.leadmanagement.domain.repository.LeadActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Lead Activity Repository Implementation
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoLeadActivityRepository implements LeadActivityRepository {

    private final LeadActivityMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public LeadActivity save(LeadActivity activity) {
        return mongoRepository.save(activity);
    }

    @Override
    public Optional<LeadActivity> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<LeadActivity> findByActivityIdAndTenantId(String activityId, String tenantId) {
        return mongoRepository.findByActivityIdAndTenantId(activityId, tenantId);
    }

    @Override
    public List<LeadActivity> findByLeadIdAndTenantId(String leadId, String tenantId) {
        return mongoRepository.findByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public List<LeadActivity> findByTenantIdAndCreatedBy(String tenantId, String createdBy) {
        return mongoRepository.findByTenantIdAndCreatedBy(tenantId, createdBy);
    }

    @Override
    public List<LeadActivity> findByTenantIdAndActivityType(String tenantId, LeadActivity.ActivityType activityType) {
        return mongoRepository.findByTenantIdAndActivityType(tenantId, activityType);
    }

    @Override
    public List<LeadActivity> findByTenantIdAndStatus(String tenantId, LeadActivity.ActivityStatus status) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<LeadActivity> findPendingActivitiesByDueDateBefore(String tenantId, Instant dueDate) {
        return mongoRepository.findByTenantIdAndStatusAndDueDateBefore(
                tenantId, LeadActivity.ActivityStatus.PENDING, dueDate);
    }

    @Override
    public List<LeadActivity> findOverdueActivities(String tenantId) {
        return mongoRepository.findByTenantIdAndStatusAndDueDateBefore(
                tenantId, LeadActivity.ActivityStatus.PENDING, Instant.now());
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByLeadIdAndTenantId(String leadId, String tenantId) {
        mongoRepository.deleteByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public void deleteByActivityIdAndTenantId(String activityId, String tenantId) {
        mongoRepository.deleteByActivityIdAndTenantId(activityId, tenantId);
    }

    @Override
    public boolean existsByActivityIdAndTenantId(String activityId, String tenantId) {
        return mongoRepository.existsByActivityIdAndTenantId(activityId, tenantId);
    }

    @Override
    public long countByLeadIdAndTenantId(String leadId, String tenantId) {
        return mongoRepository.countByLeadIdAndTenantId(leadId, tenantId);
    }

    @Override
    public List<LeadActivity> findRecentActivitiesByLeadId(String leadId, String tenantId, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("leadId").is(leadId));
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, LeadActivity.class).stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<LeadActivity> findUpcomingActivitiesForUser(String tenantId, String userId, Instant since) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("createdBy").is(userId));
        query.addCriteria(Criteria.where("status").is(LeadActivity.ActivityStatus.PENDING));
        query.addCriteria(Criteria.where("dueDate").gte(since));
        query.with(Sort.by(Sort.Direction.ASC, "dueDate"));
        return mongoTemplate.find(query, LeadActivity.class);
    }

    @Override
    public long countByLeadIdAndTenantIdAndActivityType(String leadId, String tenantId, LeadActivity.ActivityType activityType) {
        return mongoRepository.countByLeadIdAndTenantIdAndActivityType(leadId, tenantId, activityType);
    }

    @Override
    public List<LeadActivity> findByLeadIdAndTenantIdOrderByCreatedAtDesc(String leadId, String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("leadId").is(leadId));
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        return mongoTemplate.find(query, LeadActivity.class);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface LeadActivityMongoRepository extends MongoRepository<LeadActivity, String> {

        Optional<LeadActivity> findByActivityIdAndTenantId(String activityId, String tenantId);

        List<LeadActivity> findByLeadIdAndTenantId(String leadId, String tenantId);

        List<LeadActivity> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

        List<LeadActivity> findByTenantIdAndActivityType(String tenantId, LeadActivity.ActivityType activityType);

        List<LeadActivity> findByTenantIdAndStatus(String tenantId, LeadActivity.ActivityStatus status);

        List<LeadActivity> findByTenantIdAndStatusAndDueDateBefore(String tenantId, LeadActivity.ActivityStatus status, Instant dueDate);

        void deleteByLeadIdAndTenantId(String leadId, String tenantId);

        void deleteByActivityIdAndTenantId(String activityId, String tenantId);

        boolean existsByActivityIdAndTenantId(String activityId, String tenantId);

        long countByLeadIdAndTenantId(String leadId, String tenantId);

        long countByLeadIdAndTenantIdAndActivityType(String leadId, String tenantId, LeadActivity.ActivityType activityType);
    }
}
