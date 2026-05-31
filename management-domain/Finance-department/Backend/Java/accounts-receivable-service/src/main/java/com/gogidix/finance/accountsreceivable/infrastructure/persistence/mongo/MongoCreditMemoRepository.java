package com.gogidix.finance.accountsreceivable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountsreceivable.domain.model.CreditMemo;
import com.gogidix.finance.accountsreceivable.domain.repository.CreditMemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - CreditMemo
 * Implements credit memo persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCreditMemoRepository implements CreditMemoRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CreditMemo save(CreditMemo creditMemo) {
        log.debug("Saving credit memo: {} for tenant: {}",
            creditMemo.getCreditMemoId(), creditMemo.getTenantId());
        return mongoTemplate.save(creditMemo);
    }

    @Override
    public List<CreditMemo> saveAll(List<CreditMemo> creditMemos) {
        return creditMemos.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<CreditMemo> findById(String id) {
        String tenantId = com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CreditMemo.class));
    }

    @Override
    public Optional<CreditMemo> findByCreditMemoIdAndTenantId(String creditMemoId, String tenantId) {
        Query query = Query.query(
            Criteria.where("creditMemoId").is(creditMemoId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CreditMemo.class));
    }

    @Override
    public Optional<CreditMemo> findByCreditMemoNumberAndTenantId(String creditMemoNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("creditMemoNumber").is(creditMemoNumber)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CreditMemo.class));
    }

    @Override
    public List<CreditMemo> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndStatus(String tenantId, CreditMemo.CreditMemoStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndCreditMemoType(String tenantId, CreditMemo.CreditMemoType creditMemoType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("creditMemoType").is(creditMemoType)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndReferenceInvoiceId(String tenantId, String referenceInvoiceId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("referenceInvoiceId").is(referenceInvoiceId)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findAvailableCreditMemosByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
                .and("status").in(List.of(CreditMemo.CreditMemoStatus.ISSUED, CreditMemo.CreditMemoStatus.PARTIALLY_APPLIED))
                .and("balanceRemaining").gt(BigDecimal.ZERO)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findExpiringCreditMemosByTenantId(String tenantId, LocalDate expirationDate) {
        Date date = Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("expirationDate").lte(date)
                .and("status").ne(CreditMemo.CreditMemoStatus.FULLY_APPLIED)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndCreditMemoDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("creditMemoDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public List<CreditMemo> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, CreditMemo.class);
    }

    @Override
    public boolean existsByCreditMemoNumberAndTenantId(String creditMemoNumber, String tenantId) {
        Query query = Query.query(
            Criteria.where("creditMemoNumber").is(creditMemoNumber)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CreditMemo.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), CreditMemo.class);
    }

    @Override
    public void deleteByCreditMemoIdAndTenantId(String creditMemoId, String tenantId) {
        Query query = Query.query(
            Criteria.where("creditMemoId").is(creditMemoId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CreditMemo.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CreditMemo.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CreditMemo.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CreditMemo.CreditMemoStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, CreditMemo.class);
    }

    @Override
    public BigDecimal sumBalanceRemainingByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("customerId").is(customerId)
                .and("status").in(List.of(CreditMemo.CreditMemoStatus.ISSUED, CreditMemo.CreditMemoStatus.PARTIALLY_APPLIED))
        );
        List<CreditMemo> creditMemos = mongoTemplate.find(query, CreditMemo.class);
        return creditMemos.stream()
            .map(CreditMemo::getBalanceRemaining)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumTotalAmountByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        List<CreditMemo> creditMemos = mongoTemplate.find(query, CreditMemo.class);
        return creditMemos.stream()
            .map(CreditMemo::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
