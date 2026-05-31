package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongo;

import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import com.gogidix.finance.bankreconciliation.domain.repository.ReconciliationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Reconciliation
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoReconciliationRepository implements ReconciliationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Reconciliation save(Reconciliation reconciliation) {
        log.debug("Saving reconciliation: {} for tenant: {}",
                reconciliation.getReconciliationId(), reconciliation.getTenantId());
        return mongoTemplate.save(reconciliation);
    }

    @Override
    public List<Reconciliation> saveAll(List<Reconciliation> reconciliations) {
        return reconciliations.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<Reconciliation> findById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Reconciliation.class));
    }

    @Override
    public Optional<Reconciliation> findByReconciliationIdAndTenantId(String reconciliationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Reconciliation.class));
    }

    @Override
    public List<Reconciliation> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        ).with(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndStatementId(String tenantId, String statementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("statementId").is(statementId)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndStatus(String tenantId,
                                                         Reconciliation.ReconciliationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        ).with(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndReconciliationDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reconciliationDate").gte(start).lte(end)
        ).with(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndPeriodStartBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStart").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndAccountIdAndStatus(
            String tenantId, String accountId, Reconciliation.ReconciliationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("status").is(status)
        ).with(Sort.by(Sort.Direction.DESC, "reconciliationDate"));
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findPendingByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Reconciliation.ReconciliationStatus.PENDING)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findInProgressByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Reconciliation.ReconciliationStatus.IN_PROGRESS)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findAwaitingApprovalByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Reconciliation.ReconciliationStatus.AWAITING_APPROVAL)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findCompletedByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of(
                                Reconciliation.ReconciliationStatus.COMPLETED,
                                Reconciliation.ReconciliationStatus.APPROVED))
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public Optional<Reconciliation> findLatestByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        )
                .with(Sort.by(Sort.Direction.DESC, "reconciliationDate"))
                .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, Reconciliation.class));
    }

    @Override
    public boolean existsByReconciliationIdAndTenantId(String reconciliationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Reconciliation.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Reconciliation.class);
    }

    @Override
    public void deleteByReconciliationIdAndTenantId(String reconciliationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Reconciliation.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Reconciliation.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Reconciliation.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Reconciliation.ReconciliationStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, Reconciliation.class);
    }

    @Override
    public long countByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.count(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndReconciliationMethod(
            String tenantId, Reconciliation.ReconciliationMethod method) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reconciliationMethod").is(method)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }

    @Override
    public List<Reconciliation> findByTenantIdAndIsBalanced(String tenantId, Boolean isBalanced) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isBalanced").is(isBalanced)
        );
        return mongoTemplate.find(query, Reconciliation.class);
    }
}
