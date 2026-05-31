package com.gogidix.finance.bankreconciliation.infrastructure.persistence.mongo;

import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;
import com.gogidix.finance.bankreconciliation.domain.repository.BankTransactionRepository;
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
import java.util.stream.Collectors;

/**
 * MongoDB Repository Implementation - Bank Transaction
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBankTransactionRepository implements BankTransactionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public BankTransaction save(BankTransaction transaction) {
        log.debug("Saving bank transaction: {} for tenant: {}",
                transaction.getTransactionId(), transaction.getTenantId());
        return mongoTemplate.save(transaction);
    }

    @Override
    public List<BankTransaction> saveAll(List<BankTransaction> transactions) {
        return transactions.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<BankTransaction> findById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankTransaction.class));
    }

    @Override
    public Optional<BankTransaction> findByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("transactionId").is(transactionId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankTransaction.class));
    }

    @Override
    public List<BankTransaction> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndAccountNumber(String tenantId, String accountNumber) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountNumber").is(accountNumber)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndStatementId(String tenantId, String statementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("statementId").is(statementId)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndTransactionDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("transactionDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndValueDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("valueDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndTransactionType(
            String tenantId, BankTransaction.TransactionType transactionType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("transactionType").is(transactionType)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndIsReconciled(String tenantId, Boolean isReconciled) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isReconciled").is(isReconciled)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findUnreconciledByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("isReconciled").is(false)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndAccountIdAndTransactionDateBetween(
            String tenantId, String accountId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
                        .and("transactionDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndAmountBetween(
            String tenantId, BigDecimal minAmount, BigDecimal maxAmount) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("amount").gte(minAmount).lte(maxAmount)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndReferenceContaining(String tenantId, String reference) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reference").regex(reference, "i")
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndDescriptionContaining(
            String tenantId, String description) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("description").regex(description, "i")
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndCounterpartyName(
            String tenantId, String counterpartyName) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("counterpartyName").regex(counterpartyName, "i")
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByReconciliationId(String reconciliationId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationId").is(reconciliationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByReconciliationLineId(String reconciliationLineId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("reconciliationLineId").is(reconciliationLineId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public Optional<BankTransaction> findByTenantIdAndCheckNumber(
            String tenantId, String checkNumber) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("checkNumber").is(checkNumber)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BankTransaction.class));
    }

    @Override
    public List<BankTransaction> findByTenantIdAndStatus(
            String tenantId, BankTransaction.TransactionStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public boolean existsByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("transactionId").is(transactionId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, BankTransaction.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankTransaction.class);
    }

    @Override
    public void deleteByTransactionIdAndTenantId(String transactionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("transactionId").is(transactionId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankTransaction.class);
    }

    @Override
    public void deleteByStatementId(String statementId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("statementId").is(statementId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BankTransaction.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, BankTransaction.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, BankTransaction.class);
    }

    @Override
    public long countByTenantIdAndAccountId(String tenantId, String accountId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("accountId").is(accountId)
        );
        return mongoTemplate.count(query, BankTransaction.class);
    }

    @Override
    public long countByTenantIdAndStatementId(String tenantId, String statementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("statementId").is(statementId)
        );
        return mongoTemplate.count(query, BankTransaction.class);
    }

    @Override
    public long countByTenantIdAndIsReconciled(String tenantId, Boolean isReconciled) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isReconciled").is(isReconciled)
        );
        return mongoTemplate.count(query, BankTransaction.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndTransactionTypeAndTransactionDateBetween(
            String tenantId, BankTransaction.TransactionType transactionType,
            LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("transactionType").is(transactionType)
                        .and("transactionDate").gte(start).lte(end)
        );

        List<BankTransaction> transactions = mongoTemplate.find(query, BankTransaction.class);
        return transactions.stream()
                .map(BankTransaction::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<BankTransaction> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }

    @Override
    public List<BankTransaction> findByOriginalTransactionId(String originalTransactionId) {
        String tenantId = com.gogidix.finance.bankreconciliation.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("originalTransactionId").is(originalTransactionId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, BankTransaction.class);
    }
}
