package com.gogidix.sales.onboarding.infrastructure.persistence.mongo;

import com.gogidix.sales.onboarding.domain.model.DocumentChecklist;
import com.gogidix.sales.onboarding.domain.repository.DocumentChecklistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - DocumentChecklist
 * Implements document checklist persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDocumentChecklistRepository implements DocumentChecklistRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DocumentChecklist save(DocumentChecklist checklist) {
        log.debug("Saving checklist: {} for tenant: {}",
                checklist.getChecklistId(), checklist.getTenantId());
        return mongoTemplate.save(checklist);
    }

    @Override
    public List<DocumentChecklist> saveAll(List<DocumentChecklist> checklists) {
        return checklists.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<DocumentChecklist> findById(String id) {
        String tenantId = com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DocumentChecklist.class));
    }

    @Override
    public Optional<DocumentChecklist> findByChecklistIdAndTenantId(String checklistId, String tenantId) {
        Query query = Query.query(
                Criteria.where("checklistId").is(checklistId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DocumentChecklist.class));
    }

    @Override
    public Optional<DocumentChecklist> findByOnboardingId(String onboardingId) {
        Query query = Query.query(Criteria.where("onboardingId").is(onboardingId));
        return Optional.ofNullable(mongoTemplate.findOne(query, DocumentChecklist.class));
    }

    @Override
    public List<DocumentChecklist> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DocumentChecklist.class);
    }

    @Override
    public List<DocumentChecklist> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("customerId").is(customerId)
        );
        return mongoTemplate.find(query, DocumentChecklist.class);
    }

    @Override
    public List<DocumentChecklist> findByOnboardingIdAndTenantId(String onboardingId, String tenantId) {
        Query query = Query.query(
                Criteria.where("onboardingId").is(onboardingId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, DocumentChecklist.class);
    }

    @Override
    public List<DocumentChecklist> findPendingVerification(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("completed").is(false)
        );
        return mongoTemplate.find(query, DocumentChecklist.class);
    }

    @Override
    public List<DocumentChecklist> findCompleted(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("completed").is(true)
        );
        return mongoTemplate.find(query, DocumentChecklist.class);
    }

    @Override
    public boolean existsByChecklistIdAndTenantId(String checklistId, String tenantId) {
        Query query = Query.query(
                Criteria.where("checklistId").is(checklistId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DocumentChecklist.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DocumentChecklist.class);
    }

    @Override
    public void deleteByChecklistIdAndTenantId(String checklistId, String tenantId) {
        Query query = Query.query(
                Criteria.where("checklistId").is(checklistId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DocumentChecklist.class);
    }

    @Override
    public void deleteByOnboardingId(String onboardingId) {
        Query query = Query.query(Criteria.where("onboardingId").is(onboardingId));
        mongoTemplate.remove(query, DocumentChecklist.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, DocumentChecklist.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, DocumentChecklist.class);
    }

    @Override
    public long countByTenantIdAndCompleted(String tenantId, Boolean completed) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("completed").is(completed)
        );
        return mongoTemplate.count(query, DocumentChecklist.class);
    }
}
