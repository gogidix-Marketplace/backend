package com.gogidix.sales.dealmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Deal Repository Implementation
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDealRepository implements DealRepository {

    private final DealMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Deal save(Deal deal) {
        return mongoRepository.save(deal);
    }

    @Override
    public Optional<Deal> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Deal> findByDealIdAndTenantId(String dealId, String tenantId) {
        return mongoRepository.findByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public List<Deal> findAllByTenantId(String tenantId) {
        return mongoRepository.findAllByTenantId(tenantId);
    }

    @Override
    public List<Deal> findByTenantIdAndStatus(String tenantId, Deal.DealStatus status) {
        return mongoRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<Deal> findByTenantIdAndStage(String tenantId, Deal.DealStage stage) {
        return mongoRepository.findByTenantIdAndStage(tenantId, stage);
    }

    @Override
    public List<Deal> findByOwnerIdAndTenantId(String ownerId, String tenantId) {
        return mongoRepository.findByOwnerIdAndTenantId(ownerId, tenantId);
    }

    @Override
    public List<Deal> findByAccountIdAndTenantId(String accountId, String tenantId) {
        return mongoRepository.findByAccountIdAndTenantId(accountId, tenantId);
    }

    @Override
    public List<Deal> findByTenantIdAndExpectedCloseDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        return mongoRepository.findByTenantIdAndExpectedCloseDateBetween(tenantId, startDate, endDate);
    }

    @Override
    public List<Deal> findByTenantIdAndStageIn(String tenantId, List<Deal.DealStage> stages) {
        return mongoRepository.findByTenantIdAndStageIn(tenantId, stages);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByDealIdAndTenantId(String dealId, String tenantId) {
        mongoRepository.deleteByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public boolean existsByDealIdAndTenantId(String dealId, String tenantId) {
        return mongoRepository.existsByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public List<Deal> findPipelineDeals(String tenantId) {
        List<Deal.DealStage> pipelineStages = List.of(
                Deal.DealStage.LEAD,
                Deal.DealStage.QUALIFIED,
                Deal.DealStage.PROPOSAL,
                Deal.DealStage.NEGOTIATION,
                Deal.DealStage.VERBAL_COMMIT
        );
        return mongoRepository.findByTenantIdAndStageIn(tenantId, pipelineStages);
    }

    @Override
    public List<Deal> findForecastDeals(String tenantId, LocalDate asOfDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").is(Deal.DealStatus.OPEN));
        query.addCriteria(Criteria.where("expectedCloseDate").gte(asOfDate));
        return mongoTemplate.find(query, Deal.class);
    }

    @Override
    public List<Deal> searchDeals(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        Query query = TextQuery.queryText(textCriteria).addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").ne(Deal.DealStatus.LOST));
        return mongoTemplate.find(query, Deal.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Deal.DealStatus status) {
        return mongoRepository.countByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public long countByTenantIdAndStage(String tenantId, Deal.DealStage stage) {
        return mongoRepository.countByTenantIdAndStage(tenantId, stage);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface DealMongoRepository extends MongoRepository<Deal, String> {

        Optional<Deal> findByDealIdAndTenantId(String dealId, String tenantId);

        List<Deal> findAllByTenantId(String tenantId);

        List<Deal> findByTenantIdAndStatus(String tenantId, Deal.DealStatus status);

        List<Deal> findByTenantIdAndStage(String tenantId, Deal.DealStage stage);

        List<Deal> findByOwnerIdAndTenantId(String ownerId, String tenantId);

        List<Deal> findByAccountIdAndTenantId(String accountId, String tenantId);

        List<Deal> findByTenantIdAndExpectedCloseDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

        List<Deal> findByTenantIdAndStageIn(String tenantId, List<Deal.DealStage> stages);

        void deleteByDealIdAndTenantId(String dealId, String tenantId);

        boolean existsByDealIdAndTenantId(String dealId, String tenantId);

        long countByTenantIdAndStatus(String tenantId, Deal.DealStatus status);

        long countByTenantIdAndStage(String tenantId, Deal.DealStage stage);

        Page<Deal> findByTenantId(String tenantId, Pageable pageable);
    }
}
