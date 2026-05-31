package com.gogidix.sales.dealmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.repository.DealActivityRepository;
import lombok.RequiredArgsConstructor;
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
 * MongoDB Deal Activity Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class MongoDealActivityRepository implements DealActivityRepository {

    private final ActivityMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public DealActivity save(DealActivity activity) {
        return mongoRepository.save(activity);
    }

    @Override
    public Optional<DealActivity> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DealActivity> findByActivityIdAndTenantId(String activityId, String tenantId) {
        return mongoRepository.findByActivityIdAndTenantId(activityId, tenantId);
    }

    @Override
    public List<DealActivity> findByDealIdAndTenantId(String dealId, String tenantId) {
        return mongoRepository.findByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public List<DealActivity> findByDealIdAndTenantIdOrderByActivityDateDesc(String dealId, String tenantId) {
        return mongoRepository.findByDealIdAndTenantIdOrderByActivityDateDesc(dealId, tenantId);
    }

    @Override
    public List<DealActivity> findByUserIdAndTenantId(String userId, String tenantId) {
        return mongoRepository.findByUserIdAndTenantId(userId, tenantId);
    }

    @Override
    public List<DealActivity> findByTenantIdAndDueDateBefore(String tenantId, Instant dueDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("dueDate").lt(dueDate));
        query.addCriteria(Criteria.where("isCompleted").ne(true));
        return mongoTemplate.find(query, DealActivity.class);
    }

    @Override
    public List<DealActivity> findPendingActivitiesByTenantId(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("isCompleted").ne(true));
        query.with(Sort.by(Sort.Direction.ASC, "dueDate"));
        return mongoTemplate.find(query, DealActivity.class);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByActivityIdAndTenantId(String activityId, String tenantId) {
        mongoRepository.deleteByActivityIdAndTenantId(activityId, tenantId);
    }

    @Override
    public void deleteByDealIdAndTenantId(String dealId, String tenantId) {
        mongoRepository.deleteByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public boolean existsByActivityIdAndTenantId(String activityId, String tenantId) {
        return mongoRepository.existsByActivityIdAndTenantId(activityId, tenantId);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface ActivityMongoRepository extends MongoRepository<DealActivity, String> {

        Optional<DealActivity> findByActivityIdAndTenantId(String activityId, String tenantId);

        List<DealActivity> findByDealIdAndTenantId(String dealId, String tenantId);

        List<DealActivity> findByDealIdAndTenantIdOrderByActivityDateDesc(String dealId, String tenantId);

        List<DealActivity> findByUserIdAndTenantId(String userId, String tenantId);

        void deleteByActivityIdAndTenantId(String activityId, String tenantId);

        void deleteByDealIdAndTenantId(String dealId, String tenantId);

        boolean existsByActivityIdAndTenantId(String activityId, String tenantId);
    }
}
