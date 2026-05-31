package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongo;

import com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine;
import com.gogidix.finance.bankreconciliation.domain.repository.ReconciliationLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * MongoDB Repository Implementation - Reconciliation Line
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoReconciliationLineRepository implements ReconciliationLineRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ReconciliationLine save(ReconciliationLine line) {
        log.debug("Saving reconciliation line: {} for tenant: {}",
                line.getLineId(), line.getTenantId());
        return mongoTemplate.save(line);
    }

    @Override
    public List<ReconciliationLine> saveAll(List<ReconciliationLine> lines) {
        return lines.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<ReconciliationLine> findById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ReconciliationLine.class));
    }

    @Override
    public Optional<ReconciliationLine> findByLineIdAndTenantId(String lineId, String tenantId) {
        Query query = Query.query(
                Criteria.where("lineId").is(lineId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ReconciliationLine.class));
    }

    @Override
    public List<ReconciliationLine> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndTenantId(String reconciliationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndMatchStatus(
            String reconciliationId, ReconciliationLine.MatchStatus matchStatus) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(matchStatus)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndRequiresManualReview(
            String reconciliationId, Boolean requiresManualReview) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("requiresManualReview").is(requiresManualReview)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndLineType(
            String reconciliationId, ReconciliationLine.LineType lineType) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("lineType").is(lineType)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByBankTransactionId(String bankTransactionId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("bankTransactionId").is(bankTransactionId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByBookTransactionId(String bookTransactionId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("bookTransactionId").is(bookTransactionId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndBankTransactionId(
            String reconciliationId, String bankTransactionId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("bankTransactionId").is(bankTransactionId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByReconciliationIdAndBookTransactionId(
            String reconciliationId, String bookTransactionId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("bookTransactionId").is(bookTransactionId)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findDiscrepanciesByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(ReconciliationLine.MatchStatus.DISCREPANCY)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findUnmatchedByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(ReconciliationLine.MatchStatus.UNMATCHED)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findPendingReviewByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(ReconciliationLine.MatchStatus.PENDING_REVIEW)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByTenantIdAndDiscrepancyCategory(
            String tenantId, ReconciliationLine.DiscrepancyCategory discrepancyCategory) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("discrepancyCategory").is(discrepancyCategory)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public boolean existsByLineIdAndTenantId(String lineId, String tenantId) {
        Query query = Query.query(
                Criteria.where("lineId").is(lineId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ReconciliationLine.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ReconciliationLine.class);
    }

    @Override
    public void deleteByLineIdAndTenantId(String lineId, String tenantId) {
        Query query = Query.query(
                Criteria.where("lineId").is(lineId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ReconciliationLine.class);
    }

    @Override
    public void deleteByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ReconciliationLine.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, ReconciliationLine.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ReconciliationLine.class);
    }

    @Override
    public long countByReconciliationIdAndTenantId(String reconciliationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, ReconciliationLine.class);
    }

    @Override
    public long countByReconciliationIdAndMatchStatus(String reconciliationId,
                                                      ReconciliationLine.MatchStatus matchStatus) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(matchStatus)
        );
        return mongoTemplate.count(query, ReconciliationLine.class);
    }

    @Override
    public long countDiscrepanciesByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("matchStatus").is(ReconciliationLine.MatchStatus.DISCREPANCY)
        );
        return mongoTemplate.count(query, ReconciliationLine.class);
    }

    @Override
    public List<ReconciliationLine> findByBankTransactionDateBetween(
            String tenantId, String accountId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("bankTransactionDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, ReconciliationLine.class);
    }

    @Override
    public void deleteByReconciliationIdAndLineType(String reconciliationId,
                                                     ReconciliationLine.LineType lineType) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
                        .and("lineType").is(lineType)
        );
        mongoTemplate.remove(query, ReconciliationLine.class);
    }
}
