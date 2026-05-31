package com.gogidix.sales.territory.infrastructure.persistence.mongo;

import com.gogidix.sales.territory.domain.model.Quota;
import com.gogidix.sales.territory.domain.repository.QuotaRepository;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Quota
 * Implements quota persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoQuotaRepository implements QuotaRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Quota save(Quota quota) {
        log.debug("Saving quota: {} for tenant: {}",
            quota.getQuotaId(), quota.getTenantId());
        return mongoTemplate.save(quota);
    }

    @Override
    public List<Quota> saveAll(List<Quota> quotas) {
        return quotas.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Quota> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Quota.class));
    }

    @Override
    public Optional<Quota> findByQuotaIdAndTenantId(String quotaId, String tenantId) {
        Query query = Query.query(
            Criteria.where("quotaId").is(quotaId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Quota.class));
    }

    @Override
    public List<Quota> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndStatus(String tenantId, Quota.QuotaStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndType(String tenantId, Quota.QuotaType type) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("type").is(type)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndPeriod(String tenantId, Quota.QuotaPeriod period) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("period").is(period)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndYear(String tenantId, Integer year) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("year").is(year)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndYearAndMonth(String tenantId, Integer year, Integer month) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("year").is(year)
                .and("month").is(month)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findActiveByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
                .and("status").is(Quota.QuotaStatus.ACTIVE)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findActiveByTenantIdAndSalesRepresentativeId(String tenantId, String salesRepresentativeId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("salesRepresentativeId").is(salesRepresentativeId)
                .and("status").is(Quota.QuotaStatus.ACTIVE)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public List<Quota> findByTenantIdAndPeriodBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(java.time.ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("startDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Quota.class);
    }

    @Override
    public Optional<Quota> findActiveByTenantIdAndTerritoryIdAndType(String tenantId, String territoryId, Quota.QuotaType type) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
                .and("type").is(type)
                .and("status").is(Quota.QuotaStatus.ACTIVE)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Quota.class));
    }

    @Override
    public boolean existsByQuotaIdAndTenantId(String quotaId, String tenantId) {
        Query query = Query.query(
            Criteria.where("quotaId").is(quotaId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Quota.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Quota.class);
    }

    @Override
    public void deleteByQuotaIdAndTenantId(String quotaId, String tenantId) {
        Query query = Query.query(
            Criteria.where("quotaId").is(quotaId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Quota.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Quota.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Quota.class);
    }

    @Override
    public long countByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
        );
        return mongoTemplate.count(query, Quota.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Quota.QuotaStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Quota.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndTerritoryId(String tenantId, String territoryId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territoryId").is(territoryId)
        );

        List<Quota> quotas = mongoTemplate.find(query, Quota.class);
        return quotas.stream()
            .map(Quota::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndYear(String tenantId, Integer year) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("year").is(year)
        );

        List<Quota> quotas = mongoTemplate.find(query, Quota.class);
        return quotas.stream()
            .map(Quota::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
